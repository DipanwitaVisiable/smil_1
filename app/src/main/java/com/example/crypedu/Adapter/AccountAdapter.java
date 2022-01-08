package com.example.crypedu.Adapter;

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
import com.example.crypedu.Pojo.AccountInfo;

import java.util.List;

/**
 * Created by sudipta on 08-12-2017.
 */

public class AccountAdapter extends BaseAdapter {
    private final Context context;
    private final List<AccountInfo> accountInfoList;
    LayoutInflater mInflater;
    LayoutInflater inflater;

    public AccountAdapter(Context context, List<AccountInfo> accountInfoList, LayoutInflater inflater) {
        mInflater = inflater;
        this.context = context;
        this.accountInfoList = accountInfoList;
    }

    @Override
    public int getCount() {
        return accountInfoList.size();
    }

    @Override
    public Object getItem(int position) {
        return accountInfoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View itemView;
        assert inflater != null;
        itemView = inflater.inflate(R.layout.account_adapter, parent, false);
        Button account_button = itemView.findViewById(R.id.account_button);

        Typeface typeface = Typeface.createFromAsset(itemView.getContext().getAssets(), Constants.BubblegumSans_Regular_font);

        final AccountInfo accountInfo = accountInfoList.get(position);
        account_button.setText(Html.fromHtml("<h2><u>" + accountInfo.subject + "</u></h2><p>" + accountInfo.date + "<br>" + accountInfo.account + "</p>" + accountInfo.postBy));
//        notice_button.setText(noticeInfo.subject+"\n"+noticeInfo.date+"\n"+noticeInfo.notice);
        account_button.setTypeface(typeface);

        return itemView;
    }
}
