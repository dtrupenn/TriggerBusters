package com.example.triggerbusters;

public class Character {
	private int _x;
	private int _y;
	private Direction _direction;
	
	public Character(int x, int y){
		_x = x;
		_y = y;
		_direction=Direction.Right;
	}
	
	public void move(int x, int y){
		_x = x;
		_y = y;
	}
	
	public int getX(){
		return _x;
	}
	
	public int getY(){
		return _y;
	}
	
	public Direction getDirection(){
		return _direction;
	}
	
	public void setDirection(Direction direction){
		_direction=direction;
	}
	
}
