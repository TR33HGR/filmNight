package com.tr33hgr.filmnight;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.tr33hgr.filmnight.filmHandlers.FilmEvent;
import com.tr33hgr.filmnight.viewHandlers.FormAdapter;

public class EventCreateActivity extends AppCompatActivity{

    private RecyclerView.Adapter adapter;
    private RecyclerView recyclerView;

    FilmEvent filmEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_create);

        Intent intent = getIntent();
        filmEvent = intent.getParcelableExtra("SELECTED_FILM");

        setupRecyclerView();
    }

    private void setupRecyclerView(){
        recyclerView = findViewById(R.id.eventCreate_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        //give the adapter the local filmList
        adapter = new FormAdapter(filmEvent);

        //link the adapter to the recyclerView
        recyclerView.setAdapter(adapter);
    }
}

