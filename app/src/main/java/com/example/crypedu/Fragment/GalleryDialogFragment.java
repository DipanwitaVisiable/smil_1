package com.example.crypedu.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.activity.smi.R;
import com.example.crypedu.Pojo.Mobile;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Objects;

public class GalleryDialogFragment extends DialogFragment {

    Context context;
    private String TAG = GalleryDialogFragment.class.getSimpleName();
    private ArrayList<Mobile> images = new ArrayList<>();
    private ViewPager viewPager;
    private TextView lblCount, lblTitle, lblDate;
    //  page change listener
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            displayMetaInfo(position);
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };
    private int selectedPosition = 0;

    public static GalleryDialogFragment newInstance() {
        return new GalleryDialogFragment();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_dialog_gallery, container, false);

        viewPager = view.findViewById(R.id.viewpager);
        lblCount = view.findViewById(R.id.lbl_count);
        lblTitle = view.findViewById(R.id.title);
        lblDate = view.findViewById(R.id.date);

        Bundle bundle = getArguments();
        assert bundle != null;
        images = (ArrayList<Mobile>) bundle.getSerializable("images");
        selectedPosition = getArguments().getInt("position");

        //Log.e(TAG, "position: " + selectedPosition);
        //Log.e(TAG, "images size: " + images.size());

        MyViewPagerAdapter myViewPagerAdapter = new MyViewPagerAdapter();
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);

        setCurrentItem(selectedPosition);

        return view;
    }

    private void setCurrentItem(int position) {
        viewPager.setCurrentItem(position, false);
        displayMetaInfo(selectedPosition);
    }

    @SuppressLint("SetTextI18n")
    private void displayMetaInfo(int position) {
        lblCount.setText((position + 1) + " of " + images.size());
        Mobile image = images.get(position);
        lblTitle.setText(image.modelName);
        lblDate.setText(image.price);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
    }

    //  adapter
    public class MyViewPagerAdapter extends PagerAdapter {

        private LayoutInflater layoutInflater;

        MyViewPagerAdapter() {
        }

        @SuppressLint("ClickableViewAccessibility")
        @NonNull
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {

            layoutInflater = (LayoutInflater) Objects.requireNonNull(getActivity()).getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            assert layoutInflater != null;
            View view = layoutInflater.inflate(R.layout.gallery_image_preview, container, false);

            final ImageView imageViewPreview = view.findViewById(R.id.image_preview);
            ImageView imageViewClose = view.findViewById(R.id.imageClose);

            imageViewClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });

            Mobile image = images.get(position);
            //imageViewPreview.setImage(ImageSource.uri(Uri.parse(image.getImageResource())));

            Picasso.with(context)
                    .load(image.imageResource)
                    .noFade()
                    .into(imageViewPreview);

            container.addView(view);

            return view;
        }

        @Override
        public int getCount() {
            return images.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object obj) {
            return view == obj;
        }


        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }

    }

}
