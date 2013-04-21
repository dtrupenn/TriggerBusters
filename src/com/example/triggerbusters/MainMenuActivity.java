package com.example.triggerbusters;

import java.lang.reflect.InvocationTargetException;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

public class MainMenuActivity extends Activity{
	
	private boolean mPaused;
    private View mStartButton;
    private View mOptionsButton;
    private View mHighScoreButton;
    private View mBackground;
    private View mTicker;
    private Animation mButtonFlickerAnimation;
    private Animation mFadeOutAnimation;
    private Animation mAlternateFadeOutAnimation;
    private Animation mFadeInAnimation;
    private boolean mJustCreated;
    private String mSelectedControlsString;
    
    
    private View.OnClickListener sStartButtonListener = new View.OnClickListener() {
        public void onClick(View v) {
            if (!mPaused) {
            	Intent i = new Intent(getBaseContext(), TypeMenuActivity.class);
            	i.putExtra("newGame", true);
                v.startAnimation(mButtonFlickerAnimation);
                mButtonFlickerAnimation.setAnimationListener(new StartActivityAfterAnimation(i));

                mPaused = true;
            }
        }
    };
    
    
    private View.OnClickListener sOptionButtonListener = new View.OnClickListener() {
        public void onClick(View v) {
            if (!mPaused) {
                Intent i = new Intent(getBaseContext(), SetPreferencesActivity.class);

                v.startAnimation(mButtonFlickerAnimation);
                mFadeOutAnimation.setAnimationListener(new StartActivityAfterAnimation(i));
                mBackground.startAnimation(mFadeOutAnimation);
                mStartButton.startAnimation(mAlternateFadeOutAnimation);
                mTicker.startAnimation(mAlternateFadeOutAnimation);
                mPaused = true;
            }
        }
    };
	
    private View.OnClickListener sHighScoreButtonListener = new View.OnClickListener() {
        public void onClick(View v) {
            if (!mPaused) {
                Intent i = new Intent(getBaseContext(), HighScoreActivity.class);

                v.startAnimation(mButtonFlickerAnimation);
                mButtonFlickerAnimation.setAnimationListener(new StartActivityAfterAnimation(i));
                mPaused = true;
            }
        }
    };
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mainmenu);
		
		mPaused = true;
		mStartButton = findViewById(R.id.startButton);
		mOptionsButton = findViewById(R.id.optionButton);
		mHighScoreButton = findViewById(R.id.highScoreButton);
		mBackground = findViewById(R.id.mainMenuBackground);
		
		if (mHighScoreButton != null) {
			mHighScoreButton.setOnClickListener(sHighScoreButtonListener);
		}

		if (mOptionsButton != null) {
            mOptionsButton.setOnClickListener(sOptionButtonListener);
        }

		if (mStartButton != null) {
			mStartButton.setOnClickListener(sStartButtonListener);
		}

		mButtonFlickerAnimation = AnimationUtils.loadAnimation(this, R.anim.button_flicker);
        mFadeOutAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_out);
        mAlternateFadeOutAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_out);
        mFadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in);

		mTicker = findViewById(R.id.ticker);
        if (mTicker != null) {
        	mTicker.setFocusable(true);
        	mTicker.requestFocus();
        	mTicker.setSelected(true);
        }

        mJustCreated = true;

        // Keep the volume control type consistent across all activities.
        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        //MediaPlayer mp = MediaPlayer.create(this, R.raw.bwv_115);
        //mp.start();
	}
	
    @Override
    protected void onPause() {
        super.onPause();
        mPaused = true;
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        mPaused = false;
        
        mButtonFlickerAnimation.setAnimationListener(null);
        
        if (mBackground != null) {
        	mBackground.clearAnimation();
        }
        
        if (mTicker != null) {
        	mTicker.clearAnimation();
        	mTicker.setAnimation(mFadeInAnimation);
        }
        
        if (mJustCreated) {
        	if (mStartButton != null) {
                mStartButton.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_slide));
            }
            
            if (mOptionsButton != null) {
            	Animation anim = AnimationUtils.loadAnimation(this, R.anim.button_slide);
                anim.setStartOffset(1000L);
                mOptionsButton.startAnimation(anim);
            }
            mJustCreated = false;
            
        } else {
        	mStartButton.clearAnimation();
        	mOptionsButton.clearAnimation();
        }
    }
    
    protected class StartActivityAfterAnimation implements Animation.AnimationListener {
        private Intent mIntent;
        
        StartActivityAfterAnimation(Intent intent) {
            mIntent = intent;
        }
            

        public void onAnimationEnd(Animation animation) {
        	
            startActivity(mIntent);      
            
            if (UIConstants.mOverridePendingTransition != null) {
		       try {
		    	   UIConstants.mOverridePendingTransition.invoke(MainMenuActivity.this, R.anim.activity_fade_in, R.anim.activity_fade_out);
		       } catch (InvocationTargetException ite) {
		           Log.d("Activity Transition", "Invocation Target Exception");
		       } catch (IllegalAccessException ie) {
		    	   Log.d("Activity Transition", "Illegal Access Exception");
		       }
            }
        }

        public void onAnimationRepeat(Animation animation) {
            // TODO Auto-generated method stub
            
        }

        public void onAnimationStart(Animation animation) {
            // TODO Auto-generated method stub
            
        }
        
    }
}
