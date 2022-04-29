package com.pb.criconet.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class MatchList implements Serializable {
    public String getPlayer_api_pur() {
        return player_api_pur;
    }

    public void setPlayer_api_pur(String player_api_pur) {
        this.player_api_pur = player_api_pur;
    }

    public String getDate_time() {
        return date_time;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }

    private String player_api_pur="";
    private String  date_time ="";

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMatchType() {
        return matchType;
    }

    public void setMatchType(String matchType) {
        this.matchType = matchType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDateTimeGMT() {
        return dateTimeGMT;
    }

    public void setDateTimeGMT(String dateTimeGMT) {
        this.dateTimeGMT = dateTimeGMT;
    }

    private String id="";
    private String name="";
    private String matchType="";
    private String status="";
    private String venue="";
    private String date ="";
    private String dateTimeGMT="";

    public ArrayList<Teams> getTeamsArrayList() {
        return teamsArrayList;
    }

    public void setTeamsArrayList(ArrayList<Teams> teamsArrayList) {
        this.teamsArrayList = teamsArrayList;
    }

    private ArrayList<Teams>teamsArrayList=null;


    public MatchList(JSONObject jsonObject){
        if(jsonObject.has("id")){
            try {
                this.id= jsonObject.getString("id");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(jsonObject.has("name")){
            try {
                this.name= jsonObject.getString("name");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(jsonObject.has("matchType")){
            try {
                this.matchType= jsonObject.getString("matchType");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(jsonObject.has("status")){
            try {
                this.status= jsonObject.getString("status");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if(jsonObject.has("venue")){
            try {
                this.venue= jsonObject.getString("venue");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(jsonObject.has("date")){
            try {
                this.date= jsonObject.getString("date");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(jsonObject.has("dateTimeGMT")){
            try {
                this.dateTimeGMT= jsonObject.getString("dateTimeGMT");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(jsonObject.has("teams")){
            try {
                this.teamsArrayList=getTeamsList(jsonObject.getJSONArray("teams"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(jsonObject.has("player_api_pur")){
            try {
                this.player_api_pur= jsonObject.getString("player_api_pur");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(jsonObject.has("date_time")){
            try {
                this.date_time= jsonObject.getString("date_time");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public static class Teams{
        public String getTeamName() {
            return TeamName;
        }

        public void setTeamName(String teamName) {
            TeamName = teamName;
        }

        public String getLogo() {
            return logo;
        }

        public void setLogo(String logo) {
            this.logo = logo;
        }

        private String TeamName="";
        private String logo="";
       public Teams(JSONObject jsonObject){
            if(jsonObject.has("name")){
                try {
                    this.TeamName=jsonObject.getString("name");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
           if(jsonObject.has("logo")){
               try {
                   this.logo=jsonObject.getString("logo");
               } catch (JSONException e) {
                   e.printStackTrace();
               }
           }
        }

    }

    public static ArrayList<Teams> getTeamsList(JSONArray jsonArray){
        ArrayList<Teams> teams=new ArrayList<>();
        for(int i=0;i<jsonArray.length();i++){
            try {
                teams.add(new Teams(jsonArray.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return teams;
    }
}
