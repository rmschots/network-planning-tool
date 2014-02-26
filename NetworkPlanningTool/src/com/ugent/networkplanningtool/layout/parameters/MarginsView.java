package com.ugent.networkplanningtool.layout.parameters;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import com.ugent.networkplanningtool.R;
import com.ugent.networkplanningtool.model.DrawingModel;

/**
 * Created by Roel on 25/02/14.
 */
public class MarginsView extends LinearLayout {

    private SeekBar interferenceMarginBar;
    private SeekBar shadowingMarginBar;
    private SeekBar fadeMarginBar;

    private DrawingModel drawingModel;

    public MarginsView(Context context, DrawingModel drawingModel) {
        super(context);
        this.drawingModel = drawingModel;
        init();
    }

    public MarginsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.parameters_view_margins, this, true);

        interferenceMarginBar = (SeekBar) findViewById(R.id.interferenceMarginBar);
        shadowingMarginBar = (SeekBar) findViewById(R.id.shadowingMarginBar);
        fadeMarginBar = (SeekBar) findViewById(R.id.fadeMarginBar);
    }

    public double getInterferenceMargin() {
        return interferenceMarginBar.getProgress();
    }

    public double getShadowMargin() {
        return shadowingMarginBar.getProgress();
    }

    public double getFadeMargin() {
        return fadeMarginBar.getProgress();
    }
}
