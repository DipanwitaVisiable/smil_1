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
import android.widget.TextView;
import com.activity.smi.R;
import com.example.crypedu.Constants.Constants;
import com.example.crypedu.Pojo.EmployeeInfo;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import de.hdodenhof.circleimageview.CircleImageView;

public class EmployeeAdapter extends BaseAdapter {
    private final Context context;
    private final List<EmployeeInfo> employeeInfos;
    private final ArrayList<EmployeeInfo> arraylist;
    LayoutInflater mInflater;
    LayoutInflater inflater;

    public EmployeeAdapter(Context context, List<EmployeeInfo> employeeInfos, LayoutInflater inflater) {
        mInflater = inflater;
        this.context = context;
        this.employeeInfos = employeeInfos;
        this.arraylist = new ArrayList<>();
        this.arraylist.addAll(employeeInfos);
    }

    @Override
    public int getCount() {
        return employeeInfos.size();
    }

    @Override
    public Object getItem(int position) {
        return employeeInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint({"ViewHolder", "SetTextI18n"})
    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View itemView;
        assert inflater != null;
        itemView = inflater.inflate(R.layout.employee_adapter, parent, false);
        TextView empId_textView = itemView.findViewById(R.id.empId_textView);
        TextView empName_textView = itemView.findViewById(R.id.empName_textView);
        TextView empAddress_textView = itemView.findViewById(R.id.empAddress_textView);
        TextView empPincode_textView = itemView.findViewById(R.id.empPincode_textView);
        TextView empDob_textView = itemView.findViewById(R.id.empDob_textView);
        TextView empSex_textView = itemView.findViewById(R.id.empSex_textView);
        TextView empBloodGroup_textView = itemView.findViewById(R.id.empBloodGroup_textView);
        TextView empEmail_textView = itemView.findViewById(R.id.empEmail_textView);
        TextView empMobile_textView = itemView.findViewById(R.id.empMobile_textView);
        TextView empMarital_textView = itemView.findViewById(R.id.empMarital_textView);
        TextView empSalary_textView = itemView.findViewById(R.id.empSalary_textView);
        TextView empFacultyType_textView = itemView.findViewById(R.id.empFacultyType_textView);
        TextView empDoj_textView = itemView.findViewById(R.id.empDoj_textView);
        TextView empResigningDate_textView = itemView.findViewById(R.id.empResigningDate_textView);
        CircleImageView imageView = itemView.findViewById(R.id.imageView);

        Typeface typeface = Typeface.createFromAsset(itemView.getContext().getAssets(), Constants.BubblegumSans_Regular_font);

        final EmployeeInfo employeeInfo = employeeInfos.get(position);
        empId_textView.setText("Employee Id: " + employeeInfo.empId);
        empId_textView.setTypeface(typeface);
        empName_textView.setText("Name: " + employeeInfo.empName);
        empName_textView.setTypeface(typeface);
        empAddress_textView.setText("Address: " + employeeInfo.empAddress);
        empAddress_textView.setTypeface(typeface);
        empPincode_textView.setText("Pincode: " + employeeInfo.empPincode);
        empPincode_textView.setTypeface(typeface);
        empDob_textView.setText("Date of birth: " + employeeInfo.empDob);
        empDob_textView.setTypeface(typeface);
        empSex_textView.setText("Sex: " + employeeInfo.empSex);
        empSex_textView.setTypeface(typeface);
        empBloodGroup_textView.setText("Blood Group: " + employeeInfo.empBloodGroup);
        empBloodGroup_textView.setTypeface(typeface);
        empEmail_textView.setText("Email: " + employeeInfo.empEmail);
        empEmail_textView.setTypeface(typeface);
        empMobile_textView.setText("Mobile: " + employeeInfo.empMobile);
        empMobile_textView.setTypeface(typeface);
        empMarital_textView.setText("Marital Status: " + employeeInfo.empMarital);
        empMarital_textView.setTypeface(typeface);
        empSalary_textView.setText("Salary: " + employeeInfo.empSalary);
        empSalary_textView.setTypeface(typeface);
        empFacultyType_textView.setText("Faculty Type: " + employeeInfo.empFacultyType);
        empFacultyType_textView.setTypeface(typeface);
        empDoj_textView.setText("Date of joining: " + employeeInfo.empJoiningDate);
        empDoj_textView.setTypeface(typeface);
        empResigningDate_textView.setText("Date of resign: " + employeeInfo.empResigningDate);
        empResigningDate_textView.setTypeface(typeface);
        Picasso.with(context).load("http://imgh.us/soumen_1.jpg").into(imageView);

        return itemView;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        employeeInfos.clear();
        if (charText.length() == 0) {
            employeeInfos.addAll(arraylist);
        }
        else
        {
            for (EmployeeInfo employeeInfo : arraylist)
            {
                if (employeeInfo.getEmployee().toLowerCase(Locale.getDefault()).contains(charText))
                {
                    employeeInfos.add(employeeInfo);
                }
            }
        }
        notifyDataSetChanged();
    }
}
