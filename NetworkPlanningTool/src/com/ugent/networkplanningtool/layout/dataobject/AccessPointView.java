package com.ugent.networkplanningtool.layout.dataobject;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.ugent.networkplanningtool.R;
import com.ugent.networkplanningtool.data.AccessPoint;
import com.ugent.networkplanningtool.data.DataObject.DataObjectType;
import com.ugent.networkplanningtool.data.enums.Frequency;
import com.ugent.networkplanningtool.data.enums.FrequencyBand;
import com.ugent.networkplanningtool.data.enums.Network;
import com.ugent.networkplanningtool.data.enums.RadioModel;
import com.ugent.networkplanningtool.data.enums.RadioType;
import com.ugent.networkplanningtool.model.DrawingModel;

public class AccessPointView extends DataObjectView {

    private EditText nameEditText;
    private Spinner networkSignalTypeSpinner;
    private Spinner networkMHzSpinner;
    private Spinner networkChannelSpinner;
    private Spinner networkModelSpinner;
    private Spinner networkIDSpinner;

    private EditText antennaGainEditText;
    private EditText transmitPowerEditText;
    private EditText elevationEditText;

    private ArrayAdapter<RadioType> typeAdapter;
    private ArrayAdapter<Frequency> freqAdapter;
    private ArrayAdapter<FrequencyBand> freqBandAdapter;
    private ArrayAdapter<RadioModel> modelAdapter;
    private ArrayAdapter<Network> networkAdapter;

    private DrawingModel drawingModel;

    public AccessPointView(Context context, ViewType type, DrawingModel drawingModel) {
        super(context, type);
        this.drawingModel = drawingModel;
        init();
        loadData();
        initComponents();
    }

