package com.facebook.bulkread.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
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

			if (lead.isNotEmpty(lead.getSince()) && lead.isNotEmpty(lead.getUntil())) {
				Data[] leadData = leadResponse.getData();
				List<Data> finalData = new ArrayList<>();

				for (int i = 0; i < leadData.length; i++) {
					Data data = leadData[i];
					Integer time = tsToSec8601(data.getCreated_time());
					if (time > Integer.parseInt(lead.getSince()) && time < Integer.parseInt(lead.getUntil())) {
						finalData.add(data);
					}
				}
				leadResponse.setData(finalData.toArray(new Data[0]));
			}
			return new ResponseEntity<LeadResponse>(leadResponse, HttpStatus.OK);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return new ResponseEntity<LeadResponse>(HttpStatus.NOT_FOUND);
	}

	public static Integer tsToSec8601(String timestamp) {
		if (timestamp == null)
			return null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
			Date dt = sdf.parse(timestamp);
			long epoch = dt.getTime();
			return (int) (epoch / 1000);
		} catch (ParseException e) {
			return null;
		}
	}
}
