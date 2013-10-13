package com.ugent.networkplanningtool;

import com.ugent.networkplanningtool.layout.DrawingView;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.TextView;
import android.app.Activity;

public class MainActivity extends Activity implements OnTouchListener {
	
	private DrawingView designView;
	private TextView locationText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        designView = (DrawingView) findViewById(R.id.drawingView);
        locationText = (TextView)findViewById(R.id.textView1);
        
        designView.setOnTouchListener(this);
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
