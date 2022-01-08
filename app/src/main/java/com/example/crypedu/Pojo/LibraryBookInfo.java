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

public class LibraryBookInfo implements Parcelable {
    private String bookId;
    public String bookTitle;
    public String bookAuthor;
    public String bookPublisher;
    public String bookEdition;
    public String bookIssuedDate;
    public String bookIssuedDays;
    public String bookReturnDate;
    public String bookStatus;

    public LibraryBookInfo(String bookId, String bookTitle, String bookAuthor, String bookPublisher, String bookEdition,
                           String bookIssuedDate, String bookIssuedDays, String bookReturnDate, String bookStatus){
        this.bookId = bookId;
        this.bookTitle = bookTitle;
        this.bookAuthor = bookAuthor;
        this.bookPublisher = bookPublisher;
        this.bookEdition = bookEdition;
        this.bookIssuedDate = bookIssuedDate;
        this.bookIssuedDays = bookIssuedDays;
        this.bookReturnDate = bookReturnDate;
        this.bookStatus = bookStatus;
    }

    private LibraryBookInfo(Parcel in){
        this.bookId = in.readString();
        this.bookTitle = in.readString();
        this.bookAuthor = in.readString();
        this.bookPublisher = in.readString();
        this.bookEdition = in.readString();
        this.bookIssuedDate = in.readString();
        this.bookIssuedDays = in.readString();
        this.bookReturnDate = in.readString();
        this.bookStatus = in.readString();
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
        dest.writeString(bookIssuedDate);
        dest.writeString(bookIssuedDays);
        dest.writeString(bookReturnDate);
        dest.writeString(bookStatus);
    }

    public static final Creator<LibraryBookInfo> CREATOR = new Creator<LibraryBookInfo>() {
        public LibraryBookInfo createFromParcel(Parcel in) {
            return new LibraryBookInfo(in);
        }

        public LibraryBookInfo[] newArray(int size) {
            return new LibraryBookInfo[size];

        }
    };
}


