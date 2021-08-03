package com.pb.criconet.models;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class PageModel implements Parcelable {

    private String page_id;
    private String page_title;
    private String avatar;
    private String cover;
    private String page_description = "";

    private String user_id = "";
    private String sub_user_id = "";
    private String page_name = "";
    private String page_category = "";
    private String website = "";
    private String facebook = "";
    private String google = "";
    private String vk = "";
    private String twitter = "";
    private String linkedin = "";
    private String company = "";
    private String phone = "";
    private String address = "";
    private String call_action_type = "";
    private String call_action_type_url = "";
    private String instagram = "";
    private String youtube = "";
    private String verified = "";
    private String active = "";
    private String registered = "";
    private String boosted = "";
    private String access_other = "";
    private String about = "";
    private String url = "";
    private String name = "";
    private String category = "";
    private Boolean is_page_onwer = false;
    private String username = "";
    private String post_count = "";
    private Boolean is_liked = false;
    private String call_action_type_text = "";


    // Decodes Page json into Page model object
    public static PageModel fromJson(JSONObject jsonObject) {
        PageModel b = new PageModel();
        // Deserialize json into object fields
        try {
            b.page_id = jsonObject.optString("page_id");
            b.page_title = jsonObject.optString("page_title");
            b.avatar = jsonObject.optString("avatar");
            b.cover = jsonObject.optString("cover");
            b.page_description = jsonObject.optString("page_description", "");

            b.user_id = jsonObject.optString("user_id", "");
            b.sub_user_id = jsonObject.optString("sub_user_id", "");
            b.page_name = jsonObject.optString("page_name", "");
            b.page_category = jsonObject.optString("page_category", "");
            b.website = jsonObject.optString("website", "");
            b.facebook = jsonObject.optString("facebook", "");
            b.google = jsonObject.optString("google", "");
            b.vk = jsonObject.optString("vk", "");
            b.twitter = jsonObject.optString("twitter", "");
            b.linkedin = jsonObject.optString("linkedin", "");
            b.company = jsonObject.optString("company", "");
            b.phone = jsonObject.optString("phone", "");
            b.address = jsonObject.optString("address", "");
            b.call_action_type = jsonObject.optString("call_action_type", "");
            b.call_action_type_url = jsonObject.optString("call_action_type_url", "");
            b.instagram = jsonObject.optString("instagram", "");
            b.youtube = jsonObject.optString("youtube", "");
            b.verified = jsonObject.optString("verified", "");
            b.active = jsonObject.optString("active", "");
            b.registered = jsonObject.optString("registered", "");
            b.boosted = jsonObject.optString("boosted", "");
            b.access_other = jsonObject.optString("access_other", "");
            b.about = jsonObject.optString("about", "");
            b.url = jsonObject.optString("url", "");
            b.name = jsonObject.optString("name", "");
            b.category = jsonObject.optString("category", "");
            b.is_page_onwer = jsonObject.optBoolean("is_page_onwer", false);
            b.username = jsonObject.optString("username", "");
            b.post_count = jsonObject.optString("post_count", "");
            b.is_liked = jsonObject.optBoolean("is_liked", false);
            b.call_action_type_text = jsonObject.optString("call_action_type_text", "");

        } catch (Exception e) {
//            e.printStackTrace();
            return b;
        }
        // Return new object
        return b;
    }

    // Decodes array of Page json results into Page model objects
    public static ArrayList<PageModel> fromJson(JSONArray jsonArray) {
        JSONObject modelJson;
        ArrayList<PageModel> modeles = new ArrayList<>(jsonArray.length());
        // Process each result in json array, decode and convert to CardModel object
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                modelJson = jsonArray.getJSONObject(i);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
            PageModel model = PageModel.fromJson(modelJson);
            if (model != null) {
                modeles.add(model);
            }
        }

        return modeles;
    }


    public final static Parcelable.Creator<PageModel> CREATOR = new Creator<PageModel>() {
        @SuppressWarnings({
                "unchecked"
        })
        public PageModel createFromParcel(Parcel in) {
            return new PageModel(in);
        }

        public PageModel[] newArray(int size) {
            return (new PageModel[size]);
        }

    };

    private PageModel(Parcel in) {
        this.page_id = ((String) in.readValue((String.class.getClassLoader())));
        this.page_title = ((String) in.readValue((String.class.getClassLoader())));
        this.avatar = ((String) in.readValue((String.class.getClassLoader())));
        this.cover = ((String) in.readValue((String.class.getClassLoader())));
    }

    public PageModel() {
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(page_id);
        dest.writeValue(page_title);
        dest.writeValue(avatar);
        dest.writeValue(cover);
    }

    public int describeContents() {
        return 0;
    }

    public String getPage_id() {
        return page_id;
    }

    public void setPage_id(String page_id) {
        this.page_id = page_id;
    }

    public String getPage_title() {
        return page_title;
    }

    public void setPage_title(String page_title) {
        this.page_title = page_title;
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

    public String getPage_description() {
        return page_description;
    }

    public void setPage_description(String page_description) {
        this.page_description = page_description;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getSub_user_id() {
        return sub_user_id;
    }

    public void setSub_user_id(String sub_user_id) {
        this.sub_user_id = sub_user_id;
    }

    public String getPage_name() {
        return page_name;
    }

    public void setPage_name(String page_name) {
        this.page_name = page_name;
    }

    public String getPage_category() {
        return page_category;
    }

    public void setPage_category(String page_category) {
        this.page_category = page_category;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getGoogle() {
        return google;
    }

    public void setGoogle(String google) {
        this.google = google;
    }

    public String getVk() {
        return vk;
    }

    public void setVk(String vk) {
        this.vk = vk;
    }

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public String getLinkedin() {
        return linkedin;
    }

    public void setLinkedin(String linkedin) {
        this.linkedin = linkedin;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCall_action_type() {
        return call_action_type;
    }

    public void setCall_action_type(String call_action_type) {
        this.call_action_type = call_action_type;
    }

    public String getCall_action_type_url() {
        return call_action_type_url;
    }

    public void setCall_action_type_url(String call_action_type_url) {
        this.call_action_type_url = call_action_type_url;
    }

    public String getInstagram() {
        return instagram;
    }

    public void setInstagram(String instagram) {
        this.instagram = instagram;
    }

    public String getYoutube() {
        return youtube;
    }

    public void setYoutube(String youtube) {
        this.youtube = youtube;
    }

    public String getVerified() {
        return verified;
    }

    public void setVerified(String verified) {
        this.verified = verified;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getRegistered() {
        return registered;
    }

    public void setRegistered(String registered) {
        this.registered = registered;
    }

    public String getBoosted() {
        return boosted;
    }

    public void setBoosted(String boosted) {
        this.boosted = boosted;
    }

    public String getAccess_other() {
        return access_other;
    }

    public void setAccess_other(String access_other) {
        this.access_other = access_other;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Boolean getIs_page_onwer() {
        return is_page_onwer;
    }

    public void setIs_page_onwer(Boolean is_page_onwer) {
        this.is_page_onwer = is_page_onwer;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPost_count() {
        return post_count;
    }

    public void setPost_count(String post_count) {
        this.post_count = post_count;
    }

    public Boolean getIs_liked() {
        return is_liked;
    }

    public void setIs_liked(Boolean is_liked) {
        this.is_liked = is_liked;
    }

    public String getCall_action_type_text() {
        return call_action_type_text;
    }

    public void setCall_action_type_text(String call_action_type_text) {
        this.call_action_type_text = call_action_type_text;
    }

    @Override
    public String toString() {
        return "PageModel{" +
                "page_id='" + page_id + '\'' +
                ", page_title='" + page_title + '\'' +
                ", avatar='" + avatar + '\'' +
                ", cover='" + cover + '\'' +
                ", page_description='" + page_description + '\'' +
                ", user_id='" + user_id + '\'' +
                ", sub_user_id='" + sub_user_id + '\'' +
                ", page_name='" + page_name + '\'' +
                ", page_category='" + page_category + '\'' +
                ", website='" + website + '\'' +
                ", facebook='" + facebook + '\'' +
                ", google='" + google + '\'' +
                ", vk='" + vk + '\'' +
                ", twitter='" + twitter + '\'' +
                ", linkedin='" + linkedin + '\'' +
                ", company='" + company + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", call_action_type='" + call_action_type + '\'' +
                ", call_action_type_url='" + call_action_type_url + '\'' +
                ", instagram='" + instagram + '\'' +
                ", youtube='" + youtube + '\'' +
                ", verified='" + verified + '\'' +
                ", active='" + active + '\'' +
                ", registered='" + registered + '\'' +
                ", boosted='" + boosted + '\'' +
                ", access_other='" + access_other + '\'' +
                ", about='" + about + '\'' +
                ", url='" + url + '\'' +
                ", name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", is_page_onwer=" + is_page_onwer +
                ", username='" + username + '\'' +
                ", post_count='" + post_count + '\'' +
                ", is_liked=" + is_liked +
                ", call_action_type_text='" + call_action_type_text + '\'' +
                '}';
    }
}