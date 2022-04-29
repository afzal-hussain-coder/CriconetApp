package com.pb.criconet.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

import io.agora.rtc.ss.gles.SrcConnector;

public class AcademyListModel implements Serializable {
    private String id = "";
    private String created_by = "";
    private String name = "";
    private String email = "";
    private String address = "";
    private String fee = "";
    private String about = "";
    private String coach_name = "";
    private String achievement = "";
    private String short_desc = "";
    private String contact_person_name = "";
    private String contact_person_phone = "";
    private String review = "";
    private String training_type = "";

    public ArrayList<AcademyCoaches> getAcademyCoachesArrayList() {
        return academyCoachesArrayList;
    }

    public void setAcademyCoachesArrayList(ArrayList<AcademyCoaches> academyCoachesArrayList) {
        this.academyCoachesArrayList = academyCoachesArrayList;
    }

    ArrayList<AcademyCoaches> academyCoachesArrayList=null;


    public int getTaxes_value() {
        return taxes_value;
    }

    public void setTaxes_value(int taxes_value) {
        this.taxes_value = taxes_value;
    }

    public int getTaxes_amount() {
        return taxes_amount;
    }

    public void setTaxes_amount(int taxes_amount) {
        this.taxes_amount = taxes_amount;
    }

    public int getPayment_amount() {
        return payment_amount;
    }

    public void setPayment_amount(int payment_amount) {
        this.payment_amount = payment_amount;
    }

    private int taxes_value;
    private int taxes_amount;
    private int payment_amount;


    public String getCat_title() {
        return cat_title;
    }

    public void setCat_title(String cat_title) {
        this.cat_title = cat_title;
    }

    private String cat_title = "";


    private String training_session_date = "";
    private String social = "";
    private String latitude = "";
    private String longitude = "";
    private String status = "";
    private String created = "";
    private String banner_image = "";
    private String logo = "";
    private String video = "";
    private String rating = "";

    public String getPackage_valid_for() {
        return package_valid_for;
    }

    public void setPackage_valid_for(String package_valid_for) {
        this.package_valid_for = package_valid_for;
    }

    private String package_valid_for = "";

    public String getFacebook() {
        return Facebook;
    }

    public void setFacebook(String facebook) {
        Facebook = facebook;
    }

    public String getTwitter() {
        return Twitter;
    }

    public void setTwitter(String twitter) {
        Twitter = twitter;
    }

    public String getYoutube() {
        return Youtube;
    }

    public void setYoutube(String youtube) {
        Youtube = youtube;
    }

    public String getInstagram() {
        return Instagram;
    }

    public void setInstagram(String instagram) {
        Instagram = instagram;
    }

    public String getLinkedIn() {
        return LinkedIn;
    }

    public void setLinkedIn(String linkedIn) {
        LinkedIn = linkedIn;
    }


    public ArrayList<String> getImageArryList() {
        return imageArryList;
    }

    public void setImageArryList(ArrayList<String> imageArryList) {
        this.imageArryList = imageArryList;
    }

    private ArrayList<String> imageArryList = null;

    public ArrayList<String> getVideoList() {
        return videoList;
    }

    public void setVideoList(ArrayList<String> videoList) {
        this.videoList = videoList;
    }

    private ArrayList<String> videoList = null;

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    private String lang = "";
    private String Facebook = "";
    private String Twitter = "";
    private String Youtube = "";
    private String Instagram = "";
    private String LinkedIn = "";

    public CriconetSupport getCriconetSupport() {
        return criconetSupport;
    }

    public void setCriconetSupport(CriconetSupport criconetSupport) {
        this.criconetSupport = criconetSupport;
    }

    private CriconetSupport criconetSupport;

    public ArrayList<AcademySlot> getAcademySlots() {
        return academySlots;
    }

    public void setAcademySlots(ArrayList<AcademySlot> academySlots) {
        this.academySlots = academySlots;
    }

    private ArrayList<AcademySlot> academySlots = null;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getCoach_name() {
        return coach_name;
    }

    public void setCoach_name(String coach_name) {
        this.coach_name = coach_name;
    }

    public String getAchievement() {
        return achievement;
    }

    public void setAchievement(String achievement) {
        this.achievement = achievement;
    }

