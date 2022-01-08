package com.example.crypedu.Adapter;

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
import com.example.crypedu.Pojo.ViewRequestInfo;

import java.util.List;

/**
 * Created by hp on 6/21/2017.
 */
public class ViewRequestAdapter extends BaseAdapter {
    private final Context context;
    private final List<ViewRequestInfo> viewRequestInfoList;

    public ViewRequestAdapter(Context context, List<ViewRequestInfo> viewRequestInfoList, LayoutInflater layoutInflater){
        this.context = context;
        this.viewRequestInfoList = viewRequestInfoList;
    }
    @Override
    public int getCount() {
        return viewRequestInfoList.size();
    }

    @Override
    public Object getItem(int position) {
        return viewRequestInfoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        @SuppressLint("ViewHolder") View view = inflater.inflate(R.layout.view_request_adapter, parent, false);
        TextView textView = view.findViewById(R.id.textView);
        Typeface typeface= Typeface.createFromAsset(view.getContext().getAssets(), Constants.BubblegumSans_Regular_font);
        textView.setTypeface(typeface);
        ViewRequestInfo viewRequestInfo = viewRequestInfoList.get(position);
        String status;
        if (viewRequestInfo.statusStr.equalsIgnoreCase("1")){
            status = "Approved";
        }else {
            status = "Not Approved";
        }
        textView.setText(viewRequestInfo.subjectStr+"\n"
                +viewRequestInfo.messageStr+"\n"+viewRequestInfo.fromDateStr+" - "+viewRequestInfo.toDateStr+"\n"+viewRequestInfo.typeStr+"\n"+status);
        return view;
    }
}
