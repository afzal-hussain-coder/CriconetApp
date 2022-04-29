package com.pb.criconet.Utills;


import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.facebook.login.LoginManager;
import com.facebook.share.Share;

/**
 * Created by Pradeep on 1/31/2017.
 */

public class SessionManager {

    private static String NAME = "NAME";
    private static String PROFILETYPE = "PROFILETYPE";
    private static String FNAME = "FNAME";
    private static String LNAME = "LNAME";
    private static String EMAILID = "EMAILID";
    private static String PHONE= "PHONE";
    private static String PHONECODE= "PHONECODE";
    private static String PRICE = "PRICE";
    private static String mobile = "mobile";
    private static String user_id = "user_id";
    private static String coupon_code = "coupon_code";


    private static String coach_id = "coach_id";
    private static String user_name = "user_name";
    private static String coach_name = "coach_name";
    private static String country_code="country_code";
    private static String user_type = "user_type";
    private static String token_type = "token_type";
    private static String chanelId = "chanelId";
    private static String SESSION_CHECK_LOGIN = "SESSION_CHECK_LOGIN";
    private static String check_agreement = "CHECK_AGREEMENT";
    private static String password = "password";
    private static String sex = "sex";
    private static String session_id = "session_id";
    private static String dob = "DOB";
    private static String fitness = "fitness";
    private static String address = "address";
    private static String pinCode = "pinCode";
    private static String country = "country";
    private static String language = "language";
    private static String states = "states";
    private static String city = "city";
    private static String stateid = "state_id";
    private static String cityid = "city_id";
    private static String image = "image";
    private static String cover = "cover";
    private static String select_type = "select_type";
    private static String firebaseId = "firebaseId";
    private static String school = "school";
    private static String studied = "studied";
    private static String employment = "employment";
    private static String devicetoken = "devicetoken";
    private static String leaugeName = "title";
    private static String Onlinestatus = "Onlinestatus";
    private static String Friends = "FRIENDS";
    private static String Notification_count = "NOTIFICATION_COUNT";
    private static String languagecode = "LANGUAGECODE";
    private static String languagekey = "LANGUAGEKEY";
    private static String is_no_verified = "is_mobile_verified";
    private static String is_ambs= "is_ambs";

    private static String ambs_email = "email";
    private static String ambs_phone_number = "phone_number";
    private static String ambs_name = "name";
    private static String ambs_full_name = "full_name";
    private static String ambs_school_college_name = "school_college_name";
    private static String ambs_height_qualification = "height_qualification";
    private static String ambs_have_you_org_event_flag = "have_you_org_event_flag";
    private static String ambs_have_you_org_event_txt = "have_you_org_event_txt";
    private static String ambs_innovative_thing = "innovative_thing";
    private static String ambs_how_many_hrs_per_week = "how_many_hrs_per_week";
    private static String ambs_passionate_thing = "passionate_thing";
    private static String ambs_do_you_want_campus_ambassdor = "do_you_want_campus_ambassdor";
    private static String ambs_thing_you_are_know_criconet = "thing_you_are_know_criconet";


//    private static String CARTYPEID = "CARTYPEID";
//    private static String CARTYPENAME = "CARTYPENAME";
//    private static String CARDDETAIL = "CARDDETAIL";
//    private static String PUNCHINSTATUS = "PUNCHINSTATUS";
//    private static String VEHICLENUMBER = "VEHICLENUMBER";

//    private static String vehicledetail = "vehicledetail";
//    private static String uploaddoc = "uploaddoc";
//    private static String docverified = "docverified";
//    private static String uploaddrivinglicence = "uploaddrivinglicence";
//    private static String uploadvehicleinsurance = "uploadvehicleinsurance";
//    private static String uploadvehiclepermit = "uploadvehiclepermit";
//    private static String uploadvehicleregistration = "uploadvehicleregistration";
//    private static String vahical_number = "vahical_number";
//    private static String brand_id = "brand_id";
//    private static String carmodel_id = "carmodel_id";
//    private static String year = "year";
//    private static String color = "color";
//    private static String carTypeId = "carTypeId";
//    private static String carTypeName = "carTypeName";
//    private static String barnd = "barnd";
//    private static String model = "model";

