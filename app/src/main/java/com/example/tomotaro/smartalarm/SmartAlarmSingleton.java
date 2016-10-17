package com.example.tomotaro.smartalarm;


import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by tomotaro on 2016/09/18.
 */
public class SmartAlarmSingleton {
    private static SmartAlarmSingleton ourInstance;
    private RequestQueue mRequestQueue;
    private static Context mCtx;

    public static synchronized SmartAlarmSingleton getInstance(Context context)
    {
        if(ourInstance == null){
            ourInstance = new SmartAlarmSingleton(context);
        }
        return ourInstance;
    }

    private SmartAlarmSingleton(Context context) {
        mCtx = context;
        mRequestQueue = getRequestQueue();
    }
    public RequestQueue getRequestQueue(){
        if(mRequestQueue == null){
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return mRequestQueue;
    }
    public <T> void addToRequestQueue(Request<T>req){
        getRequestQueue().add(req);
    }
}
