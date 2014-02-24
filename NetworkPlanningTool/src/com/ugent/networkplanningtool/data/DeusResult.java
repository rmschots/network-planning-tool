package com.ugent.networkplanningtool.data;

import java.util.List;
import java.util.Vector;

public class DeusResult {

    private List<AccessPoint> accessPoints;
    private String benchmarks;
    private List<CSVResult> csv;
    private Double diffusePower;
    private Double[] grid;
    private Double[] info; //0 => aantal APs waaraan blootgesteld ; 1 => mediaan blootstelling ; 2 => p95 blootstelling
    private String infomsg;
    private Vector<Double> losPower;
    private FloorPlan normalizedPlan;
    private FloorPlan optimizedPlan;

    public DeusResult(List<AccessPoint> accessPoints, String benchmarks, List<CSVResult> csv, Double diffusePower, Double[] grid, Double[] info, String infomsg, Vector<Double> losPower, FloorPlan normalizedPlan, FloorPlan optimizedPlan) {
        this.accessPoints = accessPoints;
        this.benchmarks = benchmarks;
        this.csv = csv;
        this.diffusePower = diffusePower;
        this.grid = grid;
        this.info = info;
        this.infomsg = infomsg;
        this.losPower = losPower;
        this.normalizedPlan = normalizedPlan;
        this.optimizedPlan = optimizedPlan;
    }

    public List<AccessPoint> getAccessPoints() {
        return accessPoints;
    }

    public String getBenchmarks() {
        return benchmarks;
    }

    public List<CSVResult> getCsv() {
        return csv;
    }

    public Double getDiffusePower() {
        return diffusePower;
    }

    public Double[] getGrid() {
        return grid;
    }

    public Double[] getInfo() {
        return info;
    }

    public String getInfomsg() {
        return infomsg;
    }

    public Vector<Double> getLosPower() {
        return losPower;
    }

    public FloorPlan getNormalizedPlan() {
        return normalizedPlan;
    }

    public FloorPlan getOptimizedPlan() {
        return optimizedPlan;
    }
}
