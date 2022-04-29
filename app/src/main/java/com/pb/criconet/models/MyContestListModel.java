package com.pb.criconet.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class MyContestListModel implements Serializable {
    public String getTotal_points() {
        return total_points;
    }

    public void setTotal_points(String total_points) {
        this.total_points = total_points;
    }

    private String total_points="";

    public JSONObject getMyContestJsonObject() {
        return myContestJsonObject;
    }

    public void setMyContestJsonObject(JSONObject myContestJsonObject) {
        this.myContestJsonObject = myContestJsonObject;
    }

    private JSONObject myContestJsonObject=null;


    public ArrayList<FantasyPlayerPointJson> getFantasyPlayerPointJsons() {
        return fantasyPlayerPointJsons;
    }

    public void setFantasyPlayerPointJsons(ArrayList<FantasyPlayerPointJson> fantasyPlayerPointJsons) {
        this.fantasyPlayerPointJsons = fantasyPlayerPointJsons;
    }

    private ArrayList<FantasyPlayerPointJson>  fantasyPlayerPointJsons=null;

    public static class FantasyPlayerPointJson{
        public String getPlayerId() {
            return playerId;
        }

        public void setPlayerId(String playerId) {
            this.playerId = playerId;
        }

        public String getPlayerName() {
            return playerName;
        }

        public void setPlayerName(String playerName) {
            this.playerName = playerName;
        }

        public String getPlayerPoint() {
            return playerPoint;
        }

        public void setPlayerPoint(String playerPoint) {
            this.playerPoint = playerPoint;
        }

        private String playerId="";
        private String playerName="";
        private String playerPoint="";

        public FantasyPlayerPointJson(JSONObject jsonObject){
            if(jsonObject.has("name")){
                try {
                    this.playerName=jsonObject.getString("name");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if(jsonObject.has("id")){
                try {
                    this.playerId=jsonObject.getString("id");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if(jsonObject.has("points")){
                try {
                    this.playerPoint=jsonObject.getString("points");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public static ArrayList<FantasyPlayerPointJson> getFantasyPlayerPointsList(JSONArray jsonArray){
        ArrayList<FantasyPlayerPointJson> teams=new ArrayList<>();
        for(int i=0;i<jsonArray.length();i++){
            try {
                teams.add(new FantasyPlayerPointJson(jsonArray.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return teams;
    }

    public MyContestListModel(JSONObject jsonObject){
        if(jsonObject.has("match_details")){
            try {
                this.myContestJsonObject=jsonObject.getJSONObject("match_details");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(jsonObject.has("fantsy_player_point_json")){
            try {
                this.fantasyPlayerPointJsons=getFantasyPlayerPointsList(jsonObject.getJSONArray("fantsy_player_point_json"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(jsonObject.has("total_points")){
            try {
                this.total_points=jsonObject.getString("total_points");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