    public AccessPointView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        initComponents();
    }

    private void init() {
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.design_view_accesspoints, this, true);

        nameEditText = (EditText) findViewById(R.id.accessPointNameEditText);

        networkSignalTypeSpinner = (Spinner) findViewById(R.id.networkSignalTypeSpinner);
        networkMHzSpinner = (Spinner) findViewById(R.id.networkMHzSpinner);
        networkChannelSpinner = (Spinner) findViewById(R.id.networkChannelSpinner);
        networkModelSpinner = (Spinner) findViewById(R.id.networkModelSpinner);
        networkIDSpinner = (Spinner) findViewById(R.id.networkIDSpinner);

        antennaGainEditText = (EditText) findViewById(R.id.antennaGainEditText);
        transmitPowerEditText = (EditText) findViewById(R.id.transmitPowerEditText);
        elevationEditText = (EditText) findViewById(R.id.elevationEditText);

        typeAdapter = new ArrayAdapter<RadioType>(getContext(),android.R.layout.simple_spinner_dropdown_item,RadioType.values());
        networkSignalTypeSpinner.setAdapter(typeAdapter);



        freqBandAdapter = new ArrayAdapter<FrequencyBand>(getContext(),android.R.layout.simple_spinner_dropdown_item,FrequencyBand.values());
        networkMHzSpinner.setAdapter(freqBandAdapter);
        networkMHzSpinner.setSelection(freqBandAdapter.getPosition(FrequencyBand.FREQBAND_2400));
        networkMHzSpinner.setEnabled(false);

        freqAdapter = new ArrayAdapter<Frequency>(getContext(),android.R.layout.simple_spinner_dropdown_item,Frequency.values());
        networkChannelSpinner.setAdapter(freqAdapter);

        modelAdapter = new ArrayAdapter<RadioModel>(getContext(),android.R.layout.simple_spinner_dropdown_item,RadioModel.values());
        networkModelSpinner.setAdapter(modelAdapter);

        networkAdapter = new ArrayAdapter<Network>(getContext(),android.R.layout.simple_spinner_dropdown_item,Network.values());
        networkIDSpinner.setAdapter(networkAdapter);
    }

    private void initComponents() {
        if (networkSignalTypeSpinner != null) {
            if (type.equals(ViewType.INFO)) {
                nameEditText.setEnabled(false);
                networkSignalTypeSpinner.setEnabled(false);
                networkMHzSpinner.setEnabled(false);
                networkChannelSpinner.setEnabled(false);
                networkModelSpinner.setEnabled(false);
                networkIDSpinner.setEnabled(false);
                antennaGainEditText.setEnabled(false);
                transmitPowerEditText.setEnabled(false);
                elevationEditText.setEnabled(false);
            } else {
                nameEditText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        System.out.println("UPDATE 1");
                        updateDrawingModel();
                    }
                });
                OnItemSelectedListener listener = new OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int arg2, long arg3) {
                        System.out.println("UPDATE 2");
                        updateDrawingModel();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                    }
                };
                networkSignalTypeSpinner.setOnItemSelectedListener(listener);
                networkMHzSpinner.setOnItemSelectedListener(listener);
                networkChannelSpinner.setOnItemSelectedListener(listener);
                networkModelSpinner.setOnItemSelectedListener(listener);
                networkIDSpinner.setOnItemSelectedListener(listener);
            }
        }
    }

    public RadioType getSelectedSignalType() {
        return typeAdapter.getItem(networkSignalTypeSpinner.getSelectedItemPosition());
    }

    public FrequencyBand getSelectedFrequencyBand() {
        return freqBandAdapter.getItem(networkMHzSpinner.getSelectedItemPosition());
    }

    public Frequency getSelectedFrequency() {
        return freqAdapter.getItem(networkChannelSpinner.getSelectedItemPosition());
    }

    public RadioModel getSelectedModel() {
        return modelAdapter.getItem(networkModelSpinner.getSelectedItemPosition());
    }

    public Network getSelectedNetwork() {
        return networkAdapter.getItem(networkIDSpinner.getSelectedItemPosition());
    }

    public void setDrawingModel(DrawingModel drawingModel) {
        this.drawingModel = drawingModel;
    }

    public void updateDrawingModel() {
        Log.d("DEBUG", "updateDrawingModel " + drawingModel.getTouchDataObject());
        String name = nameEditText.getText().toString();
        RadioType signalType = getSelectedSignalType();
        Frequency freq = getSelectedFrequency();
        System.out.println(freq);
        FrequencyBand freqBand = getSelectedFrequencyBand();
        RadioModel model = getSelectedModel();
        Network network = getSelectedNetwork();
        int transmitPower = Integer.parseInt(transmitPowerEditText.getText().toString());
        int antennaGain = Integer.parseInt(antennaGainEditText.getText().toString());
        int elevation = Integer.parseInt(elevationEditText.getText().toString());

        if (drawingModel.getTouchDataObject() != null
                && drawingModel.getTouchDataObject().DATA_OBJECT_TYPE.equals(DataObjectType.ACCESS_POINT)) {
            AccessPoint ap = (AccessPoint) drawingModel.getTouchDataObject();
            ap.setName(name);
            ap.setType(signalType);
            ap.setFrequency(freq);
            ap.setFrequencyband(freqBand);
            ap.setModel(model);
            ap.setNetwork(network);
            ap.setPower(transmitPower);
            ap.setGain(antennaGain);
            ap.setHeight(elevation);
        } else {
            drawingModel.setPlaceMode(new AccessPoint(name, elevation, signalType, model, freqBand, freq, antennaGain, transmitPower, network));
        }
        Log.d("DEBUG", "updateDrawingModel " + drawingModel.getTouchDataObject());
    }

    private void loadData() {
        AccessPoint ap = (AccessPoint) drawingModel.getSelected();
        nameEditText.setText(ap.getName());
        networkSignalTypeSpinner.setSelection(typeAdapter.getPosition(ap.getType()));
        networkChannelSpinner.setSelection(freqAdapter.getPosition(ap.getFrequency()));
        networkMHzSpinner.setSelection(freqBandAdapter.getPosition(ap.getFrequencyband()));
        networkModelSpinner.setSelection(modelAdapter.getPosition(ap.getModel()));
        networkIDSpinner.setSelection(networkAdapter.getPosition(ap.getNetwork()));
    }


}
