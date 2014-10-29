package com.akaz.aglpi;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.akaz.aglpi.api.Computer;
import com.akaz.aglpi.api.GlpiClient;

public class ComputerListAdapter extends BaseAdapter{
	private Activity		activity;
	private List<Computer>	computers;
	private LayoutInflater	inflater;
	
    static class ViewHolder {
    	ImageView	icon;
    	TextView	title;
    	TextView	dateTime;
    	TextView	author;
    	TextView	category;
    }
	
	public ComputerListAdapter(Activity a, List<Computer> computers) {
		assert (computers != null);
		this.computers = computers;
		this.activity = a;
	}
	
	@Override
	public int getCount() {
		return computers.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	public List<Computer> getComputers() {
		return computers;
	}

	public void setComputers(List<Computer> computers) {
		this.computers = computers;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (inflater == null)
			inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (convertView == null)
            convertView = inflater.inflate(R.layout.computers_row, null);
		if (computers.get(position).get("name") != null)
			((TextView)convertView.findViewById(R.id.label)).setText(computers.get(position).get("name").toString());
		if (computers.get(position).get("locations_id") != null)
			((TextView)convertView.findViewById(R.id.location)).setText(MainActivity.glpi.getLocationDropdown().get(Integer.valueOf(computers.get(position).get("locations_id").toString())));
		return convertView;
	}

}
