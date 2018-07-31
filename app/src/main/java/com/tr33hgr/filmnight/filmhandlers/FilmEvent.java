package com.tr33hgr.filmnight.filmhandlers;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class FilmEvent extends Film implements Serializable {
    @SerializedName("Plot")
    private String plot;

    @SerializedName("Genre")
    private String genre;

    @SerializedName("Language")
    private String language;

    private FilmEvent(String id, String title, String year, String posterURL, String plot, String genre, String language) {
        super(id, title, year, posterURL);

        this.plot = plot;
        this.genre = genre;
        this.language = language;
    }
}
