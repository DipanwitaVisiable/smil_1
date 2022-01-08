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
import com.example.crypedu.Pojo.PathologyServiceSetterGetter;

import java.util.ArrayList;
import java.util.List;

public class CustomSingleLabAdapter extends RecyclerView.Adapter<CustomSingleLabAdapter.ViewHolder> {
    Context context;
    private List<PathologyServiceSetterGetter> setterGetterArrayList;

    public CustomSingleLabAdapter(Context context, ArrayList<PathologyServiceSetterGetter> setterGetterArrayList) {
       // Log.e("Adapter call ", "true");
        this.context = context;
        this.setterGetterArrayList = setterGetterArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        View v = inflater.inflate(R.layout.custom_pathology_service_lab, parent, false);
        //Constants.backFlag = 0;
        return new ViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final PathologyServiceSetterGetter list = setterGetterArrayList.get(position);
        holder.tv_pathology_service.setText(list.getPathology_service_name());
        holder.tv_pathology_discount.setText(list.getPathology_discount()+"%");
    }

    @Override
    public int getItemCount() {
        //Log.e("Size ", String.valueOf(setterGetterArrayList.size()));
        return setterGetterArrayList.size();
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_pathology_service, tv_pathology_discount;
        ViewHolder(View itemView) {
            super(itemView);
            //Log.e("View call ", "true");
            tv_pathology_service = itemView.findViewById(R.id.tv_pathology_service);
            tv_pathology_discount = itemView.findViewById(R.id.tv_pathology_discount);
            Typeface typeface = Typeface.createFromAsset(itemView.getContext().getAssets(), Constants.BubblegumSans_Regular_font);
            tv_pathology_service.setTypeface(typeface);
            tv_pathology_discount.setTypeface(typeface);
        }
    }

}
