package com.example.crypedu.Pojo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chandan on 06/09/18.
 */
public class Brand {

    private Category brandName;
    public List<Mobile> mobiles;

    public Brand(Category brandName, List<Mobile> mobiles) {
        this.brandName = brandName;
        this.mobiles = mobiles;
    }

    public Category getBrandName() {
        return brandName;
    }

    public void setBrandName(Category brandName) {
        this.brandName = brandName;
    }

    public List<Mobile> getMobiles() {
        return mobiles;
    }

    public void setMobiles(ArrayList<Mobile> mobiles) {
        this.mobiles = mobiles;
    }
}
