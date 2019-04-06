package com.facebook.bulkread.model;

public class LeadRequest {
	private String accessToken = "EAAFNsBlN4joBANQO80CWkQhkdRZBPFTGQYSHF2qAKyqwom7ZAJbvV3bqwmNis2ZBv0OoqQZBKp9EFUlS7wWpttsfjZCbY6yZBqjVdi195iZAwWXRfRKVLle5GhlZAkH90d81pNyzHr4zCzj0cl263odWv7WefZANrqc8ZD";
	private String formID;
	private String limit = "1000";
	private String since;
	private String until;
	private String before;
	private String after;

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getFormID() {
		return formID;
	}

	public void setFormID(String formID) {
		this.formID = formID;
	}

	public String getLimit() {
		return limit;
	}

	public void setLimit(String limit) {
		this.limit = limit;
	}

	public String constructQueryParams() {
		StringBuilder builder = new StringBuilder();

		builder.append("?access_token=" + this.accessToken);
		builder.append("&limit=" + this.limit);
		if (isNotEmpty(this.since))
			builder.append("&since=" + this.since);
		if (isNotEmpty(this.until))
			builder.append("&until=" + this.until);
		if (isNotEmpty(this.before))
			builder.append("&before=" + this.before);
		if (isNotEmpty(this.after))
			builder.append("&after=" + this.after);

		return builder.toString();
	}

	private boolean isNotEmpty(String input) {
		if (input != null && !input.isEmpty())
			return true;

		return false;
	}
}
