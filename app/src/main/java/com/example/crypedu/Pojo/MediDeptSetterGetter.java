package com.example.crypedu.Pojo;

/**
 * Created by INDID on 27-02-2018.
 */

public class MediDeptSetterGetter {
    private String cat_id;
    private String cat_name;
    private String chamber_id;

    public String getChamber_id() {
        return chamber_id;
    }
    public void setChamber_id(String chamber_id) {
        this.chamber_id = chamber_id;
    }


    public String getCat_id() {
        return cat_id;
    }

    public void setCat_id(String cat_id) {
        this.cat_id = cat_id;
    }

    public String getCat_name() {
        return cat_name;
    }

    public void setCat_name(String cat_name) {
        this.cat_name = cat_name;
    }
}
