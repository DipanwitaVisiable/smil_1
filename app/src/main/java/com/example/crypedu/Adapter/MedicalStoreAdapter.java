package com.example.crypedu.Adapter;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import androidx.annotation.NonNull;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.activity.smi.R;
import com.example.crypedu.Activity.MediStoreDetailsActivity;
import com.example.crypedu.Constants.Constants;
import com.example.crypedu.Pojo.MedicalStoreSetterGetter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by INDID on 08-01-2018.
 */

public class MedicalStoreAdapter extends RecyclerView.Adapter<MedicalStoreAdapter.ViewHolder> {
    private Context context;
    private ArrayList<MedicalStoreSetterGetter> medicalStoreSetterGetters;
    private PopupWindow popupWindow;
    private ArrayList<String> arrayList;

    public MedicalStoreAdapter(Context context, ArrayList<MedicalStoreSetterGetter> medicalStoreSetterGetters) {
        this.context = context;
        this.medicalStoreSetterGetters = medicalStoreSetterGetters;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        View v = inflater.inflate(R.layout.medical_store_one_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final MedicalStoreSetterGetter list = medicalStoreSetterGetters.get(position);
        holder.tv_store_name.setText(list.getStore_name());
        holder.tv_store_description.setText(list.getStore_description());
        holder.tv_store_address.setText(list.getStore_address());
        holder.tv_store_phone_no.setText(list.getStore_phn());

        Picasso.with(context)
                .load(list.getStore_image())
                .fit()
                .into(holder.iv_store_image);

        holder.linearLayout_medical_store.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, MediStoreDetailsActivity.class);
                context.startActivity(i);
                Constants.MEDICAL_STORE_ID = list.getStore_id();
                Log.e("sent MEDICAL_STORE_ID", Constants.MEDICAL_STORE_ID);
                //Toast.makeText(context, "Id" + holder.getAdapterPosition(), Toast.LENGTH_LONG).show();
            }
        });

        holder.iv_nextPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, MediStoreDetailsActivity.class);
                context.startActivity(i);
                Constants.MEDICAL_STORE_ID = list.getStore_id();
                Log.e("sent MEDICAL_STORE_ID", Constants.MEDICAL_STORE_ID);
            }
        });

        holder.iv_mediCallStore_phoneCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] numbers = list.getStore_phn().split("/"); // Split according to the hyphen and put them in an array
                Log.e("NUMBERS", Arrays.toString(numbers));
                arrayList = new ArrayList<>();
                for (String subString : numbers) { // Cycle through the array
                    System.out.println(subString);
                    arrayList.add(subString);
                    Log.e("Arraylist Medi Numbers", arrayList.toString());
                }
                if (popupWindow != null && popupWindow.isShowing()) {
                    Snackbar.make(v, "Already opened the Window.", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                } else {
                    Snackbar.make(v, "Choose Number for calling", Snackbar.LENGTH_SHORT).setAction("Action", null).show();

                    //instantiate the popup.xml layout file
                    LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    assert layoutInflater != null;
                    @SuppressLint("InflateParams") View customView = layoutInflater.inflate(R.layout.popup_window_medicalstore, null);
                    ImageView closePopupBtn = customView.findViewById(R.id.closePopupBtn);
                    ListView listView_number = customView.findViewById(R.id.listView_number);

                    ArrayAdapter<String> arrayAdapter =
                            new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, arrayList);
                    // Set The Adapter
                    listView_number.setAdapter(arrayAdapter);

                    // register onClickListener to handle click events on each item
                    listView_number.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        // argument position gives the index of item which is clicked
                        public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3) {

                            String selectedNumber = arrayList.get(position);
                            Toast.makeText(context, "Calling : " + selectedNumber, Toast.LENGTH_LONG).show();
                            Context context = v.getContext();
                            Intent intent = new Intent(Intent.ACTION_CALL,  Uri.parse("tel:" + selectedNumber));
                            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                Toast.makeText(context, "permission not granted", Toast.LENGTH_SHORT).show();
                                ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CALL_PHONE},100);
                            }else{
                                context.startActivity(intent);
                            }
                        }
                    });

                    //instantiate popup window
                    popupWindow = new PopupWindow(customView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    popupWindow.setAnimationStyle(R.style.DialogAnimation_2);
                    //display the popup window
                    popupWindow.showAtLocation(holder.linearLayout_medical_store, Gravity.CENTER, 0, 0);
                    //close the popup window on button click
                    closePopupBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            popupWindow.dismiss();
                        }
                    });

                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return medicalStoreSetterGetters.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_store_image, iv_mediCallStore_phoneCall, iv_nextPage;
        TextView tv_store_name, tv_store_description, tv_store_address, tv_store_phone_no;
        LinearLayout linearLayout_medical_store;

        ViewHolder(View itemView) {
            super(itemView);
            iv_store_image = itemView.findViewById(R.id.iv_store_image);
            iv_mediCallStore_phoneCall = itemView.findViewById(R.id.iv_mediCallStore_phoneCall);
            iv_nextPage = itemView.findViewById(R.id.iv_nextPage);
            linearLayout_medical_store = itemView.findViewById(R.id.linearLayout_medical_store);
            tv_store_name = itemView.findViewById(R.id.tv_store_name);
            tv_store_description = itemView.findViewById(R.id.tv_store_description);
            tv_store_address = itemView.findViewById(R.id.tv_store_address);
            tv_store_phone_no = itemView.findViewById(R.id.tv_store_phone_no);
            Typeface typeface = Typeface.createFromAsset(itemView.getContext().getAssets(), Constants.BubblegumSans_Regular_font);
            tv_store_name.setTypeface(typeface);
            tv_store_description.setTypeface(typeface);
            tv_store_address.setTypeface(typeface);
            tv_store_phone_no.setTypeface(typeface);
        }
    }
}
