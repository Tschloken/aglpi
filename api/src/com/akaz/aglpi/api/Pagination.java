package com.akaz.aglpi.api;

/**
 * Define a page for a request
 */
public class Pagination {
	private int count, start, limit;
	
	public Pagination(int count, int start, int limit) {
		this.count = count;
		this.start = start;
		this.limit = limit;
	}
	
	public Pagination(int count) {
		this.count = count;
		this.start = 0;
		this.limit = -1;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
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
