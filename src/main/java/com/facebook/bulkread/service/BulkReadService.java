package com.facebook.bulkread.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class BulkReadService {
	private final String USER_AGENT = "Mozilla/5.0";
	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(BulkReadService.class);

	public String fetchAllLeads(String url) {
		String response = "";
		try {
			URL obj = new URL(url);
			HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
			//con.setRequestProperty("User-Agent", USER_AGENT);

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
			System.out.println(content);
			response = content.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

}
