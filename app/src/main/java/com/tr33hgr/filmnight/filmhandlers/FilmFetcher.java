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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

public class FilmFetcher {

    private Context context;

    private RequestQueue requestQueue;

    private String API_URL = "http://www.omdbapi.com/?";
    private String API_KEY = "&apikey=3e0fcb90";
    private String SEARCH_COMMAND = "s=";
    private String SEARCH_BY_ID = "i=";

    private List<Film> filmList;
    private Film selectedFilm;

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

    public void searchQuery(String query){

        if(query.contains(" ")){

            query = query.replace(' ', '+');
        }

        makeRequest(SEARCH_COMMAND, query);
    }

    public String test;

    private void makeRequest(String command, String query){

        String url = API_URL + command + query + API_KEY;

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
                            filmList = Arrays.asList(parser.fromJson(responseArray.toString(), Film[].class));
                        }

                        Log.d("VOLLEY RECEIVE", "request complete: films " + filmList.get(1).getTitle());
                    } else if (command == SEARCH_BY_ID) {
                        //selectedFilm = parser.fromJson(response.getJSONObject("data").toString(), Film.class);
                        selectedFilm = parser.fromJson(response.toString(), Film.class);

                        Log.d("VOLLEY RECEIVE", "request complete: single film" + selectedFilm.getTitle());
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
}
