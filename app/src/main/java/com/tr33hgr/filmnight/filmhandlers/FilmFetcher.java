package com.tr33hgr.filmnight.filmhandlers;

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
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

public class FilmFetcher {
    //constants for OMDb API search
    private String API_URL = "http://www.omdbapi.com/?";
    private String API_KEY = "&apikey=3e0fcb90";
    private String TYPE_COMMAND = "&type=movie";
    private String FORMAT_COMMAND = "&r=json";
    private String PLOT_LENGTH_COMMAND = "&plot=full";
    private String SEARCH_COMMAND = "s=";
    private String PAGE_COMMAND = "&page=";
    private String SEARCH_BY_ID = "i=";

    private Context context;

    private RequestQueue requestQueue;

    private Film[] filmList;
    private FilmEvent selectedFilm;

    private Gson parser;

    public FilmFetcher(Context context){

        //context from which this class was called
        this.context = context;

        //instantiate JSON parser
        parser = new GsonBuilder().create();

        //setup queue of queries for OMDb API
        setupRequestQueue();
    }

    private void setupRequestQueue(){
        //instantiate cache, 1MB cap
        Cache cache = new DiskBasedCache(context.getCacheDir(), 1024 * 1024);

        //setup the network to use HttpURLConnection as the HTTP client
        Network network = new BasicNetwork(new HurlStack());

        //instantiate request queue of queries for OMDb API
        requestQueue = new RequestQueue(cache, network);

        requestQueue.start();
    }

    //formats general search query before starting search
    public void searchQuery(String query, int page){
        //replace all space with +
        if(query.contains(" ")){

            query = query.replace(' ', '+');
        }

        makeRequest(SEARCH_COMMAND, query, page);
    }

    public void searchSelectedFilm(String id) {
        //make a search request based on IMDb id, page = 0 as no pages expected
        makeRequest(SEARCH_BY_ID, id, 0);
    }

    private void makeRequest(String command, String query, int page){

        //start building API search url from type of search command, and the search query
        String url = API_URL + command + query;

        if(page != 0) {//page != 0 if general search query
            url+=PAGE_COMMAND + Integer.toString(page);//add page to be requested
        }else{// specific film search
            url+=PLOT_LENGTH_COMMAND;//add length of plot to be requested
        }
        //limit to only requesting movies, search results being in JSON format, and add API key
        url+=TYPE_COMMAND + FORMAT_COMMAND + API_KEY;

        Log.d("URL CREATED", url);

        //make search request
        JsonObjectRequest request = objectRequest(url, command);

        //tag with context
        request.setTag(this);

        //add request to queue
        requestQueue.add(request);

    }

    private JsonObjectRequest objectRequest(String url, final String command){

        //return a request to GET from the url, and respond as below
        return new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>(){

            @Override
            public void onResponse(JSONObject response){

                try {
                    if (command == SEARCH_COMMAND) {//if doing a general search
                        //get the array marked response from the received JSON object
                        JSONArray responseArray = response.getJSONArray("Search");

                        if (responseArray.length() > 0) {//if there are object in the array
                            //parse and create array of Films
                            filmList = parser.fromJson(responseArray.toString(), Film[].class);
                        }

                        Log.d("VOLLEY RECEIVE", "request complete: films " + filmList[1].getTitle());

                    } else if (command == SEARCH_BY_ID) {//if specific film search
                        //parse and create FilmEvent
                        selectedFilm = parser.fromJson(response.toString(), FilmEvent.class);

                        Log.d("FILM VOLLEY RECEIVE", selectedFilm.getTitle() + response.toString());
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }



            }

        }, new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError error){
                Log.d("VOLLEY ERROR", "Failed request");
            }

        });
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

    public ArrayList<Film> getFilmList(){
        //return list of films in ArrayList format
        return new ArrayList<Film>(Arrays.asList(filmList));
    }

    public FilmEvent getSelectedFilm(){
        return selectedFilm;
    }
}
