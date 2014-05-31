package com.ugent.networkplanningtool.layout;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ugent.networkplanningtool.R;

/**
 * View used to import a background image
 */
public class ImportImage extends Dialog implements OnTouchListener, OnKeyListener, OnClickListener{
	
	private boolean completed = false;
	
	private double distance;
	private double scale;
	
	private Button moveButton;
	private Button selectButton;
	
	private ImageButton zoomInButton;
	private ImageButton zoomOutButton;
	
	private TextView coord1Tv;
	private TextView coord2Tv;
	private TextView scaleTv;
	private EditText distanceEt;
	
	private ScaleImageView iv;
	
	private Button okButton;
	private Button cancelButton;

    /**
     * Default constructor
     *
     * @param context context of the parent
     */
    public ImportImage(Context context) {
		super(context);
		LinearLayout ll = new LinearLayout(context);
		
		LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(R.layout.import_image, ll);
        
        moveButton = (Button) ll.findViewById(R.id.moveButton);
        selectButton = (Button) ll.findViewById(R.id.selectButton);
        
        zoomInButton = (ImageButton) ll.findViewById(R.id.zoomInButton);
        zoomOutButton = (ImageButton) ll.findViewById(R.id.zoomOutButton);
        
        coord1Tv = (TextView) ll.findViewById(R.id.coord1TextView);
        coord2Tv = (TextView) ll.findViewById(R.id.coord2TextView);
        scaleTv = (TextView) ll.findViewById(R.id.scaleTextView);
        distanceEt = (EditText) ll.findViewById(R.id.distanceEditText);
        distance = Double.parseDouble(distanceEt.getText().toString());
        distanceEt.setOnKeyListener(this);
        
        moveButton.setOnClickListener(this);
        selectButton.setOnClickListener(this);
        
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

    /**
     * Sets the background image bitmap
     * @param image the background image bitmap
     */
    public void setImage(Bitmap image){
		iv.setImageBitmap(image);
    }

    /**
     * Invoked when a button of this view is clicked.
     * @param v the button that has been clicked
     */
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
		}else if(v == moveButton){
			moveButton.setEnabled(false);
			selectButton.setEnabled(true);
			iv.setMode(ScaleImageView.Mode.PRE_MOVE);
		}else if(v == selectButton){
			selectButton.setEnabled(false);
			moveButton.setEnabled(true);
			iv.setMode(ScaleImageView.Mode.SELECT);
        }
    }

    /**
     * Called when the area containing the image has been touched.
     * @param v the view that has been touched
     * @param event the touch data
     * @return whether to consume the touch
     */
    @Override
	public boolean onTouch(View v, MotionEvent event) {
		updateViews();
		return false;
    }

    /**
     * Updates the components of this view.
     */
    private void updateViews(){
		Point c1 = iv.getCoord1();
		Point c2 = iv.getCoord2();
		coord1Tv.setText(c1==null?"not set":c1.x+" "+c1.y);
		coord2Tv.setText(c2==null?"not set":c2.x+" "+c2.y);
		updateScale(c1, c2);
    }

    /**
     * Updates the scale set by the user.
     * @param c1 first point to base scale on
     * @param c2 second point to base scale on
     */
    private void updateScale(Point c1, Point c2){
		if(c2!=null){
			scale = Math.sqrt(Math.pow((c1.x-c2.x), 2) + Math.pow((c1.y-c2.y), 2))/(distance*100.0);
			scaleTv.setText(String.format("%.6f", scale));
			okButton.setEnabled(true);
		}else{
			okButton.setEnabled(false);
			scaleTv.setText("\"?\"");
        }
    }

    /**
     * Called when the text of the distance EditText has been changed.
     * @param v the EditText
     * @param keyCode the relevant key
     * @param event what action the key did
     * @return whether to consume the event
     */
    @Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		try{
			distance = Double.parseDouble(distanceEt.getText().toString());
			updateScale(iv.getCoord1(), iv.getCoord2());
		}catch(NumberFormatException ex){
			okButton.setEnabled(false);
			scaleTv.setText("\"?\"");
		}
		return false;
    }

    /**
     * Returns the scale of the image
     * @return the scale of the image
     */
    public double getScale() {
		return scale;
    }

    /**
     * Returns whether the import and scaling is completed.
     * @return whether the import and scaling is completed
     */
    public boolean isCompleted(){
		return completed;
	}

}
