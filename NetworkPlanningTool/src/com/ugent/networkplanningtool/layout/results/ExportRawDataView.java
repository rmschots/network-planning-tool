package com.ugent.networkplanningtool.layout.results;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.ugent.networkplanningtool.R;
import com.ugent.networkplanningtool.data.enums.results.ExportRawDataType;

/**
 * View used to export resulting data
 */
public class ExportRawDataView extends LinearLayout {

    private RadioGroup exportRadioGroup;
    private RadioButton normalizedPlanRadioButton;
    private RadioButton optimizedPlanRadioButton;
    private RadioButton coverageDataRadioButton;
    private RadioButton exposureInfoRadioButton;
    private RadioButton benchmarksRadioButton;

    /**
     * Default constructor
     * @param context the context of the parent
     * @param attrs the attribute set
     */
    public ExportRawDataView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.results_view_exportrawdata, this, true);

        exportRadioGroup = (RadioGroup) findViewById(R.id.exportRawDataRadioGroup);

        normalizedPlanRadioButton = (RadioButton) findViewById(R.id.normalizedPlanRadioButton);
        optimizedPlanRadioButton = (RadioButton) findViewById(R.id.optimizedPlanRadioButton);
        coverageDataRadioButton = (RadioButton) findViewById(R.id.coverageDataRadioButton);
        exposureInfoRadioButton = (RadioButton) findViewById(R.id.exposureInfoRadioButton);
        benchmarksRadioButton = (RadioButton) findViewById(R.id.benchmarksRadioButton);
        coverageDataRadioButton.setChecked(true);

        normalizedPlanRadioButton.setTag(ExportRawDataType.NORMALIZED_PLAN);
        optimizedPlanRadioButton.setTag(ExportRawDataType.OPTIMIZED_PLAN);
        coverageDataRadioButton.setTag(ExportRawDataType.COVERAGE_DATA);
        exposureInfoRadioButton.setTag(ExportRawDataType.EXPOSURE_INFO);
        benchmarksRadioButton.setTag(ExportRawDataType.BENCHMARK);
    }

    /**
     * Returns which data to export
     * @return which data to export
     */
    public ExportRawDataType getExportType() {
        Object o = findViewById(exportRadioGroup.getCheckedRadioButtonId()).getTag();
        if (o != null && o instanceof ExportRawDataType) ;
        return (ExportRawDataType) o;
    }
}