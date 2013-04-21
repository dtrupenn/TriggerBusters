package com.example.triggerbusters;

import java.util.ArrayList;
import java.util.Random;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;

public class GameGridView extends GridView {


	Paint paint = new Paint();
	ArrayList<EvilCharacter> _villains = new ArrayList<EvilCharacter>();
	EvilCharacter _villain1;
	EvilCharacter _villain2;
	EvilCharacter _villain3;
	EvilCharacter _villain4;
	private boolean _gameOver;
	private long _stopTime;
	private boolean _registerStop;
	private Character _clutter;
	private int _meter;
	private boolean _paused;
	private boolean _clutterMoving;
	private boolean _collision;
	private int _meterPicture;
	private long _score;
	private long _clutterScore;
	private GridValue _encounteredObject;
	private int counter;
	
	// number of moves before villain's direction is recalculated
	private int  _villIncrement;

	public GameGridView(Context c) {
		super(c);
	}

	public GameGridView(Context c, AttributeSet a) {
		super(c, a);
		this.setGridOffset(6);
		asthma_kid = new Character(2, 12);
		_clutter = new Character(5, 6);
		_villain1 = new EvilCharacter(9, 2, Direction.Up, GridValue.CAT);
		_villain2 = new EvilCharacter(9, 2, Direction.Down, GridValue.SMOKE);
		_villain3 = new EvilCharacter(7, 2, Direction.Left, GridValue.MOLD);
		_villain4 = new EvilCharacter(11, 2, Direction.Right,
				GridValue.COCKROACH);
		_villains.add(_villain1);
		_villains.add(_villain2);
		_villains.add(_villain3);
		_villains.add(_villain4);
		_meter = 4;
		_villIncrement = 7;
		_meterPicture = R.drawable.meterfull;
		_counter = 0;
		fillGrid();
	}

	protected void timerCalled() {
		_collision = false;
		if (_paused) {
			return;
		}

		postInvalidate();
		// every 3 moves, recalculate the direction.
		moveCharacter();
	


		if(counter!=0){
			counter++;
			if(counter==6){
				for(EvilCharacter villain : _villains){
					moveVillainToStart(villain);
				}
				counter = 0;
			}
			else{
				makeVillainsDisappear();
			}
		}
		else{
			for (EvilCharacter villain : _villains) {
				// Every _villIncrement number of iterations, change the villain's direction
				if (_counter % _villIncrement == 0) {
					calculateVillainNewDirection(villain);
					continue;
				}
				if (!moveVillain(villain)) {
					calculateVillainNewDirection(villain);
				}
				_grid[villain.getY()][villain.getX()] = villain.getGridValue();
			}
		}
		// See if there is a collision now that the villains moved.
		if (!_collision) {
			checkCollision();
		}
		checkCollision();
		if (_clutterMoving) {
			clutterRelocate();
		}
		_counter++;
	}

	public long getStopTime() {
		return _stopTime;
	}

