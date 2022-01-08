package com.example.crypedu.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.activity.smi.R;
import com.example.crypedu.Pojo.AssignmentFileModel;
import java.util.ArrayList;

public class AssignmentHistoryListAdapter extends RecyclerView.Adapter<AssignmentHistoryListAdapter.PeriodViewHolder> {
    Context context;
    ArrayList<AssignmentFileModel> receiveHistoryList;
    public AssignmentHistoryListAdapter(Context context, ArrayList<AssignmentFileModel> receiveHistoryList){
        this.context = context;
        this.receiveHistoryList = receiveHistoryList;

    }

    @NonNull
    @Override
    public AssignmentHistoryListAdapter.PeriodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        View view = inflater.inflate(R.layout.item_assignment_history_list_adapter, parent, false);
        return new AssignmentHistoryListAdapter.PeriodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AssignmentHistoryListAdapter.PeriodViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        if (position%5==0)
            holder.rl_item_background.setBackgroundColor(Color.parseColor("#009688"));
        else if (position%5==1)
            holder.rl_item_background.setBackgroundColor(Color.parseColor("#2F8078"));
        else if (position%5==2)
            holder.rl_item_background.setBackgroundColor(Color.parseColor("#1D605A"));
        else if (position%5==3)
            holder.rl_item_background.setBackgroundColor(Color.parseColor("#1D4A46"));
        else if (position%5==4)
            holder.rl_item_background.setBackgroundColor(Color.parseColor("#57807B"));

        holder.tv_serial_no.setText(String.valueOf(position+1));
        holder.tv_assignment_topic_name.setText("Assignment " + String.valueOf(position+1));

        holder.btn_view_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(receiveHistoryList.get(position).getFile_url()));
                context.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return receiveHistoryList.size();
    }

    class PeriodViewHolder extends RecyclerView.ViewHolder{
        TextView tv_assignment_topic_name, tv_serial_no;
        Button btn_view_file;
        RelativeLayout rl_item_background;

        public PeriodViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_assignment_topic_name = itemView.findViewById(R.id.tv_assignment_topic_name);
            btn_view_file = itemView.findViewById(R.id.btn_view_file);
            tv_serial_no = itemView.findViewById(R.id.tv_serial_no);
            rl_item_background = itemView.findViewById(R.id.rl_item_background);
        }
    }
}
