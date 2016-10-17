package com.example.tomotaro.smartalarm;

import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class StopAlarm extends AppCompatActivity {
    SoundPool mSoundPool;
    int mSoundResId;
    TextView mTitle;
    TextView mDescription;
    TextView mDateLabel0, mDateLabel1;
    TextView mTelop0, mTelop1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stop_alarm);

        mDescription = (TextView) findViewById(R.id.description);
        mTitle = (TextView) findViewById(R.id.title);
        mDateLabel0 = (TextView) findViewById(R.id.dateLabel0);
        mTelop0 = (TextView) findViewById(R.id.telop0);
        mDateLabel1 = (TextView) findViewById(R.id.dateLabel1);
        mTelop1 = (TextView) findViewById(R.id.telop1);

//ここから天気取得と挿入
        String id = "130010";
        String url = "http://weather.livedoor.com/forecast/webservice/json/v1?city=" + id;

        JsonObjectRequest jsonObjRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            mTitle.setText(response.getString("title"));
                            mDescription.setText(response
                                    .getJSONObject("description")
                                    .getString("text"));
                            mDateLabel0.setText(response
                                    .getJSONArray("forecasts")
                                    .getJSONObject(0)
                                    .getString("dateLabel"));
                            mTelop0.setText(response
                                    .getJSONArray("forecasts")
                                    .getJSONObject(0)
                                    .getString("telop"));
                            mDateLabel1.setText(response
                                    .getJSONArray("forecasts")
                                    .getJSONObject(1)
                                    .getString("dateLabel"));
                            mTelop1.setText(response
                                    .getJSONArray("forecasts")
                                    .getJSONObject(1)
                                    .getString("telop"));

                            Log.d("Weather", response.toString(2));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("StopError", error.toString());
                    }
                }
        );

        SmartAlarmSingleton.getInstance(this).addToRequestQueue(jsonObjRequest);

//ここまで天気

        Toast.makeText(getApplicationContext(), "ALARMここまで１ ", Toast.LENGTH_SHORT).show();
        mSoundPool = new SoundPool(2, AudioManager.STREAM_ALARM, 0);
        mSoundResId = mSoundPool.load(this, R.raw.bellsound, 1);
        mSoundPool.play(mSoundResId, 1.0f, 1.0f, 0, 0, 1.0f);
        Toast.makeText(getApplicationContext(), "ALARMここまで２ ", Toast.LENGTH_SHORT).show();
    }

    public void onClickStop(View view) {
        mSoundPool.release();
        Toast.makeText(getApplicationContext(), "ALARMを解除しました。", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

        Toast.makeText(getApplicationContext(), "次のアラームを設定します。", Toast.LENGTH_SHORT).show();


    }
}
