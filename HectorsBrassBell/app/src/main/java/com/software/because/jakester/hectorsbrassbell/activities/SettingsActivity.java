package com.software.because.jakester.hectorsbrassbell.activities;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.software.because.jakester.hectorsbrassbell.R;
import com.software.because.jakester.hectorsbrassbell.utils.BellConstants;

public class SettingsActivity extends AppCompatActivity {

    Switch explosionSwitch;
    SharedPreferences settingsPrefs;
    SharedPreferences.Editor settingsEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setTitle(BellConstants.SETTINGS_PREFS);
        settingsPrefs = getSharedPreferences(BellConstants.SETTINGS_PREFS, MODE_PRIVATE);
        final SharedPreferences.Editor settingsEditorListener = settingsPrefs.edit();
        settingsEditor = settingsEditorListener;

        explosionSwitch = (Switch) findViewById(R.id.s_explosion_option);
        explosionSwitch.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    settingsEditorListener.putBoolean(BellConstants.EXPLOSION_OPTION, true);
                    settingsEditorListener.commit();
                } else {
                    settingsEditorListener.putBoolean(BellConstants.EXPLOSION_OPTION, false);
                    settingsEditorListener.commit();
                }
            }
        });


    }

    @Override
    public void onResume(){
        super.onResume();
        settingsPrefs = getSharedPreferences(BellConstants.SETTINGS_PREFS, MODE_PRIVATE);
        if(settingsPrefs.contains(BellConstants.EXPLOSION_OPTION) &&
                settingsPrefs.getBoolean(BellConstants.EXPLOSION_OPTION, false)){
            explosionSwitch.setChecked(true);
        }
        else{
            explosionSwitch.setChecked(false);
            settingsEditor.putBoolean(BellConstants.EXPLOSION_OPTION, false);
            settingsEditor.commit();
        }

    }
}
