package com.example.crypedu.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.activity.smi.R;
import com.example.crypedu.Constants.Constants;
import com.example.crypedu.Pojo.AnswerSolutionGetSet;
import com.example.crypedu.Pojo.QuestionSolutionGetSet;

import java.util.ArrayList;
import java.util.HashMap;

public class SmallQuizSolutionQuestionAdapter extends PagerAdapter {
    Context context;
    ArrayList<QuestionSolutionGetSet> quesSolutionList;
    private LayoutInflater inflater;
    HashMap<String,ArrayList<AnswerSolutionGetSet>> solutionListHashMap;

    public SmallQuizSolutionQuestionAdapter(Context context, ArrayList<QuestionSolutionGetSet> quesSolutionList, HashMap<String,ArrayList<AnswerSolutionGetSet>> solutionListHashMap) {
        this.context = context;
        this.quesSolutionList = quesSolutionList;
        this.solutionListHashMap = solutionListHashMap;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return solutionListHashMap.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }
    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getItemPosition(@NonNull Object object) {

        return POSITION_NONE;

    }

    @SuppressLint("WrongConstant")
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup view, int position) {
        View myView = inflater.inflate(R.layout.item_small_quiz_solution_question, view, false);
        TextView question_no = myView.findViewById(R.id.question_no);
        WebView question_body = myView.findViewById(R.id.question_body);
        WebView tv_solution=myView.findViewById(R.id.tv_solution);
        NestedScrollView nsv_holder=myView.findViewById(R.id.nsv_holder);
        int no = position + 1;
        Constants.QUES_NO = no;
        question_no.setText("Q. " + no);
        SmallQuizSolutionAnswerAdapter solutionAnswerAdapter;
        solutionAnswerAdapter=new SmallQuizSolutionAnswerAdapter(context, solutionListHashMap.get(quesSolutionList.get(position).getQuestion_id()), quesSolutionList.get(position).getQuestion(), quesSolutionList.get(position).getQuestion_id(), quesSolutionList.get(position).getStudent_answer_id());


        //This below code used for changing text color of web view
        question_body.setBackgroundColor(Color.TRANSPARENT);
        tv_solution.setBackgroundColor(Color.TRANSPARENT);
        String ques = "<html><head>"
                + "<style type=\"text/css\">body{color: #fff;}"
                + "</style></head>"
                + "<body>"
                + quesSolutionList.get(position).getQuestion()
                + "</body></html>";

        String solution = "<html><head>"
                + "<style type=\"text/css\">body{color: #fff;}"
                + "</style></head>"
                + "<body>"
                + quesSolutionList.get(position).getSolution()
                + "</body></html>";

        GradientDrawable gd = new GradientDrawable(
                GradientDrawable.Orientation.LEFT_RIGHT,
                new int[] {Color.parseColor("#135080"),Color.parseColor("#7cbaeb")});
        gd.setCornerRadius(0f);
        nsv_holder.setBackgroundDrawable(gd);
        question_body.setBackgroundDrawable(gd);
        tv_solution.setBackgroundDrawable(gd);


        if (quesSolutionList.get(position).getSolution()!=null && !quesSolutionList.get(position).getSolution().equalsIgnoreCase("")){
            tv_solution.setVisibility(View.VISIBLE);
//            tv_solution.loadDataWithBaseURL(null, "Solution:\n"+quesSolutionList.get(position).getSolution(), "text/html", "utf-8", null);
            tv_solution.loadDataWithBaseURL(null, "Solution:\n" + solution, "text/html", "utf-8", null);
        }else {
            tv_solution.setVisibility(View.GONE);
        }
//        question_body.loadDataWithBaseURL(null, quesSolutionList.get(position).getQuestion(), "text/html", "utf-8", null);
        question_body.loadDataWithBaseURL(null, ques, "text/html", "utf-8", null);


        RecyclerView ans_listView = myView.findViewById(R.id.ans_listView);
        ans_listView.setLayoutManager(new LinearLayoutManager(context, LinearLayout.VERTICAL, false ));
        ans_listView.setAdapter(solutionAnswerAdapter);
        view.addView(myView);
        return myView;

    }
}
