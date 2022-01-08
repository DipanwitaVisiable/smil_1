package com.example.crypedu.Adapter;

import android.content.Context;
import android.graphics.Typeface;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.activity.smi.R;
import com.example.crypedu.Constants.Constants;
import com.example.crypedu.Pojo.EmergencyListSetterGetter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by INDID on 26-03-2018.
 */

public class EmergencyListAdapter extends RecyclerView.Adapter<EmergencyListAdapter.viewholder> {
    Context context;
    private ArrayList<EmergencyListSetterGetter> emergencyListSetterGetterArrayList;

    public EmergencyListAdapter(Context context, ArrayList<EmergencyListSetterGetter> emergencyListSetterGetterArrayList) {
        this.context = context;
        this.emergencyListSetterGetterArrayList = emergencyListSetterGetterArrayList;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        View v = inflater.inflate(R.layout.single_emergeny_listitem, parent, false);
        return new viewholder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {
        EmergencyListSetterGetter list = emergencyListSetterGetterArrayList.get(position);
        holder.tv_service_name.setText(list.getService_name());
        Picasso.with(context)
                .load(list.getService_icon())
                .placeholder(R.drawable.placeholder1)
                .into(holder.iv_service);
        holder.rv_sub.setAdapter(new SubRvAdapter(context,list.getSublist()));

    }

    @Override
    public int getItemCount() {
        return emergencyListSetterGetterArrayList.size();
    }

    class viewholder extends RecyclerView.ViewHolder {
        TextView tv_service_name;
        ImageView iv_service;
        RecyclerView rv_sub;

        viewholder(View itemView) {
            super(itemView);
            tv_service_name = itemView.findViewById(R.id.tv_service_name);
            iv_service = itemView.findViewById(R.id.iv_service);

            Typeface typeface = Typeface.createFromAsset(itemView.getContext().getAssets(), Constants.BubblegumSans_Regular_font);
            tv_service_name.setTypeface(typeface);
            rv_sub = itemView.findViewById(R.id.rv_sub);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
            rv_sub.setLayoutManager(layoutManager);

        }
    }
}
