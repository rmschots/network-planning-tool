package com.ugent.networkplanningtool.layout.dataobject;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Spinner;

import com.ugent.networkplanningtool.R;
import com.ugent.networkplanningtool.data.AccessPoint;
import com.ugent.networkplanningtool.data.Frequency;
import com.ugent.networkplanningtool.data.FrequencyBand;
import com.ugent.networkplanningtool.data.Network;
import com.ugent.networkplanningtool.data.RadioModel;
import com.ugent.networkplanningtool.data.RadioType;
import com.ugent.networkplanningtool.data.DataObject.DataObjectType;
import com.ugent.networkplanningtool.model.DrawingModel;

public class AccessPointView extends DataObjectView {

	private Spinner networkSignalTypeSpinner;
	private Spinner networkMHzSpinner;
	private Spinner networkChannelSpinner;
	private Spinner networkModelSpinner;
	private Spinner networkIDSpinner;
	
	private EditText antennaGainEditText;
	private EditText transmitPowerEditText;
	private EditText elevationEditText;
	
	private DrawingModel drawingModel;
	
	public AccessPointView(Context context, ViewType type, DrawingModel drawingModel){
		super(context, type);
		this.drawingModel = drawingModel;
		init();
		loadData();
	}

	public AccessPointView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	
	private void init(){
		LayoutInflater inflater = (LayoutInflater) getContext()
		        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.design_view_accesspoints, this, true);
		
		networkSignalTypeSpinner = (Spinner) findViewById(R.id.networkSignalTypeSpinner);
		networkMHzSpinner = (Spinner) findViewById(R.id.networkMHzSpinner);
		networkChannelSpinner = (Spinner) findViewById(R.id.networkChannelSpinner);
		networkModelSpinner = (Spinner) findViewById(R.id.networkModelSpinner);
		networkIDSpinner = (Spinner) findViewById(R.id.networkIDSpinner);
		
		antennaGainEditText = (EditText) findViewById(R.id.antennaGainEditText);
		transmitPowerEditText = (EditText) findViewById(R.id.transmitPowerEditText);
		elevationEditText = (EditText) findViewById(R.id.elevationEditText);
		
