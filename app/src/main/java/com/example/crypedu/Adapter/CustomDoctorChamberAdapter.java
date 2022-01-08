package com.example.crypedu.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.activity.smi.R;
import com.example.crypedu.Constants.Constants;
import com.example.crypedu.Pojo.DoctorSetterGetter;

import java.util.ArrayList;
import java.util.List;

public class CustomDoctorChamberAdapter extends RecyclerView.Adapter<CustomDoctorChamberAdapter.ViewHolder>{

    private Context context;
    private List<DoctorSetterGetter> setterGetters;

    public CustomDoctorChamberAdapter(Context context, ArrayList<DoctorSetterGetter> setterGetters){
        //Log.e("Constructor ", "true");
        this.context = context;
        this.setterGetters = setterGetters;
    }
    @NonNull
    @Override
    public CustomDoctorChamberAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Log.e("View ", "true");
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        View view = inflater.inflate(R.layout.custom_doctor_chamber_list, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull CustomDoctorChamberAdapter.ViewHolder holder, int position) {
        //Log.e("Binder ", "true");
        DoctorSetterGetter setterGetter = setterGetters.get(position);
        holder.service_name.setText(setterGetter.getDoctor_service_name());
        holder.discount.setText(setterGetter.getDoctor_discount()+"%");
    }

    @Override
    public int getItemCount() {
        //Log.e("Size ", String.valueOf(setterGetters.size()));
        return setterGetters.size();
    }
    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView service_name, discount;

        ViewHolder(View itemView) {
            super(itemView);
            //Log.e("View ", "true");
            service_name = itemView.findViewById(R.id.service_name);
            discount = itemView.findViewById(R.id.doctor_discount);
            Typeface typeface = Typeface.createFromAsset(itemView.getContext().getAssets(), Constants.BubblegumSans_Regular_font);
            discount.setTypeface(typeface);
        }
    }
}