    public String getShort_desc() {
        return short_desc;
    }

    public void setShort_desc(String short_desc) {
        this.short_desc = short_desc;
    }

    public String getContact_person_name() {
        return contact_person_name;
    }

    public void setContact_person_name(String contact_person_name) {
        this.contact_person_name = contact_person_name;
    }

    public String getContact_person_phone() {
        return contact_person_phone;
    }

    public void setContact_person_phone(String contact_person_phone) {
        this.contact_person_phone = contact_person_phone;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getTraining_type() {
        return training_type;
    }

    public void setTraining_type(String training_type) {
        this.training_type = training_type;
    }


    public String getTraining_session_date() {
        return training_session_date;
    }

    public void setTraining_session_date(String training_session_date) {
        this.training_session_date = training_session_date;
    }

    public String getSocial() {
        return social;
    }

    public void setSocial(String social) {
        this.social = social;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getBanner_image() {
        return banner_image;
    }

    public void setBanner_image(String banner_image) {
        this.banner_image = banner_image;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public AcademyListModel(JSONObject jsonObject) {
        if (jsonObject.has("id")) {
            try {
                this.id = jsonObject.getString("id");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (jsonObject.has("created_by")) {
            try {
                this.created_by = jsonObject.getString("created_by");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (jsonObject.has("name")) {
            try {
                this.name = jsonObject.getString("name");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (jsonObject.has("email")) {
            try {
                this.email = jsonObject.getString("email");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (jsonObject.has("address")) {
            try {
                this.address = jsonObject.getString("address");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (jsonObject.has("coach_name")) {
            try {
                this.coach_name = jsonObject.getString("coach_name");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (jsonObject.has("banner_image")) {
            try {
                this.banner_image = jsonObject.getString("banner_image");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (jsonObject.has("logo")) {
            try {
                this.logo = jsonObject.getString("logo");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (jsonObject.has("video")) {
            try {
                this.video = jsonObject.getString("video");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (jsonObject.has("lang")) {
            try {
                this.lang = jsonObject.getString("lang");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (jsonObject.has("latitude")) {
            try {
                this.latitude = jsonObject.getString("latitude");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (jsonObject.has("longitude")) {
            try {
                this.longitude = jsonObject.getString("longitude");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (jsonObject.has("status")) {
            try {
                this.status = jsonObject.getString("status");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (jsonObject.has("created_by")) {
            try {
                this.created_by = jsonObject.getString("created_by");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (jsonObject.has("achievement")) {
            try {
                this.achievement = jsonObject.getString("achievement");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (jsonObject.has("social")) {
            try {
                this.social = jsonObject.getString("social");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (jsonObject.has("training_session_date")) {
            try {
                this.training_session_date = jsonObject.getString("training_session_date");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (jsonObject.has("training_type")) {
            try {
                this.training_type = jsonObject.getString("training_type");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (jsonObject.has("rating")) {
            try {
                this.rating = jsonObject.getString("rating");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (jsonObject.has("review")) {
            try {
                this.review = jsonObject.getString("review");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (jsonObject.has("contact_person_phone")) {
            try {
                this.contact_person_phone = jsonObject.getString("contact_person_phone");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (jsonObject.has("contact_person_name")) {
            try {
                this.contact_person_name = jsonObject.getString("contact_person_name");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (jsonObject.has("short_desc")) {
            try {
                this.short_desc = jsonObject.getString("short_desc");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (jsonObject.has("fee")) {
            try {
                this.fee = jsonObject.getString("fee");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (jsonObject.has("about")) {
            try {
                this.about = jsonObject.getString("about");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (jsonObject.has("cat_title")) {
            try {
                this.cat_title = jsonObject.getString("cat_title");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (jsonObject.has("Facebook")) {
            try {
                this.Facebook = jsonObject.getString("Facebook");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (jsonObject.has("Twitter")) {
            try {
                this.Twitter = jsonObject.getString("Twitter");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (jsonObject.has("Youtube")) {
            try {
                this.Youtube = jsonObject.getString("Youtube");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (jsonObject.has("LinkedIn")) {
            try {
                this.LinkedIn = jsonObject.getString("LinkedIn");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (jsonObject.has("Instagram")) {
            try {
                this.Instagram = jsonObject.getString("Instagram");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (jsonObject.has("academy_slots")) {
            try {
                this.academySlots = getAcademySlot(jsonObject.getJSONArray("academy_slots"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (jsonObject.has("academy_image")) {
            try {
                this.imageArryList = getImageGallery(jsonObject.getJSONArray("academy_image"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (jsonObject.has("academy_video")) {
            try {
                this.videoList = getVideoGallery(jsonObject.getJSONArray("academy_video"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (jsonObject.has("coaches")) {
            try {
                this.academyCoachesArrayList = getAcademyCoachesList(jsonObject.getJSONArray("coaches"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (jsonObject.has("taxes_amount")) {
            try {
                this.taxes_amount = jsonObject.getInt("taxes_amount");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (jsonObject.has("payment_amount")) {
            try {
                this.payment_amount = jsonObject.getInt("payment_amount");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (jsonObject.has("taxes_value")) {
            try {
                this.taxes_value = jsonObject.getInt("taxes_value");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (jsonObject.has("package_valid_for")) {
            try {
                this.package_valid_for = jsonObject.getString("package_valid_for");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (jsonObject.has("criconet_support_info")) {
            try {
                this.criconetSupport = getSupport(jsonObject.getJSONObject("criconet_support_info"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    public class AcademySlot implements Serializable {
        public String getSlot_id() {
            return slot_id;
        }

        public void setSlot_id(String slot_id) {
            this.slot_id = slot_id;
        }

        public String getSlot_time() {
            return slot_time;
        }

        public void setSlot_time(String slot_time) {
            this.slot_time = slot_time;
        }

        private String slot_id = "";
        private String slot_time = "";


        AcademySlot(JSONObject jsonObject) {
            if (jsonObject.has("slot_id")) {
                try {
                    this.slot_id = jsonObject.getString("slot_id");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (jsonObject.has("slot_time")) {
                try {
                    this.slot_time = jsonObject.getString("slot_time");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private ArrayList<AcademySlot> getAcademySlot(JSONArray jsonArray) {
        ArrayList<AcademySlot> academySlots = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                academySlots.add(new AcademySlot(jsonArray.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return academySlots;
    }

    private ArrayList<String> getImageGallery(JSONArray jsonArray) {
        ArrayList<String> academySlots = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                academySlots.add(jsonArray.getString(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return academySlots;
    }

    private ArrayList<String> getVideoGallery(JSONArray jsonArray) {
        ArrayList<String> academySlots = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                academySlots.add(jsonArray.getString(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return academySlots;
    }

    private CriconetSupport getSupport(JSONObject jsonObject) {
        return new CriconetSupport(jsonObject);

    }

    private ArrayList<AcademyCoaches> getAcademyCoachesList(JSONArray jsonArray){
        ArrayList<AcademyCoaches> academySlots = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                academySlots.add(new AcademyCoaches(jsonArray.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return academySlots;
    }

    public class CriconetSupport implements Serializable {

        CriconetSupport(JSONObject jsonObject) {
            if (jsonObject.has("txtmsg2")) {
                try {
                    this.txtmsg2 = jsonObject.getString("txtmsg2");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (jsonObject.has("txtmsg1")) {
                try {
                    this.txtmsg1 = jsonObject.getString("txtmsg1");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (jsonObject.has("email")) {
                try {
                    this.email = jsonObject.getString("email");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (jsonObject.has("contact_number")) {
                try {
                    this.contact_number = jsonObject.getString("contact_number");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getContact_number() {
            return contact_number;
        }

        public void setContact_number(String contact_number) {
            this.contact_number = contact_number;
        }

        public String getTxtmsg1() {
            return txtmsg1;
        }

        public void setTxtmsg1(String txtmsg1) {
            this.txtmsg1 = txtmsg1;
        }

        public String getTxtmsg2() {
            return txtmsg2;
        }

        public void setTxtmsg2(String txtmsg2) {
            this.txtmsg2 = txtmsg2;
        }

        private String email;
        private String contact_number;

        private String txtmsg1;

        private String txtmsg2;

    }

    public class AcademyCoaches implements Serializable {
        public String getProfile_title() {
            return profile_title;
        }

        public void setProfile_title(String profile_title) {
            this.profile_title = profile_title;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getCoach_id() {
            return coach_id;
        }

        public void setCoach_id(String coach_id) {
            this.coach_id = coach_id;
        }

        public String getExp_years() {
            return exp_years;
        }

        public void setExp_years(String exp_years) {
            this.exp_years = exp_years;
        }

        public String getExp_months() {
            return exp_months;
        }

        public void setExp_months(String exp_months) {
            this.exp_months = exp_months;
        }

        public String getAchievement() {
            return achievement;
        }

        public void setAchievement(String achievement) {
            this.achievement = achievement;
        }

        public String getAbout_coach_profile() {
            return about_coach_profile;
        }

        public void setAbout_coach_profile(String about_coach_profile) {
            this.about_coach_profile = about_coach_profile;
        }

        public String getCharge_amount() {
            return charge_amount;
        }

        public void setCharge_amount(String charge_amount) {
            this.charge_amount = charge_amount;
        }

        public String getCuurency_code() {
            return cuurency_code;
        }

        public void setCuurency_code(String cuurency_code) {
            this.cuurency_code = cuurency_code;
        }

        public String getSkills_you_learn() {
            return skills_you_learn;
        }

        public void setSkills_you_learn(String skills_you_learn) {
            this.skills_you_learn = skills_you_learn;
        }

        public String getWhat_you_teach() {
            return what_you_teach;
        }

        public void setWhat_you_teach(String what_you_teach) {
            this.what_you_teach = what_you_teach;
        }

        public String getBooking_close_time() {
            return booking_close_time;
        }

        public void setBooking_close_time(String booking_close_time) {
            this.booking_close_time = booking_close_time;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getLang() {
            return lang;
        }

        public void setLang(String lang) {
            this.lang = lang;
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

        public String getMid_name() {
            return mid_name;
        }

        public void setMid_name(String mid_name) {
            this.mid_name = mid_name;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getAvatar() {
            return avatar;
        }

        public String getCover() {
            return cover;
        }

        public void setCover(String cover) {
            this.cover = cover;
        }

        private String cover="";

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getCat_title() {
            return cat_title;
        }

        public void setCat_title(String cat_title) {
            this.cat_title = cat_title;
        }

        public String getSelected_cat_id() {
            return selected_cat_id;
        }

        public void setSelected_cat_id(String selected_cat_id) {
            this.selected_cat_id = selected_cat_id;
        }

        public String getProfile_link() {
            return profile_link;
        }

        public void setProfile_link(String profile_link) {
            this.profile_link = profile_link;
        }

        public String getIs_offer() {
            return is_offer;
        }

        public void setIs_offer(String is_offer) {
            this.is_offer = is_offer;
        }

        public String getExps() {
            return exps;
        }

        public void setExps(String exps) {
            this.exps = exps;
        }

        private String profile_title = "";
        private String user_id = "";
        private String coach_id = "";
        private String exp_years = "";
        private String exp_months = "";
        private String achievement = "";
        private String about_coach_profile = "";
        private String charge_amount = "";
        private String cuurency_code = "";
        private String skills_you_learn = "";
        private String what_you_teach = "";
        private String booking_close_time = "";
        private String name = "";
        private String lang = "";
        private String first_name = "";
        private String last_name = "";
        private String mid_name = "";
        private String username = "";
        private String avatar = "";
        private String cat_title = "";
        private String selected_cat_id = "";
        private String profile_link = "";
        private String is_offer = "";
        private String exps = "";

        public int getRating() {
            return rating;
        }

        public void setRating(int rating) {
            this.rating = rating;
        }

        private int rating;

        public ArrayList<certificate> getCertificatesList() {
            return certificatesList;
        }

        public void setCertificatesList(ArrayList<certificate> certificatesList) {
            this.certificatesList = certificatesList;
        }

        ArrayList<certificate> certificatesList=null;

        public Price getPrice() {
            return price;
        }

        public void setPrice(Price price) {
            this.price = price;
        }

        Price price=null;


        AcademyCoaches(JSONObject jsonObject) {
            if(jsonObject.has("exps")){
                try {
                    this.exps= jsonObject.getString("exps");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if(jsonObject.has("is_offer")){
                try {
                    this.is_offer= jsonObject.getString("is_offer");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if(jsonObject.has("profile_link")){
                try {
                    this.profile_link= jsonObject.getString("profile_link");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if(jsonObject.has("selected_cat_id")){
                try {
                    this.selected_cat_id= jsonObject.getString("selected_cat_id");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if(jsonObject.has("cat_title")){
                try {
                    this.cat_title= jsonObject.getString("cat_title");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if(jsonObject.has("avatar")){
                try {
                    this.avatar= jsonObject.getString("avatar");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if(jsonObject.has("cover")){
                try {
                    this.cover= jsonObject.getString("cover");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if(jsonObject.has("rating")){
                try {
                    this.rating= jsonObject.getInt("rating");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            if(jsonObject.has("username")){
                try {
                    this.username= jsonObject.getString("username");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if(jsonObject.has("mid_name")){
                try {
                    this.mid_name= jsonObject.getString("mid_name");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if(jsonObject.has("last_name")){
                try {
                    this.last_name= jsonObject.getString("last_name");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if(jsonObject.has("first_name")){
                try {
                    this.first_name= jsonObject.getString("first_name");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if(jsonObject.has("lang")){
                try {
                    this.lang= jsonObject.getString("lang");
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
            if(jsonObject.has("booking_close_time")){
                try {
                    this.booking_close_time= jsonObject.getString("booking_close_time");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if(jsonObject.has("what_you_teach")){
                try {
                    this.what_you_teach= jsonObject.getString("what_you_teach");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if(jsonObject.has("skills_you_learn")){
                try {
                    this.skills_you_learn= jsonObject.getString("skills_you_learn");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if(jsonObject.has("cuurency_code")){
                try {
                    this.cuurency_code= jsonObject.getString("cuurency_code");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if(jsonObject.has("charge_amount")){
                try {
                    this.charge_amount= jsonObject.getString("charge_amount");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            if(jsonObject.has("about_coach_profile")){
                try {
                    this.about_coach_profile= jsonObject.getString("about_coach_profile");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            if(jsonObject.has("achievement")){
                try {
                    this.achievement= jsonObject.getString("achievement");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if(jsonObject.has("exp_months")){
                try {
                    this.exp_months= jsonObject.getString("exp_months");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if(jsonObject.has("exp_years")){
                try {
                    this.exp_years= jsonObject.getString("exp_years");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if(jsonObject.has("coach_id")){
                try {
                    this.coach_id= jsonObject.getString("coach_id");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if(jsonObject.has("user_id")){
                try {
                    this.user_id= jsonObject.getString("user_id");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            if(jsonObject.has("profile_title")){
                try {
                    this.profile_title= jsonObject.getString("profile_title");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if(jsonObject.has("certificate")){
                try {
                    this.certificatesList= getcertificateList(jsonObject.getJSONArray("certificate"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if(jsonObject.has("price")){
                try {
                    this.price= getPriceDetails(jsonObject.getJSONObject("price"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }



        }


        public class Price implements Serializable {
            public String getOffer_id() {
                return offer_id;
            }

            public void setOffer_id(String offer_id) {
                this.offer_id = offer_id;
            }

            public int getOffer_percentage() {
                return offer_percentage;
            }

            public void setOffer_percentage(int offer_percentage) {
                this.offer_percentage = offer_percentage;
            }

            public String getCoach_price() {
                return coach_price;
            }

            public void setCoach_price(String coach_price) {
                this.coach_price = coach_price;
            }

            public String getPayment_price() {
                return payment_price;
            }

            public void setPayment_price(String payment_price) {
                this.payment_price = payment_price;
            }

            public String getOffer_amount() {
                return offer_amount;
            }

            public void setOffer_amount(String offer_amount) {
                this.offer_amount = offer_amount;
            }

            public String getOffer_off_price() {
                return offer_off_price;
            }

            public void setOffer_off_price(String offer_off_price) {
                this.offer_off_price = offer_off_price;
            }

            public String getPrice_str() {
                return price_str;
            }

            public void setPrice_str(String price_str) {
                this.price_str = price_str;
            }

            public String getCuurency_code() {
                return cuurency_code;
            }

            public void setCuurency_code(String cuurency_code) {
                this.cuurency_code = cuurency_code;
            }

            public String getCoach_charge_str() {
                return coach_charge_str;
            }

            public void setCoach_charge_str(String coach_charge_str) {
                this.coach_charge_str = coach_charge_str;
            }

            public String getTaxes_amount() {
                return taxes_amount;
            }

            public void setTaxes_amount(String taxes_amount) {
                this.taxes_amount = taxes_amount;
            }

            public String getWith_taxes_amount() {
                return with_taxes_amount;
            }

            public void setWith_taxes_amount(String with_taxes_amount) {
                this.with_taxes_amount = with_taxes_amount;
            }

            public String getPayment_amount() {
                return payment_amount;
            }

            public void setPayment_amount(String payment_amount) {
                this.payment_amount = payment_amount;
            }

            public String getOffer_price() {
                return offer_price;
            }

            public void setOffer_price(String offer_price) {
                this.offer_price = offer_price;
            }

            private String offer_id = "";
            private int offer_percentage;
            private String coach_price = "";
            private String payment_price = "";
            private String offer_amount = "";
            private String offer_off_price = "";
            private String price_str = "";
            private String cuurency_code = "";
            private String coach_charge_str = "";
            private String taxes_amount = "";
            private String with_taxes_amount = "";
            private String payment_amount = "";
            private String offer_price = "";


            Price(JSONObject jsonObject) {
             if(jsonObject.has("offer_price")){
                 try {
                     this.offer_price = jsonObject.getString("offer_price");
                 } catch (JSONException e) {
                     e.printStackTrace();
                 }
             }
                if(jsonObject.has("payment_amount")){
                    try {
                        this.payment_amount = jsonObject.getString("payment_amount");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                if(jsonObject.has("with_taxes_amount")){
                    try {
                        this.with_taxes_amount = jsonObject.getString("with_taxes_amount");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                if(jsonObject.has("taxes_amount")){
                    try {
                        this.taxes_amount = jsonObject.getString("taxes_amount");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                if(jsonObject.has("coach_charge_str")){
                    try {
                        this.coach_charge_str = jsonObject.getString("coach_charge_str");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                if(jsonObject.has("cuurency_code")){
                    try {
                        this.cuurency_code = jsonObject.getString("cuurency_code");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                if(jsonObject.has("price_str")){
                    try {
                        this.price_str = jsonObject.getString("price_str");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                if(jsonObject.has("offer_off_price")){
                    try {
                        this.offer_off_price = jsonObject.getString("offer_off_price");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                if(jsonObject.has("offer_amount")){
                    try {
                        this.offer_amount = jsonObject.getString("offer_amount");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                if(jsonObject.has("payment_price")){
                    try {
                        this.payment_price = jsonObject.getString("payment_price");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                if(jsonObject.has("coach_price")){
                    try {
                        this.coach_price = jsonObject.getString("coach_price");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                if(jsonObject.has("offer_percentage")){
                    try {
                        this.offer_percentage = jsonObject.getInt("offer_percentage");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                if(jsonObject.has("offer_id")){
                    try {
                        this.offer_id = jsonObject.getString("offer_id");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }

        }

        public class certificate implements Serializable {

            public certificate(JSONObject jsonObject) {
                try {
                    this.id = jsonObject.getString("id");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    this.title = jsonObject.getString("title");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    this.file_path = jsonObject.getString("file_path");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    this.certificate_url = jsonObject.getString("certificate_url");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getFile_path() {
                return file_path;
            }

            public void setFile_path(String file_path) {
                this.file_path = file_path;
            }

            public String getCertificate_url() {
                return certificate_url;
            }

            public void setCertificate_url(String certificate_url) {
                this.certificate_url = certificate_url;
            }

            @SerializedName("id")
            @Expose
            private String id;
            @SerializedName("title")
            @Expose
            private String title;
            @SerializedName("file_path")
            @Expose
            private String file_path;
            @SerializedName("certificate_url")
            @Expose
            private String certificate_url;


        }


        public ArrayList<certificate> getcertificateList(JSONArray jsonArray){
            ArrayList<certificate> certificates=new ArrayList<>();
            for(int i=0;i<jsonArray.length();i++){
                try {
                    certificates.add(new certificate(jsonArray.getJSONObject(i)));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return certificates;

        }
        private Price getPriceDetails(JSONObject jsonObject) {
            return new Price(jsonObject);

        }

    }

}
