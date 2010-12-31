package com.android.spare_parts;


import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.EditTextPreference;
import android.preference.PreferenceScreen;
import android.widget.Toast;
import android.util.Log;
import android.provider.Settings;


public class LockscreenParts extends PreferenceActivity
implements SharedPreferences.OnSharedPreferenceChangeListener {

	private static final String CARRIER_CAP = "carrier_caption";
	private static final String LOCKSCREEN_ROTARY_LOCK = "use_rotary_lockscreen";
	
	private EditTextPreference mCarrierCaption;
	private CheckBoxPreference mUseRotaryLockPref;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {	
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.lockscreen_prefs);
		PreferenceScreen prefSet = getPreferenceScreen();
		
		
		mUseRotaryLockPref = (CheckBoxPreference)prefSet.findPreference(LOCKSCREEN_ROTARY_LOCK);
		mUseRotaryLockPref.setChecked(Settings.System.getInt(getContentResolver(), Settings.System.USE_ROTARY_LOCKSCREEN, 0) != 0);		

		mCarrierCaption = (EditTextPreference) findPreference(CARRIER_CAP);

		getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }	
    
	
	public boolean onDialogClosed() {
		//Log.i("EVPARTS","onDialogClosed()");
		return false;
	}


    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        boolean value;	
        if (preference == mUseRotaryLockPref) {
            value = mUseRotaryLockPref.isChecked();
            Settings.System.putInt(getContentResolver(), Settings.System.USE_ROTARY_LOCKSCREEN, value ? 1 : 0);
            ActivityManager am = (ActivityManager)getSystemService(
                    Context.ACTIVITY_SERVICE);
            am.forceStopPackage("com.android.phone");
        }
        return false;
    }
	
	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		//Log.i("EVPARTS","onSharedPreferenceChanged()" + key);
		if (CARRIER_CAP.equals(key)) {
			Settings.System.putString(getContentResolver(),CARRIER_CAP, sharedPreferences.getString(CARRIER_CAP, ""));
			Log.i("EVPARTS","onSharedPreferenceChanged()" + key + "IFSTATE-" + sharedPreferences.getString(CARRIER_CAP, ""));
		}
		
	}
	
}
