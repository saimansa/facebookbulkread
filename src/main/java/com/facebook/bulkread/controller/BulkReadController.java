package com.facebook.bulkread.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.facebook.bulkread.service.BulkReadService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class BulkReadController {

	@Autowired
	BulkReadService bulkReadService;

	public final String access_token = "EAAFNsBlN4joBANQO80CWkQhkdRZBPFTGQYSHF2qAKyqwom7ZAJbvV3bqwmNis2ZBv0OoqQZBKp9EFUlS7wWpttsfjZCbY6yZBqjVdi195iZAwWXRfRKVLle5GhlZAkH90d81pNyzHr4zCzj0cl263odWv7WefZANrqc8ZD";
	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(BulkReadController.class);

	@GetMapping("/{formId}/leads")
	public Map<String, Object> getLeads(HttpServletRequest request, @PathVariable("formId") String formId) {
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> map = new HashMap<String, Object>();
		int limit = 1000;
		Boolean status = false;
		Map<String, Object> paging = null;
		List<Map<String, Object>> data = new ArrayList<>();
		List<Map<String, Object>> finalData = new ArrayList<>();
		try {
			Enumeration<String> enumeration = request.getParameterNames();
			Map<String, Object> modelMap = new HashMap<>();
			while (enumeration.hasMoreElements()) {
				String parameterName = enumeration.nextElement();
				modelMap.put(parameterName, request.getParameter(parameterName));
			}
			log.info("Input query params {}", modelMap);
			log.info("Input path variable {}", formId);
			String url = "https://graph.facebook.com/v3.2/" + formId + "/leads";
			StringBuilder inputParams = new StringBuilder();
			inputParams.append("?access_token=" + access_token);
			if (modelMap.containsKey("limit")) {
				limit = Integer.parseInt((String) modelMap.get("limit"));
				inputParams.append("&limit=" + modelMap.get("limit"));
			} else {
				inputParams.append("&limit=" + limit);
			}
			// Time based pagination
			if (modelMap.containsKey("since"))
				inputParams.append("&since=" + modelMap.get("since"));
			if (modelMap.containsKey("until"))
				inputParams.append("&until=" + modelMap.get("until"));
			// Cursor based pagination
			if (modelMap.containsKey("before"))
				inputParams.append("&before=" + modelMap.get("before"));
			if (modelMap.containsKey("after"))
				inputParams.append("&after=" + modelMap.get("after"));

			String urlpath = url + inputParams.toString();

			do {
				Map<String, String> res = bulkReadService.fetchAllLeads(urlpath);
				if (res.get("status").equals("false")) {
					throw new RuntimeException(res.get("error"));
				}
				String response = res.get("response");
				System.out.println("response:" + response);
				Map<String, Object> mapRes = mapper.readValue(response, new TypeReference<Map<String, Object>>() {
				});

				paging = (Map<String, Object>) mapRes.get("paging");
				data = (List<Map<String, Object>>) mapRes.get("data");
				if (paging.containsKey("next") && data.size() < limit) {
					urlpath = (String) paging.get("next");
					status = true;
				} else {
					status = false;
				}
				finalData.addAll(data);
			} while (status);

			status = true;

		} catch (MalformedURLException e1) {
			log.error(e1.getMessage(), e1);
			map.put("error", e1.getMessage());
		} catch (IOException e) {
			log.error(e.getMessage(), e);
			map.put("error", e.getMessage());
		} catch(Exception ex){
			log.error(ex.getMessage(), ex);
			map.put("error", ex.getMessage());
		}finally {
			map.put("data", finalData);
			map.put("paging", paging);
			map.put("status", status);
		}
		return map;
	}

}
