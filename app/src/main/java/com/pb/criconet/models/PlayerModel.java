package com.pb.criconet.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class PlayerModel implements Serializable {
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getBattingStyle() {
        return battingStyle;
    }

    public void setBattingStyle(String battingStyle) {
        this.battingStyle = battingStyle;
    }

    public String getBowlingStyle() {
        return bowlingStyle;
    }

    public void setBowlingStyle(String bowlingStyle) {
        this.bowlingStyle = bowlingStyle;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPlayerImg() {
        return playerImg;
    }

    public void setPlayerImg(String playerImg) {
        this.playerImg = playerImg;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    private String id="";
    private String name ="";
    private String role="";
    private String battingStyle="";
    private String bowlingStyle="";
    private String country="";
    private String playerImg="";
    private String teamName="";

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    private boolean isSelected;


    public PlayerModel(JSONObject jsonObject){
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
        if(jsonObject.has("role")){
            try {
                this.role= jsonObject.getString("role");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(jsonObject.has("battingStyle")){
            try {
                this.battingStyle= jsonObject.getString("battingStyle");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(jsonObject.has("bowlingStyle")){
            try {
                this.bowlingStyle= jsonObject.getString("bowlingStyle");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(jsonObject.has("country")){
            try {
                this.country= jsonObject.getString("country");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(jsonObject.has("playerImg")){
            try {
                this.playerImg= jsonObject.getString("playerImg");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(jsonObject.has("teamName")){
            try {
                this.teamName= jsonObject.getString("teamName");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }
}
