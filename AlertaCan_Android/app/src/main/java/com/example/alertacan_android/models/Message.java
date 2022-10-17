package com.example.alertacan_android.models;

public class Message {
    private String mPhone;
    private String mAddress;
    private String mMessage;

    public Message(){}

    public Message(String phone, String address, String message){
        mPhone = phone;
        mAddress = address;
        mMessage = message;
    }

    public String getPhone(){ return mPhone; }

    public void setPhone(String phone){
        mPhone = phone;
    }

    public String getAddress(){
        return mAddress;
    }

    public void setAddress(String address){
        mAddress = address;
    }

    public String getMessage(){
        return mMessage;
    }

    public void setMessage(String message){
        mMessage = message;
    }


}
