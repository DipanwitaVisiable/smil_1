package com.example.crypedu.Adapter;

import android.content.Context;
import android.graphics.Color;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.activity.smi.R;
import com.example.crypedu.Pojo.AnswerSolutionGetSet;

import java.util.ArrayList;
import java.util.List;

public class SmallQuizSolutionAnswerAdapter extends RecyclerView.Adapter<SmallQuizSolutionAnswerAdapter.soluionViewHolder> {
    private Context context;
    private List<AnswerSolutionGetSet> ansSolutionList;
    private String question,questionId, student_answer_id;

    public SmallQuizSolutionAnswerAdapter(Context context,
                                          ArrayList<AnswerSolutionGetSet> ansSolutionList,
                                          String question, String questionId, String student_answer_id) {
        this.context = context;
        this.ansSolutionList = ansSolutionList;
        this.question = question;
        this.questionId = questionId;
        this.student_answer_id = student_answer_id;
    }

    @NonNull
    @Override
    public SmallQuizSolutionAnswerAdapter.soluionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        View view = LayoutInflater.from(context).inflate(R.layout.item_small_quiz_solution_answer, parent, false);
        return new SmallQuizSolutionAnswerAdapter.soluionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SmallQuizSolutionAnswerAdapter.soluionViewHolder viewHolder, int position) {
        int no=position+1;
        viewHolder.tv_ans_no.setText("("+(char)(no+'A'-1)+ ")");
        viewHolder.ans_body.loadDataWithBaseURL(null, ansSolutionList.get(position).getAnswer(), "text/html", "utf-8", null);

        if (student_answer_id.equals(ansSolutionList.get(position).getAnswer_id()) &&
                ansSolutionList.get(position).getAnswer_status().equals("1"))
        {
            viewHolder.card_ans.setBackground(context.getResources().getDrawable( R.drawable.answer_right_selector));
            viewHolder.ans_body.setBackgroundColor(Color.parseColor("#D6FFD7"));
        }
        else if (student_answer_id.equals(ansSolutionList.get(position).getAnswer_id()) &&
                ansSolutionList.get(position).getAnswer_status().equals("0"))
        {
            viewHolder.card_ans.setBackground(context.getResources().getDrawable( R.drawable.answer_wrong_selector));
            viewHolder.ans_body.setBackgroundColor(Color.parseColor("#FFD5D5"));
        }
        else if (!student_answer_id.equals(ansSolutionList.get(position).getAnswer_id()) &&
                ansSolutionList.get(position).getAnswer_status().equals("1"))
        {
            viewHolder.card_ans.setBackground(context.getResources().getDrawable( R.drawable.answer_right_selector));
            viewHolder.ans_body.setBackgroundColor(Color.parseColor("#D6FFD7"));
        }
        else {
            viewHolder.card_ans.setBackground(context.getResources().getDrawable( R.drawable.b));
            viewHolder.ans_body.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }


        /*if (ansSolutionList.get(position).getAnswer_status().equalsIgnoreCase("1")){
            viewHolder.card_ans.setBackground(context.getResources().getDrawable( R.drawable.answer_right_selector));
            viewHolder.ans_body.setBackgroundColor(Color.parseColor("#D6FFD7"));

        }else if (ansSolutionList.get(position).getStudent_answer().equalsIgnoreCase("2")){
            viewHolder.card_ans.setBackground(context.getResources().getDrawable( R.drawable.answer_wrong_selector));
            viewHolder.ans_body.setBackgroundColor(Color.parseColor("#FFD5D5"));

        }else if (ansSolutionList.get(position).getStudent_answer().equalsIgnoreCase("1")){

            viewHolder.card_ans.setBackground(context.getResources().getDrawable( R.drawable.answer_right_selector));
            viewHolder.ans_body.setBackgroundColor(Color.parseColor("#D6FFD7"));
        }else {

            viewHolder.card_ans.setBackground(context.getResources().getDrawable( R.drawable.b));
            viewHolder.ans_body.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }*/

    }

    @Override
    public int getItemCount() {
        return  ansSolutionList.size();
    }

    class soluionViewHolder extends RecyclerView.ViewHolder{
        LinearLayout rl_ans;
        TextView tv_ans_no/*,ans_body*/;
        WebView ans_body;
        CardView card_ans;
        public soluionViewHolder(@NonNull View itemView) {
            super(itemView);
            rl_ans=itemView.findViewById(R.id.rl_ans);
            tv_ans_no = itemView.findViewById(R.id.tv_ans_no);
            ans_body = itemView.findViewById(R.id.ans_body);
            card_ans = itemView.findViewById(R.id.card_ans);
        }
    }
}
