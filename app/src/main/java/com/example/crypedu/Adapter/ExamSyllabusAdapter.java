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
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.activity.smi.R;
import com.example.crypedu.Constants.Constants;
import com.example.crypedu.Pojo.ExamSyllabusInfo;

import java.util.List;

public class ExamSyllabusAdapter extends BaseAdapter {
    private final Context context;
    private final List<ExamSyllabusInfo> examSyllabusInfoList;
    LayoutInflater mInflater;
    LayoutInflater inflater;

    public ExamSyllabusAdapter(Context context, List<ExamSyllabusInfo> examSyllabusInfoList, LayoutInflater inflater) {
        mInflater = inflater;
        this.context = context;
        this.examSyllabusInfoList = examSyllabusInfoList;
    }

    @Override
    public int getCount() {
        return examSyllabusInfoList.size();
    }

    @Override
    public Object getItem(int position) {
        return examSyllabusInfoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint({"ViewHolder", "SetTextI18n"})
    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View itemView;
        assert inflater != null;
        itemView = inflater.inflate(R.layout.exam_syllabus_adapter, parent, false);
        TextView chapterTextView = itemView.findViewById(R.id.chapterTextView);
        TextView noOfClassTextView = itemView.findViewById(R.id.noOfClassTextView);

        Typeface typeface = Typeface.createFromAsset(itemView.getContext().getAssets(), Constants.BubblegumSans_Regular_font);

        final ExamSyllabusInfo examSyllabusInfo = examSyllabusInfoList.get(position);
        chapterTextView.setText("Chapter name: " + examSyllabusInfo.chapter);
        noOfClassTextView.setText("No of classes: " + examSyllabusInfo.no_of_class);
        chapterTextView.setTypeface(typeface);
        noOfClassTextView.setTypeface(typeface);

        return itemView;
    }
}

