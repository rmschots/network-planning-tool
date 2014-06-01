package com.ugent.networkplanningtool.data.ServiceData;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;

import com.ugent.networkplanningtool.data.AccessPoint;
import com.ugent.networkplanningtool.data.FloorPlan;
import com.ugent.networkplanningtool.model.DrawingModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Represents the result returned by the web service
 */
public class DeusResult {

    /**
     * Possible types of returned results and contains legend display data
     */
    public enum ResultType {
        DOWNLOAD(new double[]{40, 21, 16, 10, 6, 6}, new int[]{Color.RED, Color.rgb(225, 130, 0), Color.YELLOW, Color.GREEN, Color.BLUE, Color.GRAY}),
        UPLOAD(new double[]{40, 21, 16, 10, 6, 6}, new int[]{Color.RED, Color.rgb(225, 130, 0), Color.YELLOW, Color.GREEN, Color.BLUE, Color.GRAY}),
        PATHLOSS(new double[]{90, 80, 70, 60, 60}, new int[]{Color.BLUE, Color.GREEN, Color.YELLOW, Color.rgb(225, 130, 0), Color.RED}),
        EFIELD(
                new double[]{5, 3, 2, 1, 0.7, 0.5, 0.2, 0.1, 0.07, 0.05, 0.02, 0.01, 0.007, 0.005, 0.002, 0.001, 0.001},
                new int[]{Color.WHITE, Color.rgb(236, 244, 248), Color.rgb(217, 233, 242), Color.rgb(199, 222, 235), Color.rgb(180, 211, 229), Color.rgb(161, 200, 223), Color.rgb(143, 189, 216), Color.rgb(124, 178, 210), Color.rgb(106, 168, 204), Color.rgb(92, 147, 178), Color.rgb(79, 126, 153), Color.rgb(66, 105, 127), Color.rgb(53, 84, 102), Color.rgb(39, 63, 76), Color.rgb(26, 42, 51), Color.rgb(13, 21, 25), Color.BLACK}),
        ABSORPTION(new double[]{100, 70, 50, 20, 10, 7, 5, 2, 1, 0.7, 0.5, 0.2, 0.2},
                new int[]{Color.WHITE, Color.rgb(254, 253, 233), Color.rgb(253, 247, 212), Color.rgb(251, 239, 191), Color.rgb(245, 226, 170), Color.rgb(236, 210, 148), Color.rgb(223, 191, 127), Color.rgb(204, 168, 106), Color.rgb(179, 141, 85), Color.rgb(147, 111, 63), Color.rgb(107, 77, 42), Color.rgb(58, 40, 21), Color.BLACK}),
        POWER_RX(new double[]{-20, -26, -32, -38, -44, -50, -56, -62, -68, -74, -80, -80},
                new int[]{Color.rgb(0, 102, 255), Color.rgb(0, 153, 255), Color.rgb(0, 204, 255), Color.rgb(0, 255, 255), Color.rgb(0, 255, 153), Color.rgb(0, 255, 51), Color.rgb(51, 255, 0), Color.rgb(153, 255, 0), Color.rgb(255, 255, 51), Color.rgb(255, 204, 0), Color.rgb(255, 102, 0), Color.RED}),
        POWER_TX(new double[]{23, 15, 7, -1, -9, -17, -25, -33, -41, -49, -57, -57},
                new int[]{Color.rgb(0, 102, 255), Color.rgb(0, 153, 255), Color.rgb(0, 204, 255), Color.rgb(0, 255, 255), Color.rgb(0, 255, 153), Color.rgb(0, 255, 51), Color.rgb(51, 255, 0), Color.rgb(153, 255, 0), Color.rgb(255, 255, 51), Color.rgb(255, 204, 0), Color.rgb(255, 102, 0), Color.RED});

        private double[] legends;
        private int[] colors;

        private ResultType(double[] legends, int[] colors) {
            this.legends = legends;
            this.colors = colors;
        }

        public double[] getLegends() {
            return legends;
        }

        public int[] getColors() {
            return colors;
        }

        public int getColor(Double value) {
            if (value == null) {
                System.out.println();
                return Color.TRANSPARENT;
            }
            for (int i = 0; i < legends.length; i++) {
                if (value >= legends[i]) {
                    return colors[i];
                }
            }
            return colors[legends.length - 1];
        }
    }

    private final DeusRequest.RequestType requestType;
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

    private Bitmap[] resultBitmaps = new Bitmap[ResultType.values().length];
    private double scale;

    /**
     * Constructor setting all variables
     * @param requestType the type of the request (and thus result)
     * @param accessPoints the returned access point
     * @param benchmarks the returned benchmarks
     * @param csv the returned location specific results
     * @param diffusePower the returned diffuse power
     * @param grid the returned grid size
     * @param info the returned extra info
     * @param infomsg the returned info message
     * @param losPower the return los power
     * @param normalizedPlan the returned normalized floor plan
     * @param optimizedPlan the returned optimized floor plan
     */
    public DeusResult(DeusRequest.RequestType requestType, List<AccessPoint> accessPoints, String benchmarks, List<CSVResult> csv, Double diffusePower, Double[] grid, Double[] info, String infomsg, Vector<Double> losPower, FloorPlan normalizedPlan, FloorPlan optimizedPlan) {
        this.requestType = requestType;
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

        generateBitmaps();
    }

    /**
     * Shifts the location specific receive power by the given shift
     * @param shift the mount to shift the location specific receive power
     */
    public void shiftResults(double shift) {
        for (CSVResult csvResult : getCsv()) {
            csvResult.setPowerRX(csvResult.getPowerRX() + shift);
        }
        generateBitmaps();
    }

