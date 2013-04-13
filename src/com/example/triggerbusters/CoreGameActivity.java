package com.example.triggerbusters;

import java.lang.reflect.InvocationTargetException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Debug;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.widget.TextView;

public class CoreGameActivity extends Activity implements SensorEventListener{
	
	public static final int VERSION = -100;
	
	public static final int QUIT_GAME_DIALOG = 0;
	
	private GLSurfaceView mGLSurfaceView;
    //private Game mGame;
    private boolean mMethodTracing;
    private float mTotalGameTime;
    private int mRobotsDestroyed;
    private int mPearlsCollected;
    private int mPearlsTotal;
    private boolean mExtrasUnlocked;
    private SensorManager mSensorManager;
    private SharedPreferences.Editor mPrefsEditor;
    private long mLastTouchTime = 0L;
    private long mLastRollTime = 0L;
    private View mPauseMessage = null;
    private View mWaitMessage = null;
    private View mLevelNameBox = null;
    private TextView mLevelName = null;
    private Animation mWaitFadeAnimation = null;
    
    /*private EventReporter mEventReporter;
    private Thread mEventReporterThread;*/
    
    
    protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	
    	Log.d("CoreGameActivity", "onCreate");
    	
    	// Setting up views
    	//setContentView(R.layout.main);
    	mGLSurfaceView = (GLSurfaceView) findViewById(R.id.glsurfaceview);
    	
    	mGLSurfaceView.setEGLConfigChooser(false); // 16bit, no z-buffer
    	//mGame = new Game();
        //mGame.setSurfaceView(mGLSurfaceView);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
    	
        int defaultWidth = 480;
        int defaultHeight = 320;
        if (dm.widthPixels != defaultWidth) {
        	float ratio =((float)dm.widthPixels) / dm.heightPixels;
        	defaultWidth = (int)(defaultHeight * ratio);
        }
        
        //mGame.bootstrap(this, dm.widthPixels, dm.heightPixels, defaultWidth, defaultHeight, mDifficulty);
    	//mGLSurfaceView.setRenderer(mGame.getRenderer());
        
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        // This activity uses the media stream.
        // setVolumeControlStream(AudioManager.STREAM_MUSIC);
        
