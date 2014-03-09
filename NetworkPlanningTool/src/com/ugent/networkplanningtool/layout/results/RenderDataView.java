package com.ugent.networkplanningtool.layout.results;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.ugent.networkplanningtool.R;
import com.ugent.networkplanningtool.data.ServiceData.DeusResult;
import com.ugent.networkplanningtool.model.DrawingModel;
import com.ugent.networkplanningtool.model.FloorPlanModel;

import java.util.Observable;
import java.util.Observer;

public class RenderDataView extends LinearLayout implements Observer {

    private Spinner viewSpinner;
    private TableLayout legendTable;

    private ArrayAdapter<DeusResult.ResultType> resultTypeArrayAdapter;

    private DrawingModel drawingModel;

    private DeusResult currentResult;

    public RenderDataView(Context context, DrawingModel drawingModel) {
        super(context);
        this.drawingModel = drawingModel;
        init();
    }

    public RenderDataView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.results_view_renderdata, this, true);

        viewSpinner = (Spinner) findViewById(R.id.viewSpinner);
        legendTable = (TableLayout) findViewById(R.id.legendTable);
    }

    private void updateViews(DeusResult dr) {
        currentResult = dr;
        legendTable.removeAllViewsInLayout();
        viewSpinner.setAdapter(null);
        if (dr != null) {
            resultTypeArrayAdapter = new ArrayAdapter<DeusResult.ResultType>(getContext(), android.R.layout.simple_spinner_dropdown_item, dr.getResultTypes());
            viewSpinner.setAdapter(resultTypeArrayAdapter);
            viewSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    legendTable.removeAllViews();
                    DeusResult.ResultType type = resultTypeArrayAdapter.getItem(i);
                    drawingModel.setResultRenderType(type);
                    double[] legends = type.getLegends();
                    int[] colors = type.getColors();
                    for (int j = 0; j < legends.length - 1; j++) {
                        TableRow tr = new TableRow(getContext());
                        TextView tvColor = new TextView(getContext());
                        tvColor.setEms(2);
                        tvColor.setBackgroundColor(colors[j]);
                        TextView tv = new TextView(getContext());
                        tv.setText(">= " + legends[j]);
                        tr.addView(tvColor);
                        tr.addView(tv);
                        legendTable.addView(tr);
                    }
                    TableRow tr = new TableRow(getContext());
                    TextView tvColor = new TextView(getContext());
                    tvColor.setEms(2);
                    tvColor.setBackgroundColor(colors[legends.length - 1]);
                    TextView tv = new TextView(getContext());
                    tv.setText("< " + legends[legends.length - 1]);
                    tr.addView(tvColor);
                    tr.addView(tv);
                    legendTable.addView(tr);
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                    drawingModel.setResultRenderType(null);
                }
            });
        } else {
            drawingModel.setResultRenderType(null);
        }
    }


    @Override
    public void update(Observable observable, Object o) {
        if (observable instanceof FloorPlanModel) {
            FloorPlanModel fpm = (FloorPlanModel) observable;
            if (o instanceof DeusResult) {
                updateViews(fpm.getDeusResult());
            }
        }
    }

    public void setDrawingModel(DrawingModel drawingModel) {
        this.drawingModel = drawingModel;
    }
}