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
import com.example.crypedu.Pojo.ReplyCommunicationInfo;
import java.util.List;

/**
 * Created by hp on 7/10/2017.
 */
public class ReplyCommunicationAdapter extends BaseAdapter {
    private final Context context;
    private final List<ReplyCommunicationInfo> replyCommunicationInfoList;

    public ReplyCommunicationAdapter(Context context, List<ReplyCommunicationInfo> replyCommunicationInfoList, LayoutInflater layoutInflater){
        this.context = context;
        this.replyCommunicationInfoList = replyCommunicationInfoList;
    }
    @Override
    public int getCount() {
        return replyCommunicationInfoList.size();
    }

    @Override
    public Object getItem(int position) {
        return replyCommunicationInfoList.get(position);
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
        @SuppressLint("ViewHolder") View view = inflater.inflate(R.layout.reply_communication_adapter, parent, false);
        Typeface typeface = Typeface.createFromAsset(view.getContext().getAssets(), Constants.BubblegumSans_Regular_font);
        TextView textView = view.findViewById(R.id.textView);
        ReplyCommunicationInfo replyCommunicationInfo = replyCommunicationInfoList.get(position);
        String requestBy;
        if (replyCommunicationInfo.requestBy.equalsIgnoreCase("a")){
            requestBy = "Admin";
        }else {
            requestBy = "Student";
        }
        textView.setText(replyCommunicationInfo.replyMessage+"\n"+ requestBy +"\n"+replyCommunicationInfo.date);
        textView.setTypeface(typeface);
        return view;
    }
}
