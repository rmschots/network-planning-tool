package com.ugent.networkplanningtool.layout.results;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.ugent.networkplanningtool.R;
import com.ugent.networkplanningtool.layout.components.MySeekBar;
import com.ugent.networkplanningtool.model.DrawingModel;

/**
 * Created by Roel on 9/03/14.
 */
public class VisualOptionsView extends LinearLayout {

    private EditText personAgeEditText;
    private EditText personHeightEditText;
    private EditText personWeightEditText;
    private MySeekBar roomHeightBar;

    private DrawingModel drawingModel;

    public VisualOptionsView(Context context, DrawingModel drawingModel) {
        super(context);
        this.drawingModel = drawingModel;
        init();
    }

    public VisualOptionsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.results_view_visualoptions, this, true);

        personAgeEditText = (EditText) findViewById(R.id.personAgeEditText);
        personHeightEditText = (EditText) findViewById(R.id.personHeightEditText);
        personWeightEditText = (EditText) findViewById(R.id.personWeightEditText);
        roomHeightBar = (MySeekBar) findViewById(R.id.roomHeightView);
    }

    public int getPersonAge() {
        return Integer.parseInt(personAgeEditText.getText().toString());
    }

    public int getPersonHeight() {
        return Integer.parseInt(personHeightEditText.getText().toString());
    }

    public int getPersonWeight() {
        return Integer.parseInt(personWeightEditText.getText().toString());
    }

    public double getRoomHeight() {
        return roomHeightBar.getValue();
    }
}