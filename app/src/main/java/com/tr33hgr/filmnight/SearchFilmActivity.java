package com.tr33hgr.filmnight;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.tr33hgr.filmnight.filmhandlers.Film;
import com.tr33hgr.filmnight.filmhandlers.FilmFetcher;
import com.tr33hgr.filmnight.viewHandlers.CardAdapter;
import com.tr33hgr.filmnight.viewHandlers.OnBottomReachedListener;

import java.util.ArrayList;

public class SearchFilmActivity extends AppCompatActivity {

    private RecyclerView.Adapter adapter;
    private RecyclerView recyclerView;
    public static View.OnClickListener onCardSelectListener;

    //global reference to this context for protected intent
    private static Context context;

    private FilmFetcher filmFetcher;

    //stores all the films received from the general search
    private ArrayList<Film> filmList;

    //Holds search state of activity, default general film search (from previous activity)
    //to get list of possible films
    private boolean genSearch = true;

    private String query;//global reference to general film search query from previous activity

    //global variable for page to be received on general search, initialized to first page
    private int page = 1;

    //handles process when a film (card) is selected
    public class OnCardSelectListener implements View.OnClickListener{
        private final Context context;

        private OnCardSelectListener(Context context){
            this.context = context;
        }

        @Override
        public void onClick(View cardView){
            //change state of this activity to search for a specific film
            genSearch = false;

            getSelectedFilm(cardView);
        }

        private void getSelectedFilm(View cardView){
            //get selected film (card) position (index) in recycler view
            int selectedItemPosition = recyclerView.getChildAdapterPosition(cardView);

            Log.d("FILM SELECTED", filmList.get(selectedItemPosition).getTitle());

            //match selected film (card) to local list of films
            //search for selected film by IMDb id
            filmFetcher.searchSelectedFilm(filmList.get(selectedItemPosition).getId());
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_search_film);

        //setup recycler (scrollable) view containing all the results of the film search
        setupRecyclerView();

        //for use creating a protected intent
        this.context = this;

        setupFilmFetcher();

        //handle the search
        handleIntent(getIntent());
    }

    private void setupRecyclerView(){
        recyclerView = findViewById(R.id.search_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    private void setupFilmFetcher(){
        //instantiate class that handles all the searching and creation of Film objects
        filmFetcher = new FilmFetcher(this);

        //setup listener for when search has been complete
        filmFetcher.getRequestQueue().addRequestFinishedListener(new RequestQueue.RequestFinishedListener(){
            @Override
            public void onRequestFinished(Request request) {
                if (genSearch == true){//if still in general search state
                    if(filmList == null){//if this is the first search
                        filmList = filmFetcher.getFilmList();

                        putFilmsInView();//put films into recyclerView

                    // if this is not the first search, and we don't receive the same list again
                    //(meaning we have already received every film)
                    }else if(filmList != filmFetcher.getFilmList()){

                        //get the list position the new page will be added at (the last position)
                        int addedPosition = filmList.size();
                        //get how many are going to be added
                        int addedCount = filmFetcher.getFilmList().size();
                        //add to local film list
                        filmList.addAll(filmFetcher.getFilmList());
                        //tell the adapter how many have been added and where
                        adapter.notifyItemRangeInserted(addedPosition, addedCount);
                    }

                    Log.d("COLLECTED FILM LIST", filmList.get(1).getTitle());

                }else{//if we are in the specific film search state from a film (card) being selected

                    Log.d("COLLECTED SELECTED FILM", filmFetcher.getSelectedFilm().getTitle());

                    //instantiate intent to start EventCreateActivity
                    Intent startFormActivity = new Intent(context, EventCreateActivity.class);
                    //give intent the specific film searched
                    startFormActivity.putExtra("SELECTED_FILM",filmFetcher.getSelectedFilm());
                    //start the EventCreateActivity
                    startActivity(startFormActivity);
                }
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {

        super.onNewIntent(intent);

        handleIntent(intent);
    }

    private void handleIntent(Intent intent){

        if(Intent.ACTION_SEARCH.equals(intent.getAction())){//if a search has happened

            query = intent.getStringExtra(SearchManager.QUERY);//get search query
            Log.d("RECEIVED", query);

            filmFetcher.searchQuery(query, page);//perform search

        }else{
            Log.d("SEARCH ERROR", "Error with getting query");
        }
    }

    private void putFilmsInView(){
        setupCardAdapter();
    }

    private void setupCardAdapter(){
        //give the adapter the local filmList
        adapter = new CardAdapter(filmList);

        //for film (card) adapter related to recyclerView, get search results from next page when
        //reached bottom of current results
        ((CardAdapter) adapter).setOnBottomReachedListener(new OnBottomReachedListener() {
            @Override
            public void onBottomReached() {
                page++;

                if(query != null){//if a general search has happened
                    filmFetcher.searchQuery(query, page);
                }
            }
        });

        //set this activity to listen for a film (card) being selected
        onCardSelectListener = new OnCardSelectListener(this);

        //link the adapter to the recyclerView
        recyclerView.setAdapter(adapter);
    }

    //when the activity ends
    @Override
    protected void onStop() {

        super.onStop();
        //stop the film fetcher
        filmFetcher.stop();
    }
}
