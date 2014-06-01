package com.ugent.networkplanningtool.layout.tools;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.ugent.networkplanningtool.R;
import com.ugent.networkplanningtool.layout.components.MySeekBar;

/**
 * View containing the settings for estimating SAR.
 */
public class EstimateSARView extends LinearLayout {

    private EditText personAgeEditText;
    private EditText personHeightEditText;
    private EditText personWeightEditText;
    private MySeekBar roomHeightBar;

    /**
     * Default constructor
     * @param context the context of the parent
     * @param attrs the attribute set
     */
    public EstimateSARView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    /**
     * Initializes the GUI
     */
    private void init() {
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.tools_view_sar, this, true);

        personAgeEditText = (EditText) findViewById(R.id.personAgeEditText);
        personHeightEditText = (EditText) findViewById(R.id.personHeightEditText);
        personWeightEditText = (EditText) findViewById(R.id.personWeightEditText);
        roomHeightBar = (MySeekBar) findViewById(R.id.roomHeightView);
    }

    /**
     * Returns the set person age
     * @return the set person age
     */
    public int getPersonAge() {
        return Integer.parseInt(personAgeEditText.getText().toString());
    }

    /**
     * Returns the set person height
     * @return the set person height
     */
    public int getPersonHeight() {
        return Integer.parseInt(personHeightEditText.getText().toString());
    }

    /**
     * Returns the set person weight
     * @return the set person weight
     */
    public int getPersonWeight() {
        return Integer.parseInt(personWeightEditText.getText().toString());
    }

    /**
     * Returns the set room height
     * @return the set room height
     */
    public double getRoomHeight() {
        return roomHeightBar.getValue();
    }
}
