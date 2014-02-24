package com.ugent.networkplanningtool.layout.dataobject;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.ugent.networkplanningtool.R;
import com.ugent.networkplanningtool.data.ActivityType;
import com.ugent.networkplanningtool.data.DataActivity;
import com.ugent.networkplanningtool.data.DataObject.DataObjectType;
import com.ugent.networkplanningtool.model.DrawingModel;

public class DataActivityView extends DataObjectView{

	private RadioGroup activityTypeRadioGroup;
	
	private DrawingModel drawingModel;
	
	public DataActivityView(Context context, ViewType type, DrawingModel drawingModel){
		super(context, type);
		this.drawingModel = drawingModel;
		init();
		loadData();
	}

	public DataActivityView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	
	private void init(){
		LayoutInflater inflater = (LayoutInflater) getContext()
		        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.design_view_activities, this, true);
		
		activityTypeRadioGroup = (RadioGroup) findViewById(R.id.activityTypeRadioGroup);
		
		initComponents();
	}
	
	private void initComponents(){
		if(activityTypeRadioGroup != null){
			if(type.equals(ViewType.INFO)){
				activityTypeRadioGroup.setEnabled(false);
				for(int i = 0; i < activityTypeRadioGroup.getChildCount(); i++){
					((RadioButton)activityTypeRadioGroup.getChildAt(i)).setEnabled(false);
				}
			}else{
				OnCheckedChangeListener listener = new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						updateDrawingModel();
					}
				};
				activityTypeRadioGroup.setOnCheckedChangeListener(listener);
			}
		}
	}
	
	public ActivityType getSelectedConnectionType(){
		RadioButton rb = (RadioButton) findViewById(activityTypeRadioGroup.getCheckedRadioButtonId());
		ActivityType type = ActivityType.getActivityTypeByText(rb.getText().toString());
		Log.d("DEBUG","ActivityType: "+type.getText());
		return type;
	}
	
	public void setDrawingModel(DrawingModel drawingModel){
		this.drawingModel = drawingModel;
	}
	
	public void updateDrawingModel(){
		Log.d("DEBUG","updateDrawingModel "+drawingModel.getTouchDataObject());
		ActivityType activityType = getSelectedConnectionType();
		
		if(drawingModel.getTouchDataObject() != null
				&& drawingModel.getTouchDataObject().DATA_OBJECT_TYPE.equals(DataObjectType.DATA_ACTIVITY)){
			DataActivity cp = (DataActivity) drawingModel.getTouchDataObject();
			cp.setType(activityType);
		}else{
			drawingModel.setPlaceMode(new DataActivity(activityType));
		}
		Log.d("DEBUG","updateDrawingModel "+drawingModel.getTouchDataObject());
	}

	private void loadData() {
		DataActivity da = (DataActivity)drawingModel.getSelected();
		for(int i = 0; i < activityTypeRadioGroup.getChildCount(); i++){
			RadioButton rb = (RadioButton) activityTypeRadioGroup.getChildAt(i);
			if(rb.getText().equals(da.getType().getText())){
				rb.setChecked(true);
				break;
			}
		}
	}
	
}
