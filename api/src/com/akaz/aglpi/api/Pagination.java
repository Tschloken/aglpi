package com.akaz.aglpi.api;

/**
 * Define a page for a request
 */
public class Pagination {
	private int start, limit;
	
	public Pagination(int start, int limit) {
		this.start = start;
		this.limit = limit;
	}
	
	public Pagination(int count) {
		this.start = 0;
		this.limit = count;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}
}
