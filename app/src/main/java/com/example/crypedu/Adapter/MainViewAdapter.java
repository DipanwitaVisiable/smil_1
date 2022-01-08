package com.example.crypedu.Adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.activity.smi.R;
import com.example.crypedu.Pojo.ListSetGet;

import java.util.List;

public class MainViewAdapter extends RecyclerView.Adapter<MainViewAdapter.mainViewHolder> {
    Context context;
    List<ListSetGet> viewList;
    public MainViewAdapter(Context context, List<ListSetGet> viewList) {
        this.context = context;
        this.viewList = viewList;
    }

    @NonNull
    @Override
    public MainViewAdapter.mainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        LayoutInflater inflater= (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.image_adapter,parent,false);
        return new mainViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MainViewAdapter.mainViewHolder holder, int position) {
        holder.textView.setText(viewList.get(position).getMainName());
        holder.imageView.setImageResource(viewList.get(position).getMainImage());
        if (viewList.get(position).getMainName().equalsIgnoreCase("Profile")){
            holder.tv_noti_count.setVisibility(View.GONE);
            holder. textView.setTextColor(context.getResources().getColor(R.color.colorProfile));

        /*}else if (viewList.get(position).getMainName().equalsIgnoreCase("Mediwallet")){
            holder.tv_noti_count.setVisibility(View.GONE);
            holder. textView.setTextColor(context.getResources().getColor(R.color.colorAbsent));*/

        }else if (viewList.get(position).getMainName().equalsIgnoreCase("Assignment")){
           // holder.tv_noti_count.setVisibility(View.GONE);
            holder. textView.setTextColor(context.getResources().getColor(R.color.colorHomeWork));
            if (!viewList.get(position).getNotiCount().equalsIgnoreCase("0")){
                holder.tv_noti_count.setVisibility(View.VISIBLE);
                holder.tv_noti_count.setText(viewList.get(position).getNotiCount());

            }else {
                 holder.tv_noti_count.setVisibility(View.GONE);

            }

        }else if (viewList.get(position).getMainName().equalsIgnoreCase("Attendance")){
           // holder.tv_noti_count.setVisibility(View.GONE);
            holder. textView.setTextColor(context.getResources().getColor(R.color.colorAttendance));
            if (!viewList.get(position).getNotiCount().equalsIgnoreCase("0")){
                holder.tv_noti_count.setVisibility(View.VISIBLE);
                holder.tv_noti_count.setText(viewList.get(position).getNotiCount());

            }else {
                holder.tv_noti_count.setVisibility(View.GONE);

            }

        }else if (viewList.get(position).getMainName().equalsIgnoreCase("Time Table")){
            holder.tv_noti_count.setVisibility(View.GONE);
            holder. textView.setTextColor(context.getResources().getColor(R.color.colorTimeTable));

        }else if (viewList.get(position).getMainName().equalsIgnoreCase("Notice")){
            // holder.tv_noti_count.setVisibility(View.GONE);
            holder. textView.setTextColor(context.getResources().getColor(R.color.colorNotice));

            if (!viewList.get(position).getNotiCount().equalsIgnoreCase("0")){
                holder.tv_noti_count.setVisibility(View.VISIBLE);
                holder.tv_noti_count.setText(viewList.get(position).getNotiCount());

            }else {
                holder.tv_noti_count.setVisibility(View.GONE);

            }

        }else if (viewList.get(position).getMainName().equalsIgnoreCase("Bulletins")){
            // holder.tv_noti_count.setVisibility(View.GONE);
            holder. textView.setTextColor(context.getResources().getColor(R.color.colorBulletins));

            if (!viewList.get(position).getNotiCount().equalsIgnoreCase("0")){
                holder.tv_noti_count.setVisibility(View.VISIBLE);
                holder.tv_noti_count.setText(viewList.get(position).getNotiCount());

            }else {
                holder.tv_noti_count.setVisibility(View.GONE);

            }

        }else if (viewList.get(position).getMainName().equalsIgnoreCase("Online Classes")){
            holder.tv_noti_count.setVisibility(View.GONE);
            holder. textView.setTextColor(context.getResources().getColor(R.color.colorOnlineClasses));

        }else if (viewList.get(position).getMainName().equalsIgnoreCase("Online Test")){
            holder.tv_noti_count.setVisibility(View.GONE);
            holder. textView.setTextColor(context.getResources().getColor(R.color.colorReset));

        }else if (viewList.get(position).getMainName().equalsIgnoreCase("Classroom Exam")){
            holder.tv_noti_count.setVisibility(View.GONE);
            holder. textView.setTextColor(context.getResources().getColor(R.color.colorReset));

        }else if (viewList.get(position).getMainName().equalsIgnoreCase("Syllabus")){
            holder.tv_noti_count.setVisibility(View.GONE);
            holder. textView.setTextColor(context.getResources().getColor(R.color.colorSyllabus));

        /*}else if (viewList.get(position).getMainName().equalsIgnoreCase("Library")){
            holder.tv_noti_count.setVisibility(View.GONE);
            holder. textView.setTextColor(context.getResources().getColor(R.color.colorLibrary));*/

        }else if (viewList.get(position).getMainName().equalsIgnoreCase("Pay Online")){
            holder.tv_noti_count.setVisibility(View.GONE);
            holder. textView.setTextColor(context.getResources().getColor(R.color.colorAccount));

        }else if (viewList.get(position).getMainName().equalsIgnoreCase("Examination")){
            // holder.tv_noti_count.setVisibility(View.GONE);
            holder. textView.setTextColor(context.getResources().getColor(R.color.colorExamination));

            if (!viewList.get(position).getNotiCount().equalsIgnoreCase("0")){
                holder.tv_noti_count.setVisibility(View.VISIBLE);
                holder.tv_noti_count.setText(viewList.get(position).getNotiCount());

            }else {
                holder.tv_noti_count.setVisibility(View.GONE);

            }

        }else if (viewList.get(position).getMainName().equalsIgnoreCase("Results")){
            holder.tv_noti_count.setVisibility(View.GONE);
            holder. textView.setTextColor(context.getResources().getColor(R.color.colorResult));

        }else if (viewList.get(position).getMainName().equalsIgnoreCase("Communication")){
            // holder.tv_noti_count.setVisibility(View.GONE);
            holder. textView.setTextColor(context.getResources().getColor(R.color.colorRequest));

            if (!viewList.get(position).getNotiCount().equalsIgnoreCase("0")){
                holder.tv_noti_count.setVisibility(View.VISIBLE);
                holder.tv_noti_count.setText(viewList.get(position).getNotiCount());

            }else {
                holder.tv_noti_count.setVisibility(View.GONE);

            }

        }else if (viewList.get(position).getMainName().equalsIgnoreCase("Transportation")){
            holder.tv_noti_count.setVisibility(View.GONE);
            holder. textView.setTextColor(context.getResources().getColor(R.color.colorTransportation));

        }else if (viewList.get(position).getMainName().equalsIgnoreCase("PTM")){
            holder.tv_noti_count.setVisibility(View.GONE);
            holder. textView.setTextColor(context.getResources().getColor(R.color.colorT));

        }else if (viewList.get(position).getMainName().equalsIgnoreCase("Zoom Meetings")){
          holder.tv_noti_count.setVisibility(View.GONE);
          holder. textView.setTextColor(context.getResources().getColor(R.color.colorTeacherCalling));

        }else if (viewList.get(position).getMainName().equalsIgnoreCase("Reset")) {
            holder.tv_noti_count.setVisibility(View.GONE);
            holder.textView.setTextColor(context.getResources().getColor(R.color.colorReset));

        } else if (viewList.get(position).getMainName().equalsIgnoreCase("Logout")) {
            holder.tv_noti_count.setVisibility(View.GONE);
            holder.textView.setTextColor(context.getResources().getColor(R.color.colorLogout));

        }


    }

    @Override
    public int getItemCount() {
        return viewList.size();
    }
    class mainViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView tv_noti_count,textView;

        public mainViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.imageView);
            tv_noti_count=itemView.findViewById(R.id.tv_noti_count);
            textView=itemView.findViewById(R.id.textView);

        }
    }
}
