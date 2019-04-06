package com.facebook.bulkread.model;

public class Field_data {
	private String[] values;

	private String name;

	public String[] getValues() {
		return values;
	}

	public void setValues(String[] values) {
		this.values = values;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "ClassPojo [values = " + values + ", name = " + name + "]";
	}
}