package com.evervolv.EVParts.Preferences;

import com.evervolv.EVParts.R;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceScreen;
import android.provider.Settings;
import android.util.Log;

public class AdvancedPrefs  extends PreferenceActivity {
	
	private static final String TETHER_ON_PLUGIN = "pref_tether_on_plugin";
    private static final String GENERAL_CATEGORY = "pref_category_general_advanced";
	
	private CheckBoxPreference mTetherOnPlugin;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {	
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.advanced_prefs);
		PreferenceScreen prefSet = getPreferenceScreen();
		
		PreferenceCategory generalCategory = (PreferenceCategory) prefSet
		.findPreference(GENERAL_CATEGORY);
		
		mTetherOnPlugin = (CheckBoxPreference)prefSet.findPreference(TETHER_ON_PLUGIN);
		mTetherOnPlugin.setChecked(Settings.System.getInt(getContentResolver(), 
						Settings.System.TETHER_ON_PLUGIN, 0) == 1);

		
    }	
	
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        boolean value;
     
        if (preference == mTetherOnPlugin) {
    		value = mTetherOnPlugin.isChecked();
    	    	Settings.System.putInt(getContentResolver(), 
    	    			Settings.System.TETHER_ON_PLUGIN, value ? 1 : 0);
        }
        
        return true;
    }
	
}
