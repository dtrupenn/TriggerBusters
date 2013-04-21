package com.example.triggerbusters;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

public class PreGameView extends GridView {
	Character _inhaler;

	public PreGameView(Context c) {
		super(c);
		asthma_kid = new Character(0, 0);
		_inhaler = new Character(5, 5);
		_grid[5][5] = GridValue.INHALER;
		_collision = false;
		this.setGridSize(8, 12);
		this.setGridOffset(3);
	}

	public PreGameView(Context c, AttributeSet a) {
		super(c, a);
		asthma_kid = new Character(0, 0);
		_inhaler = new Character(5, 5);
		_grid[5][5] = GridValue.INHALER;
		_collision = false;
		this.setGridSize(8, 12);
		this.setGridOffset(3);
	}

	public void timerCalled() {
		_collision = false;
		if (_paused) {
			return;
		}
		// every 3 moves, recalculate the direction.
		moveCharacter();
		checkCollision();
		if (_counter % 3 == 0) {
			_counter = 0;
		}

		if (!_collision) {
			checkCollision();
		}
		_counter++;
		postInvalidate();

	}

	private void fillGrid() {
		for (int i = 0; i < _nrows; i++) {
			for (int j = 0; j < _ncols; j++) {
				if ((_grid[i][j] != GridValue.CHARACTER)
						&& (_grid[i][j] != GridValue.INHALER))
					_grid[i][j] = GridValue.SIDEWALK;
			}

		}

	}

	protected boolean isEmpty(int x, int y) {
		if (_inhaler.getX() == x && _inhaler.getY() == y) {
			return false;
		}
		return true;
	}

	protected void onDraw(Canvas canvas) {
		fillGrid();
		drawSidewalk(canvas);
		drawCharacter(canvas);
		int inhale_x = _inhaler.getX();
		int inhale_y = _inhaler.getY();

		Rect dstv = new Rect(inhale_y * PIXEL_WIDTH, inhale_x * PIXEL_HEIGHT,
				(inhale_y + 1) * PIXEL_WIDTH, (inhale_x + 1) * PIXEL_HEIGHT);

		canvas.drawBitmap(BitmapFactory.decodeResource(getResources(),
				R.drawable.inhaler), null, dstv, null);

	}

	private void drawSidewalk(Canvas canvas) {
		Paint paint = new Paint();
		for (int i = 0; i < _nrows; i++) {
			for (int j = 0; j < _ncols; j++) {
				if (_grid[i][j] == GridValue.SIDEWALK) {
					paint.setColor(Color.rgb(170, 170, 170));
					canvas.drawRect(j * PIXEL_WIDTH, i * PIXEL_HEIGHT, (j + 1)
							* PIXEL_WIDTH, (i + 1) * PIXEL_HEIGHT, paint);

				}
			}
		}

	}

	protected void checkCollision() {

		if (_inhaler.getX() == asthma_kid.getX()
				&& _inhaler.getY() == asthma_kid.getY()) {
			_collision = true;
			postInvalidate();
			Log.v("found collision", "collision");
		}

	}

	protected void changeCharacterPosition(int currX, int currY, int newX,
			int newY, int compareValue, int compareLower, int compareHigher) {
		if ((compareValue > compareLower) && (compareValue < compareHigher)
				&& (isEmpty(newX, newY))) {
			_grid[currY][currX] = GridValue.SIDEWALK;
			asthma_kid.move(newX, newY);

		} else if (!isEmpty(newX, newY)) {
			_collision = true;
		}

	}

}
