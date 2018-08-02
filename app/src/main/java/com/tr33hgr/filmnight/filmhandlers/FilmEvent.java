package com.tr33hgr.filmnight.filmhandlers;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class FilmEvent extends Film implements Parcelable {
    @SerializedName("Plot")
    private String plot;

    @SerializedName("Genre")
    private String genre;

    @SerializedName("Language")
    private String language;

    //creator to allow class to be parceled and then un-parceled
    public static final Creator<FilmEvent> CREATOR = new Creator<FilmEvent>() {
        @Override
        public FilmEvent createFromParcel(Parcel in) {
            return new FilmEvent(in);
        }

        @Override
        public FilmEvent[] newArray(int size) {
            return new FilmEvent[size];
        }
    };

    //main constructor, uses ArrayList to be compatible with parcelable
    private FilmEvent(ArrayList<String> params) {
        //ArrayList order comes from writeToParcel
        //0 = id, 1 = title, 2 = year, 3 = posterURL
        super(params.get(0), params.get(1), params.get(2), params.get(3));
        //4 = plot, 5 = genre, 6 = language
        this.plot = params.get(4);
        this.genre = params.get(5);
        this.language = params.get(6);
    }

    //parcelable constructor
    private FilmEvent(Parcel in) {
        this(in.createStringArrayList());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    //creates parcel from class
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        List<String> parcelParams = new ArrayList<>();//ArrayList storing all info
        //add all info to ArrayList, order is important and reflected in main constructor
        parcelParams.add(id);
        parcelParams.add(title);
        parcelParams.add(year);
        parcelParams.add(posterURL);
        parcelParams.add(plot);
        parcelParams.add(genre);
        parcelParams.add(language);

        //add ArrayList to parcel
        parcel.writeStringList(parcelParams);
    }

    public String getPlot(){
        return this.plot;
    }

    public String getGenre(){
        return this.genre;
    }

    public String getLanguage(){
        return this.language;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public void setYear(String year){
        this.year = year;
    }

    public void setPosterURL(String url){
        this.posterURL = url;
    }

    public void setPlot(String plot){
        this.plot = plot;
    }

    public void setGenre(String genre){
        this.genre = genre;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

}
