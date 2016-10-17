package com.example.tomotaro.smartalarm;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Calendar;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import io.realm.Sort;

public class MainActivity extends AppCompatActivity {
    private static final int bid2 = 2;
    ListView mListView;
    private Button button3;
    String currentWeather;

    //メニュー
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.firstButton) {
            Toast.makeText(this, "へるぷ", Toast.LENGTH_LONG).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mListView = (ListView) findViewById(R.id.list);
        RealmConfiguration realmConfig = new RealmConfiguration.Builder(getApplicationContext())
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfig);
        Realm r = Realm.getDefaultInstance();
        final RealmResults<AlarmList> alarmLists = r.where(AlarmList.class).findAllSorted("hour", Sort.ASCENDING, "minute", Sort.ASCENDING);
        AlarmListAdapter adapter = new AlarmListAdapter(this, alarmLists);
        mListView.setAdapter(adapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AlarmList alarmList = (AlarmList) parent.getItemAtPosition(position);
                startActivity(new Intent(MainActivity.this, AlarmEdit.class).putExtra("alarmid", alarmList.getId()));
            }
        });

        button3 = (Button) this.findViewById(R.id.button3);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //時刻設定画面へ移る
                Intent intent1 = new Intent(getApplicationContext(), PickingTime.class);
                startActivity(intent1);
            }
        });


        alarmLists.addChangeListener(new RealmChangeListener<RealmResults<AlarmList>>() {
            @Override
            public void onChange(RealmResults<AlarmList>alarmListRealmResults) {
                //  Toast.makeText(getApplicationContext(),"Realm更新",Toast.LENGTH_SHORT).show();
                Calendar calendar = Calendar.getInstance();
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);
                int dif = 0;

                AlarmList next = alarmLists.where().equalTo("on", true).greaterThan("hour", hour - 1).greaterThan("minute", minute).findFirst();
             //   if (next.isCasting() != true){
                    if (next == null) {
                        Toast.makeText(getApplicationContext(), "next == null", Toast.LENGTH_SHORT).show();
                        next = alarmLists.where().equalTo("on", true).findFirst();
                    }//next確定

                    if (next != null) {
                        if (next.isFirstAlarm()) {
                            Toast.makeText(getApplicationContext(), "firstなので1分早くアラームが鳴ります", Toast.LENGTH_SHORT).show();
                            dif = 1; //ここは設定できるようにしたい
                        } else {
                            Toast.makeText(getApplicationContext(), "firstではありません", Toast.LENGTH_SHORT).show();
                        }
                        Toast.makeText(getApplicationContext(), String.valueOf(next.getHour()) + String.valueOf(next.getMinute() - dif), Toast.LENGTH_SHORT).show();
                        next.setAlarm(next.getHour(), next.getMinute() - dif, getApplicationContext());

                    } else { //nextが見つからない場合
                        Toast.makeText(getApplicationContext(), "アラームはすべてoffです", Toast.LENGTH_SHORT).show();
                    }
               // }
                // クエリの結果は自動的に更新されます
            }
        });
    }

    private void close(){
        finish();
    }
}