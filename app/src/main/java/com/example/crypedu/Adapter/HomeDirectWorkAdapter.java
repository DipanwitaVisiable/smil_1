package com.example.crypedu.Adapter;

/*
 *************************************************************************************************
 * Description:
 *
 * ***********************************************************************************************
 * Author:
 * Date: 05:05:2017
 * ***********************************************************************************************
 * Change:
 * Author:
 * Date:
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
import com.example.crypedu.Pojo.HomeWorkInfo;
import java.util.List;

public class HomeDirectWorkAdapter extends BaseAdapter {
    private final Context context;
    private final List<HomeWorkInfo> homeWorkInfoList;
    LayoutInflater mInflater;
    LayoutInflater inflater;

    public HomeDirectWorkAdapter(Context context, List<HomeWorkInfo> homeWorkInfoList, LayoutInflater inflater) {
        mInflater = inflater;
        this.context = context;
        this.homeWorkInfoList = homeWorkInfoList;
    }

    @Override
    public int getCount() {
        return homeWorkInfoList.size();
    }

    @Override
    public Object getItem(int position) {
        return homeWorkInfoList.get(position);
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
        itemView = inflater.inflate(R.layout.home_direct_work_adapter, parent, false);
        TextView subject_textView = itemView.findViewById(R.id.subject_textView);
        TextView chapter_textView = itemView.findViewById(R.id.chapter_textView);
        TextView assign_textView = itemView.findViewById(R.id.assign_textView);

        Typeface typeface = Typeface.createFromAsset(itemView.getContext().getAssets(), Constants.BubblegumSans_Regular_font);

        final HomeWorkInfo homeWorkInfo = homeWorkInfoList.get(position);
        subject_textView.setText(Html.fromHtml("<u>"+homeWorkInfo.subject+"</u>"));
        subject_textView.setTypeface(typeface);
        chapter_textView.setText(homeWorkInfo.topic);
        chapter_textView.setTypeface(typeface);
        assign_textView.setText(Html.fromHtml("<font color="+"red"+">"+homeWorkInfo.assign +"</font>"+"  To  "+ "<font color="+"green"+">"+homeWorkInfo.submit+"</font>"));
        assign_textView.setTypeface(typeface);

        return itemView;
    }
}
