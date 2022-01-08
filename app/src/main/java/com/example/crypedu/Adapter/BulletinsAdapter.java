package com.example.crypedu.Adapter;
/*
 *************************************************************************************************
 * Developed by Cryptonsoftech
 * Date: 30-05-2017
 * Details:
 * ***********************************************************************************************
 */
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.activity.smi.R;
import com.example.crypedu.Activity.ImageShowingActivity;
import com.example.crypedu.Activity.PdfOpenActivity;
import com.example.crypedu.Constants.Constants;
import com.example.crypedu.Pojo.BulletinsInfo;
import com.example.crypedu.Pojo.NoticeInfo;
import com.jsibbold.zoomage.ZoomageView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class BulletinsAdapter extends BaseAdapter {
    private final Context context;
    private final List<BulletinsInfo> bulletinsInfoList;
    LayoutInflater mInflater;
    LayoutInflater inflater;

    public BulletinsAdapter(Context context, List<BulletinsInfo> bulletinsInfoList, LayoutInflater inflater) {
        mInflater = inflater;
        this.context = context;
        this.bulletinsInfoList = bulletinsInfoList;
    }

    @Override
    public int getCount() {
        return bulletinsInfoList.size();
    }

    @Override
    public Object getItem(int position) {
        return bulletinsInfoList.get(position);
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
        itemView = inflater.inflate(R.layout.bulletins_adapter, parent, false);
        /*TextView bulletins_button = itemView.findViewById(R.id.bulletins_button);

        Typeface typeface = Typeface.createFromAsset(itemView.getContext().getAssets(), Constants.BubblegumSans_Regular_font);

        final BulletinsInfo bulletinsInfo = bulletinsInfoList.get(position);
        bulletins_button.setText(Html.fromHtml("<h3><u>"+bulletinsInfo.subject+"</u></h3>" +
                "<p>"+bulletinsInfo.date+"<br>"+bulletinsInfo.notice+"</p><p>"+bulletinsInfo.postBy+"</p>"));
        bulletins_button.setTypeface(typeface);*/

        TextView tv_header = itemView.findViewById(R.id.tv_header);
        TextView tv_date = itemView.findViewById(R.id.tv_date);
        TextView tv_post_by = itemView.findViewById(R.id.tv_post_by);
        WebView wv_body = itemView.findViewById(R.id.wv_body);
        RelativeLayout rl_pdf_bulletins = itemView.findViewById(R.id.rl_pdf_bulletins);
        RelativeLayout rl_image_bulletin = itemView.findViewById(R.id.rl_image_bulletin);

        final BulletinsInfo bulletinsInfo = bulletinsInfoList.get(position);

        tv_header.setText(Html.fromHtml("<u>"+bulletinsInfo.subject+"</u>"));
        tv_date.setText(bulletinsInfo.date);
        tv_post_by.setText(bulletinsInfo.postBy);

        wv_body.loadDataWithBaseURL(null, bulletinsInfo.notice, "text/html", "utf-8", null);
        wv_body.setBackgroundColor(Color.TRANSPARENT); // to change background color of web view

        if (bulletinsInfo.bullet_type.equals("1"))
        {
            wv_body.setVisibility(View.VISIBLE);
            rl_pdf_bulletins.setVisibility(View.GONE);
            rl_image_bulletin.setVisibility(View.GONE);
        }
        else if (bulletinsInfo.bullet_type.equals("2"))
        {
            rl_pdf_bulletins.setVisibility(View.VISIBLE);
            wv_body.setVisibility(View.GONE);
            rl_image_bulletin.setVisibility(View.GONE);
        }
        else if (bulletinsInfo.bullet_type.equals("3"))
        {
            rl_image_bulletin.setVisibility(View.VISIBLE);
            wv_body.setVisibility(View.GONE);
            rl_pdf_bulletins.setVisibility(View.GONE);
        }

        rl_pdf_bulletins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constants.CHAPTER_ID="";
                Constants.CHAPTER_NAME="Bulletin PDF";
                Constants.PDF_URL=bulletinsInfo.bullet_pdf;
                Intent intent = new Intent(context, PdfOpenActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        /*rl_image_bulletin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog nagDialog = new Dialog(context,android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
                nagDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                nagDialog.setCancelable(true);
                nagDialog.setContentView(R.layout.preview_image);
                Button btnClose = (Button)nagDialog.findViewById(R.id.btnIvClose);
                ZoomageView ivPreview = (ZoomageView)nagDialog.findViewById(R.id.iv_preview_image);
//                ivPreview.setImageDrawable(holder.iv_post_image.getDrawable());
                Picasso.with(context)
                        .load(bulletinsInfo.bullet_pdf)
                        .placeholder(R.drawable.placeholder1)
                        .into(ivPreview);

                btnClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {

                        nagDialog.dismiss();
                    }
                });
                nagDialog.show();
            }
        });*/

        rl_image_bulletin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ImageShowingActivity.class);
                intent.putExtra("image_url", bulletinsInfo.bullet_pdf);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        return itemView;
    }
}
