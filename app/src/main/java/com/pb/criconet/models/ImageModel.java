package com.pb.criconet.models;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ImageModel implements Parcelable {

    private String id;
    private String image;
    private String post_id;
    private String image_org;

    // Decodes ImageModel json into ImageModel model object
    public static ImageModel fromJson(JSONObject jsonObject) {
        ImageModel b = new ImageModel();
        // Deserialize json into object fields
        try {
            b.id = jsonObject.optString("id");
            b.image = jsonObject.optString("image");
            b.post_id = jsonObject.optString("post_id");
            b.image_org = jsonObject.optString("image_org");
        } catch (Exception e) {
            e.printStackTrace();
            return b;
        }
        // Return new object
        return b;
    }

    // Decodes array of ImageModel json results into ImageModel model objects
    public static ArrayList<ImageModel> fromJson(JSONArray jsonArray) {
        JSONObject modelJson;
        ArrayList<ImageModel> modeles = new ArrayList<>(jsonArray.length());
        // Process each result in json array, decode and convert to CardModel object
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                modelJson = jsonArray.getJSONObject(i);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
            ImageModel model = ImageModel.fromJson(modelJson);
            if (model != null) {
                modeles.add(model);
            }
        }

        return modeles;
    }




    public final static Parcelable.Creator<ImageModel> CREATOR = new Creator<ImageModel>() {
        @SuppressWarnings({"unchecked"})
        public ImageModel createFromParcel(Parcel in) {
            return new ImageModel(in);
        }

        public ImageModel[] newArray(int size) {
            return (new ImageModel[size]);
        }

    };

    protected ImageModel(Parcel in) {
        this.id = ((String) in.readValue((String.class.getClassLoader())));
        this.image = ((String) in.readValue((String.class.getClassLoader())));
        this.post_id = ((String) in.readValue((String.class.getClassLoader())));
        this.image_org = ((String) in.readValue((String.class.getClassLoader())));
    }

    public ImageModel() {
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(image);
        dest.writeValue(post_id);
        dest.writeValue(image_org);
    }

    public int describeContents() {
        return 0;
    }

     public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    public String getImage_org() {
        return image_org;
    }

    public void setImage_org(String image_org) {
        this.image_org = image_org;
    }

    @Override
    public String toString() {
        return "ImageModel{" +
                "id='" + id + '\'' +
                ", image='" + image + '\'' +
                ", post_id='" + post_id + '\'' +
                ", image_org='" + image_org + '\'' +
                '}';
    }
    
}
