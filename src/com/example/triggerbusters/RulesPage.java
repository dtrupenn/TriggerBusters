package com.example.triggerbusters;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

public class RulesPage extends Activity {

	private boolean mouseDown;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rules);
	}

	public boolean onTouchEvent(MotionEvent e) {
		if (e.getAction() == MotionEvent.ACTION_UP) {
			mouseDown = false;
			return true;
		} else if (e.getAction() == MotionEvent.ACTION_DOWN) {
			if(mouseDown){
				return false;
			}
			mouseDown=true;
			RulesView view = (RulesView) (findViewById(R.id.rulesview1));
			float x = e.getX();
			float y = e.getY();
			if (view.rightArrow(x, y)) {
				if (view.picture1()) {
					view.switchPicture();
					return true;
				} else {
					Intent i = new Intent(this, PreScreen.class);
					startActivity(i);
					return true;
				}
			} else if (view.leftArrow(x, y)) {
				if (view.picture1()) {
					Intent i = new Intent(this, PreScreen.class);
					startActivity(i);
					return true;
				} else {
					view.switchPicture();
					return true;
				}
			}
		}
		return false;
	}

}
