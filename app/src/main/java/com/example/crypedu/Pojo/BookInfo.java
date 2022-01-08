package com.example.crypedu.Pojo;
/*
 *************************************************************************************************
 * Developed by Cryptonsoftech
 * Date: 30-05-2017
 * Details:
 * ***********************************************************************************************
 */
import android.os.Parcel;
import android.os.Parcelable;

public class BookInfo implements Parcelable {
    private String bookId;
    private String bookTitle;
    private String bookAuthor;
    private String bookPublisher;
    private String bookEdition;
    private String bookSubjectId;
    private String bookPrice;
    private String bookIsbnNumber;
    private String bookShortDescription;

    public BookInfo(String bookId, String bookTitle, String bookAuthor, String bookPublisher, String bookEdition,
                    String bookSubjectId, String bookPrice, String bookIsbnNumber, String bookShortDescription){
        this.bookId = bookId;
        this.bookTitle = bookTitle;
        this.bookAuthor = bookAuthor;
        this.bookPublisher = bookPublisher;
        this.bookEdition = bookEdition;
        this.bookSubjectId = bookSubjectId;
        this.bookPrice = bookPrice;
        this.bookIsbnNumber = bookIsbnNumber;
        this.bookShortDescription = bookShortDescription;
    }

    public BookInfo(Parcel in){
        this.bookId = in.readString();
        this.bookTitle = in.readString();
        this.bookAuthor = in.readString();
        this.bookPublisher = in.readString();
        this.bookEdition = in.readString();
        this.bookSubjectId = in.readString();
        this.bookPrice = in.readString();
        this.bookIsbnNumber = in.readString();
        this.bookShortDescription = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(bookId);
        dest.writeString(bookTitle);
        dest.writeString(bookAuthor);
        dest.writeString(bookPublisher);
        dest.writeString(bookEdition);
        dest.writeString(bookSubjectId);
        dest.writeString(bookPrice);
        dest.writeString(bookIsbnNumber);
        dest.writeString(bookShortDescription);
    }

    public static final Creator<SubjectInfo> CREATOR = new Creator<SubjectInfo>() {
        public SubjectInfo createFromParcel(Parcel in) {
            return new SubjectInfo(in);
        }

        public SubjectInfo[] newArray(int size) {
            return new SubjectInfo[size];

        }
    };
}

