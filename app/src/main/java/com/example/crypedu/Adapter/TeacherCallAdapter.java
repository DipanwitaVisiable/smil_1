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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.activity.smi.R;
import com.example.crypedu.Constants.Constants;
import java.util.List;

public class TeacherCallAdapter extends BaseAdapter {
    private final Context context;
    private final List<String> stringInfoList;
    private final List<String> mobileInfoList;
    LayoutInflater mInflater;
    LayoutInflater inflater;
    private LinearLayout linearLayout;

    public TeacherCallAdapter(Context context, List<String> stringInfoList, List<String> mobileInfoList, LayoutInflater inflater) {
        mInflater = inflater;
        this.context = context;
        this.stringInfoList = stringInfoList;
        this.mobileInfoList = mobileInfoList;
    }

    @Override
    public int getCount() {
        return stringInfoList.size();
    }

    @Override
    public Object getItem(int position) {
        return stringInfoList.get(position);
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
        itemView = inflater.inflate(R.layout.teacher_calling_adapter, parent, false);
        TextView textViewName = itemView.findViewById(R.id.textViewName);
        TextView textViewMobile = itemView.findViewById(R.id.textViewMobile);

        Typeface typeface= Typeface.createFromAsset(itemView.getContext().getAssets(), Constants.BubblegumSans_Regular_font);

        final String nameInfo = stringInfoList.get(position);
        final String mobileInfo = mobileInfoList.get(position);
        textViewName.setText(nameInfo);
        textViewMobile.setText(mobileInfo);
        textViewName.setTypeface(typeface);
        textViewMobile.setTypeface(typeface);

        return itemView;
    }
}
