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
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.activity.smi.R;
import com.example.crypedu.Constants.Constants;
import com.example.crypedu.Pojo.SyllabusPlanningInfo;
import java.util.List;

public class SyllabusPlanningAdapter extends BaseAdapter {
    private final Context context;
    private final List<SyllabusPlanningInfo> syllabusPlanningInfoList;
    LayoutInflater mInflater;
    LayoutInflater inflater;

    public SyllabusPlanningAdapter(Context context, List<SyllabusPlanningInfo> syllabusPlanningInfoList, LayoutInflater inflater) {
        mInflater = inflater;
        this.context = context;
        this.syllabusPlanningInfoList = syllabusPlanningInfoList;
    }

    @Override
    public int getCount() {
        return syllabusPlanningInfoList.size();
    }

    @Override
    public Object getItem(int position) {
        return syllabusPlanningInfoList.get(position);
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
        itemView = inflater.inflate(R.layout.syllabus_planning_adapter, parent, false);
        TextView class_name_textView = itemView.findViewById(R.id.class_name_textView);
        TextView teachers_nam_textView = itemView.findViewById(R.id.teachers_nam_textView);
        TextView subject_textView = itemView.findViewById(R.id.subject_textView);
        TextView chapter_textView = itemView.findViewById(R.id.chapter_textView);
        TextView no_of_class_textView = itemView.findViewById(R.id.no_of_class_textView);

        Typeface typeface= Typeface.createFromAsset(itemView.getContext().getAssets(), Constants.BubblegumSans_Regular_font);

        final SyllabusPlanningInfo syllabusPlanningInfo = syllabusPlanningInfoList.get(position);
        class_name_textView.setText(syllabusPlanningInfo.class_name);
        class_name_textView.setTypeface(typeface);
        teachers_nam_textView.setText(syllabusPlanningInfo.teachers_name);
        teachers_nam_textView.setTypeface(typeface);
        subject_textView.setText(Html.fromHtml("<h2><u>"+ syllabusPlanningInfo.subject +"</u></h2>"));
        subject_textView.setTypeface(typeface);
        chapter_textView.setText(syllabusPlanningInfo.chapter);
        chapter_textView.setTypeface(typeface);
        no_of_class_textView.setText(syllabusPlanningInfo.no_of_class);
        no_of_class_textView.setTypeface(typeface);

        return itemView;
    }
}