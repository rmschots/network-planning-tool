package com.ugent.networkplanningtool.layout.parameters;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.ugent.networkplanningtool.R;
import com.ugent.networkplanningtool.data.enums.FrequencyBand;
import com.ugent.networkplanningtool.data.enums.RadioType;
import com.ugent.networkplanningtool.layout.components.MySeekBar;
import com.ugent.networkplanningtool.model.DrawingModel;

/**
 * Created by Roel on 25/02/14.
 */
public class GeneratedAPsView extends LinearLayout {

    private ArrayAdapter<RadioType> apTypeAdapter;
    private ArrayAdapter<FrequencyBand> freqBandAdapter;

    private Spinner generatedAPsSpinner;
    private Spinner frequencySpinner;

    private EditText transmitPowerEditText;
    private MySeekBar antennaGainBar;
    private MySeekBar elevationBar;

    private DrawingModel drawingModel;

    public GeneratedAPsView(Context context, DrawingModel drawingModel) {
        super(context);
        this.drawingModel = drawingModel;
        init();
    }

    public GeneratedAPsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.parameters_view_generatedaps, this, true);

        generatedAPsSpinner = (Spinner) findViewById(R.id.generatedAPsSpinner);
        frequencySpinner = (Spinner) findViewById(R.id.generatedAPsFreqSpinner);

        transmitPowerEditText = (EditText) findViewById(R.id.gapsTransmitPowerEditText);
        antennaGainBar = (MySeekBar) findViewById(R.id.antennaGainView);
        elevationBar = (MySeekBar) findViewById(R.id.elevationView);

        apTypeAdapter = new ArrayAdapter<RadioType>(getContext(), android.R.layout.simple_spinner_dropdown_item, new RadioType[]{RadioType.WIFI, RadioType.SENSOR, RadioType.LTE_FEMTOCELL});
        freqBandAdapter = new ArrayAdapter<FrequencyBand>(getContext(), android.R.layout.simple_spinner_dropdown_item, apTypeAdapter.getItem(0).getFrequencyBands());

        generatedAPsSpinner.setAdapter(apTypeAdapter);
        frequencySpinner.setAdapter(freqBandAdapter);

        generatedAPsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                freqBandAdapter = new ArrayAdapter<FrequencyBand>(getContext(), android.R.layout.simple_spinner_dropdown_item, apTypeAdapter.getItem(i).getFrequencyBands());
                frequencySpinner.setAdapter(freqBandAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    public RadioType getGeneratedAPType() {
        return apTypeAdapter.getItem(generatedAPsSpinner.getSelectedItemPosition());
    }

    public FrequencyBand getGeneratedAPFrequencyBand() {
        return freqBandAdapter.getItem(frequencySpinner.getSelectedItemPosition());
    }

    public double getTransmitPower() {
        return Double.parseDouble(transmitPowerEditText.getText().toString());
    }

    public double getAntennaGain() {
        return antennaGainBar.getValue();
    }

    public double getElevation() {
        return elevationBar.getValue();
    }
}
