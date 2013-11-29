package com.ugent.networkplanningtool.data;

public class FloorPlanOperation {
	public static enum Type{
		ADD,
		REMOVE;
	}
	
	private Type type;
	private DataObject dataObject;

	public FloorPlanOperation(Type type, DataObject dataObject) {
		this.type = type;
		this.dataObject = dataObject;
	}
	
	public Type getType() {
		return type;
	}
	
	public DataObject getDataObject() {
		return dataObject;
	}
	
	

}
