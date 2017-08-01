package com.example.android.quakereport;

import android.content.SharedPreferences;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
    }

    public static class EarthquakePreferenceFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.settings_main);
            Preference minMag = findPreference(getString(R.string.settings_min_magnitude_key));
            Preference minLimit = findPreference(getString(R.string.settings_min_limit_key));
            Preference orderBy = findPreference(getString(R.string.settings_order_by_key));

            bindPreferenceSummaryToValue(minMag);
            bindPreferenceSummaryToValue(minLimit);
            bindPreferenceSummaryToValue(orderBy);
        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object o) {
            String str = o.toString();
            //preference.setSummary(str);   // 1 way (THIS WILL DISPLAY word 'TIME' instead of  label in sort by setting )
            //return true;

            // CAN SKIP THIS IF CONDITION AND INSTEAD DIRECTLY EXECUTE ABOVE 2 COMMENTED LINES
            if (preference instanceof ListPreference) {
                ListPreference listPreference = (ListPreference) preference;
                int prefIndex = listPreference.findIndexOfValue(str);
                if (prefIndex >= 0) {
                    CharSequence[] labels = listPreference.getEntries();
                    preference.setSummary(labels[prefIndex]);   // 2nd WAY (THIS WILL DISPLAY word ' MOST RECENT' in sort by settings option )
                }
            } else {
                preference.setSummary(str);
            }
            return true;
        }

        private void bindPreferenceSummaryToValue(Preference pref) {
            pref.setOnPreferenceChangeListener(this);
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(pref.getContext());
            String preferenceString = preferences.getString(pref.getKey(), "");
            onPreferenceChange(pref, preferenceString);
        }
    }
}