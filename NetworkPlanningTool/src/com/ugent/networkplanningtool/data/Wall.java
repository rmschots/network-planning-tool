package com.ugent.networkplanningtool.data;

public class Wall {
	private int wallX1;
	private int wallY1;
	private int wallX2;
	private int wallY2;
	
	public Wall(int wallX1, int wallY1) {
		super();
		this.wallX1 = wallX1;
		this.wallY1 = wallY1;
		this.wallX2 = -1;
		this.wallY2 = -1;
	}
	
	public Wall(int wallX1, int wallY1, int wallX2, int wallY2) {
		super();
		this.wallX1 = wallX1;
		this.wallY1 = wallY1;
		this.wallX2 = wallX2;
		this.wallY2 = wallY2;
	}

	public int getWallX1() {
		return wallX1;
	}

	public void setWallX1(int wallX1) {
		this.wallX1 = wallX1;
	}

	public int getWallY1() {
		return wallY1;
	}

	public void setWallY1(int wallY1) {
		this.wallY1 = wallY1;
	}

	public int getWallX2() {
		return wallX2;
	}

	public void setWallX2(int wallX2) {
		this.wallX2 = wallX2;
	}

	public int getWallY2() {
		return wallY2;
	}

	public void setWallY2(int wallY2) {
		this.wallY2 = wallY2;
	}
	
	public boolean isConnected(){
		return wallX2 != -1 && wallY2 != -1;
	}
}
