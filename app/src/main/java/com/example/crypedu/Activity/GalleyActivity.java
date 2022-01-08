package com.example.crypedu.Activity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.activity.smi.R;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.crypedu.Adapter.BrandAdapter;
import com.example.crypedu.Constants.Constants;
import com.example.crypedu.Pojo.Brand;
import com.example.crypedu.Pojo.Category;
import com.example.crypedu.Pojo.Mobile;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GalleyActivity extends AppCompatActivity {

    private ExpandableListView mBrandsListView;
    private List<Brand> brandList = new ArrayList<>();
    private List<Mobile> mobileList = new ArrayList<>();
    private Category category;
    private Mobile mobile;
    private Brand brand;


    private BrandAdapter mBrandAdapter;
    private TextView galleryText;
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_galley);
        /*if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }  // To prevent screen recording*/

        galleryText = findViewById(R.id.gallery_year);

        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(ActivityActivity.this, MenuActivity.class);
                //startActivity(intent);
                onBackPressed();
                finish();
            }
        });

        mBrandsListView = findViewById(R.id.list_brands);

        getGalleryInfo();

        mBrandsListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            int previousGroup = -1;

            @Override
            public void onGroupExpand(int groupPosition) {
                if ((previousGroup != -1) && (groupPosition != previousGroup)) {
                    mBrandsListView.collapseGroup(previousGroup);
                }
                previousGroup = groupPosition;
            }
        });
        mBrandsListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                //Toast.makeText(MainActivity.this, brands.get(groupPosition).brandName + " => "+ brands.get(groupPosition).getMobiles().get(childPosition).modelName, Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }

    private void getGalleryInfo(){
        StringRequest request = new StringRequest(Request.Method.POST, Constants.BASE_SERVER + "galery_section", new Response.Listener<String>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject object = new JSONObject(response);
                    String status  = object.getString("status");
                    String message = object.getString("message");
                    String year = object.getString("year");
                    if (status.equalsIgnoreCase("200")){
                        String s = "Album of the year "+year;
                        SpannableStringBuilder builder = new SpannableStringBuilder(s);
                        // Initialize a new StyleSpan to display bold text
                        StyleSpan boldSpan = new StyleSpan(Typeface.BOLD_ITALIC);
                        // Apply the bold text style span
                        builder.setSpan(
                                boldSpan, // Span to add
                                s.indexOf(year), // Start of the span (inclusive)
                                s.indexOf(year) + year.length(), // End of the span (exclusive)
                                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE // Do not extend the span when text add later
                        );
                        galleryText.setText( builder);
                        JSONArray jsonArray = object.getJSONArray("message");
                        brandList.clear();
                        mobileList.clear();
                        //Log.e("Array ", jsonArray.toString());
                        for (int i = 0;i < jsonArray.length(); i++){
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            category = new Category(jsonObject.getString("title"));
                            JSONArray array = jsonObject.getJSONArray("image_link");
                            mobileList = new ArrayList<>();
                            for (int j = 0; j<array.length(); j++) {
                                mobile = new Mobile(String.valueOf(array.get(j)), "", "");
                                mobileList.add(mobile);
                            }
                            brand = new Brand(category, mobileList);
                            brandList.add(brand);
                        }

                        mBrandAdapter = new BrandAdapter(GalleyActivity.this, brandList, getSupportFragmentManager());

                        mBrandsListView.setAdapter(mBrandAdapter);
                    }else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(GalleyActivity.this);
                        builder.setMessage(message);
                        builder.setCancelable(false);
                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                onBackPressed();
                            }
                        }).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }

    //-------------------------
    // Hardware back key.
    //-------------------------
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch(keyCode){
            case KeyEvent.KEYCODE_BACK:
                //startActivity(new Intent(ActivityActivity.this, MenuActivity.class));
                onBackPressed();
                finish();
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
