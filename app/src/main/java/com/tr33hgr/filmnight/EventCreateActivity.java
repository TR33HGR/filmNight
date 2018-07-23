package com.tr33hgr.filmnight;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import java.util.Arrays;
import java.util.List;

public class EventCreateActivity extends AppCompatActivity{

    List<String> BASE_URL = Arrays.asList("http://www.omdbapi.com/?t=","&plot=full&r=xml");

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_event_create);
    }

    public void searchFilm(View view){

        EditText filmText = (EditText) findViewById(R.id.searchFilmName);
        String filmName = filmText.getText().toString();

        //for search formatting
        if(filmName.indexOf(' ') >= 0){ //if the film name contains a space character
            filmName = filmName.replace(' ', '+'); //replace with '+'
        };

        String searchURL = BASE_URL.get(0) + filmName + BASE_URL.get(1);
    }
}
