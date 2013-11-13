package com.ugent.networkplanningtool.data;


public abstract class WallType {
	
	private int x1 = -1;
	private int y1 = -1;
	private int x2 = -1;
	private int y2 = -1;
	
	private Material material;
	
	public WallType(int x1, int y1, Material material) {
		super();
		this.x1 = x1;
		this.y1 = y1;
		this.material = material;
	}

	public WallType(int x1, int y1, int x2, int y2, Material material) {
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
		this.material = material;
	}
	
	/**
	 * @return the x1
	 */
	public int getX1() {
		return x1;
	}

	/**
	 * @param x1 the x1 to set
	 */
	public void setX1(int x1) {
		this.x1 = x1;
	}

	/**
	 * @return the y1
	 */
	public int getY1() {
		return y1;
	}

	/**
	 * @param y1 the y1 to set
	 */
	public void setY1(int y1) {
		this.y1 = y1;
	}

	/**
	 * @return the x2
	 */
	public int getX2() {
		return x2;
	}

	/**
	 * @param x2 the x2 to set
	 */
	public void setX2(int x2) {
		this.x2 = x2;
	}
	
	/**
	 * @return the y2
	 */
	public int getY2() {
		return y2;
	}

	/**
	 * @param y2 the y2 to set
	 */
	public void setY2(int y2) {
		this.y2 = y2;
	}

	/**
	 * @return the Material
	 */
	public Material getMaterial() {
		return material;
	}

	/**
	 * @param color the Material to set
	 */
	public void setMaterial(Material material) {
		if(material != null){
			this.material = material;
		}
	}

	public boolean enoughData(){
		return x1 != -1 && x2 != -1 && y1 != -1 && y2 != -1;
	}

}
