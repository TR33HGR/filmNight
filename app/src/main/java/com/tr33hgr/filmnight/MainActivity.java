package com.tr33hgr.filmnight;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SearchView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView filmSearchView = findViewById(R.id.filmSearch);
        ComponentName searchFilmActivity = new ComponentName(this, SearchFilmActivity.class);
        if (searchManager != null) {
            filmSearchView.setSearchableInfo(searchManager.getSearchableInfo(searchFilmActivity));
        }else{
            Log.d("Null Error", "Null SearchManager");
        }

    }


}
