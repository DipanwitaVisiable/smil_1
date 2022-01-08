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
import com.example.crypedu.Pojo.LateReasonInfo;

import java.util.List;

/**
 * Created by hp on 6/23/2017.
 */
public class LateReasonAdapter extends BaseAdapter {
    private final Context context;
    private final List<LateReasonInfo> lateReasonInfoList;

    public LateReasonAdapter(Context context, List<LateReasonInfo> lateReasonInfoList, LayoutInflater layoutInflater){
        this.context = context;
        this.lateReasonInfoList = lateReasonInfoList;
    }
    @Override
    public int getCount() {
        return lateReasonInfoList.size();
    }

    @Override
    public Object getItem(int position) {
        return lateReasonInfoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint({"SetTextI18n", "ViewHolder"})
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View itemView;
        assert inflater != null;
        itemView = inflater.inflate(R.layout.late_reason_adapter, parent, false);
        TextView textView = itemView.findViewById(R.id.textView);
        Typeface typeface = Typeface.createFromAsset(itemView.getContext().getAssets(), Constants.BubblegumSans_Regular_font);
        textView.setTypeface(typeface);
        String status;
        LateReasonInfo lateReasonInfo = lateReasonInfoList.get(position);
        if (!lateReasonInfo.lateReason.equalsIgnoreCase("")){
            if (lateReasonInfo.attendanceStatus.equalsIgnoreCase("a")){
                status = "Absent";
                textView.setText(lateReasonInfo.lateReason+"\n"+lateReasonInfo.date+"\n"+status);
            }else if (lateReasonInfo.attendanceStatus.equalsIgnoreCase("p")){
                status = "Present";
                textView.setText(lateReasonInfo.lateReason+"\n"+lateReasonInfo.date+"\n"+status);
            }
        }

        return itemView;
    }
}
