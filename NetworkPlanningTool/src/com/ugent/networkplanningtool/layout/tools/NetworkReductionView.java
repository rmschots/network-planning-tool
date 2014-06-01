package com.ugent.networkplanningtool.layout.tools;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.ugent.networkplanningtool.R;
import com.ugent.networkplanningtool.data.enums.ActivityType;

/**
 * View containing the settings for reducing the network
 */
public class NetworkReductionView extends LinearLayout {
    private ArrayAdapter<ActivityType> activityAdapter;

    private Spinner defaultActivitySpinner;

    /**
     * Default constructor
     * @param context the context of the parent
     * @param attrs the attribute set
     */
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

    /**
     * Returns the set default activity
     * @return the set default activity
     */
    public ActivityType getDefaultActivity() {
        return activityAdapter.getItem(defaultActivitySpinner.getSelectedItemPosition());
    }
}
