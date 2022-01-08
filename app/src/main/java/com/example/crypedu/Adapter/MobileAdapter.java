package com.example.crypedu.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.activity.smi.R;
import com.example.crypedu.Pojo.Mobile;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Chandan on 18/09/2018.
 */
public class MobileAdapter extends RecyclerView.Adapter<MobileAdapter.ViewHolder> {

    private Context context;
    private List<Mobile> mobiles;

    MobileAdapter(Context context, List<Mobile> mobiles) {
        this.context = context;
        this.mobiles = mobiles;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        View cardView = inflater.inflate(R.layout.custom_gallery_child, parent, false);
//        ViewHolder viewHolder = new ViewHolder(cardView);
       /* viewHolder.mobileImage = cardView.findViewById(R.id.image_mobile);
        viewHolder.modelName = cardView.findViewById(R.id.text_mobile_model);
        viewHolder.price = cardView.findViewById(R.id.text_mobile_price);*/
        return new ViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        Mobile mobile = mobiles.get(position);
        Picasso.with(context)
                .load(mobile.imageResource)
                .noFade()
                .into(holder.mobileImage);
        holder.modelName.setText(mobile.modelName);
        holder.price.setText(mobile.price);

    }

    @Override
    public int getItemCount() {
        //Log.e("Child Size ", String.valueOf(mobiles.size()));
        return mobiles.size();
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView mobileImage;
        TextView modelName;
        TextView price;

        public ViewHolder(View itemView) {
            super(itemView);
            mobileImage = itemView.findViewById(R.id.image_mobile);
            modelName = itemView.findViewById(R.id.text_mobile_model);
            price = itemView.findViewById(R.id.text_mobile_price);
        }
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private MobileAdapter.ClickListener clickListener;

        RecyclerTouchListener(Context context, final RecyclerView recyclerView, final MobileAdapter.ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                int recyclerViewPosition = rv.getChildAdapterPosition(child);
                clickListener.onClick(child, recyclerViewPosition);

            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

}
