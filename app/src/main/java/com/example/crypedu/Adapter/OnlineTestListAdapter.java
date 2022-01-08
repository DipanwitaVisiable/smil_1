package com.example.crypedu.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.activity.smi.R;
import com.example.crypedu.Activity.OnlineTestResultActivity;
import com.example.crypedu.Activity.SingleOnlineTestActivity;
import com.example.crypedu.Activity.SmallQuizTestActivity;
import com.example.crypedu.Helper.UtilHelper;
import com.example.crypedu.Pojo.OnlineTestInfo;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class OnlineTestListAdapter extends RecyclerView.Adapter<OnlineTestListAdapter.OnlineTestViewHolder> {

    Context context;
    ArrayList<OnlineTestInfo> receiveTestList;
    public OnlineTestListAdapter(Context context, ArrayList<OnlineTestInfo> receiveTestList){

        this.context = context;
        this.receiveTestList = receiveTestList;

    }

    @NonNull
    @Override
    public OnlineTestListAdapter.OnlineTestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        View view = inflater.inflate(R.layout.item_of_small_quiz_list_adapter, parent, false);
        return new OnlineTestListAdapter.OnlineTestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final OnlineTestListAdapter.OnlineTestViewHolder holder, final int position) {

        holder.tv_quiz_name.setText("Examination: " + receiveTestList.get(position).getExam_name());
//        holder.tv_class_name.setText("Class Name: " + receiveTestList.get(position).getClass_or_year());
        holder.tv_subject_name.setText("Subject Name: " +receiveTestList.get(position).getSubject_name());
        holder.tv_duration.setText("Time: " +receiveTestList.get(position).getTot_time());
        holder.tv_total_question.setText("Total Question: " +receiveTestList.get(position).getTot_qus());


        String date_of_exam= UtilHelper.parseDateToddMMyyyy(receiveTestList.get(position).getExam_date());
        holder.tv_test_date.setText("Exam Date: " + date_of_exam);

        /*holder.btn_start_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (holder.btn_start_test.getText().toString().trim().equals("Start Test")) {
                    final Dialog dialog = new Dialog(context);
                    dialog.setContentView(R.layout.quiz_dialog_view);
                    dialog.setCancelable(true);
                    Button noB = dialog.findViewById(R.id.noButton);
                    Button yesB = dialog.findViewById(R.id.yesButton);
                    TextView textViewTopic = dialog.findViewById(R.id.testTopic);
                    TextView questions = dialog.findViewById(R.id.testQuestions);
                    TextView duration = dialog.findViewById(R.id.duration);
                    textViewTopic.setText(receiveTestList.get(position).getExam_name());
                    questions.setText(receiveTestList.get(position).getTot_qus());
                    duration.setText(receiveTestList.get(position).getTot_time() + " min");
                    noB.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                    yesB.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            Intent intent = new Intent(context, SmallQuizTestActivity.class);
                            intent.putExtra("quiz_id", receiveTestList.get(position).getExam_id());
                            intent.putExtra("time", receiveTestList.get(position).getTot_time());
                            context.startActivity(intent);

                        }
                    });
                    dialog.show();
                }
                 else if (holder.btn_start_test.getText().toString().trim().equals("View Result"))
                    {
                        Intent intent = new Intent(context, OnlineTestResultActivity.class);
                        intent.putExtra("exam_taken_id", receiveTestList.get(position).getExam_taken_id());
                        intent.putExtra("tot_qus", receiveTestList.get(position).getTot_qus());
                        intent.putExtra("exam_id", receiveTestList.get(position).getExam_id());
                        context.startActivity(intent);

                    }
            }
        });*/

        holder.btn_view_result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, OnlineTestResultActivity.class);
                intent.putExtra("exam_taken_id", receiveTestList.get(position).getExam_taken_id());
                intent.putExtra("tot_qus", receiveTestList.get(position).getTot_qus());
                intent.putExtra("exam_id", receiveTestList.get(position).getExam_id());
                context.startActivity(intent);
            }
        });
        holder.btn_view_exam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, SingleOnlineTestActivity.class);
                intent.putExtra("page_link", receiveTestList.get(position).getChapter_name());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
//        return 1;
        return receiveTestList.size();
    }

    class OnlineTestViewHolder extends RecyclerView.ViewHolder{
        TextView tv_quiz_name, tv_duration, tv_total_question, tv_subject_name, tv_test_date, tv_class_name, tv_test_marks;
        Button btn_view_result, btn_view_exam ;
        WebView wv_instruction;

        public OnlineTestViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_quiz_name = itemView.findViewById(R.id.tv_quiz_name);
            tv_duration = itemView.findViewById(R.id.tv_time);
            tv_total_question = itemView.findViewById(R.id.tv_total_question);
            tv_subject_name = itemView.findViewById(R.id.tv_subject_name);
            tv_test_date = itemView.findViewById(R.id.tv_test_date);
            btn_view_result = itemView.findViewById(R.id.btn_view_result);
            btn_view_exam = itemView.findViewById(R.id.btn_view_exam);
        }
    }
}
