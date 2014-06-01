package com.ugent.networkplanningtool.io.xml;

import com.ugent.networkplanningtool.data.FloorPlan;
import com.ugent.networkplanningtool.io.AbstractASyncTask;

import java.io.File;

/**
 * Task to load a floor plan from a file
 */
public class LoadFloorPlanTask extends AbstractASyncTask<File, FloorPlan> {

    /**
     * Performs the floor plan loading task
     * @param file the file to load the floor plan from
     * @return the loaded floor plan
     * @throws Exception any exception preventing the task from being executed
     */
    @Override
    protected FloorPlan performTaskInBackground(File file) throws Exception {
        return XMLIO.loadFloorPlan(file);
    }
}
