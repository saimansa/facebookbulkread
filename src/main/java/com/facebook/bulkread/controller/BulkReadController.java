package com.facebook.bulkread.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.facebook.bulkread.model.LeadRequest;
import com.facebook.bulkread.model.LeadResponse;
import com.facebook.bulkread.service.BulkReadService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class BulkReadController {

	@Autowired
	BulkReadService bulkReadService;

	@PostMapping(path = "/api/getLeadDetails", consumes = "application/json", produces = "application/json")
	public ResponseEntity<LeadResponse> getLeadsByFilter(@RequestBody LeadRequest lead) {
		return bulkReadService.getLeadsByFilter(lead);

	}
}
