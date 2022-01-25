package com.pb.criconet.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Language {
    @SerializedName("api_status")
    @Expose
    private String apiStatus;
    @SerializedName("api_text")
    @Expose
    private String apiText;
    @SerializedName("api_version")
    @Expose
    private String apiVersion;
    @SerializedName("data")
    @Expose
    private List<Datum> data = null;

    public String getApiStatus() {
        return apiStatus;
    }

    public void setApiStatus(String apiStatus) {
        this.apiStatus = apiStatus;
    }

    public String getApiText() {
        return apiText;
    }

    public void setApiText(String apiText) {
        this.apiText = apiText;
    }

    public String getApiVersion() {
        return apiVersion;
    }

    public void setApiVersion(String apiVersion) {
        this.apiVersion = apiVersion;
    }

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }

    public class Datum {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("lang_name")
        @Expose
        private String lang_name;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return lang_name;
        }

        public void setName(String name) {
            this.lang_name = lang_name;
        }

        public String getName_eng() {
            return name_eng;
        }

        public void setName_eng(String name_eng) {
            this.name_eng = name_eng;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getCreated() {
            return created;
        }

        public void setCreated(String created) {
            this.created = created;
        }

        @SerializedName("name_eng")
        @Expose
        private String name_eng;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("created")
        @Expose
        private String created;


    }
}
