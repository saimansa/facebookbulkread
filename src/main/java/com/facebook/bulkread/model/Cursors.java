package com.facebook.bulkread.model;

public class Cursors {
	private String before;

	private String after;

	public String getBefore() {
		return before;
	}

	public void setBefore(String before) {
		this.before = before;
	}

	public String getAfter() {
		return after;
	}

	public void setAfter(String after) {
		this.after = after;
	}

	@Override
	public String toString() {
		return "ClassPojo [before = " + before + ", after = " + after + "]";
	}
}