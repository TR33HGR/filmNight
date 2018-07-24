package com.tr33hgr.filmnight;

import android.content.Context;
import android.util.Log;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;

public class FilmFetcher {

    private Context context;

    private RequestQueue requestQueue;

    private String API_URL = "http://www.omdbapi.com/?";
    private String API_KEY = "&apikey=3e0fcb90";
    private String SEARCH_COMMAND = "s=";
    private String SEARCH_BY_ID = "i=";

    protected FilmFetcher(Context context){

        this.context = context;

        setupRequestQueue();
    }

    private void setupRequestQueue(){
        Cache cache = new DiskBasedCache(context.getCacheDir(), 1024 * 1024);//1MB cap

        //setup the network to use HttpURLConnection as the HTTP client
        Network network = new BasicNetwork(new HurlStack());

        requestQueue = new RequestQueue(cache, network);

        requestQueue.start();
    }

    public void searchQuery(String query){

        if(query.contains(" ")){

            query = query.replace(' ', '+');
        }

        makeRequest(SEARCH_COMMAND, query);
    }

    String test;

    private void makeRequest(String command, String query){


        String url = API_URL + command + query + API_KEY;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>(){

            @Override
            public void onResponse(String response){

                test = response;
                //TODO: parse response


                Log.d("VOLLEY RECEIVE", "request complete" + response);
            }

        }, new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError error){

                test = "ERROR";

                Log.d("VOLLEY ERROR", "Failed request");
            }

        });

        stringRequest.setTag(this);

        requestQueue.add(stringRequest);
    }

    public void stop(){

        if(requestQueue != null){

            requestQueue.cancelAll(this);
        }
    }

    public RequestQueue getRequestQueue() {
        return requestQueue;
    }
}
