package com.pb.criconet.retrofit;

import com.google.gson.annotations.SerializedName;

public class ResultObject {
//    "api_status":"200","api_text":"success","api_version":"1.4.4","post_id":9074,"multi_image":0}

    @SerializedName("api_status")
    private String api_status;

    @SerializedName("api_text")
    private String api_text;

    @SerializedName("api_version")
    private String api_version;

    @SerializedName("post_id")
    private String post_id;

    @SerializedName("multi_image")
    private String multi_image;

    public ResultObject(String api_status, String api_text, String api_version, String post_id, String multi_image) {
        this.api_status = api_status;
        this.api_text = api_text;
        this.api_version = api_version;
        this.post_id = post_id;
        this.multi_image = multi_image;
    }

    public ResultObject() {
    }

    public String getApi_status() {
        return api_status;
    }

    public void setApi_status(String api_status) {
        this.api_status = api_status;
    }

    public String getApi_text() {
        return api_text;
    }

    public void setApi_text(String api_text) {
        this.api_text = api_text;
    }

    public String getApi_version() {
        return api_version;
    }

    public void setApi_version(String api_version) {
        this.api_version = api_version;
    }

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    public String getMulti_image() {
        return multi_image;
    }

    public void setMulti_image(String multi_image) {
        this.multi_image = multi_image;
    }

    @Override
    public String toString() {
        return "ResultObject{" +
                "api_status='" + api_status + '\'' +
                ", api_text='" + api_text + '\'' +
                ", api_version='" + api_version + '\'' +
                ", post_id='" + post_id + '\'' +
                ", multi_image='" + multi_image + '\'' +
                '}';
    }
}