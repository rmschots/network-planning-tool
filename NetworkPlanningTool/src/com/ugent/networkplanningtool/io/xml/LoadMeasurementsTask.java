package com.ugent.networkplanningtool.io.xml;

import com.ugent.networkplanningtool.data.ApMeasurement;
import com.ugent.networkplanningtool.io.AbstractASyncTask;

import java.io.File;
import java.util.List;

/**
 * Created by Roel on 16/04/2014.
 */
public class LoadMeasurementsTask extends AbstractASyncTask<File, List<ApMeasurement>> {
    @Override
    protected List<ApMeasurement> performTaskInBackground(File file) throws Exception {
        return XMLIO.loadMeasurements(file);
    }
}
