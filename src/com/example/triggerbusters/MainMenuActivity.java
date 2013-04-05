package com.example.triggerbusters;

import android.app.Activity;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;

public class MainMenuActivity extends Activity{
	
	private boolean mPaused;
    private View mStartButton;
    private View mOptionsButton;
    private View mExtrasButton;
    private View mBackground;
    private View mTicker;
    private Animation mButtonFlickerAnimation;
    private Animation mFadeOutAnimation;
    private Animation mAlternateFadeOutAnimation;
    private Animation mFadeInAnimation;
    private boolean mJustCreated;
    private String mSelectedControlsString;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mainmenu);
		
		mPaused = true;
		mStartButton = findViewById(R.id.startButton);
		mOptionsButton = findViewById(R.id.optionButton);
		mBackground = findViewById(R.id.mainMenuBackground);
		
		mTicker = findViewById(R.id.ticker);
        if (mTicker != null) {
        	mTicker.setFocusable(true);
        	mTicker.requestFocus();
        	mTicker.setSelected(true);
        }
        
        // Keep the volume control type consistent across all activities.
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
	}
	
    @Override
    protected void onPause() {
        super.onPause();
        mPaused = true;
    }
}
