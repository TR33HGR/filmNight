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

    private FilmEvent(ArrayList<String> params) {
        super(params.get(0), params.get(1), params.get(2), params.get(3));

        this.plot = params.get(4);
        this.genre = params.get(5);
        this.language = params.get(6);
    }

    private FilmEvent(Parcel in) {
        this(in.createStringArrayList());
    }

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        List<String> parcelParams = new ArrayList<>();
        parcelParams.add(id);
        parcelParams.add(title);
        parcelParams.add(year);
        parcelParams.add(posterURL);
        parcelParams.add(plot);
        parcelParams.add(genre);
        parcelParams.add(language);

        parcel.writeStringList(parcelParams);
    }
}
