package com.tr33hgr.filmnight.filmhandlers;

import com.google.gson.annotations.SerializedName;

public class Film {

    @SerializedName("imdbID")
    private String id;

    @SerializedName("Title")
    private String title;

    @SerializedName("Year")
    private String year;

    @SerializedName("Poster")
    private String posterURL;

    Film(String id, String title, String year, String posterURL){
        this.id = id;
        this.title = title;
        this.year = year;
        this.posterURL = posterURL;
    }

    public String getId(){
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getYear() {
        return year;
    }

    public String getPosterURL() {
        return posterURL;
    }
}
