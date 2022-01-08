package com.example.crypedu.Adapter;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import com.example.crypedu.Pojo.SubSetGet;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by INDID on 27-03-2018.
 */

public class SubRvAdapter extends RecyclerView.Adapter<SubRvAdapter.viewHolder> {
    private Context context;
    private ArrayList<SubSetGet> subSetGetArrayList;
    private PopupWindow popupWindow;
    private ArrayList<String> arrayList;

    SubRvAdapter(Context context, ArrayList<SubSetGet> subSetGetArrayList) {
        this.context = context;
        this.subSetGetArrayList = subSetGetArrayList;
    }


    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        View v = inflater.inflate(R.layout.single_sub_row_emergency, parent, false);
        return new viewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final viewHolder holder, int position) {
        final SubSetGet setget = subSetGetArrayList.get(position);
        holder.tv_id.setText(setget.getEmrg_id());
        holder.tv_add.setText(setget.getEmrg_add());
        holder.tv_name.setText(setget.getEmrg_name());
        holder.linearLayout_emergency_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] numbers = setget.getEmrg_id().split("/"); // Split according to the hyphen and put them in an array
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
                    popupWindow.showAtLocation(holder.linearLayout_sub_row, Gravity.CENTER, 0, 0);
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
        return subSetGetArrayList.size();
    }

    class viewHolder extends RecyclerView.ViewHolder {
        TextView tv_name, tv_id, tv_add;
        LinearLayout linearLayout_emergency_call,linearLayout_sub_row;

        viewHolder(View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_id = itemView.findViewById(R.id.tv_id);
            tv_add = itemView.findViewById(R.id.tv_add);
            linearLayout_emergency_call = itemView.findViewById(R.id.linearLayout_emergency_call);
            linearLayout_sub_row = itemView.findViewById(R.id.linearLayout_sub_row);

        }
    }


}