	/**
	 * hard codes the current "board"
	 */
	public void fillGrid() {
		// initialize all the values as sidewalk
		for (int i = 0; i < _ncols; i++) {
			for (int j = 0; j < _nrows; j++) {

				_grid[j][i] = GridValue.SIDEWALK;
			}
		}
		for (int i = 2; i < _ncols - 1; i++) {
			_grid[1][i] = GridValue.WALL_HORIZONTAL;
			_grid[_nrows / 2 + 2][i] = GridValue.WALL_HORIZONTAL;
		}

		for (int j = 2; j < _nrows / 4; j++) {
			_grid[j][1] = GridValue.WALL_VERTICAL;
			_grid[j][_ncols - 2] = GridValue.WALL_VERTICAL;
		}

		for (int j = _nrows / 4 + 5; j < _nrows / 2 + 3; j++) {
			_grid[j][1] = GridValue.WALL_VERTICAL;
			_grid[j][_ncols - 2] = GridValue.WALL_VERTICAL;
		}

		for (int i = 1; i < _ncols / 4; i++) {
			_grid[_ncols / 3][i] = GridValue.WALL_HORIZONTAL;
			_grid[_ncols / 3 + 6][i] = GridValue.WALL_HORIZONTAL;
		}

		for (int i = _ncols - 6; i < _ncols - 1; i++) {
			_grid[_nrows / 5][i] = GridValue.WALL_HORIZONTAL;
			_grid[_nrows / 5 + 1][i] = GridValue.WALL_HORIZONTAL;
		}

		for (int i = 0; i < _ncols / 4; i++) {
			_grid[_nrows / 4 + 1][i] = GridValue.WALL_HORIZONTAL;
			_grid[_nrows / 4 + 2][i] = GridValue.WALL_HORIZONTAL;
		}

		for (int i = _ncols - 6; i < _ncols; i++) {
			_grid[_nrows / 4 + 1][i] = GridValue.WALL_HORIZONTAL;
			_grid[_nrows / 4 + 2][i] = GridValue.WALL_HORIZONTAL;
		}

		_grid[_nrows / 4 - 1][_ncols / 4 - 1] = GridValue.WALL_HORIZONTAL;
		_grid[_nrows / 4 + 3][_ncols / 4 - 1] = GridValue.WALL_HORIZONTAL;

		_grid[_nrows / 4 - 1][_ncols - 6] = GridValue.WALL_HORIZONTAL;
		_grid[_nrows / 4 + 3][_ncols - 6] = GridValue.WALL_HORIZONTAL;

		_grid[_nrows / 2 - 1][2] = GridValue.WALL_HORIZONTAL;
		_grid[_nrows / 2 - 1][_ncols - 3] = GridValue.WALL_HORIZONTAL;

		for (int i = 3; i < 7; i++) {
			_grid[3][i] = GridValue.WALL_HORIZONTAL;
		}

		for (int i = 9; i < 13; i++) {
			_grid[3][i] = GridValue.WALL_HORIZONTAL;
		}

		for (int j = 5; j < 13; j++) {
			_grid[j][7] = GridValue.WALL_VERTICAL;

		}
		_grid[1][1] = GridValue.WALL_CORNER_UL;
		_grid[1][14] = GridValue.WALL_CORNER_UR;
		_grid[5][1] = GridValue.WALL_CORNER_LL;
		_grid[5][14] = GridValue.WALL_CORNER_LR;
		_grid[14][14] = GridValue.WALL_CORNER_LR;
		_grid[14][1] = GridValue.WALL_CORNER_LL;
		_grid[11][1] = GridValue.WALL_CORNER_UL;
		_grid[11][14] = GridValue.WALL_CORNER_UR;
		_grid[14][0] = GridValue.WALL_HORIZONTAL;
		_grid[14][15] = GridValue.WALL_HORIZONTAL;
		_grid[_clutter.getY()][_clutter.getX()] = GridValue.CLUTTER;
		// re-add trees for now
		// _grid[14][14] = GridValue.TREE;
		// _grid[10][1] = GridValue.TREE;
		// _grid[15][5] = GridValue.TREE;
		// _grid[2][0] = GridValue.TREE;
		// _grid[13][0] = GridValue.TREE;
		// _grid[3][2] = GridValue.TREE;
		// _grid[9][8] = GridValue.TREE;
		// _grid[0][9] = GridValue.TREE;
		// _grid[4][15] = GridValue.TREE;
	}

	protected boolean isEmpty(int x, int y) {
		return ((_grid[y][x] != GridValue.WALL_CORNER_LL)
				&& (_grid[y][x] != GridValue.WALL_CORNER_LR)
				&& (_grid[y][x] != GridValue.WALL_CORNER_UL)
				&& (_grid[y][x] != GridValue.WALL_CORNER_UR)
				&& (_grid[y][x] != GridValue.WALL_HORIZONTAL) && (_grid[y][x] != GridValue.WALL_VERTICAL));
	}

	public void changeCharacterPosition(int currX, int currY, int newX,
			int newY, int compareValue, int compareLower, int compareHigher) {
		if ((compareValue > compareLower) && (compareValue < compareHigher)
				&& ((isEmpty(newX, newY)))) {
			_grid[currY][currX] = GridValue.SIDEWALK;
			if (_grid[newY][newX] == GridValue.CLUTTER) {
				clutterReached();
			}
			asthma_kid.move(newX, newY);
		}
	}

	

