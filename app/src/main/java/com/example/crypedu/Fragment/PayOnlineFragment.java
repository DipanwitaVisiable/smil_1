package com.example.crypedu.Fragment;

import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;

import com.activity.smi.InternetCheckActivity;
import com.activity.smi.R;
import com.example.crypedu.Constants.Constants;
import com.example.crypedu.Pojo.AccountInfo;
import com.google.android.material.appbar.AppBarLayout;

import java.util.ArrayList;
import java.util.Objects;

public class PayOnlineFragment extends Fragment {

    private View myFragmentView;
    private WebView wv_pay_online;
    private AppBarLayout app_bar;
    private TextView tv_form_link;

    public PayOnlineFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        myFragmentView = inflater.inflate(R.layout.activity_account, container, false);
        app_bar = myFragmentView.findViewById(R.id.app_bar);
        app_bar.setVisibility(View.GONE);
        wv_pay_online =myFragmentView.findViewById(R.id.wv_pay_online);
        tv_form_link =myFragmentView.findViewById(R.id.tv_form_link);
        wv_pay_online.getSettings().setJavaScriptEnabled(true);
        wv_pay_online.setWebViewClient(new WebViewClient());
        wv_pay_online.loadUrl(Constants.PAY_ONLINE_URL);

        tv_form_link.setText(Constants.PAY_ONLINE_URL);


        return myFragmentView;
    }
}