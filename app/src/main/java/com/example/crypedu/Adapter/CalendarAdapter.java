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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.activity.smi.R;
import com.example.crypedu.Constants.Constants;
import com.example.crypedu.Constants.Util;
import com.example.crypedu.Pojo.AttendanceInfo;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class CalendarAdapter extends BaseAdapter {
    public static List<String> dayString;
    private GregorianCalendar pMonth;
    /**
     * calendar instance for previous month for getting complete view
     */
    int lastWeekDay, leftDays;
    private DateFormat df;
    Calendar a;
    private ArrayList<AttendanceInfo> attendanceArrayList;
    private Context mContext;
    private Calendar month;
    private GregorianCalendar selectedDate;
    private int firstDay;
    private String curentDateString;
    private ArrayList<String> items;
    private View previousView;
    private int stringFlag = 0;

    public CalendarAdapter(Context c, GregorianCalendar monthCalendar, ArrayList<AttendanceInfo> attendanceArrayList) {

        mContext = c;
        initCalendarAdapter(monthCalendar);
        this.attendanceArrayList = attendanceArrayList;


    }

    private void initCalendarAdapter(GregorianCalendar monthCalendar) {
        CalendarAdapter.dayString = new ArrayList<>();
        month = monthCalendar;
        //Log.e("Months ", String.valueOf(month));

        selectedDate = (GregorianCalendar) monthCalendar.clone();

        month.set(GregorianCalendar.DAY_OF_MONTH, 1);
        this.items = new ArrayList<>();

        adapterSetDate(selectedDate);
        refreshDays();
    }

    public void setItems(ArrayList<String> items) {

        if (items == null)
            return;

        for (int i = 0; i != items.size(); i++) {
            if (items.get(i).length() == 1) {
                items.set(i, "0" + items.get(i));
            }
        }
        this.items = items;
    }


    public int getCount() {
        //Log.e("SIZE ", String.valueOf(dayString.size()));
        return dayString.size();
    }

    public Object getItem(int position) {
        return dayString.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    // create a new view for each item referenced by the Adapter
    @SuppressLint("InflateParams")
    public View getView(int position, View convertView, ViewGroup parent) {

        TextView dayView;
        if (convertView == null) { // if it's not recycled, initialize some
            // attributes
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            assert inflater != null;
            convertView = inflater.inflate(R.layout.calendar_item, null,false);

        }
        Typeface typeface = Typeface.createFromAsset(convertView.getContext().getAssets(), Constants.BubblegumSans_Regular_font);
        dayView = convertView.findViewById(R.id.date);

        String[] parts = curentDateString.split("-");
        int currMonth = month.get(Calendar.MONTH) + 1;

        //Log.e("Months : ", String.valueOf(currMonth));


        // separates daystring into parts.
        String[] separatedTime = dayString.get(position).split("-");
        // taking last part of date. ie; 2 from 2012-12-02
        String gridValue = separatedTime[2].replaceFirst("^0*", "");
        String gridMonth = separatedTime[1].replaceFirst("^0*", "");
        int gridMonthInt = Integer.valueOf(gridMonth);
        //Toast.makeText(mContext, "Pampa"+gridMonthInt, Toast.LENGTH_SHORT).show();

        // checking whether the day is in current month or not.
        if ((Integer.parseInt(gridValue) > 1) && (position < firstDay)) {
            // setting offdays to white color.
            dayView.setTextColor(convertView.getResources().getColor(R.color.colorDiamondWhite));
            dayView.setClickable(false);
            dayView.setFocusable(false);
        } else if ((Integer.parseInt(gridValue) < 15) && (position > 28)) {
            dayView.setTextColor(convertView.getResources().getColor(R.color.colorDiamondWhite));
            dayView.setClickable(false);
            dayView.setFocusable(false);
        } else {
            // setting current month's days in blue color.
            dayView.setTextColor(convertView.getResources().getColor(R.color.colorAccent));
            dayView.setTypeface(typeface);
        }

        if (dayString.get(position).equals(curentDateString)) {
            setSelected(convertView);
            dayView.setTextColor(convertView.getResources().getColor(R.color.colorWhite));
            previousView = convertView;
        } else {
            convertView.setBackgroundResource(R.color.colorTransparent);
        }

        for (AttendanceInfo attendanceInfo : attendanceArrayList) {
            if (attendanceInfo.attendence_status.equalsIgnoreCase("a")) {

                if (currMonth != gridMonthInt) {
                    dayView.setTextColor(convertView.getResources().getColor(R.color.colorDiamondWhite));
                    dayView.setClickable(false);
                    dayView.setFocusable(false);
                } else {

                    if (dayString.get(position).equalsIgnoreCase(attendanceInfo.date)) {
                        dayView.setTextColor(convertView.getResources().getColor(R.color.colorWhite));
                        convertView.setBackgroundResource(R.color.colorAbsent);
                    }
                }

            } else if (attendanceInfo.attendence_status.equalsIgnoreCase("p")) {
                 if (currMonth != gridMonthInt) {
                    dayView.setTextColor(convertView.getResources().getColor(R.color.colorDiamondWhite));
                    dayView.setClickable(false);
                    dayView.setFocusable(false);
                } else if (!attendanceInfo.late_reason.equalsIgnoreCase("")) {
                    if (dayString.get(position).equalsIgnoreCase(attendanceInfo.date)) {
                        dayView.setTextColor(convertView.getResources().getColor(R.color.colorDiamondWhite));
                        convertView.setBackgroundResource(R.color.lateReasonBtnColor);
                    }
                } else {
                    if (dayString.get(position).equalsIgnoreCase(attendanceInfo.date)) {
                        dayView.setTextColor(convertView.getResources().getColor(R.color.colorWhite));
                        convertView.setBackgroundResource(R.color.colorPresent);
                    }
                }

            } else if (attendanceInfo.attendence_status.equalsIgnoreCase("h")) {
                if (dayString.get(position).equalsIgnoreCase(attendanceInfo.date)) {
                    dayView.setTextColor(convertView.getResources().getColor(R.color.colorDiamondWhite));
                    convertView.setBackgroundResource(R.color.colorHoliday);
                }
            } else if (attendanceInfo.attendence_status.equalsIgnoreCase("l")) {
                if (dayString.get(position).equalsIgnoreCase(attendanceInfo.date)) {
                    dayView.setTextColor(convertView.getResources().getColor(R.color.colorDiamondWhite));
                    convertView.setBackgroundResource(R.color.colorLeave);
                }
            }
        }

        dayView.setText(gridValue);

        // create date string for comparison
        String date = dayString.get(position);

        if (date.length() == 1) {
            date = "0" + date;
        }
        String monthStr = "" + (month.get(GregorianCalendar.MONTH) + 1);
        if (monthStr.length() == 1) {
        }

        // show icon if date is not empty and it exists in the items array
        ImageView iw = convertView.findViewById(R.id.date_icon);
        if (date.length() > 0 && items != null && items.contains(date)) {
            iw.setVisibility(View.VISIBLE);
        } else {
            iw.setVisibility(View.INVISIBLE);
        }
        return convertView;
    }

    private void setSelected(View view) {
        if (previousView != null) {
            previousView.setBackgroundResource(R.color.colorAccent);
        }
        previousView = view;
        view.setBackgroundResource(R.color.colorAccent);
    }

    public void refreshDays() {
        // clear items
        items.clear();
        dayString.clear();

        pMonth = (GregorianCalendar) month.clone();
        // month start day. ie; sun, mon, etc
        firstDay = month.get(GregorianCalendar.DAY_OF_WEEK);
        // finding number of weeks in current month.
        int maxWeeknumber = month.getActualMaximum(GregorianCalendar.WEEK_OF_MONTH);
        // allocating maximum row number for the gridview.
        int mnthlength = maxWeeknumber * 7;
        int maxP = getMaxP();
        int calMaxP = maxP - (firstDay - 1);
        /*
          Calendar instance for getting a complete gridview including the three
          month's (previous,current,next) dates.
         */
        // calendar instance for previous month
        GregorianCalendar pMonthMaxSet = (GregorianCalendar) pMonth.clone();
        /*
          setting the start date as previous month's required date.
         */
        pMonthMaxSet.set(GregorianCalendar.DAY_OF_MONTH, calMaxP + 1);

        /*
          filling calendar gridview.
         */
        for (int n = 0; n <= 42; n++) {

            String itemvalue = df.format(pMonthMaxSet.getTime());
            pMonthMaxSet.add(GregorianCalendar.DATE, 1);
            dayString.add(itemvalue);

        }
    }

    private int getMaxP() {
        int maxP;
        if (month.get(GregorianCalendar.MONTH) == month.getActualMinimum(GregorianCalendar.MONTH)) {
            pMonth.set((month.get(GregorianCalendar.YEAR) - 1), month.getActualMaximum(GregorianCalendar.MONTH), 1);
        } else {
            pMonth.set(GregorianCalendar.MONTH,
                    month.get(GregorianCalendar.MONTH) - 1);
        }
        maxP = pMonth.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);

        return maxP;
    }

    private void adapterSetDate(GregorianCalendar monthCalendar) {

        df = new SimpleDateFormat("yyyy-MM-dd", Util.getLocale());
        selectedDate = monthCalendar;
        curentDateString = df.format(selectedDate.getTime());

    }

    public String getSelectedDate() {
        return curentDateString;
    }


}