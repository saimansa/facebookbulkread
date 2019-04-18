package com.facebook.bulkread.service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
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
import com.facebook.bulkread.util.Utility;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class BulkReadService {

	@Autowired
	RestTemplate restTemplate;

	@Autowired
	Utility utility;

	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(BulkReadService.class);
	private final String BASEURL = "https://graph.facebook.com/v3.2/";

	public ResponseEntity<LeadResponse> getLeadsByFilter(LeadRequest leadRequest) {
		List<LeadResponse> listLeadRes = new ArrayList<>();
		boolean fetchStatus = false;
		LeadResponse leadResponse = null;
		boolean isTimestampBased = true;
		try {
			log.info("Input request: {}", leadRequest);
			String graphEndpoint = BASEURL + leadRequest.getFormID() + "/leads";
			String sinceUnixTimestamp = leadRequest.getSince();
			String untilUnixTimestamp = leadRequest.getUntil();
			List<Data> finalData = new ArrayList<>();

			if (utility.isEmpty(sinceUnixTimestamp) || utility.isEmpty(untilUnixTimestamp)) {
				LocalDateTime localDateTime = LocalDateTime.now(ZoneId.of("UTC"));

				untilUnixTimestamp = Long.toString(localDateTime.toEpochSecond(ZoneOffset.UTC));
				sinceUnixTimestamp = Long.toString(localDateTime.minusDays(1).toEpochSecond(ZoneOffset.UTC));

				leadRequest.setSince(sinceUnixTimestamp);
				leadRequest.setUntil(untilUnixTimestamp);
				isTimestampBased = false;
			}
			log.info("Input is time based query :{}", isTimestampBased);
			do {
				String url = graphEndpoint + utility.constructQueryParams(leadRequest, leadResponse, fetchStatus);
				log.info("Graph Request Url : {}", url);
				leadResponse = restTemplate.getForObject(url, LeadResponse.class);
				log.info("Response body: {}", leadResponse);
				if (utility.isEmpty(leadResponse.getPaging().getNext()))
					fetchStatus = false;
				else if (isTimestampBased)
					fetchStatus = true;

				listLeadRes.add(leadResponse);

			} while (fetchStatus);

			for (LeadResponse response : listLeadRes) {
				Data[] leadData = response.getData();
				for (Data data : Arrays.asList(leadData)) {
					if (tsToSec8601(data.getCreated_time()) >= Long.parseLong(sinceUnixTimestamp)
							&& tsToSec8601(data.getCreated_time()) <= Long.parseLong(untilUnixTimestamp))
						finalData.add(data);
					log.info("Response data: {} ", data);
				}
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
