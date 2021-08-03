package com.pb.criconet.models;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class NewUserModel implements Parcelable {

    public String user_id;
    private String first_name;
    private String last_name;
    private String avatar;
    private String cover;

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    private String gender;
    private String name;
    private String username="";
    private String email="";
    private String active="";

    // Decodes NewUserModel json into NewUserModel model object
    public static NewUserModel fromJson(JSONObject jsonObject) {
        NewUserModel b = new NewUserModel();
        // Deserialize json into object fields
        try {
            b.user_id = jsonObject.optString("user_id");
            b.first_name = jsonObject.optString("first_name");
            b.last_name = jsonObject.optString("last_name");
            b.avatar = jsonObject.optString("avatar");
            b.cover = jsonObject.optString("cover");
            b.gender = jsonObject.optString("gender");
            b.name = jsonObject.optString("name");
            b.username = jsonObject.optString("username","");
            b.email = jsonObject.optString("email","");
            b.active = jsonObject.optString("active","");
        } catch (Exception e) {
//            e.printStackTrace();
            return b;
        }
        // Return new object
        return b;
    }

    // Decodes array of NewUserModel json results into NewUserModel model objects
    public static ArrayList<NewUserModel> fromJson(JSONArray jsonArray) {
        JSONObject modelJson;
        ArrayList<NewUserModel> modeles = new ArrayList<>(jsonArray.length());
        // Process each result in json array, decode and convert to CardModel object
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                modelJson = jsonArray.getJSONObject(i);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
            NewUserModel model = NewUserModel.fromJson(modelJson);
            if (model != null) {
                modeles.add(model);
            }
        }

        return modeles;
    }


    public final static Parcelable.Creator<NewUserModel> CREATOR = new Creator<NewUserModel>() {
        @SuppressWarnings({"unchecked"})
        public NewUserModel createFromParcel(Parcel in) {
            return new NewUserModel(in);
        }

        public NewUserModel[] newArray(int size) {
            return (new NewUserModel[size]);
        }

    };

    protected NewUserModel(Parcel in) {
        this.user_id = ((String) in.readValue((String.class.getClassLoader())));
        this.first_name = ((String) in.readValue((String.class.getClassLoader())));
        this.last_name = ((String) in.readValue((String.class.getClassLoader())));
        this.avatar = ((String) in.readValue((String.class.getClassLoader())));
        this.cover = ((String) in.readValue((String.class.getClassLoader())));
        this.gender = ((String) in.readValue((String.class.getClassLoader())));
        this.name = ((String) in.readValue((String.class.getClassLoader())));
        this.username = ((String) in.readValue((String.class.getClassLoader())));
        this.email = ((String) in.readValue((String.class.getClassLoader())));
        this.active = ((String) in.readValue((String.class.getClassLoader())));
    }

    public NewUserModel() {
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(user_id);
        dest.writeValue(first_name);
        dest.writeValue(last_name);
        dest.writeValue(avatar);
        dest.writeValue(cover);
        dest.writeValue(gender);
        dest.writeValue(name);
        dest.writeValue(username);
        dest.writeValue(email);
        dest.writeValue(active);
    }

    public int describeContents() {
        return 0;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "NewUserModel{" +
                "user_id='" + user_id + '\'' +
                ", first_name='" + first_name + '\'' +
                ", last_name='" + last_name + '\'' +
                ", avatar='" + avatar + '\'' +
                ", cover='" + cover + '\'' +
                ", gender='" + gender + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }
}