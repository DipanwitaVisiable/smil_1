package com.example.crypedu.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.activity.smi.R;
import com.example.crypedu.Activity.AssignmentReplyActivity;

import java.io.IOException;
import java.util.ArrayList;

public class MultipleImageShowingAdapter extends RecyclerView.Adapter<MultipleImageShowingAdapter.PeriodViewHolder> {

  Context context;
  //    ArrayList<Uri> receiveImageList;
  Bitmap bitmap;
  public MultipleImageShowingAdapter(Context context, ArrayList<Uri> receiveImageList){
    this.context = context;
//        this.receiveImageList = receiveImageList;

  }

  @NonNull
  @Override
  public MultipleImageShowingAdapter.PeriodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    assert inflater != null;
    View view = inflater.inflate(R.layout.item_multiple_image_adapter, parent, false);
    return new MultipleImageShowingAdapter.PeriodViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull MultipleImageShowingAdapter.PeriodViewHolder holder, @SuppressLint("RecyclerView") final int position) {

    if (AssignmentReplyActivity.imageUriStaticList.get(position) != null) {
      try {
        bitmap = MediaStore.Images.Media.getBitmap(context.getApplicationContext().getContentResolver(), AssignmentReplyActivity.imageUriStaticList.get(position));
        holder.image.setImageBitmap(bitmap);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    holder.iv_cancel_image.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (AssignmentReplyActivity.imageUriStaticList.size()>0) {
          AssignmentReplyActivity.imageUriStaticList.remove(position);
//                    receiveImageList.remove(position);
          notifyDataSetChanged();
        }
      }
    });

  }

  @Override
  public int getItemCount () {
    return AssignmentReplyActivity.imageUriStaticList.size();
  }

  class PeriodViewHolder extends RecyclerView.ViewHolder{
    RelativeLayout rl_for_image;
    ImageView image, iv_cancel_image;

    public PeriodViewHolder(@NonNull View itemView) {
      super(itemView);
      rl_for_image = itemView.findViewById(R.id.rl_for_image);
      image = itemView.findViewById(R.id.image);
      iv_cancel_image = itemView.findViewById(R.id.iv_cancel_image);
    }
  }
}