		initComponents();
	}
	
	private void initComponents(){
		if(networkSignalTypeSpinner != null){
			if(type.equals(ViewType.INFO)){
				networkSignalTypeSpinner.setEnabled(false);
				networkMHzSpinner.setEnabled(false);
				networkChannelSpinner.setEnabled(false);
				networkModelSpinner.setEnabled(false);
				networkIDSpinner.setEnabled(false);
				antennaGainEditText.setEnabled(false);
				transmitPowerEditText.setEnabled(false);
				elevationEditText.setEnabled(false);
			}else{
				OnItemSelectedListener listener = new OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						updateDrawingModel();
					}
					@Override
					public void onNothingSelected(AdapterView<?> arg0) {}
				};
				networkSignalTypeSpinner.setOnItemSelectedListener(listener);
				networkMHzSpinner.setOnItemSelectedListener(listener);
				networkChannelSpinner.setOnItemSelectedListener(listener);
				networkModelSpinner.setOnItemSelectedListener(listener);
				networkIDSpinner.setOnItemSelectedListener(listener);
			}
		}
	}
	
	public RadioType getSelectedSignalType(){
		String itemAsString = networkSignalTypeSpinner.getSelectedItem().toString();
		RadioType type = RadioType.getRadioTypeByText(itemAsString);
		Log.d("DEBUG","RadioType: "+type.getText());
		return type;
	}
	
	public Frequency getSelectedFrequency(){
		String itemAsString = networkMHzSpinner.getSelectedItem().toString();
		Frequency frequency = Frequency.getFrequencyByText(itemAsString);
		Log.d("DEBUG","Frequency: "+frequency.getText());
		return frequency;
	}
	
	public FrequencyBand getSelectedChannel(){
		String itemAsString = networkChannelSpinner.getSelectedItem().toString();
		FrequencyBand band = FrequencyBand.getFrequencyBandByText(itemAsString);
		Log.d("DEBUG","FrequencyBand: "+band.getText());
		return band;
	}
	
	public RadioModel getSelectedModel(){
		String itemAsString = networkModelSpinner.getSelectedItem().toString();
		RadioModel model = RadioModel.getRadioModelByText(itemAsString);
		Log.d("DEBUG","RadioModel: "+model.getText());
		return model;
	}
	
	public Network getSelectedNetwork(){
		String itemAsString = networkIDSpinner.getSelectedItem().toString();
		Network network = Network.getNetworkByText(itemAsString);
		Log.d("DEBUG","Network: "+network.getText());
		return network;
	}
	
	public void setDrawingModel(DrawingModel drawingModel){
		this.drawingModel = drawingModel;
	}
	
	public void updateDrawingModel(){
		Log.d("DEBUG","updateDrawingModel "+drawingModel.getTouchDataObject());
		RadioType signalType = getSelectedSignalType();
		Frequency freq = getSelectedFrequency();
		FrequencyBand freqBand = getSelectedChannel();
		RadioModel model = getSelectedModel();
		Network network = getSelectedNetwork();
		int transmitPower = Integer.parseInt(transmitPowerEditText.getText().toString());
		int antennaGain = Integer.parseInt(antennaGainEditText.getText().toString());
		int elevation = Integer.parseInt(elevationEditText.getText().toString());
		
		if(drawingModel.getTouchDataObject() != null
				&& drawingModel.getTouchDataObject().DATA_OBJECT_TYPE.equals(DataObjectType.ACCESS_POINT)){
			AccessPoint ap = (AccessPoint) drawingModel.getTouchDataObject();
			ap.setType(signalType);
			ap.setFrequency(freq);
			ap.setFrequencyband(freqBand);
			ap.setModel(model);
			ap.setNetwork(network);
			ap.setPower(transmitPower);
			ap.setGain(antennaGain);
			ap.setHeight(elevation);
		}else{
			drawingModel.setPlaceMode(new AccessPoint("", elevation, signalType, model, freq, freqBand, antennaGain, transmitPower, network));
		}
		Log.d("DEBUG","updateDrawingModel "+drawingModel.getTouchDataObject());
	}

	private void loadData() {
		AccessPoint ap = (AccessPoint)drawingModel.getSelected();
		for(int i = 0; i < networkSignalTypeSpinner.getChildCount(); i++){
			String itemAsString = networkSignalTypeSpinner.getChildAt(i).toString();
			if(itemAsString.equals(ap.getType().getText())){
				networkSignalTypeSpinner.setSelection(i);
				break;
			}
		}
		for(int i = 0; i < networkMHzSpinner.getChildCount(); i++){
			String itemAsString = networkMHzSpinner.getChildAt(i).toString();
			if(itemAsString.equals(ap.getFrequency().getText())){
				networkMHzSpinner.setSelection(i);
				break;
			}
		}
		for(int i = 0; i < networkChannelSpinner.getChildCount(); i++){
			String itemAsString = networkChannelSpinner.getChildAt(i).toString();
			if(itemAsString.equals(ap.getFrequencyband().getText())){
				networkChannelSpinner.setSelection(i);
				break;
			}
		}
		for(int i = 0; i < networkModelSpinner.getChildCount(); i++){
			String itemAsString = networkModelSpinner.getChildAt(i).toString();
			if(itemAsString.equals(ap.getModel().getText())){
				networkModelSpinner.setSelection(i);
				break;
			}
		}
		for(int i = 0; i < networkIDSpinner.getChildCount(); i++){
			String itemAsString = networkIDSpinner.getChildAt(i).toString();
			if(itemAsString.equals(ap.getNetwork().getText())){
				networkIDSpinner.setSelection(i);
				break;
			}
		}
	}


}
