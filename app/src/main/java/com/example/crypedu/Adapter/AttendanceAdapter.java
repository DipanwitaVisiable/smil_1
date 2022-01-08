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
import android.widget.TextView;
import com.activity.smi.R;
import com.example.crypedu.Constants.Constants;
import com.example.crypedu.Pojo.AttendanceInfo;
import java.util.List;

public class AttendanceAdapter extends BaseAdapter {
    private final Context context;
    private final List<AttendanceInfo> attendanceInfos;
    LayoutInflater mInflater;
    LayoutInflater inflater;

    public AttendanceAdapter(Context context, List<AttendanceInfo> attendanceInfos, LayoutInflater inflater) {
        mInflater = inflater;
        this.context = context;
        this.attendanceInfos = attendanceInfos;
    }

    @Override
    public int getCount() {
        return attendanceInfos.size();
    }

    @Override
    public Object getItem(int position) {
        return attendanceInfos.get(position);
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
        itemView = inflater.inflate(R.layout.attendance_adapter, parent, false);
        TextView date_textView = itemView.findViewById(R.id.date_textView);
        TextView status_textView = itemView.findViewById(R.id.status_textView);

        Typeface typeface = Typeface.createFromAsset(itemView.getContext().getAssets(), Constants.openSans_Regular_font);

        final AttendanceInfo attendanceInfo = attendanceInfos.get(position);
        date_textView.setText(attendanceInfo.date);
        if (attendanceInfo.attendence_status.equalsIgnoreCase("a")){
            status_textView.setText("Absent");
            status_textView.setTextColor(Color.parseColor(Constants.colorAbsent));
            date_textView.setTextColor(Color.parseColor(Constants.colorAbsent));
        }else if (attendanceInfo.attendence_status.equalsIgnoreCase("p")){
            status_textView.setText("Present");
            status_textView.setTextColor(Color.parseColor(Constants.colorPresent));
            date_textView.setTextColor(Color.parseColor(Constants.colorPresent));
        }
        status_textView.setTypeface(typeface);
        date_textView.setTypeface(typeface);

        return itemView;
    }
}

