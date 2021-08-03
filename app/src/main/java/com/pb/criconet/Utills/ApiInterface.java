package com.pb.criconet.Utills;

import com.pb.criconet.models.OrderCreate;

import org.json.JSONObject;

import retrofit.Callback;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

public interface ApiInterface {
    @FormUrlEncoded // annotation used in POST type requests
    @POST("/create_booking_order")     // API's endpoints
    public void booking(@Field("user_id") String user_id,
                        @Field("coach_id") String coach_id,
                        @Field("booking_slot_date") String booking_slot_date,
                        @Field("booking_slot_id") String booking_slot_id,
                        @Field("booking_amount") String booking_amount,
                        @Field("payment_amount") String payment_amount,
                        @Field("taxes_amount") String taxes_amount,
                        @Field("offer_id") String offer_id,
                        @Field("cuurency_code") String cuurency_code,
                        Callback<JSONObject> callback);

}
