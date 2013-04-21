package com.example.triggerbusters;

import java.lang.reflect.InvocationTargetException;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

public class TypeMenuActivity extends Activity{
	
	private View mOriginalButton;
    private View mRegularButton;
    private View mBackground;
    private View mOriginalText;
    private View mRegularText;
    private Animation mButtonFlickerAnimation;
    private Animation mFadeOutAnimation;
    private Animation mAlternateFadeOutAnimation;
	
	private View.OnClickListener sOriginalButtonListener = new View.OnClickListener() {
        public void onClick(View v) {
        
            Intent i = new Intent(getBaseContext(), PreScreen.class);
        	//Intent i = new Intent(getBaseContext(), RulesPage.class);
        	i.putExtras(getIntent());
            i.putExtra("difficulty", 0);

            v.startAnimation(mButtonFlickerAnimation);
            mFadeOutAnimation.setAnimationListener(new StartActivityAfterAnimation(i));
            mBackground.startAnimation(mFadeOutAnimation);
            mRegularButton.startAnimation(mAlternateFadeOutAnimation);
            
            mOriginalText.startAnimation(mAlternateFadeOutAnimation);
            mRegularText.startAnimation(mAlternateFadeOutAnimation);
        }
    };
    
    private View.OnClickListener sRegularButtonListener = new View.OnClickListener() {
        public void onClick(View v) {
        
            Intent i = new Intent(getBaseContext(), Game.class);
            i.putExtras(getIntent());
            i.putExtra("difficulty", 0);

            v.startAnimation(mButtonFlickerAnimation);
            mFadeOutAnimation.setAnimationListener(new StartActivityAfterAnimation(i));
            mBackground.startAnimation(mFadeOutAnimation);
            mOriginalButton.startAnimation(mAlternateFadeOutAnimation);
            
            mOriginalText.startAnimation(mAlternateFadeOutAnimation);
            mRegularText.startAnimation(mAlternateFadeOutAnimation);
        }
    };
    
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_type_menu);
        
        
        mOriginalButton = findViewById(R.id.OriginalButton);
        mRegularButton = findViewById(R.id.RegularButton);
        mOriginalText = findViewById(R.id.originalText);
        mRegularText = findViewById(R.id.regularText);
        mBackground = findViewById(R.id.mainMenuBackground);
        
        mOriginalButton.setOnClickListener(sOriginalButtonListener);
        mRegularButton.setOnClickListener(sRegularButtonListener);
        
        mButtonFlickerAnimation = AnimationUtils.loadAnimation(this, R.anim.button_flicker);
        mFadeOutAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_out);
        mAlternateFadeOutAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_out);        
        
        // Keep the volume control type consistent across all activities.
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
    }
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		boolean result = true;
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			
			if (UIConstants.mOverridePendingTransition != null) {
 		       try {
 		    	  UIConstants.mOverridePendingTransition.invoke(TypeMenuActivity.this, R.anim.activity_fade_in, R.anim.activity_fade_out);
 		       } catch (InvocationTargetException ite) {
 		           Log.d("Activity Transition", "Invocation Target Exception");
 		       } catch (IllegalAccessException ie) {
 		    	   Log.d("Activity Transition", "Illegal Access Exception");
 		       }
             }
		} else {
			result = super.onKeyDown(keyCode, event);
		}
		return result;
	}
	
	 protected class StartActivityAfterAnimation implements Animation.AnimationListener {
	        private Intent mIntent;
	        
	        StartActivityAfterAnimation(Intent intent) {
	            mIntent = intent;
	        }
	            

	        public void onAnimationEnd(Animation animation) {
	        	mOriginalButton.setVisibility(View.INVISIBLE);
	        	mOriginalButton.clearAnimation();
	        	mRegularButton.setVisibility(View.INVISIBLE);
	        	mRegularButton.clearAnimation();
	            startActivity(mIntent);     
	            finish();	// This activity dies when it spawns a new intent.
	            
	            if (UIConstants.mOverridePendingTransition != null) {
	 		       try {
	 		    	  UIConstants.mOverridePendingTransition.invoke(TypeMenuActivity.this, R.anim.activity_fade_in, R.anim.activity_fade_out);
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
