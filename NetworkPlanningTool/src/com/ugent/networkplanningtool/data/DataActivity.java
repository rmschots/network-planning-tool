package com.ugent.networkplanningtool.data;

public class DataActivity extends DataObject{
	
	private ActivityType type;

	public DataActivity(int x, int y, ActivityType type) {
		super(x,y);
		this.type = type;
	}

	/**
	 * @return the type
	 */
	public ActivityType getType() {
		return type;
	}
}
