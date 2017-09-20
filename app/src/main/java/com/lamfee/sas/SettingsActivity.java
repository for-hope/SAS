package com.lamfee.sas;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * Created by Lamine on 6/2/2017.
 */

public class SettingsActivity extends PreferenceActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

    }
}