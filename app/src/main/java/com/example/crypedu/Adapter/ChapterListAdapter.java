package com.example.crypedu.Adapter;

import android.annotation.SuppressLint;
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
import com.example.crypedu.Activity.AssignmentTopicListActivity;
import com.example.crypedu.Activity.PdfOpenActivity;
import com.example.crypedu.Constants.Constants;
import com.example.crypedu.Pojo.ChapterListInfo;

import java.util.ArrayList;

public class ChapterListAdapter extends RecyclerView.Adapter<ChapterListAdapter.PeriodViewHolder> {

    Context context;
    ArrayList<ChapterListInfo> receiveChapterList;
    public ChapterListAdapter(Context context, ArrayList<ChapterListInfo> receiveChapterList){

        this.context = context;
        this.receiveChapterList = receiveChapterList;

    }

    @NonNull
    @Override
    public ChapterListAdapter.PeriodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        View view = inflater.inflate(R.layout.item_period_list_adapter, parent, false);
        return new PeriodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChapterListAdapter.PeriodViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT
        int color = generator.getColor(position);
        TextDrawable drawable = TextDrawable.builder()
                .buildRound(String.valueOf(receiveChapterList.get(position).getChapter_name().charAt(0)), color);
        holder.iv_period_image.setImageDrawable(drawable);

//        holder.textView.setTextColor(Color.parseColor("#074f8f"));
        holder.tv_period_name.setText(receiveChapterList.get(position).getChapter_name());

        holder.cd_period.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              Intent intent;
              Constants.CHAPTER_ID=receiveChapterList.get(position).getId();
                Constants.CHAPTER_NAME=receiveChapterList.get(position).getChapter_name();
               /* if (Constants.FROM_SCREEN.equals("online_exam"))
                  intent = new Intent(context, AssignmentTopicListActivity.class);
                else*/
                  intent = new Intent(context, PdfOpenActivity.class);
                context.startActivity(intent);



            }
        });

    }

    @Override
    public int getItemCount() {
        return receiveChapterList.size();
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
