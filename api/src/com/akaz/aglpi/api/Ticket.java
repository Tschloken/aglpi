package com.akaz.aglpi.api;

import java.util.HashMap;

public class Ticket {
	private HashMap<String, Object> data;
	
	public Ticket () {
		this.data = new HashMap<String, Object>();
	}
	
	public Ticket(HashMap<String, Object> data) {
		this.data = data;
	}
	
	void put(String field, Object value) {
		this.data.put(field, value);
	}
	
	Object get(String field) {
		if (this.data.containsKey(field))
			return this.data.get(field);
		else
			return null;
	}
	
}
