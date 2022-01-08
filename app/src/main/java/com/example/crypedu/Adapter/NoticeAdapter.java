package com.example.crypedu.Adapter;
/*
 *************************************************************************************************
 * Developed by Cryptonsoftech
 * Date: 30-05-2017
 * Details:
 * ***********************************************************************************************
 */

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.activity.smi.R;
import com.example.crypedu.Activity.ImageShowingActivity;
import com.example.crypedu.Activity.NoticeActivity;
import com.example.crypedu.Activity.PdfOpenActivity;
import com.example.crypedu.Constants.Constants;
import com.example.crypedu.Helper.UtilHelper;
import com.example.crypedu.Pojo.NoticeInfo;
import com.jsibbold.zoomage.ZoomageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import at.blogc.android.views.ExpandableTextView;

public class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.ViewHolder> {


    private Context context;
    private ArrayList<NoticeInfo> mList;
    private int selectedPosition=-1;

    public NoticeAdapter(Context context, ArrayList<NoticeInfo> list) {
        this.context = context;
        this.mList=list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater=LayoutInflater.from(context);

        View view=layoutInflater.inflate(R.layout.item_notice_adapter,parent,false);

        ViewHolder viewHolder=new ViewHolder(view);

        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        final NoticeInfo noticeModel=mList.get(position);

        String date_format= UtilHelper.parseDateToddMMyyyy(noticeModel.date);
        holder.tv_notice_subject.setText(noticeModel.subject);
        holder.tv_notice_date.setText(date_format);
        holder.tv_text.setText(Html.fromHtml(noticeModel.notice));

        //This below code used for changing text color of web view
        String message = "<html><head>"
                + "<style type=\"text/css\">body{color: #fff;}"
                + "</style></head>"
                + "<body>"
                + noticeModel.notice
                + "</body></html>";

        holder.wv_body.loadDataWithBaseURL(null, message, "text/html", "utf-8", null);
        holder.wv_body.setBackgroundColor(Color.TRANSPARENT); // to change background color of web view

        GradientDrawable gd = new GradientDrawable(
                GradientDrawable.Orientation.LEFT_RIGHT,
                new int[] {Color.parseColor("#135080"),Color.parseColor("#7cbaeb")});
        gd.setCornerRadius(0f);

        GradientDrawable gd2 = new GradientDrawable(
                GradientDrawable.Orientation.LEFT_RIGHT,
                    new int[] {Color.parseColor("#109490"),Color.parseColor("#72d6d3")});
        gd.setCornerRadius(0f);



        if(position %2 == 1)
        {
            holder.lin.setBackgroundDrawable(gd);
        }
        else
        {
            holder.lin.setBackgroundDrawable(gd2);
        }

        if (noticeModel.notice_type.equals("1"))
        {
            holder.tv_text.setVisibility(View.VISIBLE);//added
            holder.rl_pdf_notice.setVisibility(View.GONE);
            holder.rl_image_bulletin_notice.setVisibility(View.GONE);

            //Start show hide web view

            if(selectedPosition==position){
                holder.wv_body.setVisibility(View.VISIBLE);
                holder.tv_text.setVisibility(View.GONE);//added
                holder.iv_collaps.setVisibility(View.VISIBLE);
                holder.iv_expand.setVisibility(View.INVISIBLE);
            }
            else {
                holder.wv_body.setVisibility(View.GONE);
                holder.tv_text.setVisibility(View.VISIBLE);//added
                holder.iv_collaps.setVisibility(View.INVISIBLE);
                holder.iv_expand.setVisibility(View.VISIBLE);
            }

            holder.iv_expand.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                        selectedPosition = position;
                        notifyDataSetChanged();


                }
            });

            holder.iv_collaps.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    holder.wv_body.setVisibility(View.GONE);
                    holder.tv_text.setVisibility(View.VISIBLE);//added
                    holder.iv_collaps.setVisibility(View.INVISIBLE);
                    holder.iv_expand.setVisibility(View.VISIBLE);

                }
            });

            //Start show hide web view


        }
        else if (noticeModel.notice_type.equals("2"))
        {
            holder.rl_pdf_notice.setVisibility(View.VISIBLE);
            holder.wv_body.setVisibility(View.GONE);
            holder.rl_image_bulletin_notice.setVisibility(View.GONE);
        }
        else if (noticeModel.notice_type.equals("3"))
        {
            holder.rl_image_bulletin_notice.setVisibility(View.VISIBLE);
            holder.wv_body.setVisibility(View.GONE);
            holder.rl_pdf_notice.setVisibility(View.GONE);
        }

        holder.rl_pdf_notice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constants.CHAPTER_ID="";
                Constants.CHAPTER_NAME="Notice PDF";
                Constants.PDF_URL=noticeModel.notice_pdf;
                Intent intent = new Intent(context, PdfOpenActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        holder.rl_image_bulletin_notice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showingImage(noticeModel.notice_pdf);
                /*final Dialog nagDialog = new Dialog(context,android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
                nagDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                nagDialog.setCancelable(true);
                nagDialog.setContentView(R.layout.preview_image);
                Button btnClose = (Button)nagDialog.findViewById(R.id.btnIvClose);
                ZoomageView ivPreview = (ZoomageView)nagDialog.findViewById(R.id.iv_preview_image);
                Picasso.with(context)
                        .load(noticeModel.notice_pdf)
//                        .placeholder(R.drawable.progress_loading)
                        .into(ivPreview);

                btnClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {

                        nagDialog.dismiss();
                    }
                });
                nagDialog.show();*/
            }
        });

    }

    private void showingImage(String notice_pdf) {
        final Dialog nagDialog = new Dialog(context,android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        nagDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        nagDialog.setCancelable(true);
        nagDialog.setContentView(R.layout.preview_image);
        Button btnClose = (Button)nagDialog.findViewById(R.id.btnIvClose);
        ZoomageView ivPreview = (ZoomageView)nagDialog.findViewById(R.id.iv_preview_image);
        Picasso.with(context)
                .load(notice_pdf)
                .into(ivPreview);

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                nagDialog.dismiss();
            }
        });
        nagDialog.show();
    }

    @Override
    public int getItemCount() {

        return mList.size();
    }




    public class ViewHolder extends RecyclerView.ViewHolder
    {
        //ExpandableTextView expand_text_view;
        at.blogc.android.views.ExpandableTextView expand_text_view;
        ImageView iv_expand,iv_collaps;
        CardView card_view;
        LinearLayout lin;
        TextView tv_notice_subject, tv_notice_date, tv_text;
        RelativeLayout rl_pdf_notice, rl_image_bulletin_notice;
        WebView wv_body;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);

            tv_notice_subject = itemView.findViewById(R.id.tv_notice_subject);
            tv_notice_date = itemView.findViewById(R.id.tv_notice_date);
            expand_text_view = itemView.findViewById(R.id.expand_text_view);
            iv_expand = itemView.findViewById(R.id.iv_expand);
            iv_collaps = itemView.findViewById(R.id.iv_collaps);
            lin = itemView.findViewById(R.id.lin);
            rl_pdf_notice = itemView.findViewById(R.id.rl_pdf_notice);
            rl_image_bulletin_notice = itemView.findViewById(R.id.rl_image_bulletin_notice);
            wv_body = itemView.findViewById(R.id.wv_body);
            tv_text = itemView.findViewById(R.id.tv_text);
            expand_text_view.setAnimationDuration(750L);

            expand_text_view.addOnExpandListener(new ExpandableTextView.OnExpandListener()
            {
                @Override
                public void onExpand(@NonNull final ExpandableTextView view)
                {
                    // Log.d(TAG, "ExpandableTextView expanded");
                }

                @Override
                public void onCollapse(@NonNull final ExpandableTextView view)
                {
                    // Log.d(TAG, "ExpandableTextView collapsed");
                }
            });


        }
    }
}



