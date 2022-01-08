package com.example.crypedu.Fragment;
/*
 *************************************************************************************************
 * Developed by Cryptonsoftech
 * Date: 30-05-2017
 * Details:
 * ***********************************************************************************************
 */

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;
import androidx.fragment.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.activity.smi.InternetCheckActivity;
import com.activity.smi.R;
import com.example.crypedu.Activity.LoginActivity;
import com.example.crypedu.Constants.Constants;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import cz.msebera.android.httpclient.Header;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {

    private View myFragmentView;
    private CoordinatorLayout coordinatorLayout;
    private TextView name_textView, class_textView, section_textView, pinCode_textView, dob_textView, gender_textView,
            rollNo_textView, motherTongue_textView, mailingAddress_textView, city_textView, state_textView,
            homePhoneNo_textView, mobileNo_textView, permanentAddress_textView, permanentCity_textView, permanentState_textView,
            permanentPinCode_textView, studentCode_textView, fname_textView, mname_textView, cno_textView, address_textView,
            country_textView, teacher_name_textView, teacherCode_textView, teacher_address_textView, teacher_pincode_textView,
            teacher_dob_textView, teacher_sex_textView, teacher_bloodgroup_textView, teacher_email_textView,
            teacher_mobile_textView, marital_status_textView, salary_textView, faculty_type_textView, joining_date_textView,
            resigning_date_textView, director_name_textView, director_pinCode_textView, director_motherTongue_textView,
            director_mailingAddress_textView, director_city_textView, director_state_textView, director_homePhoneNo_textView,
            director_mobileNo_textView, director_permanentAddress_textView, director_permanentCity_textView,
            director_permanentState_textView, director_permanentPinCode_textView, director_address_textView,
            director_country_textView;
    private CircleImageView imageView, teacherImageView;


    public ProfileFragment() {
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
        myFragmentView = inflater.inflate(R.layout.fragment_profile, container, false);
        coordinatorLayout = myFragmentView.findViewById(R.id.coordinatorLayout);

        /*
          Set BubbleGumSans Regular font.
         */
        Typeface typeface = Typeface.createFromAsset(Objects.requireNonNull(getActivity()).getApplication().getAssets(), Constants.BubblegumSans_Regular_font);

        /*
          Display student profile details if login role is 'p'.
         */
        imageView = myFragmentView.findViewById(R.id.imageView);

        TextView copyRight_textView = myFragmentView.findViewById(R.id.copyRight_textView);
        copyRight_textView.setText(Constants.strCopyright);
        copyRight_textView.setTypeface(typeface);

        TextView studentCode_title_textView = myFragmentView.findViewById(R.id.studentCode_title_textView);
        studentCode_title_textView.setTypeface(typeface);

        studentCode_textView = myFragmentView.findViewById(R.id.studentCode_textView);
        studentCode_textView.setTypeface(typeface);

        fname_textView = myFragmentView.findViewById(R.id.fname_textView);
        fname_textView.setTypeface(typeface);

        mname_textView = myFragmentView.findViewById(R.id.mname_textView);
        mname_textView.setTypeface(typeface);

        cno_textView = myFragmentView.findViewById(R.id.cno_textView);
        cno_textView.setTypeface(typeface);

        address_textView = myFragmentView.findViewById(R.id.address_textView);
        address_textView.setTypeface(typeface);

        country_textView = myFragmentView.findViewById(R.id.country_textView);
        country_textView.setTypeface(typeface);

        TextView class_title_textView = myFragmentView.findViewById(R.id.class_title_textView);
        name_textView = myFragmentView.findViewById(R.id.name_textView);
        name_textView.setTypeface(typeface);
        class_title_textView.setTypeface(typeface);

        TextView section_title_textView = myFragmentView.findViewById(R.id.section_title_textView);
        class_textView = myFragmentView.findViewById(R.id.class_textView);
        class_textView.setTypeface(typeface);
        section_title_textView.setTypeface(typeface);

        TextView pincode_title_textView = myFragmentView.findViewById(R.id.pincode_title_textView);
        section_textView = myFragmentView.findViewById(R.id.section_textView);
        section_textView.setTypeface(typeface);
        pincode_title_textView.setTypeface(typeface);

        TextView dob_title_textView = myFragmentView.findViewById(R.id.dob_title_textView);
        pinCode_textView = myFragmentView.findViewById(R.id.pinCode_textView);
        pinCode_textView.setTypeface(typeface);
        dob_title_textView.setTypeface(typeface);

        TextView gender_title_textView = myFragmentView.findViewById(R.id.gender_title_textView);
        dob_textView = myFragmentView.findViewById(R.id.dob_textView);
        dob_textView.setTypeface(typeface);
        gender_title_textView.setTypeface(typeface);

        TextView rollNo_title_textView = myFragmentView.findViewById(R.id.rollNo_title_textView);
        gender_textView = myFragmentView.findViewById(R.id.gender_textView);
        gender_textView.setTypeface(typeface);
        rollNo_title_textView.setTypeface(typeface);

        TextView motherTongue_title_textView = myFragmentView.findViewById(R.id.motherTongue_title_textView);
        rollNo_textView = myFragmentView.findViewById(R.id.rollNo_textView);
        rollNo_textView.setTypeface(typeface);
        motherTongue_title_textView.setTypeface(typeface);

        TextView mailingAddress_title_textView = myFragmentView.findViewById(R.id.mailingAddress_title_textView);
        motherTongue_textView = myFragmentView.findViewById(R.id.motherTongue_textView);
        motherTongue_textView.setTypeface(typeface);
        mailingAddress_title_textView.setTypeface(typeface);

        TextView city_title_textView = myFragmentView.findViewById(R.id.city_title_textView);
        mailingAddress_textView = myFragmentView.findViewById(R.id.mailingAddress_textView);
        mailingAddress_textView.setTypeface(typeface);
        city_title_textView.setTypeface(typeface);

        TextView state_title_textView = myFragmentView.findViewById(R.id.state_title_textView);
        city_textView = myFragmentView.findViewById(R.id.city_textView);
        city_textView.setTypeface(typeface);
        state_title_textView.setTypeface(typeface);

        TextView homePhoneNo_title_textView = myFragmentView.findViewById(R.id.homePhoneNo_title_textView);
        state_textView = myFragmentView.findViewById(R.id.state_textView);
        state_textView.setTypeface(typeface);
        homePhoneNo_title_textView.setTypeface(typeface);

        TextView mobileNo_title_textView = myFragmentView.findViewById(R.id.mobileNo_title_textView);
        homePhoneNo_textView = myFragmentView.findViewById(R.id.homePhoneNo_textView);
        homePhoneNo_textView.setTypeface(typeface);
        mobileNo_title_textView.setTypeface(typeface);

        TextView permanentAddress_title_textView = myFragmentView.findViewById(R.id.permanentAddress_title_textView);
        mobileNo_textView = myFragmentView.findViewById(R.id.mobileNo_textView);
        mobileNo_textView.setTypeface(typeface);
        permanentAddress_title_textView.setTypeface(typeface);

        TextView permanentCity_title_textView = myFragmentView.findViewById(R.id.permanentCity_title_textView);
        permanentAddress_textView = myFragmentView.findViewById(R.id.permanentAddress_textView);
        permanentAddress_textView.setTypeface(typeface);
        permanentCity_title_textView.setTypeface(typeface);

        TextView permanentState_title_textView = myFragmentView.findViewById(R.id.permanentState_title_textView);
        permanentCity_textView = myFragmentView.findViewById(R.id.permanentCity_textView);
        permanentCity_textView.setTypeface(typeface);
        permanentState_title_textView.setTypeface(typeface);

        TextView permanentPinCode_title_textView = myFragmentView.findViewById(R.id.permanentPinCode_title_textView);
        permanentState_textView = myFragmentView.findViewById(R.id.permanentState_textView);
        permanentState_textView.setTypeface(typeface);
        permanentPinCode_title_textView.setTypeface(typeface);

        permanentPinCode_textView = myFragmentView.findViewById(R.id.permanentPinCode_textView);
        permanentPinCode_textView.setTypeface(typeface);

        TextView fname_title_textView = myFragmentView.findViewById(R.id.fname_title_textView);
        fname_title_textView.setTypeface(typeface);

        TextView mname_title_textView = myFragmentView.findViewById(R.id.mname_title_textView);
        mname_title_textView.setTypeface(typeface);

        TextView cno_title_textView = myFragmentView.findViewById(R.id.cno_title_textView);
        cno_title_textView.setTypeface(typeface);

        TextView address_title_textView = myFragmentView.findViewById(R.id.address_title_textView);
        address_title_textView.setTypeface(typeface);

        TextView country_title_textView = myFragmentView.findViewById(R.id.country_title_textView);
        country_title_textView.setTypeface(typeface);

        /*
          Student and Teacher profile layout default visibility is 'GONE'.
         */
        LinearLayout studentLinearLayout = myFragmentView.findViewById(R.id.studentLinearLayout);
        LinearLayout teacherLinearLayout = myFragmentView.findViewById(R.id.teacherLinearLayout);
        LinearLayout directorLinearLayout = myFragmentView.findViewById(R.id.directorLinearLayout);

        // Director TextView

        director_name_textView = myFragmentView.findViewById(R.id.director_name_textView);
        director_name_textView.setTypeface(typeface);

        director_pinCode_textView = myFragmentView.findViewById(R.id.director_pinCode_textView);
        director_pinCode_textView.setTypeface(typeface);

        director_motherTongue_textView = myFragmentView.findViewById(R.id.director_motherTongue_textView);
        director_motherTongue_textView.setTypeface(typeface);

        director_mailingAddress_textView = myFragmentView.findViewById(R.id.director_mailingAddress_textView);
        director_mailingAddress_textView.setTypeface(typeface);

        director_city_textView = myFragmentView.findViewById(R.id.director_city_textView);
        director_city_textView.setTypeface(typeface);

        director_state_textView = myFragmentView.findViewById(R.id.director_state_textView);
        director_state_textView.setTypeface(typeface);

        director_homePhoneNo_textView = myFragmentView.findViewById(R.id.director_homePhoneNo_textView);
        director_homePhoneNo_textView.setTypeface(typeface);

        director_mobileNo_textView = myFragmentView.findViewById(R.id.director_mobileNo_textView);
        director_mobileNo_textView.setTypeface(typeface);

        director_permanentAddress_textView = myFragmentView.findViewById(R.id.director_permanentAddress_textView);
        director_permanentAddress_textView.setTypeface(typeface);

        director_permanentCity_textView = myFragmentView.findViewById(R.id.director_permanentCity_textView);
        director_permanentCity_textView.setTypeface(typeface);

        director_permanentState_textView = myFragmentView.findViewById(R.id.director_permanentState_textView);
        director_permanentState_textView.setTypeface(typeface);

        director_permanentPinCode_textView = myFragmentView.findViewById(R.id.director_permanentPinCode_textView);
        director_permanentPinCode_textView.setTypeface(typeface);


        director_address_textView = myFragmentView.findViewById(R.id.director_address_textView);
        director_address_textView.setTypeface(typeface);

        director_country_textView = myFragmentView.findViewById(R.id.director_country_textView);
        director_country_textView.setTypeface(typeface);

        TextView director_pincode_title_textView = myFragmentView.findViewById(R.id.director_pincode_title_textView);
        director_pincode_title_textView.setTypeface(typeface);

        TextView director_motherTongue_title_textView = myFragmentView.findViewById(R.id.director_motherTongue_title_textView);
        director_motherTongue_title_textView.setTypeface(typeface);

        TextView director_mailingAddress_title_textView = myFragmentView.findViewById(R.id.director_mailingAddress_title_textView);
        director_mailingAddress_title_textView.setTypeface(typeface);

        TextView director_city_title_textView = myFragmentView.findViewById(R.id.director_city_title_textView);
        director_city_title_textView.setTypeface(typeface);

        TextView director_state_title_textView = myFragmentView.findViewById(R.id.director_state_title_textView);
        director_state_title_textView.setTypeface(typeface);

        TextView director_homePhoneNo_title_textView = myFragmentView.findViewById(R.id.director_homePhoneNo_title_textView);
        director_homePhoneNo_title_textView.setTypeface(typeface);

        TextView director_mobileNo_title_textView = myFragmentView.findViewById(R.id.director_mobileNo_title_textView);
        director_mobileNo_title_textView.setTypeface(typeface);

        TextView director_permanentAddress_title_textView = myFragmentView.findViewById(R.id.director_permanentAddress_title_textView);
        director_permanentAddress_title_textView.setTypeface(typeface);

        TextView director_permanentCity_title_textView = myFragmentView.findViewById(R.id.director_permanentCity_title_textView);
        director_permanentCity_title_textView.setTypeface(typeface);

        TextView director_permanentState_title_textView = myFragmentView.findViewById(R.id.director_permanentState_title_textView);
        director_permanentState_title_textView.setTypeface(typeface);

        TextView director_permanentPinCode_title_textView = myFragmentView.findViewById(R.id.director_permanentPinCode_title_textView);
        director_permanentPinCode_title_textView.setTypeface(typeface);

        TextView director_address_title_textView = myFragmentView.findViewById(R.id.director_address_title_textView);
        director_address_title_textView.setTypeface(typeface);

        TextView director_country_title_textView = myFragmentView.findViewById(R.id.director_country_title_textView);
        director_country_title_textView.setTypeface(typeface);

        /*
          Display teacher profile details if login role is 't'.
         */
        teacherImageView = myFragmentView.findViewById(R.id.teacherImageView);
        teacher_name_textView = myFragmentView.findViewById(R.id.teacher_name_textView);

        TextView teacherCode_title_textView = myFragmentView.findViewById(R.id.teacherCode_title_textView);
        teacherCode_title_textView.setTypeface(typeface);
        teacherCode_textView = myFragmentView.findViewById(R.id.teacherCode_textView);

        TextView teacher_address_title_textView = myFragmentView.findViewById(R.id.teacher_address_title_textView);
        teacher_address_title_textView.setTypeface(typeface);
        teacher_address_textView = myFragmentView.findViewById(R.id.teacher_address_textView);

        TextView teacher_pincode_title_textView = myFragmentView.findViewById(R.id.teacher_pincode_title_textView);
        teacher_pincode_title_textView.setTypeface(typeface);
        teacher_pincode_textView = myFragmentView.findViewById(R.id.teacher_pincode_textView);

        TextView teacher_dob_title_textView = myFragmentView.findViewById(R.id.teacher_dob_title_textView);
        teacher_dob_title_textView.setTypeface(typeface);
        teacher_dob_textView = myFragmentView.findViewById(R.id.teacher_dob_textView);

        TextView teacher_sex_title_textView = myFragmentView.findViewById(R.id.teacher_sex_title_textView);
        teacher_sex_title_textView.setTypeface(typeface);
        teacher_sex_textView = myFragmentView.findViewById(R.id.teacher_sex_textView);

        TextView teacher_bloodgroup_title_textView = myFragmentView.findViewById(R.id.teacher_bloodgroup_title_textView);
        teacher_bloodgroup_title_textView.setTypeface(typeface);
        teacher_bloodgroup_textView = myFragmentView.findViewById(R.id.teacher_bloodgroup_textView);

        TextView teacher_email_title_textView = myFragmentView.findViewById(R.id.teacher_email_title_textView);
        teacher_email_title_textView.setTypeface(typeface);
        teacher_email_textView = myFragmentView.findViewById(R.id.teacher_email_textView);

        TextView teacher_mobile_title_textView = myFragmentView.findViewById(R.id.teacher_mobile_title_textView);
        teacher_mobile_title_textView.setTypeface(typeface);
        teacher_mobile_textView = myFragmentView.findViewById(R.id.teacher_mobile_textView);

        TextView marital_status_title_textView = myFragmentView.findViewById(R.id.marital_status_title_textView);
        marital_status_title_textView.setTypeface(typeface);
        marital_status_textView = myFragmentView.findViewById(R.id.marital_status_textView);

        TextView salary_title_textView = myFragmentView.findViewById(R.id.salary_title_textView);
        salary_title_textView.setTypeface(typeface);
        salary_textView = myFragmentView.findViewById(R.id.salary_textView);

        TextView faculty_type_title_textView = myFragmentView.findViewById(R.id.faculty_type_title_textView);
        faculty_type_title_textView.setTypeface(typeface);
        faculty_type_textView = myFragmentView.findViewById(R.id.faculty_type_textView);

        TextView joining_date_title_textView = myFragmentView.findViewById(R.id.joining_date_title_textView);
        joining_date_title_textView.setTypeface(typeface);
        joining_date_textView = myFragmentView.findViewById(R.id.joining_date_textView);

        TextView resigning_date_title_textView = myFragmentView.findViewById(R.id.resigning_date_title_textView);
        resigning_date_title_textView.setTypeface(typeface);
        resigning_date_textView = myFragmentView.findViewById(R.id.resigning_date_textView);

        /*
          If internet connection is working and
          userId is available otherwise go to login screen.
         */
        if (InternetCheckActivity.isConnected()) {
            if (Constants.USER_ID != null) {
                if (Constants.USER_ROLE.equalsIgnoreCase("s")) {
                    studentLinearLayout.setVisibility(View.VISIBLE);
                    fetchStudentProfile();
                } else if (Constants.USER_ROLE.equalsIgnoreCase("t")) {
                    teacherLinearLayout.setVisibility(View.VISIBLE);
                    fetchTeacherProfile();
                } else if (Constants.USER_ROLE.equalsIgnoreCase("d")) {
                    directorLinearLayout.setVisibility(View.VISIBLE);
                    fetchStudentProfile();
                }
            } else {
//            startActivity(new Intent(getContext(), LoginActivity.class));
            }
        } else {
            showSnack();
        }

        return myFragmentView;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onStart() {
        super.onStart();
        SharedPreferences pref = Objects.requireNonNull(getActivity()).getSharedPreferences("ActivityPREF", Context.MODE_PRIVATE);
        if (pref.getBoolean("activity_executed", false)) {
            Constants.USER_ID = pref.getString("student_id", "");
            Constants.USER_ROLE = pref.getString("user_role", "");
            Constants.PROFILENAME = pref.getString("profile_name", "");
            Constants.PhoneNo = pref.getString("phoneNo", "");
        } else {
            Intent intent = new Intent(getActivity().getApplication(), LoginActivity.class);
            startActivity(intent);
            getActivity().finish();
        }
    }

    private void showSnack() {
        Snackbar snackbar = Snackbar.make(coordinatorLayout, " Please connect your working internet connection. ", Snackbar.LENGTH_LONG);
        // Changing action button text color
        View sbView = snackbar.getView();
        TextView textView = sbView.findViewById(R.id.snackbar_text);
        textView.setTextColor(Color.parseColor(Constants.colorAccent));
        snackbar.show();
    }

    //--------------------------------
    // fetchStudentProfile from server
    // with the 'studentID' param.
    //--------------------------------
    public void fetchStudentProfile_old() {
        final ProgressBar pbHeaderProgress = myFragmentView.findViewById(R.id.pbHeaderProgress);
        pbHeaderProgress.setVisibility(View.VISIBLE);
        try {
            AsyncHttpClient clientReg = new AsyncHttpClient();
            RequestParams requestParams = new RequestParams();
            requestParams.put("id", Constants.USER_ID);
            clientReg.post(Constants.BASE_SERVER + "student_profile/", requestParams, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject obj) {
                    try {
                        pbHeaderProgress.setVisibility(View.GONE);
                        String status = obj.getString("status");
                        String message = obj.getString("message");
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
                                String pinCode = jsonObject.getString("pin_Code");
                                String dob = jsonObject.getString("date_of_birth"); //active
                                String gender = jsonObject.getString("gender"); //active
                                String rollNo = jsonObject.getString("roll_no");
                                String motherTongue = jsonObject.getString("mother_tongue");
                                String mailingAddress = jsonObject.getString("mailing_address");
                                String city = jsonObject.getString("city"); //active
                                String state = jsonObject.getString("state"); //active
                                String homePhoneNo = jsonObject.getString("home_phoneno");
                                String contactNo = jsonObject.getString("mobile_number"); // active
                                String permanentAddress = jsonObject.getString("permanent_address"); // active
                                String permanentCity = jsonObject.getString("permanent_city");
                                String permanentState = jsonObject.getString("permanent_state");
                                String permanentPinCode = jsonObject.getString("permanent_pin");
                                String country = jsonObject.getString("country"); // active
                                String studentCode = jsonObject.getString("student_code"); // active
                                String mobileNo = jsonObject.getString("second_mobile_number"); // active
                                String fatherName = jsonObject.getString("father_name"); // active
                                String motherName = jsonObject.getString("mother_name"); // active
                                String imageUrl = jsonObject.getString("image_url"); // active


                                if (imageUrl != null && !imageUrl.equalsIgnoreCase("")) {
                                    Picasso.with(getContext())
                                            .load(imageUrl)
                                            .placeholder(R.drawable.placeholder)
                                            .noFade()
                                            .into(imageView);
                                }
                                if (Constants.USER_ROLE.equalsIgnoreCase("s")) {
                                    name_textView.setText(Html.fromHtml("<h2>" + name + "</h2>"));
                                    studentCode_textView.setText(studentCode);
                                    class_textView.setText(className);
                                    section_textView.setText(section);
                                    pinCode_textView.setText(pinCode);
                                    dob_textView.setText(dob);
                                    gender_textView.setText(gender);
                                    rollNo_textView.setText(rollNo);
                                    motherTongue_textView.setText(motherTongue);
                                    mailingAddress_textView.setText(mailingAddress);
                                    city_textView.setText(city);
                                    state_textView.setText(state);
                                    homePhoneNo_textView.setText(homePhoneNo);
                                    mobileNo_textView.setText(mobileNo);
                                    permanentAddress_textView.setText(permanentAddress);
                                    permanentCity_textView.setText(permanentCity);
                                    permanentState_textView.setText(permanentState);
                                    permanentPinCode_textView.setText(permanentPinCode);
                                    fname_textView.setText(fatherName);
                                    mname_textView.setText(motherName);
                                    cno_textView.setText(contactNo);
                                    address_textView.setText(permanentAddress);
                                    country_textView.setText(country);
                                }
                                if (Constants.USER_ROLE.equalsIgnoreCase("d")) {
                                    director_name_textView.setText(name);
                                    director_pinCode_textView.setText(pinCode);
                                    director_motherTongue_textView.setText(motherTongue);
                                    director_mailingAddress_textView.setText(mailingAddress);
                                    director_city_textView.setText(city);
                                    director_state_textView.setText(state);
                                    director_homePhoneNo_textView.setText(homePhoneNo);
                                    director_mobileNo_textView.setText(mobileNo);
                                    director_permanentAddress_textView.setText(permanentAddress);
                                    director_permanentCity_textView.setText(permanentCity);
                                    director_permanentState_textView.setText(permanentState);
                                    director_permanentPinCode_textView.setText(permanentPinCode);
                                    director_address_textView.setText(permanentAddress);
                                    director_country_textView.setText(country);
                                }
                            }
                        } else {
                            Snackbar snackbar = Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_LONG);
                            // Changing action button text color
                            View sbView = snackbar.getView();
                            TextView textView = sbView.findViewById(R.id.snackbar_text);
                            textView.setTextColor(Color.parseColor(Constants.colorAccent));
                            snackbar.show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                // When the response returned by REST has Http response code other than '200'
                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    pbHeaderProgress.setVisibility(View.GONE);
                }
            });
        } catch (Exception e) {
            pbHeaderProgress.setVisibility(View.GONE);
            e.printStackTrace();
        }
    }

    //--------------------------------
    // fetchTeacherProfile from server
    // with the 'teacherID' param.
    //--------------------------------
    public void fetchTeacherProfile_old() {
        final ProgressBar pbHeaderProgress = myFragmentView.findViewById(R.id.pbHeaderProgress);
        pbHeaderProgress.setVisibility(View.VISIBLE);
        try {
            AsyncHttpClient clientReg = new AsyncHttpClient();
            RequestParams requestParams = new RequestParams();
            requestParams.put("id", Constants.USER_ID);
            clientReg.post(Constants.BASE_SERVER + "teacher_profile/", requestParams, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject obj) {
                    try {
                        pbHeaderProgress.setVisibility(View.GONE);
                        String status = obj.getString("status");
                        String message = obj.getString("message");
                        if (status.equalsIgnoreCase("200")) {
                            JSONArray jsonArrayList = obj.getJSONArray("teacher_details");
                            for (int i = 0; i < jsonArrayList.length(); i++) {
                                JSONObject jsonObject = jsonArrayList.getJSONObject(i);
                                String empId = jsonObject.getString("emp_id");
                                String firstName = jsonObject.getString("first_name");
                                String middleName = jsonObject.getString("middle_name");
                                String lastName = jsonObject.getString("last_name");
                                String name = firstName + " " + middleName + " " + lastName;
                                String address = jsonObject.getString("address");
                                String pincode = jsonObject.getString("pincode");
                                String dob = jsonObject.getString("dob");
                                String sex = jsonObject.getString("sex");
                                String bloodGroup = jsonObject.getString("blood_grp");
                                String email = jsonObject.getString("email");
                                String mobile = jsonObject.getString("mobile");
                                String maritalStatus = jsonObject.getString("marital_status");
                                String salary = jsonObject.getString("salary");
                                String facultyType = jsonObject.getString("faculty_type");
                                String joiningDate = jsonObject.getString("joining_date");
                                String resigningDate = jsonObject.getString("resigning_date");
                                String imageUrl = jsonObject.getString("image");

                                teacher_name_textView.setText(Html.fromHtml("<h2>" + name + "</h2>"));
                                if (imageUrl != null && !imageUrl.equalsIgnoreCase("")) {
                                    Picasso.with(getContext()).load(imageUrl).placeholder(R.drawable.placeholder).noFade().into(teacherImageView);
                                }
                                teacherCode_textView.setText(empId);
                                teacher_address_textView.setText(address);
                                teacher_pincode_textView.setText(pincode);
                                teacher_dob_textView.setText(dob);
                                teacher_sex_textView.setText(sex);
                                teacher_bloodgroup_textView.setText(bloodGroup);
                                teacher_email_textView.setText(email);
                                teacher_mobile_textView.setText(mobile);
                                marital_status_textView.setText(maritalStatus);
                                salary_textView.setText(salary);
                                faculty_type_textView.setText(facultyType);
                                joining_date_textView.setText(joiningDate);
                                resigning_date_textView.setText(resigningDate);
                            }
                        } else {
                            Snackbar snackbar = Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_LONG);
                            // Changing action button text color
                            View sbView = snackbar.getView();
                            TextView textView = sbView.findViewById(R.id.snackbar_text);
                            textView.setTextColor(Color.parseColor(Constants.colorAccent));
                            snackbar.show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                // When the response returned by REST has Http response code other than '200'
                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    pbHeaderProgress.setVisibility(View.GONE);
                }
            });
        } catch (Exception e) {
            pbHeaderProgress.setVisibility(View.GONE);
            e.printStackTrace();
        }
    }

    public void fetchStudentProfile() {
        final ProgressBar pbHeaderProgress = myFragmentView.findViewById(R.id.pbHeaderProgress);
        pbHeaderProgress.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.BASE_SERVER + "student_profile/", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    pbHeaderProgress.setVisibility(View.GONE);
                    String status = obj.getString("status");
                    String message = obj.getString("message");
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
                            String pinCode = jsonObject.getString("pin_Code");
                            String dob = jsonObject.getString("date_of_birth"); //active
                            String gender = jsonObject.getString("gender"); //active
                            String rollNo = jsonObject.getString("roll_no");
                            String motherTongue = jsonObject.getString("mother_tongue");
                            String mailingAddress = jsonObject.getString("mailing_address");
                            String city = jsonObject.getString("city"); //active
                            String state = jsonObject.getString("state"); //active
                            String homePhoneNo = jsonObject.getString("home_phoneno");
                            String contactNo = jsonObject.getString("mobile_number"); // active
                            String permanentAddress = jsonObject.getString("permanent_address"); // active
                            String permanentCity = jsonObject.getString("permanent_city");
                            String permanentState = jsonObject.getString("permanent_state");
                            String permanentPinCode = jsonObject.getString("permanent_pin");
                            String country = jsonObject.getString("country"); // active
                            String studentCode = jsonObject.getString("student_code"); // active
                            String mobileNo = jsonObject.getString("second_mobile_number"); // active
                            String fatherName = jsonObject.getString("father_name"); // active
                            String motherName = jsonObject.getString("mother_name"); // active
                            String imageUrl = jsonObject.getString("image_url"); // active


                            if (imageUrl != null && !imageUrl.equalsIgnoreCase("")) {
                                Picasso.with(getContext())
                                        .load(imageUrl)
                                        .placeholder(R.drawable.placeholder)
                                        .noFade()
                                        .into(imageView);
                            }
                            if (Constants.USER_ROLE.equalsIgnoreCase("s")) {
                                name_textView.setText(Html.fromHtml("<h2>" + name + "</h2>"));
                                studentCode_textView.setText(studentCode);
                                class_textView.setText(className);
                                section_textView.setText(section);
                                pinCode_textView.setText(pinCode);
                                dob_textView.setText(dob);
                                gender_textView.setText(gender);
                                rollNo_textView.setText(rollNo);
                                motherTongue_textView.setText(motherTongue);
                                mailingAddress_textView.setText(mailingAddress);
                                city_textView.setText(city);
                                state_textView.setText(state);
                                homePhoneNo_textView.setText(homePhoneNo);
                                mobileNo_textView.setText(mobileNo);
                                permanentAddress_textView.setText(permanentAddress);
                                permanentCity_textView.setText(permanentCity);
                                permanentState_textView.setText(permanentState);
                                permanentPinCode_textView.setText(permanentPinCode);
                                fname_textView.setText(fatherName);
                                mname_textView.setText(motherName);
                                cno_textView.setText(contactNo);
                                address_textView.setText(permanentAddress);
                                country_textView.setText(country);
                            }
                            if (Constants.USER_ROLE.equalsIgnoreCase("d")) {
                                director_name_textView.setText(name);
                                director_pinCode_textView.setText(pinCode);
                                director_motherTongue_textView.setText(motherTongue);
                                director_mailingAddress_textView.setText(mailingAddress);
                                director_city_textView.setText(city);
                                director_state_textView.setText(state);
                                director_homePhoneNo_textView.setText(homePhoneNo);
                                director_mobileNo_textView.setText(mobileNo);
                                director_permanentAddress_textView.setText(permanentAddress);
                                director_permanentCity_textView.setText(permanentCity);
                                director_permanentState_textView.setText(permanentState);
                                director_permanentPinCode_textView.setText(permanentPinCode);
                                director_address_textView.setText(permanentAddress);
                                director_country_textView.setText(country);
                            }
                        }
                    } else {
                        Snackbar snackbar = Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_LONG);
                        // Changing action button text color
                        View sbView = snackbar.getView();
                        TextView textView = sbView.findViewById(R.id.snackbar_text);
                        textView.setTextColor(Color.parseColor(Constants.colorAccent));
                        snackbar.show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    pbHeaderProgress.setVisibility(View.GONE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pbHeaderProgress.setVisibility(View.GONE);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id", Constants.USER_ID);
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
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(stringRequest);
    }

    public void fetchTeacherProfile() {
        final ProgressBar pbHeaderProgress = myFragmentView.findViewById(R.id.pbHeaderProgress);
        pbHeaderProgress.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.BASE_SERVER + "teacher_profile/", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    pbHeaderProgress.setVisibility(View.GONE);
                    String status = obj.getString("status");
                    String message = obj.getString("message");
                    if (status.equalsIgnoreCase("200")) {
                        JSONArray jsonArrayList = obj.getJSONArray("teacher_details");
                        for (int i = 0; i < jsonArrayList.length(); i++) {
                            JSONObject jsonObject = jsonArrayList.getJSONObject(i);
                            String empId = jsonObject.getString("emp_id");
                            String firstName = jsonObject.getString("first_name");
                            String middleName = jsonObject.getString("middle_name");
                            String lastName = jsonObject.getString("last_name");
                            String name = firstName + " " + middleName + " " + lastName;
                            String address = jsonObject.getString("address");
                            String pincode = jsonObject.getString("pincode");
                            String dob = jsonObject.getString("dob");
                            String sex = jsonObject.getString("sex");
                            String bloodGroup = jsonObject.getString("blood_grp");
                            String email = jsonObject.getString("email");
                            String mobile = jsonObject.getString("mobile");
                            String maritalStatus = jsonObject.getString("marital_status");
                            String salary = jsonObject.getString("salary");
                            String facultyType = jsonObject.getString("faculty_type");
                            String joiningDate = jsonObject.getString("joining_date");
                            String resigningDate = jsonObject.getString("resigning_date");
                            String imageUrl = jsonObject.getString("image");

                            teacher_name_textView.setText(Html.fromHtml("<h2>" + name + "</h2>"));
                            if (imageUrl != null && !imageUrl.equalsIgnoreCase("")) {
                                Picasso.with(getContext()).load(imageUrl).placeholder(R.drawable.placeholder).noFade().into(teacherImageView);
                            }
                            teacherCode_textView.setText(empId);
                            teacher_address_textView.setText(address);
                            teacher_pincode_textView.setText(pincode);
                            teacher_dob_textView.setText(dob);
                            teacher_sex_textView.setText(sex);
                            teacher_bloodgroup_textView.setText(bloodGroup);
                            teacher_email_textView.setText(email);
                            teacher_mobile_textView.setText(mobile);
                            marital_status_textView.setText(maritalStatus);
                            salary_textView.setText(salary);
                            faculty_type_textView.setText(facultyType);
                            joining_date_textView.setText(joiningDate);
                            resigning_date_textView.setText(resigningDate);
                        }
                    } else {
                        Snackbar snackbar = Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_LONG);
                        // Changing action button text color
                        View sbView = snackbar.getView();
                        TextView textView = sbView.findViewById(R.id.snackbar_text);
                        textView.setTextColor(Color.parseColor(Constants.colorAccent));
                        snackbar.show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    pbHeaderProgress.setVisibility(View.GONE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pbHeaderProgress.setVisibility(View.GONE);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id", Constants.USER_ID);
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
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(stringRequest);
    }
}