	public boolean changeVillainPosition(int curr_x, int curr_y, int newX,
			int newY, int compareValue, int compareLower, int compareHigher,
			EvilCharacter villain) {
		if ((compareValue > compareLower)
				&& (compareValue < compareHigher)
				&& ((_grid[newY][newX] == GridValue.SIDEWALK)
						|| (_grid[newY][newX] == GridValue.CLUTTER) || (_grid[newY][newX] == GridValue.CHARACTER))) {
			if (!((curr_y == _clutter.getY()) && (curr_x == _clutter.getX()))) {
				_grid[curr_y][curr_x] = GridValue.SIDEWALK;
			} else {
				_grid[curr_y][curr_x] = GridValue.CLUTTER;
			}
			// move villain twice as slow
			if (_counter % 2 == 0)
			{
			villain.move(newX, newY);
			}
			
			return true;
		}
		return false;
	}

	public boolean moveVillain(EvilCharacter villain) {
		// get the current grid position of the character
		int curr_x = villain.getX();
		int curr_y = villain.getY();
		switch (villain.getDirection()) {
		case Up:
			if (changeVillainPosition(curr_x, curr_y, curr_x, curr_y - 1,
					curr_y - 1, 0, curr_y, villain)) {
				return true;
			}
			break;
		case Down:
			if (changeVillainPosition(curr_x, curr_y, curr_x, curr_y + 1,
					curr_y + 1, curr_y, _nrows - 6, villain)) {
				return true;
			}
			break;
		case Left:
			if (changeVillainPosition(curr_x, curr_y, curr_x - 1, curr_y,
					curr_x - 1, -1, curr_x, villain)) {
				return true;
			}
			break;
		case Right:
			if (changeVillainPosition(curr_x, curr_y, curr_x + 1, curr_y,
					curr_x + 1, curr_x, _ncols, villain)) {
				return true;
			}
		}
		return false;
	}

	private void changeVillainDirection(Direction one, Direction two,
			Direction three, Direction four, EvilCharacter villain) {
		villain.setDirection(one);
		if (!moveVillain(villain)) {
			villain.setDirection(two);
		} else
			return;
		if (!moveVillain(villain)) {
			villain.setDirection(three);
		} else
			return;
		if (!moveVillain(villain)) {
			villain.setDirection(four);
		}
	}

	private void calculateVillainNewDirection(EvilCharacter villain) {
		int curr_x = villain.getX();
		int curr_y = villain.getY();
		// figure out if it is farther away by x or by y
		int dx = curr_x - asthma_kid.getX();
		int dy = curr_y - asthma_kid.getY();
		if (Math.abs(dy) > Math.abs(dx)) {
			if (dy > 0) {
				changeVillainDirection(Direction.Up, Direction.Left,
						Direction.Right, Direction.Down, villain);
			} else {
				changeVillainDirection(Direction.Down, Direction.Left,
						Direction.Right, Direction.Up, villain);
			}
		} else {
			if (dx > 0) {
				changeVillainDirection(Direction.Left, Direction.Up,
						Direction.Right, Direction.Down, villain);
			} else {
				changeVillainDirection(Direction.Right, Direction.Up,
						Direction.Down, Direction.Left, villain);
			}
		}
	}
	
	/*
	 * Checks for collision based on directionality
	 */
	public boolean nextCollision() {
		Direction aDir = asthma_kid.getDirection();
		for (EvilCharacter villain : _villains) {
			switch (aDir) {
			case Up:
				if(villain.getDirection() == Direction.Down && 
					villain.getX() == asthma_kid.getX() &&
					(villain.getY() - 1) == asthma_kid.getY()) {
					return true;
				}
				break;
				
			case Down:
				if(villain.getDirection() == Direction.Up &&
					villain.getX() == asthma_kid.getX() &&
					(villain.getY() + 1) == asthma_kid.getY()) {
					return true;
				}
				break;
				
			case Left:
				if(villain.getDirection() == Direction.Right &&
					villain.getY() == asthma_kid.getY() &&
					(villain.getX() - 1) == asthma_kid.getY()) {
					return true;
				}
				break;
				
			case Right:
				if(villain.getDirection() == Direction.Left &&
					villain.getY() == asthma_kid.getY() &&
					(villain.getX() - 1) == asthma_kid.getY()) {
					return true;
				}
				break;
			}
		}
		return false;
	}

