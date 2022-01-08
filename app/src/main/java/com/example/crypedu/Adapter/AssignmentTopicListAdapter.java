package com.example.crypedu.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.activity.smi.R;
import com.example.crypedu.Activity.AssignmentHistoryListActivity;
import com.example.crypedu.Activity.AssignmentReplyActivity;
import com.example.crypedu.Activity.AssignmentTopicListActivity;
import com.example.crypedu.Activity.DriveDocumentsDownloadActivity;
import com.example.crypedu.Helper.UtilHelper;
import com.example.crypedu.Pojo.AssignmentTopicListInfo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class AssignmentTopicListAdapter extends RecyclerView.Adapter<AssignmentTopicListAdapter.PeriodViewHolder> {

  public Timer updateTimer;
  private String exam_status="";
  Context context;
  ArrayList<AssignmentTopicListInfo> receiveChapterList;
  public AssignmentTopicListAdapter(Context context, ArrayList<AssignmentTopicListInfo> receiveChapterList){

    this.context = context;
    this.receiveChapterList = receiveChapterList;

  }

  @NonNull
  @Override
  public AssignmentTopicListAdapter.PeriodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    assert inflater != null;
    View view = inflater.inflate(R.layout.item_assignment_topic_list_adapter, parent, false);
    return new AssignmentTopicListAdapter.PeriodViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull final AssignmentTopicListAdapter.PeriodViewHolder holder, @SuppressLint("RecyclerView") final int position) {

    if (receiveChapterList.get(position).getReslt_cnt().equals("0"))
      holder.btn_history.setVisibility(View.GONE);
    else
      holder.btn_history.setVisibility(View.VISIBLE);

    if (position%5==0)
      holder.rl_item_background.setBackgroundColor(Color.parseColor("#52738E"));
    else if (position%5==1)
      holder.rl_item_background.setBackgroundColor(Color.parseColor("#417FB1"));
    else if (position%5==2)
      holder.rl_item_background.setBackgroundColor(Color.parseColor("#1875BF"));
    else if (position%5==3)
      holder.rl_item_background.setBackgroundColor(Color.parseColor("#11578F"));
    else if (position%5==4)
      holder.rl_item_background.setBackgroundColor(Color.parseColor("#134368"));

    holder.tv_serial_no.setText(String.valueOf(position+1));
    holder.tv_assignment_topic_name.setText(receiveChapterList.get(position).getSubject_name());
    String strDate= UtilHelper.parseDateToddMMyyyy(receiveChapterList.get(position).getExam_date());
    holder.tv_assignment_date.setText("Exam date: " + strDate);

    String exam_end_date = receiveChapterList.get(position).getExam_date();
    String current_date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
    exam_status=compareDates(exam_end_date, current_date);
    holder.tv_assignment_start_time.setText("Start: " + receiveChapterList.get(position).getStart_time());
    holder.tv_assignment_end_time.setText("End: " + receiveChapterList.get(position).getEnd_time());

    // start checking to showing reply button

    if (exam_status.equals("Assignment not over")) {
      holder.btn_reply.setVisibility(View.VISIBLE);
      holder.btn_reply.setAlpha(1);
      holder.btn_reply.setEnabled(true);
      holder.btn_reply.setClickable(true);

      holder.btn_history.setVisibility(View.VISIBLE);
      holder.btn_history.setAlpha(1);
      holder.btn_history.setEnabled(true);
      holder.btn_history.setClickable(true);
    }
    else if (exam_status.equals("Assignment over"))
    {
      holder.btn_reply.setAlpha(.3f);
      holder.btn_reply.setEnabled(false);
      holder.btn_reply.setClickable(false);

      holder.btn_history.setAlpha(.3f);
      holder.btn_history.setEnabled(false);
      holder.btn_history.setClickable(false);
    }
    else {
        //start add timer
        updateTimer = new Timer();
        AssignmentTopicListActivity.timer_created=true;
        updateTimer.schedule(new TimerTask() {
          public void run() {
            try {
              Date currentTime = null, endDate = null;
              SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm:ss a");
              currentTime = dateFormat.parse(dateFormat.format(new Date()));
              endDate = dateFormat.parse(receiveChapterList.get(position).getEnd_time());
//              endDate = dateFormat.parse("03:25:00 PM");
              long mills = endDate.getTime() - currentTime.getTime();
              if (mills>1)
              {
                holder.btn_reply.setAlpha(1);
                holder.btn_reply.setEnabled(true);
                holder.btn_reply.setClickable(true);

                holder.btn_history.setAlpha(1);
                holder.btn_history.setEnabled(true);
                holder.btn_history.setClickable(true);
              }
              else {
                holder.btn_reply.setAlpha(.3f);
                holder.btn_reply.setEnabled(false);
                holder.btn_reply.setClickable(false);

                holder.btn_history.setAlpha(.3f);
                holder.btn_history.setEnabled(false);
                holder.btn_history.setClickable(false);
              }
            } catch (Exception e) {
              e.printStackTrace();
            }
          }

        }, 0, 1000); // here 1000 means 1000 mills i.e. 1 second

        //end add timer

    }

    // End checking to showing reply button

    holder.btn_view.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(receiveChapterList.get(position).getFile_url()));
        context.startActivity(i);

        /*Intent intent = new Intent(context, DriveDocumentsDownloadActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("file_url", receiveChapterList.get(position).getFile_url());
        intent.putExtras(bundle);
        context.startActivity(intent);*/
      }
    });

    holder.btn_reply.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        Intent intent = new Intent(context, AssignmentReplyActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("exam_id", receiveChapterList.get(position).getId());
        bundle.putString("exam_date", receiveChapterList.get(position).getExam_date());
        bundle.putString("end_time", receiveChapterList.get(position).getEnd_time());
        intent.putExtras(bundle);
        context.startActivity(intent);
      }
    });

    holder.btn_history.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent intent = new Intent(context, AssignmentHistoryListActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("exam_id", receiveChapterList.get(position).getId());
        bundle.putString("exam_date", receiveChapterList.get(position).getExam_date());
        bundle.putString("end_time", receiveChapterList.get(position).getEnd_time());
        intent.putExtras(bundle);
        context.startActivity(intent);
      }
    });

  }

  @Override
  public int getItemCount() {
    return receiveChapterList.size();
  }

  class PeriodViewHolder extends RecyclerView.ViewHolder{
    TextView tv_assignment_topic_name, tv_serial_no, tv_assignment_date, tv_assignment_start_time, tv_assignment_end_time;
    Button btn_view, btn_reply, btn_history;
    RelativeLayout rl_item_background;

    public PeriodViewHolder(@NonNull View itemView) {
      super(itemView);
      tv_assignment_topic_name = itemView.findViewById(R.id.tv_assignment_topic_name);
      btn_view = itemView.findViewById(R.id.btn_view);
      btn_reply = itemView.findViewById(R.id.btn_reply);
      tv_serial_no = itemView.findViewById(R.id.tv_serial_no);
      rl_item_background = itemView.findViewById(R.id.rl_item_background);
      tv_assignment_date = itemView.findViewById(R.id.tv_assignment_date);
      tv_assignment_start_time = itemView.findViewById(R.id.tv_assignment_start_time);
      tv_assignment_end_time = itemView.findViewById(R.id.tv_assignment_end_time);
      btn_history = itemView.findViewById(R.id.btn_history);
    }
  }

  public static String compareDates(String end_s,String today_s)
  {
    String s="";
    try{
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
      Date end_d = sdf.parse(end_s);
      Date today_d = sdf.parse(today_s);

      if(end_d.after(today_d)){
        s= "Assignment not over";
      }
      if(end_d.before(today_d)){
        s= "Assignment over";
      }
      if(end_d.equals(today_d)){
        s= "Assignment today";
      }
    }
    catch(ParseException ex){
      ex.printStackTrace();
    }
    return s;
  }
}
