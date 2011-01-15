package com.evervolv.EVParts;


import com.evervolv.EVParts.R;
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
import android.preference.PreferenceScreen;
import android.preference.Preference.OnPreferenceChangeListener;
import android.widget.Toast;
import android.util.Log;
import android.provider.Settings;


public class UiOptions extends PreferenceActivity implements OnPreferenceChangeListener {

	private static final String USE_SCREENOFF_ANIM = "use_screenoff_anim";
	private static final String USE_SCREENON_ANIM = "use_screenon_anim";
	private static final String BATTERY_OPTION = "battery_option";
	
	private CheckBoxPreference mUseScreenOnAnim;
	private CheckBoxPreference mUseScreenOffAnim;
	private ListPreference mBatteryOption;
	
	
	@Override
    public void onCreate(Bundle savedInstanceState) {	
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.ui_options);
		PreferenceScreen prefSet = getPreferenceScreen();

		mUseScreenOnAnim = (CheckBoxPreference)prefSet.findPreference(USE_SCREENON_ANIM);
		mUseScreenOnAnim.setChecked(Settings.System.getInt(getContentResolver(), Settings.System.USE_SCREENON_ANIM, 1) == 1);
		mUseScreenOffAnim = (CheckBoxPreference)prefSet.findPreference(USE_SCREENOFF_ANIM);
		mUseScreenOffAnim.setChecked(Settings.System.getInt(getContentResolver(), Settings.System.USE_SCREENOFF_ANIM, 1) == 1);		
		
		mBatteryOption = (ListPreference) prefSet.findPreference(BATTERY_OPTION);
		mBatteryOption.setOnPreferenceChangeListener(this);
    }

    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        boolean value;
        
        if (preference == mUseScreenOnAnim) {
        	value = mUseScreenOnAnim.isChecked();
            Settings.System.putInt(getContentResolver(), Settings.System.USE_SCREENON_ANIM, value ? 1 : 0);
        }
        
        if (preference == mUseScreenOffAnim) {
        	value = mUseScreenOffAnim.isChecked();
            Settings.System.putInt(getContentResolver(), Settings.System.USE_SCREENOFF_ANIM, value ? 1 : 0);
        }
        
        return true;
    }

    public boolean onPreferenceChange(Preference preference, Object objValue) {
        if (preference == mBatteryOption) {;
        	Settings.System.putInt(getContentResolver(), Settings.System.BATTERY_OPTION, Integer.valueOf((String) objValue));
        }
        // always let the preference setting proceed.
        return true;
    }
    

}
