package com.pb.criconet.models;

public class Drawer {

    private String title;
    private boolean counter;
    private int image;

    public Drawer(String title, boolean counter, int image) {
        this.title = title;
        this.counter = counter;
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isCounter() {
        return counter;
    }

    public void setCounter(boolean counter) {
        this.counter = counter;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
