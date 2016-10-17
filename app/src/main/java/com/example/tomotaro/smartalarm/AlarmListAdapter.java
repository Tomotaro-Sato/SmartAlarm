package com.example.tomotaro.smartalarm;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import io.realm.OrderedRealmCollection;
import io.realm.RealmBaseAdapter;

/**
 * Created by tomotaro on 2016/10/10.
 */

public class AlarmListAdapter extends RealmBaseAdapter<AlarmList> {
    private static class ViewHolder {
        TextView time;
    }

    public AlarmListAdapter(Context context, OrderedRealmCollection<AlarmList> realmResults) {
        super(context, realmResults);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.time = (TextView) convertView.findViewById(android.R.id.text1);
         //   viewHolder.minute = (TextView)convertView.findViewById(android.R
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        AlarmList alarmList = adapterData.get(position);
        viewHolder.time.setText(String.format("%02d",alarmList.getHour())+":"+String.format("%02d",alarmList.getMinute()));
        //viewHolder.minute.setText(alarmList.getMinute());
        return convertView;
    }
}
