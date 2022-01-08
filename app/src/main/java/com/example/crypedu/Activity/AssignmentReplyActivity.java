package com.example.crypedu.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.activity.smi.R;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.crypedu.Adapter.MultipleFileShowingAdapter;
import com.example.crypedu.Adapter.MultipleImageShowingAdapter;
import com.example.crypedu.Constants.Constants;
import com.example.crypedu.Helper.AndroidMultiPartEntity;
import com.example.crypedu.Helper.Utility;
import com.example.crypedu.Helper.VolleyMultipartRequestTwo;
import com.squareup.picasso.Picasso;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import cz.msebera.android.httpclient.entity.mime.content.FileBody;
import cz.msebera.android.httpclient.entity.mime.content.StringBody;
import de.hdodenhof.circleimageview.CircleImageView;

public class AssignmentReplyActivity extends AppCompatActivity {
  private Context context;
  private TextView tv_add_attachment, tv_add_photo, et_written_text, tv_user_name, tv_class, tv_section;
  private static final int PICKFILE_RESULT_CODE = 101;
  private static final int REQUEST_CAMERA = 0;
  private static final int SELECT_FILE = 2;
  private String userChoosenTask, conImage, output;
  private Bitmap bm = null;
  private CircleImageView civ_user_image;
  private String[] permissions = {"android.permission.WRITE_EXTERNAL_STORAGE",
    "android.permission.ACCESS_FINE_LOCATION", "android.permission.READ_PHONE_STATE",
    "android.permission.SYSTEM_ALERT_WINDOW", "android.permission.CAMERA"};
  private ProgressDialog progressDialog;
  private RequestQueue rQueue;
  public static int count = 0;
  private int requestCode = 200;

  //new
  private String displayName = null;
  private ProgressBar pb_loader;
  private RequestQueue requestQueue;
  private Button btn_submit;


  //new method
  private int file_count;
  private ArrayList<String> videoPathList;
  public static ArrayList<Uri> fileUriStaticList = new ArrayList<>();
  public static ArrayList<Uri> finalUriStaticList = new ArrayList<>();
  public static ArrayList<Uri> imageUriStaticList = new ArrayList<>();
  public static ArrayList<String> fileDisplayNameList = new ArrayList<>();
  public static ArrayList<byte[]> fileInputByteList = new ArrayList<>();
  private RecyclerView rv_image_list, rv_attachment_list;
  private Uri main_file_uri;
  private TextView txtPercentage;
  private ProgressBar progressBar;
  long totalSize = 0;
  private String filePath = null;
  private RelativeLayout rl_percentage;
  String backend_url =Constants.FILE_UPLOAD_IN_DRIVE;
  private String exam_id, exam_date, end_time;


  @Override
  public void onAttachedToWindow() {
    super.onAttachedToWindow();
  }

  @Override
  protected void onStart() {
    super.onStart();
  }

