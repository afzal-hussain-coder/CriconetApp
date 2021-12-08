package com.pb.criconet.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class RewardsModell implements Serializable {
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public String getText_1() {
        return text_1;
    }

    public void setText_1(String text_1) {
        this.text_1 = text_1;
    }

    public String getText_2() {
        return text_2;
    }

    public void setText_2(String text_2) {
        this.text_2 = text_2;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    private String id="";
    private String points="";
    private String text_1="";
    private String text_2="";
    private String image="";

    public RewardsModell(JSONObject jsonObject){
        if(jsonObject.has("id")){
            try {
                this.id=jsonObject.getString("id");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(jsonObject.has("points")){
            try {
                this.points=jsonObject.getString("points");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(jsonObject.has("text_1")){
            try {
                this.text_1=jsonObject.getString("text_1");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(jsonObject.has("text_2")){
            try {
                this.text_2=jsonObject.getString("text_2");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(jsonObject.has("image")){
            try {
                this.image=jsonObject.getString("image");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }
}
