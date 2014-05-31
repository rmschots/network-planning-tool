package com.ugent.networkplanningtool.layout.tools;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.ugent.networkplanningtool.R;
import com.ugent.networkplanningtool.data.enums.ActivityType;
import com.ugent.networkplanningtool.model.DrawingModel;

public class NetworkReductionView extends LinearLayout {
    private ArrayAdapter<ActivityType> activityAdapter;

    private Spinner defaultActivitySpinner;

    private DrawingModel drawingModel;

    public NetworkReductionView(Context context, DrawingModel drawingModel) {
        super(context);
        this.drawingModel = drawingModel;
        init();
    }

    public NetworkReductionView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.tools_view_network, this, true);

        defaultActivitySpinner = (Spinner) findViewById(R.id.defaultActivitySpinner);

        activityAdapter = new ArrayAdapter<ActivityType>(getContext(), android.R.layout.simple_spinner_dropdown_item, ActivityType.values());
        defaultActivitySpinner.setAdapter(activityAdapter);
    }

    public ActivityType getDefaultActivity() {
        return activityAdapter.getItem(defaultActivitySpinner.getSelectedItemPosition());
    }
}
