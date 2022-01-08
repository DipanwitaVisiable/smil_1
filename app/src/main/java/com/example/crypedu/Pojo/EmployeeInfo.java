package com.example.crypedu.Pojo;
/*
 *************************************************************************************************
 * Developed by Cryptonsoftech
 * Date: 30-05-2017
 * Details:
 * ***********************************************************************************************
 */
import android.os.Parcel;
import android.os.Parcelable;

public class EmployeeInfo implements Parcelable {
    public String id;
    public String empId;
    public String empName;
    public String empAddress;
    public String empPincode;
    public String empDob;
    public String empSex;
    public String empBloodGroup;
    public String empEmail;
    public String empMobile;
    public String empMarital;
    public String empSalary;
    public String empFacultyType;
    public String empJoiningDate;
    public String empResigningDate;

    public EmployeeInfo(String id, String empId, String empName, String empAddress, String empPincode,
                        String empDob, String empSex, String empBloodGroup, String empEmail, String empMobile,
                        String empMarital, String empSalary, String empFacultyType,
                        String empJoiningDate, String empResigningDate){
        this.id = id;
        this.empId = empId;
        this.empName = empName;
        this.empAddress = empAddress;
        this.empPincode = empPincode;
        this.empDob = empDob;
        this.empSex = empSex;
        this.empBloodGroup = empBloodGroup;
        this.empEmail = empEmail;
        this.empMobile = empMobile;
        this.empMarital = empMarital;
        this.empSalary = empSalary;
        this.empFacultyType = empFacultyType;
        this.empJoiningDate = empJoiningDate;
        this.empResigningDate = empResigningDate;
    }

    private EmployeeInfo(Parcel in){
        this.id = in.readString();
        this.empId = in.readString();
        this.empName = in.readString();
        this.empAddress = in.readString();
        this.empPincode = in.readString();
        this.empDob = in.readString();
        this.empSex = in.readString();
        this.empBloodGroup = in.readString();
        this.empEmail = in.readString();
        this.empMobile = in.readString();
        this.empMarital = in.readString();
        this.empSalary = in.readString();
        this.empFacultyType = in.readString();
        this.empJoiningDate = in.readString();
        this.empResigningDate = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(empId);
        dest.writeString(empName);
        dest.writeString(empAddress);
        dest.writeString(empPincode);
        dest.writeString(empDob);
        dest.writeString(empSex);
        dest.writeString(empBloodGroup);
        dest.writeString(empEmail);
        dest.writeString(empMobile);
        dest.writeString(empMarital);
        dest.writeString(empSalary);
        dest.writeString(empFacultyType);
        dest.writeString(empJoiningDate);
        dest.writeString(empResigningDate);
    }

    public static final Creator<EmployeeInfo> CREATOR = new Creator<EmployeeInfo>() {
        public EmployeeInfo createFromParcel(Parcel in) {
            return new EmployeeInfo(in);
        }

        public EmployeeInfo[] newArray(int size) {
            return new EmployeeInfo[size];

        }
    };

    public String getEmployee() {
        return this.empName;
    }
}
