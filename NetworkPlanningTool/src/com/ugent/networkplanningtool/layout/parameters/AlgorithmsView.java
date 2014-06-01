package com.ugent.networkplanningtool.layout.parameters;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.ugent.networkplanningtool.R;
import com.ugent.networkplanningtool.data.enums.parameters.PathLossModel;

/**
 * View for setting the model to use
 */
public class AlgorithmsView extends LinearLayout {

    private CheckBox frequenciesCheckBox;
    private Spinner pathLossModelSpinner;

    /**
     * Default constructor
     * @param context the context of the parent
     * @param attrs the attribute set
     */
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
    }

    /**
     * Returns whether to request the frequencies
     * @return whether to request the frequencies
     */
    public boolean isGetFrequencies() {
        return frequenciesCheckBox.isChecked();
    }

    /**
     * Gets the path loss model to use
     * @return the path loss model to use
     */
    public PathLossModel getPathLossModel() {
        String itemAsString = pathLossModelSpinner.getSelectedItem().toString();
        PathLossModel type = PathLossModel.getPathLossModelByName(itemAsString);
        Log.d("DEBUG", "PathLossModel: " + type.getValue());
        return type;
    }

}
