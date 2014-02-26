package com.ugent.networkplanningtool.layout.parameters;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.ugent.networkplanningtool.R;
import com.ugent.networkplanningtool.data.enums.parameters.PathLossModel;
import com.ugent.networkplanningtool.model.DrawingModel;

/**
 * Created by Roel on 25/02/14.
 */
public class AlgorithmsView extends LinearLayout {

    private CheckBox frequenciesCheckBox;
    private Spinner pathLossModelSpinner;
    private EditText refDistanceEditText;
    private EditText refPathLossEditText;
    private EditText pathLossExponentEditText;

    private DrawingModel drawingModel;

    public AlgorithmsView(Context context, DrawingModel drawingModel) {
        super(context);
        this.drawingModel = drawingModel;
        init();
    }

    public AlgorithmsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.parameters_view_algorithms, this, true);

        frequenciesCheckBox = (CheckBox) findViewById(R.id.frequenciesCheckBox);
        pathLossModelSpinner = (Spinner) findViewById(R.id.pathlossModelSpinner);
        refDistanceEditText = (EditText) findViewById(R.id.refDistanceEditText);
        refPathLossEditText = (EditText) findViewById(R.id.refPathLossEditText);
        pathLossExponentEditText = (EditText) findViewById(R.id.pathLossExponentEditText);
    }

    public boolean isGetFrequencies() {
        return frequenciesCheckBox.isChecked();
    }

    public PathLossModel getPathLossModel() {
        String itemAsString = pathLossModelSpinner.getSelectedItem().toString();
        PathLossModel type = PathLossModel.getPathLossModelByName(itemAsString);
        Log.d("DEBUG", "PathLossModel: " + type.getValue());
        return type;
    }

    public double getRefDistance() {
        try {
            return Double.parseDouble(refDistanceEditText.getText().toString());
        } catch (NumberFormatException ex) {
            return 1;
        }
    }

    public double getRefPathLoss() {
        try {
            return Double.parseDouble(refPathLossEditText.getText().toString());
        } catch (NumberFormatException ex) {
            return 40;
        }
    }

    public double getPathLossExponent() {
        try {
            return Double.parseDouble(pathLossExponentEditText.getText().toString());
        } catch (NumberFormatException ex) {
            return 2;
        }
    }

}
