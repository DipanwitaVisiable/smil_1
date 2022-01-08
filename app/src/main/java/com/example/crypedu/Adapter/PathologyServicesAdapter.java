package com.example.crypedu.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.activity.smi.R;
import com.example.crypedu.Activity.PathologyActivity;
import com.example.crypedu.Constants.Constants;
import com.example.crypedu.Pojo.PathologyServiceSetterGetter;

import java.util.ArrayList;

/**
 * Created by sudipta on 23-02-2018.
 */

public class PathologyServicesAdapter extends RecyclerView.Adapter<PathologyServicesAdapter.ViewHolder> {
    Context context;
    private ArrayList<PathologyServiceSetterGetter> pathologyServiceSetterGetterArrayList;

    public PathologyServicesAdapter(Context context, ArrayList<PathologyServiceSetterGetter> pathologyServiceSetterGetterArrayList) {
        this.context = context;
        this.pathologyServiceSetterGetterArrayList = pathologyServiceSetterGetterArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        View v = inflater.inflate(R.layout.pathology_service_singleitem, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final PathologyServiceSetterGetter list = pathologyServiceSetterGetterArrayList.get(position);
        holder.tv_pathology_service.setText(list.getPathology_service_name());
        holder.linearLayout_select_pathology.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, PathologyActivity.class);
                context.startActivity(i);
                Constants.PATHOLOGY_SRVS_ID = list.getPathology_id();
                Log.e("ID sent PathologyPage", Constants.PATHOLOGY_SRVS_ID);
                //Toast.makeText(context, "Id" + holder.getAdapterPosition(), Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return pathologyServiceSetterGetterArrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_pathology_service;
        LinearLayout linearLayout_select_pathology;

        ViewHolder(View itemView) {
            super(itemView);
            tv_pathology_service = itemView.findViewById(R.id.tv_pathology_service);
            linearLayout_select_pathology = itemView.findViewById(R.id.linearLayout_select_pathology);
            Typeface typeface = Typeface.createFromAsset(itemView.getContext().getAssets(), Constants.BubblegumSans_Regular_font);
            tv_pathology_service.setTypeface(typeface);
        }
    }

}
