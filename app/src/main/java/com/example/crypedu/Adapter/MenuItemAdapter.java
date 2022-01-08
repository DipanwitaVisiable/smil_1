package com.example.crypedu.Adapter;
/*
 *************************************************************************************************
 * Developed by Cryptonsoftech
 * Date: 30-05-2017
 * Details:
 * ***********************************************************************************************
 */
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.activity.smi.R;
import com.example.crypedu.Constants.Constants;
import java.util.List;

public class MenuItemAdapter extends BaseAdapter {
    private final Context context;
    private final List<String> stringInfoList;
    LayoutInflater mInflater;
    LayoutInflater inflater;

    public MenuItemAdapter(Context context, List<String> stringInfoList, LayoutInflater inflater) {
        mInflater = inflater;
        this.context = context;
        this.stringInfoList = stringInfoList;
    }

    @Override
    public int getCount() {
        return stringInfoList.size();
    }

    @Override
    public Object getItem(int position) {
        return stringInfoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View itemView;
        assert inflater != null;
        itemView = inflater.inflate(R.layout.menu_item_adapter, parent, false);
        TextView textView = itemView.findViewById(R.id.textView);
        ImageView item_imageView = itemView.findViewById(R.id.item_imageView);
        Typeface typeface= Typeface.createFromAsset(itemView.getContext().getAssets(), Constants.BubblegumSans_Regular_font);
        final String info = stringInfoList.get(position);

        if (!Constants.USER_ROLE.equalsIgnoreCase("") || Constants.USER_ROLE != null){
            if (Constants.USER_ROLE.equalsIgnoreCase("t")){
                // Keep all Images in array
                Integer[] menuItemImage = {
                        R.drawable.ic_menu_profile,
                        R.drawable.ic_menu_assignment,
                        R.drawable.ic_menu_attendance,
                        R.drawable.ic_menu_notice,
                        R.drawable.ic_menu_bulletins,
                        R.drawable.ic_menu_syllabus,
                        R.drawable.ic_menu_book,
                        R.drawable.ic_menu_reset,
                        R.drawable.ic_menu_logout
                };

                final int imagePosition = menuItemImage[position];
                item_imageView.setImageResource(imagePosition);
                textView.setText(info);
                textView.setTypeface(typeface);
            }else if (Constants.USER_ROLE.equalsIgnoreCase("s")){
                // Keep all Images in array
                Integer[] menuItemImage = {
                        R.drawable.ic_menu_profile,
                        R.drawable.ic_menu_assignment,
                        R.drawable.ic_menu_attendance,
                        R.drawable.ic_menu_time,
                        R.drawable.ic_menu_notice,
                        R.drawable.ic_menu_bulletins,
                        R.drawable.ic_menu_account,
                        R.drawable.ic_menu_syllabus,
                        R.drawable.ic_menu_book,
                        R.drawable.ic_menu_exam,
                        R.drawable.ic_menu_request,
                        R.drawable.ic_menu_reset,
                        R.drawable.ic_menu_logout,
                        R.drawable.ic_menu_transportation
                };

                final int imagePosition = menuItemImage[position];
                item_imageView.setImageResource(imagePosition);
                textView.setText(info);
                textView.setTypeface(typeface);
            }
        }
        return itemView;
    }
}

