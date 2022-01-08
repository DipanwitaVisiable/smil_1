package com.example.crypedu.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.activity.smi.R;
import com.example.crypedu.Pojo.DoctorSetterGetter;

import java.util.List;

public class NewCustomLabAdapter extends BaseAdapter {

    private Context context;
    private List<DoctorSetterGetter> setterGetters;
    public NewCustomLabAdapter(Context context, List<DoctorSetterGetter> setterGetters){
        this.context = context;
        this.setterGetters = setterGetters;
    }

    @Override
    public int getCount() {
        Log.e("Size ", String.valueOf(setterGetters.size()));
        return setterGetters.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint({"SetTextI18n", "InflateParams"})
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView textView, textView1;
        if (convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            assert inflater != null;
            convertView = inflater.inflate(R.layout.custom_doctor_chamber_list, null, false);
        }

        Log.e("Attached ", "true");
        textView = convertView.findViewById(R.id.service_name);
        textView1 = convertView.findViewById(R.id.doctor_discount);

        textView.setText(setterGetters.get(position).getDoctor_service_name());
        textView1.setText(setterGetters.get(position).getDoctor_discount()+"%");
        return convertView;
    }
}
