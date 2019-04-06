package com.facebook.bulkread.model;

public class Paging {
	private String next;

	private Cursors cursors;

	public String getNext() {
		return next;
	}

	public void setNext(String next) {
		this.next = next;
	}

	public Cursors getCursors() {
		return cursors;
	}

	public void setCursors(Cursors cursors) {
		this.cursors = cursors;
	}

	@Override
	public String toString() {
		return "ClassPojo [next = " + next + ", cursors = " + cursors + "]";
	}
}