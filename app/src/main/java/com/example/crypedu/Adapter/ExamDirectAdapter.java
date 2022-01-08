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
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.activity.smi.R;
import com.example.crypedu.Pojo.ExamInfo;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by hp on 5/22/2017.
 */
public class ExamDirectAdapter extends BaseAdapter {
    private final Context context;
    private final ArrayList<ExamInfo> examInfoArrayList;

    public ExamDirectAdapter(Context context, ArrayList<ExamInfo> examInfoArrayList, LayoutInflater layoutInflater){
        this.context = context;
        this.examInfoArrayList = examInfoArrayList;
    }
    @Override
    public int getCount() {
        return examInfoArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return examInfoArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("ResourceType")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        @SuppressLint("ViewHolder")
        View view = inflater.inflate(R.layout.exam_direct_adapter, parent, false);
        TextView textView = view.findViewById(R.id.textView);
        Random rnd = new Random();
        int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        ExamInfo examInfo = examInfoArrayList.get(position);
        textView.setText(examInfo.name);
        textView.setTextColor(Color.WHITE);
        view.setBackgroundColor(color);
        return view;
    }
}
