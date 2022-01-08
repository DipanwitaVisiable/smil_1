package com.example.crypedu.Adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.activity.smi.R;
import com.example.crypedu.Pojo.ExaminationInfo;

import java.util.List;

/**
 * Created by hp on 5/22/2017.
 */
public class ExaminationDirectAdapter extends BaseAdapter {
    private final Context context;
    private final List<ExaminationInfo> examInfoArrayList;

    public ExaminationDirectAdapter(Context context, List<ExaminationInfo> examInfoArrayList){
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        @SuppressLint("ViewHolder")
        View view = inflater.inflate(R.layout.examination_direct_adapter, parent, false);
        TextView subjectTextView = view.findViewById(R.id.subjectTextView);
        TextView examNameTextView = view.findViewById(R.id.examNameTextView);
        TextView timeTextView = view.findViewById(R.id.timeTextView);
        TextView dateTextView = view.findViewById(R.id.dateTextView);
        ExaminationInfo examinationInfo = examInfoArrayList.get(position);
        subjectTextView.setText(examinationInfo.subject);
        examNameTextView.setText(examinationInfo.exam_name);
        timeTextView.setText(examinationInfo.time);
        dateTextView.setText(examinationInfo.examDate);
        return view;
    }
}

