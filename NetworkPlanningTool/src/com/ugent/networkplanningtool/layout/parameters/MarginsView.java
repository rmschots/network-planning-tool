package com.ugent.networkplanningtool.layout.parameters;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.ugent.networkplanningtool.R;
import com.ugent.networkplanningtool.layout.components.MySeekBar;

/**
 * View used to set different types of margins for the models
 */
public class MarginsView extends LinearLayout {

    private MySeekBar interferenceMarginBar;
    private MySeekBar shadowingMarginBar;
    private MySeekBar fadeMarginBar;

    /**
     * Default constructor
     * @param context the context of the parent
     * @param attrs the attribute set
     */
    public MarginsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.parameters_view_margins, this, true);

        interferenceMarginBar = (MySeekBar) findViewById(R.id.interferenceMarginView);
        shadowingMarginBar = (MySeekBar) findViewById(R.id.shadowingMarginView);
        fadeMarginBar = (MySeekBar) findViewById(R.id.fadeMarginView);
    }

    /**
     * Returns the set interference margin
     * @return the set interference margin
     */
    public double getInterferenceMargin() {
        return interferenceMarginBar.getValue();
    }

    /**
     * Returns the set shadowing margin
     * @return the set shadowing margin
     */
    public double getShadowMargin() {
        return shadowingMarginBar.getValue();
    }

    /**
     * Returns the set fade margin
     * @return the set fade margin
     */
    public double getFadeMargin() {
        return fadeMarginBar.getValue();
    }
}
