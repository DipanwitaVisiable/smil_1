package com.example.crypedu.Adapter;
/*
 *************************************************************************************************
 * Developed by Cryptonsoftech
 * Date: 30-05-2017
 * Details:
 * ***********************************************************************************************
 */
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import com.activity.smi.R;
import com.example.crypedu.Constants.Constants;
import com.example.crypedu.Pojo.LibraryBookInfo;
import java.util.List;

public class BookAdapter extends BaseAdapter {
    private final Context context;
    private final List<LibraryBookInfo> bookInfoList;
    LayoutInflater mInflater;
    LayoutInflater inflater;

    public BookAdapter(Context context, List<LibraryBookInfo> bookInfos, LayoutInflater inflater) {
        mInflater = inflater;
        this.context = context;
        this.bookInfoList = bookInfos;
    }

    @Override
    public int getCount() {
        return bookInfoList.size();
    }

    @Override
    public Object getItem(int position) {
        return bookInfoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View itemView;
        assert inflater != null;
        itemView = inflater.inflate(R.layout.book_adapter, parent, false);
        Button book_button = itemView.findViewById(R.id.book_button);

        Typeface typeface = Typeface.createFromAsset(itemView.getContext().getAssets(), Constants.BubblegumSans_Regular_font);

        final LibraryBookInfo libraryBookInfo = bookInfoList.get(position);
        book_button.setText(Html.fromHtml("<h2><u>"+ libraryBookInfo.bookTitle +"</u></h2>" +
                "<p>"+ libraryBookInfo.bookAuthor +"<br>"+ libraryBookInfo.bookPublisher +"" +
                "<br>"+ libraryBookInfo.bookEdition +"<br>" + libraryBookInfo.bookIssuedDate +
                "<br>"+ libraryBookInfo.bookIssuedDays +"<br>" + libraryBookInfo.bookReturnDate +
                "</p>"+ libraryBookInfo.bookStatus));
        book_button.setTypeface(typeface);

        return itemView;
    }
}