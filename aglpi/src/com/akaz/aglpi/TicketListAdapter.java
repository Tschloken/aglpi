package com.akaz.aglpi;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.akaz.aglpi.api.Computer;
import com.akaz.aglpi.api.GlpiClient;
import com.akaz.aglpi.api.Ticket;

public class TicketListAdapter extends BaseAdapter{
	private Activity		activity;
	private List<Ticket>	tickets;
	private LayoutInflater	inflater;
	
	public TicketListAdapter(Activity a, List<Ticket> tickets) {
		assert (tickets != null);
		this.tickets = tickets;
		this.activity = a;
	}
	/*
	{
	assign=[Ljava.lang.Object;@52832214,
	observer=[Ljava.lang.Object;@528325e4,
	requester=[Ljava.lang.Object;@52832540
	}
	*/
	
	@Override
	public int getCount() {
		return tickets.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	public List<Ticket> getTickets() {
		return tickets;
	}

	public void setTickets(List<Ticket> tickets) {
		this.tickets = tickets;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (inflater == null)
			inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (convertView == null)
            convertView = inflater.inflate(R.layout.tickets_row, null);
		if (tickets.get(position).get("name") != null)
			try {
				((TextView)convertView.findViewById(R.id.title)).setText(new String(tickets.get(position).get("name").toString().getBytes("iso8859-1"), "UTF-8"));
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}
		if (tickets.get(position).get("date") != null)
			((TextView)convertView.findViewById(R.id.date)).setText(tickets.get(position).get("date").toString());
		if (tickets.get(position).get("users") != null) {
			HashMap<String, Object> users = (HashMap<String, Object>) tickets.get(position).get("users");
			//HashMap<String, HashMap<String, String>> users = (HashMap<String, HashMap<String, String>>) tickets.get(position).get("users");
			//((TextView)convertView.findViewById(R.id.requester)).setText(users.get("requester").get("name"));
			//((TextView)convertView.findViewById(R.id.user)).setText(tickets.get(position).get("users").toString());
			StringBuilder sb = new StringBuilder();
			for (Object row : ((Object[])users.get("requester"))) {
				sb.append(((HashMap<String, String>)row).get("name"));
				sb.append(",");
			}
			//Log.d("Type", sb.substring(0, sb.length()-1));
			try {
				((TextView)convertView.findViewById(R.id.requester)).setText(new String(sb.substring(0, sb.length()-1).getBytes("iso8859-1"), "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			//Log.d("Type", ((HashMap<String, String>)[0]).get("name").toString());
		}
		
		return convertView;
	}

}
