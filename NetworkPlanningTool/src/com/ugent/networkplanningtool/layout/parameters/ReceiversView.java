package com.ugent.networkplanningtool.layout.parameters;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.ugent.networkplanningtool.R;
import com.ugent.networkplanningtool.data.enums.parameters.Receiver;
import com.ugent.networkplanningtool.layout.components.MySeekBar;

import java.util.Arrays;
import java.util.List;

/**
 * View used to set the receiver's parameters
 */
public class ReceiversView extends LinearLayout {

    private ArrayAdapter<Receiver> receiversAdapter;

    private Spinner receiversSpinner;
    private MySeekBar elevationSeekBar;
    private MySeekBar gridSizeSeeBar;

    private CheckBox[] receiverTypeCheckboxes;

    /**
     * Default constructor
     * @param context the context of the parent
     * @param attrs the attribute set
     */
    public ReceiversView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.parameters_view_receivers, this, true);

        receiversSpinner = (Spinner) findViewById(R.id.receiversSpinner);
        elevationSeekBar = (MySeekBar) findViewById(R.id.elevationView);
        gridSizeSeeBar = (MySeekBar) findViewById(R.id.receiverGridView);

        receiversAdapter = new ArrayAdapter<Receiver>(getContext(), android.R.layout.simple_spinner_dropdown_item, Receiver.values());
        receiversSpinner.setAdapter(receiversAdapter);

        receiverTypeCheckboxes = new CheckBox[4];
        receiverTypeCheckboxes[0] = (CheckBox) findViewById(R.id.wifiCheckBox);
        receiverTypeCheckboxes[1] = (CheckBox) findViewById(R.id.sensonCheckBox);
        receiverTypeCheckboxes[2] = (CheckBox) findViewById(R.id.threeGCheckBox);
        receiverTypeCheckboxes[3] = (CheckBox) findViewById(R.id.fourGCheckBox);

        receiversSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Receiver r = receiversAdapter.getItem(i);
                List<String> checked = Arrays.asList(r.getTypes());
                for (CheckBox cb : receiverTypeCheckboxes) {
                    cb.setChecked(checked.contains(cb.getText()));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    /**
     * Returns the receiver type as String
     * @return the receiver type as String
     */
    public String getReceiver() {
        return receiversAdapter.getItem(receiversSpinner.getSelectedItemPosition()).getText();
    }

    /**
     * Returns the height of the receiver
     * @return the height of the receiver
     */
    public double getElevation() {
        return elevationSeekBar.getValue();
    }

    /**
     * Returns set the grid size (smaller means more locations are returned with results)
     * @return set the grid size
     */
    public double getGridSize() {
        return gridSizeSeeBar.getValue();
    }
}
