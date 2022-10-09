package com.example.alertacan_android;

public class Dog {
    private String mDogName;
    private String mDogRace;
    private String mImageUrl;
    private String mId;

    public Dog() {

    }

    public Dog(String id, String dogName, String dogRace, String imageUrl) {
        mId = id;
        mDogName = dogName;
        mDogRace = dogRace;
        mImageUrl = imageUrl;
    }

    public String getId(){
        return mId;
    }

    public String getName(){
        return mDogName;
    }

    public void setName(String name){
        mDogName = name;
    }

    public String getRace(){
        return mDogRace;
    }

    public void setRace(String race){
        mDogRace = race;
    }

    public String getImageUrl(){
        return mImageUrl;
    }

    public void setImageUrl(String url){
        mImageUrl = url;
    }




}