  @RequiresApi(api = Build.VERSION_CODES.KITKAT)
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_assignment_reply);
    context = AssignmentReplyActivity.this;
    requestQueue = Volley.newRequestQueue(this);

    Intent intent = getIntent();
    Bundle bundle = intent.getExtras();
    exam_id = bundle.getString("exam_id");
    exam_date = bundle.getString("exam_date");
    end_time = bundle.getString("end_time");

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      requestPermissions(permissions, requestCode);
    }

    initView();

  }

  @Override
  protected void onResume() {
    super.onResume();
    if (count > 0) {
      count = 0;
      onBackPressed();
    }
  }

  @RequiresApi(api = Build.VERSION_CODES.KITKAT)
  private void initView() {
    pb_loader = findViewById(R.id.pb_loader);
    btn_submit = findViewById(R.id.btn_submit);
    tv_add_attachment = findViewById(R.id.tv_add_attachment);
    tv_add_photo = findViewById(R.id.tv_add_photo);
    et_written_text = findViewById(R.id.et_written_text);
    civ_user_image = findViewById(R.id.civ_user_image);
    tv_user_name = findViewById(R.id.tv_user_name);
    tv_class = findViewById(R.id.tv_class);
    tv_section = findViewById(R.id.tv_section);
    rv_image_list = findViewById(R.id.rv_image_list);
    rv_attachment_list = findViewById(R.id.rv_attachment_list);
    txtPercentage = findViewById(R.id.txtPercentage);
    progressBar = findViewById(R.id.progressBar);
    rl_percentage = findViewById(R.id.rl_percentage);


    final Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setDisplayShowHomeEnabled(true);
    toolbar.setTitleTextColor(Color.WHITE);
    toolbar.setNavigationIcon(R.drawable.ic_left_arrow);

    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        onBackPressed();
      }
    });

    GridLayoutManager layoutManager = new GridLayoutManager(this, 3);//For Image only
    rv_image_list.setLayoutManager(layoutManager);

    GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);//For attachment only
    rv_attachment_list.setLayoutManager(gridLayoutManager);

    getProfileDetails();

    tv_add_attachment.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        openFolder();
      }
    });
    tv_add_photo.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        selectImage();
      }
    });

    btn_submit.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (fileUriStaticList.size()==0 && imageUriStaticList.size() == 0) {
          Toast.makeText(getApplicationContext(), "Please select image/attachment first", Toast.LENGTH_LONG).show();
        } else {
          if (fileUriStaticList.size()>0)
            finalUriStaticList.addAll(fileUriStaticList);
          if (imageUriStaticList.size()>0)
            finalUriStaticList.addAll(imageUriStaticList);
          getFinalInputByteList();
          multipleFileUpload();
//                    new UploadFileToServer().execute();
        }

      }
    });

  }


  public void openFolder() {
    Intent intent = new Intent();
    intent.setType("*/*");
    intent.setAction(Intent.ACTION_GET_CONTENT);
    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
    intent.putExtra("return-data", true);
    startActivityForResult(Intent.createChooser(intent, "Complete action using"), PICKFILE_RESULT_CODE);
  }

  private void selectImage() {
    final CharSequence[] items = {"Take Photo", "Choose from Gallery"};
    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
    builder.setTitle("Add Photo!");
    builder.setItems(items, new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int item) {
        boolean result = Utility.checkPermission(context);

        if (items[item].equals("Take Photo")) {
          userChoosenTask = "Take Photo";
          if (result)
            cameraIntent();

        } else if (items[item].equals("Choose from Gallery")) {
          userChoosenTask = "Choose from Gallery";
          if (result)
            galleryIntent();
        }
      }
    });
    builder.show();
  }

  private void galleryIntent() {
    Intent intent = new Intent();
    intent.setType("image/*");
    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
    intent.setAction(Intent.ACTION_PICK);//
    startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
  }

  private void cameraIntent() {
    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    startActivityForResult(intent, REQUEST_CAMERA);
  }

  @RequiresApi(api = Build.VERSION_CODES.KITKAT)
  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    switch (requestCode) {
      case PICKFILE_RESULT_CODE:
        if (resultCode == RESULT_OK) {
          rv_attachment_list.setVisibility(View.VISIBLE);

          if (data.getClipData() != null) {
            file_count = data.getClipData().getItemCount();

            for (int i = 0; i < file_count; i++) {
              fileUriStaticList.add(data.getClipData().getItemAt(i).getUri());
            }
          }
          else {
            main_file_uri = data.getData();
            fileUriStaticList.add(main_file_uri);
          }
          rv_attachment_list.setAdapter(new MultipleFileShowingAdapter(context, fileUriStaticList));

        }
        break;

      case SELECT_FILE:
        if (resultCode == Activity.RESULT_OK) {

          if (requestCode == SELECT_FILE) {
            main_file_uri = data.getData();
//                        getFileDisplayName(main_file_uri);
            onSelectFromGalleryResult(data);
          }
        }
        break;

      case REQUEST_CAMERA:
        if (resultCode == Activity.RESULT_OK) {
          if (requestCode == REQUEST_CAMERA) {
            onCaptureImageResult(data);
          }
        }
        break;

    }
  }


  @RequiresApi(api = Build.VERSION_CODES.KITKAT)
  private void onCaptureImageResult(Intent data) {
    bm = (Bitmap) Objects.requireNonNull(data.getExtras()).get("data");
    main_file_uri = getImageUri(context, bm);
    imageUriStaticList.add(main_file_uri);
    rv_image_list.setVisibility(View.VISIBLE);
    rv_image_list.setAdapter(new MultipleImageShowingAdapter(context, imageUriStaticList));

  }

  @SuppressWarnings("deprecation")
  private void onSelectFromGalleryResult(Intent data) {
    if (data.getClipData() != null) {
      file_count = data.getClipData().getItemCount();
      for (int i = 0; i < file_count; i++) {
        imageUriStaticList.add(data.getClipData().getItemAt(i).getUri());
      }
    } else {
      imageUriStaticList.add(data.getData());
    }
    rv_image_list.setVisibility(View.VISIBLE);
    rv_image_list.setAdapter(new MultipleImageShowingAdapter(context, imageUriStaticList));

  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    switch (requestCode) {
      case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
          if (userChoosenTask.equals("Take Photo"))
            cameraIntent();
          else if (userChoosenTask.equals("Choose from Gallery"))
            galleryIntent();
        }
        break;
    }
  }

  /**
   * pass files using below method
   **/
  public byte[] getBytes(InputStream inputStream) throws IOException {
    ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
    int bufferSize = 1024;
    byte[] buffer = new byte[bufferSize];

    int len = 0;
    while ((len = inputStream.read(buffer)) != -1) {
      byteBuffer.write(buffer, 0, len);
    }
    return byteBuffer.toByteArray();
  }


  public void getProfileDetails() {
    pb_loader.setVisibility(View.VISIBLE);
    StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.BASE_SERVER + "student_profile/", new Response.Listener<String>() {
      @Override
      public void onResponse(String response) {
        try {
          pb_loader.setVisibility(View.GONE);
          JSONObject obj = new JSONObject(response);
          String status = obj.getString("status");

          if (status.equalsIgnoreCase("200")) {
            JSONArray jsonArrayList = obj.getJSONArray("student_details");
            for (int i = 0; i < jsonArrayList.length(); i++) {
              JSONObject jsonObject = jsonArrayList.getJSONObject(i);
              String firstName = jsonObject.getString("First_Name");
              String middleName = jsonObject.getString("middle_name");
              String lastName = jsonObject.getString("Last_Name"); //active
              String name = firstName + " " + middleName + " " + lastName;
              String className = jsonObject.getString("class_or_year"); //active
              String section = jsonObject.getString("section"); //active
              String contactNo = jsonObject.getString("mobile_number"); // active
              String imageUrl = jsonObject.getString("image_url"); // active

              if (!imageUrl.equals("")) {
                Picasso.with(getBaseContext())
                  .load(imageUrl)
                  .placeholder(R.drawable.profile)
                  .noFade()
                  .into(civ_user_image);
              }
              tv_user_name.setText(name);
              tv_class.setText("Class- " + className);
              tv_section.setText("Section- " + section);
            }
          } else if (status.equalsIgnoreCase("206")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage(R.string.force_logout)
              .setCancelable(false)
              .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                  Intent intent = new Intent(context, LoginActivity.class);
                  intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                  startActivity(intent);
                }
              });
            AlertDialog alert = builder.create();
            alert.show();
          } else {
            Toast.makeText(context, "No data found", Toast.LENGTH_SHORT).show();
          }

        } catch (JSONException e) {
          e.printStackTrace();
          pb_loader.setVisibility(View.GONE);

        }
      }
    }, new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError error) {
        pb_loader.setVisibility(View.GONE);
      }
    }) {
      @Override
      protected Map<String, String> getParams() {
        Map<String, String> params = new HashMap<>();
        params.put("id", Constants.USER_ID);
//                params.put("student_id", "2623");
        return checkParams(params);
      }

      private Map<String, String> checkParams(Map<String, String> map) {
        for (Map.Entry<String, String> pairs : map.entrySet()) {
          if (pairs.getValue() == null) {
            map.put(pairs.getKey(), "");
          }
        }
        return map;
      }
    };
    stringRequest.setRetryPolicy(new RetryPolicy() {
      @Override
      public int getCurrentTimeout() {
        return 50000;
      }

      @Override
      public int getCurrentRetryCount() {
        return 50000;
      }

      @Override
      public void retry(VolleyError error) throws VolleyError {

      }
    });
    requestQueue.add(stringRequest);
  }

  public Uri getImageUri(Context inContext, Bitmap inImage) {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
    String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
    return Uri.parse(path);
  }


  public byte[] getFileDataFromDrawable(Bitmap bitmap) {
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
    return byteArrayOutputStream.toByteArray();
  }

  private void multipleFileUpload() {
//    private String multipleFileUpload() {
//        String demo_string="test";
    progressDialog = new ProgressDialog(this);
    progressDialog.setMessage("Loading....");
    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    progressDialog.setCancelable(false);
    progressDialog.show();

    VolleyMultipartRequestTwo multipartRequest = new VolleyMultipartRequestTwo(Request.Method.POST,
      backend_url, new Response.Listener<JSONObject>() {

      @Override
      public void onResponse(JSONObject response) {

        try {
          et_written_text.setText("");
          imageUriStaticList.clear();
          fileUriStaticList.clear();
          finalUriStaticList.clear();
          fileDisplayNameList.clear();
          fileInputByteList.clear();
          rv_image_list.setVisibility(View.GONE);
          rv_attachment_list.setVisibility(View.GONE);

          progressDialog.hide();
          Toast.makeText(AssignmentReplyActivity.this, response.getString("message"), Toast.LENGTH_LONG).show();
          onBackPressed();

        } catch (JSONException e) {
          e.printStackTrace();
          progressDialog.hide();
        }
      }
    }, new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError error) {
        parseVolleyError(error);
        progressDialog.hide();
        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
          Toast.makeText(AssignmentReplyActivity.this, "connection_time_out", Toast.LENGTH_SHORT).show();
        }
      }
    }) {
      @Override
      protected Map<String, String> getParams() {
        Map<String, String> params = new HashMap<>();
        params.put("student_id", Constants.USER_ID);
        params.put("exam_id", exam_id);
        params.put("exam_date", exam_date);
        params.put("end_time", end_time);
        return params;
      }

      @Override
      protected Map<String, ArrayList<DataPart>> getByteData() {
        Map<String, VolleyMultipartRequestTwo.DataPart> params = new HashMap<>();
        Map<String, ArrayList<DataPart>> imageList = new HashMap<>();
        ArrayList<DataPart> dataPart = new ArrayList<>();
        dataPart.clear();
        for (int i = 0; i < fileDisplayNameList.size(); i++) {
          VolleyMultipartRequestTwo.DataPart dp = new VolleyMultipartRequestTwo.DataPart(fileDisplayNameList.get(i), fileInputByteList.get(i));
          dataPart.add(dp);
        }
//        imageList.put("attachement[]", dataPart);
        imageList.put("upload_file[]", dataPart);

        return imageList;
      }
    };
    int socketTimeout = 500000;//30 seconds - change to what you want
    RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
    multipartRequest.setRetryPolicy(policy);
    rQueue = Volley.newRequestQueue(AssignmentReplyActivity.this);
    rQueue.add(multipartRequest);
