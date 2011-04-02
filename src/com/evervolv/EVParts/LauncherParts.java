package com.evervolv.EVParts;


import java.util.ArrayList;
import java.util.List;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.preference.Preference.OnPreferenceChangeListener;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.provider.Settings;


public class LauncherParts extends PreferenceActivity
implements SharedPreferences.OnSharedPreferenceChangeListener , OnClickListener, OnPreferenceChangeListener{

	//private static final int REQUEST_PICK_APPLICATION = 6;
	//private static final String FARRIGHT_AB = "farrightaction_button";
	

	private ListPreference mLauncherStylePref;
	
	private String[] mAppNames;
	private static final String LAUNCHER_STYLE = "launcher_style";
	
	@Override
    public void onCreate(Bundle savedInstanceState) {	
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.launcher_prefs);
		getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
		PreferenceScreen prefSet = getPreferenceScreen();
	
		
		mLauncherStylePref = (ListPreference) prefSet.findPreference(LAUNCHER_STYLE);
		mLauncherStylePref.setValueIndex(Settings.System.getInt(getContentResolver(),
                Settings.System.LAUNCHER_STYLE, 1));
		mLauncherStylePref.setOnPreferenceChangeListener(this);
    }

	@Override
	public void onClick(View v) {
		
	}


	@Override
	public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference == mLauncherStylePref) {
        	Settings.System.putInt(getContentResolver(), Settings.System.LAUNCHER_STYLE, Integer.valueOf((String) newValue));
            ActivityManager am = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
            am.forceStopPackage("com.android.launcher");
        }
        return false;
	}



	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		// TODO Auto-generated method stub
		
	}
	class Package {  
		private String Appname = "";
		private String PackagName = ""; 
		private Drawable icon;
	}  
}

 