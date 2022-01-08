package com.example.crypedu.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.activity.smi.R;
import com.example.crypedu.Pojo.FirstTermInfo;

import java.util.ArrayList;

/**
 * Created by INDID on 09-01-2018.
 */

public class FirstTermAdapter extends BaseAdapter{
    private final Context context;
    private final ArrayList<FirstTermInfo> firstTermInfoArrayList;

    public FirstTermAdapter(Context context, ArrayList<FirstTermInfo> firstTermInfoArrayList, LayoutInflater layoutInflater) {
        this.context = context;
        this.firstTermInfoArrayList = firstTermInfoArrayList;

    }

    @Override
    public int getCount() {
        return firstTermInfoArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return firstTermInfoArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        @SuppressLint("ViewHolder") View view = inflater.inflate(R.layout.first_term_adapter, parent, false);
        TextView subject_TextView = view.findViewById(R.id.subject_TextView);
        TextView periodic_TextView = view.findViewById(R.id.periodic_TextView);
        TextView note_book_TextView = view.findViewById(R.id.note_book_TextView);
        TextView sub_enrichment_TextView = view.findViewById(R.id.sub_enrichment_TextView);
        TextView half_yearly_TextView = view.findViewById(R.id.half_yearly_TextView);
        TextView marks_obtained_TextView = view.findViewById(R.id.marks_obtained_TextView);
        TextView grade_TextView = view.findViewById(R.id.grade_TextView);

        FirstTermInfo firstTermInfo = firstTermInfoArrayList.get(position);
        subject_TextView.setText(firstTermInfo.ft_subject);
        periodic_TextView.setText(firstTermInfo.ft_periodic);
        note_book_TextView.setText(firstTermInfo.ft_note_book);
        sub_enrichment_TextView.setText(firstTermInfo.ft_sub_enrich);
        half_yearly_TextView.setText(firstTermInfo.ft_half_yearly);
        /*if (firstTermInfo.ft_class.equalsIgnoreCase("I") || firstTermInfo.ft_class.equalsIgnoreCase("II")) {
            marks_obtained_TextView.setVisibility(View.GONE);
        }*/ //code off on 14/10/2020
        marks_obtained_TextView.setText(firstTermInfo.ft_marks_obtain);
        grade_TextView.setText(firstTermInfo.ft_grade);
//       Typeface typeface = Typeface.createFromAsset(convertView.getContext().getAssets(), Constants.BubblegumSans_Regular_font);
//        subject_TextView.setTypeface(typeface);
//        fa_TextView.setTypeface(typeface);
//        sa_TextView.setTypeface(typeface);
//        total_TextView.setTypeface(typeface);
        return view;
    }
}
