package com.example.crypedu.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.activity.smi.R;
import com.example.crypedu.Pojo.BusInfo;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CustomBusAdapter extends BaseAdapter {

    private Context context;
    private List<BusInfo> busInfos;

    public CustomBusAdapter(Context context, List<BusInfo> busInfos){
        this.context = context;
        this.busInfos = busInfos;
    }
    @Override
    public int getCount() {
        return busInfos.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView textView;
        ImageView imageView;
        if (convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            assert inflater != null;
            convertView = inflater.inflate(R.layout.custom_direct_bus_view, parent, false);
        }

        textView = convertView.findViewById(R.id.busName);
        imageView = convertView.findViewById(R.id.busIndicate);
        BusInfo busInfo = busInfos.get(position);
        textView.setText(busInfo.busNo);
        if (busInfo.busStatus.equalsIgnoreCase("0")){
            Picasso.with(context)
                    .load(R.drawable.red)
                    .fit()
                    .into(imageView);
        }
        if (position % 2 == 0){
            convertView.setBackgroundColor(Color.parseColor("#FFB9B6B6"));
        }
        return convertView;
    }
}
