package com.ugent.networkplanningtool.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Observable;

import com.ugent.networkplanningtool.data.AccessPoint;
import com.ugent.networkplanningtool.data.DeusResult;

public class OptimizeResultModel extends Observable {

	private List<AccessPoint> apList;
	private String benchmarks;
	private List<DeusResult> resultList;
	private double[] infoArray; //0 => aantal APs waaraan blootgesteld ; 1 => mediaan blootstelling ; 2 => p95 blootstelling
	private String infomsg;
	private String normalizedPlan;
	private String optimizedPlan;

	private final static OptimizeResultModel instance= new OptimizeResultModel();

	private OptimizeResultModel() {
		apList = new ArrayList<AccessPoint>();
		resultList = new ArrayList<DeusResult>();
		infoArray = new double[3];
	}
	
	public void loadModel(List<AccessPoint> apList, String benchmarks, List<DeusResult> resultList, double[] infoArray, String infomsg, String normalizedPlan, String optimizedPlan){
		reset();
		this.apList.addAll(apList);
		this.benchmarks = benchmarks;
		this.resultList.addAll(resultList);
		System.arraycopy(infoArray, 0, this.infoArray, 0, infoArray.length );
		this.infomsg = infomsg;
		this.normalizedPlan = normalizedPlan;
		this.optimizedPlan = optimizedPlan;
		setChanged();
		notifyObservers();
	}

	private void reset() {
		apList.clear();
		benchmarks = null;
		resultList.clear();
		Arrays.fill(infoArray, 0.0);
		infomsg = "";
		normalizedPlan = null;
		optimizedPlan = null;
		setChanged();
		notifyObservers();
	}

	public static OptimizeResultModel getInstance() {
		return instance;
	}

	/**
	 * @return the apList
	 */
	public List<AccessPoint> getApList() {
		return apList;
	}

	/**
	 * @return the benchmarks
	 */
	public String getBenchmarks() {
		return benchmarks;
	}

	/**
	 * @return the resultList
	 */
	public List<DeusResult> getResultList() {
		return resultList;
	}

	/**
	 * @return the infoList
	 */
	public double[] getInfoArray() {
		return infoArray;
	}

	/**
	 * @return the infomsg
	 */
	public String getInfomsg() {
		return infomsg;
	}

	/**
	 * @return the normalizedPlan
	 */
	public String getNormalizedPlan() {
		return normalizedPlan;
	}

	/**
	 * @return the optimizedPlan
	 */
	public String getOptimizedPlan() {
		return optimizedPlan;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "OptimizeResultModel [apList=" + apList + ", benchmarks="
				+ benchmarks + ", resultList=" + resultList + ", infoArray="
				+ Arrays.toString(infoArray) + ", infomsg=" + infomsg
				+ ", normalizedPlan=" + normalizedPlan + ", optimizedPlan="
				+ optimizedPlan + "]";
	}
}
