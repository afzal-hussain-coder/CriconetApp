package com.pb.criconet.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TimeSlot {

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

    @SerializedName("errors")
    @Expose
    private Errors errors;

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

    public Errors getErrors() {
        return errors;
    }

    public void setErrors(Errors errors) {
        this.errors = errors;
    }

    public class Datum {

        @SerializedName("slot_id")
        @Expose
        private String slotId;
        @SerializedName("slot_value")
        @Expose
        private String slotValue;

        @SerializedName("is_selected")
        @Expose
        private String is_selected;

        @SerializedName("is_booked")
        @Expose
        private String is_booked;

        private boolean isActive;

        public String getSlotId() {
            return slotId;
        }

        public void setSlotId(String slotId) {
            this.slotId = slotId;
        }

        public String getSlotValue() {
            return slotValue;
        }

        public void setSlotValue(String slotValue) {
            this.slotValue = slotValue;
        }

        public boolean isActive() {
            return isActive;
        }

        public void setActive(boolean active) {
            isActive = active;
        }

        public String getIs_selected() {
            return is_selected;
        }

        public void setIs_selected(String is_selected) {
            this.is_selected = is_selected;
        }

        public String getIs_booked() {
            return is_booked;
        }

        public void setIs_booked(String is_booked) {
            this.is_booked = is_booked;
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
