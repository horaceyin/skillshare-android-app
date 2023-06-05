package com.randi_horace.skillshare;

public class Mission {

    private String title;
    private String desc;
    private String image;
    private String uid;
    private String latitude;
    private String longitude;

    public Mission(){

    }

    public Mission(String title, String desc, String image, String uid,String latitude, String longitude){
        this.title = title;
        this.desc = desc;
        this.image = image;
        this.uid = uid;
        this.latitude = latitude;
        this.longitude = longitude;
    }


    public String getImage(){
        return image;
    }

    public void setImage(String image){
        this.image = image;
    }

    public String getDesc(){
        return desc;
    }

    public void setDesc(String desc){
        this.desc = desc;
    }

    public String getTitle(){
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUid(){
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getLatitude(){
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude(){
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

}
