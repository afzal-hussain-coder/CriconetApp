package com.pb.criconet.models;

// [START blog_user_class]
public class FBUser {

    public String username;
    public String email;
    public String id;

    public FBUser() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public FBUser(String username, String email, String id) {
        this.username = username;
        this.email = email;
        this.id = id;
    }

}
// [END blog_user_class]
