package com.example.crypedu.Adapter;
/*
 *************************************************************************************************
 * Developed by Cryptonsoftech
 * Date: 30-05-2017
 * Details:
 * ***********************************************************************************************
 */
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import com.activity.smi.R;
import java.util.List;

/**
 * Created by chandan on 4/18/2017.
 */
public class AdvertisementAdapter extends BaseAdapter {

    private final Context context;
    private final List<String> stringList;

    public AdvertisementAdapter(Context context, List<String> stringList, LayoutInflater layoutInflater){
        this.context = context;
        this.stringList = stringList;
    }

    @Override
    public int getCount() {
        return stringList.size();
    }

    @Override
    public Object getItem(int position) {
        return stringList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        @SuppressLint("ViewHolder")
        View itemView = inflater.inflate(R.layout.advertisement_adapter, parent, false);
        ImageView advertisement_imageView = itemView.findViewById(R.id.advertisement_imageView);
        return itemView;
    }
}