    public static void savePreference(SharedPreferences prefs, String key, Boolean value) {
        Editor e = prefs.edit();
        e.putBoolean(key, value);
        e.commit();
    }

    public static void savePreference(SharedPreferences prefs, String key, int value) {
        Editor e = prefs.edit();
        e.putInt(key, value);
        e.commit();
    }

    public static void savePreference(SharedPreferences prefs, String key, String value) {
        Editor e = prefs.edit();
        e.putString(key, value);
        e.commit();
    }

    public static void dataclear(SharedPreferences prefs) {
        Editor e = prefs.edit();
        e.clear();
        e.commit();
    }

//    public static void save_card(SharedPreferences prefs, String value) {
//        SessionManager.savePreference(prefs, CARDDETAIL, value);
//    }
//
//    public static String get_card(SharedPreferences prefs) {
//        return prefs.getString(CARDDETAIL, "");
//    }


    public static void save_check_login(SharedPreferences prefs, Boolean value) {
        SessionManager.savePreference(prefs, SESSION_CHECK_LOGIN, value);
    }

    public static Boolean get_check_login(SharedPreferences prefs) {
        return prefs.getBoolean(SESSION_CHECK_LOGIN, false);
    }

    public static void save_check_agreement(SharedPreferences prefs, Boolean value) {
        SessionManager.savePreference(prefs, check_agreement, value);
    }

    public static Boolean get_check_agreement(SharedPreferences prefs) {
        return prefs.getBoolean(check_agreement, false);
    }

    public static void save_name(SharedPreferences prefs, String value) {
        SessionManager.savePreference(prefs, NAME, value);
    }

    public static String get_name(SharedPreferences prefs) {
        return prefs.getString(NAME, "");
    }

    public static void save_profiletype(SharedPreferences prefs, String value) {
        SessionManager.savePreference(prefs, PROFILETYPE, value);
    }

    public static String get_profiletype(SharedPreferences prefs) {
        return prefs.getString(PROFILETYPE, "");
    }


    public static void save_fname(SharedPreferences prefs, String value) {
        SessionManager.savePreference(prefs, FNAME, value);
    }

    public static String get_fname(SharedPreferences prefs) {
        return prefs.getString(FNAME, "");
    }

    public static void save_lname(SharedPreferences prefs, String value) {
        SessionManager.savePreference(prefs, LNAME, value);
    }

    public static void save_profileType(SharedPreferences prefs, String value) {
        SessionManager.savePreference(prefs, LNAME, value);
    }

    public static String get_lname(SharedPreferences prefs) {
        return prefs.getString(LNAME, "");
    }

    public static void save_emailid(SharedPreferences prefs, String value) {
        SessionManager.savePreference(prefs, EMAILID, value);
    }
    public static void savePhone(SharedPreferences prefs, String value){
        SessionManager.savePreference(prefs, PHONE, value);
    }
    public static void savePhoneCode(SharedPreferences prefs, String value){
        SessionManager.savePreference(prefs, PHONECODE, value);
    }

    public static String get_emailid(SharedPreferences prefs) {
        return prefs.getString(EMAILID, "");
    }

    public static void save_password(SharedPreferences prefs, String value) {
        SessionManager.savePreference(prefs, password, value);
    }

    public static String get_password(SharedPreferences prefs) {
        return prefs.getString(password, "");
    }

    public static void save_sex(SharedPreferences prefs, String value) {
        SessionManager.savePreference(prefs, sex, value);
    }

    public static String get_sex(SharedPreferences prefs) {
        return prefs.getString(sex, "");
    }

    public static void save_session_id(SharedPreferences prefs, String value) {
        SessionManager.savePreference(prefs, session_id, value);
    }

    public static String get_session_id(SharedPreferences prefs) {
        return prefs.getString(session_id, "");
    }

    public static void save_dob(SharedPreferences prefs, String value) {
        SessionManager.savePreference(prefs, dob, value);
    }

    public static String get_dob(SharedPreferences prefs) {
        return prefs.getString(dob, "");
    }

    public static void save_mobile(SharedPreferences prefs, String value) {
        SessionManager.savePreference(prefs, mobile, value);
    }

    public static String get_mobile(SharedPreferences prefs) {
        return prefs.getString(mobile, "");
    }

