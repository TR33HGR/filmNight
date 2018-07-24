package com.tr33hgr.filmnight;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.tr33hgr.filmnight.filmhandlers.FilmFetcher;

public class SearchFilmActivity extends AppCompatActivity {

    TextView output;

    FilmFetcher filmFetcher;

    @Override
    public void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);

        setContentView(R.layout.search);

        output = (TextView) findViewById(R.id.outputView);

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

        output.setText(filmFetcher.test);
    }

    @Override
    protected void onStop() {

        super.onStop();

        filmFetcher.stop();
    }
}
