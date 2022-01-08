package com.example.crypedu.Pojo;

public class ListSetGet {
    public int mainImage;
    public String mainName, notiCount;

    public ListSetGet(int mainImage, String mainName, String notiCount) {
        this.mainImage = mainImage;
        this.mainName = mainName;
        this.notiCount = notiCount;
    }

    public int getMainImage() {
        return mainImage;
    }

    public void setMainImage(int mainImage) {
        this.mainImage = mainImage;
    }

    public String getMainName() {
        return mainName;
    }

    public void setMainName(String mainName) {
        this.mainName = mainName;
    }

    public String getNotiCount() {
        return notiCount;
    }

    public void setNotiCount(String notiCount) {
        this.notiCount = notiCount;
    }
}