    public static void save_user_id(SharedPreferences prefs, String value) {
        SessionManager.savePreference(prefs, user_id, value);
    }

    public static void save_coupon_code(SharedPreferences prefs, String value) {
        SessionManager.savePreference(prefs, coupon_code, value);
    }
    public static void save_coach_id(SharedPreferences prefs, String value) {
        SessionManager.savePreference(prefs, coach_id, value);
    }
    public static String get_coach_id(SharedPreferences prefs) {
        return prefs.getString(coach_id, "");
    }

    public static void save_user_name(SharedPreferences prefs, String value) {
        SessionManager.savePreference(prefs, user_name, value);
    }
    public static String get_user_name(SharedPreferences prefs) {
        return prefs.getString(user_name, "");
    }

    public static void save_coach_name(SharedPreferences prefs, String value) {
        SessionManager.savePreference(prefs, coach_name, value);
    }
    public static String get_coach_name(SharedPreferences prefs) {
        return prefs.getString(coach_name, "");
    }

    public static void saveCountryCode(SharedPreferences prefs, String value){
        SessionManager.savePreference(prefs, country_code, value);
    }

    public static String get_user_id(SharedPreferences prefs) {
        return prefs.getString(user_id, "");
    }

    public static String getCouponCode(SharedPreferences prefs) {
        return prefs.getString(coupon_code, "");
    }

    public static void save_select_type(SharedPreferences prefs, String value) {
        SessionManager.savePreference(prefs, select_type, value);
    }

    public static String get_select_type(SharedPreferences prefs) {
        return prefs.getString(select_type, "");
    }


    public static void save_accessToken(SharedPreferences prefs, String value) {
        SessionManager.savePreference(prefs, token_type, value);
    }

    public static String get_accessToken(SharedPreferences prefs) {
        return prefs.getString(token_type, "");
    }
    public static void save_chanelId(SharedPreferences prefs, String value) {
        SessionManager.savePreference(prefs, chanelId, value);
    }

    public static String get_chanelId(SharedPreferences prefs) {
        return prefs.getString(chanelId, "");
    }

    public static void save_userType(SharedPreferences prefs, String value) {
        SessionManager.savePreference(prefs, user_type, value);
    }


    public static String get_userType(SharedPreferences prefs) {
        return prefs.getString(user_type, "");
    }

    public static void save_price(SharedPreferences prefs, String value) {
        SessionManager.savePreference(prefs, PRICE, value);
    }

    public static String get_price(SharedPreferences prefs) {
        return prefs.getString(PRICE, "");
    }


    public static void save_address(SharedPreferences prefs, String value) {
        SessionManager.savePreference(prefs, address, value);
    }

    public static String get_address(SharedPreferences prefs) {
        return prefs.getString(address, "");
    }

    public static void savepinCode(SharedPreferences prefs, String value) {
        SessionManager.savePreference(prefs, pinCode, value);
    }

    public static String getpinCode(SharedPreferences prefs) {
        return prefs.getString(pinCode, "");
    }

    public static String getPhoneNumber(SharedPreferences prefs){
        return prefs.getString(PHONE,"");
    }
    public static String getPhoneCode(SharedPreferences prefs){
        return prefs.getString(PHONECODE,"");
    }
    public static String getProfileType(SharedPreferences prefs){
        return prefs.getString(PROFILETYPE,"");
    }
    public static void saveCountry(SharedPreferences prefs, String value) {
        SessionManager.savePreference(prefs, country, value);
    }

    public static String getCountry(SharedPreferences prefs) {
        return prefs.getString(country, "");
    }

    public static void saveStates(SharedPreferences prefs, String value) {
        SessionManager.savePreference(prefs, states, value);
    }

    public static String getStates(SharedPreferences prefs) {
        return prefs.getString(states, "");
    }

    public static void saveCity(SharedPreferences prefs, String value) {
        SessionManager.savePreference(prefs, city, value);
    }

    public static String getCity(SharedPreferences prefs) {
        return prefs.getString(city, "");
    }
    public static void saveLanguage(SharedPreferences prefs, String value) {
        SessionManager.savePreference(prefs, language, value);
    }

    public static String getLanguage(SharedPreferences prefs) {
        return prefs.getString(language, "");
    }

