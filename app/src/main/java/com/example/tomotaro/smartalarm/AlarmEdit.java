package com.example.tomotaro.smartalarm;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class AlarmEdit extends AppCompatActivity {
    private static final String TAG = "RadioButtonTest";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_edit);

        long alarmId = getIntent().getLongExtra("alarmid", -1);
        RealmConfiguration realmConfig = new RealmConfiguration.Builder(getApplicationContext())
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfig);
        Realm r = Realm.getDefaultInstance();
        final AlarmList alarmList = r.where(AlarmList.class).equalTo("id", alarmId).findFirst();
        TextView textTime = (TextView)findViewById(R.id.time);
        textTime.setText(String.format("%02d",alarmList.getHour())+":"+String.format("%02d",alarmList.getMinute()));
        if(alarmList.getDetail() != null){
            EditText editDetail = (EditText)findViewById(R.id.editDetail);
            editDetail.setText(alarmList.getDetail());
        }
        if(alarmList.isOn()){
            RadioButton on = (RadioButton)findViewById(R.id.on);
            on.setChecked(true);
        }else{
            RadioButton off = (RadioButton)findViewById(R.id.off);
            off.setChecked(true);
        }

        r.beginTransaction();
        //変更などの処理
        r.commitTransaction();
        r.close();

        Button delete = (Button)findViewById(R.id.buttonDelete);
        delete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                long alarmId = getIntent().getLongExtra("alarmid", -1);
                RealmConfiguration realmConfig = new RealmConfiguration.Builder(getApplicationContext())
                        .deleteRealmIfMigrationNeeded()
                        .build();
                Realm.setDefaultConfiguration(realmConfig);
                Realm r = Realm.getDefaultInstance();
                RealmResults<AlarmList> results = r.where(AlarmList.class).equalTo("id", alarmId).findAll();
                r.beginTransaction();
                results.deleteFirstFromRealm();
                r.commitTransaction();
                r.close();
                finish();
            }
        });

        Button save = (Button)findViewById(R.id.buttonSave);
        save.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Toast.makeText(getApplicationContext(),"onSaveTapped",Toast.LENGTH_SHORT).show();

                long alarmId = getIntent().getLongExtra("alarmid", -1);
                RealmConfiguration realmConfig = new RealmConfiguration.Builder(getApplicationContext())
                        .deleteRealmIfMigrationNeeded()
                        .build();
                Realm.setDefaultConfiguration(realmConfig);
                Realm r = Realm.getDefaultInstance();
                AlarmList alarmList = r.where(AlarmList.class).equalTo("id", alarmId).findFirst();
                EditText editDetail = (EditText)findViewById(R.id.editDetail);
                RadioButton on = (RadioButton)findViewById(R.id.on);
                r.beginTransaction();
                alarmList.setDetail(editDetail.getText().toString());
                r.commitTransaction();
                r.close();

                finish();
            }
        });
    }

    public void onClick(View view) {
        boolean checked = ((RadioButton)view).isChecked();
        long alarmId = getIntent().getLongExtra("alarmid", -1);

        RealmConfiguration realmConfig = new RealmConfiguration.Builder(getApplicationContext())
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfig);
        Realm r = Realm.getDefaultInstance();
        AlarmList alarmList = r.where(AlarmList.class).equalTo("id", alarmId).findFirst();
        r.beginTransaction();

        switch (view.getId()) {
            case R.id.on:
                if (checked) Log.d(TAG, "on is checked");
                else Log.d(TAG, "on is unchecked");
                alarmList.setOn(true);
                r.commitTransaction();
                Log.d(TAG, String.valueOf(alarmList.isOn()));
                r.close();
                break;

            case R.id.off:
                if (checked) Log.d(TAG, "off is checked");
                else Log.d(TAG, "off is unchecked");
                alarmList.setOn(false);
                r.commitTransaction();
                Log.d(TAG, String.valueOf(alarmList.isOn()));
                r.close();
                break;

        }
    }
}
