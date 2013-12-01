package com.ugent.networkplanningtool.data;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;

import com.ugent.networkplanningtool.model.DrawingModel;

public class AccessPoint extends DataObject{
	
	private String name;
	private int height;
	
	private RadioType type;
	private RadioModel model;
	private int frequency;
	private int frequencyband;
	private int gain;
	private int power;
	private Network network;

	public AccessPoint(String name, int height, RadioType type,
			RadioModel model, int frequency, int frequencyband, int gain,
			int power, Network network) {
		super();
		this.name = name;
		this.height = height;
		this.type = type;
		this.model = model;
		this.frequency = frequency;
		this.frequencyband = frequencyband;
		this.gain = gain;
		this.power = power;
		this.network = network;
	}
	
	public AccessPoint(Point point, String name, int height, RadioType type,
			RadioModel model, int frequency, int frequencyband, int gain,
			int power, Network network) {
		super(point);
		this.name = name;
		this.height = height;
		this.type = type;
		this.model = model;
		this.frequency = frequency;
		this.frequencyband = frequencyband;
		this.gain = gain;
		this.power = power;
		this.network = network;
	}



	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}



	/**
	 * @return the height
	 */
	public int getHeight() {
		return height;
	}



	/**
	 * @return the type
	 */
	public RadioType getType() {
		return type;
	}



	/**
	 * @return the model
	 */
	public RadioModel getModel() {
		return model;
	}



	/**
	 * @return the frequency
	 */
	public int getFrequency() {
		return frequency;
	}



	/**
	 * @return the frequencyband
	 */
	public int getFrequencyband() {
		return frequencyband;
	}



	/**
	 * @return the gain
	 */
	public int getGain() {
		return gain;
	}



	/**
	 * @return the power
	 */
	public int getPower() {
		return power;
	}



	/**
	 * @return the network
	 */
	public Network getNetwork() {
		return network;
	}



	@Override
	public void drawOnCanvas(Canvas canvas, DrawingModel drawingModel, Paint paint) {
		float circleRadius1 = drawingModel.getPixelsPerInterval()/3;
		float circleRadius2 = circleRadius1*5/6;
		float x = convertCoordinateToLocation(drawingModel, true, getPoint1().x);
		float y = convertCoordinateToLocation(drawingModel, false, getPoint1().y);
		
		paint.setColor(Color.BLACK);
		canvas.drawCircle(x, y, circleRadius1, paint);
		paint.setColor(getNetwork().getColor());
		canvas.drawCircle(x, y, circleRadius2, paint);
	}



	@Override
	public DataObject getPartialDeepCopy() {
		return new AccessPoint(name, height, type, model, frequency, frequencyband, gain, power, network);
	}
}
