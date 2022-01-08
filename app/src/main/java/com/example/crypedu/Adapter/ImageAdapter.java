package com.example.crypedu.Adapter;
/*
 *************************************************************************************************
 * Developed by Cryptonsoftech
 * Date: 30-05-2017
 * Details:
 * ***********************************************************************************************
 */

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.activity.smi.R;
import com.example.crypedu.Constants.Constants;
import com.key.Key;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class ImageAdapter extends BaseAdapter {
    private final Integer[] integer;
    private final String[] strings;
    private Context mContext;
    LayoutInflater inflater;
    TextView textView;
    TextView tv_noti_count;
    Typeface typeface;
    String student_id;
    int selectedPosition;

    // Constructor
    public ImageAdapter(Context c, Integer[] integer, String[] strings) {
        mContext = c;
        this.integer = integer;
        this.strings = strings;
    }

    public int getCount() {
        return integer.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    @SuppressLint("ViewHolder")
    public View getView(int position, View convertView, ViewGroup parent) {

        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View itemView;
        assert inflater != null;
        selectedPosition=position;

        itemView = inflater.inflate(R.layout.image_adapter, parent, false);
        ImageView imageView = itemView.findViewById(R.id.imageView);
        textView = itemView.findViewById(R.id.textView);
        tv_noti_count = itemView.findViewById(R.id.tv_noti_count);
        imageView.setImageResource(integer[position]);
        textView.setText(strings[position]);
        student_id=mContext.getSharedPreferences(Key.KEY_ACTIVITY_PREF, Context.MODE_PRIVATE).getString("student_id", null);

        typeface = Typeface.createFromAsset(itemView.getContext().getAssets(), Constants.BubblegumSans_Regular_font);
        textView.setTypeface(typeface);

        if (strings[position].equalsIgnoreCase("Profile")) {
            tv_noti_count.setVisibility(View.GONE);
            textView.setTextColor(itemView.getResources().getColor(R.color.colorProfile));

        /*} else if (strings[position].equalsIgnoreCase("Mediwallet")) {
            tv_noti_count.setVisibility(View.GONE);
            textView.setTextColor(itemView.getResources().getColor(R.color.colorAbsent));*/

        } else if (strings[position].equalsIgnoreCase("Activity")) {
            tv_noti_count.setVisibility(View.GONE);
            textView.setTextColor(itemView.getResources().getColor(R.color.colorActivity));

        } else if (strings[position].equalsIgnoreCase("Results")) {
            tv_noti_count.setVisibility(View.GONE);
            textView.setTextColor(itemView.getResources().getColor(R.color.colorResult));

        } else if (strings[position].equalsIgnoreCase("Time Table")) {
            tv_noti_count.setVisibility(View.GONE);
            textView.setTextColor(itemView.getResources().getColor(R.color.colorTimeTable));

        } else if (strings[position].equalsIgnoreCase("Assignment")) {
            //tv_noti_count.setVisibility(View.VISIBLE);
            fetchNotificationCount();
            textView.setTextColor(itemView.getResources().getColor(R.color.colorHomeWork));

        } else if (strings[position].equalsIgnoreCase("Attendance")) {
            //tv_noti_count.setVisibility(View.VISIBLE);
            fetchNotificationCount();
            textView.setTextColor(itemView.getResources().getColor(R.color.colorAttendance));

        } else if (strings[position].equalsIgnoreCase("Notice")) {
            //tv_noti_count.setVisibility(View.VISIBLE);
            fetchNotificationCount();
            textView.setTextColor(itemView.getResources().getColor(R.color.colorNotice));

        } else if (strings[position].equalsIgnoreCase("Bulletins")) {
           // tv_noti_count.setVisibility(View.VISIBLE);
            fetchNotificationCount();
            textView.setTextColor(itemView.getResources().getColor(R.color.colorBulletins));

        } else if (strings[position].equalsIgnoreCase("Pay Online")) {
            tv_noti_count.setVisibility(View.GONE);
            textView.setTextColor(itemView.getResources().getColor(R.color.colorAccount));

        } else if (strings[position].equalsIgnoreCase("Syllabus")) {
            //tv_noti_count.setVisibility(View.GONE);
            textView.setTextColor(itemView.getResources().getColor(R.color.colorSyllabus));

        } else if (strings[position].equalsIgnoreCase("Communication")) {
            //tv_noti_count.setVisibility(View.VISIBLE);
            fetchNotificationCount();
            textView.setTextColor(itemView.getResources().getColor(R.color.colorRequest));

        } else if (strings[position].equalsIgnoreCase("Reset")) {
            tv_noti_count.setVisibility(View.GONE);
            textView.setTextColor(itemView.getResources().getColor(R.color.colorReset));

        } else if (strings[position].equalsIgnoreCase("Logout")) {
            tv_noti_count.setVisibility(View.GONE);
            textView.setTextColor(itemView.getResources().getColor(R.color.colorLogout));

        } else if (strings[position].equalsIgnoreCase("Transportation")) {
            tv_noti_count.setVisibility(View.GONE);
            textView.setTextColor(itemView.getResources().getColor(R.color.colorTransportation));

        /*} else if (strings[position].equalsIgnoreCase("Library")) {
            tv_noti_count.setVisibility(View.GONE);
            textView.setTextColor(itemView.getResources().getColor(R.color.colorLibrary));*/

        } else if (strings[position].equalsIgnoreCase("Examination")) {
            //tv_noti_count.setVisibility(View.VISIBLE);
            fetchNotificationCount();
            textView.setTextColor(itemView.getResources().getColor(R.color.colorExamination));

        }else if (strings[position].equalsIgnoreCase("Online Classes")) {
            tv_noti_count.setVisibility(View.GONE);
            textView.setTextColor(itemView.getResources().getColor(R.color.colorOnlineClasses));

         }else if (strings[position].equalsIgnoreCase("Online Test")) {
            tv_noti_count.setVisibility(View.GONE);
            textView.setTextColor(itemView.getResources().getColor(R.color.colorReset));

        }else if (strings[position].equalsIgnoreCase("Classroom Exam")) {
            tv_noti_count.setVisibility(View.GONE);
            textView.setTextColor(itemView.getResources().getColor(R.color.colorReset));

        }else if (strings[position].equalsIgnoreCase("PTM")) {
            tv_noti_count.setVisibility(View.GONE);
            textView.setTextColor(itemView.getResources().getColor(R.color.colorT));

        }else if (strings[position].equalsIgnoreCase("Zoom Meetings")) {
            tv_noti_count.setVisibility(View.GONE);
            textView.setTextColor(itemView.getResources().getColor(R.color.colorTeacherCalling));
        }

        return itemView;
    }

    private void fetchNotificationCount() {
        try {
            final RequestParams params = new RequestParams();
            params.put("student_id",771 /*Integer.valueOf(student_id)*/);
            Log.d("NotiValue", "getNoti: "+params);
            AsyncHttpClient clientReg = new AsyncHttpClient();
            clientReg.get(Constants.BASE_SERVER + "assignment_count", new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject obj) {
                    try {
                        String status = obj.getString("status");
                        if (status.equalsIgnoreCase("200")) {
                            JSONObject jsonObject = obj.getJSONObject("count");

                            if (!jsonObject.getString("assignment_count").equalsIgnoreCase("0")){
                                tv_noti_count.setVisibility(View.VISIBLE);
                                tv_noti_count.setText(jsonObject.getString("assignment_count"));

                            }else if (!jsonObject.getString("attendance_count").equalsIgnoreCase("0")){
                                tv_noti_count.setVisibility(View.VISIBLE);
                                tv_noti_count.setText(jsonObject.getString("attendance_count"));

                            }else if (!jsonObject.getString("notice_count").equalsIgnoreCase("0")){
                                tv_noti_count.setVisibility(View.VISIBLE);
                                tv_noti_count.setText(jsonObject.getString("notice_count"));

                            }else if (!jsonObject.getString("bulletin_count").equalsIgnoreCase("0")){
                                tv_noti_count.setVisibility(View.VISIBLE);
                                tv_noti_count.setText(jsonObject.getString("bulletin_count"));

                            }else if (!jsonObject.getString("exam_count").equalsIgnoreCase("0")){
                                tv_noti_count.setVisibility(View.VISIBLE);
                                tv_noti_count.setText(jsonObject.getString("exam_count"));

                            }else if (!jsonObject.getString("communication_count").equalsIgnoreCase("0")){
                                tv_noti_count.setVisibility(View.VISIBLE);
                                tv_noti_count.setText(jsonObject.getString("communication_count"));

                            }else {
                                tv_noti_count.setVisibility(View.GONE);
                            }

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                // When the response returned by REST has Http response code other than '200'
                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
