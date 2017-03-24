package com.software.because.jakester.hectorsbrassbell;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

public class BellActivity extends AppCompatActivity {

    ImageView mBellImage;
    private SoundPool soundPool;
    private int soundID;
    boolean plays = false, loaded = false;
    float actVolume, maxVolume, volume;
    AudioManager audioManager;
    int counter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bell);
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        actVolume = (float) audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        maxVolume = (float) audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        volume = actVolume / maxVolume;

        this.setVolumeControlStream(AudioManager.STREAM_MUSIC);

        soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                loaded = true;
            }
        });
        soundID = soundPool.load(this, R.raw.ding1, 1);


        mBellImage = (ImageView) findViewById(R.id.iv_hector_bell);
        mBellImage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        stopSound();
                        mBellImage.setImageDrawable(getResources().getDrawable(R.drawable.bell2));
                        playSound();
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

    public void playSound() {
        // Is the sound loaded does it already play?
        if (loaded && !plays) {
            soundPool.play(soundID, volume, volume, 1, 0, 1f);
            counter = counter++;
        }
    }

    public void stopSound() {
        // Is the sound loaded does it already play?
        if (plays) {
            soundPool.stop(soundID);
            soundID = soundPool.load(this, R.raw.ding1, counter);
            plays = false;
        }
    }

}
