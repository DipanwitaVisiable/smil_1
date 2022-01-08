package com.example.crypedu.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DownloadManager;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaScannerConnection;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.PowerManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.activity.smi.R;
import com.example.crypedu.Constants.Constants;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.UUID;

public class DriveDocumentsDownloadActivity extends AppCompatActivity {

  private Button btn_download;
  private String file_url="";
  private DownloadManager downloadManager;
  private long refid;
  private Uri Download_Uri;
  private ArrayList<Long> list = new ArrayList<>();


  @RequiresApi(api = Build.VERSION_CODES.KITKAT)
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_days_list);
    btn_download=findViewById(R.id.btn_download);
    Intent intent = getIntent();
    Bundle bundle = intent.getExtras();
    file_url = bundle.getString("file_url");

    downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
    registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    Download_Uri = Uri.parse(file_url);
    if(!isStoragePermissionGranted())
    {

    }

    btn_download.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        list.clear();
        DownloadManager.Request request = new DownloadManager.Request(Download_Uri);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        request.setAllowedOverRoaming(false);
        request.setTitle("GadgetSaint Downloading " + "Sample" + ".png");
        request.setDescription("Downloading " + "Sample" + ".png");
        request.setVisibleInDownloadsUi(true);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "/GadgetSaint/"  + "/" + "Sample" + ".png");
        refid = downloadManager.enqueue(request);
        Log.e("OUT", "" + refid);
        list.add(refid);
      }
    });
  }

  public  boolean isStoragePermissionGranted() {
    if (Build.VERSION.SDK_INT >= 23) {
      if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
        == PackageManager.PERMISSION_GRANTED) {
        return true;
      } else {

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        return false;
      }
    }
    else { //permission is automatically granted on sdk<23 upon installation
      return true;
    }
  }

  BroadcastReceiver onComplete = new BroadcastReceiver() {
    public void onReceive(Context ctxt, Intent intent) {
      long referenceId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
      Log.e("IN", "" + referenceId);
      list.remove(referenceId);
      if (list.isEmpty())
      {
        Log.e("INSIDE", "" + referenceId);
        NotificationCompat.Builder mBuilder =
          new NotificationCompat.Builder(DriveDocumentsDownloadActivity.this)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("GadgetSaint")
            .setContentText("All Download completed");

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(455, mBuilder.build());
        
      }

    }
  };

  @Override
  protected void onDestroy() {
    super.onDestroy();
    unregisterReceiver(onComplete);
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
      // permission granted
    }
  }

}


