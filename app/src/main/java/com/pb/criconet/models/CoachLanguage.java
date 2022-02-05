package com.pb.criconet.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class CoachLanguage implements Serializable {
    public String getLang_name() {
        return lang_name;
    }

    public void setLang_name(String lang_name) {
        this.lang_name = lang_name;
    }

    public String getName_eng() {
        return name_eng;
    }

    public void setName_eng(String name_eng) {
        this.name_eng = name_eng;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String lang_name="";
    private String name_eng="";
    private String id="";

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    private boolean isSelected;

    public CoachLanguage(JSONObject jsonObject){
        if(jsonObject.has("lang_name")){
            try {
                this.lang_name= jsonObject.getString("lang_name");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(jsonObject.has("name_eng")){
            try {
                this.name_eng= jsonObject.getString("name_eng");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(jsonObject.has("id")){
            try {
                this.id= jsonObject.getString("id");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
