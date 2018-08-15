package com.tr33hgr.filmnight;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LocationFetcher extends Fetcher {
    private String API_URL = "https://maps.googleapis.com/maps/api/geocode/";
    private String API_KEY = "&key=AIzaSyDep2Ctl0mIc9OHJWzy6To_0ByKAAV-Ag4";
    private String FORMAT_COMMAND = "json";
    private String ADDRESS_COMMAND = "?address=";

    private Result[] result;

    public LocationFetcher(Context context){
        super(context);
    }

    public void queryLatLong(String postCode) {

        if(postCode.contains(" ")){

            postCode = postCode.replace(' ', '+');
        }

        String url = API_URL + FORMAT_COMMAND + ADDRESS_COMMAND + postCode + API_KEY;

        Log.d("URL CREATED", url);

        //make search request
        JsonObjectRequest request = objectRequest(url);

        //tag with context
        request.setTag(this);

        //add request to queue
        requestQueue.add(request);


    }

    private JsonObjectRequest objectRequest(String url){

        //return a request to GET from the url, and respond as below
        return new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //get the array marked response from the received JSON object
                try {
                    JSONArray responseArray = response.getJSONArray("results");

                    result = parser.fromJson(getUsefulInfo(responseArray.toString()), Result[].class);

                    Log.d("LOCATION VOLLEY RECEIVE", Double.toString(result[0].geometry.location.lat));
                } catch (JSONException e) {
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

    private String getUsefulInfo(String response) {
        int start = response.indexOf("address_components");
        int end = response.indexOf("formatted_address");

        String formatRseponse = response.substring(0,start) + response.substring(end);

        return formatRseponse;

    }

    public double[] getLatLong(){
        double[] latLong = new double[2];

        latLong[0] = result[0].geometry.location.lat;
        latLong[1] = result[0].geometry.location.lng;

        return latLong;
    }

    public class Result{
        @SerializedName("geometry")
        Geometry geometry;

        Result(Geometry geometry){
            this.geometry = geometry;
        }
    }

    public class Geometry{
        @SerializedName("location")
        Location location;

        Geometry(Location location){
            this.location = location;
        }
    }

    public class Location{
        @SerializedName("lat")
        double lat;

        @SerializedName("lng")
        double lng;

        Location(double lat, double lng){
            this.lat = lat;
            this.lng = lng;
        }
    }
}
