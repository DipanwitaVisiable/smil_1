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
import com.example.crypedu.Pojo.MediStoreDetailsSetterGetter;

import java.util.ArrayList;

/**
 * Created by INDID on 27-02-2018.
 */

public class MediStoreDetailsAdapter extends RecyclerView.Adapter<MediStoreDetailsAdapter.mediStoreDetailsViewHolder> {
    Context context;
    private ArrayList<MediStoreDetailsSetterGetter> mediStoreDetailsSetterGetterArrayList;

    public MediStoreDetailsAdapter(Context context, ArrayList<MediStoreDetailsSetterGetter> mediStoreDetailsSetterGetterArrayList) {
        this.context = context;
        this.mediStoreDetailsSetterGetterArrayList = mediStoreDetailsSetterGetterArrayList;
    }

    @NonNull
    @Override
    public mediStoreDetailsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        View v = inflater.inflate(R.layout.medi_store_details_single_item, parent, false);
        return new mediStoreDetailsViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull mediStoreDetailsViewHolder holder, int position) {
        final MediStoreDetailsSetterGetter list = mediStoreDetailsSetterGetterArrayList.get(position);
        holder.tv_med_disc.setText("Discount: "+list.getMedicine_discount()+"%");
        holder.tv_med_name.setText(list.getMedicine_name());
    }

    @Override
    public int getItemCount() {
        return mediStoreDetailsSetterGetterArrayList.size();
    }

    class mediStoreDetailsViewHolder extends RecyclerView.ViewHolder {
        TextView tv_med_name, tv_med_disc;

        mediStoreDetailsViewHolder(View itemView) {
            super(itemView);
            tv_med_name = itemView.findViewById(R.id.tv_med_name);
            tv_med_disc = itemView.findViewById(R.id.tv_med_disc);
            Typeface typeface = Typeface.createFromAsset(itemView.getContext().getAssets(), Constants.BubblegumSans_Regular_font);
            tv_med_name.setTypeface(typeface);
            tv_med_disc.setTypeface(typeface);

        }
    }
}
