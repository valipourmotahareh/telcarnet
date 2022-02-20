package com.thimat.sockettelkarnet.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ListNewsModel {
    @SerializedName("news")
    @Expose
    ArrayList<NewsModel> newsModelList = new ArrayList<NewsModel>();
    //------------------------------------------------------

    public ArrayList<NewsModel> getNewsModelList() {
        return newsModelList;
    }

    public void setNewsModelList(ArrayList<NewsModel> newsModelList) {
        this.newsModelList = newsModelList;
    }
}
