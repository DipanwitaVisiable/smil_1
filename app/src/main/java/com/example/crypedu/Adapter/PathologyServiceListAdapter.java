package com.example.crypedu.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.activity.smi.R;
import com.example.crypedu.Constants.Constants;
import com.example.crypedu.Pojo.PathologySetterGetter;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by sudipta on 08-01-2018.
 */


public class PathologyServiceListAdapter extends RecyclerView.Adapter<PathologyServiceListAdapter.ViewHolder> {
    private Context context;
    private ArrayList<PathologySetterGetter> pathologySetterGetters;
    private PopupWindow popupWindow;
    private ArrayList<String> arrayList;

    public PathologyServiceListAdapter(Context context, ArrayList<PathologySetterGetter> pathologySetterGetters) {
        this.context = context;
        this.pathologySetterGetters = pathologySetterGetters;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        View v = inflater.inflate(R.layout.custom_pathology_service_list, parent, false);
        return new ViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final PathologySetterGetter list = pathologySetterGetters.get(position);
        holder.tv_pathology_name.setText(list.getPathology_name());
        holder.tv_pathology_address.setText(list.getPathology_address());
        holder.tv_pathology_phone_no.setText(list.getPathology_phn());
        if (list.getPathology_image().isEmpty() || list.getPathology_image() == null){
            Picasso.with(context)
                    .load(R.drawable.pathology)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .placeholder(R.drawable.pathology)
                    .error(R.drawable.pathology)
                    .fit()
                    .into(holder.iv_pathology_image);
        }else {
            Picasso.with(context)
                    .load(list.getPathology_image())
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .placeholder(R.drawable.pathology)
                    .error(R.drawable.pathology)
                    .fit()
                    .into(holder.iv_pathology_image);
        }
    }

    @Override
    public int getItemCount() {
        return pathologySetterGetters.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_pathology_image;
        TextView tv_pathology_name, tv_pathology_address, tv_pathology_phone_no;
        LinearLayout linearLayout_pathology_item;

        ViewHolder(View itemView) {
            super(itemView);
            linearLayout_pathology_item = itemView.findViewById(R.id.linearLayout_pathology_item);
            iv_pathology_image = itemView.findViewById(R.id.iv_pathology_image);

            tv_pathology_name = itemView.findViewById(R.id.tv_pathology_name);
            tv_pathology_address = itemView.findViewById(R.id.tv_pathology_address);
            tv_pathology_phone_no = itemView.findViewById(R.id.tv_pathology_phone_no);
            Typeface typeface = Typeface.createFromAsset(itemView.getContext().getAssets(), Constants.BubblegumSans_Regular_font);
            tv_pathology_name.setTypeface(typeface);
            tv_pathology_address.setTypeface(typeface);
            tv_pathology_phone_no.setTypeface(typeface);

        }

    }
}
