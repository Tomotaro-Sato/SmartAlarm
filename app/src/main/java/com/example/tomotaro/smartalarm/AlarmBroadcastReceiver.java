package com.example.tomotaro.smartalarm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;


public class AlarmBroadcastReceiver extends BroadcastReceiver {
    Context context;
    String currentWeather;

    @Override   // データを受信した
    public void onReceive(final Context context, final Intent intent) {
        this.context = context;
        Log.d("AlarmBroadcastReceiver", "onReceive() pid=" + android.os.Process.myPid());

        String id = "130010";
        String url = "http://weather.livedoor.com/forecast/webservice/json/v1?city=" + id;

        JsonObjectRequest jsonObjRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Toast.makeText(context, "onResponse", Toast.LENGTH_SHORT).show();
                        try {
                            currentWeather = response.getJSONArray("forecasts").getJSONObject(0).getString("telop");
                            Log.d("Weather", response.toString(2));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Toast.makeText(context, currentWeather, Toast.LENGTH_SHORT).show();
                            //firstかどうか　雨かどうか
                        Boolean first = intent.getBooleanExtra("first",false);
                        Boolean cast;
                        Intent intent1;

                        currentWeather = "雨";

                        if(currentWeather.matches(".*雨.*")) {
                            Toast.makeText(context, "雨", Toast.LENGTH_SHORT).show();
                            cast = true;
                        }else {
                            Toast.makeText(context, "雨じゃない", Toast.LENGTH_SHORT).show();
                            cast = false;
                        }


                        if(cast){
                            Intent intent2 = new Intent(context, StopAlarm.class);
                            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent2, PendingIntent.FLAG_UPDATE_CURRENT);
                            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                            Notification notification = new NotificationCompat.Builder(context)
                                    .setSmallIcon(R.mipmap.ic_launcher)
                                    .setTicker("時間です")
                                    .setWhen(System.currentTimeMillis())
                                    .setContentTitle("TestAlarm ")
                                    .setContentText("時間になりました")
                                    // 音、バイブレート、LEDで通知
                                    .setDefaults(Notification.DEFAULT_ALL)
                                    // 通知をタップした時にStopAlarmを立ち上げる
                                    .setContentIntent(pendingIntent)
                                    .build();
                            // 古い通知を削除
                            notificationManager.cancelAll();
                            // 通知
                            notificationManager.notify(R.string.app_name, notification);
                            intent1 = new Intent(context, StopAlarm.class);
                            intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        }else{
                            intent1 = new Intent(context, MainActivity.class);
                            intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            Toast.makeText(context, "cast == false", Toast.LENGTH_SHORT).show();
                        }
                        context.startActivity(intent1);

                        //アラームのfirst切り替え
  /*                      long id = intent.getLongExtra("id",-1);
                        Calendar calendar = Calendar.getInstance();
                        int hour = calendar.get(Calendar.HOUR_OF_DAY);
                        int minute = calendar.get(Calendar.MINUTE);
                        RealmConfiguration realmConfig = new RealmConfiguration.Builder(context)
                                .deleteRealmIfMigrationNeeded()
                                .build();
                        Realm.setDefaultConfiguration(realmConfig);
                        Realm r = Realm.getDefaultInstance();
                      //  AlarmList alarmList = r.where(AlarmList.class).equalTo("id",id).findFirst();

                        //alarmListのフィールドを参照しようとすると終了する

                   //     Toast.makeText(context, String.format("%02d:%02d",alarmList.getHour(),alarmList.getMinute()), Toast.LENGTH_SHORT).show();
                        r.beginTransaction();
                    //    if (alarmList.isFirstAlarm()) {
                           Toast.makeText(context, "このアラームはsecondになる", Toast.LENGTH_SHORT).show();
                            //alarmList.setFirstAlarm(false);
                    //    } else {
                            Toast.makeText(context, "このアラームはfirstになる", Toast.LENGTH_SHORT).show();
                            //alarmList.setFirstAlarm(true);
                    //    }
                        r.commitTransaction();
                        r.close();
*/

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("StopError", error.toString());
                    }
                }
        );

        SmartAlarmSingleton.getInstance(context).addToRequestQueue(jsonObjRequest);

    }
}