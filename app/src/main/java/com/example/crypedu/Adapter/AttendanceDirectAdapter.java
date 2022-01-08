package com.example.crypedu.Adapter;
/*
 *************************************************************************************************
 * Developed by Cryptonsoftech
 * Date: 30-05-2017
 * Details:
 * ***********************************************************************************************
 */
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.activity.smi.R;
import com.example.crypedu.Pojo.AttendanceDirectInfo;

import java.util.List;

public class AttendanceDirectAdapter extends RecyclerView.Adapter<AttendanceDirectAdapter.ViewHolder>{

    private Context context;
    private List<AttendanceDirectInfo> infoList;

    public AttendanceDirectAdapter(Context context, List<AttendanceDirectInfo> infoList){
        this.context = context;
        this.infoList = infoList;
    }
    @NonNull
    @Override
    public AttendanceDirectAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        View view = inflater.inflate(R.layout.attendance_direct_adapter, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AttendanceDirectAdapter.ViewHolder holder, int position) {
        AttendanceDirectInfo info = infoList.get(position);
        holder.classText.setText(info.classSt);
        holder.classText.setSelected(true);
        holder.sectionText.setText(info.sectionSt);
        holder.totalStText.setText(info.totalSt);
        holder.totalPrestText.setText(info.totalPresent);
        holder.percentText.setText(info.percentage);
    }

    @Override
    public int getItemCount() {
        return infoList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView classText, sectionText, totalStText, totalPrestText, percentText;
        public ViewHolder(View itemView) {
            super(itemView);
            classText = itemView.findViewById(R.id.add_class);
            sectionText = itemView.findViewById(R.id.add_sec);
            totalStText = itemView.findViewById(R.id.add_total_st);
            totalPrestText = itemView.findViewById(R.id.add_total_p);
            percentText = itemView.findViewById(R.id.add_per);
        }
    }
}

