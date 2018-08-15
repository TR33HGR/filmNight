package com.tr33hgr.filmnight;

import android.content.Context;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public abstract class Fetcher {
    protected String API_URL;
    protected String API_KEY;
    protected String FORMAT_COMMAND;

    protected Context context;

    protected RequestQueue requestQueue;

    protected Gson parser;

    protected Fetcher(Context context){
        //context from which this class was called
        this.context = context;

        //instantiate JSON parser
        parser = new GsonBuilder().create();

        //setup queue of queries for OMDb API
        setupRequestQueue();
    }

    protected void setupRequestQueue(){
        //instantiate cache, 1MB cap
        Cache cache = new DiskBasedCache(context.getCacheDir(), 1024 * 1024);

        //setup the network to use HttpURLConnection as the HTTP client
        Network network = new BasicNetwork(new HurlStack());

        //instantiate request queue of queries for OMDb API
        requestQueue = new RequestQueue(cache, network);

        requestQueue.start();
    }

    public void stop(){
        //close request queue
        if(requestQueue != null){
            //close all requests tags with this context
            requestQueue.cancelAll(this);
        }
    }

    public RequestQueue getRequestQueue() {
        return requestQueue;
    }
}
