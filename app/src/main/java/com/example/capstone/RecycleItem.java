package com.example.capstone;

public class RecycleItem {

    String recycleName;
    String recycleInfo;


    public RecycleItem(String recycleName, String recycleInfo) {
        this.recycleName = recycleName;
        this.recycleInfo = recycleInfo;
    }


    public String getRecycleName() {
        return recycleName;
    }

    public void setRecycleName(String recycleName) {
        this.recycleName = recycleName;
    }

    public String getRecycleInfo() {
        return recycleInfo;
    }

    public void setRecycleInfo(String recycleInfo) {
        this.recycleInfo = recycleInfo;
    }

}
