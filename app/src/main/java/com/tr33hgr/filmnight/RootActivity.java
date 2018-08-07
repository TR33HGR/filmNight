package com.tr33hgr.filmnight;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.SearchView;

public class RootActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_root);


        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        //USER CREATING AN EVENT:
        //search film to be watched:

        //get reference to film search view
        SearchView filmSearchView = findViewById(R.id.root_filmSearch_srch);
        //create component that works as Intent to start the SearchFilmActivity
        ComponentName searchFilmActivity = new ComponentName(this, SearchFilmActivity.class);

        if (searchManager != null) {
            //launches Intent to start SearchFilmActivity on user commiting a film search (via filmSearchView)
            filmSearchView.setSearchableInfo(searchManager.getSearchableInfo(searchFilmActivity));
        }else{
            Log.d("Null Error", "Null SearchManager");
        }

    }
}
