package com.evervolv.EVParts.Preferences;


import java.util.ArrayList;

import com.evervolv.EVParts.R;
import com.evervolv.EVParts.R.bool;
import com.evervolv.EVParts.R.xml;
import com.evervolv.EVParts.utils.ShortcutPickHelper;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
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


public class LockscreenPrefs extends PreferenceActivity implements 
		OnPreferenceChangeListener, ShortcutPickHelper.OnPickListener{
	
	private static final String CARRIER_CAP_PREF = "pref_carrier_caption";
	private static final String LOCKSCREEN_STYLE_PREF = "pref_lockscreen_style";
    private static final String TRACKBALL_UNLOCK_PREF = "pref_trackball_unlock";
    private static final String VOLBTN_MUSIC_CTRL_PREF = "pref_volbtn_music_controls";
    private static final String GENERAL_CATEGORY = "pref_lockscreen_general_category";
    private static final String LOCKSCREEN_CUSTOM_APP_ACTIVITY = "pref_lockscreen_custom_app_activity";
	private static final String LOCKSCREEN_RING_LOCATION_PREF = "pref_lockscreen_ring_location";
    
    private static final int LOCK_STYLE_TABS   = 1;
    private static final int LOCK_STYLE_ROTARY = 2;
    private static final int LOCK_STYLE_RING = 3;
    private int mLockScreen;
    
	private EditTextPreference mCarrierCaption;
	private ListPreference mLockscreenStyle;
	private ListPreference mLockscreenRingLocation;
	private Preference mCustomAppActivityPref;
	private CheckBoxPreference mTrackballUnlockPref;
	private CheckBoxPreference mVolBtnMusicCtrlPref;

	private ShortcutPickHelper mPicker;
	
    private int mWhichApp = -1;

    private int mMaxRingCustomApps = Settings.System.LOCKSCREEN_CUSTOM_RING_APP_ACTIVITIES.length;
	
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
		
		/* Lockscreen style */
		mLockscreenRingLocation = (ListPreference) prefSet.findPreference(LOCKSCREEN_RING_LOCATION_PREF);
		mLockscreenRingLocation.setOnPreferenceChangeListener(this);
		
		mLockScreen = Settings.System.getInt(getContentResolver(), 
				Settings.System.LOCKSCREEN_STYLE, LOCK_STYLE_ROTARY);
		
		/* Carrier caption */
		mCarrierCaption = (EditTextPreference)prefSet.findPreference(CARRIER_CAP_PREF);
		mCarrierCaption.setOnPreferenceChangeListener(this);

        /* Trackball Unlock */
        mTrackballUnlockPref = (CheckBoxPreference) prefSet.findPreference(TRACKBALL_UNLOCK_PREF);
        mTrackballUnlockPref.setChecked(Settings.System.getInt(getContentResolver(),
                Settings.System.TRACKBALL_UNLOCK_SCREEN, 0) == 1);

        mCustomAppActivityPref = prefSet
        .findPreference(LOCKSCREEN_CUSTOM_APP_ACTIVITY);
        
        /* Volume button music controls */
        mVolBtnMusicCtrlPref = (CheckBoxPreference) prefSet.findPreference(VOLBTN_MUSIC_CTRL_PREF);
        mVolBtnMusicCtrlPref.setChecked(Settings.System.getInt(getContentResolver(),
                Settings.System.VOLBTN_MUSIC_CONTROLS, 1) == 1);

		PreferenceCategory generalCategory = (PreferenceCategory) prefSet
    	.findPreference(GENERAL_CATEGORY);
		
		if (!getResources().getBoolean(R.bool.has_trackball)) {
			if (DEBUG) Log.d(TAG, "does not have trackball!");
			generalCategory.removePreference(mTrackballUnlockPref);
		}
		
		mPicker = new ShortcutPickHelper(this, this);
    }	
	
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        boolean value;
        
        if (preference == mTrackballUnlockPref) {
            value = mTrackballUnlockPref.isChecked();
            Settings.System.putInt(getContentResolver(), Settings.System.TRACKBALL_UNLOCK_SCREEN,
                    value ? 1 : 0);
            return true;
        } else if (preference == mVolBtnMusicCtrlPref) {
            value = mVolBtnMusicCtrlPref.isChecked();
            Settings.System.putInt(getContentResolver(),
                    Settings.System.VOLBTN_MUSIC_CONTROLS, value ? 1 : 0);
            return true;
        } else if (preference == mCustomAppActivityPref) {
            final String[] items = getCustomRingAppItems();

            if (items.length == 0) {
                mWhichApp = 0;
                mPicker.pickShortcut();
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.pref_lockscreen_ring_custom_apps_dialog_title_set);
                builder.setItems(items, new Dialog.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        mWhichApp = which;
                        mPicker.pickShortcut();
					}
                });
                if (items.length < mMaxRingCustomApps) {
                    builder.setPositiveButton(R.string.pref_lockscreen_ring_custom_apps_dialog_add,
                            new Dialog.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            mWhichApp = items.length;
                            mPicker.pickShortcut();
                        }
                    });
                }
                builder.setNeutralButton(R.string.pref_lockscreen_ring_custom_apps_dialog_remove,
                        new Dialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                        AlertDialog.Builder builder = new AlertDialog.Builder(LockscreenPrefs.this);
                        builder.setTitle(R.string.pref_lockscreen_ring_custom_apps_dialog_title_unset);
                        builder.setItems(items, new Dialog.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                Settings.System.putString(getContentResolver(),
                                        Settings.System.LOCKSCREEN_CUSTOM_RING_APP_ACTIVITIES[which], null);
                                //shift the rest of items down
                                for (int q = which + 1; q < mMaxRingCustomApps; q++) {
                                    Settings.System.putString(getContentResolver(),
                                            Settings.System.LOCKSCREEN_CUSTOM_RING_APP_ACTIVITIES[q - 1],
                                            Settings.System.getString(getContentResolver(),
                                            Settings.System.LOCKSCREEN_CUSTOM_RING_APP_ACTIVITIES[q]));
                                    Settings.System.putString(getContentResolver(),
                                            Settings.System.LOCKSCREEN_CUSTOM_RING_APP_ACTIVITIES[q], null);
                                }
                                mCustomAppActivityPref.setSummary(getCustomRingAppSummary());
                            }
                        });
                        builder.setNegativeButton(R.string.pref_lockscreen_ring_custom_apps_dialog_cancel,
                                new Dialog.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        builder.setCancelable(true);
                        builder.create().show();
                    }
                });
                builder.setNegativeButton(R.string.pref_lockscreen_ring_custom_apps_dialog_cancel,
                        new Dialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.setCancelable(true);
                builder.create().show();
            }
        }
        return true;
    }
	
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mPicker.onActivityResult(requestCode, resultCode, data);
    }
    
    private void updateCustomAppSummary() {
    	if (mLockScreen != LOCK_STYLE_RING) {
    		mCustomAppActivityPref.setEnabled(false);
    	} else {
    		mCustomAppActivityPref.setEnabled(true);
    	}
    	mCustomAppActivityPref.setSummary(getCustomRingAppSummary());
    }

    @Override
    public void onResume() {
        super.onResume();
        updateCustomAppSummary();
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
        	
        	mLockScreen = Integer.valueOf((String) objValue);
        	if (mLockScreen != LOCK_STYLE_RING) {
        		mCustomAppActivityPref.setEnabled(false);
        		mLockscreenRingLocation.setEnabled(false);
        	} else {
        		mCustomAppActivityPref.setEnabled(true);
        		mLockscreenRingLocation.setEnabled(true);
        	}
            return true;
        } else if (preference == mLockscreenRingLocation) {
        	Settings.System.putInt(getContentResolver(), Settings.System.LOCKSCREEN_RING_LOCATION, 
        			Integer.valueOf((String) objValue));
        	Log.d(TAG, "ringLoc: " + objValue);
            return true;
        }
        return false;
    }
    
    private String getCustomRingAppSummary() {
        String summary = "";
        String[] items = getCustomRingAppItems();

        for (int q = 0; q < items.length; q++) {
            if (q != 0) {
                summary += ", ";
            }
            summary += items[q];
        }

        return summary;
    }

    private String[] getCustomRingAppItems() {
        ArrayList<String> items = new ArrayList<String>();
        for (int q = 0; q < mMaxRingCustomApps; q++) {
            String uri = Settings.System.getString(getContentResolver(),
                    Settings.System.LOCKSCREEN_CUSTOM_RING_APP_ACTIVITIES[q]);
            if (uri != null) {
                items.add(mPicker.getFriendlyNameForUri(uri));
            }
        }
        return items.toArray(new String[0]);
    }

    @Override
    public void shortcutPicked(String uri, String friendlyName, boolean isApplication) {
        if (mWhichApp == -1) {
        	//nothing, remove this later
        } else {
            Settings.System.putString(getContentResolver(),
                    Settings.System.LOCKSCREEN_CUSTOM_RING_APP_ACTIVITIES[mWhichApp], uri);
            mCustomAppActivityPref.setSummary(getCustomRingAppSummary());
            mWhichApp = -1;
        }
    }
    
}
