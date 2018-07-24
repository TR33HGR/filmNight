package com.tr33hgr.filmnight;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.tr33hgr.filmnight.filmhandlers.Film;
import com.tr33hgr.filmnight.filmhandlers.FilmFetcher;
import com.tr33hgr.filmnight.viewHandlers.CustomAdapter;

import java.util.List;

public class SearchFilmActivity extends AppCompatActivity {

    TextView output;

    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView recyclerView;

    FilmFetcher filmFetcher;

    private List<Film> filmList;

    @Override
    public void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_search_film);

        output = findViewById(R.id.outputView);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        filmFetcher = new FilmFetcher(this);

        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {

        super.onNewIntent(intent);

        handleIntent(intent);
    }

    private void handleIntent(Intent intent){

        if(Intent.ACTION_SEARCH.equals(intent.getAction())){

            String query = intent.getStringExtra(SearchManager.QUERY);
            Log.d("RECEIVED", query);

            filmFetcher.searchQuery(query);
        }else{
            Log.d("SEARCH ERROR", "Error with getting query");
        }

        filmFetcher.getRequestQueue().addRequestFinishedListener(new RequestQueue.RequestFinishedListener(){

            @Override
            public void onRequestFinished(Request request) {
                filmList = filmFetcher.getFilmList();

                Log.d("COLLECTED FILM LIST", filmFetcher.getFilmList().get(1).getTitle());

                putFilmsInView();
            }
        });

    }

    private void putFilmsInView(){
        adapter = new CustomAdapter(filmList);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onStop() {

        super.onStop();

        filmFetcher.stop();
    }
}