/*extends BaseAdapter {
    private Context context;
    private final List<NoticeInfo> noticeInfoList;
    LayoutInflater mInflater;
    LayoutInflater inflater;

    public NoticeAdapter(Context context, List<NoticeInfo> noticeInfoList, LayoutInflater inflater) {
        mInflater = inflater;
        this.context = context;
        this.noticeInfoList = noticeInfoList;
    }

    @Override
    public int getCount() {
        return noticeInfoList.size();
    }

    @Override
    public Object getItem(int position) {
        return noticeInfoList.get(position);
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
        itemView = inflater.inflate(R.layout.notice_adapter, parent, false);
        TextView tv_header = itemView.findViewById(R.id.tv_header);
        TextView tv_date = itemView.findViewById(R.id.tv_date);
        WebView wv_body = itemView.findViewById(R.id.wv_body);
        RelativeLayout rl_pdf_notice=itemView.findViewById(R.id.rl_pdf_notice);
        RelativeLayout rl_image_notice=itemView.findViewById(R.id.rl_image_notice);

        final NoticeInfo noticeInfo = noticeInfoList.get(position);

        tv_header.setText(Html.fromHtml("<u>" + noticeInfo.subject + "</u>" ));
        tv_date.setText(noticeInfo.date);
        //This below code used for changing text color of web view
        String message = "<html><head>"
                + "<style type=\"text/css\">body{color: #fff;}"
                + "</style></head>"
                + "<body>"
                + noticeInfo.notice
                + "</body></html>";

        wv_body.loadDataWithBaseURL(null, message, "text/html", "utf-8", null);
        wv_body.setBackgroundColor(Color.TRANSPARENT); // to change background color of web view

        if (noticeInfo.notice_type.equals("1"))
        {
            wv_body.setVisibility(View.VISIBLE);
            rl_pdf_notice.setVisibility(View.GONE);
            rl_image_notice.setVisibility(View.GONE);
        }
        else if (noticeInfo.notice_type.equals("2"))
        {
            rl_pdf_notice.setVisibility(View.VISIBLE);
            wv_body.setVisibility(View.GONE);
            rl_image_notice.setVisibility(View.GONE);
        }
        else if (noticeInfo.notice_type.equals("3"))
        {
            rl_image_notice.setVisibility(View.VISIBLE);
            rl_pdf_notice.setVisibility(View.GONE);
            wv_body.setVisibility(View.GONE);
        }

        rl_pdf_notice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constants.CHAPTER_ID="";
                Constants.CHAPTER_NAME="Notice PDF";
                Constants.PDF_URL=noticeInfo.notice_pdf;
                Intent intent = new Intent(context, PdfOpenActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        rl_image_notice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ImageShowingActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("image_url", noticeInfo.notice_pdf);
                context.startActivity(intent);
            }
        });

        return itemView;
    }
}*/

