package com.ugent.networkplanningtool.layout.dataobject;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.ugent.networkplanningtool.R;
import com.ugent.networkplanningtool.data.DataObject.DataObjectType;
import com.ugent.networkplanningtool.data.Wall;
import com.ugent.networkplanningtool.data.enums.Material;
import com.ugent.networkplanningtool.data.enums.SnapTo;
import com.ugent.networkplanningtool.data.enums.Thickness;
import com.ugent.networkplanningtool.data.enums.WallType;
import com.ugent.networkplanningtool.model.DrawingModel;
import com.ugent.networkplanningtool.utils.UniqueIdGenerator;

public class WallView extends DataObjectView {
	
	private WallType wallType;
	private RadioGroup materialsRadioGroup;
	private RadioGroup thicknessRadioGroup;
	private TextView snapToText;
	private RadioGroup snapToRadioGroup;
	private Button stopDrawingButton;
	
	private Object tag;
	
	private DrawingModel drawingModel;
	
	public WallView(Context context, ViewType type, DrawingModel drawingModel){
		super(context, type);
		this.drawingModel = drawingModel;
		this.tag = ((Wall)drawingModel.getTouchDataObject()).getWallType().getText();
		init();
		loadData();
	}

	public WallView(Context context, AttributeSet attrs) {
		super(context, attrs);
        tag = getTag();
        init();

	}
	
	private void init(){
		LayoutInflater inflater = (LayoutInflater) getContext()
		        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.design_view_wall, this, true);
		
		materialsRadioGroup = (RadioGroup) findViewById(R.id.materialRadioGroup);
		thicknessRadioGroup = (RadioGroup) findViewById(R.id.thicknessRadioGroup);
		snapToText = (TextView) findViewById(R.id.snapToText);
		snapToRadioGroup = (RadioGroup) findViewById(R.id.snapToRadioGroup);
		stopDrawingButton = (Button) findViewById(R.id.stopDrawingButton);
		
		if(tag != null){
			if(tag.equals(getResources().getString(R.string.doorText))){
				wallType = WallType.DOOR;
				initComponents(R.array.doorMaterials);
			}else if(tag.equals(getResources().getString(R.string.wallText))){
				wallType = WallType.WALL;
				initComponents(R.array.wallMaterials);
			}else if(tag.equals(getResources().getString(R.string.windowText))){
				wallType = WallType.WINDOW;
				initComponents(R.array.windowMaterials);
			}
		}
	}
	
	private void initComponents(int materialsArrayId){
		if(materialsRadioGroup != null){
			String[] materials = getResources().getStringArray(materialsArrayId);
			RadioButton button;
			for(String material : materials) {
			    button = new RadioButton(getContext());
			    button.setText(material);
			    button.setTag(Material.getMaterialByText(material));
			    button.setId(UniqueIdGenerator.generateViewId());
			    materialsRadioGroup.addView(button);
			    if(type.equals(ViewType.INFO)){
			    	button.setEnabled(false);
			    }
			}
			button = (RadioButton) materialsRadioGroup.getChildAt(0);
			button.setChecked(true);
			
			String[] thicknessess = getResources().getStringArray(R.array.thicknessArray);
			for(String thickness : thicknessess) {
			    button = new RadioButton(getContext());
			    button.setText(thickness);
			    button.setTag(Thickness.getThicknessByText(thickness));
			    button.setId(UniqueIdGenerator.generateViewId());
			    thicknessRadioGroup.addView(button);
			    if(type.equals(ViewType.INFO)){
			    	button.setEnabled(false);
			    }
			}
			button = (RadioButton) thicknessRadioGroup.getChildAt(0);
			button.setChecked(true);
			
			String[] snaptos = getResources().getStringArray(R.array.snapToArray);
			for(String snapto : snaptos) {
			    button = new RadioButton(getContext());
			    button.setText(snapto);
			    button.setTag(SnapTo.getSnapToByText(snapto));
			    button.setId(UniqueIdGenerator.generateViewId());
			    snapToRadioGroup.addView(button);
			    if(type.equals(ViewType.INFO)){
			    	button.setEnabled(false);
			    }
			}
			
			if(type.equals(ViewType.DRAW)){
				button = (RadioButton) snapToRadioGroup.getChildAt(0);
				button.setChecked(true);
			}else{
				snapToText.setVisibility(View.GONE);
				snapToRadioGroup.setVisibility(View.GONE);
			}
			
			if(type.equals(ViewType.INFO)){
				materialsRadioGroup.setEnabled(false);
				thicknessRadioGroup.setEnabled(false);
			}else{
				OnCheckedChangeListener listener = new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId){
						updateDrawingModel();
					}
				};
				materialsRadioGroup.setOnCheckedChangeListener(listener);
				thicknessRadioGroup.setOnCheckedChangeListener(listener);
				snapToRadioGroup.setOnCheckedChangeListener(listener);
			}
			
			stopDrawingButton.setVisibility(type.equals(ViewType.DRAW)?View.VISIBLE:View.GONE);
		}
	}
	
	public Material getSelectedMaterial(){
		RadioButton rb = (RadioButton) findViewById(materialsRadioGroup.getCheckedRadioButtonId());
		Material material = Material.getMaterialByText(rb.getText().toString());
		Log.d("DEBUG","material: "+material.getText());
		return material;
	}
	
	public Thickness getSelectedThickness(){
		RadioButton rb = (RadioButton) findViewById(thicknessRadioGroup.getCheckedRadioButtonId());
		Thickness thickness = Thickness.getThicknessByText(rb.getText().toString());
		Log.d("DEBUG","thickness: "+thickness.getText());
		return thickness;
	}
	
	public SnapTo getSnapTo(){
		RadioButton rb = (RadioButton) findViewById(snapToRadioGroup.getCheckedRadioButtonId());
		SnapTo snapTo = SnapTo.getSnapToByText(rb.getText().toString());
		Log.d("DEBUG","snapTo: "+snapTo.getText());
		return snapTo;
	}
	
	public WallType getWallType(){
		return wallType;
	}
	
	public void setDrawingModel(DrawingModel drawingModel){
		this.drawingModel = drawingModel;
	}
	
	public void updateDrawingModel(){
		Log.d("DEBUG","updateDrawingModel "+drawingModel.getTouchDataObject());
		WallType wallType = getWallType();
		Material material = getSelectedMaterial();
		Thickness thickness = getSelectedThickness();
		if(drawingModel.getTouchDataObject() != null
				&& drawingModel.getTouchDataObject().DATA_OBJECT_TYPE.equals(DataObjectType.WALL)){
			Wall wall = (Wall) drawingModel.getTouchDataObject();
			wall.setWallType(wallType);
			wall.setMaterial(material);
			wall.setThickness(thickness);
		}else{
            System.out.println("TOLO");
            drawingModel.setPlaceMode(new Wall(wallType, thickness, material));
		}
		if(type.equals(ViewType.DRAW)){
			drawingModel.setSnapToGrid(getSnapTo());
		}
	}

	private void loadData() {
		Wall wall = (Wall)drawingModel.getSelected();
		for(int i = 0; i < materialsRadioGroup.getChildCount(); i++){
			RadioButton rb = (RadioButton) materialsRadioGroup.getChildAt(i);
			if(rb.getTag().equals(wall.getMaterial())){
				rb.setChecked(true);
				break;
			}
		}
		for(int i = 0; i < thicknessRadioGroup.getChildCount(); i++){
			RadioButton rb = (RadioButton) thicknessRadioGroup.getChildAt(i);
			if(rb.getTag().equals(wall.getThickness())){
				rb.setChecked(true);
				break;
			}
		}
	}
}
