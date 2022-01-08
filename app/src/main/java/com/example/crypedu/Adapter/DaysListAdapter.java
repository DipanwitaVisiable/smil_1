package com.example.crypedu.Adapter;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.activity.smi.R;
import com.example.crypedu.Activity.PeriodListActivity;
import com.example.crypedu.Constants.Constants;
import com.example.crypedu.Pojo.DaysListInfo;

import java.util.ArrayList;

public class DaysListAdapter extends RecyclerView.Adapter<DaysListAdapter.DaysViewHolder> {

    Context context;
    ArrayList<DaysListInfo> receiveDaysList;
    public DaysListAdapter(Context context, ArrayList<DaysListInfo> receiveDaysList){

        this.context = context;
        this.receiveDaysList = receiveDaysList;

    }

    @NonNull
    @Override
    public DaysListAdapter.DaysViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
//        View view = inflater.inflate(R.layout.item_days_list_adapter, parent, false);
        View view = inflater.inflate(R.layout.item_of_recycler_view_demo, parent, false);
        return new DaysViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DaysListAdapter.DaysViewHolder holder, final int position) {

        //*****Start****//
        holder.tv_days_name.setText(receiveDaysList.get(position).getWeekdays());

        /*if (position==1)
            holder.ll_background.setBackgroundResource(R.drawable.button_shape_demo_2);
        else if (position==3)
            holder.ll_background.setBackgroundResource(R.drawable.button_shape_demo_2);
        else if (position==5)
            holder.ll_background.setBackgroundResource(R.drawable.button_shape_demo_2);*/

        holder.ll_background.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constants.DAYS_ID=receiveDaysList.get(position).getId();
                Constants.DAYS_NAME=receiveDaysList.get(position).getWeekdays();
                Intent intent = new Intent(context, PeriodListActivity.class);
                context.startActivity(intent);


            }
        });

        //*****End****//

       /* if (position==0) {
            holder.ll_background.setBackgroundResource(R.drawable.monday);
            holder.cd_week_days.setBackgroundResource(R.drawable.monday);
            holder.days_image.setTextColor(Color.parseColor("#1976D2"));
        }
        else if (position==1) {
            holder.ll_background.setBackgroundResource(R.drawable.tuesday);
            holder.cd_week_days.setBackgroundResource(R.drawable.tuesday);
            holder.days_image.setTextColor(Color.parseColor("#d14bc9"));
        }
        else if (position==2) {
            holder.ll_background.setBackgroundResource(R.drawable.wednesday);
            holder.cd_week_days.setBackgroundResource(R.drawable.wednesday);
            holder.days_image.setTextColor(Color.parseColor("#51915b"));
        }
        else if (position==3) {
            holder.ll_background.setBackgroundResource(R.drawable.friday);
            holder.cd_week_days.setBackgroundResource(R.drawable.friday);
            holder.days_image.setTextColor(Color.parseColor("#ba312f"));
        }
        else if (position==4) {
            holder.ll_background.setBackgroundResource(R.drawable.saturday);
            holder.cd_week_days.setBackgroundResource(R.drawable.saturday);
            holder.days_image.setTextColor(Color.parseColor("#745d9e"));
        }
        else if (position==5) {
            holder.ll_background.setBackgroundResource(R.drawable.thrusday);
            holder.cd_week_days.setBackgroundResource(R.drawable.thrusday);
            holder.days_image.setTextColor(Color.parseColor("#548a88"));
        }

        String test = receiveDaysList.get(position).getWeekdays();
        String first_letter=test.substring(0,1);
        holder.days_image.setText(first_letter);

        holder.tv_days_name.setTextColor(Color.parseColor("#ffffff"));
        holder.tv_days_name.setText(receiveDaysList.get(position).getWeekdays());

        holder.cd_week_days.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constants.DAYS_ID=receiveDaysList.get(position).getId();
                Constants.DAYS_NAME=receiveDaysList.get(position).getWeekdays();
                Intent intent = new Intent(context, PeriodListActivity.class);
                context.startActivity(intent);


            }
        });*/

    }

    @Override
    public int getItemCount() {
        return receiveDaysList.size();
    }

    class DaysViewHolder extends RecyclerView.ViewHolder{
        TextView tv_days_name;
        CardView cd_week_days;
        TextView days_image;
        LinearLayout ll_background;

        public DaysViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_days_name = itemView.findViewById(R.id.tv_days_name);
           /* days_image = itemView.findViewById(R.id.days_image);
            cd_week_days = itemView.findViewById(R.id.cd_week_days);*/
            ll_background = itemView.findViewById(R.id.ll_background);
        }
    }
}
