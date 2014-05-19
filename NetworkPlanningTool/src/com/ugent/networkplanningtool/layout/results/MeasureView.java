package com.ugent.networkplanningtool.layout.results;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.ugent.networkplanningtool.R;
import com.ugent.networkplanningtool.data.ApMeasurement;
import com.ugent.networkplanningtool.data.FloorPlanObject;
import com.ugent.networkplanningtool.model.DrawingModel;

/**
 * Created by Roel on 24/03/14.
 */
public class MeasureView extends LinearLayout {

    private DrawingModel drawingModel;

    private Button drawButton;
    private Button selectButton;
    private EditText sampleAmountEditText;

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

        drawButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                drawingModel.setPlaceMode(new ApMeasurement(Integer.parseInt(sampleAmountEditText.getText().toString())));
                drawButton.setEnabled(false);
                selectButton.setEnabled(true);
            }
        });
        selectButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                drawingModel.setMeasureRemoveMode();
                selectButton.setEnabled(false);
                drawButton.setEnabled(true);
            }
        });
        sampleAmountEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (drawingModel.getTouchFloorPlanObject() != null
                        && drawingModel.getTouchFloorPlanObject().DATA_OBJECT_TYPE.equals(FloorPlanObject.DataObjectType.AP_MEASUREMENT)) {
                    ApMeasurement apm = (ApMeasurement) drawingModel.getTouchFloorPlanObject();
                    int samplePoolSize;
                    try {
                        samplePoolSize = Integer.parseInt(editable.toString());
                        if (samplePoolSize <= 0 || samplePoolSize > 10) {
                            samplePoolSize = ApMeasurement.DEFAULT_SAMPLE_POOL_SIZE;
                        }
                    } catch (NumberFormatException ex) {
                        samplePoolSize = ApMeasurement.DEFAULT_SAMPLE_POOL_SIZE;
                    }
                    apm.setSamplePoolSize(samplePoolSize);
                }
            }
        });

        drawButton.setEnabled(false);
    }

    public void setDrawingModel(DrawingModel drawingModel) {
        this.drawingModel = drawingModel;
    }

    public void updateDrawingModel() {
        drawingModel.setPlaceMode(new ApMeasurement(Integer.parseInt(sampleAmountEditText.getText().toString())));
    }
}
