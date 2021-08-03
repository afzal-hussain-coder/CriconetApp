package com.pb.criconet.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

public class FeedBackFormChildData implements Serializable {
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getLow_str() {
        return low_str;
    }

    public void setLow_str(String low_str) {
        this.low_str = low_str;
    }

    public String getHigh_str() {
        return high_str;
    }

    public void setHigh_str(String high_str) {
        this.high_str = high_str;
    }

    private String id="";
    private String question="";
    private String low_str="";
    private String high_str="";

    public ArrayList<String> getRatinglist() {
        return ratinglist;
    }

    public void setRatinglist(ArrayList<String> ratinglist) {
        this.ratinglist = ratinglist;
    }

    ArrayList<String>ratinglist;


    public FeedBackFormChildData(JSONObject jsonObject){
          if(jsonObject.has("id")){
              try {
                  this.id= jsonObject.getString("id");
              } catch (JSONException e) {
                  e.printStackTrace();
              }
          }

        if(jsonObject.has("question")){
            try {
                this.question= jsonObject.getString("question");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if(jsonObject.has("low_str")){
            try {
                this.low_str= jsonObject.getString("low_str");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if(jsonObject.has("high_str")){
            try {
                this.high_str= jsonObject.getString("high_str");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(jsonObject.has("rating")){
            try {
                this.ratinglist= handleProductPrice(jsonObject.getJSONArray("rating"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private ArrayList<String> handleProductPrice(JSONArray jsonArray) throws JSONException {
        ArrayList<String> marketPlaces = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            marketPlaces.add(jsonArray.getString(i));
        }
        return marketPlaces;
    }
}
