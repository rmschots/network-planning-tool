package com.ugent.networkplanningtool.data;

public class DeusRequest {
	public static enum RequestType{
		PREDICT_COVERAGE,
		OPTIMAL_PLACEMENT,
		EXPOSURE_REDUCTION,
		NETWORK_REDUCTION,
		ESTIMATE_SAR
	}
	
	private final RequestType type;
	private String clientVersion;
	private String xml;
	private String model;
	private double gridSize;
    private double roomHeightM;
	private String defaultType;
	private String receiverName;
	private double receiverGain;
	private double receiverHeight;
	private double interference;
	private double shadowMargin;
	private double fadeMargin;
	private String apType;
	private int apFrequency;
	private int apPower;
	private int apGain;
	private int apHeight;
	private int maxEField;
	private int distanceToAp;
	private int function;
	private boolean frequencyPlanning;

    public DeusRequest(RequestType type, String clientVersion, String xml, String model, double gridSize, double roomHeightM, String defaultType, String receiverName, double receiverGain, double receiverHeight, double interference, double shadowMargin, double fadeMargin, String apType, int apFrequency, int apPower, int apGain, int apHeight, int maxEField, int distanceToAp, int function, boolean frequencyPlanning) {
        this.type = type;
        this.clientVersion = clientVersion;
        this.xml = xml;
        this.model = model;
        this.gridSize = gridSize;
        this.roomHeightM = roomHeightM;
        this.defaultType = defaultType;
        this.receiverName = receiverName;
        this.receiverGain = receiverGain;
        this.receiverHeight = receiverHeight;
        this.interference = interference;
        this.shadowMargin = shadowMargin;
        this.fadeMargin = fadeMargin;
        this.apType = apType;
        this.apFrequency = apFrequency;
        this.apPower = apPower;
        this.apGain = apGain;
        this.apHeight = apHeight;
        this.maxEField = maxEField;
        this.distanceToAp = distanceToAp;
        this.function = function;
        this.frequencyPlanning = frequencyPlanning;
    }

    /**
	 * @return the type
	 */
	public RequestType getType() {
		return type;
	}

	/**
	 * @return the clientVersion
	 */
	public String getClientVersion() {
		return clientVersion;
	}

	/**
	 * @return the xml
	 */
	public String getXml() {
		return xml;
	}

	/**
	 * @return the model
	 */
	public String getModel() {
		return model;
	}

	/**
	 * @return the gridSize
	 */
	public double getGridSize() {
		return gridSize;
	}

    public double getRoomHeightM() {
        return roomHeightM;
    }

    /**
	 * @return the defaultType
	 */
	public String getDefaultType() {
		return defaultType;
	}

	/**
	 * @return the receiverName
	 */
	public String getReceiverName() {
		return receiverName;
	}

	/**
	 * @return the receiverGain
	 */
	public double getReceiverGain() {
		return receiverGain;
	}

	/**
	 * @return the receiverHeight
	 */
	public double getReceiverHeight() {
		return receiverHeight;
	}

	/**
	 * @return the interference
	 */
	public double getInterference() {
		return interference;
	}

	/**
	 * @return the shadowMargin
	 */
	public double getShadowMargin() {
		return shadowMargin;
	}

	/**
	 * @return the fadeMargin
	 */
	public double getFadeMargin() {
		return fadeMargin;
	}

	/**
	 * @return the apType
	 */
	public String getApType() {
		return apType;
	}

	/**
	 * @return the apFrequency
	 */
	public int getApFrequency() {
		return apFrequency;
	}

	/**
	 * @return the apPower
	 */
	public int getApPower() {
		return apPower;
	}

	/**
	 * @return the apGain
	 */
	public int getApGain() {
		return apGain;
	}

	/**
	 * @return the apHeight
	 */
	public int getApHeight() {
		return apHeight;
	}

	/**
	 * @return the maxEField
	 */
	public int getMaxEField() {
		return maxEField;
	}

	/**
	 * @return the distanceToAp
	 */
	public int getDistanceToAp() {
		return distanceToAp;
	}

	/**
	 * @return the function
	 */
	public int getFunction() {
		return function;
	}

	/**
	 * @return the frequencyPlanning
	 */
	public boolean isFrequencyPlanning() {
		return frequencyPlanning;
	}

}
