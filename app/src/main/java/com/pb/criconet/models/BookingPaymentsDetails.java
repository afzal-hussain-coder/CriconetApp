package com.pb.criconet.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class BookingPaymentsDetails implements Serializable {

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getCoachUserId() {
        return coachUserId;
    }

    public void setCoachUserId(String coachUserId) {
        this.coachUserId = coachUserId;
    }

    public String getIsPayment() {
        return isPayment;
    }

    public void setIsPayment(String isPayment) {
        this.isPayment = isPayment;
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

    public String getBookingSlotTxt() {
        return bookingSlotTxt;
    }

    public void setBookingSlotTxt(String bookingSlotTxt) {
        this.bookingSlotTxt = bookingSlotTxt;
    }

    public String getBookingAmount() {
        return bookingAmount;
    }

    public void setBookingAmount(String bookingAmount) {
        this.bookingAmount = bookingAmount;
    }

    public String getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(String paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
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

    public String getCoachName() {
        return coachName;
    }

    public void setCoachName(String coachName) {
        this.coachName = coachName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getProfileLine() {
        return profileLine;
    }

    public void setProfileLine(String profileLine) {
        this.profileLine = profileLine;
    }

    private String Id = "";
    private String coachUserId = "";
    private String isPayment = "";
    private String userId = "";
    private String bookingSlotDate = "";
    private String bookingSlotTxt = "";
    private String bookingAmount = "";
    private String paymentAmount ="";
    private String currencyCode ="";
    private String bookingTime ="";
    private String slotDuration ="";
    private String bookingStatus ="";
    private String onlineSessionStartTime ="";
    private String onlineSessionEndTime ="";
    private String coachName ="";
    private String userName ="";
    private String avatar ="";
    private String profileLine="";
    private String bookingSlotId ="";

    public String getBooking_id() {
        return booking_id;
    }

    public void setBooking_id(String booking_id) {
        this.booking_id = booking_id;
    }

    private String booking_id ="";

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    private String bookingId;


    public String getBookingSlotId() {
        return bookingSlotId;
    }

    public void setBookingSlotId(String bookingSlotId) {
        this.bookingSlotId = bookingSlotId;
    }

    public BookingPaymentsDetails(JSONObject jsonObject) {
        if (jsonObject.has("id")) {
            try {
                this.Id = jsonObject.getString("id");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (jsonObject.has("booking_id")) {
            try {
                this.booking_id = jsonObject.getString("booking_id");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (jsonObject.has("coach_user_id")) {
            try {
                this.coachUserId = jsonObject.getString("coach_user_id");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (jsonObject.has("is_paymet")) {
            try {
                this.isPayment = jsonObject.getString("is_paymet");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (jsonObject.has("user_id")) {
            try {
                this.userId = jsonObject.getString("user_id");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (jsonObject.has("booking_slot_date")) {
            try {
                this.bookingSlotDate = jsonObject.getString("booking_slot_date");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (jsonObject.has("booking_slot_id")) {
            try {
                this.bookingSlotId = jsonObject.getString("booking_slot_id");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (jsonObject.has("booking_slot_txt")) {
            try {
                this.bookingSlotTxt = jsonObject.getString("booking_slot_txt");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (jsonObject.has("booking_amount")) {
            try {
                this.bookingAmount = jsonObject.getString("booking_amount");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (jsonObject.has("payment_amount")) {
            try {
                this.paymentAmount = jsonObject.getString("payment_amount");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (jsonObject.has("currency_code")) {
            try {
                this.currencyCode = jsonObject.getString("currency_code");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (jsonObject.has("booking_time")) {
            try {
                this.bookingTime = jsonObject.getString("booking_time");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (jsonObject.has("slot_duration")) {
            try {
                this.slotDuration = jsonObject.getString("slot_duration");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (jsonObject.has("booking_status")) {
            try {
                this.bookingStatus = jsonObject.getString("booking_status");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (jsonObject.has("online_session_start_time")) {
            try {
                this.onlineSessionStartTime = jsonObject.getString("online_session_start_time");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (jsonObject.has("online_session_end_time")) {
            try {
                this.onlineSessionEndTime = jsonObject.getString("online_session_end_time");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (jsonObject.has("name")) {
            try {
                this.coachName = jsonObject.getString("name");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (jsonObject.has("username")) {
            try {
                this.userName = jsonObject.getString("username");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (jsonObject.has("avatar")) {
            try {
                this.avatar = jsonObject.getString("avatar");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (jsonObject.has("profile_link")) {
            try {
                this.profileLine = jsonObject.getString("profile_link");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
