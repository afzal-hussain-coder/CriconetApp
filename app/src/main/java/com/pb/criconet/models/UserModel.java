package com.pb.criconet.models;

/**
 * Created by Manish Yadav on 10/22/2018.
 */

public class UserModel {
    // {"id":"128","firstName":"Raman","quickbloxID":"","emailId":"raman@mailinator.com","image":"http:\/\/demo.evirtualservices.com\/criconet\/img\/profile\/1540448941profile.jpeg",
// "password2":"123456","dob":"","hear_about":"","gender":"Male","address":"Noida","about_me":"", "deviceToken":"42840239dnasdhqsau28093842",
// "device":"ios","firebaseId":"brbMXKbzheTlO59H9GmodDBwKLy2","payment_id":"","payment_amount":"","payment_time":"","mute":"0","visibility":"1",
// "enableDisablePushNotification":"1","photoo.jpg.jpg":0,"video":0,"school":"","studied":"","employment":"","Friend":1,"countPageFriend":1,
// "totalNotification":1,"friendId":"32","friendStatus":"1"}
    private String id;
    private String firstName;
    private String quickbloxID;
    private String emailId;
    private String image;
    private String password2;
    private String dob;
    private String hear_about;
    private String gender;
    private String address;
    private String about_me;
    private String deviceToken;
    private String device;
    private String firebaseId;
    private String payment_id;
    private String payment_amount;
    private String payment_time;
    private String mute;
    private String visibility;
    private String enableDisablePushNotification;
    private String photo;
    private String video;
    private String school;
    private String studied;
    private String employment;
    private String Friend;
    private String countPageFriend;
    private String totalNotification;
    private String friendId;
    private String friendStatus;
    private String callStatus;
    private String callTime;
    private String callType;
    private String Type;
    private String reqId;
    private String phone;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmployment() {
        return employment;
    }

    public void setEmployment(String employment) {
        this.employment = employment;
    }

    public String getStudied() {
        return studied;
    }

    public void setStudied(String studied) {
        this.studied = studied;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getFriendId() {
        return friendId;
    }

    public void setFriendId(String friendId) {
        this.friendId = friendId;
    }

    public String getFriendStatus() {
        return friendStatus;
    }

    public void setFriendStatus(String friendStatus) {
        this.friendStatus = friendStatus;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuickbloxID() {
        return quickbloxID;
    }

    public void setQuickbloxID(String quickbloxID) {
        this.quickbloxID = quickbloxID;
    }

    public String getPassword2() {
        return password2;
    }

    public void setPassword2(String password2) {
        this.password2 = password2;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getHear_about() {
        return hear_about;
    }

    public void setHear_about(String hear_about) {
        this.hear_about = hear_about;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAbout_me() {
        return about_me;
    }

    public void setAbout_me(String about_me) {
        this.about_me = about_me;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getFirebaseId() {
        return firebaseId;
    }

    public void setFirebaseId(String firebaseId) {
        this.firebaseId = firebaseId;
    }

    public String getPayment_id() {
        return payment_id;
    }

    public void setPayment_id(String payment_id) {
        this.payment_id = payment_id;
    }

    public String getPayment_amount() {
        return payment_amount;
    }

    public void setPayment_amount(String payment_amount) {
        this.payment_amount = payment_amount;
    }

    public String getPayment_time() {
        return payment_time;
    }

    public void setPayment_time(String payment_time) {
        this.payment_time = payment_time;
    }

    public String getMute() {
        return mute;
    }

    public void setMute(String mute) {
        this.mute = mute;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public String getEnableDisablePushNotification() {
        return enableDisablePushNotification;
    }

    public void setEnableDisablePushNotification(String enableDisablePushNotification) {
        this.enableDisablePushNotification = enableDisablePushNotification;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getFriend() {
        return Friend;
    }

    public void setFriend(String friend) {
        Friend = friend;
    }

    public String getCountPageFriend() {
        return countPageFriend;
    }

    public void setCountPageFriend(String countPageFriend) {
        this.countPageFriend = countPageFriend;
    }

    public String getTotalNotification() {
        return totalNotification;
    }

    public void setTotalNotification(String totalNotification) {
        this.totalNotification = totalNotification;
    }

    public String getCallStatus() {
        return callStatus;
    }

    public void setCallStatus(String callStatus) {
        this.callStatus = callStatus;
    }

    public String getCallTime() {
        return callTime;
    }

    public void setCallTime(String callTime) {
        this.callTime = callTime;
    }

    public String getCallType() {
        return callType;
    }

    public void setCallType(String callType) {
        this.callType = callType;
    }

    public String getReqId() {
        return reqId;
    }

    public void setReqId(String reqId) {
        this.reqId = reqId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }
}
