package com.pb.criconet.models;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class LiveStreamingModel {

    private String title;
    private String address;
    private String first_player;
    private String first_player_type;
    private String first_player_link;
    private String first_player_status;
    private String second_player;
    private String second_player_type;
    private String second_player_link;
    private String is_power;
    private String power_msg;
    private String streaming_feedback_id;
    private String is_free_access;
    private String cover;
    private String desc;

    public String getSecond_player_status() {
        return second_player_status;
    }

    public void setSecond_player_status(String second_player_status) {
        this.second_player_status = second_player_status;
    }

    private String second_player_status;

    public String getIs_match_start_lebel() {
        return is_match_start_lebel;
    }

    public void setIs_match_start_lebel(String is_match_start_lebel) {
        this.is_match_start_lebel = is_match_start_lebel;
    }

    private String is_match_start_lebel;

    public String getIs_match_start() {
        return is_match_start;
    }

    public void setIs_match_start(String is_match_start) {
        this.is_match_start = is_match_start;
    }

    private String is_match_start;

    // Decodes LiveStreamingModel json into LiveStreamingModel model object
    public static LiveStreamingModel fromJson(JSONObject jsonObject) {
        LiveStreamingModel b = new LiveStreamingModel();
        // Deserialize json into object fields
        try {
            b.title = jsonObject.optString("title");
            b.address = jsonObject.optString("address");
            b.first_player = jsonObject.optString("first_player");
            b.first_player_type = jsonObject.optString("first_player_type");
            b.title = jsonObject.optString("title");
            b.first_player_link = jsonObject.optString("first_player_link");
            b.first_player_status = jsonObject.optString("first_player_status");
            b.second_player = jsonObject.optString("second_player");
            b.second_player_type = jsonObject.optString("second_player_type");
            b.second_player_link = jsonObject.optString("second_player_link");
            b.second_player_status = jsonObject.optString("second_player_status");
            b.is_power = jsonObject.optString("is_power");
            b.power_msg = jsonObject.optString("power_msg");
            b.streaming_feedback_id = jsonObject.optString("streaming_feedback_id");
            b.is_free_access = jsonObject.optString("is_free_access");
            b.cover = jsonObject.optString("cover");
            b.desc = jsonObject.optString("desc");
            b.is_match_start = jsonObject.optString("is_match_start");
            b.is_match_start_lebel = jsonObject.optString("is_match_start_lebel");

        } catch (Exception e) {
            e.printStackTrace();
            return b;
        }
        // Return new object
        return b;
    }

    // Decodes LiveStreamingModel model into json object
    public static JSONObject toJson(LiveStreamingModel b) {
        JSONObject object = new JSONObject();
        // Deserialize object into json fields
        try {
            object.putOpt("title",b.title);
            object.putOpt("address",b.address);
            object.putOpt("first_player",b.first_player);
            object.putOpt("first_player_type",b.first_player_type);
            object.putOpt("first_player_link",b.first_player_link);
            object.putOpt("first_player_status",b.first_player_status);
            object.putOpt("second_player",b.second_player);
            object.putOpt("second_player_type",b.second_player_type);
            object.putOpt("second_player_link",b.second_player_link);
            object.putOpt("second_player_status",b.second_player_status);
            object.putOpt("is_power",b.is_power);
            object.putOpt("power_msg",b.power_msg);
            object.putOpt("streaming_feedback_id",b.streaming_feedback_id);
            object.putOpt("is_free_access",b.is_free_access);
            object.putOpt("cover",b.cover);
            object.putOpt("desc",b.desc);
            object.putOpt("is_match_start",b.is_match_start);
            object.putOpt("is_match_start_lebel",b.is_match_start_lebel);
        } catch (Exception e) {
            e.printStackTrace();
            return object;
        }
        // Return new object
        return object;
    }

    // Decodes array of LiveStreamingModel json results into LiveStreamingModel model objects
    public static ArrayList<LiveStreamingModel> fromJson(JSONArray jsonArray) {
        JSONObject modelJson;
        ArrayList<LiveStreamingModel> modeles = new ArrayList<>(jsonArray.length());
        // Process each result in json array, decode and convert to CardModel object
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                modelJson = jsonArray.getJSONObject(i);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
            LiveStreamingModel model = LiveStreamingModel.fromJson(modelJson);
            if (model != null) {
                modeles.add(model);
            }
        }

        return modeles;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getFirst_player() {
        return first_player;
    }

    public void setFirst_player(String first_player) {
        this.first_player = first_player;
    }

    public String getFirst_player_type() {
        return first_player_type;
    }

    public void setFirst_player_type(String first_player_type) {
        this.first_player_type = first_player_type;
    }

    public String getFirst_player_link() {
        return first_player_link;
    }

    public void setFirst_player_link(String first_player_link) {
        this.first_player_link = first_player_link;
    }

    public String getFirst_player_status() {
        return first_player_status;
    }

    public void setFirst_player_status(String first_player_status) {
        this.first_player_status = first_player_status;
    }

    public String getSecond_player() {
        return second_player;
    }

    public void setSecond_player(String second_player) {
        this.second_player = second_player;
    }

    public String getSecond_player_type() {
        return second_player_type;
    }

    public void setSecond_player_type(String second_player_type) {
        this.second_player_type = second_player_type;
    }

    public String getSecond_player_link() {
        return second_player_link;
    }

    public void setSecond_player_link(String second_player_link) {
        this.second_player_link = second_player_link;
    }

    public String getIs_power() {
        return is_power;
    }

    public void setIs_power(String is_power) {
        this.is_power = is_power;
    }

    public String getPower_msg() {
        return power_msg;
    }

    public void setPower_msg(String power_msg) {
        this.power_msg = power_msg;
    }

    public String getStreaming_feedback_id() {
        return streaming_feedback_id;
    }

    public void setStreaming_feedback_id(String streaming_feedback_id) {
        this.streaming_feedback_id = streaming_feedback_id;
    }

    public String getIs_free_access() {
        return is_free_access;
    }

    public void setIs_free_access(String is_free_access) {
        this.is_free_access = is_free_access;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
