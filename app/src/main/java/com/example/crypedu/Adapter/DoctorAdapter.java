package com.example.crypedu.Adapter;

import android.content.Context;
import android.graphics.Typeface;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.activity.smi.R;
import com.example.crypedu.Constants.Constants;
import com.example.crypedu.Pojo.DoctorSetterGetter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by INDID on 08-01-2018.
 */

public class DoctorAdapter extends RecyclerView.Adapter<DoctorAdapter.viewholder> {
     private Context context;
     private ArrayList<DoctorSetterGetter> doctorSetterGetters;


    public DoctorAdapter(Context context, ArrayList<DoctorSetterGetter> doctorSetterGetters) {
        this.context = context;
        this.doctorSetterGetters = doctorSetterGetters;

    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        View v = inflater.inflate(R.layout.doctor_single_item, parent, false);
        return new viewholder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull  viewholder holder, int position) {

        DoctorSetterGetter list = doctorSetterGetters.get(position);
        holder.tv_doctor_discount.setText(String.format("%s%%", list.getDoctor_discount()));
        holder.tv_doctor_name.setText(list.getDoctor_name());
        holder.tv_doctor_cat_name.setText(list.getDoctor_cat_name());
        holder.tv_doctor_degree.setText(list.getDoctor_degree());

        Picasso.with(context)
                .load(list.getDoctor_image())
                .placeholder(R.drawable.placeholder1)
                .into(holder.iv_doctor_image);
        //Picasso.get().load(R.drawable.drawableName).into(imageView);

        //PicassoProvider.get().load(list.getDoctor_image()).fit().centerInside().into(holder.iv_doctor_image);

    }

    @Override
    public int getItemCount() {
        return doctorSetterGetters.size();
    }

    class viewholder extends RecyclerView.ViewHolder {
        ImageView iv_doctor_image;
        TextView tv_doctor_discount, tv_doctor_name, tv_doctor_degree, tv_doctor_cat_name,  discount;

        viewholder(View itemView) {
            super(itemView);
            iv_doctor_image = itemView.findViewById(R.id.iv_doctor_image);
            tv_doctor_discount = itemView.findViewById(R.id.tv_doctor_discount);
            tv_doctor_name = itemView.findViewById(R.id.tv_doctor_name);
            tv_doctor_degree = itemView.findViewById(R.id.tv_doctor_degree);
            tv_doctor_cat_name = itemView.findViewById(R.id.tv_doctor_cat_name);
            discount = itemView.findViewById(R.id.discount);
            Typeface typeface = Typeface.createFromAsset(itemView.getContext().getAssets(), Constants.BubblegumSans_Regular_font);
            discount.setTypeface(typeface);
            tv_doctor_discount.setTypeface(typeface);
            tv_doctor_name.setTypeface(typeface);
            tv_doctor_degree.setTypeface(typeface);
            tv_doctor_cat_name.setTypeface(typeface);

        }
    }


}



