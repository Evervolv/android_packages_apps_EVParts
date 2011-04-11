package com.evervolv.EVParts.Preferences;


import com.evervolv.EVParts.R;
import com.evervolv.EVParts.R.bool;
import com.evervolv.EVParts.R.xml;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.EditTextPreference;
import android.preference.PreferenceScreen;
import android.preference.Preference.OnPreferenceChangeListener;
import android.widget.Toast;
import android.util.Log;
import android.provider.Settings;

public class InterfacePrefs extends PreferenceActivity implements OnPreferenceChangeListener {

    private static final String USE_SCREENOFF_ANIM = "pref_use_screenoff_anim";
    private static final String USE_SCREENON_ANIM = "pref_use_screenon_anim";
    private static final String BATTERY_STYLE = "pref_battery_style";
    private static final String HIDE_CLOCK_PREF = "pref_hide_clock";
    private static final String AM_PM_PREF = "pref_hide_ampm";
    private static final String USE_TRANSPARENT_STATUSBAR = "pref_use_transparent_statusbar";
    private static final String TRACKBALL_WAKE_PREF = "pref_trackball_wake";
    private static final String GENERAL_CATEGORY = "pref_category_general_interface";
    private static final String STATUSBAR_CATEGORY = "pref_category_statusbar_interface";
    
    private CheckBoxPreference mTrackballWakePref;
    private CheckBoxPreference mHideClock;
    private CheckBoxPreference mHideAmPm;
    private CheckBoxPreference mUseScreenOnAnim;
    private CheckBoxPreference mUseScreenOffAnim;
    private CheckBoxPreference mUseTransparentStatusBar;
    private ListPreference mBatteryOption;
    
    private static final String TAG = "EVParts";
    private static final boolean DEBUG = false;
	@Override
    public void onCreate(Bundle savedInstanceState) {	
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.interface_prefs);
		PreferenceScreen prefSet = getPreferenceScreen();
		
		PreferenceCategory generalCategory = (PreferenceCategory) prefSet
		.findPreference(GENERAL_CATEGORY);
		
		mUseScreenOnAnim = (CheckBoxPreference)prefSet.findPreference(USE_SCREENON_ANIM);
		mUseScreenOnAnim.setChecked(Settings.System.getInt(getContentResolver(), 
						Settings.System.USE_SCREENON_ANIM, 0) == 1);
		
		mUseScreenOffAnim = (CheckBoxPreference)prefSet.findPreference(USE_SCREENOFF_ANIM);
		mUseScreenOffAnim.setChecked(Settings.System.getInt(getContentResolver(), 
						Settings.System.USE_SCREENOFF_ANIM, 0) == 1);		
		
		mBatteryOption = (ListPreference) prefSet.findPreference(BATTERY_STYLE);
		mBatteryOption.setOnPreferenceChangeListener(this);

		mUseTransparentStatusBar = (CheckBoxPreference)prefSet.findPreference(USE_TRANSPARENT_STATUSBAR);
		mUseTransparentStatusBar.setChecked(Settings.System.getInt(getContentResolver(), 
						Settings.System.USE_TRANSPARENT_STATUSBAR, 1) == 1);	
		
		mHideClock = (CheckBoxPreference) prefSet.findPreference(HIDE_CLOCK_PREF);
		mHideClock.setChecked(Settings.System.getInt(getContentResolver(), 
						Settings.System.HIDE_CLOCK, 0) == 1);
		mHideAmPm = (CheckBoxPreference) prefSet.findPreference(AM_PM_PREF);
		mHideAmPm.setChecked(Settings.System.getInt(getContentResolver(), 
						Settings.System.HIDE_CLOCK_AMPM, 0) == 1);


		        /* Trackball Wake */
    	mTrackballWakePref = (CheckBoxPreference) prefSet.findPreference(TRACKBALL_WAKE_PREF);
    	mTrackballWakePref.setChecked(Settings.System.getInt(getContentResolver(),
            			Settings.System.TRACKBALL_WAKE_SCREEN, 0) == 1);
    	
		if (!getResources().getBoolean(R.bool.has_trackball)) {
			generalCategory.removePreference(mTrackballWakePref);
		}
		
    	if (mHideClock.isChecked()) {
    		mHideAmPm.setEnabled(false);
    	} else if (!mHideClock.isChecked()) {
    		mHideAmPm.setEnabled(true);
    	}
		
    }
	
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        boolean value;
        
        if (preference == mHideClock) {
        	value = mHideClock.isChecked();
    	    	Settings.System.putInt(getContentResolver(), Settings.System.HIDE_CLOCK, value ? 1 : 0);
    	    	
    	    	if (value) {
    	    		mHideAmPm.setEnabled(false);
    	    	} else if (!value) {
    	    		mHideAmPm.setEnabled(true);
    	    	}
    	    	
    	} else if (preference == mHideAmPm) {
    		value = mHideAmPm.isChecked();
    	    	Settings.System.putInt(getContentResolver(), Settings.System.HIDE_CLOCK_AMPM, value ? 1 : 0);
    	} else if (preference == mUseTransparentStatusBar) {
    		value = mUseTransparentStatusBar.isChecked();
    	    	Settings.System.putInt(getContentResolver(), Settings.System.USE_TRANSPARENT_STATUSBAR, value ? 1 : 0);
    	} else if (preference == mUseScreenOnAnim) {
    		value = mUseScreenOnAnim.isChecked();
            	Settings.System.putInt(getContentResolver(), Settings.System.USE_SCREENON_ANIM, value ? 1 : 0);
        } else if (preference == mUseScreenOffAnim) {
        	value = mUseScreenOffAnim.isChecked();
            	Settings.System.putInt(getContentResolver(), Settings.System.USE_SCREENOFF_ANIM, value ? 1 : 0);
        } else if (preference == mTrackballWakePref) {
            value = mTrackballWakePref.isChecked();
            	Settings.System.putInt(getContentResolver(), Settings.System.TRACKBALL_WAKE_SCREEN, value ? 1 : 0);
        }
        
        return true;
    }
    
    public boolean onPreferenceChange(Preference preference, Object objValue) {
        if (preference == mBatteryOption) {
        	Settings.System.putInt(getContentResolver(), Settings.System.BATTERY_STYLE, Integer.valueOf((String) objValue));
        }
        return true;
    }
    

}
