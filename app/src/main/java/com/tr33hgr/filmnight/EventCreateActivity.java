package com.tr33hgr.filmnight;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.tr33hgr.filmnight.filmhandlers.FilmEvent;

public class EventCreateActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_create);

        //TODO: https://developer.android.com/guide/components/activities/parcelables-and-bundles
        Intent intent = getIntent();
        FilmEvent filmEvent = (FilmEvent) intent.getParcelableExtra("SELECTED_FILM");

        EditText filmName = findViewById(R.id.formFilmNameText);
        EditText filmYear = findViewById(R.id.formYearText);
        EditText filmLanguage = findViewById(R.id.formLanguageText);
        EditText filmGenre = findViewById(R.id.formGenreText);
        EditText filmPlot = findViewById(R.id.formPlotText);

        ImageView posterView = findViewById(R.id.formPosterView);

        RequestOptions options = new RequestOptions().error((R.mipmap.ic_launcher));
        Glide.with(posterView.getContext()).load(filmEvent.getPosterURL()).apply(options).into(posterView);

        filmName.setText(filmEvent.getTitle());
        filmYear.setText(filmEvent.getYear());
        filmLanguage.setText(filmEvent.getLanguage());
        filmGenre.setText(filmEvent.getGenre());
        filmPlot.setText(filmEvent.getPlot());
    }
}

