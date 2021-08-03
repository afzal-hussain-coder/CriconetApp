package com.pb.criconet.models;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class VideoModel {

    private String id;
    private String user_id;
    private String ground_name;
    private String team_name;
    private String title;
    private String location;
    private String date_time;
    private String video_link;
    private String status;
    private String country_id;
    private String state_id;
    private String city_id;
    private String created;
    private VideoDetailsModel video_details;

    // Decodes VideoModel json into VideoModel model object
    public static VideoModel fromJson(JSONObject jsonObject) {
        VideoModel b = new VideoModel();
        // Deserialize json into object fields
        try {
            b.id = jsonObject.optString("galleryId");
            b.user_id = jsonObject.optString("user_id");
            b.ground_name = jsonObject.optString("ground_name");
            b.team_name = jsonObject.optString("team_name");
            b.title = jsonObject.optString("title");
            b.location = jsonObject.optString("location");
            b.date_time = jsonObject.optString("date_time");
            b.video_link = jsonObject.optString("video_link");
            b.status = jsonObject.optString("status");
            b.country_id = jsonObject.optString("country_id");
            b.state_id = jsonObject.optString("state_id");
            b.city_id = jsonObject.optString("city_id");
            b.created = jsonObject.optString("created");
            b.video_details = VideoDetailsModel.fromJson(jsonObject.optJSONObject("video_details"));
        } catch (Exception e) {
            e.printStackTrace();
            return b;
        }
        // Return new object
        return b;
    }

    // Decodes array of VideoModel json results into VideoModel model objects
    public static ArrayList<VideoModel> fromJson(JSONArray jsonArray) {
        JSONObject modelJson;
        ArrayList<VideoModel> modeles = new ArrayList<>(jsonArray.length());
        // Process each result in json array, decode and convert to CardModel object
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                modelJson = jsonArray.getJSONObject(i);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
            VideoModel model = VideoModel.fromJson(modelJson);
            if (model != null) {
                modeles.add(model);
            }
        }

        return modeles;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getGround_name() {
        return ground_name;
    }

    public void setGround_name(String ground_name) {
        this.ground_name = ground_name;
    }

    public String getTeam_name() {
        return team_name;
    }

    public void setTeam_name(String team_name) {
        this.team_name = team_name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDate_time() {
        return date_time;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }

    public String getVideo_link() {
        return video_link;
    }

    public void setVideo_link(String video_link) {
        this.video_link = video_link;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCountry_id() {
        return country_id;
    }

    public void setCountry_id(String country_id) {
        this.country_id = country_id;
    }

    public String getState_id() {
        return state_id;
    }

    public void setState_id(String state_id) {
        this.state_id = state_id;
    }

    public String getCity_id() {
        return city_id;
    }

    public void setCity_id(String city_id) {
        this.city_id = city_id;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public VideoDetailsModel getVideo_details() {
        return video_details;
    }

    public void setVideo_details(VideoDetailsModel video_details) {
        this.video_details = video_details;
    }
}
