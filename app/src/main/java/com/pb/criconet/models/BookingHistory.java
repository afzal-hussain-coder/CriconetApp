package com.pb.criconet.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class BookingHistory {

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


    public class Datum implements Serializable {

        @SerializedName("id")
        @Expose
        private String id;

        public long getDuration_in_milisecond() {
            return duration_in_milisecond;
        }

        public void setDuration_in_milisecond(long duration_in_milisecond) {
            this.duration_in_milisecond = duration_in_milisecond;
        }

        @SerializedName("duration_in_milisecond")
        @Expose
        private long duration_in_milisecond;

        @SerializedName("coach_user_id")
        @Expose
        private String coachUserId;
        @SerializedName("is_paymet")
        @Expose
        private String isPaymet;
        @SerializedName("user_id")
        @Expose
        private String userId;

        public String getCancel_enable() {
            return cancel_enable;
        }

        public void setCancel_enable(String cancel_enable) {
            this.cancel_enable = cancel_enable;
        }

        @SerializedName("cancel_enable")
        @Expose
        private String cancel_enable;

        @SerializedName("booking_slot_date")
        @Expose
        private String bookingSlotDate;

        public String getAcademy_name() {
            return academy_name;
        }

        public void setAcademy_name(String academy_name) {
            this.academy_name = academy_name;
        }

        public String getAcademy_id() {
            return academy_id;
        }

        public void setAcademy_id(String academy_id) {
            this.academy_id = academy_id;
        }

        public String getLogo() {
            return logo;
        }

        public void setLogo(String logo) {
            this.logo = logo;
        }

        @SerializedName("academy_name")
        @Expose
        private String academy_name;
        @SerializedName("academy_id")
        @Expose
        private String academy_id;
        @SerializedName("logo")
        @Expose
        private String logo;
        @SerializedName("Address")
        @Expose
        private String Address;

        public String getAddress() {
            return Address;
        }

        public void setAddress(String address) {
            Address = address;
        }

        public String getPackage_valid_for() {
            return package_valid_for;
        }

        public void setPackage_valid_for(String package_valid_for) {
            this.package_valid_for = package_valid_for;
        }

        @SerializedName("package_valid_for")
        @Expose
        private String package_valid_for;

        public String getTraining_type() {
            return training_type;
        }

        public void setTraining_type(String training_type) {
            this.training_type = training_type;
        }

        @SerializedName("training_type")
        @Expose
        private String training_type;











        public String getBooking_date() {
            return booking_date;
        }

        public void setBooking_date(String booking_date) {
            this.booking_date = booking_date;
        }

        @SerializedName("booking_date")
        @Expose
        private String booking_date;

        @SerializedName("booking_slot_id")
        @Expose
        private String bookingSlotId;
        @SerializedName("booking_slot_txt")
        @Expose
        private String bookingSlotTxt;
        @SerializedName("online_session_start_time")
        @Expose
        private String onlineSessionStartTime;
        @SerializedName("online_session_end_time")
        @Expose
        private String onlineSessionEndTime;
        @SerializedName("booking_amount")
        @Expose
        private String bookingAmount;

        public String getPayment_amount() {
            return payment_amount;
        }

        public void setPayment_amount(String payment_amount) {
            this.payment_amount = payment_amount;
        }

        @SerializedName("payment_amount")
        @Expose
        private String payment_amount;

        public String getBookingId() {
            return bookingId;
        }

        public void setBookingId(String bookingId) {
            this.bookingId = bookingId;
        }

        @SerializedName("booking_id")
        @Expose
        private String bookingId;

        public String getChanel_id() {
            return chanel_id;
        }

        public void setChanel_id(String chanel_id) {
            this.chanel_id = chanel_id;
        }

        @SerializedName("chanel_id")
        @Expose
        private String chanel_id;

        public String getPay_leter_str() {
            return pay_leter_str;
        }

        public void setPay_leter_str(String pay_leter_str) {
            this.pay_leter_str = pay_leter_str;
        }

        @SerializedName("pay_leter_str")
        @Expose
        private String pay_leter_str;

        public String getMessage_counter() {
            return message_counter;
        }

        public void setMessage_counter(String message_counter) {
            this.message_counter = message_counter;
        }

        @SerializedName("message_counter")
        @Expose
        private String message_counter;


        @SerializedName("booking_time")
        @Expose
        private String bookingTime;
        @SerializedName("slot_duration")
        @Expose
        private String slotDuration;
        @SerializedName("booking_status")
        @Expose
        private String bookingStatus;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("username")
        @Expose
        private String username;
        @SerializedName("avatar")
        @Expose
        private String avatar;
        @SerializedName("profile_link")
        @Expose
        private String profileLink;
        @SerializedName("btn1")
        @Expose
        private String btn1;

        @SerializedName("btn2")
        @Expose
        private String btn2;

        public String getDevice_message() {
            return device_message;
        }

        public void setDevice_message(String device_message) {
            this.device_message = device_message;
        }

        @SerializedName("device_message")
        @Expose
        private String device_message;


        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCoachUserId() {
            return coachUserId;
        }

        public void setCoachUserId(String coachUserId) {
            this.coachUserId = coachUserId;
        }

        public String getIsPaymet() {
            return isPaymet;
        }

        public void setIsPaymet(String isPaymet) {
            this.isPaymet = isPaymet;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getBookingSlotDate() {
            return bookingSlotDate;
        }

        public void setBookingSlotDate(String bookingSlotDate) {
            this.bookingSlotDate = bookingSlotDate;
        }

        public String getBookingSlotId() {
            return bookingSlotId;
        }

        public void setBookingSlotId(String bookingSlotId) {
            this.bookingSlotId = bookingSlotId;
        }

        public String getBookingSlotTxt() {
            return bookingSlotTxt;
        }

        public void setBookingSlotTxt(String bookingSlotTxt) {
            this.bookingSlotTxt = bookingSlotTxt;
        }

        public String getOnlineSessionStartTime() {
            return onlineSessionStartTime;
        }

        public void setOnlineSessionStartTime(String onlineSessionStartTime) {
            this.onlineSessionStartTime = onlineSessionStartTime;
        }

        public String getOnlineSessionEndTime() {
            return onlineSessionEndTime;
        }

        public void setOnlineSessionEndTime(String onlineSessionEndTime) {
            this.onlineSessionEndTime = onlineSessionEndTime;
        }

        public String getBookingAmount() {
            return bookingAmount;
        }

        public void setBookingAmount(String bookingAmount) {
            this.bookingAmount = bookingAmount;
        }

        public String getBookingTime() {
            return bookingTime;
        }

        public void setBookingTime(String bookingTime) {
            this.bookingTime = bookingTime;
        }

        public String getSlotDuration() {
            return slotDuration;
        }

        public void setSlotDuration(String slotDuration) {
            this.slotDuration = slotDuration;
        }

        public String getBookingStatus() {
            return bookingStatus;
        }

        public void setBookingStatus(String bookingStatus) {
            this.bookingStatus = bookingStatus;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getProfileLink() {
            return profileLink;
        }

        public void setProfileLink(String profileLink) {
            this.profileLink = profileLink;
        }

        public String getBtn1() {
            return btn1;
        }

        public void setBtn1(String btn1) {
            this.btn1 = btn1;
        }

        public String getBtn2() {
            return btn2;
        }

        public void setBtn2(String btn2) {
            this.btn2 = btn2;
        }
    }
}
