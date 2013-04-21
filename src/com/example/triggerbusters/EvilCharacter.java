package com.example.triggerbusters;

public class EvilCharacter extends Character {

	private int _startX;
	private int _startY;
	private Direction _startDirection;
	private GridValue _gridValue;
	public EvilCharacter(int x, int y, Direction d, GridValue g) {
		super(x, y);
		_gridValue = g;
		_startX = x;
		_startY = y;
		this.setDirection(d);
		_startDirection = d;
	}

	public int getStartX() {
		return _startX;
	}

	public int getStartY() {
		return _startY;
	}

	public Direction getStartDirection() {
		return _startDirection;
	}
	
	public GridValue getGridValue(){
		return _gridValue;
		
	}

}
