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
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.activity.smi.R;
import com.example.crypedu.Constants.Constants;
import com.example.crypedu.Pojo.NoticeInfo;

import java.util.List;

public class NoticeDirectAdapter extends BaseAdapter {
    private final Context context;
    private final List<NoticeInfo> noticeInfoList;
    LayoutInflater mInflater;
    LayoutInflater inflater;

    public NoticeDirectAdapter(Context context, List<NoticeInfo> noticeInfoList, LayoutInflater inflater) {
        mInflater = inflater;
        this.context = context;
        this.noticeInfoList = noticeInfoList;
    }

    @Override
    public int getCount() {
        return noticeInfoList.size();
    }

    @Override
    public Object getItem(int position) {
        return noticeInfoList.get(position);
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
        itemView = inflater.inflate(R.layout.notice_direct_adapter, parent, false);
        TextView noticeHead = itemView.findViewById(R.id.notice_head);
        TextView noticeDate = itemView.findViewById(R.id.notice_date);
        TextView noticeBody = itemView.findViewById(R.id.notice_body);

        Typeface typeface = Typeface.createFromAsset(itemView.getContext().getAssets(), Constants.BubblegumSans_Regular_font);

        final NoticeInfo noticeInfo = noticeInfoList.get(position);
        noticeHead.setText(Html.fromHtml("<u><font color = Navy>"+noticeInfo.subject+"</font> </u>"));
        noticeDate.setText(Html.fromHtml("<font color=Teal>"+noticeInfo.date+"</font>"));
        noticeBody.setText(Html.fromHtml("<font color=Black>" +noticeInfo.notice+"</font>"));
        itemView.setBackgroundColor(Color.WHITE);
        return itemView;
    }
}

