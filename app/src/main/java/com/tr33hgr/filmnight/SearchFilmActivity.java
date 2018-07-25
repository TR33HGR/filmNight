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
import com.tr33hgr.filmnight.viewHandlers.OnBottomReachedListener;

import java.util.ArrayList;
import java.util.List;

public class SearchFilmActivity extends AppCompatActivity {

    TextView output;

    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView recyclerView;

    FilmFetcher filmFetcher;

    private ArrayList<Film> filmList;

    @Override
    public void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_search_film);

        output = findViewById(R.id.selFilmInstrTag);

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

    String query;
    int page = 1;

    private void handleIntent(Intent intent){

        if(Intent.ACTION_SEARCH.equals(intent.getAction())){

            query = intent.getStringExtra(SearchManager.QUERY);
            Log.d("RECEIVED", query);



            filmFetcher.searchQuery(query, page);
        }else{
            Log.d("SEARCH ERROR", "Error with getting query");
        }

        filmFetcher.getRequestQueue().addRequestFinishedListener(new RequestQueue.RequestFinishedListener(){

            @Override
            public void onRequestFinished(Request request) {
                if(filmList == null){
                    filmList = filmFetcher.getFilmList();

                    putFilmsInView();
                }else if(filmList != filmFetcher.getFilmList()){
                    int addedPosition = filmList.size();
                    int addedCount = filmFetcher.getFilmList().size();
                    filmList.addAll(filmFetcher.getFilmList());

                    adapter.notifyItemRangeInserted(addedPosition, addedCount);
                }


                Log.d("COLLECTED FILM LIST", filmList.get(1).getTitle());


            }


        });



    }

    private void putFilmsInView(){
        adapter = new CustomAdapter(filmList);
        recyclerView.setAdapter(adapter);

        ((CustomAdapter) adapter).setOnBottomReachedListener(new OnBottomReachedListener() {
            @Override
            public void onBottomReached(int position) {
                page++;

                if(query != null){
                    filmFetcher.searchQuery(query, page);
                }
            }
        });
    }

    @Override
    protected void onStop() {

        super.onStop();

        filmFetcher.stop();
    }
}
