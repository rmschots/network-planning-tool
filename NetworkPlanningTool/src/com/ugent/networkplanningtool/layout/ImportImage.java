package com.ugent.networkplanningtool.layout;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.View.OnKeyListener;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ugent.networkplanningtool.R;

public class ImportImage extends Dialog implements OnTouchListener, OnKeyListener, OnClickListener{
	
	private boolean completed = false;
	
	private double distance;
	private double scale;
	
	private ImageButton zoomInButton;
	private ImageButton zoomOutButton;
	
	private TextView coord1Tv;
	private TextView coord2Tv;
	private TextView scaleTv;
	private EditText distanceEt;
	
	private ScaleImageView iv;
	
	private Button okButton;
	private Button cancelButton;

	public ImportImage(Context context) {
		super(context);
		LinearLayout ll = new LinearLayout(context);
		
		LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(R.layout.import_image, ll);
        
        
        
        zoomInButton = (ImageButton) ll.findViewById(R.id.zoomInButton);
        zoomOutButton = (ImageButton) ll.findViewById(R.id.zoomOutButton);
        
        coord1Tv = (TextView) ll.findViewById(R.id.coord1TextView);
        coord2Tv = (TextView) ll.findViewById(R.id.coord2TextView);
        scaleTv = (TextView) ll.findViewById(R.id.scaleTextView);
        distanceEt = (EditText) ll.findViewById(R.id.distanceEditText);
        distanceEt.setOnKeyListener(this);
        
        zoomInButton.setOnClickListener(this);
        zoomOutButton.setOnClickListener(this);
        
        iv = (ScaleImageView) ll.findViewById(R.id.importImageView);
        iv.setOnTouchListener(this);
        
        okButton = (Button) ll.findViewById(R.id.okButton);
        cancelButton = (Button) ll.findViewById(R.id.cancelButton);
        
        okButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
        
        setContentView(ll);
	}

	public void setImage(Bitmap image){
		iv.setImageBitmap(image);
	}
	
	@Override
	public void onClick(View v) {
		if(v == zoomInButton){
			iv.zoomIn();
			updateViews();
		}else if(v == zoomOutButton){
			iv.zoomOut();
			updateViews();
		}else if(v == okButton){
			completed = true;
			dismiss();
		}else if(v == cancelButton){
			dismiss();
		}
	}
	
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		updateViews();
		return false;
	}
	
	private void updateViews(){
		Point c1 = iv.getCoord1();
		Point c2 = iv.getCoord2();
		coord1Tv.setText(c1==null?"not set":c1.x+" "+c1.y);
		coord2Tv.setText(c2==null?"not set":c2.x+" "+c2.y);
		if(c2!=null){
			scaleTv.setText(""+iv.getImageScale());
		}
	}

	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		try{
			distance = Double.parseDouble(distanceEt.getText().toString());
		}catch(NumberFormatException ex){
			// fail allowed, when user is not done typing
		}
		return false;
	}

	public double getScale() {
		
		return scale;
	}
	
	public boolean isCompleted(){
		return completed;
	}

}
