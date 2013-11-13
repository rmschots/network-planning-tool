package com.ugent.networkplanningtool.data;

public class Wall extends WallType{
	
	public Wall(int wallX1, int wallY1, Material material) {
		super(wallX1, wallY1, material);
	}
	
	public Wall(int wallX1, int wallY1, int wallX2, int wallY2, Material material) {
		super(wallX1, wallY1, wallX2, wallY2, material);
	}
}
