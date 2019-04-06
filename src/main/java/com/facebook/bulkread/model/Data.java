package com.facebook.bulkread.model;

public class Data {
	private String created_time;

	private Field_data[] field_data;

	private String id;

	public String getCreated_time() {
		return created_time;
	}

	public void setCreated_time(String created_time) {
		this.created_time = created_time;
	}

	public Field_data[] getField_data() {
		return field_data;
	}

	public void setField_data(Field_data[] field_data) {
		this.field_data = field_data;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "ClassPojo [created_time = " + created_time + ", field_data = " + field_data + ", id = " + id + "]";
	}
}