package com.pb.criconet.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BookCoach {

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
    private Data data;
    @SerializedName("errors")
    @Expose
    private TimeSlot.Errors errors;

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

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }
    public TimeSlot.Errors getErrors() {
        return errors;
    }

    public void setErrors(TimeSlot.Errors errors) {
        this.errors = errors;
    }

    public class Data {

        @SerializedName("bookin_id")
        @Expose
        private Integer bookinId;

        public Integer getBookinId() {
            return bookinId;
        }

        public void setBookinId(Integer bookinId) {
            this.bookinId = bookinId;
        }

    }

    public class Errors {

        @SerializedName("error_id")
        @Expose
        private String errorId;
        @SerializedName("error_text")
        @Expose
        private String errorText;

        public String getErrorId() {
            return errorId;
        }

        public void setErrorId(String errorId) {
            this.errorId = errorId;
        }

        public String getErrorText() {
            return errorText;
        }

        public void setErrorText(String errorText) {
            this.errorText = errorText;
        }

    }
}
