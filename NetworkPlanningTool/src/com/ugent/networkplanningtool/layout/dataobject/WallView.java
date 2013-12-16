package com.ugent.networkplanningtool.layout.dataobject;

import com.ugent.networkplanningtool.R;
import com.ugent.networkplanningtool.data.Material;
import com.ugent.networkplanningtool.data.SnapTo;
import com.ugent.networkplanningtool.data.Thickness;
import com.ugent.networkplanningtool.data.Wall;
import com.ugent.networkplanningtool.data.WallType;
import com.ugent.networkplanningtool.model.DrawingModel;
import com.ugent.networkplanningtool.utils.UniqueIdGenerator;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class WallView extends LinearLayout {
	
	private WallType wallType;
	private RadioGroup materialsRadioGroup;
	private RadioGroup thicknessRadioGroup;
	private RadioGroup snapToRadioGroup;
	private Button stopDrawingButton;
	
	private boolean draw;
	
	private DrawingModel drawingModel;

	public WallView(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.WallView);
		draw = a.getBoolean(R.styleable.WallView_draw, false);
		a.recycle();
		
		LayoutInflater inflater = (LayoutInflater) context
		        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.design_view_wall, this, true);
		
		materialsRadioGroup = (RadioGroup) findViewById(R.id.materialRadioGroup);
		thicknessRadioGroup = (RadioGroup) findViewById(R.id.thicknessRadioGroup);
		snapToRadioGroup = (RadioGroup) findViewById(R.id.snapToRadioGroup);
		stopDrawingButton = (Button) findViewById(R.id.stopDrawingButton);
		
		Object tag = getTag();
		if(tag != null){
			if(tag.equals("doors")){
				wallType = WallType.DOOR;
				initComponents(R.array.doorMaterials);
			}else if(tag.equals("walls")){
				wallType = WallType.WALL;
				initComponents(R.array.wallMaterials);
			}else if(tag.equals("windows")){
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
			}
			button = (RadioButton) materialsRadioGroup.getChildAt(0);
			//button.setBackgroundColor((Material.getMaterialByText(button.getText().toString())).getColor());
			button.setChecked(true);
			OnCheckedChangeListener listener = new OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(RadioGroup group, int checkedId){
					if(drawingModel != null){
						RadioButton rb = (RadioButton) findViewById(checkedId);
						((Wall)drawingModel.getTouchDataObject()).setMaterial((Material) rb.getTag());
					}
				}
			};
			materialsRadioGroup.setOnCheckedChangeListener(listener);
			
			String[] thicknessess = getResources().getStringArray(R.array.thicknessArray);
			for(String thickness : thicknessess) {
			    button = new RadioButton(getContext());
			    button.setText(thickness);
			    button.setTag(Thickness.getThicknessByText(thickness));
			    button.setId(UniqueIdGenerator.generateViewId());
			    thicknessRadioGroup.addView(button);
			}
			button = (RadioButton) thicknessRadioGroup.getChildAt(0);
			button.setChecked(true);
			listener = new OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(RadioGroup group, int checkedId){
					if(drawingModel != null){
						RadioButton rb = (RadioButton) findViewById(checkedId);
						((Wall)drawingModel.getTouchDataObject()).setThickness((Thickness) rb.getTag());
					}
				}
			};
			thicknessRadioGroup.setOnCheckedChangeListener(listener);
			
			String[] snaptos = getResources().getStringArray(R.array.snapToArray);
			for(String snapto : snaptos) {
			    button = new RadioButton(getContext());
			    button.setText(snapto);
			    button.setTag(SnapTo.getSnapToByText(snapto));
			    button.setId(UniqueIdGenerator.generateViewId());
			    snapToRadioGroup.addView(button);
			}
			button = (RadioButton) snapToRadioGroup.getChildAt(0);
			button.setChecked(true);
			listener = new OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(RadioGroup group, int checkedId){
					if(drawingModel != null){
						RadioButton rb = (RadioButton) findViewById(checkedId);
						drawingModel.setSnapToGrid((SnapTo)rb.getTag());
					}
				}
			};
			snapToRadioGroup.setOnCheckedChangeListener(listener);
			stopDrawingButton.setVisibility(draw?View.VISIBLE:View.GONE);
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
		if(drawingModel.getTouchDataObject() instanceof Wall){
			Wall wall = (Wall) drawingModel.getTouchDataObject();
			wall.setWallType(wallType);
			wall.setMaterial(material);
			wall.setThickness(thickness);
		}else{
			drawingModel.setPlaceMode(new Wall(wallType, thickness, material));
		}
		drawingModel.setSnapToGrid(getSnapTo());
		Log.d("DEBUG","updateDrawingModel "+drawingModel.getTouchDataObject());
	}

}
