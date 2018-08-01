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
import java.util.List;

public class FilmFetcher {

    private Context context;

    private RequestQueue requestQueue;

    private String API_URL = "http://www.omdbapi.com/?";
    private String API_KEY = "&apikey=3e0fcb90";
    private String TYPE_COMMAND = "&type=movie";
    private String FORMAT_COMMAND = "&r=json";
    private String PLOT_LENGTH_COMMAND = "&plot=full";
    private String SEARCH_COMMAND = "s=";
    private String PAGE_COMMAND = "&page=";
    private String SEARCH_BY_ID = "i=";

    private Film[] filmList;
    private FilmEvent selectedFilm;

    private Gson parser;

    public FilmFetcher(Context context){

        this.context = context;

        GsonBuilder gsonBuilder = new GsonBuilder();
        parser = gsonBuilder.create();

        setupRequestQueue();
    }

    private void setupRequestQueue(){
        Cache cache = new DiskBasedCache(context.getCacheDir(), 1024 * 1024);//1MB cap

        //setup the network to use HttpURLConnection as the HTTP client
        Network network = new BasicNetwork(new HurlStack());

        requestQueue = new RequestQueue(cache, network);

        requestQueue.start();


    }

    public void searchQuery(String query, int page){

        if(query.contains(" ")){

            query = query.replace(' ', '+');
        }

        makeRequest(SEARCH_COMMAND, query, page);
    }

    public String test;

    private void makeRequest(String command, String query, int page){

        String url = API_URL + command + query;

        if(page != 0) {
            url+=PAGE_COMMAND + Integer.toString(page);
        }else{
            url+=PLOT_LENGTH_COMMAND;
        }

        url+=TYPE_COMMAND + FORMAT_COMMAND + API_KEY;

        Log.d("URL CREATED", url);

        JsonObjectRequest request = objectRequest(url, command);

        request.setTag(this);

        requestQueue.add(request);

    }

    private JsonObjectRequest objectRequest(String url, final String command){

        return new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>(){

            @Override
            public void onResponse(JSONObject response){

                test = response.toString();
                try {
                    if (command == SEARCH_COMMAND) {
                        JSONArray responseArray = response.getJSONArray("Search");

                        if (responseArray.length() > 0) {
                            filmList = parser.fromJson(responseArray.toString(), Film[].class);
                        }

                        Log.d("VOLLEY RECEIVE", "request complete: films " + filmList[1].getTitle());

                    } else if (command == SEARCH_BY_ID) {
                        //selectedFilm = parser.fromJson(response.getJSONObject("data").toString(), Film.class);
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

                test = "ERROR";

                Log.d("VOLLEY ERROR", "Failed request");
            }

        });
    }

    public void stop(){

        if(requestQueue != null){

            requestQueue.cancelAll(this);
        }
    }

    public RequestQueue getRequestQueue() {
        return requestQueue;
    }

    public ArrayList<Film> getFilmList(){
        return new ArrayList<Film>(Arrays.asList(filmList));
    }

    public FilmEvent getSelectedFilm(){
        return selectedFilm;
    }

    public void searchSelectedFilm(String id) {
        makeRequest(SEARCH_BY_ID, id, 0);
    }
}
