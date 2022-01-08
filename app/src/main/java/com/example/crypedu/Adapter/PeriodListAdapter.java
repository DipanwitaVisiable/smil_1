package com.example.crypedu.Adapter;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.activity.smi.R;
import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.example.crypedu.Activity.LiveVideoListActivity;
import com.example.crypedu.Constants.Constants;
import com.example.crypedu.Pojo.PeriodListInfo;

import java.util.ArrayList;

public class PeriodListAdapter extends RecyclerView.Adapter<PeriodListAdapter.PeriodViewHolder> {

    Context context;
    ArrayList<PeriodListInfo> receivePeriodList;
    public PeriodListAdapter(Context context, ArrayList<PeriodListInfo> receivePeriodList){

        this.context = context;
        this.receivePeriodList = receivePeriodList;

    }

    @NonNull
    @Override
    public PeriodListAdapter.PeriodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        View view = inflater.inflate(R.layout.item_period_list_adapter, parent, false);
        return new PeriodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PeriodListAdapter.PeriodViewHolder holder, final int position) {
        ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT
        int color = generator.getColor(position);
        TextDrawable drawable = TextDrawable.builder()
                .buildRound(String.valueOf(receivePeriodList.get(position).getPeriods().charAt(0)), color);
        holder.iv_period_image.setImageDrawable(drawable);

//        holder.textView.setTextColor(Color.parseColor("#074f8f"));
        holder.tv_period_name.setText(receivePeriodList.get(position).getPeriods());

        holder.cd_period.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Constants.PERIOD_ID=receivePeriodList.get(position).getId();
                Constants.PERIOD_NAME=receivePeriodList.get(position).getPeriods();
                Intent intent = new Intent(context, LiveVideoListActivity.class);
                context.startActivity(intent);

                /*Bundle bundle = new Bundle();
                bundle.putString("days_id", receiveDaysList.get(position).getId());
                bundle.putString("period_id", receiveDaysList.get(position).getWeekdays());
                bundle.putString("period_name", receiveDaysList.get(position).getWeekdays());
                Intent intent = new Intent(context, LiveVideoListActivity.class);
                intent.putExtras(bundle);
                context.startActivity(intent);*/


            }
        });

    }

    @Override
    public int getItemCount() {
        return receivePeriodList.size();
    }

    class PeriodViewHolder extends RecyclerView.ViewHolder{
        TextView tv_period_name;
        CardView cd_period;
        ImageView iv_period_image;

        public PeriodViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_period_name = itemView.findViewById(R.id.tv_period_name);
            iv_period_image = itemView.findViewById(R.id.iv_period_image);
            cd_period = itemView.findViewById(R.id.cd_period);
        }
    }
}
