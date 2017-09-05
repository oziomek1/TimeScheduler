package me.ozimek.timescheduler;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

/**
 * Created by wojtek on 2017-08-22.
 */

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<String> listHeader;
    private HashMap<String, List<String>> listItem;

    public ExpandableListAdapter(Context _context, List<String> _listHeader, HashMap<String, List<String>> _listItem) {
        this.context = _context;
        this.listHeader = _listHeader;
        this.listItem = _listItem;
    }
    @Override
    public int getGroupCount() {
        return this.listHeader.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.listItem.get(this.listHeader.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.listHeader.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.listItem.get(this.listHeader.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String header = (String) getGroup(groupPosition);
        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.cat_list_header, null);
        }

        TextView listHeader = (TextView) convertView.findViewById(R.id.cat_listHeader);
        listHeader.setTypeface(null, Typeface.BOLD);
        listHeader.setText(header);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final String child = (String) getChild(groupPosition, childPosition);
        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.cat_list_item, null);
        }

        TextView listChild = (TextView) convertView.findViewById(R.id.cat_listItem);
        listChild.setText(child);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
