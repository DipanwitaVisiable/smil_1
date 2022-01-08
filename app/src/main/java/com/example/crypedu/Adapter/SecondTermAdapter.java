package com.example.crypedu.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.activity.smi.R;
import com.example.crypedu.Pojo.SecondTermInfo;

import java.util.ArrayList;

/**
 * Created by INDID on 09-01-2018.
 */

public class SecondTermAdapter extends BaseAdapter {
    private final Context context;
    private final ArrayList<SecondTermInfo> secondTermInfoArrayList;

    public SecondTermAdapter(Context context, ArrayList<SecondTermInfo> secondTermInfoArrayList, LayoutInflater layoutInflater) {
        this.context = context;
        this.secondTermInfoArrayList = secondTermInfoArrayList;
    }

    @Override
    public int getCount() {
        return secondTermInfoArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return secondTermInfoArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        @SuppressLint("ViewHolder") View view = inflater.inflate(R.layout.second_term_adapter, parent, false);
        TextView subject2_TextView = view.findViewById(R.id.subject2_TextView);
        TextView periodic2_TextView = view.findViewById(R.id.periodic2_TextView);
        TextView note_book2_TextView = view.findViewById(R.id.note_book2_TextView);
        TextView sub_enrichment2_TextView = view.findViewById(R.id.sub_enrichment2_TextView);
        TextView yearly_TextView = view.findViewById(R.id.yearly_TextView);
        TextView marks_obtained2_TextView = view.findViewById(R.id.marks_obtained2_TextView);
        TextView grade2_TextView = view.findViewById(R.id.grade2_TextView);

        SecondTermInfo secondTermInfo = secondTermInfoArrayList.get(position);
        subject2_TextView.setText(secondTermInfo.sd_subject);
        periodic2_TextView.setText(secondTermInfo.sd_periodic);
        note_book2_TextView.setText(secondTermInfo.sd_note_book);
        sub_enrichment2_TextView.setText(secondTermInfo.sd_sub_enrich);
        yearly_TextView.setText(secondTermInfo.sd_yearly);
        marks_obtained2_TextView.setText(secondTermInfo.sd_marks_obtain);
        grade2_TextView.setText(secondTermInfo.sd_grade);
//        Typeface typeface = Typeface.createFromAsset(convertView.getContext().getAssets(), Constants.BubblegumSans_Regular_font);
//        subject_TextView.setTypeface(typeface);
//        fa_TextView.setTypeface(typeface);
//        sa_TextView.setTypeface(typeface);
//        total_TextView.setTypeface(typeface);
        return view;
    }
}
