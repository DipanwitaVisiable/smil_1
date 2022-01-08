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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.activity.smi.R;
import com.example.crypedu.Activity.DoctorChamberActivity;
import com.example.crypedu.Activity.MediDepartmentActivity;
import com.example.crypedu.Constants.Constants;
import com.example.crypedu.Pojo.MediChamberSetterGetter;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by INDID on 27-02-2018.
 */

public class MediChamberAdapter extends RecyclerView.Adapter<MediChamberAdapter.mediViewHolder> {
    Context context;
    private ArrayList<MediChamberSetterGetter> mediChamberSetterGetterArrayList;

    public MediChamberAdapter(Context context, ArrayList<MediChamberSetterGetter> mediChamberSetterGetterArrayList) {
        this.context = context;
        this.mediChamberSetterGetterArrayList = mediChamberSetterGetterArrayList;
    }

    @NonNull
    @Override
    public mediViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        View v = inflater.inflate(R.layout.medi_chamber_single_item, parent, false);
        return new mediViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull mediViewHolder holder, int position) {
        final MediChamberSetterGetter list = mediChamberSetterGetterArrayList.get(position);
        holder.tv_chamber_name.setText(list.getChamber_name());
        holder.tv_chamber_address.setText(list.getChamber_address());
        holder.tv_chamber_description.setText(list.getChamber_desc());
        holder.tv_chamber_phone_no.setText(list.getChamber_phone());
        holder.linearLayout_chamber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constants.CHAMBER_ID = list.getChamber_id();
                Constants.CHAMBER_TYPE = list.getChamber_type();
                Log.e("ID sent CHAMBER_ID ", Constants.CHAMBER_ID);
                Log.e("Type sent CHAMBER_TYPE ", Constants.CHAMBER_TYPE);
                Intent intent;
                if (Constants.CHAMBER_TYPE.equalsIgnoreCase("1")) {
                    intent = new Intent(context, DoctorChamberActivity.class);
                    //intent = new Intent(context, MediDepartmentActivity.class);
                }else {
                    intent = new Intent(context, MediDepartmentActivity.class);
                }
                context.startActivity(intent);
                //i.putExtra("link", list.getChapter_pdf());
                //Toast.makeText(context, "Id" + holder.getAdapterPosition(), Toast.LENGTH_LONG).show();
            }
        });
        Constants.CHAMBER_ID = list.getChamber_id();
        if (list.getChamber_img().isEmpty() || list.getChamber_img() == null){
            Picasso.with(context)
                    .load(R.drawable.doctor)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .placeholder(R.drawable.doctor)
                    .error(R.drawable.doctor)
                    .fit()
                    .into(holder.iv_chamber_image);
        }else {
            Picasso.with(context)
                    .load(list.getChamber_img())
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .placeholder(R.drawable.doctor)
                    .error(R.drawable.doctor)
                    .fit()
                    .into(holder.iv_chamber_image);
        }
    }

    @Override
    public int getItemCount() {
        return mediChamberSetterGetterArrayList.size();
    }

    class mediViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_chamber_image;
        TextView tv_chamber_name, tv_chamber_description, tv_chamber_phone_no, tv_chamber_address;
        LinearLayout linearLayout_chamber;

        mediViewHolder(View itemView) {
            super(itemView);
            iv_chamber_image = itemView.findViewById(R.id.iv_chamber_image);
            tv_chamber_name = itemView.findViewById(R.id.tv_chamber_name);
            tv_chamber_description = itemView.findViewById(R.id.tv_chamber_description);
            tv_chamber_phone_no = itemView.findViewById(R.id.tv_chamber_phone_no);
            tv_chamber_address = itemView.findViewById(R.id.tv_chamber_address);
            linearLayout_chamber = itemView.findViewById(R.id.linearLayout_chamber);

            Typeface typeface = Typeface.createFromAsset(itemView.getContext().getAssets(), Constants.BubblegumSans_Regular_font);
            tv_chamber_name.setTypeface(typeface);
            tv_chamber_description.setTypeface(typeface);
            tv_chamber_phone_no.setTypeface(typeface);
            tv_chamber_address.setTypeface(typeface);
        }
    }
}