	public void checkCollision() {
		for (EvilCharacter villain : _villains) {
			if((villain.getX() == asthma_kid.getX()	&& villain.getY() == asthma_kid.getY()) ||//) {
					nextCollision()) {
				_grid[villain.getY()][villain.getX()] = GridValue.SIDEWALK;
				collision(villain);
			}
		}
	}

	private void moveVillainToStart(EvilCharacter villain) {
		villain.move(villain.getStartX(), villain.getStartY());
		villain.setDirection(villain.getStartDirection());
	}


	public void onDraw(Canvas canvas) {
		if (_gameOver) {
			handleGameOver(canvas);
			return;
		}
		fillGrid();
		drawBoard(canvas);
		drawVillains(canvas);
		drawCharacter(canvas);
		drawMeter(canvas);
		drawScore(canvas);
	}

	private void handleGameOver(Canvas canvas) {
		if (!_registerStop) {
			_stopTime = System.currentTimeMillis();
			_registerStop = true;
		}
		paint.setTextSize(30);
		canvas.drawText("Game over", 75, 75, paint);
		canvas.drawText("Hit any button to continue", 75, 120, paint);
	}

	private void drawBoard(Canvas canvas) {
		for (int i = 0; i < 15; i++) {
			for (int j = 0; j < _ncols; j++) {
				switch (_grid[i][j]) {
				case SIDEWALK:
					paint.setColor(Color.rgb(170, 170, 170));
					canvas.drawRect(j * PIXEL_WIDTH, i * PIXEL_HEIGHT, (j + 1)
							* PIXEL_WIDTH, (i + 1) * PIXEL_HEIGHT, paint);
					break;
				case WALL_CORNER_LL:
					drawWallCorner((j * PIXEL_WIDTH + PIXEL_WIDTH / 3),
							(i * PIXEL_HEIGHT),
							((j + 1) * PIXEL_WIDTH - PIXEL_WIDTH / 3), ((i + 1)
									* PIXEL_HEIGHT - PIXEL_HEIGHT / 3), (j
									* PIXEL_WIDTH + PIXEL_WIDTH / 3), (i
									* PIXEL_HEIGHT + PIXEL_HEIGHT / 3),
							((j + 1) * PIXEL_WIDTH),
							((i + 1) * PIXEL_HEIGHT - PIXEL_HEIGHT / 3), i, j,
							canvas);
					break;
				case WALL_CORNER_LR:
					drawWallCorner((j * PIXEL_WIDTH + PIXEL_WIDTH / 3),
							(i * PIXEL_HEIGHT),
							((j + 1) * PIXEL_WIDTH - PIXEL_WIDTH / 3), ((i + 1)
									* PIXEL_HEIGHT - PIXEL_HEIGHT / 3),
							(j * PIXEL_WIDTH),
							(i * PIXEL_HEIGHT + PIXEL_HEIGHT / 3), ((j + 1)
									* PIXEL_WIDTH - PIXEL_WIDTH / 3), ((i + 1)
									* PIXEL_HEIGHT - PIXEL_HEIGHT / 3), i, j,
							canvas);
					break;
				case WALL_CORNER_UL:
					drawWallCorner((j * PIXEL_WIDTH + PIXEL_WIDTH / 3), (i
							* PIXEL_HEIGHT + PIXEL_HEIGHT / 3), ((j + 1)
							* PIXEL_WIDTH - PIXEL_WIDTH / 3),
							((i + 1) * PIXEL_HEIGHT),
							(j * PIXEL_WIDTH + PIXEL_WIDTH / 3), (i
									* PIXEL_HEIGHT + PIXEL_HEIGHT / 3),
							((j + 1) * PIXEL_WIDTH),
							((i + 1) * PIXEL_HEIGHT - PIXEL_HEIGHT / 3), i, j,
							canvas);
					break;
				case WALL_CORNER_UR:
					drawWallCorner((j * PIXEL_WIDTH + PIXEL_WIDTH / 3), (i
							* PIXEL_HEIGHT + PIXEL_HEIGHT / 3), ((j + 1)
							* PIXEL_WIDTH - PIXEL_WIDTH / 3),
							((i + 1) * PIXEL_HEIGHT), (j * PIXEL_WIDTH), (i
									* PIXEL_HEIGHT + PIXEL_HEIGHT / 3),
							((j + 1) * PIXEL_WIDTH - PIXEL_WIDTH / 3), ((i + 1)
									* PIXEL_HEIGHT - PIXEL_HEIGHT / 3), i, j,
							canvas);
					break;
				case WALL_VERTICAL:
					drawRegularWall(j * PIXEL_WIDTH + PIXEL_WIDTH / 3, i
							* PIXEL_HEIGHT, (j + 1) * PIXEL_WIDTH - PIXEL_WIDTH
							/ 3, (i + 1) * PIXEL_HEIGHT, i, j, canvas);
					break;
				case WALL_HORIZONTAL:
					drawRegularWall(j * PIXEL_WIDTH, i * PIXEL_HEIGHT
							+ PIXEL_HEIGHT / 3, (j + 1) * PIXEL_WIDTH, (i + 1)
							* PIXEL_HEIGHT - PIXEL_HEIGHT / 3, i, j, canvas);
					break;
				case CLUTTER:
					drawClutterOrTree(canvas, i, j, R.drawable.dust);
					break;
				case TREE:
					drawClutterOrTree(canvas, i, j, R.drawable.tree);
					break;
				}
			}
		}
	}

