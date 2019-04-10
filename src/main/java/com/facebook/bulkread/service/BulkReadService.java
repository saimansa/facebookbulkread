package com.facebook.bulkread.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.facebook.bulkread.model.Data;
import com.facebook.bulkread.model.LeadRequest;
import com.facebook.bulkread.model.LeadResponse;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class BulkReadService {

	@Autowired
	RestTemplate restTemplate;

	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(BulkReadService.class);
	private final String BASEURL = "https://graph.facebook.com/v3.2/";

	public ResponseEntity<LeadResponse> getLeadsByFilter(LeadRequest lead) {
		try {
			log.info("Input request: {}", lead);
			String url = BASEURL + lead.getFormID() + "/leads" + lead.constructQueryParams();
			log.info("Graph Request Url : {}", url);
			LeadResponse leadResponse = restTemplate.getForObject(url, LeadResponse.class);

			String sinceUnixTimestamp = lead.getSince();
			String untilUnixTimestamp = lead.getUntil();

			if (lead.isEmpty(sinceUnixTimestamp) || lead.isEmpty(untilUnixTimestamp))
				return new ResponseEntity<LeadResponse>(leadResponse, HttpStatus.OK);

			Data[] leadData = leadResponse.getData();
			List<Data> finalData = new ArrayList<>();
			for (Data data : Arrays.asList(leadData)) {
				if (tsToSec8601(data.getCreated_time()) >= Long.parseLong(sinceUnixTimestamp)
						&& tsToSec8601(data.getCreated_time()) <= Long.parseLong(untilUnixTimestamp))
					finalData.add(data);
			}

			leadResponse.setData(finalData.toArray(new Data[0]));

			return new ResponseEntity<LeadResponse>(leadResponse, HttpStatus.OK);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return new ResponseEntity<LeadResponse>(HttpStatus.NOT_FOUND);
	}

	public static long tsToSec8601(String timestamp) {
		// parsing date from ISO 8601 standard
		Instant instance = Instant.parse(timestamp.split("\\+")[0] + 'Z');
		// get given time in unix time
		return instance.getEpochSecond();
	}
}
