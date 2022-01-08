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
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.activity.smi.R;
import com.example.crypedu.Constants.Constants;
import com.example.crypedu.Pojo.TimeInfo;

import java.util.ArrayList;
import java.util.List;

public class TimeTableAdapter extends BaseAdapter {
    private final Context context;
    private final List<TimeInfo> timeTaInfoList;
    LayoutInflater mInflater;
    LayoutInflater inflater;
    private LinearLayout linearLayout;
    private ArrayList<TimeInfo> timeList;

    public TimeTableAdapter(Context context, List<TimeInfo> timeTaInfoList, LayoutInflater inflater) {
        mInflater = inflater;
        this.context = context;
        this.timeTaInfoList = timeTaInfoList;
    }

    @Override
    public int getCount() {
        return timeTaInfoList.size();
    }

    @Override
    public Object getItem(int position) {
        return timeTaInfoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View itemView;
        assert inflater != null;
        itemView = inflater.inflate(R.layout.time_table_adapter, parent, false);
        TextView textView_time = itemView.findViewById(R.id.textView_time);
        TextView textView_monday_sub = itemView.findViewById(R.id.textView_monday_sub);
        TextView textView_tuesday_sub = itemView.findViewById(R.id.textView_tuesday_sub);
        TextView textView_wednesday_sub = itemView.findViewById(R.id.textView_wednesday_sub);
        TextView textView_thursday_sub = itemView.findViewById(R.id.textView_thursday_sub);
        TextView textView_friday_sub = itemView.findViewById(R.id.textView_friday_sub);
        TextView textView_saturday_sub = itemView.findViewById(R.id.textView_saturday_sub);

        Typeface typeface = Typeface.createFromAsset(itemView.getContext().getAssets(), Constants.BubblegumSans_Regular_font);
        final TimeInfo tTableInfo = timeTaInfoList.get(position);
//        textView_monday_sub.setText(tTableInfo.subject1+"\n"+tTableInfo.time);
//        textView_tuesday_sub.setText(tTableInfo.subject2+"\n"+tTableInfo.time);
//        textView_wednesday_sub.setText(tTableInfo.subject3+"\n"+tTableInfo.time);
//        textView_thursday_sub.setText(tTableInfo.subject4+"\n"+tTableInfo.time);
//        textView_friday_sub.setText(tTableInfo.subject5+"\n"+tTableInfo.time);
//        textView_saturday_sub.setText(tTableInfo.subject6+"\n"+tTableInfo.time);

        textView_monday_sub.setText(tTableInfo.subject1);
        textView_tuesday_sub.setText(tTableInfo.subject2);
        textView_wednesday_sub.setText(tTableInfo.subject3);
        textView_thursday_sub.setText(tTableInfo.subject4);
        textView_friday_sub.setText(tTableInfo.subject5);

        if (!tTableInfo.subject6.equals(""))
            textView_saturday_sub.setVisibility(View.VISIBLE);
        textView_saturday_sub.setText(tTableInfo.subject6);
        textView_time.setText(tTableInfo.time);

        String subject = textView_monday_sub.getText().toString();
        if (subject.startsWith("A")){
            itemView.setBackgroundResource(R.color.colorA);
        }else if (subject.startsWith("B")){
            itemView.setBackgroundResource(R.color.colorB);
        }else if (subject.startsWith("C")){
            itemView.setBackgroundResource(R.color.colorC);
        }else if (subject.startsWith("D")){
            itemView.setBackgroundResource(R.color.colorD);
        }else if (subject.startsWith("E")){
            itemView.setBackgroundResource(R.color.colorE);
        }else if (subject.startsWith("F")){
            itemView.setBackgroundResource(R.color.colorF);
        }else if (subject.startsWith("G")){
            itemView.setBackgroundResource(R.color.colorG);
        }else if (subject.startsWith("H")){
            itemView.setBackgroundResource(R.color.colorH);
        }else if (subject.startsWith("I")){
            itemView.setBackgroundResource(R.color.colorI);
        }else if (subject.startsWith("J")){
            itemView.setBackgroundResource(R.color.colorJ);
        }else if (subject.startsWith("K")){
            itemView.setBackgroundResource(R.color.colorK);
        }else if (subject.startsWith("L")){
            itemView.setBackgroundResource(R.color.colorL);
        }else if (subject.startsWith("M")){
            itemView.setBackgroundResource(R.color.colorM);
        }else if (subject.startsWith("N")){
            itemView.setBackgroundResource(R.color.colorN);
        }else if (subject.startsWith("O")){
            itemView.setBackgroundResource(R.color.colorO);
        }else if (subject.startsWith("P")){
            itemView.setBackgroundResource(R.color.colorP);
        }else if (subject.startsWith("Q")){
            itemView.setBackgroundResource(R.color.colorQ);
        }else if (subject.startsWith("R")){
            itemView.setBackgroundResource(R.color.colorR);
        }else if (subject.startsWith("S")){
            itemView.setBackgroundResource(R.color.colorS);
        }else if (subject.startsWith("T")){
            itemView.setBackgroundResource(R.color.colorT);
        }else if (subject.startsWith("U")){
            itemView.setBackgroundResource(R.color.colorU);
        }else if (subject.startsWith("V")){
            itemView.setBackgroundResource(R.color.colorV);
        }else if (subject.startsWith("W")){
            itemView.setBackgroundResource(R.color.colorW);
        }else if (subject.startsWith("X")){
            itemView.setBackgroundResource(R.color.colorX);
        }else if (subject.startsWith("Y")){
            itemView.setBackgroundResource(R.color.colorY);
        }else if (subject.startsWith("Z")){
            itemView.setBackgroundResource(R.color.colorZ);
        }
        textView_time.setTypeface(typeface);
        textView_monday_sub.setTypeface(typeface);
        textView_tuesday_sub.setTypeface(typeface);
        textView_wednesday_sub.setTypeface(typeface);
        textView_thursday_sub.setTypeface(typeface);
        textView_friday_sub.setTypeface(typeface);
        textView_saturday_sub.setTypeface(typeface);

        return itemView;
    }
}