    public static void saveStateId(SharedPreferences prefs, String value) {
        SessionManager.savePreference(prefs, stateid, value);
    }

    public static String getStateId(SharedPreferences prefs) {
        return prefs.getString(stateid, "");
    }

    public static void saveCityId(SharedPreferences prefs, String value) {
        SessionManager.savePreference(prefs, cityid, value);
    }

    public static String getCityid(SharedPreferences prefs) {
        return prefs.getString(cityid, "");
    }


    public static void save_image(SharedPreferences prefs, String value) {
        SessionManager.savePreference(prefs, image, value);
    }

    public static String get_image(SharedPreferences prefs) {
        return prefs.getString(image, "");
    }

    public static void save_cover(SharedPreferences prefs, String value) {
        SessionManager.savePreference(prefs, cover, value);
    }

    public static String get_cover(SharedPreferences prefs) {
        return prefs.getString(cover, "");
    }

    public static void save_devicetoken(SharedPreferences prefs, String value) {
        SessionManager.savePreference(prefs, devicetoken, value);
    }

    public static String get_devicetoken(SharedPreferences prefs) {
        return prefs.getString(devicetoken, "");
    }
    public static void save_leaugeName(SharedPreferences prefs, String value) {
        SessionManager.savePreference(prefs, leaugeName, value);
    }

    public static String get_leaugeName(SharedPreferences prefs) {
        return prefs.getString(leaugeName, "");
    }

    public static void save_fitness(SharedPreferences prefs, String value) {
        SessionManager.savePreference(prefs, fitness, value);
    }

    public static String get_fitness(SharedPreferences prefs) {
        return prefs.getString(fitness, "");
    }

    public static void save_firebaseId(SharedPreferences prefs, String value) {
        SessionManager.savePreference(prefs, firebaseId, value);
    }

    public static String get_firebaseId(SharedPreferences prefs) {
        return prefs.getString(firebaseId, "");
    }

    public static void save_school(SharedPreferences prefs, String value) {
        SessionManager.savePreference(prefs, school, value);
    }

    public static String get_school(SharedPreferences prefs) {
        return prefs.getString(school, "");
    }

    public static void save_studied(SharedPreferences prefs, String value) {
        SessionManager.savePreference(prefs, studied, value);
    }

    public static String get_studied(SharedPreferences prefs) {
        return prefs.getString(studied, "");
    }

    public static void save_employment(SharedPreferences prefs, String value) {
        SessionManager.savePreference(prefs, employment, value);
    }

    public static String get_employment(SharedPreferences prefs) {
        return prefs.getString(employment, "");
    }

    public static void save_onlinestatus(SharedPreferences prefs, Boolean value) {
        SessionManager.savePreference(prefs, Onlinestatus, value);
    }

    public static Boolean get_onlinestatus(SharedPreferences prefs) {
        return prefs.getBoolean(Onlinestatus, false);
    }

    public static void save_Friends(SharedPreferences prefs, String value) {
        SessionManager.savePreference(prefs, Friends, value);
    }

    public static String get_Friends(SharedPreferences prefs) {
        return prefs.getString(Friends, "0");
    }

    public static void save_Notification(SharedPreferences prefs, String value) {
        SessionManager.savePreference(prefs, Notification_count, value);
    }

    public static String get_Notification(SharedPreferences prefs) {
        return prefs.getString(Notification_count, "0");
    }

    public static void save_languagecode(SharedPreferences prefs, String value) {
        SessionManager.savePreference(prefs, languagecode, value);
    }

    public static String get_languagecode(SharedPreferences prefs) {
        return prefs.getString(languagecode, "en");
    }

    public static void save_languagekey(SharedPreferences prefs, String value) {
        SessionManager.savePreference(prefs, languagekey, value);
    }

    public static String get_languagekey(SharedPreferences prefs) {
        return prefs.getString(languagekey, "home");
    }

    public static void save_mobile_verified(SharedPreferences prefs, String value) {
        SessionManager.savePreference(prefs, is_no_verified, value);
    }

    public static String get_mobile_verified(SharedPreferences prefs) {
        return prefs.getString(is_no_verified, "");
    }
    public static void save_is_ambassador(SharedPreferences prefs, String value) {
        SessionManager.savePreference(prefs, is_ambs, value);
    }

