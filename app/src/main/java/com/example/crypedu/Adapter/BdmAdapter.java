package com.example.crypedu.Adapter;

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
import com.example.crypedu.Pojo.BdmSetterGetter;

import java.util.ArrayList;

public class BdmAdapter extends RecyclerView.Adapter<BdmAdapter.bdmViewHolder> {
    private Context context;
    private ArrayList<BdmSetterGetter> bdmSetterGetterArrayList;

    public BdmAdapter(Context context, ArrayList<BdmSetterGetter> bdmSetterGetterArrayList) {
        this.context = context;
        this.bdmSetterGetterArrayList = bdmSetterGetterArrayList;
    }

    @NonNull
    @Override
    public bdmViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        View v = inflater.inflate(R.layout.single_item_meeting_details, parent, false);
        return new bdmViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull bdmViewHolder holder, int position) {
        BdmSetterGetter list = bdmSetterGetterArrayList.get(position);
        String date = "Date: ";
        holder.textView_meeting_date.setText(date.concat(list.getMeeting_date()));
        holder.textView_parents.setText(list.getGuardain_brf());
        holder.textView_teacher.setText(list.getTeacher_brf());
    }

    @Override
    public int getItemCount() {
        return bdmSetterGetterArrayList.size();
    }

    class bdmViewHolder extends RecyclerView.ViewHolder {
        private TextView textView_meeting_date, textView_parents, textView_teacher, teachers, parents;

        bdmViewHolder(View itemView) {
            super(itemView);
            textView_meeting_date = itemView.findViewById(R.id.textView_meeting_date);
            textView_parents = itemView.findViewById(R.id.textView_parents);
            textView_teacher = itemView.findViewById(R.id.textView_teacher);
            teachers = itemView.findViewById(R.id.teachers);
            parents = itemView.findViewById(R.id.parents);

            Typeface typeface = Typeface.createFromAsset(itemView.getContext().getAssets(), Constants.BubblegumSans_Regular_font);
            textView_meeting_date.setTypeface(typeface);
//            textView_teacher.setTypeface(typeface);
//            textView_parents.setTypeface(typeface);
            teachers.setTypeface(typeface);
            parents.setTypeface(typeface);

        }
    }
}