//        return demo_string;
  }


///////Start- this is from androidhive site


  /**
   * Uploading the file to server
   */
  private class UploadFileToServer extends AsyncTask<Void, Integer, String> {
    @Override
    protected void onPreExecute() {
      // setting progress bar to zero
      progressBar.setProgress(0);
      super.onPreExecute();
    }

    @Override
    protected void onProgressUpdate(Integer... progress) {
      // Making progress bar visible
      rl_percentage.setVisibility(View.VISIBLE);

      // updating progress bar value
      progressBar.setProgress(progress[0]);

      // updating percentage value
      txtPercentage.setText(String.valueOf(progress[0]) + "%");
    }

    @Override
    protected String doInBackground(Void... params) {
      return uploadFile();
//            return multipleFileUpload();
    }

    @SuppressWarnings("deprecation")
    private String uploadFile() {
      String responseString = null;

      HttpClient httpclient = new DefaultHttpClient();
      HttpPost httppost = new HttpPost(backend_url);

      try {
        AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
          new AndroidMultiPartEntity.ProgressListener() {

            @Override
            public void transferred(long num) {
              publishProgress((int) ((num / (float) totalSize) * 100));
            }
          });

        File sourceFile = new File(filePath);

        /*// Adding file data to http body
        entity.addPart("attachement[]", new FileBody(sourceFile));
        // Extra parameters if you want to pass to server
        entity.addPart("student_id", new StringBody(Constants.USER_ID));
        entity.addPart("topic_id", new StringBody("4"));*/

        // Adding file data to http body
        entity.addPart("attachement[]", (ContentBody) new FileBody(sourceFile));
        // Extra parameters if you want to pass to server
        entity.addPart("student_id", (ContentBody) new StringBody(Constants.USER_ID));
        entity.addPart("topic_id", (ContentBody) new StringBody(Constants.TOPIC_ID));
        entity.addPart("topic_text", (ContentBody) new StringBody(et_written_text.getText().toString()));

        totalSize = entity.getContentLength();
        httppost.setEntity(entity);

        // Making server call
        HttpResponse response = httpclient.execute(httppost);
        HttpEntity r_entity = response.getEntity();

        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode == 200) {
          // Server response
          responseString = EntityUtils.toString(r_entity);
        } else {
          responseString = "Error occurred! Http Status Code: "
            + statusCode;
        }

      } catch (ClientProtocolException e) {
        responseString = e.toString();
      } catch (IOException e) {
        responseString = e.toString();
      }

      return responseString;

    }

    @Override
    protected void onPostExecute(String result) {
      Log.e("TAG", "Response from server: " + result);

      // showing the server response in an alert dialog
//            showAlert(result);
      rl_percentage.setVisibility(View.GONE);

      super.onPostExecute(result);
    }

  }