	private void drawRegularWall(float R1, float R2, float R3, float R4, int i,
			int j, Canvas canvas) {
		paint.setColor(Color.rgb(170, 170, 170));
		canvas.drawRect(j * PIXEL_WIDTH, i * PIXEL_HEIGHT, (j + 1)
				* PIXEL_WIDTH, (i + 1) * PIXEL_HEIGHT, paint);
		// fills in "bricks" at positions where the grid=1
		if ((i + j) % 2 == 0) {
			paint.setColor(Color.rgb(128, 0, 0));
		} else
			paint.setColor(Color.rgb(205, 92, 92));
		canvas.drawRect(R1, R2, R3, R4, paint);

	}

	private void drawWallCorner(float R21, float R22, float R23, float R24,
			float R31, float R32, float R33, float R34, int i, int j,
			Canvas canvas) {
		// fills in "bricks" at positions where the grid=1
		Log.v("corner", "wall corner");
		paint.setColor(Color.rgb(170, 170, 170));
		canvas.drawRect(j * PIXEL_WIDTH, i * PIXEL_HEIGHT, (j + 1)
				* PIXEL_WIDTH, (i + 1) * PIXEL_HEIGHT, paint);
		if ((i + j) % 2 == 0) {
			paint.setColor(Color.rgb(128, 0, 0));
		} else
			paint.setColor(Color.rgb(205, 92, 92));
		canvas.drawRect(R21, R22, R23, R24, paint);
		canvas.drawRect(R31, R32, R33, R34, paint);
	}

	private void drawClutterOrTree(Canvas canvas, int i, int j, int picture) {
		Rect dstv = new Rect(j * PIXEL_WIDTH, i * PIXEL_HEIGHT, (j + 1)
				* PIXEL_WIDTH, (i + 1) * PIXEL_HEIGHT);
		canvas.drawBitmap(
				BitmapFactory.decodeResource(getResources(), picture), null,
				dstv, null);
	}

	private void drawVillains(Canvas canvas) {
		for (EvilCharacter villain : _villains) {
			Rect boundRect = new Rect(PIXEL_WIDTH * (villain.getX()),
					PIXEL_HEIGHT * (villain.getY()), PIXEL_WIDTH
							* (villain.getX() + 1), PIXEL_HEIGHT
							* (villain.getY() + 1));
			switch (villain.getGridValue()) {
			case CAT:
				canvas.drawBitmap(BitmapFactory.decodeResource(getResources(),
						R.drawable.cat), null, boundRect, null);
				break;
			case MOLD:
				canvas.drawBitmap(BitmapFactory.decodeResource(getResources(),
						R.drawable.mold), null, boundRect, null);
				break;
			case SMOKE:
				canvas.drawBitmap(BitmapFactory.decodeResource(getResources(),
						R.drawable.cigarette), null, boundRect, null);
				break;
			case COCKROACH:
				canvas.drawBitmap(BitmapFactory.decodeResource(getResources(),
						R.drawable.cockroach), null, boundRect, null);
				break;
			}
		}
	}



