package com.example.triggerbusters;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

public class SetPreferencesActivity extends PreferenceActivity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);
	}

}