    /**
     * Generates the bitmaps used to draw the location specific results
     */
    private void generateBitmaps() {
        int width = DrawingModel.FLOOR_WIDTH / 10;
        int height = DrawingModel.FLOOR_HEIGHT / 10;

        scale = 1.0 / 10.0;
        Canvas[] canvases = new Canvas[resultBitmaps.length];
        for (int i = 0; i < resultBitmaps.length; i++) {
            resultBitmaps[i] = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            canvases[i] = new Canvas(resultBitmaps[i]);
        }
        Paint p = new Paint();
        for (CSVResult csvR : csv) {
            int x1 = (csvR.getPoint1().x - 10) / 10;
            int x2 = (csvR.getPoint1().x + 10) / 10;
            int y1 = (csvR.getPoint1().y - 10) / 10;
            int y2 = (csvR.getPoint1().y + 10) / 10;

            p.setColor(ResultType.DOWNLOAD.getColor(csvR.getDownload()));
            canvases[ResultType.DOWNLOAD.ordinal()].drawRect(x1, y1, x2, y2, p);

            p.setColor(ResultType.UPLOAD.getColor(csvR.getUpload()));
            canvases[ResultType.UPLOAD.ordinal()].drawRect(x1, y1, x2, y2, p);

            p.setColor(ResultType.PATHLOSS.getColor(csvR.getPathloss()));
            canvases[ResultType.PATHLOSS.ordinal()].drawRect(x1, y1, x2, y2, p);

            p.setColor(ResultType.EFIELD.getColor(csvR.geteField()));
            canvases[ResultType.EFIELD.ordinal()].drawRect(x1, y1, x2, y2, p);

            p.setColor(ResultType.ABSORPTION.getColor(csvR.getAbsorption()));
            canvases[ResultType.ABSORPTION.ordinal()].drawRect(x1, y1, x2, y2, p);

            p.setColor(ResultType.POWER_RX.getColor(csvR.getPowerRX()));
            canvases[ResultType.POWER_RX.ordinal()].drawRect(x1, y1, x2, y2, p);

            p.setColor(ResultType.POWER_TX.getColor(csvR.getPowerTX()));
            canvases[ResultType.POWER_TX.ordinal()].drawRect(x1, y1, x2, y2, p);
        }
    }

    /**
     * returns the request type
     * @return the request type
     */
    public DeusRequest.RequestType getRequestType() {
        return requestType;
    }

    /**
     * Returns the returned access points
     * @return the returned access points
     */
    public List<AccessPoint> getAccessPoints() {
        return accessPoints;
    }

    /**
     * returns the benchmarks
     * @return the benchmarks
     */
    public String getBenchmarks() {
        return benchmarks;
    }

    /**
     * Returns the returned location specific results
     * @return the returned location specific results
     */
    public List<CSVResult> getCsv() {
        return csv;
    }

    /**
     * Returns the returned diffuse power
     * @return the returned diffuse power
     */
    public Double getDiffusePower() {
        return diffusePower;
    }

    /**
     * Returns the grid size
     * @return the grid size
     */
    public Double[] getGrid() {
        return grid;
    }

    /**
     * Returns the returned extra info
     * @return the returned extra info
     */
    public Double[] getInfo() {
        return info;
    }

    /**
     * Returns the returned info message
     * @return the returned info message
     */
    public String getInfomsg() {
        return infomsg;
    }

    /**
     * Returns the returned los power
     * @return the returned los power
     */
    public Vector<Double> getLosPower() {
        return losPower;
    }

    /**
     * Returns the normalized floor plan
     * @return the normalized floor plan
     */
    public FloorPlan getNormalizedPlan() {
        return normalizedPlan;
    }

    /**
     * Returns the optimized floor plan
     * @return the optimized floor plan
     */
    public FloorPlan getOptimizedPlan() {
        return optimizedPlan;
    }

    /**
     * Draws the location specific results
     * @param canvas the canvas to draw on
     * @param drawingModel the DrawingModel used for information how to draw exactly
     * @param rt the type of result to draw
     */
    public void drawResult(Canvas canvas, DrawingModel drawingModel, ResultType rt) {
        Matrix m = new Matrix();
        double newScale = drawingModel.getPixelsPerInterval() / (scale * DrawingModel.INTERVAL);
        m.postScale((float) newScale, (float) newScale);
        float offsetX = drawingModel.getOffsetX();
        float offsetY = drawingModel.getOffsetY();

        float offsetXpx = offsetX * drawingModel.getPixelsPerInterval() / DrawingModel.INTERVAL;
        float offsetYpx = offsetY * drawingModel.getPixelsPerInterval() / DrawingModel.INTERVAL;
        m.postTranslate(-offsetXpx, -offsetYpx);
        canvas.drawBitmap(resultBitmaps[rt.ordinal()], m, new Paint());
    }

    /**
     * Returns all types of results returned by the web service
     * @return all types of results returned by the web service
     */
    public List<ResultType> getResultTypes() {
        ArrayList<ResultType> rts = new ArrayList<ResultType>();
        for (ResultType rt : ResultType.values()) {
            if (resultBitmaps[rt.ordinal()] != null) {
                rts.add(rt);
            }
        }
        return rts;
    }

    /**
     * Returns the returned extra info including amount of access points, median exposure and P95 exposure
     * @return the returned extra info including amount of access points, median exposure and P95 exposure
     */
    public String getInfoAsString(){
        if(info==null || info.length < 3){
            return "";
        }
        return info[0]+" accesspoint(s)\n" +
                "Median exposure: "+info[1]+" V/m\n" +
                "P95 exposure: "+info[2]+" V/m\n";
    }
}
