package com.tr33hgr.filmnight;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class SearchFilmActivity extends AppCompatActivity {

    TextView output;

    String API_URL = "http://www.omdbapi.com/?";
    String API_KEY = "&apikey=3e0fcb90";
    String SEARCH_COMMAND = "s=";
    String SEARCH_BY_ID = "i=";

    RequestQueue queue;
    String sumString;

    @Override
    public void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);

        setContentView(R.layout.search);

        output = (TextView) findViewById(R.id.outputView);

        queue = Volley.newRequestQueue(this);

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

            if(query.contains(" ")){
                query = query.replace(' ', '+');
            }

            makeRequest(query);
        }else{
            Log.d("SEARCH ERROR", "Error with getting query");
        }

        if(sumString != null){
            Log.d("RECEIVED", sumString);
        }
    }

    private void makeRequest(String query){
        String url = API_URL + SEARCH_COMMAND + query + API_KEY;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>(){
            @Override
            public void onResponse(String response){
                sumString = response;
                output.setText(sumString);
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
                Log.d("VOLLEY ERROR", "Failed request");
            }
        });

        stringRequest.setTag(this);

        queue.add(stringRequest);
    }

    @Override
    protected void onStop() {
        super.onStop();

        if(queue != null){
            queue.cancelAll(this);
        }
    }
}
