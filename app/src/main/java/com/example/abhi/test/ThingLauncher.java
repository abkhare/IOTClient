package com.example.abhi.test;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.things.pio.Gpio;
import com.google.android.things.pio.PeripheralManagerService;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class ThingLauncher extends AppCompatActivity implements Response.Listener<Data>, Response.ErrorListener {

    private final String TAG = this.getClass().getSimpleName();
    private Gpio gpio;
    private Timer timer;
    private TimerTask timerTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thing_launcher);

        timerTask = new TimerTask() {
            @Override
            public void run() {

                 VolleyRequest<Data> volleyRequest = new VolleyRequest<Data>(VolleyRequest.Method.GET,
                        "https://iot-android.000webhostapp.com/response.txt",
                        Data.class,
                        null, null, null, ThingLauncher.this, ThingLauncher.this);
                VolleyNetwork.getInstance(ThingLauncher.this).addToRequestQueue(volleyRequest);
            }
        };


        timer = new Timer();
        timer.schedule(timerTask, 1000, 1000);




        PeripheralManagerService service = new PeripheralManagerService();
        Log.e(TAG, "Available GPIO: " + service.getGpioList());
        Log.e(TAG, "Available UART: " + service.getUartDeviceList());

        try {
            gpio = service.openGpio("BCM4");
            gpio.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);
            gpio.setActiveType(Gpio.ACTIVE_HIGH);
        }
        catch (IOException e) {
            Log.e(TAG, "Unable to access this GPIO :"+e.getMessage());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(gpio != null) {
            try {
                gpio.close();
            }
            catch(IOException e) {
                Log.e(TAG, "Unable to close this GPIO :"+e.getMessage());
            }
            gpio = null;
        }
        timer.cancel();
    }

    @Override
    public void onResponse(Data response) {
        Log.i(TAG, response.getStatus());
        try {
            if (response.getStatus().equals("1")) {
                gpio.setValue(true);
            } else {
                gpio.setValue(false);
            }
        }
        catch(IOException e) {
            Log.e(TAG, "Unable to set this GPIO :"+e.getMessage());
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Log.e(TAG, error.getMessage());
    }
}
