package com.pb.criconet.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CoachList {
    @SerializedName("api_status")
    @Expose
    private String apiStatus;
    @SerializedName("api_text")
    @Expose
    private String apiText;
    @SerializedName("api_version")
    @Expose
    private String apiVersion;
    @SerializedName("data")
    @Expose
    private List<Datum> data = null;

    @SerializedName("errors")
    @Expose
    private Errors errors;

    public String getApiStatus() {
        return apiStatus;
    }

    public void setApiStatus(String apiStatus) {
        this.apiStatus = apiStatus;
    }

    public String getApiText() {
        return apiText;
    }

    public void setApiText(String apiText) {
        this.apiText = apiText;
    }

    public String getApiVersion() {
        return apiVersion;
    }

    public void setApiVersion(String apiVersion) {
        this.apiVersion = apiVersion;
    }

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }


    public Errors getErrors() {
        return errors;
    }

    public void setErrors(Errors errors) {
        this.errors = errors;
    }


    public class Datum {

        @SerializedName("user_id")
        @Expose
        private String userId;
        @SerializedName("profile_title")
        @Expose
        private String profileTitle;
        @SerializedName("about_coach_profile")
        @Expose
        private String aboutCoachProfile;
        @SerializedName("specialization")
        @Expose
        private String specialization;
        @SerializedName("charge_amount")
        @Expose
        private String chargeAmount;
        @SerializedName("cuurency_code")
        @Expose
        private String cuurencyCode;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("username")
        @Expose
        private String username;
        @SerializedName("avatar")
        @Expose
        private String avatar;

        public String getLang() {
            return lang;
        }

        public void setLang(String lang) {
            this.lang = lang;
        }

        @SerializedName("lang")
        @Expose
        private String lang;

        public String getCover() {
            return cover;
        }

        public void setCover(String cover) {
            this.cover = cover;
        }

        @SerializedName("cover")
        @Expose
        private String cover;

        @SerializedName("price")
        @Expose
        private Price price;

        public ArrayList<CoachList.certificate> getCertificate() {
            return certificate;
        }

        public void setCertificate(ArrayList<CoachList.certificate> certificate) {
            this.certificate = certificate;
        }

        @SerializedName("certificate")
        @Expose
        private ArrayList<certificate> certificate;

        // Decodes array of CommentModel json results into CommentModel model objects
        public  ArrayList<certificate> fromJson(JSONArray jsonArray) {
            JSONObject modelJson;
            ArrayList<certificate> models = new ArrayList<>(jsonArray.length());
            // Process each result in json array, decode and convert to CardModel object
            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    modelJson = jsonArray.getJSONObject(i);
                } catch (Exception e) {
                    e.printStackTrace();
                    continue;
                }
                certificate model = new certificate(modelJson);
                if (model != null) {
                    models.add(model);
                }
            }

            return models;
        }


        public int getRating() {
            return rating;
        }

        public void setRating(int rating) {
            this.rating = rating;
        }

        @SerializedName("rating")
        @Expose
        private int rating;


        public String getExps() {
            return exps;
        }

        public void setExps(String exps) {
            this.exps = exps;
        }

        @SerializedName("exps")
        @Expose
        private String exps;


        public String getCat_title() {
            return cat_title;
        }

        public void setCat_title(String cat_title) {
            this.cat_title = cat_title;
        }

        @SerializedName("cat_title")
        @Expose
        private String cat_title;

        public String getADAYS() {
            return ADAYS;
        }

        public void setADAYS(String ADAYS) {
            this.ADAYS = ADAYS;
        }

        @SerializedName("ADAYS")
        @Expose
        private String ADAYS;

        public String getADAYS_msg() {
            return ADAYS_msg;
        }

        public void setADAYS_msg(String ADAYS_msg) {
            this.ADAYS_msg = ADAYS_msg;
        }

        private String ADAYS_msg;




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

        @SerializedName("exp_years")
        @Expose
        private String exp_years;
        @SerializedName("exp_months")
        @Expose
        private String exp_months;

        public String getAchievement() {
            return achievement;
        }

        public void setAchievement(String achievement) {
            this.achievement = achievement;
        }

        @SerializedName("achievement")
        @Expose
        private String achievement;



        @SerializedName("profile_link")
        @Expose
        private String profileLink;

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getProfileTitle() {
            return profileTitle;
        }

        public void setProfileTitle(String profileTitle) {
            this.profileTitle = profileTitle;
        }

        public String getAboutCoachProfile() {
            return aboutCoachProfile;
        }

        public void setAboutCoachProfile(String aboutCoachProfile) {
            this.aboutCoachProfile = aboutCoachProfile;
        }

        public String getSpecialization() {
            return specialization;
        }

        public void setSpecialization(String specialization) {
            this.specialization = specialization;
        }

        public String getChargeAmount() {
            return chargeAmount;
        }

        public void setChargeAmount(String chargeAmount) {
            this.chargeAmount = chargeAmount;
        }

        public String getCuurencyCode() {
            return cuurencyCode;
        }

        public void setCuurencyCode(String cuurencyCode) {
            this.cuurencyCode = cuurencyCode;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
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

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getProfileLink() {
            return profileLink;
        }

        public void setProfileLink(String profileLink) {
            this.profileLink = profileLink;
        }
        public Price getPrice() {
            return price;
        }

        public void setPrice(Price price) {
            this.price = price;
        }

    }
    public class Price implements Serializable {

        @SerializedName("offer_id")
        @Expose
        private String offerId;
        @SerializedName("offer_percentage")
        @Expose
        private String offerPercentage;
        @SerializedName("coach_price")
        @Expose
        private String coachPrice;
        @SerializedName("payment_price")
        @Expose
        private String paymentPrice;
        @SerializedName("price_str")
        @Expose
        private String priceStr;
        @SerializedName("cuurency_code")
        @Expose
        private String cuurencyCode;
        @SerializedName("coach_charge_str")
        @Expose
        private String coachChargeStr;
        @SerializedName("taxes_amount")
        @Expose
        private String taxesAmount;
        @SerializedName("with_taxes_amount")
        @Expose
        private String withTaxesAmount;

        public String getOfferId() {
            return offerId;
        }

        public void setOfferId(String offerId) {
            this.offerId = offerId;
        }

        public String getOfferPercentage() {
            return offerPercentage;
        }

        public void setOfferPercentage(String offerPercentage) {
            this.offerPercentage = offerPercentage;
        }

        public String getCoachPrice() {
            return coachPrice;
        }

        public void setCoachPrice(String coachPrice) {
            this.coachPrice = coachPrice;
        }

        public String getPaymentPrice() {
            return paymentPrice;
        }

        public void setPaymentPrice(String paymentPrice) {
            this.paymentPrice = paymentPrice;
        }

        public String getPriceStr() {
            return priceStr;
        }

        public void setPriceStr(String priceStr) {
            this.priceStr = priceStr;
        }

        public String getCuurencyCode() {
            return cuurencyCode;
        }

        public void setCuurencyCode(String cuurencyCode) {
            this.cuurencyCode = cuurencyCode;
        }

        public String getCoachChargeStr() {
            return coachChargeStr;
        }

        public void setCoachChargeStr(String coachChargeStr) {
            this.coachChargeStr = coachChargeStr;
        }

        public String getTaxesAmount() {
            return taxesAmount;
        }

        public void setTaxesAmount(String taxesAmount) {
            this.taxesAmount = taxesAmount;
        }

        public String getWithTaxesAmount() {
            return withTaxesAmount;
        }

        public void setWithTaxesAmount(String withTaxesAmount) {
            this.withTaxesAmount = withTaxesAmount;
        }

    }


    public static class certificate implements Serializable {

        public certificate(JSONObject jsonObject){
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
    @Override
    public String toString() {
        return "CoachList{" +
                "apiStatus='" + apiStatus + '\'' +
                ", apiText='" + apiText + '\'' +
                ", apiVersion='" + apiVersion + '\'' +
                ", data=" + data +
                '}';
    }

    public class Errors {

        @SerializedName("error_id")
        @Expose
        private String errorId;
        @SerializedName("error_text")
        @Expose
        private String errorText;

        public String getErrorId() {
            return errorId;
        }

        public void setErrorId(String errorId) {
            this.errorId = errorId;
        }

        public String getErrorText() {
            return errorText;
        }

        public void setErrorText(String errorText) {
            this.errorText = errorText;
        }

    }
}
