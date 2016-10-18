package com.example.tomotaro.smartalarm;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TimePicker;

import java.util.Calendar;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class PickingTime extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picking_time);


        // 現在時刻を取得
        Calendar calendar = Calendar.getInstance();
        int hour   = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        // 時間選択ダイアログの生成
        TimePickerDialog timepick= new TimePickerDialog(
                this,
                new TimePickerDialog.OnTimeSetListener() {
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        Log.d("AAA","Created");

                        RealmConfiguration realmConfig = new RealmConfiguration.Builder(getApplicationContext())
                                .deleteRealmIfMigrationNeeded()
                                .build();
                        Realm.setDefaultConfiguration(realmConfig);
                        Realm r = Realm.getDefaultInstance();

                        RealmResults<AlarmList> alarmlists = r.where(AlarmList.class).findAll();
                        Number maxId = r.where(AlarmList.class).max("id");
                        long nextId = 0;
                        if(maxId != null){nextId = maxId.longValue() +1;}
                        AlarmList a = new AlarmList();
                        a.setId(nextId);
                        a.setHour(hourOfDay);
                        a.setMinute(minute);
                        a.setOn(true);
                        // トランザクション開始
                        r.beginTransaction();
                        r.copyToRealm(a);
                        r.commitTransaction();
                        // トランザクション終了

                        r.close();
                        finish();
                        // 設定 ボタンクリック時の処理
                    }
                },
                hour,
                minute,
                true);

        // 表示
        timepick.show();


    }
}


