package com.tr33hgr.filmnight;

import android.content.Context;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.GeoPoint;

import java.util.Date;

public class User {

    private int YEAR = 0;
    private int MONTH = 1;
    private int DAY = 2;

    private LocationFetcher locationFetcher;

    private String name;
    private String email;
    private String dob;
    private Timestamp dobDate;
    private String houseNum;
    private String postCode;
    private GeoPoint addressLoc;

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getDob() {
        return dob;
    }

    public Timestamp getDobDate() {
        return dobDate;
    }

    public String getHouseNum() {
        return houseNum;
    }

    public String getPostCode() {
        return postCode;
    }

    public GeoPoint getAddressLoc() {
        return addressLoc;
    }

    public boolean getGender() {
        return gender;
    }

    private boolean gender;

    public User(){}

    User(Context context, String name, String email, String dob, String houseNum, String postCode, boolean gender){
        locationFetcher = new LocationFetcher(context);

        this.name = name;
        this.email = email;
        this.dob = dob;
        int[] date = formatDate(dob);
        this.dobDate = new Timestamp(new Date(date[YEAR], date[MONTH], date[DAY]));
        this.houseNum = houseNum;
        this.postCode = postCode;

        findAddressLoc(postCode);

        this.gender = gender;

    }

    private void findAddressLoc(String postCode){
        locationFetcher.queryLatLong(postCode);
    }

    private int[] formatDate(String dob) {

        if (dob.contains("/")) {

            dob = dob.replace("/", "");
        }

        int day = Integer.parseInt(dob.substring(0, 2));
        int month = Integer.parseInt(dob.substring(2, 4));
        int year = Integer.parseInt(dob.substring(4));

        int[] date = new int[3];

        date[YEAR] = year - 1900;
        date[MONTH] = month - 1;
        date[DAY] = day;

        return date;
    }

    @Exclude
    public LocationFetcher getLocationFetcher() {
        return locationFetcher;
    }

    public void setAddressLoc(GeoPoint addressLoc) {
        this.addressLoc = addressLoc;
    }
}
