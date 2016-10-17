package com.example.tomotaro.smartalarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import java.util.Calendar;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

import static android.content.Context.ALARM_SERVICE;

public class AlarmList extends RealmObject  {
    private int hour;
    private int minute;
    private boolean on;
    private static boolean casting = false;
    private boolean firstAlarm = true;
    private String detail;
    @PrimaryKey
    private long id;


    public String getDetail() {
        return detail;
    }

    public static boolean isCasting() {
        return casting;
    }

    public static void setCasting(boolean casting) {
        AlarmList.casting = casting;
    }

    public void setDetail(String detail) {this.detail = detail;}

    public boolean isOn() {
        return on;
    }

    public void setOn(boolean on) {
        this.on = on;
    }

    public boolean isFirstAlarm() {return firstAlarm;}

    public void setFirstAlarm(boolean firstAlarm) {this.firstAlarm = firstAlarm;}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }


    public void setAlarm(int hour, int minute, Context context){
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTimeInMillis(System.currentTimeMillis());
        // 設定した時刻をカレンダーに設定
        calendar2.set(Calendar.HOUR_OF_DAY, hour);
        calendar2.set(Calendar.MINUTE, minute);
        calendar2.set(Calendar.SECOND, 0);
        calendar2.set(Calendar.MILLISECOND, 0);

        // 過去だったら明日にする
        if(calendar2.getTimeInMillis() < System.currentTimeMillis()){
            calendar2.add(Calendar.DAY_OF_YEAR, 1);
        }
        Toast.makeText(context, String.format("%02d時%02d分にアラームがなります.", hour, minute), Toast.LENGTH_LONG).show();
        Intent intent1 = new Intent(context, AlarmBroadcastReceiver.class);
        intent1.putExtra("first",this.isFirstAlarm());
  //      intent1.putExtra("id", this.getId());
        PendingIntent pending = PendingIntent.getBroadcast(context, 0, intent1, 0);
        // アラームをセットする
        AlarmManager am = (AlarmManager)context.getSystemService(ALARM_SERVICE);
        am.setExact(AlarmManager.RTC_WAKEUP, calendar2.getTimeInMillis(), pending);
       // Toast.makeText(context, String.format("%02d:%02dにアラームを設定しました。",hour,minute), Toast.LENGTH_LONG).show();


        //RealmConfiguration realmConfig = new RealmConfiguration.Builder(context)
             //   .deleteRealmIfMigrationNeeded()
               // .build();
        //Realm.setDefaultConfiguration(realmConfig);
        //Realm r = Realm.getDefaultInstance();
        //  AlarmList alarmList = r.where(AlarmList.class).equalTo("id",id).findFirst();

        //alarmListのフィールドを参照しようとすると終了する

       // setCasting(true);
        //r.beginTransaction();
        //if (this.isFirstAlarm()) {
         //   Toast.makeText(context, "このアラームはsecondになる", Toast.LENGTH_SHORT).show();
        //    this.setFirstAlarm(false);
        //} else {
       //     Toast.makeText(context, "このアラームはfirstになる", Toast.LENGTH_SHORT).show();
        //    this.setFirstAlarm(true);
        //}
        //r.commitTransaction();
        //r.close();
    }
}
