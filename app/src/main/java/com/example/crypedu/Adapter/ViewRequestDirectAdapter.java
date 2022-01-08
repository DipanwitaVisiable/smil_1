package com.example.crypedu.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.activity.smi.R;
import com.example.crypedu.Pojo.ViewRequestDirectInfo;

import java.util.List;

/**
 * Created by hp on 6/21/2017.
 */
public class ViewRequestDirectAdapter extends BaseAdapter {
    private final Context context;
    private final List<ViewRequestDirectInfo> viewRequestInfoList;

    public ViewRequestDirectAdapter(Context context, List<ViewRequestDirectInfo> viewRequestInfoList, LayoutInflater layoutInflater){
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
        @SuppressLint("ViewHolder")
        View view = inflater.inflate(R.layout.view_request_direct_adapter, parent, false);
        TextView textTitle = view.findViewById(R.id.textTitle);
        TextView textBody = view.findViewById(R.id.textBody);
        TextView textDate = view.findViewById(R.id.textDate);
        TextView textType = view.findViewById(R.id.textType);
        TextView textStatus = view.findViewById(R.id.textStatus);
        TextView textDetails = view.findViewById(R.id.textDetail);
        //Typeface typeface= Typeface.createFromAsset(view.getContext().getAssets(), Constants.BubblegumSans_Regular_font);
        //textView.setTypeface(typeface);
        ViewRequestDirectInfo viewRequestInfo = viewRequestInfoList.get(position);

        textDetails.setText(viewRequestInfo.name + ", Class: "+viewRequestInfo.classStr+", Section: "+viewRequestInfo.sectionStr);
        textTitle.setText(viewRequestInfo.subjectStr);
        textBody.setText(viewRequestInfo.messageStr);
        textDate.setText(viewRequestInfo.dateStr);
        textType.setText(viewRequestInfo.typeStr);

//        textView.setText(viewRequestInfo.subjectStr+"\n"
//                +viewRequestInfo.messageStr+"\n"+viewRequestInfo.fromDateStr+" - "+viewRequestInfo.toDateStr+"\n"+viewRequestInfo.typeStr+"\n"+status);
        return view;
    }
}
