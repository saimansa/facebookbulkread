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

	public String getSince() {
		return since;
	}

	public void setSince(String since) {
		this.since = since;
	}

	public String getUntil() {
		return until;
	}

	public void setUntil(String until) {
		this.until = until;
	}

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
		return "LeadRequest [accessToken=" + accessToken + ", formID=" + formID + ", limit=" + limit + ", since="
				+ since + ", until=" + until + ", before=" + before + ", after=" + after + "]";
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

	public boolean isNotEmpty(String input) {
		if (input != null && !input.isEmpty())
			return true;

		return false;
	}

	public boolean isEmpty(String input) {
		if (input != null && !input.isEmpty())
			return false;

		return true;
	}
}
