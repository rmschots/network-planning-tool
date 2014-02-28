package com.ugent.networkplanningtool.layout.tools;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.ugent.networkplanningtool.R;
import com.ugent.networkplanningtool.data.enums.ActivityType;
import com.ugent.networkplanningtool.layout.components.MySeekBar;
import com.ugent.networkplanningtool.model.DrawingModel;

/**
 * Created by Roel on 28/02/14.
 */
public class EstimateSARView extends LinearLayout{

    private EditText personAgeEditText;
    private EditText personHeightEditText;
    private EditText personWeightEditText;
    private MySeekBar roomHeightBar;

    private DrawingModel drawingModel;

    public EstimateSARView(Context context, DrawingModel drawingModel) {
        super(context);
        this.drawingModel = drawingModel;
        init();
    }

    public EstimateSARView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.tools_view_sar, this, true);

        personAgeEditText = (EditText) findViewById(R.id.personAgeEditText);
        personHeightEditText = (EditText) findViewById(R.id.personHeightEditText);
        personWeightEditText = (EditText) findViewById(R.id.personWeightEditText);
        roomHeightBar = (MySeekBar) findViewById(R.id.roomHeightView);
    }

    public int getPersonAge(){
        return Integer.parseInt(personAgeEditText.getText().toString());
    }

    public int getPersonHeight(){
        return Integer.parseInt(personHeightEditText.getText().toString());
    }

    public int getPersonWeight() {
        return Integer.parseInt(personWeightEditText.getText().toString());
    }

    public double getRoomHeight() {
        return roomHeightBar.getValue();
    }
}
