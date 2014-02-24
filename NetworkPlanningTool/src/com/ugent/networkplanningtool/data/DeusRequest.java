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
	private double grid_size;
	private String default_type;
	private String receiver_name;
	private double receiver_gain;
	private double receiver_height;
	private double interference;
	private double shadow_margin;
	private double fade_margin;
	private String ap_type;
	private int ap_frequency;
	private int ap_power;
	private int ap_gain;
	private int ap_height;
	private int max_e_field;
	private int distance_to_ap;
	private int function;
	private boolean frequency_planning;
	
	private DeusRequest(RequestType type, String clientVersion, String xml, String model,
			double grid_size, String default_type, String receiver_name,
			double receiver_gain, double receiver_height, double interference,
			double shadow_margin, double fade_margin, String ap_type,
			int ap_frequency, int ap_power, int ap_gain, int ap_height,
			int max_e_field, int distance_to_ap, int function,
			boolean frequency_planning) {
		this.type = type;
		this.clientVersion = clientVersion;
		this.xml = xml;
		this.model = model;
		this.grid_size = grid_size;
		this.default_type = default_type;
		this.receiver_name = receiver_name;
		this.receiver_gain = receiver_gain;
		this.receiver_height = receiver_height;
		this.interference = interference;
		this.shadow_margin = shadow_margin;
		this.fade_margin = fade_margin;
		this.ap_type = ap_type;
		this.ap_frequency = ap_frequency;
		this.ap_power = ap_power;
		this.ap_gain = ap_gain;
		this.ap_height = ap_height;
		this.max_e_field = max_e_field;
		this.distance_to_ap = distance_to_ap;
		this.function = function;
		this.frequency_planning = frequency_planning;
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
	 * @return the grid_size
	 */
	public double getGrid_size() {
		return grid_size;
	}

	/**
	 * @return the default_type
	 */
	public String getDefault_type() {
		return default_type;
	}

	/**
	 * @return the receiver_name
	 */
	public String getReceiver_name() {
		return receiver_name;
	}

	/**
	 * @return the receiver_gain
	 */
	public double getReceiver_gain() {
		return receiver_gain;
	}

	/**
	 * @return the receiver_height
	 */
	public double getReceiver_height() {
		return receiver_height;
	}

	/**
	 * @return the interference
	 */
	public double getInterference() {
		return interference;
	}

	/**
	 * @return the shadow_margin
	 */
	public double getShadow_margin() {
		return shadow_margin;
	}

	/**
	 * @return the fade_margin
	 */
	public double getFade_margin() {
		return fade_margin;
	}

	/**
	 * @return the ap_type
	 */
	public String getAp_type() {
		return ap_type;
	}

	/**
	 * @return the ap_frequency
	 */
	public int getAp_frequency() {
		return ap_frequency;
	}

	/**
	 * @return the ap_power
	 */
	public int getAp_power() {
		return ap_power;
	}

	/**
	 * @return the ap_gain
	 */
	public int getAp_gain() {
		return ap_gain;
	}

	/**
	 * @return the ap_height
	 */
	public int getAp_height() {
		return ap_height;
	}

	/**
	 * @return the max_e_field
	 */
	public int getMax_e_field() {
		return max_e_field;
	}

	/**
	 * @return the distance_to_ap
	 */
	public int getDistance_to_ap() {
		return distance_to_ap;
	}

	/**
	 * @return the function
	 */
	public int getFunction() {
		return function;
	}

	/**
	 * @return the frequency_planning
	 */
	public boolean isFrequency_planning() {
		return frequency_planning;
	}

}
