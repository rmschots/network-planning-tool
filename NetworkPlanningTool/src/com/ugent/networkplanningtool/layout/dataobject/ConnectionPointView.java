package com.ugent.networkplanningtool.layout.dataobject;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.ugent.networkplanningtool.R;
import com.ugent.networkplanningtool.data.ConnectionPoint;
import com.ugent.networkplanningtool.data.ConnectionPointType;
import com.ugent.networkplanningtool.data.DataObject.DataObjectType;
import com.ugent.networkplanningtool.model.DrawingModel;

public class ConnectionPointView extends DataObjectView {

	private RadioGroup connectionTypeRadioGroup;
	
	private DrawingModel drawingModel;
	
	public ConnectionPointView(Context context, ViewType type, DrawingModel drawingModel){
		super(context, type);
		this.drawingModel = drawingModel;
		init();
		loadData();
	}

	public ConnectionPointView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	
	private void init(){
		LayoutInflater inflater = (LayoutInflater) getContext()
		        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.design_view_connections, this, true);
		
		connectionTypeRadioGroup = (RadioGroup) findViewById(R.id.connectionTypeRadioGroup);
		
		initComponents();
	}
	
	private void initComponents(){
		if(connectionTypeRadioGroup != null){
			if(type.equals(ViewType.INFO)){
				connectionTypeRadioGroup.setEnabled(false);
				for(int i = 0; i < connectionTypeRadioGroup.getChildCount(); i++){
					(connectionTypeRadioGroup.getChildAt(i)).setEnabled(false);
				}
			}else{
				OnCheckedChangeListener listener = new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						updateDrawingModel();
					}
				};
				connectionTypeRadioGroup.setOnCheckedChangeListener(listener);
			}
		}
	}
	
	public ConnectionPointType getSelectedConnectionType(){
		RadioButton rb = (RadioButton) findViewById(connectionTypeRadioGroup.getCheckedRadioButtonId());
		ConnectionPointType type = ConnectionPointType.getConnectionPointTypeByText(rb.getText().toString());
		Log.d("DEBUG","ConnectionPointType: "+type.getText());
		return type;
	}
	
	public void setDrawingModel(DrawingModel drawingModel){
		this.drawingModel = drawingModel;
	}
	
	public void updateDrawingModel(){
		Log.d("DEBUG","updateDrawingModel "+drawingModel.getTouchDataObject());
		ConnectionPointType connectionPointType = getSelectedConnectionType();
		
		if(drawingModel.getTouchDataObject() != null
				&& drawingModel.getTouchDataObject().DATA_OBJECT_TYPE.equals(DataObjectType.CONNECTION_POINT)){
			ConnectionPoint cp = (ConnectionPoint) drawingModel.getTouchDataObject();
			cp.setType(connectionPointType);
		}else{
			drawingModel.setPlaceMode(new ConnectionPoint(connectionPointType));
		}
		Log.d("DEBUG","updateDrawingModel "+drawingModel.getTouchDataObject());
	}

	private void loadData() {
		ConnectionPoint cp = (ConnectionPoint)drawingModel.getSelected();
		for(int i = 0; i < connectionTypeRadioGroup.getChildCount(); i++){
			RadioButton rb = (RadioButton) connectionTypeRadioGroup.getChildAt(i);
			if(rb.getText().equals(cp.getType().getText())){
				rb.setChecked(true);
				break;
			}
		}
	}
}