    public static String get_is_ambassador(SharedPreferences prefs) {
        return prefs.getString(is_ambs, "");
    }
    public static void save_is_amb_email(SharedPreferences prefs, String value) {
        SessionManager.savePreference(prefs, ambs_email, value);
    }

    public static String get_is_amb_email(SharedPreferences prefs) {
        return prefs.getString(ambs_email, "");
    }
    public static void save_is_amb_name(SharedPreferences prefs, String value) {
        SessionManager.savePreference(prefs, ambs_name, value);
    }

    public static String get_is_amb_name(SharedPreferences prefs) {
        return prefs.getString(ambs_name, "");
    }

    public static void save_is_amb_fullname(SharedPreferences prefs, String value) {
        SessionManager.savePreference(prefs, ambs_full_name, value);
    }

    public static String get_is_amb_fullname(SharedPreferences prefs) {
        return prefs.getString(ambs_full_name, "");
    }
    public static void save_is_amb_phone(SharedPreferences prefs, String value) {
        SessionManager.savePreference(prefs, ambs_phone_number, value);
    }

    public static String get_is_amb_phone(SharedPreferences prefs) {
        return prefs.getString(ambs_phone_number, "");
    }
    public static void save_is_amb_college(SharedPreferences prefs, String value) {
        SessionManager.savePreference(prefs, ambs_school_college_name, value);
    }

    public static String get_is_amb_college(SharedPreferences prefs) {
        return prefs.getString(ambs_school_college_name, "");
    }
    public static void save_is_amb_highestQ(SharedPreferences prefs, String value) {
        SessionManager.savePreference(prefs, ambs_height_qualification, value);
    }

    public static String get_is_amb_highestQ(SharedPreferences prefs) {
        return prefs.getString(ambs_height_qualification, "");
    }
    public static void save_is_ambs_have_you_org_event_flag(SharedPreferences prefs, String value) {
        SessionManager.savePreference(prefs, ambs_have_you_org_event_flag, value);
    }

    public static String get_is_ambs_have_you_org_event_flag(SharedPreferences prefs) {
        return prefs.getString(ambs_have_you_org_event_flag, "");
    }
    public static void save_is_ambs_have_you_org_event_txt(SharedPreferences prefs, String value) {
        SessionManager.savePreference(prefs, ambs_have_you_org_event_txt, value);
    }

    public static String get_is_ambs_have_you_org_event_txt(SharedPreferences prefs) {
        return prefs.getString(ambs_have_you_org_event_txt, "");
    }
    public static void save_is_ambs_innovative_thing(SharedPreferences prefs, String value) {
        SessionManager.savePreference(prefs, ambs_innovative_thing, value);
    }

    public static String get_is_ambs_innovative_thing(SharedPreferences prefs) {
        return prefs.getString(ambs_innovative_thing, "");
    }
    public static void save_is_ambs_how_many_hrs_per_week(SharedPreferences prefs, String value) {
        SessionManager.savePreference(prefs, ambs_how_many_hrs_per_week, value);
    }

    public static String get_is_ambs_how_many_hrs_per_week(SharedPreferences prefs) {
        return prefs.getString(ambs_how_many_hrs_per_week, "");
    }
    public static void save_is_ambs_passionate_thing(SharedPreferences prefs, String value) {
        SessionManager.savePreference(prefs, ambs_passionate_thing, value);
    }

    public static String get_is_ambs_passionate_thing(SharedPreferences prefs) {
        return prefs.getString(ambs_passionate_thing, "");
    }
    public static void save_is_ambs_do_you_want_campus_ambassdor(SharedPreferences prefs, String value) {
        SessionManager.savePreference(prefs, ambs_do_you_want_campus_ambassdor, value);
    }

    public static String get_is_ambs_do_you_want_campus_ambassdor(SharedPreferences prefs) {
        return prefs.getString(ambs_do_you_want_campus_ambassdor, "");
    }
    public static void save_is_ambs_thing_you_are_know_criconet(SharedPreferences prefs, String value) {
        SessionManager.savePreference(prefs, ambs_thing_you_are_know_criconet, value);
    }

    public static String get_is_ambs_thing_you_are_know_criconet(SharedPreferences prefs) {
        return prefs.getString(ambs_thing_you_are_know_criconet, "");
    }

}