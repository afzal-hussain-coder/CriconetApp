package com.pb.criconet.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class FeedbackModel implements Serializable {
    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    private String question ="question";
    private String type ="type";

    public String getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(String question_id) {
        this.question_id = question_id;
    }

    private String question_id ="question_id";
    public FeedbackModel(JSONObject jsonObject){
        if(jsonObject.has("type")){
            try {
                this.type = jsonObject.getString("type");
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        if(jsonObject.has("question")){
            try {
                this.question = jsonObject.getString("question");
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        if(jsonObject.has("question_id")){
            try {
                this.question_id = jsonObject.getString("question_id");
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }
}
