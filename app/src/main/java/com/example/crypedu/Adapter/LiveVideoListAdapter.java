package com.example.crypedu.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.activity.smi.R;
import com.example.crypedu.Activity.CustomYouTubePlayerActivity;
import com.example.crypedu.Activity.PlayVideoActivity;
import com.example.crypedu.Helper.UtilHelper;
import com.example.crypedu.Pojo.LiveVideoListInfo;
import com.example.crypedu.youtube_api.YoutubeApiKey;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;

import java.util.ArrayList;

public class LiveVideoListAdapter extends RecyclerView.Adapter<LiveVideoListAdapter.ViewHolder> {


    private Context context;
    //    private ArrayList<VideosInfo> mList;
    private ArrayList<LiveVideoListInfo> mList;

    public LiveVideoListAdapter(Context context, ArrayList<LiveVideoListInfo> mList) {
        this.context = context;
        this.mList=mList;
    }

    @NonNull
    @Override
    public LiveVideoListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater=LayoutInflater.from(context);

        View view=layoutInflater.inflate(R.layout.item_live_video_list_adapter,parent,false);

        LiveVideoListAdapter.ViewHolder viewHolder=new LiveVideoListAdapter.ViewHolder(view);

        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull final LiveVideoListAdapter.ViewHolder holder, int position) {

        final LiveVideoListInfo videosInfo=mList.get(position);

        holder.youTubeThumbnailView.initialize(YoutubeApiKey.YOUTUBE_API_KEY_PART_1 + YoutubeApiKey.YOUTUBE_API_KEY_PART_2
                + YoutubeApiKey.YOUTUBE_API_KEY_PART_3 + YoutubeApiKey.YOUTUBE_API_KEY_PART_4, new YouTubeThumbnailView.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader youTubeThumbnailLoader) {

                youTubeThumbnailLoader.setVideo(videosInfo.getVideo_link());
                youTubeThumbnailLoader.setOnThumbnailLoadedListener(new YouTubeThumbnailLoader.OnThumbnailLoadedListener() {
                    @Override
                    public void onThumbnailLoaded(YouTubeThumbnailView youTubeThumbnailView, String s) {
                        holder.content_load.setVisibility(View.GONE);
                        holder.iv_play_video.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onThumbnailError(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader.ErrorReason errorReason) {

                    }
                });
            }

            @Override
            public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView, YouTubeInitializationResult youTubeInitializationResult) {

            }

        });

        holder.video_title.setText(videosInfo.getTopic_name());

            String strDate= UtilHelper.parseDateToddMMyyyy(videosInfo.getClass_date());
            holder.video_date_time.setText("Date: " + strDate );


        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Bundle b = new Bundle();
                b.putString("video_id", videosInfo.getId());
                b.putString("url", videosInfo.getVideo_link());
                context.startActivity(new Intent(context, PlayVideoActivity.class).putExtras(b));*/

              Bundle b = new Bundle();
              b.putString("video_id", videosInfo.getId());
              b.putString("url", videosInfo.getVideo_link());
              context.startActivity(new Intent(context, CustomYouTubePlayerActivity.class).putExtras(b));

            }
        });

        holder.iv_play_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.cardView.performClick();
            }
        });



    }

    /*private void showAlertDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("This video is not available for you")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

    }*/

    @Override
    public int getItemCount() {

        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView video_title, video_date_time;
        YouTubeThumbnailView youTubeThumbnailView;
        CardView cardView;
        ContentLoadingProgressBar content_load;
        ImageView iv_play_video;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);

            video_title=(TextView)itemView.findViewById(R.id.video_title);
            youTubeThumbnailView=itemView.findViewById(R.id.youtube_thumbnail);
            content_load=itemView.findViewById(R.id.content_load);
            cardView=itemView.findViewById(R.id.cardView);
            iv_play_video=itemView.findViewById(R.id.iv_play_video);
            video_date_time=itemView.findViewById(R.id.video_date_time);


        }
    }
}
