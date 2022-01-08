package com.example.crypedu.Adapter;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import androidx.viewpager.widget.PagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.activity.smi.R;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.crypedu.Constants.Constants;
import com.example.crypedu.Pojo.AnswerTable;
import com.example.crypedu.Pojo.QuestionTable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class SmallQuizQuestionAdapter extends PagerAdapter {
    Context context;
    private ArrayList<QuestionTable> quesList;
    private LayoutInflater inflater;
    private HashMap<String, ArrayList<AnswerTable>> listHashMap;
    private Button submitButton;
    private String exam_ques;
    private String single_ques;
    private RequestQueue requestQueue;

    public SmallQuizQuestionAdapter(Context context, ArrayList<QuestionTable> quesList,
                                    HashMap<String, ArrayList<AnswerTable>> listHashMap, Button submitButton) {
        this.context = context;
        this.quesList = quesList;
        this.listHashMap = listHashMap;
        this.submitButton = submitButton;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
//        return listHashMap.size();
        return quesList.size();
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

    @SuppressLint({"SetTextI18n", "WrongConstant"})
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup view, final int position) {

        View myView = inflater.inflate(R.layout.item_small_quiz_question_adapter, view, false);
        TextView question_no = myView.findViewById(R.id.question_no);
        final WebView question_body = myView.findViewById(R.id.question_body);
        NestedScrollView nsv_holder = myView.findViewById(R.id.nsv_holder);
        LinearLayout ll_ques = myView.findViewById(R.id.ll_ques);
        RecyclerView ans_listView = myView.findViewById(R.id.ans_listView);
        RelativeLayout rl_background = myView.findViewById(R.id.rl_background);



        //This below code used for changing text color of web view
        question_body.setBackgroundColor(Color.TRANSPARENT);
        String message = "<html><head>"
                + "<style type=\"text/css\">body{color: #fff;}"
                + "</style></head>"
                + "<body>"
                + quesList.get(position).getQuestion()
                + "</body></html>";
        GradientDrawable gd = new GradientDrawable(
                GradientDrawable.Orientation.LEFT_RIGHT,
                new int[] {Color.parseColor("#135080"),Color.parseColor("#7cbaeb")});
        gd.setCornerRadius(0f);
        nsv_holder.setBackgroundDrawable(gd);

        question_body.setBackgroundDrawable(gd);
        ll_ques.setBackgroundDrawable(gd);
        ans_listView.setBackgroundDrawable(gd);
        rl_background.setBackgroundDrawable(gd);

        int no = position + 1;
        Constants.QUES_NO = no;
        question_no.setText("Q. " + no);
        SmallQuizAnswerAdapter answerAdapter;
        /*if (Constants.answerCheckHash.size() > 0 && Constants.answerCheckHash.containsKey(quesList.get(position).getQuestionId())) {
            answerAdapter = new SmallQuizAnswerAdapter(context, listHashMap.get(quesList.get(position).getQuestionId()), quesList.get(position).getQuestion(), quesList.get(position).getQuestionId(), submitButton, Integer.parseInt(Constants.answerCheckHash.get(quesList.get(position).getQuestionId())));
        } else {
            answerAdapter = new SmallQuizAnswerAdapter(context, listHashMap.get(quesList.get(position).getQuestionId()), quesList.get(position).getQuestion(), quesList.get(position).getQuestionId(), submitButton, -1);
        }*/

      answerAdapter = new SmallQuizAnswerAdapter(context, quesList.get(position).getQuestionId(), quesList.get(position).getQuestion(), quesList.get(position).getAnswerDetails(), submitButton);

      question_body.loadDataWithBaseURL(null, message, "text/html", "utf-8", null);
        ans_listView.setLayoutManager(new LinearLayoutManager(context, LinearLayout.VERTICAL, false));
        ans_listView.setAdapter(answerAdapter);
        view.addView(myView);
        return myView;
    }

}
