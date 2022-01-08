package com.example.crypedu.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.activity.smi.R;
import com.example.crypedu.Pojo.TimeInfo;

import java.util.List;

public class TimeTableNewAdapter extends RecyclerView.Adapter<TimeTableNewAdapter.ViewHolder> {


    private Context context;
    private List<TimeInfo> mList;

    public TimeTableNewAdapter(Context context, List<TimeInfo> list) {
        this.context = context;
        this.mList=list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater=LayoutInflater.from(context);

        View view=layoutInflater.inflate(R.layout.item_time_table_adapter,parent,false);

        ViewHolder viewHolder=new ViewHolder(view);

        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        ViewHolder rowViewHolder = (ViewHolder) holder;

        int rowPos = rowViewHolder.getAdapterPosition();

        if (rowPos == 0)
        {

            rowViewHolder.tv_periods.setBackgroundColor(Color.parseColor("#EC407A"));
            rowViewHolder.tv_mon.setBackgroundColor(Color.parseColor("#E91E63"));
            rowViewHolder.tv_tue.setBackgroundColor(Color.parseColor("#D81B60"));
            rowViewHolder.tv_wed.setBackgroundColor(Color.parseColor("#C2185B"));
            rowViewHolder.tv_thu.setBackgroundColor(Color.parseColor("#AD1457"));
            rowViewHolder.tv_fri.setBackgroundColor(Color.parseColor("#880E4F"));
            rowViewHolder.tv_sat.setBackgroundColor(Color.parseColor("#6b0a3e"));


            rowViewHolder.tv_periods.setText("PERIODS");
            rowViewHolder.tv_mon.setText("MON");
            rowViewHolder.tv_tue.setText("TUE");
            rowViewHolder.tv_wed.setText("WED");
            rowViewHolder.tv_thu.setText("THU");
            rowViewHolder.tv_fri.setText("FRI");

            if (mList.get(0).subject6.equals(""))
                rowViewHolder.tv_sat.setVisibility(View.GONE);
            else {
                rowViewHolder.tv_sat.setText("SAT");
                rowViewHolder.tv_sat.setVisibility(View.VISIBLE);
            }



            rowViewHolder.tv_periods.setTextColor(Color.parseColor("#FFFFFF"));
            rowViewHolder.tv_mon.setTextColor(Color.parseColor("#FFFFFF"));
            rowViewHolder.tv_tue.setTextColor(Color.parseColor("#FFFFFF"));
            rowViewHolder.tv_wed.setTextColor(Color.parseColor("#FFFFFF"));
            rowViewHolder.tv_thu.setTextColor(Color.parseColor("#FFFFFF"));
            rowViewHolder.tv_fri.setTextColor(Color.parseColor("#FFFFFF"));
            rowViewHolder.tv_sat.setTextColor(Color.parseColor("#FFFFFF"));


        }

        else
        {

            TimeInfo timeTableModel = mList.get(rowPos -1);


            rowViewHolder.tv_periods.setBackgroundResource(R.drawable.table_content_cell_bg);
            rowViewHolder.tv_mon.setBackgroundResource(R.drawable.table_content_cell_bg);
            rowViewHolder.tv_tue.setBackgroundResource(R.drawable.table_content_cell_bg);
            rowViewHolder.tv_wed.setBackgroundResource(R.drawable.table_content_cell_bg);
            rowViewHolder.tv_thu.setBackgroundResource(R.drawable.table_content_cell_bg);
            rowViewHolder.tv_fri.setBackgroundResource(R.drawable.table_content_cell_bg);
            rowViewHolder.tv_sat.setBackgroundResource(R.drawable.table_content_cell_bg);

            rowViewHolder.tv_periods.setTextColor(Color.parseColor("#F06292"));
            rowViewHolder.tv_mon.setTextColor(Color.parseColor("#F06292"));
            rowViewHolder.tv_tue.setTextColor(Color.parseColor("#F06292"));
            rowViewHolder.tv_wed.setTextColor(Color.parseColor("#F06292"));
            rowViewHolder.tv_thu.setTextColor(Color.parseColor("#F06292"));
            rowViewHolder.tv_fri.setTextColor(Color.parseColor("#F06292"));
            rowViewHolder.tv_sat.setTextColor(Color.parseColor("#F06292"));



            rowViewHolder.tv_periods.setText(timeTableModel.time);
            rowViewHolder.tv_mon.setText(timeTableModel.subject1);
            rowViewHolder.tv_tue.setText(timeTableModel.subject2);
            rowViewHolder.tv_wed.setText(timeTableModel.subject3);
            rowViewHolder.tv_thu.setText(timeTableModel.subject4);
            rowViewHolder.tv_fri.setText(timeTableModel.subject5);
            if (timeTableModel.subject6.equals(""))
                rowViewHolder.tv_sat.setVisibility(View.GONE);
            else
                rowViewHolder.tv_sat.setVisibility(View.VISIBLE);
            rowViewHolder.tv_sat.setText(timeTableModel.subject6);


        }



    }

    @Override
    public int getItemCount() {

        return mList.size()+1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView tv_periods,tv_mon,tv_tue,tv_wed,tv_thu,tv_fri,tv_sat;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);

            tv_periods = itemView.findViewById(R.id.tv_periods);
            tv_mon = itemView.findViewById(R.id.tv_mon);
            tv_tue = itemView.findViewById(R.id.tv_tue);
            tv_wed = itemView.findViewById(R.id.tv_wed);
            tv_thu = itemView.findViewById(R.id.tv_thu);
            tv_fri = itemView.findViewById(R.id.tv_fri);
            tv_sat = itemView.findViewById(R.id.tv_sat);


        }
    }
}