package com.ugent.networkplanningtool.layout.design;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.ugent.networkplanningtool.R;
import com.ugent.networkplanningtool.data.ConnectionPoint;
import com.ugent.networkplanningtool.data.FloorPlanObject;
import com.ugent.networkplanningtool.data.enums.ConnectionPointType;
import com.ugent.networkplanningtool.model.DrawingModel;

/**
 * View containing ConnectionPoint properties
 */
public class ConnectionPointView extends FloorPlanObjectView {

    private RadioGroup connectionTypeRadioGroup;

    private DrawingModel drawingModel;

    /**
     * Constructor setting the view type and drawing model
     * @param context the context of the parent
     * @param type the view type to be used
     * @param drawingModel the drawing model to use
     */
    public ConnectionPointView(Context context, ViewType type, DrawingModel drawingModel) {
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
    public ConnectionPointView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.design_view_connections, this, true);

        connectionTypeRadioGroup = (RadioGroup) findViewById(R.id.connectionTypeRadioGroup);

        initComponents();
    }

    private void initComponents() {
        if (connectionTypeRadioGroup != null) {
            if (type.equals(ViewType.INFO)) {
                connectionTypeRadioGroup.setEnabled(false);
                for (int i = 0; i < connectionTypeRadioGroup.getChildCount(); i++) {
                    (connectionTypeRadioGroup.getChildAt(i)).setEnabled(false);
                }
            } else {
                OnCheckedChangeListener listener = new OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        updateDrawingModel();
                    }
                };
                connectionTypeRadioGroup.setOnCheckedChangeListener(listener);
            }
        }
    }

    /**
     * Returns the selected connection type
     * @return the selected connection type
     */
    public ConnectionPointType getSelectedConnectionType() {
        RadioButton rb = (RadioButton) findViewById(connectionTypeRadioGroup.getCheckedRadioButtonId());
        ConnectionPointType type = ConnectionPointType.getConnectionPointTypeByText(rb.getText().toString());
        Log.d("DEBUG", "ConnectionPointType: " + type.getText());
        return type;
    }

    /**
     * Sets the drawing model
     * @param drawingModel the drawing model to use
     */
    public void setDrawingModel(DrawingModel drawingModel) {
        this.drawingModel = drawingModel;
    }

    /**
     * Updates the drawing model with currently selected data
     */
    public void updateDrawingModel() {
        Log.d("DEBUG", "updateDrawingModel " + drawingModel.getTouchFloorPlanObject());
        ConnectionPointType connectionPointType = getSelectedConnectionType();

        if (drawingModel.getTouchFloorPlanObject() != null
                && drawingModel.getTouchFloorPlanObject().DATA_OBJECT_TYPE.equals(FloorPlanObject.FloorPlanObjectType.CONNECTION_POINT)) {
            ConnectionPoint cp = (ConnectionPoint) drawingModel.getTouchFloorPlanObject();
            cp.setType(connectionPointType);
        } else {
            drawingModel.setPlaceMode(new ConnectionPoint(connectionPointType));
        }
        Log.d("DEBUG", "updateDrawingModel " + drawingModel.getTouchFloorPlanObject());
    }

    /**
     * Loads data of the drawing model in the view
     */
    private void loadData() {
        ConnectionPoint cp = (ConnectionPoint) drawingModel.getSelected();
        for (int i = 0; i < connectionTypeRadioGroup.getChildCount(); i++) {
            RadioButton rb = (RadioButton) connectionTypeRadioGroup.getChildAt(i);
            if (rb.getText().equals(cp.getType().getText())) {
                rb.setChecked(true);
                break;
            }
        }
    }
}
