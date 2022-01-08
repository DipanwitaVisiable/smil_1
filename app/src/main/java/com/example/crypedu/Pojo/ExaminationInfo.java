package com.example.crypedu.Pojo;


import android.os.Parcel;
import android.os.Parcelable;
/**
 * Created by Chandan on 6/20/2018.
 */
public class ExaminationInfo implements Parcelable {
    public String subject;
    public String exam_name;
    public String time;
    public String examDate;

    public ExaminationInfo(String subject, String exam_name, String time, String examDate){
        this.subject = subject;
        this.exam_name = exam_name;
        this.time = time;
        this.examDate = examDate;
    }

    private ExaminationInfo(Parcel in){
        this.subject = in.readString();
        this.exam_name = in.readString();
        this.time = in.readString();
        this.examDate = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(subject);
        dest.writeString(exam_name);
        dest.writeString(time);
        dest.writeString(examDate);
    }

    public static final Creator<ExaminationInfo> CREATOR = new Creator<ExaminationInfo>() {
        public ExaminationInfo createFromParcel(Parcel in) {
            return new ExaminationInfo(in);
        }

        public ExaminationInfo[] newArray(int size) {
            return new ExaminationInfo[size];

        }
    };
}

