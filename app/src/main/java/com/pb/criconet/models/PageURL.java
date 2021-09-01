package com.pb.criconet.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class PageURL implements Serializable {

    public JSONObject getAboutCoach() {
        return aboutCoach;
    }

    public void setAboutCoach(JSONObject aboutCoach) {
        this.aboutCoach = aboutCoach;
    }

    public JSONObject getAboutECoaching() {
        return aboutECoaching;
    }

    public void setAboutECoaching(JSONObject aboutECoaching) {
        this.aboutECoaching = aboutECoaching;
    }

    public JSONObject getAboutUs() {
        return aboutUs;
    }

    public void setAboutUs(JSONObject aboutUs) {
        this.aboutUs = aboutUs;
    }

    public JSONObject getFaq() {
        return faq;
    }

    public void setFaq(JSONObject faq) {
        this.faq = faq;
    }

    public JSONObject getTearms() {
        return tearms;
    }

    public void setTearms(JSONObject tearms) {
        this.tearms = tearms;
    }

    public JSONObject getPrivacyPolicy() {
        return privacyPolicy;
    }

    public void setPrivacyPolicy(JSONObject privacyPolicy) {
        this.privacyPolicy = privacyPolicy;
    }

    public JSONObject getUserAggreement() {
        return userAggreement;
    }

    public void setUserAggreement(JSONObject userAggreement) {
        this.userAggreement = userAggreement;
    }

    public JSONObject getPartner() {
        return partner;
    }

    public void setPartner(JSONObject partner) {
        this.partner = partner;
    }

    public JSONObject getGroundOwner() {
        return groundOwner;
    }

    public void setGroundOwner(JSONObject groundOwner) {
        this.groundOwner = groundOwner;
    }

    public JSONObject getLiveStreaming() {
        return liveStreaming;
    }

    public void setLiveStreaming(JSONObject liveStreaming) {
        this.liveStreaming = liveStreaming;
    }

    private JSONObject aboutCoach;
    private JSONObject aboutECoaching;
    private JSONObject aboutUs;
    private JSONObject faq;
    private JSONObject tearms;
    private JSONObject privacyPolicy;
    private JSONObject userAggreement;
    private JSONObject partner;
    private JSONObject groundOwner;
    private JSONObject liveStreaming;

    public JSONObject getMediaReleases() {
        return MediaReleases;
    }

    public void setMediaReleases(JSONObject mediaReleases) {
        MediaReleases = mediaReleases;
    }

    private JSONObject MediaReleases;

    public JSONObject getContact_us() {
        return contact_us;
    }

    public void setContact_us(JSONObject contact_us) {
        this.contact_us = contact_us;
    }

    private JSONObject contact_us;
//
//            "about_e_coaching":
//            "about_coaches"
//            "faq":
//            "live_streaming":
//            "ground_owner":
//            "partners":
//            "about_us":
//            "privacy_policy":
//            "terms":
//            "user_agreement":


     public PageURL(JSONObject jsonObject) {
         if(jsonObject.has("about_e_coaching")){
             try {
                 this.aboutECoaching=jsonObject.getJSONObject("about_e_coaching");
             } catch (JSONException e) {
                 e.printStackTrace();
             }

         }
         if(jsonObject.has("about_coaches")){
             try {
                 this.aboutCoach=jsonObject.getJSONObject("about_coaches");
             } catch (JSONException e) {
                 e.printStackTrace();
             }

         }
         if(jsonObject.has("contact")){
             try {
                 this.contact_us=jsonObject.getJSONObject("contact");
             } catch (JSONException e) {
                 e.printStackTrace();
             }

         }
         if(jsonObject.has("faq")){
             try {
                 this.faq=jsonObject.getJSONObject("faq");
             } catch (JSONException e) {
                 e.printStackTrace();
             }

         }

         if(jsonObject.has("live_streaming")){
             try {
                 this.liveStreaming=jsonObject.getJSONObject("live_streaming");
             } catch (JSONException e) {
                 e.printStackTrace();
             }

         }
         if(jsonObject.has("ground_owner")){
             try {
                 this.groundOwner=jsonObject.getJSONObject("ground_owner");
             } catch (JSONException e) {
                 e.printStackTrace();
             }

         }

         if(jsonObject.has("partners")){
             try {
                 this.partner=jsonObject.getJSONObject("partners");
             } catch (JSONException e) {
                 e.printStackTrace();
             }

         }

         if(jsonObject.has("about_us")){
             try {
                 this.aboutUs=jsonObject.getJSONObject("about_us");
             } catch (JSONException e) {
                 e.printStackTrace();
             }

         }

         if(jsonObject.has("privacy_policy")){
             try {
                 this.privacyPolicy=jsonObject.getJSONObject("privacy_policy");
             } catch (JSONException e) {
                 e.printStackTrace();
             }

         }

         if(jsonObject.has("terms")){
             try {
                 this.tearms=jsonObject.getJSONObject("terms");
             } catch (JSONException e) {
                 e.printStackTrace();
             }

         }

         if(jsonObject.has("user_agreement")){
             try {
                 this.userAggreement=jsonObject.getJSONObject("user_agreement");
             } catch (JSONException e) {
                 e.printStackTrace();
             }

         }
         if(jsonObject.has("media_releases")){
             try {
                 this.MediaReleases=jsonObject.getJSONObject("media_releases");
             } catch (JSONException e) {
                 e.printStackTrace();
             }

         }

    }

}



