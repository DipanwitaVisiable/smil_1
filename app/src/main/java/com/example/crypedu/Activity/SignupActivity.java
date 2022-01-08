package com.example.crypedu.Activity;
/*
 *************************************************************************************************
 * Developed by Cryptonsoftech
 * Date: 30-05-2017
 * Details:
 * ***********************************************************************************************
 */

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.RequiresApi;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.activity.smi.InternetCheckActivity;
import com.activity.smi.R;
import com.example.crypedu.Constants.Constants;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

import cz.msebera.android.httpclient.Header;

public class SignupActivity extends AppCompatActivity {

    private static final int FILE_SELECT_CODE = 0;
    private EditText name_editText;
    private EditText mobile_editText;
    private EditText address_editText;
    private EditText pincode_editText;
    private EditText dob_editText;
    private EditText marital_editText;
    private EditText category_editText;
    private EditText doj_editText;
    private EditText browse_editText;
    private EditText email_editText;
    private Calendar myCalendar;
    private String imagepath;
    private RadioGroup gender_radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        /*if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }  // To prevent screen recording*/


        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();

        name_editText = findViewById(R.id.name_editText);
        mobile_editText = findViewById(R.id.mobile_editText);
        address_editText = findViewById(R.id.address_editText);
        pincode_editText = findViewById(R.id.pincode_editText);
        dob_editText = findViewById(R.id.dob_editText);
        marital_editText = findViewById(R.id.marital_editText);
        category_editText = findViewById(R.id.category_editText);
        doj_editText = findViewById(R.id.doj_editText);
        browse_editText = findViewById(R.id.browse_editText);
        email_editText = findViewById(R.id.email_editText);
        Button signup_button = findViewById(R.id.signup_button);
        gender_radioGroup = findViewById(R.id.gender_radioGroup);

        signup_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    if (InternetCheckActivity.isConnected()) {
                        fetchAddEmployee();
                    }else {
                        showSnack();
                    }
                }
            }
        });

        browse_editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });

        myCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener dateOfJoining = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "MM/dd/yyyy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                doj_editText.setText(sdf.format(myCalendar.getTime()));
            }
        };
        doj_editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(SignupActivity.this, dateOfJoining, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        final DatePickerDialog.OnDateSetListener dateOfBirth = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "MM/dd/yyyy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                dob_editText.setText(sdf.format(myCalendar.getTime()));
            }
        };
        dob_editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(SignupActivity.this, dateOfBirth, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private void showSnack(){
        Snackbar snackbar = Snackbar.make(browse_editText, " Please connect your working internet connection. ", Snackbar.LENGTH_LONG);
        // Changing action button text color
        View sbView = snackbar.getView();
        TextView textView = sbView.findViewById(R.id.snackbar_text);
        textView.setTextColor(Color.parseColor(Constants.colorAccent));
        snackbar.show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Complete action using"), 1);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            String photo = Objects.requireNonNull(data.getData()).getPath();
            browse_editText.setText(photo);
        }
    }

    public boolean validate() {
        boolean valid = true;
        String name = name_editText.getText().toString();
        String mobile = mobile_editText.getText().toString();
        String address = address_editText.getText().toString();
        String pincode = pincode_editText.getText().toString();
        String dob = dob_editText.getText().toString();
        String marital = marital_editText.getText().toString();
        String category = category_editText.getText().toString();
        String doj = doj_editText.getText().toString();
        String browse = browse_editText.getText().toString();
        String email = email_editText.getText().toString();

        if (name.isEmpty() || name.trim().length() == 0) {
            name_editText.setError("enter name");
            valid = false;
        } else if (name.length() < 3) {
            name_editText.setError("at least 3 characters");
            valid = false;
        } else {
            name_editText.setError(null);
        }
        if (mobile.isEmpty() || mobile.trim().length() == 0) {
            mobile_editText.setError("enter mobile number");
            valid = false;
        } else if (mobile.length() < 10) {
            mobile_editText.setError("at least 10 numbers");
            valid = false;
        } else {
            mobile_editText.setError(null);
        }
        if (address.isEmpty() || address.trim().length() == 0) {
            address_editText.setError("enter address");
            valid = false;
        } else {
            address_editText.setError(null);
        }
        if (pincode.isEmpty() || pincode.trim().length() == 0) {
            pincode_editText.setError("enter pincode");
            valid = false;
        } else if (pincode.length() < 6) {
            pincode_editText.setError("at least 6 numbers");
            valid = false;
        } else {
            pincode_editText.setError(null);
        }
        if (dob.isEmpty() || dob.trim().length() == 0) {
            dob_editText.setError("enter date of birth");
            valid = false;
        } else {
            dob_editText.setError(null);
        }
        if (marital.isEmpty() || marital.trim().length() == 0) {
            marital_editText.setError("enter marital status");
            valid = false;
        } else {
            marital_editText.setError(null);
        }
        if (category.isEmpty() || category.trim().length() == 0) {
            category_editText.setError("enter marital status");
            valid = false;
        } else {
            category_editText.setError(null);
        }
        if (doj.isEmpty() || doj.trim().length() == 0) {
            doj_editText.setError("enter date of joining");
            valid = false;
        } else {
            doj_editText.setError(null);
        }
        if (browse.isEmpty() || browse.trim().length() == 0) {
            browse_editText.setError("browse profile image");
            valid = false;
        } else {
            browse_editText.setError(null);
        }
        if (email.isEmpty() || email.trim().length() == 0) {
            email_editText.setError("enter an email address");
            valid = false;
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            email_editText.setError("enter a valid email address");
            valid = false;
        } else {
            email_editText.setError(null);
        }

        return valid;
    }

    public void fetchAddEmployee() {
        /*
         *  Progress Bar
         */
        final ProgressBar pbHeaderProgress = findViewById(R.id.pbHeaderProgress);
        pbHeaderProgress.setVisibility(View.VISIBLE);
        try {
            String name = name_editText.getText().toString();
            String mobile = mobile_editText.getText().toString();
            String address = address_editText.getText().toString();
            String pincode = pincode_editText.getText().toString();
            String dob = dob_editText.getText().toString();
            String marital = marital_editText.getText().toString();
            String category = category_editText.getText().toString();
            String doj = doj_editText.getText().toString();
            String browse = browse_editText.getText().toString();
            String email = email_editText.getText().toString();

            int selectedId = gender_radioGroup.getCheckedRadioButtonId();
            RadioButton radioButton = findViewById(selectedId);

            File[] files = {new File(browse)};
            RequestParams params = new RequestParams();
            params.put("full_name", name);
            params.put("address", address);
            params.put("pincode", pincode);
            params.put("dob", dob);
            params.put("marital", marital);
            params.put("mobile", mobile);
            params.put("category", category);
            params.put("join_date", doj);
            params.put("profile_image", (File[]) null);
            params.put("email", email);
            params.put("gender", radioButton.getText().toString());
            AsyncHttpClient clientReg = new AsyncHttpClient();
            clientReg.post("http://" + Constants.server_name + "webservices/websvc/add_employee/", params, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject obj) {
                    try {
                        pbHeaderProgress.setVisibility(View.GONE);
                        String status = obj.getString("status");
                        String message = obj.getString("message");
                        if (status.equalsIgnoreCase("200")) {
//                            String name = obj.getString("name");
                            Intent intent = new Intent(SignupActivity.this, MenuActivity.class);
                            startActivity(intent);
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
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
}
