package com.example.crypedu.Pojo;




import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


//@Entity(tableName = "answers", primaryKeys = {"question_id", "quiz_id", "answer_id"})
public class AnswerTable implements Serializable {

    @SerializedName("answer_id")
    @Expose
//    @ColumnInfo(name = "answer_id")
//    @NonNull
    private String answerId;


    @SerializedName("question_id")
    @Expose
//    @ColumnInfo(name = "question_id")
//    @NonNull
    private String questionId;

    @SerializedName("quiz_id")
    @Expose
//    @ColumnInfo(name = "quiz_id")
//    @NonNull
    private String quizId;

    @SerializedName("answer")
    @Expose
    private String answer;

    @SerializedName("ans_stat")
    @Expose
    private String ans_stat;

    //For local purpose
//    @ColumnInfo(name = "is_selected")
    @SerializedName("is_selected")
    @Expose
    private boolean isSelected;

    @NonNull
    public String getAnswerId() {
        return answerId;
    }

    public void setAnswerId(@NonNull String answerId) {
        this.answerId = answerId;
    }

    @NonNull
    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(@NonNull String questionId) {
        this.questionId = questionId;
    }

    @NonNull
    public String getQuizId() {
        return quizId;
    }

    public void setQuizId(@NonNull String quizId) {
        this.quizId = quizId;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getAns_stat() {
        return ans_stat;
    }

    public void setAns_stat(String ans_stat) {
        this.ans_stat = ans_stat;
    }
//For local purpose

}
