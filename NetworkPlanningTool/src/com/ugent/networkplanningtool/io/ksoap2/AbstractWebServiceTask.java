package com.ugent.networkplanningtool.io.ksoap2;

import android.graphics.Point;
import android.os.AsyncTask;
import android.util.Log;

import com.ugent.networkplanningtool.data.AccessPoint;
import com.ugent.networkplanningtool.data.FloorPlan;
import com.ugent.networkplanningtool.data.ServiceData.CSVResult;
import com.ugent.networkplanningtool.data.ServiceData.DeusResult;
import com.ugent.networkplanningtool.data.enums.Frequency;
import com.ugent.networkplanningtool.data.enums.FrequencyBand;
import com.ugent.networkplanningtool.data.enums.RadioModel;
import com.ugent.networkplanningtool.data.enums.RadioType;
import com.ugent.networkplanningtool.io.FloorPlanIO;

import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractWebServiceTask<P, R> extends AsyncTask<P, Void, R> {

    private final String TAG = getClass().getName();

    private OnAsyncTaskCompleteListener<R> taskCompletionListener;
    private WebServiceTaskManager progressTracker;
    // Most recent exception (used to diagnose failures)
    private Exception mostRecentException;

    public AbstractWebServiceTask() {
    }

    public final void setOnTaskCompletionListener(OnAsyncTaskCompleteListener<R> taskCompletionListener) {
        this.taskCompletionListener = taskCompletionListener;
    }

    public final void setProgressTracker(WebServiceTaskManager progressTracker) {
        if (progressTracker != null) {
            this.progressTracker = progressTracker;
        }
    }

    @Override
    protected final void onPreExecute() {
        if (progressTracker != null) {
            this.progressTracker.onStartProgress();
        }
    }

    /**
     * Invoke the web service request
     */
    @Override
    protected final R doInBackground(P... parameters) {
        mostRecentException = null;
        R result = null;

        try {
            result = performTaskInBackground(parameters[0]);
        } catch (Exception e) {
            Log.e(TAG, "Failed to invoke the web service: ", e);
            mostRecentException = e;
        }

        return result;
    }

    protected abstract R performTaskInBackground(P parameter) throws Exception;

    /**
     * @param result to be sent back to the observer (typically an {@link android.app.Activity} running on the UI Thread). This can be <code>null</code> if
     *               an error occurs while attempting to invoke the web service (e.g. web service was unreachable, or network I/O issue etc.)
     */
    @Override
    protected final void onPostExecute(R result) {
        if (progressTracker != null) {
            progressTracker.onStopProgress();
        }

        if (taskCompletionListener != null) {
            if (result == null || mostRecentException != null) {
                taskCompletionListener.onTaskFailed(mostRecentException);

            } else {
                taskCompletionListener.onTaskCompleteSuccess(result);
            }
        }

        // clean up listeners since we are done with this task
        progressTracker = null;
        taskCompletionListener = null;
    }

    protected DeusResult parseDeusResult(SoapObject so) throws ServiceException {
        System.out.println("PIEMELS: " + so.toString());
        if (so.hasProperty("errormsg")) {
            throw new ServiceException(so.getPropertyAsString("errormsg"));
        }

        Double[] infoArray = new Double[3];
        int infoIndex = 0;
        PropertyInfo ai = new PropertyInfo();
        for (int i = 0; i < so.getPropertyCount(); i++) {
            so.getPropertyInfo(i, ai);
            if (ai.getName().equals("info")) {
                infoArray[infoIndex] = Double.parseDouble(ai.getValue().toString());
                System.out.println(infoArray[infoIndex]);
                infoIndex++;
            }
        }
        List<AccessPoint> accessPoints = new ArrayList<AccessPoint>();
        if (so.hasProperty("accesspoints")) {
            accessPoints = parseAccessPoints(so.getPropertyAsString("accesspoints"));
        }

        String benchmarks = so.getPropertyAsString("benchmarks");
        List<CSVResult> csv = null;
        if (so.hasProperty("csv")) {
            csv = parseCSV(so.getPropertyAsString("csv"));
        }

        FloorPlan normalizedPlan = null;
        if (so.hasProperty("normalizedPlan") && !so.getPropertyAsString("normalizedPlan").equals("anyType{}")) {
            try {
                normalizedPlan = FloorPlanIO.loadFloorPlan(so.getPropertyAsString("normalizedPlan"));
            } catch (Exception e) {
                throw new ServiceException("Error parsing service response normalized xml");
            }
        }
        FloorPlan optimizedPlan = null;
        if (so.hasProperty("optimizedPlan") && !so.getPropertyAsString("optimizedPlan").equals("anyType{}")) {
            try {
                optimizedPlan = FloorPlanIO.loadFloorPlan(so.getPropertyAsString("optimizedPlan"));
            } catch (Exception e) {
                throw new ServiceException("Error parsing service response normalized xml");
            }
        }

        return new DeusResult(accessPoints, benchmarks, csv, null, null, infoArray, null, null, normalizedPlan, optimizedPlan);
    }

    private static List<CSVResult> parseCSV(String csvString) {
        List<CSVResult> orList = new ArrayList<CSVResult>();
        if (csvString.startsWith("L")) {
            csvString = csvString.substring(csvString.indexOf('\n') + 1);
        }
        String[] orStrings = csvString.split("\n");
        for (String orString : orStrings) {
            String[] attrStrings = orString.split(",");
            int i = 0;
            double level = Double.parseDouble(attrStrings[i++]);
            double x = Double.parseDouble(attrStrings[i++]);
            double y = Double.parseDouble(attrStrings[i++]);
            double download = Double.parseDouble(attrStrings[i++]);
            double upload = Double.parseDouble(attrStrings[i++]);
            double pathloss = Double.parseDouble(attrStrings[i++]);
            double powerRX = Double.parseDouble(attrStrings[i++]);
            double powerTX = Double.parseDouble(attrStrings[i++]);
            double absorption = Double.parseDouble(attrStrings[i++]);
            double eField = Double.parseDouble(attrStrings[i++]);
            double pdLos = 0;
            double pdDif = 0;
            if (attrStrings.length != 12) {
                pdLos = Double.parseDouble(attrStrings[i++]);
                pdDif = Double.parseDouble(attrStrings[i++]);
            }
            double roomNumber = Double.parseDouble(attrStrings[i++]);
            double drawingSize = Double.parseDouble(attrStrings[i]);
            CSVResult or = new CSVResult(new Point((int) x, (int) y),
                    (int) level, (int) download, (int) upload, pathloss, powerRX,
                    powerTX, absorption, eField, pdLos, pdDif, (int) roomNumber,
                    (int) drawingSize);
            orList.add(or);
        }
        return orList;
    }

    // 1633.0;263.0;250;\n\tWiFi;14.0;2;DLink;2400;2462
    private static List<AccessPoint> parseAccessPoints(String apString) {
        List<AccessPoint> apList = new ArrayList<AccessPoint>();
        String[] apStrings = apString.split("\n\t?");
        for (int i = 0; i + 1 < apStrings.length; i += 2) {
            String[] attrs = apStrings[i].split(";");
            String[] radioAttrs = apStrings[i + 1].split(";");
            double x = Double.parseDouble(attrs[0]);
            double y = Double.parseDouble(attrs[1]);
            int height = Integer.parseInt(attrs[2]);
            RadioType type = RadioType.getRadioTypeByText(radioAttrs[0]);
            double gain = Double.parseDouble(radioAttrs[1]);
            double power = Double.parseDouble(radioAttrs[2]);
            RadioModel model = RadioModel.getRadioModelByText(radioAttrs[3]);
            FrequencyBand frequency = FrequencyBand
                    .getFrequencyBandByText(radioAttrs[4]);
            Frequency frequencyBand = Frequency.getFreqByNumber(Integer
                    .parseInt(radioAttrs[5]));
            AccessPoint ap = new AccessPoint(new Point((int) x, (int) y), "",
                    height, type, model, frequency, frequencyBand, (int) gain,
                    (int) power, null);
            apList.add(ap);
        }
        return apList;
    }
}
