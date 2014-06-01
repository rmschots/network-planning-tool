package com.ugent.networkplanningtool.layout.tools;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.ugent.networkplanningtool.R;
import com.ugent.networkplanningtool.data.enums.ActivityType;
import com.ugent.networkplanningtool.layout.components.MySeekBar;

/**
 * View containing the settings for reducing the exposure
 */
public class ExposureReductionView extends LinearLayout {
    private ArrayAdapter<ActivityType> activityAdapter;

    private MySeekBar apDistanceSeekBar;
    private EditText maxEFieldEditText;
    private Spinner placeAccesspointsSpinner;
    private Spinner defaultActivitySpinner;

    /**
     * Default constructor
     * @param context the context of the parent
     * @param attrs the attribute set
     */
    public ExposureReductionView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.tools_view_exposure, this, true);

        apDistanceSeekBar = (MySeekBar) findViewById(R.id.distanceToAPView);
        placeAccesspointsSpinner = (Spinner) findViewById(R.id.placeAccesspointsSpinner);
        defaultActivitySpinner = (Spinner) findViewById(R.id.defaultActivitySpinner);
        maxEFieldEditText = (EditText) findViewById(R.id.maxElectricFieldEditText);

        activityAdapter = new ArrayAdapter<ActivityType>(getContext(), android.R.layout.simple_spinner_dropdown_item, ActivityType.values());
        defaultActivitySpinner.setAdapter(activityAdapter);
    }

    /**
     * Returns the maximum distance to the access point
     * @return the maximum distance to the access point
     */
    public double getdistanceToAP() {
        return apDistanceSeekBar.getValue();
    }

    /**
     * Returns he maximum electric field value
     * @return he maximum electric field value
     */
    public double getMaxEField() {
        return Double.parseDouble(maxEFieldEditText.getText().toString());
    }

    /**
     * Returns the set default activity
     * @return the set default activity
     */
    public ActivityType getDefaultActivity() {
        return activityAdapter.getItem(defaultActivitySpinner.getSelectedItemPosition());
    }

    /**
     * Returns the set access point placing mode
     * @return the set access point placing mode
     */
    public String getPlaceAccessPoints() {
        return placeAccesspointsSpinner.getSelectedItem().toString();
    }
}
