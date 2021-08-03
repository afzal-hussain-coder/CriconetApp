package com.pb.criconet.models;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class Publisher implements Parcelable {

    private NewUserModel user;
    private PageModel pageModel;

    // Decodes Publisher json into Publisher model object
    public static Publisher fromJson(JSONObject jsonObject) {
        Publisher b = new Publisher();
        // Deserialize json into object fields
        try {
            b.user = NewUserModel.fromJson(jsonObject.optJSONObject("user"));
        } catch (Exception e) {
//            e.printStackTrace();
        }
        try {
            b.pageModel = PageModel.fromJson(jsonObject.optJSONObject("page"));
        } catch (Exception e) {
//            e.printStackTrace();
            return b;
        }
        // Return new object
        return b;
    }

    // Decodes array of Publisher json results into Publisher model objects
    public static ArrayList<Publisher> fromJson(JSONArray jsonArray) {
        JSONObject modelJson;
        ArrayList<Publisher> modeles = new ArrayList<>(jsonArray.length());
        // Process each result in json array, decode and convert to CardModel object
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                modelJson = jsonArray.getJSONObject(i);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
            Publisher model = Publisher.fromJson(modelJson);
            if (model != null) {
                modeles.add(model);
            }
        }

        return modeles;
    }


    public final static Parcelable.Creator<Publisher> CREATOR = new Creator<Publisher>() {
        @SuppressWarnings({"unchecked"})
        public Publisher createFromParcel(Parcel in) {
            return new Publisher(in);
        }

        public Publisher[] newArray(int size) {
            return (new Publisher[size]);
        }

    };

    protected Publisher(Parcel in) {
        this.user = ((NewUserModel) in.readValue((NewUserModel.class.getClassLoader())));
        this.pageModel = ((PageModel) in.readValue((PageModel.class.getClassLoader())));
    }

    public Publisher() {
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(user);
        dest.writeValue(pageModel);
    }

    public int describeContents() {
        return 0;
    }

    public NewUserModel getUser() {
        return user;
    }

    public void setUser(NewUserModel user) {
        this.user = user;
    }

    public PageModel getPageModel() {
        return pageModel;
    }

    public void setPageModel(PageModel pageModel) {
        this.pageModel = pageModel;
    }

    @Override
    public String toString() {
        return "Publisher{" +
                "user=" + user +
                ", page=" + pageModel +
                '}';
    }
}