package com.ugent.networkplanningtool.io.xml;

import com.ugent.networkplanningtool.data.FloorPlan;
import com.ugent.networkplanningtool.io.AbstractASyncTask;

import java.io.File;

public class LoadFloorPlanTask extends AbstractASyncTask<File, FloorPlan> {

    @Override
    protected FloorPlan performTaskInBackground(File file) throws Exception {
        return XMLIO.loadFloorPlan(file);
    }
}
