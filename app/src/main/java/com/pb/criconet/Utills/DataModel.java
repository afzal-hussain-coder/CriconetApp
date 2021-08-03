package com.pb.criconet.Utills;

public class DataModel {
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name;

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    private String Id;

    public DataModel(String name){
        this.name=name;
    }
    public DataModel(String name,String Id){
        this.name=name;
        this.Id = Id;
    }

}
