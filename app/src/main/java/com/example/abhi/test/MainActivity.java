package com.example.abhi.test;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.android.volley.Response;
import com.android.volley.VolleyError;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, Response.Listener<String>, Response.ErrorListener {

    private final String TAG = this.getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.button_on).setOnClickListener(this);
        findViewById(R.id.button_off).setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_on:
                executeRequest("1");
                break;
            case R.id.button_off:
                executeRequest("0");
                break;
        }
    }

    private void executeRequest(String status) {
        VolleyRequest<String> volleyRequest = new VolleyRequest<String>(VolleyRequest.Method.GET,
                "https://iot-android.000webhostapp.com/?status="+status,
                String.class,
                null, null, null, this, this);
        VolleyNetwork.getInstance(this).addToRequestQueue(volleyRequest);
    }

    @Override
    public void onResponse(String response) {
        Log.i(TAG, response);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Log.e(TAG, error.getMessage());
    }
}
