package com.evervolv.EVParts;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class EvervolvToolbox extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.evervolv_toolbox);
    }
    
	public void openPrefLockscreen(View view) {
    	Intent intent = new Intent();
    	
    	intent.setClassName("com.evervolv.EVParts", "com.evervolv.EVParts.Preferences.LockscreenPrefs");
    	startActivity(intent);
    }

	public void openPrefAdvanced(View view) {
    	Intent intent = new Intent();
    	
    	intent.setClassName("com.evervolv.EVParts", "com.evervolv.EVParts.Preferences.AdvancedPrefs");
    	startActivity(intent);
    }
    
	public void openPrefInterface(View view) {
    	Intent intent = new Intent();
    	
    	intent.setClassName("com.evervolv.EVParts", "com.evervolv.EVParts.Preferences.InterfacePrefs");
    	startActivity(intent);
    }
    
	public void openCreoSettings(View view) {
    	Intent intent = new Intent();
    	
    	intent.setClassName("com.evervolv.creo", "com.evervolv.creo.LauncherPreferences");
    	startActivity(intent);
    }
	
	public void handleToolButtons(View v) {
		//TODO: Use this single method and parse which button is being pushed for action...
	}
}