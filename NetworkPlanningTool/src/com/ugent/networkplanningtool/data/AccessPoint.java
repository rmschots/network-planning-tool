package com.ugent.networkplanningtool.data;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
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
	
	public AccessPoint(AccessPoint accessPoint){
		super(accessPoint);
		this.name = new String(accessPoint.name);
		this.height = accessPoint.height;
		this.type = accessPoint.type;
		this.model = accessPoint.model;
		this.frequency = accessPoint.frequency;
		this.frequencyband = accessPoint.frequencyband;
		this.gain = accessPoint.gain;
		this.power = accessPoint.power;
		this.network = accessPoint.network;
	}

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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public RadioType getType() {
		return type;
	}

	public void setType(RadioType type) {
		this.type = type;
	}

	public RadioModel getModel() {
		return model;
	}

	public void setModel(RadioModel model) {
		this.model = model;
	}

	public int getFrequency() {
		return frequency;
	}

	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}

	public int getFrequencyband() {
		return frequencyband;
	}

	public void setFrequencyband(int frequencyband) {
		this.frequencyband = frequencyband;
	}

	public int getGain() {
		return gain;
	}

	public void setGain(int gain) {
		this.gain = gain;
	}

	public int getPower() {
		return power;
	}

	public void setPower(int power) {
		this.power = power;
	}

	public Network getNetwork() {
		return network;
	}

	public void setNetwork(Network network) {
		this.network = network;
	}

	@Override
	public void drawOnCanvas(Canvas canvas, DrawingModel drawingModel, Paint paint, boolean touch) {
		// TODO display gain+power
		float x = convertCoordinateToLocation(drawingModel, true, getPoint1().x);
		float y = convertCoordinateToLocation(drawingModel, false, getPoint1().y);
		float circleRadius;
		switch(type){
		case WIFI:
			circleRadius = drawingModel.getPixelsPerInterval()/4;
			paint.setColor(network.getColor());
			paint.setStyle(Style.FILL);
			canvas.drawCircle(x, y, circleRadius, paint);
			paint.setStyle(Style.STROKE);
			paint.setStrokeWidth(drawingModel.getPixelsPerInterval()/16);
			paint.setColor(Color.BLACK);
			canvas.drawCircle(x, y, circleRadius, paint);
			if(touch){
				paint.setColor(Color.RED);
				paint.setPathEffect(dottedLineEffect);
				canvas.drawCircle(x, y, circleRadius, paint);
			}
			break;
		case SENSOR:
			circleRadius = drawingModel.getPixelsPerInterval()/5;
			paint.setColor(network.getColor());
			paint.setStyle(Style.FILL);
			canvas.drawCircle(x, y, circleRadius, paint);
			paint.setStyle(Style.STROKE);
			paint.setStrokeWidth(drawingModel.getPixelsPerInterval()/16);
			paint.setColor(Color.BLACK);
			canvas.drawCircle(x, y, circleRadius, paint);
			if(touch){
				paint.setColor(Color.RED);
				paint.setPathEffect(dottedLineEffect);
				canvas.drawCircle(x, y, circleRadius, paint);
			}
			break;
		case LTE_FEMTOCELL:
		case UMTS_FEMTOCELL:
			float dist1 = drawingModel.getPixelsPerInterval()/3;
			float dist2 = dist1*4/6;
			paint.setStyle(Style.FILL);
			paint.setColor(network.getColor());
			Path p = new Path();
			p.reset();
			p.moveTo(x-dist1, y);
			p.lineTo(x-dist2, y-dist2);
			p.lineTo(x+dist2, y-dist2);
			
			p.lineTo(x+dist1, y);
			p.lineTo(x+dist2, y+dist2);
			p.lineTo(x-dist2, y+dist2);
			p.lineTo(x-dist1, y);
			p.lineTo(x-dist1, y);
			
			canvas.drawPath(p, paint);
			paint.setStrokeCap(Paint.Cap.ROUND);
			
			paint.setStyle(Style.STROKE);
			paint.setStrokeWidth(drawingModel.getPixelsPerInterval()/16);
			paint.setColor(Color.BLACK);
			canvas.drawPath(p, paint);
			if(touch){
				paint.setStrokeCap(Paint.Cap.BUTT);
				paint.setColor(Color.RED);
				paint.setPathEffect(dottedLineEffect);
				canvas.drawPath(p, paint);
			}
			break;
		}
		
		paint.reset();
	}



	@Override
	public DataObject getPartialDeepCopy() {
		return new AccessPoint(name, height, type, model, frequency, frequencyband, gain, power, network);
	}
}
