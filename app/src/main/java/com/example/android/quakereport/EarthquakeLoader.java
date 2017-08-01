package com.example.android.quakereport;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.List;

/**
 * Created by Manoj on 12/31/2016.
 */

public class EarthquakeLoader extends AsyncTaskLoader<List<Earthquake>> {

    private static final String LOG_TAG = EarthquakeLoader.class.getName();
    private String mUrl;

    public EarthquakeLoader(Context context, String mUrl) {
        super(context);
        this.mUrl = mUrl;
    }

    @Override
    protected void onStartLoading() {
        Log.i(LOG_TAG, " inside onStartLoading of loader class");
       forceLoad();
    }

    @Override
    public List<Earthquake> loadInBackground() {
        Log.i(LOG_TAG, " inside loadInBackground of loader class");
        if(mUrl == null)
            return null;

        List<Earthquake> earthquakes = QueryUtils.fetchEarthquakeData(mUrl);
        return earthquakes;
    }
}
