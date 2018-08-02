package com.tr33hgr.filmnight;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.tr33hgr.filmnight.filmhandlers.FilmEvent;

public class EventCreateActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_create);

        Intent intent = getIntent();
        FilmEvent filmEvent = intent.getParcelableExtra("SELECTED_FILM");

        LinearLayout containerLayout = findViewById(R.id.eventCreate_container_layout);

        TextView filmName = findViewById(R.id.eventCreate_filmTitle_txt);
        TextView filmYear = findViewById(R.id.eventCreate_filmYear_txt);
        TextView filmLanguage = findViewById(R.id.eventCreate_filmLanguage_txt);
        TextView filmGenre = findViewById(R.id.eventCreate_filmGenre_txt);
        TextView filmPlot = findViewById(R.id.eventCreate_filmPlot_txt);
        ImageView posterView = findViewById(R.id.eventCreate_poster_img);

        filmPlot.setMovementMethod(new ScrollingMovementMethod());

        RequestOptions options = new RequestOptions().error((R.mipmap.ic_launcher));
        Glide.with(posterView.getContext()).load(filmEvent.getPosterURL()).apply(options).into(posterView);

        filmName.setText(filmEvent.getTitle());
        filmYear.setText(filmEvent.getYear());
        filmLanguage.setText(filmEvent.getLanguage());
        filmGenre.setText(filmEvent.getGenre());
        filmPlot.setText(filmEvent.getPlot());
    }
}

