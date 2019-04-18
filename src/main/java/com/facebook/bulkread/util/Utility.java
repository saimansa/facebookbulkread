package com.facebook.bulkread.util;

import org.springframework.stereotype.Component;

import com.facebook.bulkread.model.LeadRequest;
import com.facebook.bulkread.model.LeadResponse;

@Component
public class Utility {

	public String constructQueryParams(LeadRequest request, LeadResponse response) {
		StringBuilder builder = new StringBuilder();
		boolean isCursor = false;
		builder.append("?access_token=" + request.getAccessToken());

		if (isNotEmpty(request.getLimit()))
			builder.append("&limit=" + request.getLimit());
		if (isNotEmpty(request.getBefore())) {
			builder.append("&before=" + request.getBefore());
			isCursor = true;
		}
		if (!isEmptyObject(response) && isNotEmpty(response.getPaging().getNext())) {
			builder.append("&after=" + response.getPaging().getCursors().getAfter());
			isCursor = true;
		} else if (isNotEmpty(request.getAfter())) {
			builder.append("&after=" + request.getAfter());
			isCursor = true;
		}
		if (isNotEmpty(request.getSince()) && !isCursor)
			builder.append("&since=" + request.getSince());
		if (isNotEmpty(request.getUntil()) && !isCursor)
			builder.append("&until=" + request.getUntil());

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

	public boolean isEmptyObject(Object object) {
		if (object != null)
			return false;
		return true;
	}

}
