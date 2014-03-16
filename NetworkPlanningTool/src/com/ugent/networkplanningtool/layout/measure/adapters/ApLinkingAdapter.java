package com.ugent.networkplanningtool.layout.measure.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.ugent.networkplanningtool.R;
import com.ugent.networkplanningtool.layout.measure.ApLinkingRowItem;

import java.util.List;

/**
 * Created by Roel on 15/03/14.
 */
public class ApLinkingAdapter extends BaseAdapter {
    Context context;
    List<ApLinkingRowItem> rowItems;

    public ApLinkingAdapter(Context context, List<ApLinkingRowItem> items) {
        this.context = context;
        this.rowItems = items;
    }

    /*private view holder class*/
    private class ViewHolder {
        TextView sidText;
        Spinner apSpinner;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        LayoutInflater mInflater = (LayoutInflater)
                context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.measure_aplinking_rowview, null);
            holder = new ViewHolder();
            holder.sidText = (TextView) convertView.findViewById(R.id.apLinkingSIDText);
            holder.apSpinner = (Spinner) convertView.findViewById(R.id.apLinkingApSpinner);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ApLinkingRowItem rowItem = (ApLinkingRowItem) getItem(position);

        holder.sidText.setText(rowItem.getAp().getName());
        holder.apSpinner.setAdapter(rowItem.getApAdapter());

        return convertView;
    }

    @Override
    public int getCount() {
        return rowItems.size();
    }

    @Override
    public Object getItem(int position) {
        return rowItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return rowItems.indexOf(getItem(position));
    }
}
