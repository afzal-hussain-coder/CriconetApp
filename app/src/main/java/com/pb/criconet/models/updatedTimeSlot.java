package com.pb.criconet.models;

import org.json.JSONException;
import org.json.JSONObject;

public class updatedTimeSlot {
    public String getSlotId() {
        return slotId;
    }

    public void setSlotId(String slotId) {
        this.slotId = slotId;
    }

    public String getIs_selected() {
        return is_selected;
    }

    public void setIs_selected(String is_selected) {
        this.is_selected = is_selected;
    }

    private String slotId="";
    private String is_selected;

    public updatedTimeSlot(JSONObject jsonObject){
        if(jsonObject.has("id")){
            try {
                this.slotId = jsonObject.getString("id");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
