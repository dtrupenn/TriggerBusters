package com.example.triggerbusters;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;



public class PreScreen extends Activity {
	private Timer _timer;
	private int genderInt;
	private PreScreen pscreen = this;
	private SoundManager mSoundManager;
	private boolean mouseDown;
	PreGameView grid;
	private int Ax;
	private int Ay;
	private int Bx;
	private int By;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.prescreen);
		grid = (PreGameView) (findViewById(R.id.pregameview1));
		grid.setPause();
		showDialog(1);
		removeDialog(1);
		showDialog(1);
		_timer = new Timer();
		TimerTask timer_task = new TimerTask() {
			// PreGameView grid = (PreGameView)
			// (findViewById(R.id.pregameview1));
			private Handler updateUI = new Handler() {
				@Override
				public void dispatchMessage(Message msg) {

					((PreGameView) grid).timerCalled();
					super.dispatchMessage(msg);
					checkForCollision();
				}
			};

			@Override
			public void run() {
				updateUI.sendEmptyMessage(0);
			}

		};

		Intent i = getIntent();
		genderInt = i.getIntExtra("Gender", 0);
		Gender gender = Gender.Boy;
		if (genderInt == 0) {
			gender = Gender.Boy;
		} else {
			gender = Gender.Girl;
		}
		grid.setGender(gender);
		_timer.scheduleAtFixedRate(timer_task, 0, 500);
		
		//Sound Control
		
		mSoundManager = new SoundManager();
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        if(sharedPrefs.getBoolean("prefSound", true)){
        	mSoundManager.initSounds(getBaseContext());
        	mSoundManager.addSound(3, R.raw.powerup);
        }
	}

	public boolean onKeyDown(int keycode, KeyEvent event) {
		// PreGameView grid = (PreGameView) (findViewById(R.id.pregameview1));
		super.onKeyDown(keycode, event);
		if (grid.getPause() && keycode != KeyEvent.KEYCODE_SPACE) {
			return false;
		}
		if (keycode == KeyEvent.KEYCODE_DPAD_UP) {
			grid.getCharacter().setDirection(Direction.Up);
			return true;
		} else if (keycode == KeyEvent.KEYCODE_DPAD_DOWN) {
			grid.getCharacter().setDirection(Direction.Down);
			return true;
		} else if (keycode == KeyEvent.KEYCODE_DPAD_LEFT) {
			grid.getCharacter().setDirection(Direction.Left);
			return true;
		} else if (keycode == KeyEvent.KEYCODE_DPAD_RIGHT) {
			grid.getCharacter().setDirection(Direction.Right);
			return true;
		} else if (keycode == KeyEvent.KEYCODE_SPACE) {
			grid.setPause();
			return true;
		}
		return false;
	}

	public boolean onTouchEvent(MotionEvent e) {
		int x1 = (int) e.getX();
	int y1 = (int) e.getY();
	if (e.getAction() == MotionEvent.ACTION_UP) {
		setEndPt(x1,y1);
		processSwipe();
	} else if (e.getAction() == MotionEvent.ACTION_DOWN) {
		setStartPt(x1,y1);
		/*if (mouseDown) {
			return false;
		}
		mouseDown = true;
		GameGridView gameview = (GameGridView) (findViewById(R.id.gridview1));
		float x = e.getX();
		float y = e.getY();
		if(!processClickJoystick(x,y, gameview.SCREEN_WIDTH, gameview.SCREEN_HEIGHT)){
			processClickBoard(x,y, gameview.SCREEN_WIDTH, gameview.SCREEN_HEIGHT);
		}
		*/
	}
	return false;
}

private void setStartPt(int x, int y){
	Ax = x;
	Ay = y;
}

private void setEndPt(int x, int y){
	Bx = x;
	By = y;
}

