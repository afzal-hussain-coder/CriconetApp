package com.pb.criconet.Utills;

import java.util.ArrayList;

public class NameSingleton {
    private static NameSingleton ourInstance;
    private ArrayList<String> list = null;
    public static NameSingleton getInstance() {
        if(ourInstance == null)
            ourInstance = new NameSingleton();
        return ourInstance;
    }

    private NameSingleton() {
        list = new ArrayList<>();
    }
    // retrieve array from anywhere
    public ArrayList<String> getArray() {
        return this.list;
    }
    //Add element to array
    public void addToArray(String value) {
        list.add(value);
    }
    public void removeArray(String value){
        list.remove(value);
    }
}
