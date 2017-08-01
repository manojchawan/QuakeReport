    /*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.quakereport;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class EarthquakeActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Earthquake>>{

    public static final String LOG_TAG = EarthquakeActivity.class.getName();
    private static final int EARTHQUAKE_LOADER_ID = 1;

    private static final String SAMPLE_JSON_URL =
            "https://earthquake.usgs.gov/fdsnws/event/1/query";
    private EarthquakeAdapter mAdapter;
    private TextView emptyView;

    // FOR SETTINGS
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    // OPEN SETTINGS ON SELECTED ITEM
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.setting_menu_item) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public Loader<List<Earthquake>> onCreateLoader(int i, Bundle bundle) {
        Log.i(LOG_TAG, " inside onCreate LOADER");

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        String minMagnitude = sp.getString(getString(R.string.settings_min_magnitude_key), getString(R.string.settings_min_magnitude_default));
        String minLimit = sp.getString(getString(R.string.settings_min_limit_key), getString(R.string.settings_min_limit_default));
        String orderBy = sp.getString(getString(R.string.settings_order_by_key),    getString(R.string.settings_order_by_default));

        Uri baseUri = Uri.parse(SAMPLE_JSON_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("format", "geojson");
        uriBuilder.appendQueryParameter("limit", minLimit);
        uriBuilder.appendQueryParameter("minmag", minMagnitude);
        uriBuilder.appendQueryParameter("orderby", orderBy);

        return new EarthquakeLoader(this, uriBuilder.toString());  //CALLS CONSTRUCTOR AND FROM THERE BACKGROUND LOADER WITH CUSTOM URL
    }

    // GETS CALLED AFTER BACKGROUND PROCESS.
    @Override
    public void onLoadFinished(Loader<List<Earthquake>> loader, List<Earthquake> earthquakes) {
        Log.i(LOG_TAG, " inside onLoadFinished");

        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);

        mAdapter.clear();
        if (earthquakes != null && !earthquakes.isEmpty()) {
            mAdapter.addAll(earthquakes);
        }
        emptyView.setText(R.string.no_earthquakes);
    }

    @Override
    public void onLoaderReset(Loader<List<Earthquake>> loader) {
        Log.i(LOG_TAG, " inside onLoaderReset");
        mAdapter.clear();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);

        Log.i(LOG_TAG, "inside oncreate");


        ListView earthquakeListView = (ListView) findViewById(R.id.list);
        mAdapter = new EarthquakeAdapter(this, new ArrayList<Earthquake>());
        earthquakeListView.setAdapter(mAdapter);

        emptyView = (TextView) findViewById(R.id.empty_view);
        earthquakeListView.setEmptyView(emptyView);

        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                Earthquake currentEarthquake = mAdapter.getItem(position);
                Uri earthquakeUri = Uri.parse(currentEarthquake.getmUrl());
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, earthquakeUri);
                startActivity(websiteIntent);
            }
        });

        ConnectivityManager connMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(EARTHQUAKE_LOADER_ID, null, this); // <-- this CALLS ON_CREATE_LOADER
        }
        else {
            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);
            emptyView.setText(R.string.no_internet_connection);
        }

/*
        EarthquakeAsyncTask task = new EarthquakeAsyncTask();
        task.execute(SAMPLE_JSON_URL);
*/
    }
/*
    //2nd way
    private void updateUi(List<Earthquake> earthquakes) {

        final EarthquakeAdapter adapter = new EarthquakeAdapter(this, earthquakes);

        ListView earthquakeListView = (ListView) findViewById(R.id.list);
        earthquakeListView.setAdapter(adapter);

        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Earthquake currentEarthquake = adapter.getItem(i); // i = position
                Uri earthquakeUri = Uri.parse(currentEarthquake.getmUrl());
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, earthquakeUri); //Implicit INTENT
                startActivity(websiteIntent);
            }
        });

    }
*/

   /* private class EarthquakeAsyncTask extends AsyncTask<String, Void, List<Earthquake> >{

            @Override
            protected List<Earthquake> doInBackground(String... urls) {

                Log.i(LOG_TAG, " inside DoInBackground of async task");
                if(urls.length<1 || urls[0]==null)
                    return null;

                List<Earthquake> earthquakes = QueryUtils.fetchEarthquakeData(urls[0]);
                return earthquakes;
            }

            @Override
            protected void onPostExecute(List<Earthquake> earthquakes) {
                Log.i(LOG_TAG, "inside post execute ofasync task");

                if (earthquakes == null)
                    return;
                mAdapter.clear();

                if (earthquakes != null && !earthquakes.isEmpty())
                    mAdapter.addAll(earthquakes);

                // updateUi(earthquakes); //2nd way
            }
    }
*/
}
