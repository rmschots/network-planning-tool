package com.ugent.networkplanningtool.data;

public class AccessPoint extends DataObject{
	
	private String name;
	private int height;
	
	private RadioType type;
	private RadioModel model;
	private int frequency;
	private int frequencyband;
	private int gain;
	private int power;
	private String network;

	
	
	public AccessPoint(int x, int y, String name, int height, RadioType type,
			RadioModel model, int frequency, int frequencyband, int gain,
			int power, String network) {
		super(x, y);
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
	public String getNetwork() {
		return network;
	}
}
