package com.pb.criconet.models;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class VideoDetailsModel {

    private String video_title;
    private String author_name;
    private String thumbnail_url;
    private String sthumbnail_url;

    // Decodes VideoDetailsModel json into VideoDetailsModel model object
    public static VideoDetailsModel fromJson(JSONObject jsonObject) {
        VideoDetailsModel b = new VideoDetailsModel();
        // Deserialize json into object fields
        try {
            b.video_title = jsonObject.optString("video_title");
            b.author_name = jsonObject.optString("author_name");
            b.thumbnail_url = jsonObject.optString("thumbnail_url");
            b.sthumbnail_url = jsonObject.optString("sthumbnail_url");
        } catch (Exception e) {
            e.printStackTrace();
            return b;
        }
        // Return new object
        return b;
    }

    // Decodes array of VideoDetailsModel json results into VideoDetailsModel model objects
    public static ArrayList<VideoDetailsModel> fromJson(JSONArray jsonArray) {
        JSONObject modelJson;
        ArrayList<VideoDetailsModel> modeles = new ArrayList<>(jsonArray.length());
        // Process each result in json array, decode and convert to CardModel object
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                modelJson = jsonArray.getJSONObject(i);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
            VideoDetailsModel model = VideoDetailsModel.fromJson(modelJson);
            if (model != null) {
                modeles.add(model);
            }
        }

        return modeles;
    }

    public String getVideo_title() {
        return video_title;
    }

    public void setVideo_title(String video_title) {
        this.video_title = video_title;
    }

    public String getAuthor_name() {
        return author_name;
    }

    public void setAuthor_name(String author_name) {
        this.author_name = author_name;
    }

    public String getThumbnail_url() {
        return thumbnail_url;
    }

    public void setThumbnail_url(String thumbnail_url) {
        this.thumbnail_url = thumbnail_url;
    }

    public String getSthumbnail_url() {
        return sthumbnail_url;
    }

    public void setSthumbnail_url(String sthumbnail_url) {
        this.sthumbnail_url = sthumbnail_url;
    }
}
