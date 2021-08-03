package com.pb.criconet.models;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class UserData implements Parcelable {

    private String user_id;
    private String username;
    private String email;
    private String first_name;
    private String last_name;
    private String avatar;
    private String cover;
    private String relationship_id;
    private String address;
    private String working;
    private String working_link;
    private String about;
    private String school;
    private String gender;
    private String birthday;
    private String website;
    private String facebook;
    private String google;
    private String twitter;
    private String linkedin;
    private String youtube;
    private String vk;
    private String instagram;
    private String language;
    private String ip_address;
    private String follow_privacy;
    private String post_privacy;
    private String message_privacy;
    private String confirm_followers;
    private String show_activities_privacy;
    private String birth_privacy;
    private String visit_privacy;
    private String verified;
    private String lastseen;
    private String showlastseen;
    private String status;
    private String active;
    private String admin;
    private String registered;
    private String phone_number;
    private String is_pro;
    private String pro_type;
    private String joined;
    private String timezone;
    private String referrer;
    private String balance;
    private String paypal_email;
    private String notifications_sound;
    private String order_posts_by;
    private String social_login;
    private String device_id;
    private Object company_id;
    private String url;
    private String name;
    private Integer is_following;
    private Integer can_follow;
    private String post_count;
    private String gender_text;
    private String lastseen_time_text;
    private String profile_type;
    private String pincode;
    private String country_name;
    private String state_name;
    private String city_name;
    private String city_id;
    private String state_id;
    private Boolean is_blocked=false;

    public ArrayList<NewUserModel> getNewUserModel() {
        return newUserModel;
    }

    public void setNewUserModel(ArrayList<NewUserModel> newUserModel) {
        this.newUserModel = newUserModel;
    }

    private ArrayList<NewUserModel>newUserModel;

    public ArrayList<NewUserModel> getFollowing_users() {
        return following_users;
    }

    public void setFollowing_users(ArrayList<NewUserModel> following_users) {
        this.following_users = following_users;
    }

    private ArrayList<NewUserModel>following_users;

    public ArrayList<NewUserModel> getFollow_request_users() {
        return follow_request_users;
    }

    public void setFollow_request_users(ArrayList<NewUserModel> follow_request_users) {
        this.follow_request_users = follow_request_users;
    }

    private ArrayList<NewUserModel>follow_request_users;


    public String getCount_follow_request() {
        return count_follow_request;
    }

    public void setCount_follow_request(String count_follow_request) {
        this.count_follow_request = count_follow_request;
    }

    private String count_follow_request;

    public String getCount_followers() {
        return count_followers;
    }

    public void setCount_followers(String count_followers) {
        this.count_followers = count_followers;
    }

    public String getCount_following() {
        return count_following;
    }

    public void setCount_following(String count_following) {
        this.count_following = count_following;
    }

    private String count_followers;
    private String count_following;


    // Decodes UserData json into UserData model object
    public static UserData fromJson(JSONObject jsonObject) {
        UserData b = new UserData();
        // Deserialize json into object fields
        try {

            b.user_id = jsonObject.optString("user_id");
            b.username = jsonObject.optString("username");
            b.email = jsonObject.optString("email");
            b.first_name = jsonObject.optString("first_name");
            b.last_name = jsonObject.optString("last_name");
            b.avatar = jsonObject.optString("avatar");
            b.cover = jsonObject.optString("cover");
            b.relationship_id = jsonObject.optString("relationship_id");
            b.address = jsonObject.optString("address");
            b.working = jsonObject.optString("working");
            b.working_link = jsonObject.optString("working_link");
            b.about = jsonObject.optString("about");
            b.school = jsonObject.optString("school");
            b.gender = jsonObject.optString("gender");
            b.birthday = jsonObject.optString("birthday");
            b.website = jsonObject.optString("website");
            b.facebook = jsonObject.optString("facebook");
            b.google = jsonObject.optString("google");
            b.twitter = jsonObject.optString("twitter");
            b.linkedin = jsonObject.optString("linkedin");
            b.youtube = jsonObject.optString("youtube");
            b.vk = jsonObject.optString("vk");
            b.instagram = jsonObject.optString("instagram");
            b.language = jsonObject.optString("language");
            b.ip_address = jsonObject.optString("ip_address");
            b.follow_privacy = jsonObject.optString("follow_privacy");
            b.post_privacy = jsonObject.optString("post_privacy");
            b.message_privacy = jsonObject.optString("message_privacy");
            b.confirm_followers = jsonObject.optString("confirm_followers");
            b.show_activities_privacy = jsonObject.optString("show_activities_privacy");
            b.birth_privacy = jsonObject.optString("birth_privacy");
            b.visit_privacy = jsonObject.optString("visit_privacy");
            b.verified = jsonObject.optString("verified");
            b.lastseen = jsonObject.optString("lastseen");
            b.showlastseen = jsonObject.optString("showlastseen");
            b.status = jsonObject.optString("status");
            b.active = jsonObject.optString("active");
            b.admin = jsonObject.optString("admin");
            b.registered = jsonObject.optString("registered");
            b.phone_number = jsonObject.optString("phone_number");
            b.is_pro = jsonObject.optString("is_pro");
            b.pro_type = jsonObject.optString("pro_type");
            b.joined = jsonObject.optString("joined");
            b.timezone = jsonObject.optString("timezone");
            b.referrer = jsonObject.optString("referrer");
            b.balance = jsonObject.optString("balance");
            b.paypal_email = jsonObject.optString("paypal_email");
            b.notifications_sound = jsonObject.optString("notifications_sound");
            b.order_posts_by = jsonObject.optString("order_posts_by");
            b.social_login = jsonObject.optString("social_login");
            b.device_id = jsonObject.optString("device_id");
            b.company_id = jsonObject.optString("company_id","");
            b.url = jsonObject.optString("url");
            b.name = jsonObject.optString("name");
            b.is_following = jsonObject.optInt("is_following");
            b.can_follow = jsonObject.optInt("can_follow");
            b.post_count = jsonObject.optString("post_count");
            b.gender_text = jsonObject.optString("gender_text");
            b.lastseen_time_text = jsonObject.optString("lastseen_time_text");
            b.profile_type = jsonObject.optString("profile_type");
            b.pincode = jsonObject.optString("pincode");
            b.country_name = jsonObject.optString("country_name");
            b.state_name = jsonObject.optString("state_name");
            b.city_name = jsonObject.optString("city_name");
            b.state_id = jsonObject.optString("state_id");
            b.city_id = jsonObject.optString("city_id");
            b.is_blocked = jsonObject.optBoolean("is_blocked");
            b.count_followers = jsonObject.optString("count_followers");
            b.count_follow_request = jsonObject.optString("count_follow_request");
            b.count_following = jsonObject.optString("count_following");
            b.newUserModel = NewUserModel.fromJson(jsonObject.optJSONArray("followers_users"));
            b.following_users = NewUserModel.fromJson(jsonObject.optJSONArray("following_users"));
            b.follow_request_users = NewUserModel.fromJson(jsonObject.optJSONArray("follow_request_users"));


        } catch (Exception e) {
            e.printStackTrace();
            return b;
        }
        // Return new object
        return b;
    }

    // Decodes array of UserData json results into UserData model objects
    public static ArrayList<UserData> fromJson(JSONArray jsonArray) {
        JSONObject modelJson;
        ArrayList<UserData> modeles = new ArrayList<>(jsonArray.length());
        // Process each result in json array, decode and convert to CardModel object
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                modelJson = jsonArray.getJSONObject(i);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
            UserData model = UserData.fromJson(modelJson);
            if (model != null) {
                modeles.add(model);
            }
        }

        return modeles;
    }

    public final static Parcelable.Creator<UserData> CREATOR = new Creator<UserData>() {
        @SuppressWarnings({"unchecked"})
        public UserData createFromParcel(Parcel in) {
            return new UserData(in);
        }

        public UserData[] newArray(int size) {
            return (new UserData[size]);
        }
    };


    private UserData(Parcel in) {
        this.user_id = ((String) in.readValue((String.class.getClassLoader())));
        this.username = ((String) in.readValue((String.class.getClassLoader())));
        this.email = ((String) in.readValue((String.class.getClassLoader())));
        this.first_name = ((String) in.readValue((String.class.getClassLoader())));
        this.last_name = ((String) in.readValue((String.class.getClassLoader())));
        this.avatar = ((String) in.readValue((String.class.getClassLoader())));
        this.cover = ((String) in.readValue((String.class.getClassLoader())));
        this.relationship_id = ((String) in.readValue((String.class.getClassLoader())));
        this.address = ((String) in.readValue((String.class.getClassLoader())));
        this.working = ((String) in.readValue((String.class.getClassLoader())));
        this.working_link = ((String) in.readValue((String.class.getClassLoader())));
        this.about = ((String) in.readValue((String.class.getClassLoader())));
        this.school = ((String) in.readValue((String.class.getClassLoader())));
        this.gender = ((String) in.readValue((String.class.getClassLoader())));
        this.birthday = ((String) in.readValue((String.class.getClassLoader())));
        this.website = ((String) in.readValue((String.class.getClassLoader())));
        this.facebook = ((String) in.readValue((String.class.getClassLoader())));
        this.google = ((String) in.readValue((String.class.getClassLoader())));
        this.twitter = ((String) in.readValue((String.class.getClassLoader())));
        this.linkedin = ((String) in.readValue((String.class.getClassLoader())));
        this.youtube = ((String) in.readValue((String.class.getClassLoader())));
        this.vk = ((String) in.readValue((String.class.getClassLoader())));
        this.instagram = ((String) in.readValue((String.class.getClassLoader())));
        this.language = ((String) in.readValue((String.class.getClassLoader())));
        this.ip_address = ((String) in.readValue((String.class.getClassLoader())));
        this.follow_privacy = ((String) in.readValue((String.class.getClassLoader())));
        this.post_privacy = ((String) in.readValue((String.class.getClassLoader())));
        this.message_privacy = ((String) in.readValue((String.class.getClassLoader())));
        this.confirm_followers = ((String) in.readValue((String.class.getClassLoader())));
        this.show_activities_privacy = ((String) in.readValue((String.class.getClassLoader())));
        this.birth_privacy = ((String) in.readValue((String.class.getClassLoader())));
        this.visit_privacy = ((String) in.readValue((String.class.getClassLoader())));
        this.verified = ((String) in.readValue((String.class.getClassLoader())));
        this.lastseen = ((String) in.readValue((String.class.getClassLoader())));
        this.showlastseen = ((String) in.readValue((String.class.getClassLoader())));
        this.status = ((String) in.readValue((String.class.getClassLoader())));
        this.active = ((String) in.readValue((String.class.getClassLoader())));
        this.admin = ((String) in.readValue((String.class.getClassLoader())));
        this.registered = ((String) in.readValue((String.class.getClassLoader())));
        this.phone_number = ((String) in.readValue((String.class.getClassLoader())));
        this.is_pro = ((String) in.readValue((String.class.getClassLoader())));
        this.pro_type = ((String) in.readValue((String.class.getClassLoader())));
        this.joined = ((String) in.readValue((String.class.getClassLoader())));
        this.timezone = ((String) in.readValue((String.class.getClassLoader())));
        this.referrer = ((String) in.readValue((String.class.getClassLoader())));
        this.balance = ((String) in.readValue((String.class.getClassLoader())));
        this.paypal_email = ((String) in.readValue((String.class.getClassLoader())));
        this.notifications_sound = ((String) in.readValue((String.class.getClassLoader())));
        this.order_posts_by = ((String) in.readValue((String.class.getClassLoader())));
        this.social_login = ((String) in.readValue((String.class.getClassLoader())));
        this.device_id = ((String) in.readValue((String.class.getClassLoader())));
        this.company_id = ((Object) in.readValue((Object.class.getClassLoader())));
        this.url = ((String) in.readValue((String.class.getClassLoader())));
        this.name = ((String) in.readValue((String.class.getClassLoader())));
        this.is_following = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.can_follow = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.post_count = ((String) in.readValue((String.class.getClassLoader())));
        this.gender_text = ((String) in.readValue((String.class.getClassLoader())));
        this.lastseen_time_text = ((String) in.readValue((String.class.getClassLoader())));
        this.profile_type = ((String) in.readValue((String.class.getClassLoader())));
        this.pincode = ((String) in.readValue((String.class.getClassLoader())));
        this.country_name = ((String) in.readValue((String.class.getClassLoader())));
        this.state_name = ((String) in.readValue((String.class.getClassLoader())));
        this.city_name = ((String) in.readValue((String.class.getClassLoader())));
        this.city_id = ((String) in.readValue((String.class.getClassLoader())));
        this.state_id = ((String) in.readValue((String.class.getClassLoader())));
        this.is_blocked = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
        this.count_followers = ((String) in.readValue((String.class.getClassLoader())));
        this.count_follow_request = ((String) in.readValue((String.class.getClassLoader())));
        this.count_following = ((String) in.readValue((String.class.getClassLoader())));
        this.newUserModel = (ArrayList<NewUserModel>) in.readValue((String.class.getClassLoader()));
        this.following_users = (ArrayList<NewUserModel>) in.readValue((String.class.getClassLoader()));
        this.follow_request_users = (ArrayList<NewUserModel>) in.readValue((String.class.getClassLoader()));

    }

    public UserData() {
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(user_id);
        dest.writeValue(username);
        dest.writeValue(email);
        dest.writeValue(first_name);
        dest.writeValue(last_name);
        dest.writeValue(avatar);
        dest.writeValue(cover);
        dest.writeValue(relationship_id);
        dest.writeValue(address);
        dest.writeValue(working);
        dest.writeValue(working_link);
        dest.writeValue(about);
        dest.writeValue(school);
        dest.writeValue(gender);
        dest.writeValue(birthday);
        dest.writeValue(website);
        dest.writeValue(facebook);
        dest.writeValue(google);
        dest.writeValue(twitter);
        dest.writeValue(linkedin);
        dest.writeValue(youtube);
        dest.writeValue(vk);
        dest.writeValue(instagram);
        dest.writeValue(language);
        dest.writeValue(ip_address);
        dest.writeValue(follow_privacy);
        dest.writeValue(post_privacy);
        dest.writeValue(message_privacy);
        dest.writeValue(confirm_followers);
        dest.writeValue(show_activities_privacy);
        dest.writeValue(birth_privacy);
        dest.writeValue(visit_privacy);
        dest.writeValue(verified);
        dest.writeValue(lastseen);
        dest.writeValue(showlastseen);
        dest.writeValue(status);
        dest.writeValue(active);
        dest.writeValue(admin);
        dest.writeValue(registered);
        dest.writeValue(phone_number);
        dest.writeValue(is_pro);
        dest.writeValue(pro_type);
        dest.writeValue(joined);
        dest.writeValue(timezone);
        dest.writeValue(referrer);
        dest.writeValue(balance);
        dest.writeValue(paypal_email);
        dest.writeValue(notifications_sound);
        dest.writeValue(order_posts_by);
        dest.writeValue(social_login);
        dest.writeValue(device_id);
        dest.writeValue(company_id);
        dest.writeValue(url);
        dest.writeValue(name);
        dest.writeValue(is_following);
        dest.writeValue(can_follow);
        dest.writeValue(post_count);
        dest.writeValue(gender_text);
        dest.writeValue(lastseen_time_text);
        dest.writeValue(profile_type);
        dest.writeValue(pincode);
        dest.writeValue(country_name);
        dest.writeValue(state_name);
        dest.writeValue(city_name);
        dest.writeValue(city_id);
        dest.writeValue(state_id);
        dest.writeValue(is_blocked);
        dest.writeValue(count_followers);
        dest.writeValue(count_follow_request);
        dest.writeValue(count_following);
        dest.writeValue(newUserModel);
        dest.writeValue(follow_request_users);
        dest.writeValue(following_users);
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

    public String getRelationship_id() {
        return relationship_id;
    }

    public void setRelationship_id(String relationship_id) {
        this.relationship_id = relationship_id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getWorking() {
        return working;
    }

    public void setWorking(String working) {
        this.working = working;
    }

    public String getWorking_link() {
        return working_link;
    }

    public void setWorking_link(String working_link) {
        this.working_link = working_link;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
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

    public String getYoutube() {
        return youtube;
    }

    public void setYoutube(String youtube) {
        this.youtube = youtube;
    }

    public String getVk() {
        return vk;
    }

    public void setVk(String vk) {
        this.vk = vk;
    }

    public String getInstagram() {
        return instagram;
    }

    public void setInstagram(String instagram) {
        this.instagram = instagram;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getIp_address() {
        return ip_address;
    }

    public void setIp_address(String ip_address) {
        this.ip_address = ip_address;
    }

    public String getFollow_privacy() {
        return follow_privacy;
    }

    public void setFollow_privacy(String follow_privacy) {
        this.follow_privacy = follow_privacy;
    }

    public String getPost_privacy() {
        return post_privacy;
    }

    public void setPost_privacy(String post_privacy) {
        this.post_privacy = post_privacy;
    }

    public String getMessage_privacy() {
        return message_privacy;
    }

    public void setMessage_privacy(String message_privacy) {
        this.message_privacy = message_privacy;
    }

    public String getConfirm_followers() {
        return confirm_followers;
    }

    public void setConfirm_followers(String confirm_followers) {
        this.confirm_followers = confirm_followers;
    }

    public String getShow_activities_privacy() {
        return show_activities_privacy;
    }

    public void setShow_activities_privacy(String show_activities_privacy) {
        this.show_activities_privacy = show_activities_privacy;
    }

    public String getBirth_privacy() {
        return birth_privacy;
    }

    public void setBirth_privacy(String birth_privacy) {
        this.birth_privacy = birth_privacy;
    }

    public String getVisit_privacy() {
        return visit_privacy;
    }

    public void setVisit_privacy(String visit_privacy) {
        this.visit_privacy = visit_privacy;
    }

    public String getVerified() {
        return verified;
    }

    public void setVerified(String verified) {
        this.verified = verified;
    }

    public String getLastseen() {
        return lastseen;
    }

    public void setLastseen(String lastseen) {
        this.lastseen = lastseen;
    }

    public String getShowlastseen() {
        return showlastseen;
    }

    public void setShowlastseen(String showlastseen) {
        this.showlastseen = showlastseen;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public String getRegistered() {
        return registered;
    }

    public void setRegistered(String registered) {
        this.registered = registered;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getIs_pro() {
        return is_pro;
    }

    public void setIs_pro(String is_pro) {
        this.is_pro = is_pro;
    }

    public String getPro_type() {
        return pro_type;
    }

    public void setPro_type(String pro_type) {
        this.pro_type = pro_type;
    }

    public String getJoined() {
        return joined;
    }

    public void setJoined(String joined) {
        this.joined = joined;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public String getReferrer() {
        return referrer;
    }

    public void setReferrer(String referrer) {
        this.referrer = referrer;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getPaypal_email() {
        return paypal_email;
    }

    public void setPaypal_email(String paypal_email) {
        this.paypal_email = paypal_email;
    }

    public String getNotifications_sound() {
        return notifications_sound;
    }

    public void setNotifications_sound(String notifications_sound) {
        this.notifications_sound = notifications_sound;
    }

    public String getOrder_posts_by() {
        return order_posts_by;
    }

    public void setOrder_posts_by(String order_posts_by) {
        this.order_posts_by = order_posts_by;
    }

    public String getSocial_login() {
        return social_login;
    }

    public void setSocial_login(String social_login) {
        this.social_login = social_login;
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public Object getCompany_id() {
        return company_id;
    }

    public void setCompany_id(Object company_id) {
        this.company_id = company_id;
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

    public Integer getIs_following() {
        return is_following;
    }

    public void setIs_following(Integer is_following) {
        this.is_following = is_following;
    }

    public Integer getCan_follow() {
        return can_follow;
    }

    public void setCan_follow(Integer can_follow) {
        this.can_follow = can_follow;
    }

    public String getPost_count() {
        return post_count;
    }

    public void setPost_count(String post_count) {
        this.post_count = post_count;
    }

    public String getGender_text() {
        return gender_text;
    }

    public void setGender_text(String gender_text) {
        this.gender_text = gender_text;
    }

    public String getLastseen_time_text() {
        return lastseen_time_text;
    }

    public void setLastseen_time_text(String lastseen_time_text) {
        this.lastseen_time_text = lastseen_time_text;
    }

    public String getProfile_type() {
        return profile_type;
    }

    public void setProfile_type(String profile_type) {
        this.profile_type = profile_type;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getCountry_name() {
        return country_name;
    }

    public void setCountry_name(String country_name) {
        this.country_name = country_name;
    }

    public String getState_name() {
        return state_name;
    }

    public void setState_name(String state_name) {
        this.state_name = state_name;
    }

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public Boolean getIs_blocked() {
        return is_blocked;
    }

    public void setIs_blocked(Boolean is_blocked) {
        this.is_blocked = is_blocked;
    }

    public String getCity_id() {
        return city_id;
    }

    public void setCity_id(String city_id) {
        this.city_id = city_id;
    }

    public String getState_id() {
        return state_id;
    }

    public void setState_id(String state_id) {
        this.state_id = state_id;
    }
}