package com.example.crypedu.Adapter;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.activity.smi.R;
import com.example.crypedu.Activity.AssignmentReplyActivity;

import java.io.File;
import java.util.ArrayList;

public class MultipleFileShowingAdapter extends RecyclerView.Adapter<MultipleFileShowingAdapter.PeriodViewHolder> {

  Context context;
  //    ArrayList<Uri> receiveImageList;
  public MultipleFileShowingAdapter(Context context, ArrayList<Uri> receiveImageList){
    this.context = context;
//        this.receiveImageList = receiveImageList;
//        this.receiveDisplayNameList = receiveDisplayNameList;

  }

  @NonNull
  @Override
  public MultipleFileShowingAdapter.PeriodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    assert inflater != null;
    View view = inflater.inflate(R.layout.item_multiple_file_adapter, parent, false);
    return new MultipleFileShowingAdapter.PeriodViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull MultipleFileShowingAdapter.PeriodViewHolder holder, final int position) {

    if (AssignmentReplyActivity.fileUriStaticList.get(position) != null) {

      String displayName="";
      Cursor returnCursor = context.getContentResolver().query(AssignmentReplyActivity.fileUriStaticList.get(position), null, null, null, null);

      int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
      int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
      returnCursor.moveToFirst();

      holder.tv_file_name.setText(returnCursor.getString(nameIndex));
      long size=returnCursor.getLong(sizeIndex);
      if (size<1024)
        holder.tv_file_size.setText(Long.toString(returnCursor.getLong(sizeIndex))+ " bytes");
      else {
        long size_in_kb=size/1024;
        if (size_in_kb<1024)
          holder.tv_file_size.setText(size_in_kb+ " KB");
        else {
          long size_in_mb=size_in_kb/1024;
          holder.tv_file_size.setText(size_in_mb+ " MB");
        }
      }

      if (returnCursor.getString(nameIndex).contains(".pdf"))
        holder.iv_file_icon.setImageResource(R.drawable.ic_pdf_icon);
      else if (returnCursor.getString(nameIndex).contains(".doc"))
        holder.iv_file_icon.setImageResource(R.drawable.ic_doc_icon);
      else if (returnCursor.getString(nameIndex).contains(".txt"))
        holder.iv_file_icon.setImageResource(R.drawable.ic_doc_icon);
      else if (returnCursor.getString(nameIndex).contains(".mp4"))
        holder.iv_file_icon.setImageResource(R.drawable.ic_video_file_icon);
      else if (returnCursor.getString(nameIndex).contains(".mp3"))
        holder.iv_file_icon.setImageResource(R.drawable.ic_audio_file_icon);
      else if (returnCursor.getString(nameIndex).contains(".png") ||
        returnCursor.getString(nameIndex).contains(".jpg") || returnCursor.getString(nameIndex).contains(".jpeg"))
        holder.iv_file_icon.setImageResource(R.drawable.ic_image_icon);

      else
        holder.iv_file_icon.setImageResource(R.drawable.ic_attach_file);

//            holder.tv_file_name.setText(receiveDisplayNameList.get(position));

      //////Show display name

      String uriString = AssignmentReplyActivity.fileUriStaticList.get(position).toString();
      File myFile = new File(uriString);
      if (uriString.startsWith("content://")) {
        Cursor cursor = null;
        try {
          cursor = context.getContentResolver().query(AssignmentReplyActivity.fileUriStaticList.get(position), null, null, null, null);
          if (cursor != null && cursor.moveToFirst()) {
            displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
            holder.tv_file_name.setText(displayName);
          }
        } finally {
          cursor.close();
        }
      } else if (uriString.startsWith("file://")) {
        displayName = myFile.getName();
        holder.tv_file_name.setText(displayName);
      }

      //////Show display name


    }
    holder.iv_attachment_cancel.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (AssignmentReplyActivity.fileUriStaticList.size()>0) {
          AssignmentReplyActivity.fileUriStaticList.remove(position);
//                    receiveImageList.remove(position);
          notifyDataSetChanged();
        }
      }
    });

  }

  @Override
  public int getItemCount () {
    return AssignmentReplyActivity.fileUriStaticList.size();
  }

  class PeriodViewHolder extends RecyclerView.ViewHolder{
    //        RelativeLayout rl_attachment;
    ImageView iv_file_icon, iv_attachment_cancel;
    TextView tv_file_name, tv_file_size;

    public PeriodViewHolder(@NonNull View itemView) {
      super(itemView);
//            rl_attachment = itemView.findViewById(R.id.rl_attachment);
      iv_file_icon = itemView.findViewById(R.id.iv_file_icon);
      iv_attachment_cancel = itemView.findViewById(R.id.iv_attachment_cancel);
      tv_file_name = itemView.findViewById(R.id.tv_file_name);
      tv_file_size = itemView.findViewById(R.id.tv_file_size);
    }
  }
}
