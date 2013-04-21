package com.example.triggerbusters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

public class RulesView extends View {

	private int SCREEN_WIDTH;
	private int SCREEN_HEIGHT;
	private int _picture;

	public RulesView(Context c) {
		super(c);
	}

	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	public RulesView(Context context, AttributeSet attrs) {
		super(context, attrs);
		final WindowManager w = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		//Get screen dimensions
		Point size = new Point();
		final Display d = w.getDefaultDisplay();
		if(Build.VERSION.SDK_INT >= 13){
			d.getSize(size);
			SCREEN_WIDTH = size.x;
			SCREEN_HEIGHT = size.y;
		}
		else {
			SCREEN_WIDTH = d.getWidth();
			SCREEN_HEIGHT = d.getHeight();
		}
		_picture = R.drawable.instructionsa;
		invalidate();
	}

	public void onDraw(Canvas canvas) {
		Rect boundRect = new Rect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT-100);
		canvas.drawBitmap(
				BitmapFactory.decodeResource(getResources(), _picture), null,
				boundRect, null);
	}

	//TODO: Make a better check for button presses
	public boolean leftArrow(float x, float y) {
		int left = SCREEN_WIDTH * 65 / 480;
		int right = SCREEN_WIDTH * 140 / 480;
		int top = SCREEN_HEIGHT * 561 / 640;
		int bottom = SCREEN_HEIGHT * 617 / 640;
		return x > left && x < right && y > top && y < bottom;
	}

	public boolean rightArrow(float x, float y) {
		int left = SCREEN_WIDTH * 335 / 480;
		int right = SCREEN_WIDTH * 410 / 480;
		int top = SCREEN_HEIGHT * 561 / 640;
		int bottom = SCREEN_HEIGHT * 617 / 640;
		return x > left && x < right && y > top && y < bottom;
	}

	public boolean picture1() {
		return _picture == R.drawable.instructionsa;
	}
	
	public void switchPicture(){
		if(_picture == R.drawable.instructionsa){
			_picture = R.drawable.instructionsb;
		}
		else{
			_picture = R.drawable.instructionsa;
		}
		invalidate();
	}

}
