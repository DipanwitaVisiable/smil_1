package com.example.crypedu.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.activity.smi.R;
import com.example.crypedu.Pojo.NotificationInfo;
import java.util.List;

/**
 * Created by hp on 6/21/2017.
 */
public class NotificationAdapter extends BaseAdapter {
    private final Context context;
    private final List<NotificationInfo> notificationInfoList;

    public NotificationAdapter(Context context, List<NotificationInfo> notificationInfoList, LayoutInflater layoutInflater){
        this.context = context;
        this.notificationInfoList = notificationInfoList;
    }
    @Override
    public int getCount() {
        return notificationInfoList.size();
    }

    @Override
    public Object getItem(int position) {
        return notificationInfoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View itemView;
        assert inflater != null;
        itemView = inflater.inflate(R.layout.notification_adapter, parent, false);
        TextView nameTextView = itemView.findViewById(R.id.nameTextView);
        TextView countTextView = itemView.findViewById(R.id.countTextView);
        NotificationInfo notificationInfo = notificationInfoList.get(position);
        nameTextView.setText(notificationInfo.name);
        countTextView.setText(notificationInfo.totalNo);
        return itemView;
    }
}
