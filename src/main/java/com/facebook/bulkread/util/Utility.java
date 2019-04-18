package com.facebook.bulkread.util;

import org.springframework.stereotype.Component;

import com.facebook.bulkread.model.LeadRequest;
import com.facebook.bulkread.model.LeadResponse;

@Component
public class Utility {

	public String constructQueryParams(LeadRequest request, LeadResponse response, boolean isCursor) {
		StringBuilder builder = new StringBuilder();
		// pass access token to graph API authentication
		builder.append("?access_token=" + request.getAccessToken());

		// cursor based filter
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
		// time based filter
		if (isNotEmpty(request.getSince()) && !isCursor)
			builder.append("&since=" + request.getSince());
		if (isNotEmpty(request.getUntil()) && !isCursor)
			builder.append("&until=" + request.getUntil());
		
		// limit filter on graph API
		if (isNotEmpty(request.getLimit()) && !isCursor)
			builder.append("&limit=" + request.getLimit());

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