	private void drawMeter(Canvas canvas) {
		Rect meterRect = new Rect(0, 10 * SCREEN_HEIGHT / 16 - 30
				+ PIXEL_HEIGHT / 3, 14 * SCREEN_WIDTH / 16 - 60 -160, 11
				* SCREEN_HEIGHT / 16 + 50 + PIXEL_HEIGHT / 3-70);
		canvas.drawBitmap(
				BitmapFactory.decodeResource(getResources(), _meterPicture),
				null, meterRect, null);
	}

	private void drawScore(Canvas canvas) {
		paint.setTextSize(PIXEL_HEIGHT);
		paint.setColor(Color.BLUE);
		canvas.drawText("Score:", 15 * SCREEN_WIDTH / 16 - 70,
				10 * SCREEN_HEIGHT / 16 + 10, paint);
		canvas.drawText(Long.toString(_score), 15 * SCREEN_WIDTH / 16 - 70
				+ PIXEL_WIDTH, 10 * SCREEN_HEIGHT / 16 + PIXEL_HEIGHT + 10,
				paint);
		paint.setColor(Color.WHITE);
	}

	public boolean gameOver() {
		return _gameOver;
	}

	public void addTime(Long score) {
		_score = score + _clutterScore;
	}

	public Long getScore() {
		return _score;
	}

	private void clutterRelocate() {
		Random randX = new Random();
		Random randY = new Random();
		int x = randX.nextInt(15);
		int y = randY.nextInt(15);
		_clutter.move(x, y);
		if (_grid[_clutter.getY()][_clutter.getX()] != GridValue.SIDEWALK
				|| x < 1 || y < 1) {
			clutterRelocate();
		} else
			_grid[_clutter.getY()][_clutter.getX()] = GridValue.CLUTTER;
	}

	private void clutterReached() {
		_clutterMoving = true;
		_clutterScore += 40;
		return;
	}

	public boolean getClutterMoving() {
		return _clutterMoving;
	}

	public void setClutterMoving() {
		_clutterMoving = false;
	}

	private void collision(EvilCharacter villain) {
		_encounteredObject = villain.getGridValue();
		Log.v("We are here setting the counter to 1", "counter = 1");
		counter = 1;
		makeVillainsDisappear();
		_meter--;
		switch (_meter) {
		case (1):
			_meterPicture = R.drawable.meterthree;
			break;
		case (2):
			_meterPicture = R.drawable.metertwo;
			break;
		case (3):
			_meterPicture = R.drawable.meterone;
			break;
		default:
			_gameOver = true;
			_collision = false;
			return;
		}
		_collision = true;
	}

	private void makeVillainsDisappear() {
		int x,y;
		for(EvilCharacter villain : _villains){
			x = villain.getX();
			y = villain.getY();
			_grid[y][x]=GridValue.SIDEWALK;
			villain.move(1, 1);
		}
		
	}

	public boolean getCollision() {
		return _collision;
	}

	public void setCollision(boolean collided) {
		_collision = collided;
	}

	public void setPause() {
		_paused = !_paused;
	}

	public boolean getPause() {
		return _paused;
	}

	public GridValue getGridValue(int x, int y) {
		return _grid[y][x];
	}

	public GridValue getEncounteredObject() {
		return _encounteredObject;
	}

	// methods for testing

	public int getCharX() {
		return asthma_kid.getX();

	}

	public int getCharY() {
		return asthma_kid.getY();

	}

	public int getVill_1X() {
		return _villain1.getX();

	}

	public int getVill_1Y() {
		return _villain1.getY();

	}

	public int getVill_2X() {
		return _villain2.getX();

	}

	public int getVill_2Y() {
		return _villain2.getY();

	}

	public int getVill_3X() {
		return _villain3.getX();

	}

	public int getVill_3Y() {
		return _villain3.getY();

	}

	public int getVill_4X() {
		return _villain4.getX();

	}

	public int getVill_4Y() {
		return _villain4.getY();

	}

	public EvilCharacter getVillain() {
		return _villain1;

	}

	public void setGridValue(int x, int y, GridValue value) {
		_grid[y][x] = value;
	}

	public void setVillainPosition(int x, int y) {
		_villain1.move(x, y);
		_grid[y][x] = _villain1.getGridValue();
	}

	public int getMeter() {
		return _meter;
	}

	public void setMeter(int newmeter) {
		_meter = newmeter;
	}

}
