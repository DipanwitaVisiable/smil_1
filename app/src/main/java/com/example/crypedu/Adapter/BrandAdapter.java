package com.example.crypedu.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.activity.smi.R;
import com.example.crypedu.Activity.ViewPagerActivity;
import com.example.crypedu.Pojo.Brand;
import com.example.crypedu.Pojo.Mobile;

import java.util.ArrayList;
import java.util.List;

import static com.example.crypedu.Constants.Constants.galleryFlag;

/**
 * Created by root on 2/3/16.
 */
public class BrandAdapter extends FragmentPagerAdapter implements ExpandableListAdapter {

    private Context context;
    private List<Brand> brands;

    public BrandAdapter(Context context, List<Brand> brands, FragmentManager manager) {
        super(manager);
        this.context = context;
        this.brands = brands;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public void registerDataSetObserver(@NonNull DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(@NonNull DataSetObserver observer) {

    }

    @Override
    public int getGroupCount() {
        return brands.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return brands.get(groupPosition).getBrandName().getCategory();
    }

    @Override
    public List<Mobile> getChild(int groupPosition, int childPosition) {
        return brands.get(groupPosition).getMobiles();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        ParentHolder parentHolder;

        if(convertView == null) {
            LayoutInflater userInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            assert userInflater != null;
            convertView = userInflater.inflate(R.layout.gallery_parent, parent, false);
            convertView.setHorizontalScrollBarEnabled(true);

            parentHolder = new ParentHolder();
            convertView.setTag(parentHolder);

        } else {
            parentHolder = (ParentHolder) convertView.getTag();
        }
        if (isExpanded){
            galleryFlag = groupPosition;
            //Log.e("FLAG ", String.valueOf(galleryFlag));
        }

        parentHolder.brandName = convertView.findViewById(R.id.text_brand);
        parentHolder.brandName.setText(getGroup(groupPosition).toString());
        //parentHolder.brandName.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        parentHolder.indicator = convertView.findViewById(R.id.image_indicator);

        if(isExpanded) {
            parentHolder.indicator.setImageResource(R.drawable.arrow_up);
        } else {
            parentHolder.indicator.setImageResource(R.drawable.arrow_down);
        }

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            assert inflater != null;
            convertView = inflater.inflate(R.layout.gallery_child, parent, false);
        }

        RecyclerView horizontalListView = convertView.findViewById(R.id.mobiles);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        horizontalListView.setLayoutManager(layoutManager);
        ViewCompat.setNestedScrollingEnabled(horizontalListView, false);

        MobileAdapter horizontalListAdapter = new MobileAdapter(context, getChild(groupPosition, 0));
        horizontalListView.setAdapter(horizontalListAdapter);
        /*horizontalListView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            GestureDetector gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent motionEvent) {
                    return true;
                }

            });
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent motionEvent) {

                View view1 = rv.findChildViewUnder(motionEvent.getX(), motionEvent.getY());

                if (view1 != null && gestureDetector.onTouchEvent(motionEvent)) {

                    int recyclerViewPosition = rv.getChildAdapterPosition(view1);
                    Intent intent = new Intent(context, ViewPagerActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList("images", (ArrayList<? extends Parcelable>) brands.get(galleryFlag).getMobiles());
                    bundle.putInt("position", recyclerViewPosition);
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                    ((Activity)context).finish();

                }
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });*/
        horizontalListView.addOnItemTouchListener(new MobileAdapter.RecyclerTouchListener(context, horizontalListView, new MobileAdapter.ClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view, int position) {
                /*Bundle bundle = new Bundle();
                bundle.putSerializable("images", (Serializable) brands.get(galleryFlag).getMobiles());
                bundle.putInt("position", position);
                Log.e("Brand Adapter image ", brands.get(galleryFlag).getMobiles().get(position).imageResource);

                FragmentTransaction ft = manager.beginTransaction().addToBackStack(null);
                GalleryDialogFragment newFragment = GalleryDialogFragment.newInstance();
                newFragment.setArguments(bundle);
                newFragment.show(ft, "slideshow");*/



                Intent intent = new Intent(context, ViewPagerActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("images", (ArrayList<? extends Parcelable>) brands.get(galleryFlag).getMobiles());
                bundle.putInt("position", position);
                intent.putExtras(bundle);
                context.startActivity(intent);
                ((Activity)context).finish();

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public void onGroupExpanded(int groupPosition) {

    }

    @Override
    public void onGroupCollapsed(int groupPosition) {

    }

    @Override
    public long getCombinedChildId(long groupId, long childId) {
        return 0;
    }

    @Override
    public long getCombinedGroupId(long groupId) {
        return 0;
    }

    @Override
    public Fragment getItem(int position) {
        return null;
    }

    private static class ParentHolder {
        TextView brandName;
        ImageView indicator;
    }
}
