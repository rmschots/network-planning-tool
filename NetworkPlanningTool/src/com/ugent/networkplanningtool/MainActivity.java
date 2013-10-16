package com.ugent.networkplanningtool;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.TextView;

import com.ugent.networkplanningtool.layout.DrawingView;
import com.ugent.networkplanningtool.layout.MyScrollBar;
import com.ugent.networkplanningtool.model.FloorModel;

public class MainActivity extends Activity implements OnTouchListener {
	
	private DrawingView designView;
	private TextView locationText;
	private MyScrollBar hScrollBar;
	private MyScrollBar vScrollBar;
	
	private FloorModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        designView = (DrawingView) findViewById(R.id.drawingView);
        hScrollBar = (MyScrollBar) findViewById(R.id.myScrollBar1);
        vScrollBar = (MyScrollBar) findViewById(R.id.myScrollBar2);
        locationText = (TextView)findViewById(R.id.textView1);
        
        model = new FloorModel(designView.getWidth(), designView.getHeight());
        designView.setModel(model);
        hScrollBar.setModel(model);
        vScrollBar.setModel(model);
    }

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if(v == designView){
			float touchX = event.getX();
			float touchY = event.getY();

			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				locationText.setText("x: "+touchX+" y: "+touchY);;
				break;

	        case MotionEvent.ACTION_MOVE:
	            //a touch placement has changed
	            break;

	        case MotionEvent.ACTION_UP:
	            //first finger went up
	            break;

	        case MotionEvent.ACTION_CANCEL:
	            //gesture aborted (I think this means the finger was dragged outside of the touchscreen)
	            break;

	        case MotionEvent.ACTION_POINTER_DOWN:
	        	
	            //second finger (or third, or more) went down.
	            break;

	        case MotionEvent.ACTION_POINTER_UP:
	            //second finger (or more) went up.
	            break;

	        default: break;
			}
		}
		return false;
	}
    
}
