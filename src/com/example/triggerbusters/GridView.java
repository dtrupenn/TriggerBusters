package com.example.triggerbusters;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

public abstract class GridView extends View {
	protected GridValue[][] _grid;
	final int SCREEN_WIDTH;
	final int SCREEN_HEIGHT;
	protected int PIXEL_WIDTH;
	protected int PIXEL_HEIGHT;
	protected int _ncols = 16;
	protected int _nrows = 25;
	protected boolean _collision;
	private int _upPicture;
	private int _downPicture;
	private int _leftPicture;
	private int _rightPicture;
	protected Character asthma_kid;
	protected boolean _paused;
	private int gridoffset;
	protected int _counter;
	
	public GridView(Context c) {
		super(c);
		final WindowManager w = (WindowManager) c
				.getSystemService(Context.WINDOW_SERVICE);
		final Display d = w.getDefaultDisplay();
		SCREEN_WIDTH = d.getWidth();
		SCREEN_HEIGHT = d.getHeight();
		PIXEL_WIDTH = SCREEN_WIDTH / _ncols;
		PIXEL_HEIGHT = SCREEN_HEIGHT / _nrows;
		_grid = new GridValue[_nrows][_ncols];
		gridoffset=0;
		_counter =0;
		
	}

	public GridView(Context c, AttributeSet a) {
		super(c, a);
		final WindowManager w = (WindowManager) c
				.getSystemService(Context.WINDOW_SERVICE);
		final Display d = w.getDefaultDisplay();
		SCREEN_WIDTH = d.getWidth();
		SCREEN_HEIGHT = d.getHeight();
		PIXEL_WIDTH = SCREEN_WIDTH / _ncols;
		PIXEL_HEIGHT = SCREEN_HEIGHT / _nrows;
		_grid = new GridValue[_nrows][_ncols];
		asthma_kid = new Character(_nrows/2, _ncols/2);
		gridoffset=0;
		_counter =0;
	}
	
	public void setGender(Gender gender) {
		if (gender == Gender.Boy) {
			_upPicture = R.drawable.boy_up;
			_downPicture = R.drawable.boy_down;
			_leftPicture = R.drawable.boy_left;
			_rightPicture = R.drawable.boy_right;
		} else {
			_upPicture = R.drawable.girl_up;
			_downPicture = R.drawable.girl_down;
			_leftPicture = R.drawable.girl_left;
			_rightPicture = R.drawable.girl_right;
		}

	}
	public Character getCharacter() {
		return asthma_kid;
	}
	
	public void moveCharacter() {
		// get the current grid position of the character
		int curr_x = asthma_kid.getX();
		int curr_y = asthma_kid.getY();
		switch (asthma_kid.getDirection()) {
		case Up:
			changeCharacterPosition(curr_x, curr_y, curr_x, curr_y - 1,
					curr_y - 1, 0, curr_y);
			break;
		case Down:
			changeCharacterPosition(curr_x, curr_y, curr_x, curr_y + 1,
					curr_y + 1, curr_y, _nrows - gridoffset);
			break;
		case Left:
			changeCharacterPosition(curr_x, curr_y, curr_x - 1, curr_y,
					curr_x - 1, -1, curr_x);
			break;
		case Right:
			changeCharacterPosition(curr_x, curr_y, curr_x + 1, curr_y,
					curr_x + 1, curr_x, _ncols);
			break;
		}
		_grid[asthma_kid.getY()][asthma_kid.getX()] = GridValue.CHARACTER;
	}
	
	protected void drawCharacter(Canvas canvas) {
		Rect dst = new Rect(PIXEL_WIDTH * (asthma_kid.getX()), PIXEL_HEIGHT
				* (asthma_kid.getY()), PIXEL_WIDTH * (asthma_kid.getX() + 1),
				PIXEL_HEIGHT * (asthma_kid.getY() + 1));
		switch (asthma_kid.getDirection()) {
		case Up:
			canvas.drawBitmap(
					BitmapFactory.decodeResource(getResources(), _upPicture),
					null, dst, null);
			break;
		case Down:
			canvas.drawBitmap(
					BitmapFactory.decodeResource(getResources(), _downPicture),
					null, dst, null);
			break;
		case Left:
			canvas.drawBitmap(
					BitmapFactory.decodeResource(getResources(), _leftPicture),
					null, dst, null);
			break;
		case Right:
			canvas.drawBitmap(
					BitmapFactory.decodeResource(getResources(), _rightPicture),
					null, dst, null);
			break;
		}
	}
	
	
	public void setGridSize(int x, int y){
		_ncols = x;
		_nrows = y;
		PIXEL_WIDTH = SCREEN_WIDTH / _ncols;
		PIXEL_HEIGHT = SCREEN_HEIGHT / _nrows;
		
	}
	public boolean getCollision() {
		return _collision;
	}
	
	public void setPause() {
		_paused = !_paused;
	}

	public boolean getPause() {
		return _paused;
	}
	public int getCounter(){
		return _counter;
	}
	
	public void setGridOffset(int newoffset){
		gridoffset = newoffset;
		
	}
	
	
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
	
	public boolean upArrow(float x, float y) {
		int left = SCREEN_WIDTH * 65 / 480;
		int right = SCREEN_WIDTH * 140 / 480;
		int top = SCREEN_HEIGHT * 561 / 640;
		int bottom = SCREEN_HEIGHT * 617 / 640;
		return x > left && x < right && y > top && y < bottom;
	}

	public boolean downArrow(float x, float y) {
		int left = SCREEN_WIDTH * 335 / 480;
		int right = SCREEN_WIDTH * 410 / 480;
		int top = SCREEN_HEIGHT * 561 / 640;
		int bottom = SCREEN_HEIGHT * 617 / 640;
		return x > left && x < right && y > top && y < bottom;
	}
	
	
	protected abstract void timerCalled();
	protected abstract boolean isEmpty(int x, int y);
	protected abstract void onDraw(Canvas canvas);
	protected abstract void changeCharacterPosition(int currX, int currY, int newX,
			int newY, int compareValue, int compareLower, int compareHigher);
	
	protected abstract void checkCollision();
}