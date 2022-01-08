package com.example.crypedu.Fragment;
/*
 *************************************************************************************************
 * Developed by Cryptonsoftech
 * Date: 30-05-2017
 * Details:
 * ***********************************************************************************************
 */
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.activity.smi.R;
import com.example.crypedu.Constants.Constants;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Objects;

public class TwelveFragment extends Fragment implements OnMapReadyCallback{


    private ListView advertisement_listView;

    public TwelveFragment() {
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
//        SharedPreferences pref = getActivity().getSharedPreferences("ActivityPREF", Context.MODE_PRIVATE);
//        if (pref.getBoolean("activity_executed", false)) {
//            Constants.USER_ID = pref.getString("student_id", "");
//            Constants.USER_ROLE = pref.getString("user_role", "");
//            Constants.PROFILENAME = pref.getString("profile_name", "");
//            Constants.PhoneNo = pref.getString("phoneNo", "");
//        } else {
//            Intent intent = new Intent(getActivity().getApplication(), LoginActivity.class);
//            startActivity(intent);
//            getActivity().finish();
//        }
        // Inflate the layout for this fragment

        View myFragmentView = inflater.inflate(R.layout.fragment_twelve, container, false);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);

        Typeface typeface = Typeface.createFromAsset(Objects.requireNonNull(getActivity()).getApplicationContext().getAssets(), Constants.BubblegumSans_Regular_font);
        CoordinatorLayout coordinatorLayout = myFragmentView.findViewById(R.id.coordinatorLayout);

        TextView sryText = myFragmentView.findViewById(R.id.sryText);
        sryText.setTypeface(typeface);
    //    coordinatorLayout = (CoordinatorLayout) myFragmentView.findViewById(R.id.coordinatorLayout);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        mapFragment.getMapAsync(this);
        return myFragmentView;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        // Add a marker in Sydney and move the camera
        LatLng smi = new LatLng(22.621260, 88.348680);
        googleMap.addMarker(new MarkerOptions().position(smi).title("SMI Liluah"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(smi));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(18));
    }

}
