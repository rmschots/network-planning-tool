package com.ugent.networkplanningtool.layout.measure;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.ugent.networkplanningtool.R;
import com.ugent.networkplanningtool.data.AccessPoint;
import com.ugent.networkplanningtool.data.RealAccessPoint;
import com.ugent.networkplanningtool.layout.measure.adapters.ApLinkingAdapter;
import com.ugent.networkplanningtool.model.FloorPlanModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Roel on 13/03/14.
 */
public class ApLinkingView extends LinearLayout {

    private ListView lv;
    private ArrayAdapter<ApLinkingRowItem> la;
    private List<RealAccessPoint> realAccessPointList;

    public ApLinkingView(Context context, List<RealAccessPoint> realAccessPointList) {
        super(context);
        this.realAccessPointList = realAccessPointList;
        init();
    }


    private void init() {
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.measure_aplinking, this, true);

        lv = (ListView) findViewById(R.id.apLinkingListView);

        FloorPlanModel.getInstance().getAccessPointList();


        List<ApLinkingRowItem> rvList = new ArrayList<ApLinkingRowItem>();

        List<AccessPoint> apList = FloorPlanModel.getInstance().getAccessPointList();

        for (AccessPoint ap : apList) {
            List<RealAccessPoint> links = new ArrayList<RealAccessPoint>();
            if (ap.getRap() != null) {
                links.add(ap.getRap());
            }
            for (RealAccessPoint rap : realAccessPointList) {
                if (!rap.equals(ap.getRap()) && ap.getFrequency().equals(rap.getFrequency())) {
                    links.add(rap);
                }
            }
            ArrayAdapter<RealAccessPoint> apAdapter = new ArrayAdapter<RealAccessPoint>(getContext(), android.R.layout.simple_spinner_dropdown_item, links);
            rvList.add(new ApLinkingRowItem(ap, apAdapter));
        }

        ApLinkingAdapter ala = new ApLinkingAdapter(getContext(), rvList);

        lv.setAdapter(ala);
    }
}