private void processSwipe(){
	int diffx=0;
	int diffy=0;
	if(Ax<0 && Bx<0){
		diffx = -Math.abs(Bx)-Math.abs(Ax);
	}
	else if(Ax<0 && Bx>=0){
		diffx = Bx-Ax;
	}
	else if(Ax>=0 && Bx<0){
		diffx = Bx-Ax;
	}
	else if(Ax>=0 && Bx>=0){
		diffx = Bx-Ax;
	} 


	if(Ay<0 && By<0){
		diffy = -Math.abs(By)-Math.abs(Ay);
	}
	else if(Ay<0 && By>=0){
		diffy = By-Ay;
	}
	else if(Ay>=0 && By<0){
		diffy = By-Ay;
	}
	else if(Ay>=0 && By>=0){
		diffy = By-Ay;
	}
	
	if(Math.abs(diffy)>=Math.abs(diffx)){
		if (diffy<0){
			this.changeCharDirection(Direction.Up);
		}
		else{ 
			this.changeCharDirection(Direction.Down);
		}
	}
	else {
		if (diffx<0){
			this.changeCharDirection(Direction.Left);
		}
		else{
			this.changeCharDirection(Direction.Right);
		}
	}
}
	
	private void processClickBoard(float x, float y, float width, float height){
		float rightX = width - x;
		float bottomY = height - 180 -y;
		float xCompare = Math.min(x, rightX);
		float yCompare = Math.min(y, bottomY);
		float result = Math.min(xCompare, yCompare);
		if(result==x){
			this.changeCharDirection(Direction.Left);
		}
		else if(result == y){
			this.changeCharDirection(Direction.Up);
		}
		else if(result == rightX){
			this.changeCharDirection(Direction.Right);
		}
		else{
			this.changeCharDirection(Direction.Down);
		}
		
		
	}
	
	private boolean processClick(float x, float y, float width, float height)
	{
		// make sure click is within joystick bounds
		if (x > width/2 + 90 || x < width/2 - 90 || y < height - 180)
		{
			return false;
		}
		
		// left
		else if (x < width/2 - 40)
		{
			this.changeCharDirection(Direction.Left);
		}
		
		// right
		else if (x > width/2 + 40)
		{
			this.changeCharDirection(Direction.Right);
		}
		// up
		else if (y < height - 140)
		{
			this.changeCharDirection(Direction.Up);
		}
		// down
		else if ( y > height - 50)
		{
			this.changeCharDirection(Direction.Down);
		}	
		return true;
	}
	public void changeCharDirection(Direction direction)
	{
		PreGameView grid = (PreGameView) (findViewById(R.id.pregameview1));
		grid.getCharacter().setDirection(direction);
	}

	private void checkForCollision() {
		PreGameView grid = (PreGameView) (findViewById(R.id.pregameview1));
		grid.checkCollision();
		boolean collides = grid.getCollision();
		if (collides) {
			grid.setPause();
			showDialog(0);
			removeDialog(0);
			showDialog(0);
		}
	}

	public Dialog onCreateDialog(int i) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		// this is the message to display
		if (i == 0) {
			SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
	        if(sharedPrefs.getBoolean("pref_sound", true)){
	        	mSoundManager.playSound(3);
	        }
			builder.setMessage(R.string.inhaler_message2);
			builder.setPositiveButton(R.string.ok,
					new DialogInterface.OnClickListener() {
						// this is the method to call when the button is clicked
						public void onClick(DialogInterface dialog, int id) {
							// this will hide the dialog
							PreGameView grid = (PreGameView) (findViewById(R.id.pregameview1));
							dialog.cancel();
							_timer.cancel();
							Intent i = new Intent(pscreen,
									AsthmaGameActivity.class);
							i.putExtra("Gender", genderInt);
							startActivity(i);

						}
					});
			return builder.create();

		} else if (i == 1) {
			builder.setMessage(R.string.inhaler_message1);
			builder.setPositiveButton(R.string.ok,
					new DialogInterface.OnClickListener() {
						// this is the method to call when the button is clicked
						public void onClick(DialogInterface dialog, int id) {
							// this will hide the dialog
							PreGameView grid = (PreGameView) (findViewById(R.id.pregameview1));
							dialog.cancel();
							grid.setPause();

						}
					});
			return builder.create();
		} else {
			return null;

		}

	}

}
