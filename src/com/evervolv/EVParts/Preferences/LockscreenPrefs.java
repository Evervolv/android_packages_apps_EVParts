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
import android.preference.EditTextPreference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceScreen;
import android.preference.Preference.OnPreferenceChangeListener;
import android.widget.Toast;
import android.util.Log;
import android.provider.Settings;


public class LockscreenPrefs extends PreferenceActivity implements OnPreferenceChangeListener {
	
	private static final String CARRIER_CAP_PREF = "pref_carrier_caption";
	private static final String LOCKSCREEN_STYLE_PREF = "pref_lockscreen_style";
    private static final String TRACKBALL_UNLOCK_PREF = "pref_trackball_unlock";
    private static final String GENERAL_CATEGORY = "pref_lockscreen_general_category";
    
    private static final int LOCK_STYLE_TABS   = 1;
    private static final int LOCK_STYLE_ROTARY = 2;
    
	private EditTextPreference mCarrierCaption;
	private ListPreference mLockscreenStyle;
	private CheckBoxPreference mTrackballUnlockPref;

	private static final String TAG = "EVParts";
	private static final boolean DEBUG = false;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {	
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.lockscreen_prefs);
		PreferenceScreen prefSet = getPreferenceScreen();
		
		/* Lockscreen style */
		mLockscreenStyle = (ListPreference) prefSet.findPreference(LOCKSCREEN_STYLE_PREF);
		mLockscreenStyle.setOnPreferenceChangeListener(this);
		
		Log.d(TAG, "LockStyle: " + Integer.toString(Settings.System.getInt(getContentResolver(), 
				Settings.System.LOCKSCREEN_STYLE, 1)));
		
		/* Carrier caption */
		mCarrierCaption = (EditTextPreference)prefSet.findPreference(CARRIER_CAP_PREF);
		mCarrierCaption.setOnPreferenceChangeListener(this);

        /* Trackball Unlock */
        mTrackballUnlockPref = (CheckBoxPreference) prefSet.findPreference(TRACKBALL_UNLOCK_PREF);
        mTrackballUnlockPref.setChecked(Settings.System.getInt(getContentResolver(),
                Settings.System.TRACKBALL_UNLOCK_SCREEN, 0) == 1);
		PreferenceCategory generalCategory = (PreferenceCategory) prefSet
    	.findPreference(GENERAL_CATEGORY);
		
		if (!getResources().getBoolean(R.bool.has_trackball)) {
			if (DEBUG) Log.d(TAG, "does not have trackball!");
			generalCategory.removePreference(mTrackballUnlockPref);
		}
    }	
	
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        boolean value;
        
        if (preference == mTrackballUnlockPref) {
            value = mTrackballUnlockPref.isChecked();
            Settings.System.putInt(getContentResolver(), Settings.System.TRACKBALL_UNLOCK_SCREEN,
                    value ? 1 : 0);
            return true;
        }
        
        return true;
    }
	
    public boolean onPreferenceChange(Preference preference, Object objValue) {
        
        if (preference == mCarrierCaption) {
			Settings.System.putString(getContentResolver(),Settings.System.CARRIER_CAP, 
					objValue.toString());
			//Didn't i say i was learning?
            ActivityManager am = (ActivityManager)getSystemService(
                    Context.ACTIVITY_SERVICE);
            am.forceStopPackage("com.android.phone");
            return true;
        } else if (preference == mLockscreenStyle) {
        	Settings.System.putInt(getContentResolver(), Settings.System.LOCKSCREEN_STYLE, 
        			Integer.valueOf((String) objValue));
          //Didn't i say i was learning?
            //ActivityManager am = (ActivityManager)getSystemService(
            //        Context.ACTIVITY_SERVICE);
            //am.forceStopPackage("com.android.phone");
            return true;
        }
        return false;
    }
    
}
