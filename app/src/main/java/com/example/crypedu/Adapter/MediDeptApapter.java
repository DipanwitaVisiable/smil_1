package com.example.crypedu.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.activity.smi.R;
import com.example.crypedu.Activity.DoctorActivity;
import com.example.crypedu.Constants.Constants;
import com.example.crypedu.Pojo.MediDeptSetterGetter;

import java.util.ArrayList;

/**
 * Created by INDID on 27-02-2018.
 */

public class MediDeptApapter extends RecyclerView.Adapter<MediDeptApapter.mediDeptViewHolder> {
    Context context;
    private ArrayList<MediDeptSetterGetter> mediDeptSetterGetterArrayList;
    private int lastPosition = -1;

    public MediDeptApapter(Context context, ArrayList<MediDeptSetterGetter> mediDeptSetterGetterArrayList) {
        this.context = context;
        this.mediDeptSetterGetterArrayList = mediDeptSetterGetterArrayList;
    }

    @NonNull
    @Override
    public mediDeptViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        View v = inflater.inflate(R.layout.medi_dept_single_item, parent, false);
        return new mediDeptViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull mediDeptViewHolder holder, int position) {
        final MediDeptSetterGetter list = mediDeptSetterGetterArrayList.get(position);
        holder.tv_dept_name.setText(list.getCat_name());
       // setAnimation(holder.itemView,position);
        holder.linearLayout_select_dept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, DoctorActivity.class);
                context.startActivity(i);
                Constants.DEPARTMENT_ID = list.getCat_id();
                Constants.CHAMBER_ID=list.getChamber_id();
                //Log.e("ID sent DEPARTMENT_ID", Constants.DEPARTMENT_ID);
                //Toast.makeText(context, "Id" + holder.getAdapterPosition(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mediDeptSetterGetterArrayList.size();
    }

    class mediDeptViewHolder extends RecyclerView.ViewHolder {
        TextView tv_dept_name;
        LinearLayout linearLayout_select_dept;

        mediDeptViewHolder(View itemView) {
            super(itemView);
            tv_dept_name = itemView.findViewById(R.id.tv_dept_name);
            linearLayout_select_dept = itemView.findViewById(R.id.linearLayout_select_dept);
            Typeface typeface = Typeface.createFromAsset(itemView.getContext().getAssets(), Constants.BubblegumSans_Regular_font);
            tv_dept_name.setTypeface(typeface);
        }
    }

//    private void setAnimation(View viewToAnimate, int position)
//    {
//        // If the bound view wasn't previously displayed on screen, it's animated
//        if (position > lastPosition)
//        {
//            Animation animation = AnimationUtils.loadAnimation(context, R.anim.slide_bottom);
//            viewToAnimate.startAnimation(animation);
//            lastPosition = position;
//        }
//    }

//    private void setAnimation(View viewToAnimate, int position) {
//        // If the bound view wasn't previously displayed on screen, it's animated
//        if (position > lastPosition) {
//            ScaleAnimation anim = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
//            anim.setDuration(new Random().nextInt(501));//to make duration random number between [0,501)
//            viewToAnimate.startAnimation(anim);
//            lastPosition = position;
//        }
//    }

    private void setAnimation(View viewToAnimate, int position)
    {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition)
        {
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.slide_from_bottom);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

}
