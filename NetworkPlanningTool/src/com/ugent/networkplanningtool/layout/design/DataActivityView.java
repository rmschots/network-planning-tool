package com.ugent.networkplanningtool.layout.design;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.ugent.networkplanningtool.R;
import com.ugent.networkplanningtool.data.DataActivity;
import com.ugent.networkplanningtool.data.FloorPlanObject;
import com.ugent.networkplanningtool.data.enums.ActivityType;
import com.ugent.networkplanningtool.model.DrawingModel;

/**
 * View containing DataActivity properties
 */
public class DataActivityView extends FloorPlanObjectView {

    private RadioGroup activityTypeRadioGroup;

    private DrawingModel drawingModel;

    /**
     * Constructor setting the view type and drawing model
     * @param context the context of the parent
     * @param type the view type to be used
     * @param drawingModel the drawing model to use
     */
    public DataActivityView(Context context, ViewType type, DrawingModel drawingModel) {
        super(context, type);
        this.drawingModel = drawingModel;
        init();
        loadData();
    }

    /**
     * Default constructor
     * @param context the context of the parent
     * @param attrs the attribute set
     */
    public DataActivityView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.design_view_activities, this, true);

        activityTypeRadioGroup = (RadioGroup) findViewById(R.id.activityTypeRadioGroup);

        initComponents();
    }

    private void initComponents() {
        if (activityTypeRadioGroup != null) {
            if (type.equals(ViewType.INFO)) {
                activityTypeRadioGroup.setEnabled(false);
                for (int i = 0; i < activityTypeRadioGroup.getChildCount(); i++) {
                    activityTypeRadioGroup.getChildAt(i).setEnabled(false);
                }
            } else {
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

    /**
     * Returns the selected connection type
     * @return the selected connection type
     */
    public ActivityType getSelectedConnectionType() {
        RadioButton rb = (RadioButton) findViewById(activityTypeRadioGroup.getCheckedRadioButtonId());
        ActivityType type = ActivityType.getActivityTypeByText(rb.getText().toString());
        Log.d("DEBUG", "ActivityType: " + type.getText());
        return type;
    }

    /**
     * Sets the drawing model
     * @param drawingModel the drawing model
     */
    public void setDrawingModel(DrawingModel drawingModel) {
        this.drawingModel = drawingModel;
    }

    /**
     * Updates the drawing model with currently selected data
     */
    public void updateDrawingModel() {
        Log.d("DEBUG", "updateDrawingModel " + drawingModel.getTouchFloorPlanObject());
        ActivityType activityType = getSelectedConnectionType();

        if (drawingModel.getTouchFloorPlanObject() != null
                && drawingModel.getTouchFloorPlanObject().DATA_OBJECT_TYPE.equals(FloorPlanObject.FloorPlanObjectType.DATA_ACTIVITY)) {
            DataActivity cp = (DataActivity) drawingModel.getTouchFloorPlanObject();
            cp.setType(activityType);
        } else {
            drawingModel.setPlaceMode(new DataActivity(activityType));
        }
        Log.d("DEBUG", "updateDrawingModel " + drawingModel.getTouchFloorPlanObject());
    }

    /**
     * Loads data of the drawing model in the view
     */
    private void loadData() {
        DataActivity da = (DataActivity) drawingModel.getSelected();
        for (int i = 0; i < activityTypeRadioGroup.getChildCount(); i++) {
            RadioButton rb = (RadioButton) activityTypeRadioGroup.getChildAt(i);
            if (rb.getText().equals(da.getType().getText())) {
                rb.setChecked(true);
                break;
            }
        }
    }

}
