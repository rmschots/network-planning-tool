package com.ugent.networkplanningtool.layout.components;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.ugent.networkplanningtool.R;

import java.text.DecimalFormat;

/**
 * SeekBar implementation used for setting properties in the application.
 */
public class MySeekBar extends LinearLayout {

    private TextView titleView;
    private SeekBar seekBar;
    private TextView unitTextView;
    private TextView unitAmountTextView;

    private String title = "";
    private double minValue = 0;
    private double maxValue = 100;
    private double defaultValue = 50;
    private double stepSize = 1;
    private String unitType = "";

    /**
     * Default constructor
     * @param context the context of the parent
     */
    public MySeekBar(Context context) {
        super(context);
        init();
    }

    /**
     * Default constructor
     * @param context the context of the parent
     * @param attrs the attribute set
     */
    public MySeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MySeekBar);
        final int N = a.getIndexCount();
        for (int i = 0; i < N; i++) {
            int attr = a.getIndex(i);
            switch (attr) {
                case R.styleable.MySeekBar_title:
                    title = a.getString(attr);
                    break;
                case R.styleable.MySeekBar_minValue:
                    minValue = Double.valueOf(Float.toString(a.getFloat(attr, 0)));
                    break;
                case R.styleable.MySeekBar_maxValue:
                    maxValue = Double.valueOf(Float.toString(a.getFloat(attr, 100)));
                    break;
                case R.styleable.MySeekBar_defaultValue:
                    defaultValue = Double.valueOf(Float.toString(a.getFloat(attr, 50)));
                    break;
                case R.styleable.MySeekBar_stepSize:
                    stepSize = Double.valueOf(Float.toString(a.getFloat(attr, 1)));
                    break;
                case R.styleable.MySeekBar_unitType:
                    unitType = a.getString(attr);
                    break;
            }
        }
        a.recycle();
        init();
    }

    private void init() {
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.component_my_seekbar, this, true);

        titleView = (TextView) findViewById(R.id.seekBarTitle);
        seekBar = (SeekBar) findViewById(R.id.mySeekBar);
        unitTextView = (TextView) findViewById(R.id.unitText);
        unitAmountTextView = (TextView) findViewById(R.id.unitAmountText);

        seekBar.setMax((int) ((maxValue - minValue) / stepSize));

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                double dTmp = (maxValue - minValue) * i / seekBar.getMax();
                double rounded = Math.round((dTmp) / stepSize) * stepSize;
                double value = Math.round((minValue + rounded) * 100.0) / 100.0;
                DecimalFormat df = new DecimalFormat("###.###");
                unitAmountTextView.setText("" + df.format(minValue + rounded));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        titleView.setText(title);
        // ensure listener gets called
        seekBar.setProgress(0);
        seekBar.setProgress(1);
        seekBar.setProgress((int) ((defaultValue - minValue) * seekBar.getMax() / (maxValue - minValue)));
        unitTextView.setText(unitType);
    }

    /**
     * Returns the selected value
     * @return the selected value
     */
    public double getValue() {
        return Double.parseDouble(unitAmountTextView.getText().toString());
    }


}
