package com.software.because.jakester.hectorsbrassbell.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.SoundPool;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.software.because.jakester.hectorsbrassbell.R;
import com.software.because.jakester.hectorsbrassbell.utils.BellConstants;

public class BellActivity extends AppCompatActivity {

    ImageView mBellImage;
    private SoundPool soundPool;
    private int dingSoundID, explosionSoundID;
    boolean plays = false, loaded = false;
    float actVolume, maxVolume, volume;
    AudioManager audioManager;
    int counter = 0;
    boolean signalOn;

    SharedPreferences settingsPrefs;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bell);
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        actVolume = (float) audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        maxVolume = (float) audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        volume = actVolume / maxVolume;
        signalOn = false;

        this.setVolumeControlStream(AudioManager.STREAM_MUSIC);

        soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                loaded = true;
            }
        });
        dingSoundID = soundPool.load(this, R.raw.ding1, 1);
        explosionSoundID = soundPool.load(this, R.raw.explosion, 1);

        mBellImage = (ImageView) findViewById(R.id.iv_hector_bell);
        mBellImage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        stopSound(dingSoundID);
                        mBellImage.setImageDrawable(getResources().getDrawable(R.drawable.bell2));
                        playSound(dingSoundID);
                        if(signalOn) {
                            counter += 1;

                            if (counter % 15 == 0) {
                                playSound(explosionSoundID);
                                //Toast.makeText(BellActivity.this, "EXPLOSIIOOOONN!! Boom!!", Toast.LENGTH_SHORT).show();
                            }
                        }
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        mBellImage.setImageDrawable(getResources().getDrawable(R.drawable.bell));
                        break;
                    }
                }
                return true;
            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();
        settingsPrefs = getSharedPreferences(BellConstants.SETTINGS_PREFS, MODE_PRIVATE);
        if(settingsPrefs.contains(BellConstants.EXPLOSION_OPTION) &&
                settingsPrefs.getBoolean(BellConstants.EXPLOSION_OPTION, false)){
            counter = 0;
            signalOn = true;
        }
        else{
            signalOn = false;
        }

    }

    public void playSound(int soundID) {
        // Is the sound loaded does it already play?
        if (loaded && !plays) {
            soundPool.play(soundID, volume, volume, 1, 0, 1f);

        }
    }

    public void stopSound(int soundID) {
        // Is the sound loaded does it already play?
        if (plays) {
            soundPool.stop(soundID);
            soundID = soundPool.load(this, R.raw.ding1, counter);
            plays = false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_settings:
                goToSettings();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void goToSettings(){
        Intent intent = new Intent(this, SettingsActivity.class);
        this.startActivity(intent);
    }

}
