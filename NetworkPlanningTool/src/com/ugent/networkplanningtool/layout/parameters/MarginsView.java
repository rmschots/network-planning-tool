package com.ugent.networkplanningtool.layout.parameters;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.ugent.networkplanningtool.R;
import com.ugent.networkplanningtool.layout.components.MySeekBar;
import com.ugent.networkplanningtool.model.DrawingModel;

/**
 * Created by Roel on 25/02/14.
 */
public class MarginsView extends LinearLayout {

    private MySeekBar interferenceMarginBar;
    private MySeekBar shadowingMarginBar;
    private MySeekBar fadeMarginBar;

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

        interferenceMarginBar = (MySeekBar) findViewById(R.id.interferenceMarginView);
        shadowingMarginBar = (MySeekBar) findViewById(R.id.shadowingMarginView);
        fadeMarginBar = (MySeekBar) findViewById(R.id.fadeMarginView);
    }

    public double getInterferenceMargin() {
        return interferenceMarginBar.getValue();
    }

    public double getShadowMargin() {
        return shadowingMarginBar.getValue();
    }

    public double getFadeMargin() {
        return fadeMarginBar.getValue();
    }
}
