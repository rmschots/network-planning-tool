package com.ugent.networkplanningtool.layout.results;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import com.ugent.networkplanningtool.R;
import com.ugent.networkplanningtool.model.DrawingModel;

/**
 * View used to set the visual options for the results
 */
public class VisualOptionsView extends LinearLayout {

    private CheckBox showAccessPointsCheckBox;
    private CheckBox showActivitiesCheckBox;
    private CheckBox showLabelsCheckBox;
    private CheckBox showGridPointsCheckBox;

    private DrawingModel drawingModel;

    /**
     * Default constructor
     * @param context the context of the parent
     * @param attrs the attribute set
     */
    public VisualOptionsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.results_view_visualoptions, this, true);

        showAccessPointsCheckBox = (CheckBox) findViewById(R.id.showAccesspointsCheckBox);
        showActivitiesCheckBox = (CheckBox) findViewById(R.id.showActivitiesCheckBox);
        showLabelsCheckBox = (CheckBox) findViewById(R.id.showLabelsCheckBox);
        showGridPointsCheckBox = (CheckBox) findViewById(R.id.showGridpointsCheckBox);
        showAccessPointsCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                drawingModel.setDrawAccessPoints(showAccessPointsCheckBox.isChecked());
            }
        });
        showActivitiesCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                drawingModel.setDrawActivities(showActivitiesCheckBox.isChecked());
            }
        });
        showLabelsCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                drawingModel.setDrawLabels(showLabelsCheckBox.isChecked());
            }
        });
        showGridPointsCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                drawingModel.setDrawGridPoints(showGridPointsCheckBox.isChecked());
            }
        });
    }

    /**
     * Sets the drawing model
     * @param drawingModel the drawing model
     */
    public void setDrawingModel(DrawingModel drawingModel) {
        this.drawingModel = drawingModel;
    }
}