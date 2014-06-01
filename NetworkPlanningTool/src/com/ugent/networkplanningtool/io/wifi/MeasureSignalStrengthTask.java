package com.ugent.networkplanningtool.io.wifi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;

import com.ugent.networkplanningtool.data.AccessPoint;
import com.ugent.networkplanningtool.data.RealAccessPoint;
import com.ugent.networkplanningtool.io.ASyncTaskException;
import com.ugent.networkplanningtool.io.AbstractASyncTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Task measuring the perceived signal strength at the current location with the device
 */
public class MeasureSignalStrengthTask extends AbstractASyncTask<MeasureParams, Integer> {

    private Context context;
    private int samplesLeft;

    private final Map<String, List<Integer>> sampleMap = new HashMap<String, List<Integer>>();

    /**
     * Default constructor to set the application context, used to detect the signals
     * @param context the application context
     */
    public MeasureSignalStrengthTask(Context context) {
        this.context = context;
    }

    /**
     * Takes the given amount of signal strength samples
     * @param parameter the access points to sample signal strengths from and the amount of samples
     * @return the average perceived signal strength
     * @throws Exception any exception preventing the task from being executed
     */
    @Override
    protected Integer performTaskInBackground(final MeasureParams parameter) throws Exception {
        final WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if (parameter.getAccessPointList().isEmpty()) {
            throw new ASyncTaskException("No accessPoints are drawn");
        }
        Map<String, AccessPoint> linkMap = new HashMap<String, AccessPoint>();
        for (AccessPoint ap : parameter.getAccessPointList()) {
            if (!ap.getRap().equals(RealAccessPoint.getEmptyDummy())) {
                linkMap.put(ap.getRap().getBSSID(), ap);
            }
        }
        if (linkMap.isEmpty()) {
            throw new ASyncTaskException("Please link the accesspoints to real accesspoints first");
        }

        samplesLeft = parameter.getSampleCount();

        context.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                final List<ScanResult> results = wifi.getScanResults();//list of access points from the last scan
                for (final ScanResult result : results) {
                    List<Integer> ssList = sampleMap.get(result.BSSID);
                    if (ssList == null) {
                        ssList = new ArrayList<Integer>();
                        sampleMap.put(result.BSSID, ssList);
                    }
                    ssList.add(result.level);
                }
                System.out.println("left: " + (samplesLeft - 1));
                if (--samplesLeft <= 0) {
                    synchronized (sampleMap) {
                        sampleMap.notify();
                    }
                    context.unregisterReceiver(this);
                } else {
                    wifi.startScan();
                }
            }
        }, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        wifi.startScan();
        synchronized (sampleMap) {
            sampleMap.wait();
        }
        AccessPoint maxSSAp = null;
        Integer maxSS = null;
        for (String bssid : sampleMap.keySet()) {
            AccessPoint ap = linkMap.get(bssid);
            if (ap != null) {
                List<Integer> sampleList = sampleMap.get(bssid);
                int calc = 0;
                for (Integer i : sampleList) {
                    calc += i;
                }
                calc /= sampleList.size();
                if (maxSS == null || maxSS < calc) {
                    maxSS = calc;
                    maxSSAp = ap;
                }
            }
        }
        if (maxSS == null) {
            throw new ASyncTaskException("No linked device detected.");
        }
        sampleMap.clear();
        System.out.println("max: " + maxSSAp + " - " + maxSS);
        return maxSS;
    }
}