        /*mEventReporter = null;
        mEventReporterThread = null;
        mEventReporter = new EventReporter();
        mEventReporterThread = new Thread(mEventReporter);
        mEventReporterThread.setName("EventReporter");
        mEventReporterThread.start();*/
    }
    
    @Override
    protected void onDestroy() {
        Log.d("AndouKun", "onDestroy()");
        //mGame.stop();
       /* if (mEventReporterThread != null) {
	        mEventReporter.stop();
	        try {
				mEventReporterThread.join();
			} catch (InterruptedException e) {
				mEventReporterThread.interrupt();
			}
        }*/
        super.onDestroy();
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        Log.d("AndouKun", "onPause");

        hidePauseMessage();
        
        
        mGLSurfaceView.onPause();
        //mGame.onPause();
        //mGame.getRenderer().onPause();	// hack!
        
        if (mMethodTracing) {
            Debug.stopMethodTracing();
            mMethodTracing = false;
        }
        if (mSensorManager != null) {
            mSensorManager.unregisterListener(this);
        }
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        
        Log.d("AndouKun", "onResume");
        mGLSurfaceView.onResume();
        //mGame.onResume(this, false);
       
        //final boolean soundEnabled = prefs.getBoolean(PreferenceConstants.PREFERENCE_SOUND_ENABLED, true);
        //mGame.setSoundEnabled(soundEnabled);
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
    	/*if (!mGame.isPaused()) {
    		mGame.onTouchEvent(event);
	    	
	        final long time = System.currentTimeMillis();
	        if (event.getAction() == MotionEvent.ACTION_MOVE && time - mLastTouchTime < 32) {
		        // Sleep so that the main thread doesn't get flooded with UI events.
		        try {
		            Thread.sleep(32);
		        } catch (InterruptedException e) {
		            // No big deal if this sleep is interrupted.
		        }
		        mGame.getRenderer().waitDrawingComplete();
	        }
	        mLastTouchTime = time;
    	}*/
        return true;
    }
    
    /*
     *  When the game thread needs to stop its own execution (to go to a new level, or restart the
     *  current level), it registers a runnable on the main thread which orders the action via this
     *  function.
     */
    /*public void onGameFlowEvent(int eventCode, int index) {
       switch (eventCode) {
           case GameFlowEvent.EVENT_END_GAME: 
               mGame.stop();
               finish();
               break;
           case GameFlowEvent.EVENT_RESTART_LEVEL:
        	   if (LevelTree.get(mLevelRow, mLevelIndex).restartable) {
        		   if (mEventReporter != null) {
	        		   mEventReporter.addEvent(EventReporter.EVENT_DEATH,
	        				   mGame.getLastDeathPosition().x, 
	        				   mGame.getLastDeathPosition().y, 
	        				   mGame.getGameTime(), 
	        				   LevelTree.get(mLevelRow, mLevelIndex).name, 
	        				   VERSION, 
	        				   mSessionId);
        		   }
        		   mGame.restartLevel();
        		   break;
        	   }
        	   // else, fall through and go to the next level.
           case GameFlowEvent.EVENT_GO_TO_NEXT_LEVEL:
               LevelTree.get(mLevelRow, mLevelIndex).completed = true;
               final LevelTree.LevelGroup currentGroup = LevelTree.levels.get(mLevelRow);
               final int count = currentGroup.levels.size();
               boolean groupCompleted = true;
               if (mEventReporter != null) {
	               mEventReporter.addEvent(EventReporter.EVENT_BEAT_LEVEL,
	    				   0, 
	    				   0, 
	    				   mGame.getGameTime(), 
	    				   LevelTree.get(mLevelRow, mLevelIndex).name, 
	    				   VERSION, 
	    				   mSessionId);
               }
               for (int x = 0; x < count; x++) {
            	   if (currentGroup.levels.get(x).completed == false) {
            		   // We haven't completed the group yet.
            		   mLevelIndex = x;
            		   groupCompleted = false;
            		   break;
            	   }
               }
               
               if (groupCompleted) {
                   mLevelIndex = 0;
                   mLevelRow++;
               }
               
    		   mTotalGameTime += mGame.getGameTime();
    		   mRobotsDestroyed += mGame.getRobotsDestroyed();
    		   mPearlsCollected += mGame.getPearlsCollected();
    		   mPearlsTotal += mGame.getPearlsTotal();
               
               if (mLevelRow < LevelTree.levels.size()) {
            	   final LevelTree.Level currentLevel = LevelTree.get(mLevelRow, mLevelIndex);
            	   if (currentLevel.inThePast || LevelTree.levels.get(mLevelRow).levels.size() > 1) {
            		   // go to the level select.
            		   Intent i = new Intent(this, LevelSelectActivity.class);
                       startActivityForResult(i, ACTIVITY_CHANGE_LEVELS);
                       if (UIConstants.mOverridePendingTransition != null) {
        	 		       try {
        	 		    	  UIConstants.mOverridePendingTransition.invoke(AndouKun.this, R.anim.activity_fade_in, R.anim.activity_fade_out);
        	 		       } catch (InvocationTargetException ite) {
        	 		           DebugLog.d("Activity Transition", "Invocation Target Exception");
        	 		       } catch (IllegalAccessException ie) {
        	 		    	   DebugLog.d("Activity Transition", "Illegal Access Exception");
        	 		       }
        	            }
            	   } else {
            		   // go directly to the next level
	                   mGame.setPendingLevel(currentLevel);
	                   if (currentLevel.showWaitMessage) {
	                	   showWaitMessage();
	                   } else {
	                	   hideWaitMessage();
	                   }
	                   mGame.requestNewLevel();
            	   }
            	   saveGame();
            	   
               } else {
            	   if (mEventReporter != null) {
	            	   mEventReporter.addEvent(EventReporter.EVENT_BEAT_GAME,
	        				   0, 
	        				   0, 
	        				   mGame.getGameTime(), 
	        				   "end", 
	        				   VERSION, 
	        				   mSessionId);
            	   }
                   // We beat the game!
            	   mLevelRow = 0;
            	   mLevelIndex = 0;
            	   mLastEnding = mGame.getLastEnding();
            	   mExtrasUnlocked = true;
            	   saveGame();
                   mGame.stop();
                   Intent i = new Intent(this, GameOverActivity.class);
                   startActivity(i);
                   if (UIConstants.mOverridePendingTransition != null) {
    	 		       try {
    	 		    	  UIConstants.mOverridePendingTransition.invoke(AndouKun.this, R.anim.activity_fade_in, R.anim.activity_fade_out);
    	 		       } catch (InvocationTargetException ite) {
    	 		           DebugLog.d("Activity Transition", "Invocation Target Exception");
    	 		       } catch (IllegalAccessException ie) {
    	 		    	   DebugLog.d("Activity Transition", "Illegal Access Exception");
    	 		       }
    	            }
                   finish();
                   
               }
               break;
           case GameFlowEvent.EVENT_SHOW_DIARY:
               Intent i = new Intent(this, DiaryActivity.class);
               LevelTree.Level level = LevelTree.get(mLevelRow, mLevelIndex);
               level.diaryCollected = true;
               i.putExtra("text", level.dialogResources.diaryEntry);
               startActivity(i);
               if (UIConstants.mOverridePendingTransition != null) {
 	 		       try {
 	 		    	  UIConstants.mOverridePendingTransition.invoke(AndouKun.this, R.anim.activity_fade_in, R.anim.activity_fade_out);
 	 		       } catch (InvocationTargetException ite) {
 	 		           DebugLog.d("Activity Transition", "Invocation Target Exception");
 	 		       } catch (IllegalAccessException ie) {
 	 		    	   DebugLog.d("Activity Transition", "Illegal Access Exception");
 	 		       }
 	            }
               break;
               
           case GameFlowEvent.EVENT_SHOW_DIALOG_CHARACTER1:
        	   i = new Intent(this, ConversationDialogActivity.class);
               i.putExtra("levelRow", mLevelRow);
               i.putExtra("levelIndex", mLevelIndex);
               i.putExtra("index", index);
               i.putExtra("character", 1);
               startActivity(i);
               break;
               
           case GameFlowEvent.EVENT_SHOW_DIALOG_CHARACTER2:
        	   i = new Intent(this, ConversationDialogActivity.class);
               i.putExtra("levelRow", mLevelRow);
               i.putExtra("levelIndex", mLevelIndex);
               i.putExtra("index", index);
               i.putExtra("character", 2);
               startActivity(i);
               break;
           case GameFlowEvent.EVENT_SHOW_ANIMATION:
        	   i = new Intent(this, AnimationPlayerActivity.class);
               i.putExtra("animation", index);
               startActivityForResult(i, ACTIVITY_ANIMATION_PLAYER);
               if (UIConstants.mOverridePendingTransition != null) {
	 		       try {
	 		    	  UIConstants.mOverridePendingTransition.invoke(AndouKun.this, R.anim.activity_fade_in, R.anim.activity_fade_out);
	 		       } catch (InvocationTargetException ite) {
	 		           DebugLog.d("Activity Transition", "Invocation Target Exception");
	 		       } catch (IllegalAccessException ie) {
	 		    	   DebugLog.d("Activity Transition", "Illegal Access Exception");
	 		       }
	            }
               break;
        	   
       }
    }*/
    
    protected void showPauseMessage() {
    	if (mPauseMessage != null) {
    		mPauseMessage.setVisibility(View.VISIBLE);
    	}
    	/*if (mLevelNameBox != null && mLevelName != null) {
    		mLevelName.setText(LevelTree.get(mLevelRow, mLevelIndex).name);
    		mLevelNameBox.setVisibility(View.VISIBLE);
    	}*/
    }
    
    
    protected void hidePauseMessage() {
    	if (mPauseMessage != null) {
    		mPauseMessage.setVisibility(View.GONE);
    	}
    	/*if (mLevelNameBox != null) {
    		mLevelNameBox.setVisibility(View.GONE);
    	}*/
    }
    
    protected void showWaitMessage() {
    	if (mWaitMessage != null) {
    		mWaitMessage.setVisibility(View.VISIBLE);
    		mWaitMessage.startAnimation(mWaitFadeAnimation);
    	}
    }
    
    protected void hideWaitMessage() {
    	if (mWaitMessage != null) {
    		mWaitMessage.setVisibility(View.GONE);
    		mWaitMessage.clearAnimation();
    	}
    }
    
    @Override
    protected Dialog onCreateDialog(int id) {
        Dialog dialog = null;
        if (id == QUIT_GAME_DIALOG) {
        	
            dialog = new AlertDialog.Builder(this)
                .setTitle(R.string.quit_game_dialog_title)
                .setPositiveButton(R.string.quit_game_dialog_ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    	finish();
                    	if (UIConstants.mOverridePendingTransition != null) {
         	 		       try {
         	 		    	  UIConstants.mOverridePendingTransition.invoke(CoreGameActivity.this, R.anim.activity_fade_in, R.anim.activity_fade_out);
         	 		       } catch (InvocationTargetException ite) {
         	 		           Log.d("Activity Transition", "Invocation Target Exception");
         	 		       } catch (IllegalAccessException ie) {
         	 		    	   Log.d("Activity Transition", "Illegal Access Exception");
         	 		       }
         	            }
                    }
                })
                .setNegativeButton(R.string.quit_game_dialog_cancel, null)
                .setMessage(R.string.quit_game_dialog_message)
                .create();
        }
        return dialog;
    }

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		
	}
}
