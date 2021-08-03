package com.pb.criconet.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Manish Yadav on 10/23/2018.
 */

public class CommentModel {
//{"id": "884","comment_text": "csdfsfaafddf","time": "1592389712","user_id": "1735","name": "dfordharma",
//"avatar": "https://criconetonline.com/upload/photos/2017/12/7zuO8Sx5gglDj5CQ1R1q_avatar.jpg","cover": "https://criconetonline.com/upload/photos/d-cover.jpg"}


    private String id;
    private String comment_text;
    private String time;
    private String user_id;
    private String name;
    private String avatar;
    private String user_email;

    public ArrayList<TagModel> getTagsusers() {
        return tagsusers;
    }

    public void setTagsusers(ArrayList<TagModel> tagsusers) {
        this.tagsusers = tagsusers;
    }

    private ArrayList<TagModel>tagsusers;

    // Decodes CommentModel json into CommentModel model object
    public static CommentModel fromJson(JSONObject jsonObject) {
        CommentModel b = new CommentModel();
        // Deserialize json into object fields
        try {
            b.id = jsonObject.optString("id");
            b.comment_text = jsonObject.optString("comment_text");
            b.time = jsonObject.optString("time");
            b.user_id = jsonObject.optString("user_id");
            b.name = jsonObject.optString("name");
            b.avatar = jsonObject.optString("avatar");
            if(jsonObject.has("tagsusers")){
                try {
                    b.tagsusers = handleTagData(jsonObject.getJSONArray("tagsusers"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

//                for (int i = 0; i < jsonObject.length(); i++) {
//                    b.tagsusers.add(new TagModel(jsonObject.getJSONObject(String.valueOf(i))));
//                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            return b;
        }
        // Return new object
        return b;
    }

    // Decodes array of CommentModel json results into CommentModel model objects
    public static ArrayList<CommentModel> fromJson(JSONArray jsonArray) {
        JSONObject modelJson;
        ArrayList<CommentModel> models = new ArrayList<>(jsonArray.length());
        // Process each result in json array, decode and convert to CardModel object
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                modelJson = jsonArray.getJSONObject(i);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
            CommentModel model = CommentModel.fromJson(modelJson);
            if (model != null) {
                models.add(model);
            }
        }

        return models;
    }
    
    

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
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

    public String getUser_name() {
        return name;
    }

    public void setUser_name(String user_name) {
        this.name = user_name;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getUser_image() {
        return avatar;
    }

    public void setUser_image(String user_image) {
        this.avatar = user_image;
    }

    public String getComment() {
        return comment_text;
    }

    public void setComment(String comment_text) {
        this.comment_text = comment_text;
    }

    public static ArrayList<TagModel> handleTagData(JSONArray jsonArray) throws JSONException {
        ArrayList<TagModel> marketPlaces = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            marketPlaces.add(new TagModel(jsonArray.getJSONObject(i)));
        }
        return marketPlaces;
    }
}
