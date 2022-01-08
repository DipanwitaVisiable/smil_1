package com.example.crypedu.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.viewpager.widget.PagerAdapter;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.activity.smi.InternetCheckActivity;
import com.activity.smi.R;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.example.crypedu.Pojo.Mobile;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;

public class ViewPagerActivity extends AppCompatActivity {

    /**
     * Step 1: Download and set up v4 support library: http://developer.android.com/tools/support-library/setup.html
     * Step 2: Create ExtendedViewPager wrapper which calls TouchImageView.canScrollHorizontallyFroyo
     * Step 3: ExtendedViewPager is a custom view and must be referred to by its full package name in XML
     * Step 4: Write TouchImageAdapter, located below
     * Step 5. The ViewPager in the XML should be ExtendedViewPager
     */

    Intent intent;
    private ArrayList<Mobile> images = new ArrayList<>();
    int selectedPosition;
    ExtendedViewPager mViewPager;
    private TextView lblCount, lblTitle, lblDate;
    TouchImageView img;
    ProgressBar progressBar;
    View itemView;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewpager);
        /*if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }  // To prevent screen recording*/


        //img = new TouchImageView(this);
        progressBar = findViewById(R.id.image_load);
        //make translucent statusBar on kitkat devices
        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
        }
        if (Build.VERSION.SDK_INT >= 19) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        //make fully Android Transparent Status bar
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ViewPagerActivity.this, GalleyActivity.class));
                finish();
                onBackPressed();
            }
        });

        //lblCount = findViewById(R.id.lbl_count);
        intent = getIntent();
        Bundle bundle = intent.getExtras();
        assert bundle != null;
        images = bundle.getParcelableArrayList("images");
        selectedPosition = bundle.getInt("position");
        mViewPager = findViewById(R.id.view_pager);
        mViewPager.setAdapter(new TouchImageAdapter(this));
        //mViewPager.setCurrentItem(selectedPosition);
        /*mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                getBitmapFromURL(images.get(position).imageResource);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });*/
        setCurrentItem(selectedPosition);
    }

    public static void setWindowFlag(Activity activity, final int bits, boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    private void setCurrentItem(int position) {
        mViewPager.setCurrentItem(position);
        Log.e("Position ", String.valueOf(position));
        //displayMetaInfo(selectedPosition);
    }

    @SuppressLint("SetTextI18n")
    private void displayMetaInfo(int position) {
        lblCount.setText((position + 1) + " of " + images.size());
        Mobile image = images.get(position);
        lblTitle.setText(image.modelName);
        lblDate.setText(image.price);
    }

    @SuppressLint("StaticFieldLeak")
    class ImageAsync extends AsyncTask<String, Void, Bitmap> {
        RequestQueue requestQueue;
        Bitmap bitmap;
        Context context;

        ImageAsync(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            requestQueue = Volley.newRequestQueue(context);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Bitmap doInBackground(String... url) {
            // Initialize a new ImageRequest
            /*ImageRequest imageRequest = new ImageRequest(url[0], new Response.Listener<Bitmap>() {
                @Override
                public void onResponse(Bitmap response) {
                    bitmap = response;
                }
            }, 0, 0, ImageView.ScaleType.CENTER_CROP, Bitmap.Config.RGB_565,
                    new Response.ErrorListener() { // Error listener
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    });
            requestQueue.add(imageRequest);
            return bitmap;*/
            try {
                //Log.e("URL 1 ", src);
                String urll = url[0];
                URL url1 = new URL(urll);
                HttpURLConnection connection = (HttpURLConnection) url1.openConnection();
                //connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                bitmap = BitmapFactory.decodeStream(input);
                connection.disconnect();
                input.close();
            } catch (Exception e) {
                String err = (e.getMessage() == null) ? "connection failed" : e.getMessage();
                Log.e("Error 1 ", err);
                Toast.makeText(getApplicationContext(), err, Toast.LENGTH_LONG).show();

            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap o) {
            if (o != null) {
                img.setImageBitmap(o);
            } else {
                Toast.makeText(context, "Network problem", Toast.LENGTH_LONG).show();
            }
            progressBar.setVisibility(View.GONE);
        }
    }

    int counter;
    void getBit(String src) {
        Bitmap bitmap = null;
        String err = "";
        try {
            progressBar.setVisibility(View.VISIBLE);
            //Log.e("URL 1 ", src);
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            if (InternetCheckActivity.isConnected()) {
                connection.setDoInput(true);
                connection.connect();
            }
            InputStream input = connection.getInputStream();
            bitmap = BitmapFactory.decodeStream(input);
            img.setImageBitmap(bitmap);
            progressBar.setVisibility(View.GONE);
            //return bitmap;
        } catch (Exception e) {
            err = (e.getMessage() == null) ? "No internet" : "";
            Log.e("Error 1 ", err);
            Toast.makeText(getApplicationContext(), err, Toast.LENGTH_LONG).show();
            progressBar.setVisibility(View.GONE);
        }
        /*if (err.equalsIgnoreCase("No internet") && counter <= 2) {
            getBit(src);
            counter++;
        }*/
//        return bitmap;
    }

    void getBitmapFromURL(String src, final String po) {

        Log.e("Url ", src);
        progressBar.setVisibility(View.VISIBLE);
        // Initialize a new RequestQueue instance
        RequestQueue requestQueue = Volley.newRequestQueue(ViewPagerActivity.this);

        // Initialize a new ImageRequest
        ImageRequest imageRequest = new ImageRequest(src, // Image URL
                new Response.Listener<Bitmap>() { // Bitmap listener
                    @Override
                    public void onResponse(Bitmap response) {
                        // Do something with response
                        progressBar.setVisibility(View.GONE);
                        img.setImageBitmap(response);
                        Log.e("Image ", "set " + po);
                        // Save this downloaded bitmap to internal storage
                        //Uri uri = saveImageToInternalStorage(response);

                        // Display the internal storage saved image to image view
                        //mImageViewInternal.setImageURI(uri);
                    }
                },
                0, // Image width
                0, // Image height
                ImageView.ScaleType.CENTER_CROP, // Image scale type
                Bitmap.Config.RGB_565, //Image decode configuration
                new Response.ErrorListener() { // Error listener
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error ", error.getMessage());
                        if (error instanceof TimeoutError) {
                            Toast.makeText(ViewPagerActivity.this, "Timeout error, try again", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(ViewPagerActivity.this, "No internet", Toast.LENGTH_LONG).show();
                        }
                        progressBar.setVisibility(View.GONE);
                    }
                }
        ) {
            @Override
            public Priority getPriority() {
                return Priority.HIGH;
            }
        };
        imageRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Add ImageRequest to the RequestQueue
        requestQueue.add(imageRequest);
    }

    class TouchImageAdapter extends PagerAdapter {

        LayoutInflater layoutInflater;

        public TouchImageAdapter(Context context) {
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return images.size();
        }

        @Override
        public void setPrimaryItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            itemView = (View) object;
        }

        @NonNull
        @Override
        public View instantiateItem(@NonNull ViewGroup container, int position) {

            itemView = layoutInflater.inflate(R.layout.galley_view_layout, container, false);
            img = itemView.findViewById(R.id.t_image);

            progressBar.setVisibility(View.VISIBLE);
            counter = 0;
            getBit(images.get(position).imageResource);
            //new ImageAsync(getBaseContext()).execute(images.get(position).imageResource);

            //getBitmapFromURL(images.get(position).imageResource, String.valueOf(position));
            container.addView(itemView);

            //Log.e("URL ", images.get(position).imageResource);
            //Log.e("Position instance ", String.valueOf(position));
            //Log.e("Url ", images.get(position).imageResource);
            //img = new TouchImageView(getApplicationContext());

            //progressBar.setVisibility(View.VISIBLE);

            //getBit(images.get(position).imageResource);
            //img.setImageBitmap(bitmap);
            //container.addView(img, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

            return itemView;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

    }

    //-------------------------
    // Hardware back key.
    //-------------------------
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                startActivity(new Intent(ViewPagerActivity.this, GalleyActivity.class));
                finish();
                //onBackPressed();
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
