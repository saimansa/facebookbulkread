package com.facebook.bulkread.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

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
			String url = BASEURL + lead.getFormID() + "/leads" + lead.constructQueryParams();
			log.info("Graph Request Url : {}", url);
			return restTemplate.getForEntity(url, LeadResponse.class);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return new ResponseEntity<LeadResponse>(HttpStatus.NOT_FOUND);
	}
}
