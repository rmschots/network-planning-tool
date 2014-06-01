package com.ugent.networkplanningtool.io.xml;

import com.ugent.networkplanningtool.data.ApMeasurement;
import com.ugent.networkplanningtool.io.AbstractASyncTask;

import java.io.File;
import java.util.List;

/**
 * Task that is loading measurements from a file
 */
public class LoadMeasurementsTask extends AbstractASyncTask<File, List<ApMeasurement>> {
    /**
     * Performs the measurement loading task
     * @param file the file to load the measurements from
     * @return the loaded measurements
     * @throws Exception any exception preventing the task from being executed
     */
    @Override
    protected List<ApMeasurement> performTaskInBackground(File file) throws Exception {
        return XMLIO.loadMeasurements(file);
    }
}
