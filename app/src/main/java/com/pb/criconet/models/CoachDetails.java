package com.pb.criconet.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class CoachDetails implements Serializable {

    @SerializedName("errors")
    @Expose
    private Errors errors;

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
    private Data data;

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

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public Errors getErrors() {
        return errors;
    }

    public void setErrors(Errors errors) {
        this.errors = errors;
    }


    public class Data implements Serializable{

        @SerializedName("profile_title")
        @Expose
        private String profileTitle;
        @SerializedName("user_id")
        @Expose
        private String userId;
        @SerializedName("coach_id")
        @Expose
        private String coachId;
        @SerializedName("exp_years")
        @Expose
        private String expYears;
        @SerializedName("exp_months")
        @Expose
        private String expMonths;
        @SerializedName("achievement")
        @Expose
        private String achievement;
        @SerializedName("about_coach_profile")
        @Expose
        private String aboutCoachProfile;
        @SerializedName("charge_amount")
        @Expose
        private String chargeAmount;

        @SerializedName("cuurency_code")
        @Expose
        private String cuurencyCode;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("first_name")
        @Expose
        private String firstName;
        @SerializedName("last_name")
        @Expose
        private String lastName;
        @SerializedName("mid_name")
        @Expose
        private String midName;
        @SerializedName("username")
        @Expose
        private String username;
        @SerializedName("avatar")
        @Expose
        private String avatar;
        @SerializedName("cat_title")
        @Expose
        private String catTitle;
        @SerializedName("selected_cat_id")
        @Expose
        private String selectedCatId;
        @SerializedName("profile_link")
        @Expose
        private String profileLink;
        @SerializedName("price")
        @Expose
        private Price price;
        @SerializedName("is_offer")
        @Expose
        private String isOffer;
        @SerializedName("exps")
        @Expose
        private String exps;


        public String getProfileTitle() {
            return profileTitle;
        }

        public void setProfileTitle(String profileTitle) {
            this.profileTitle = profileTitle;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getCoachId() {
            return coachId;
        }

        public void setCoachId(String coachId) {
            this.coachId = coachId;
        }

        public String getExpYears() {
            return expYears;
        }

        public void setExpYears(String expYears) {
            this.expYears = expYears;
        }

        public String getExpMonths() {
            return expMonths;
        }

        public void setExpMonths(String expMonths) {
            this.expMonths = expMonths;
        }

        public String getAchievement() {
            return achievement;
        }

        public void setAchievement(String achievement) {
            this.achievement = achievement;
        }

        public String getAboutCoachProfile() {
            return aboutCoachProfile;
        }

        public void setAboutCoachProfile(String aboutCoachProfile) {
            this.aboutCoachProfile = aboutCoachProfile;
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

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getMidName() {
            return midName;
        }

        public void setMidName(String midName) {
            this.midName = midName;
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

        public String getCatTitle() {
            return catTitle;
        }

        public void setCatTitle(String catTitle) {
            this.catTitle = catTitle;
        }

        public String getSelectedCatId() {
            return selectedCatId;
        }

        public void setSelectedCatId(String selectedCatId) {
            this.selectedCatId = selectedCatId;
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

        public String getIsOffer() {
            return isOffer;
        }

        public void setIsOffer(String isOffer) {
            this.isOffer = isOffer;
        }

        public String getExps() {
            return exps;
        }

        public void setExps(String exps) {
            this.exps = exps;
        }
    }

    public class Price implements Serializable{

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
        public String getPayment_amount() {
            return payment_amount;
        }

        public void setPayment_amount(String payment_amount) {
            this.payment_amount = payment_amount;
        }

        @SerializedName("payment_amount")
        @Expose
        private String payment_amount;

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
