package com.ugent.networkplanningtool.layout.measure.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.ugent.networkplanningtool.R;
import com.ugent.networkplanningtool.data.RealAccessPoint;
import com.ugent.networkplanningtool.layout.measure.ApLinkingRowItem;

import java.util.List;

/**
 * Adapter for linking drawn access points with real device detected access points
 */
public class ApLinkingAdapter extends BaseAdapter {
    private Context context;
    private List<ApLinkingRowItem> rowItems;

    public ApLinkingAdapter(Context context, List<ApLinkingRowItem> items) {
        this.context = context;
        this.rowItems = items;
    }

    /*private view holder class*/
    private class ViewHolder {
        TextView sidText;
        Spinner apSpinner;
    }

    /**
     * Returns the selected real access point at the given position in the list
     * @param position the position in the list
     * @param convertView the ap linking row view
     * @param parent the parent this view will be attached to
     * @return A View corresponding to the data at the specified position.
     */
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        final ApLinkingRowItem rowItem = (ApLinkingRowItem) getItem(position);

        if (convertView == null) {
            System.out.println("isnull");
            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.measure_aplinking_rowview, null);
            holder = new ViewHolder();
            holder.sidText = (TextView) convertView.findViewById(R.id.apLinkingSIDText);
            holder.apSpinner = (Spinner) convertView.findViewById(R.id.apLinkingApSpinner);
            holder.apSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    updateLink(rowItem, rowItem.getApAdapter().getItem(i));

                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                }
            });
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.sidText.setText(rowItem.getAp().getName());
        holder.apSpinner.setAdapter(rowItem.getApAdapter());
        holder.apSpinner.setSelection(rowItem.getApAdapter().getPosition(rowItem.getAp().getRap()));

        return convertView;
    }

    /**
     * Returns the amount of items in the list
     * @return the amount of items in the list
     */
    @Override
    public int getCount() {
        return rowItems.size();
    }

    /**
     * Returns the item at the given position in the list
     * @param position the position in the list
     * @return the item at the given position in the list
     */
    @Override
    public Object getItem(int position) {
        return rowItems.get(position);
    }

    /**
     * Returns the id of the item at the given position in the list
     * @param position the position in the list
     * @return the id of the item at the given position in the list
     */
    @Override
    public long getItemId(int position) {
        return rowItems.indexOf(getItem(position));
    }

    /**
     * Updates a link
     * @param apLinkingRowItem the item to change
     * @param rap the new real access point to link
     */
    public void updateLink(ApLinkingRowItem apLinkingRowItem, RealAccessPoint rap) {
        boolean updateOtherLink = false;
        if (!rap.equals(RealAccessPoint.getEmptyDummy())) {
            for (ApLinkingRowItem apLinkingRowItem1 : rowItems) {
                if (!apLinkingRowItem1.equals(apLinkingRowItem) && apLinkingRowItem1.getAp().getRap().equals(rap)) {
                    apLinkingRowItem1.getAp().setRap(RealAccessPoint.getEmptyDummy());
                    updateOtherLink = true;
                }
            }
        }
        apLinkingRowItem.getAp().setRap(rap);
        if (updateOtherLink) {
            notifyDataSetChanged();
        }
    }
}
