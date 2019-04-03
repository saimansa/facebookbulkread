package com.facebook.bulkread.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class BulkReadService {
	
	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(BulkReadService.class);

	public Map<String, String> fetchAllLeads(String url) {
		String response = "";
		Map<String, String> map = new HashMap<>();
		String status = "false";
		try {
			URL obj = new URL(url);
			HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
			// con.setRequestProperty("User-Agent", USER_AGENT);

			int responseCode = con.getResponseCode();
			log.info("\nSending 'GET' request to URL : {}", url);
			log.info("Response Code : {}", responseCode);

			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer content = new StringBuffer();
			while ((inputLine = in.readLine()) != null) {
				content.append(inputLine);
			}
			in.close();
			con.disconnect();
			
			response = content.toString();
			log.info("Response data : {}",response);
			
			status = "true";
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			map.put("error", e.getMessage());
		} finally {
			map.put("status", status);
			map.put("response", response);
		}
		return map;
	}

}
