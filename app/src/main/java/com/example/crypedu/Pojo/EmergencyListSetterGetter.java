package com.example.crypedu.Pojo;

import java.util.ArrayList;

/**
 * Created by INDID on 26-03-2018.
 */

public class EmergencyListSetterGetter {
    private String service_name;
    private String service_icon;

    public String getService_icon() {
        return service_icon;
    }

    public void setService_icon(String service_icon) {
        this.service_icon = service_icon;
    }

    private ArrayList<SubSetGet> sublist;

    public ArrayList<SubSetGet> getSublist() {
        return sublist;
    }

    public void setSublist(ArrayList<SubSetGet> sublist) {
        this.sublist = sublist;
    }

    public String getService_name() {
        return service_name;
    }

    public void setService_name(String service_name) {
        this.service_name = service_name;
    }

}