//////end- this is from androidhive coding site


  public void parseVolleyError(VolleyError error) {
    try {
      String responseBody = new String(error.networkResponse.data, "utf-8");
      JSONObject data = new JSONObject(responseBody);
      JSONArray errors = data.getJSONArray("errors");
      JSONObject jsonMessage = errors.getJSONObject(0);
      String message = jsonMessage.getString("message");
      Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    } catch (JSONException e) {
    } catch (UnsupportedEncodingException errorr) {
    }
  }

  private void getFinalInputByteList() {
    String uriString;
    for (int i = 0; i < finalUriStaticList.size(); i++) {
      InputStream iStream = null;
      try {
        iStream = getContentResolver().openInputStream(finalUriStaticList.get(i));
        final byte[] inputData = getBytes(iStream);
        fileInputByteList.add(inputData);
      } catch (FileNotFoundException e) {
        e.printStackTrace();
      } catch (IOException e) {
        e.printStackTrace();
      }


      uriString = finalUriStaticList.get(i).toString();
      File myFile = new File(uriString);
      if (uriString.startsWith("content://")) {
        Cursor cursor = null;
        try {
          cursor = context.getContentResolver().query(finalUriStaticList.get(i), null, null, null, null);
          if (cursor != null && cursor.moveToFirst()) {
            displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
            fileDisplayNameList.add(displayName);
          }
        } finally {
          cursor.close();
        }
      } else if (uriString.startsWith("file://")) {
        displayName = myFile.getName();
        fileDisplayNameList.add(displayName);
      }
    }

  }

}
