package com.example.crypedu.Pojo;



import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class QuestionTable implements Serializable {

    @SerializedName("question_id")
    @Expose
    private String questionId;

    @SerializedName("quiz_id")
    @Expose
    private String quizId;


    @SerializedName("directions")
    @Expose
    private String directions;

    @SerializedName("question")
    @Expose
    private String question;

    @SerializedName("ans_arr")
    @Expose
    private ArrayList<AnswerTable> answerDetails;

    @SerializedName("is_marked")
    @Expose
    private boolean isMarked;

    @SerializedName("is_visited")
    @Expose
    private boolean isVisited;

    @SerializedName("is_answered")
    @Expose
    private boolean isAnswered;

    @SerializedName("exam_taken_id")
    @Expose
    private boolean exam_taken_id;

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

    public String getDirections() {
        return directions;
    }

    public void setDirections(String directions) {
        this.directions = directions;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public ArrayList<AnswerTable> getAnswerDetails() {
        return answerDetails;
    }

    public void setAnswerDetails(ArrayList<AnswerTable> answerDetails) {
        this.answerDetails = answerDetails;
    }

    public boolean isMarked() {
        return isMarked;
    }

    public void setMarked(boolean marked) {
        isMarked = marked;
    }

    public boolean isVisited() {
        return isVisited;
    }

    public void setVisited(boolean visited) {
        isVisited = visited;
    }

    public boolean isAnswered() {
        return isAnswered;
    }

    public void setAnswered(boolean answered) {
        isAnswered = answered;
    }

    public boolean isExam_taken_id() {
        return exam_taken_id;
    }

    public void setExam_taken_id(boolean exam_taken_id) {
        this.exam_taken_id = exam_taken_id;
    }
}
