package com.example.crypedu.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;

import com.activity.smi.R;
import com.example.crypedu.Constants.Constants;
import com.example.crypedu.Pojo.AnswerTable;
import com.example.crypedu.Pojo.QuestionTable;

import java.util.ArrayList;
import java.util.HashMap;

public class OnlineWrittenTestQuestionAdapter extends PagerAdapter {
  Context context;
  private ArrayList<QuestionTable> quesList;
  private LayoutInflater inflater;
  private HashMap<String, ArrayList<AnswerTable>> listHashMap;
  private Button submitButton;

  public OnlineWrittenTestQuestionAdapter(Context context, ArrayList<QuestionTable> quesList,
                                  HashMap<String, ArrayList<AnswerTable>> listHashMap, Button submitButton) {
    this.context = context;
    this.quesList = quesList;
    this.listHashMap = listHashMap;
    this.submitButton = submitButton;
    inflater = LayoutInflater.from(context);
  }

  @Override
  public int getCount() {
    return listHashMap.size();
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

  @SuppressLint("SetTextI18n")
  @NonNull
  @Override
  public Object instantiateItem(@NonNull ViewGroup view, final int position) {

    View myView = inflater.inflate(R.layout.item_written_test_question_adapter, view, false);
    TextView question_no = myView.findViewById(R.id.question_no);
    WebView question_body = myView.findViewById(R.id.question_body);
    RecyclerView ans_listView = myView.findViewById(R.id.ans_listView);
    final EditText et_written_ans = myView.findViewById(R.id.et_written_ans);
    final TextView tv_word_limit = myView.findViewById(R.id.tv_word_limit);
    int no = position + 1;
    Constants.QUES_NO = no;
    question_no.setText("Q. " + no);
    SmallQuizAnswerAdapter answerAdapter;

    final int maximum_character=10;
    et_written_ans.setFilters(new InputFilter[] {new InputFilter.LengthFilter(maximum_character)});
    et_written_ans.addTextChangedListener(new TextWatcher() {

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
        tv_word_limit.setText(" "+String.valueOf(maximum_character - et_written_ans.getText().length()) + "/" + maximum_character);
      }

      @Override
      public void beforeTextChanged(CharSequence s, int start, int count,
                                    int after) {
        // TODO Auto-generated method stub

      }

      @Override
      public void afterTextChanged(Editable s) {
        // TODO Auto-generated method stub

      }
    });

    /*if (Constants.answerCheckHash.size() > 0 && Constants.answerCheckHash.containsKey(quesList.get(position).getQuestionId())) {
      answerAdapter = new SmallQuizAnswerAdapter(context, listHashMap.get(quesList.get(position).getQuestionId()), quesList.get(position).getQuestion(), quesList.get(position).getQuestionId(), submitButton, Integer.parseInt(Constants.answerCheckHash.get(quesList.get(position).getQuestionId())));
    } else {
      answerAdapter = new SmallQuizAnswerAdapter(context, listHashMap.get(quesList.get(position).getQuestionId()), quesList.get(position).getQuestion(), quesList.get(position).getQuestionId(), submitButton, -1);
    }*/
    if (Constants.answerCheckHash.size() > 0 && Constants.answerCheckHash.containsKey(quesList.get(position).getQuestionId())) {
      if (listHashMap.get(quesList.get(position).getQuestionId()).size()==0)
      {
        ans_listView.setVisibility(View.GONE);
        et_written_ans.setVisibility(View.VISIBLE);
        tv_word_limit.setVisibility(View.VISIBLE);
      }
      else {
        ans_listView.setVisibility(View.VISIBLE);
        et_written_ans.setVisibility(View.GONE);
        tv_word_limit.setVisibility(View.GONE);
        answerAdapter = new SmallQuizAnswerAdapter(context, listHashMap.get(quesList.get(position).getQuestionId()), quesList.get(position).getQuestion(), quesList.get(position).getQuestionId(), submitButton, Integer.parseInt(Constants.answerCheckHash.get(quesList.get(position).getQuestionId())));
        ans_listView.setLayoutManager(new LinearLayoutManager(context ));
        ans_listView.setAdapter(answerAdapter);
      }
    } else {
      if (listHashMap.get(quesList.get(position).getQuestionId()).size()==0)
      {
        ans_listView.setVisibility(View.GONE);
        et_written_ans.setVisibility(View.VISIBLE);
        tv_word_limit.setVisibility(View.VISIBLE);
      }
      else {
        ans_listView.setVisibility(View.VISIBLE);
        et_written_ans.setVisibility(View.GONE);
        tv_word_limit.setVisibility(View.GONE);
        answerAdapter = new SmallQuizAnswerAdapter(context, listHashMap.get(quesList.get(position).getQuestionId()), quesList.get(position).getQuestion(), quesList.get(position).getQuestionId(), submitButton, -1);
        ans_listView.setLayoutManager(new LinearLayoutManager(context ));
        ans_listView.setAdapter(answerAdapter);
      }
      }
    question_body.loadDataWithBaseURL(null, quesList.get(position).getQuestion(), "text/html", "utf-8", null);

    /*ans_listView.setLayoutManager(new LinearLayoutManager(context ));
    ans_listView.setAdapter(answerAdapter);*/
    view.addView(myView);
    return myView;
  }


}
