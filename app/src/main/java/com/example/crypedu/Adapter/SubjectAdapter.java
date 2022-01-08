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
import android.widget.TextView;
import com.activity.smi.R;
import com.example.crypedu.Constants.Constants;
import java.util.List;

public class SubjectAdapter extends BaseAdapter {
    private final Context context;
    private final List<String> stringInfoList;
    LayoutInflater mInflater;
    LayoutInflater inflater;

    public SubjectAdapter(Context context, List<String> stringInfoList, LayoutInflater inflater) {
        mInflater = inflater;
        this.context = context;
        this.stringInfoList = stringInfoList;
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
        itemView = inflater.inflate(R.layout.subject_adapter, parent, false);
        TextView subject_textView = itemView.findViewById(R.id.subject_textView);

        Typeface typeface= Typeface.createFromAsset(itemView.getContext().getAssets(), Constants.BubblegumSans_Regular_font);

        final String info = stringInfoList.get(position);
        subject_textView.setText(info);
        subject_textView.setTypeface(typeface);

        return itemView;
    }
}

