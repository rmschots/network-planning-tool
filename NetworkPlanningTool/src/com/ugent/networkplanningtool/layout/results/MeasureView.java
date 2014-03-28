package com.ugent.networkplanningtool.layout.results;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.ugent.networkplanningtool.R;
import com.ugent.networkplanningtool.data.ApMeasurement;
import com.ugent.networkplanningtool.model.DrawingModel;

/**
 * Created by Roel on 24/03/14.
 */
public class MeasureView extends LinearLayout {

    private DrawingModel drawingModel;

    private Button drawButton;
    private Button selectButton;
    private EditText sampleAmountEditText;
    private Button measureButton;

    public MeasureView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.results_view_measure, this, true);

        drawButton = (Button) findViewById(R.id.measureDrawButton);
        selectButton = (Button) findViewById(R.id.measureSelectButton);
        sampleAmountEditText = (EditText) findViewById(R.id.measureAmountEditText);
        measureButton = (Button) findViewById(R.id.measureMeasureButton);

        drawButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                drawingModel.setPlaceMode(new ApMeasurement());
            }
        });
        selectButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                drawingModel.setMeasureRemoveMode();
            }
        });


    }

    public void setDrawingModel(DrawingModel drawingModel) {
        this.drawingModel = drawingModel;
    }
}
