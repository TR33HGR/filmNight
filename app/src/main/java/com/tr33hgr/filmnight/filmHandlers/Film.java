package com.tr33hgr.filmnight.filmHandlers;

import com.google.gson.annotations.SerializedName;

public class Film {

    @SerializedName("imdbID")
    protected String id;

    @SerializedName("Title")
    protected String title;

    @SerializedName("Year")
    protected String year;

    @SerializedName("Poster")
    protected String posterURL;

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
