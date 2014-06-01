package com.ugent.networkplanningtool.data.ServiceData;

/**
 * Represents a request to the web service
 */
public class DeusRequest {

    /**
     * The different types of requests
     */
    public static enum RequestType {
        PREDICT_COVERAGE,
        OPTIMAL_PLACEMENT,
        EXPOSURE_REDUCTION,
        NETWORK_REDUCTION,
        ESTIMATE_SAR
    }

    private final RequestType type;
    private static final String clientVersion = "1.8.0.a";
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
    private double apPower;
    private double apGain;
    private double apHeight;
    private double maxEField;
    private int distanceToAp;
    private int function;
    private boolean frequencyPlanning;

    /**
     * Constructor setting all variables
     * @param type the type of request
     * @param xml the floor plan as XML String
     * @param model the path loss model to use
     * @param gridSize the grid size
     * @param roomHeightM the room height
     * @param defaultType the default type of access point to generate
     * @param receiverName the receiver name
     * @param receiverGain the receiver gain
     * @param receiverHeight the height the receiver is at
     * @param interference the interference
     * @param shadowMargin the shadow margin for the model
     * @param fadeMargin the fade margin for the model
     * @param apType the generated access point type
     * @param apFrequency the generated access point frequency
     * @param apPower the generated access point power
     * @param apGain the generated access point gain
     * @param apHeight the generated access point height
     * @param maxEField the maximum electric field
     * @param distanceToAp the maximum distance to the access point
     * @param function method to place access point (default 1)
     * @param frequencyPlanning whether to take frequency into account
     */
    public DeusRequest(RequestType type, String xml, String model, double gridSize, double roomHeightM, String defaultType, String receiverName, double receiverGain, double receiverHeight, double interference, double shadowMargin, double fadeMargin, String apType, int apFrequency, double apPower, double apGain, double apHeight, double maxEField, int distanceToAp, int function, boolean frequencyPlanning) {
        this.type = type;
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
     * Returns the type of request
     * @return the type of request
     */
    public RequestType getType() {
        return type;
    }

    /**
     * Returns the client version
     * @return the client version
     */
    public String getClientVersion() {
        return clientVersion;
    }

    /**
     * Returns the floor plan as XML String
     * @return the floor plan as XML String
     */
    public String getXml() {
        return xml;
    }

    /**
     * Returns the path loss model to use
     * @return the path loss model to use
     */
    public String getModel() {
        return model;
    }

    /**
     * Returns the grid size
     * @return the grid size
     */
    public double getGridSize() {
        return gridSize;
    }

    /**
     * Returns the room height
     * @return the room height
     */
    public double getRoomHeightM() {
        return roomHeightM;
    }

    /**
     * Returns the default type of access point to generate
     * @return the default type of access point to generate
     */
    public String getDefaultType() {
        return defaultType;
    }

    /**
     * Returns the receiver name
     * @return the receiver name
     */
    public String getReceiverName() {
        return receiverName;
    }

    /**
     * Returns the receiver gain
     * @return the receiver gain
     */
    public double getReceiverGain() {
        return receiverGain;
    }

    /**
     * Returns the height the receiver is at
     * @return the height the receiver is at
     */
    public double getReceiverHeight() {
        return receiverHeight;
    }

    /**
     * Returns the interference
     * @return the interference
     */
    public double getInterference() {
        return interference;
    }

    /**
     * Returns the shadow margin for the model
     * @return the shadow margin for the model
     */
    public double getShadowMargin() {
        return shadowMargin;
    }

    /**
     * Returns the fade margin for the model
     * @return the fade margin for the model
     */
    public double getFadeMargin() {
        return fadeMargin;
    }

    /**
     * Returns the generated access point type
     * @return the generated access point type
     */
    public String getApType() {
        return apType;
    }

    /**
     * Returns the generated access point frequency
     * @return the generated access point frequency
     */
    public int getApFrequency() {
        return apFrequency;
    }

    /**
     * Returns the generated access point power
     * @return the generated access point power
     */
    public double getApPower() {
        return apPower;
    }

    /**
     * Returns the generated access point gain
     * @return the generated access point gain
     */
    public double getApGain() {
        return apGain;
    }

    /**
     * Returns the generated access point height
     * @return the generated access point height
     */
    public double getApHeight() {
        return apHeight;
    }

    /**
     * Returns the maximum electric field
     * @return the maximum electric field
     */
    public double getMaxEField() {
        return maxEField;
    }

    /**
     * Returns the maximum distance to the access point
     * @return the maximum distance to the access point
     */
    public int getDistanceToAp() {
        return distanceToAp;
    }

    /**
     * Returns the method to place access point (default 1)
     * @return the method to place access point (default 1)
     */
    public int getFunction() {
        return function;
    }

    /**
     * Returns whether to take frequency into account
     * @return whether to take frequency into account
     */
    public boolean isFrequencyPlanning() {
        return frequencyPlanning;
    }

}
