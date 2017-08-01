package com.example.android.quakereport;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Manoj on 12/30/2016.
 */

public class QueryUtils {

    public static final String LOG_TAG = QueryUtils.class.getSimpleName();

    private QueryUtils() {
    }

    public static List<Earthquake> fetchEarthquakeData(String str) {

        URL url = createUrl(str);

        String jsonResponse = null;

        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        List<Earthquake> earthquakes = extractFeatureFromJson(jsonResponse);
        return earthquakes;
    }

    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException exception) {
            Log.e(LOG_TAG, "Error with creating URL", exception);
            return null;
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        if (url == null) {
            return jsonResponse;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
               // Log.d(LOG_TAG,"make HTTP REQUEST");
            } else {
                Log.e(LOG_TAG, "Error IN RESPONSE code: " + urlConnection.getResponseCode());

            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static List<Earthquake> extractFeatureFromJson(String earthquakeJSON) {

        //for loading progress bar.
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (TextUtils.isEmpty(earthquakeJSON))
            return null;

        List<Earthquake> earthquakes = new ArrayList<>();
        try {

            JSONObject jsonObj = new JSONObject(earthquakeJSON);
            JSONArray jsonArrObj = jsonObj.optJSONArray("features");

            for (int i = 0; i < jsonArrObj.length(); i++)
            {
                JSONObject c = jsonArrObj.getJSONObject(i);
                JSONObject prop = c.getJSONObject("properties");

                double mag = prop.getDouble("mag");
                String location = prop.getString("place");
                long time = prop.getLong("time");
                String url = prop.getString("url");


                Earthquake earthquake = new Earthquake(mag,location, time, url);
                earthquakes.add(earthquake);
            }
        } catch (JSONException e) {
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }
        return earthquakes;
    }
}
