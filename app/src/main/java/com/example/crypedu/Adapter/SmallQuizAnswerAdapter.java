package com.example.crypedu.Adapter;

import android.content.Context;
import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.activity.smi.R;
import com.example.crypedu.Constants.Constants;
import com.example.crypedu.Pojo.AnswerTable;
import com.example.crypedu.Pojo.QuestionTable;

import java.util.ArrayList;
import java.util.List;


public class SmallQuizAnswerAdapter extends RecyclerView.Adapter<SmallQuizAnswerAdapter.ansViewHolder> {
    private Context context;
    private List<AnswerTable> ansList;

    private int selectedPosition = -1;
    private Button btn_save_next;
    private String question, questionId;
//    private ArrayList<AnswerTable> answerList;
    public SmallQuizAnswerAdapter(Context context, List<AnswerTable> ansList, String question, String questionId, Button btn_save_next, Integer selectedPosition) {

        this.context = context;
        this.ansList = ansList;
        this.btn_save_next = btn_save_next;
        this.question = question;
        this.questionId = questionId;
        if (selectedPosition == -1){

        }else {
            this.selectedPosition = selectedPosition;
        }
        Constants.testquestionAnswerStoreHash.put(question, ansList);
    }

  public SmallQuizAnswerAdapter(Context context, String questionId, String question, ArrayList<AnswerTable> ansList, Button btn_save_next) {

    this.context = context;
    this.questionId = questionId;
    this.question = question;
    this.ansList = ansList;
    this.btn_save_next = btn_save_next;
  }

    @NonNull
    @Override
    public SmallQuizAnswerAdapter.ansViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        View view = inflater.inflate(R.layout.item_small_quiz_answer_adapter, parent, false);
        return new SmallQuizAnswerAdapter.ansViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final SmallQuizAnswerAdapter.ansViewHolder viewHolder, final int position) {

        final int no=position+1;
        viewHolder.tv_ans_no.setText("("+(char)(no+'A'-1)+ ")");
        viewHolder.ans_body.loadDataWithBaseURL(null, ansList.get(position).getAnswer(), "text/html", "utf-8", null);

        final int finalPosition = position;
        viewHolder.ll_ans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectOption(position);
            }
        });
        viewHolder.card_ans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectOption(position);
            }
        });
        viewHolder.tv_ans_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectOption(position);
            }
        });
        // start code to Detect web View click event

        viewHolder.ans_body.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        viewHolder.ans_body.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                /*// TODO Auto-generated method stub
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //Do something after 100ms
                        selectOption(position);
                    }
                }, 50);

//                selectOption(position);*/
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    selectOption(position);
                }

                return false;
            }
        });
        //End
        if (ansList.get(position).isSelected()) {
            viewHolder.card_ans.setBackground(context.getResources().getDrawable(R.drawable.a));
//            viewHolder.ans_body.setBackgroundColor(Color.parseColor("#E2F0FF"));
            viewHolder.ans_body.setBackgroundColor(Color.parseColor("#94C5F8"));

        } else {
            viewHolder.card_ans.setBackground(context.getResources().getDrawable(R.drawable.b));
            viewHolder.ans_body.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }
    }

    private void selectOption(int position) {
        for (AnswerTable answerDetailsSchema : ansList) {
            answerDetailsSchema.setSelected(false);
        }
        ansList.get(position).setSelected(true);
        Constants.CHOOSE_QUESTION_ID = questionId;
        Constants.ANSWER_ID = ansList.get(position).getAnswerId();
        btn_save_next.setVisibility(View.VISIBLE);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return ansList.size();
    }
    class ansViewHolder extends RecyclerView.ViewHolder{

        private TextView tv_ans_no;
        private WebView ans_body;
        private LinearLayout ll_ans;
        private CardView card_ans;

        public ansViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_ans_no = itemView.findViewById(R.id.tv_ans_no);
            ans_body = itemView.findViewById(R.id.ans_body);
            ll_ans = itemView.findViewById(R.id.ll_ans);
            card_ans = itemView.findViewById(R.id.card_ans);
        }
    }

    //On selecting any view set the current position to selectedPosition and notify adapter
    private void itemCheckChanged(View v) {
        selectedPosition = (Integer) v.getTag();
        notifyDataSetChanged();
        Constants.CHOOSE_QUESTION_ID = questionId;
        Constants.ANSWER_ID = ansList.get(selectedPosition).getAnswerId();
        btn_save_next.setVisibility(View.VISIBLE);
        Constants.answerStoreHash.put(questionId, ansList.get(selectedPosition).getAnswer());
        Constants.answerCheckHash.put(questionId, String.valueOf(selectedPosition));
        Constants.answerQuestionStoreHash.put(question, ansList.get(selectedPosition).getAnswer());

    }
}
