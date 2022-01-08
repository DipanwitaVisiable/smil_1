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
import com.example.crypedu.Constants.Constants;
import com.example.crypedu.Pojo.PathologySetterGetter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by sudipta on 08-01-2018.
 */


public class PathologyAdapter extends RecyclerView.Adapter<PathologyAdapter.ViewHolder> {
    private Context context;
    private ArrayList<PathologySetterGetter> pathologySetterGetters;
    private PopupWindow popupWindow;
    private ArrayList<String> arrayList;

    public PathologyAdapter(Context context, ArrayList<PathologySetterGetter> pathologySetterGetters) {
        this.context = context;
        this.pathologySetterGetters = pathologySetterGetters;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        View v = inflater.inflate(R.layout.pathology_single_item, parent, false);
        return new ViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final PathologySetterGetter list = pathologySetterGetters.get(position);
        holder.tv_pathology_discount.setText(list.getPathology_discount() + "%");
        holder.tv_pathology_name.setText(list.getPathology_name());
        holder.tv_pathology_description.setText(list.getPathology_description());
        holder.tv_pathology_address.setText(list.getPathology_address());
        holder.tv_pathology_phone_no.setText(list.getPathology_phn());

        Log.e("PATHOLOGY IMAGES", list.getPathology_image());
        Picasso.with(context)
                .load(list.getPathology_image())
                .placeholder(R.drawable.placeholder1)
                .into(holder.iv_pathology_image);

        holder.iv_pathology_phoneCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] numbers = list.getPathology_phn().split("/"); // Split according to the hyphen and put them in an array
                Log.e("NUMBERS", Arrays.toString(numbers));
                arrayList = new ArrayList<>();
                for (String subString : numbers) { // Cycle through the array
                    System.out.println(subString);
                    arrayList.add(subString);
                    Log.e("Arraylist Patho Numbers", arrayList.toString());
                }
                if (popupWindow != null && popupWindow.isShowing()) {
                    Snackbar.make(v, "Already opened the Window.", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                } else {
                    Snackbar.make(v, "Choose Number for calling", Snackbar.LENGTH_SHORT).setAction("Action", null).show();

                    //instantiate the popup.xml layout file
                    LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    assert layoutInflater != null;
                    @SuppressLint("InflateParams")
                    View customView = layoutInflater.inflate(R.layout.popup_window_pathology, null);
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
                            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + selectedNumber));
                            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                Toast.makeText(context, "permission not granted", Toast.LENGTH_SHORT).show();
                                ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CALL_PHONE}, 100);
                            } else {
                                context.startActivity(intent);
                            }
                        }
                    });

                    //instantiate popup window
                    popupWindow = new PopupWindow(customView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    popupWindow.setAnimationStyle(R.style.DialogAnimation_2);
                    // Closes the popup window when touch outside.
                    popupWindow.setOutsideTouchable(true);
                    popupWindow.setFocusable(true);
                    //display the popup window
                    popupWindow.showAtLocation(holder.linearLayout_pathology_item, Gravity.CENTER, 0, 0);
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
        return pathologySetterGetters.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_pathology_image, iv_pathology_phoneCall;
        TextView tv_pathology_discount, tv_pathology_name, tv_pathology_description, tv_pathology_address, tv_pathology_phone_no, discount;
        LinearLayout linearLayout_pathology_item;

        ViewHolder(View itemView) {
            super(itemView);
            linearLayout_pathology_item = itemView.findViewById(R.id.linearLayout_pathology_item);
            iv_pathology_image = itemView.findViewById(R.id.iv_pathology_image);
            iv_pathology_phoneCall = itemView.findViewById(R.id.iv_pathology_phoneCall);
            tv_pathology_discount = itemView.findViewById(R.id.tv_pathology_discount);
            tv_pathology_name = itemView.findViewById(R.id.tv_pathology_name);
            tv_pathology_description = itemView.findViewById(R.id.tv_pathology_description);
            tv_pathology_address = itemView.findViewById(R.id.tv_pathology_address);
            tv_pathology_phone_no = itemView.findViewById(R.id.tv_pathology_phone_no);
            discount = itemView.findViewById(R.id.discount);
            Typeface typeface = Typeface.createFromAsset(itemView.getContext().getAssets(), Constants.BubblegumSans_Regular_font);
            discount.setTypeface(typeface);
            tv_pathology_discount.setTypeface(typeface);
            tv_pathology_name.setTypeface(typeface);
            tv_pathology_description.setTypeface(typeface);
            tv_pathology_address.setTypeface(typeface);
            tv_pathology_phone_no.setTypeface(typeface);

        }

    }
}
