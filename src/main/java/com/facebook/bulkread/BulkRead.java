package com.facebook.bulkread;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class BulkRead {

	public final String access_token = "EAAFNsBlN4joBABxNuw8kvaIWwEJrDYbC8ckASEypxPfCVdK5RmPEPotIXedp4z6J1J1YLEKPDuWhZCrIlZCKZCPNJm85TmAYYFIZCCz2ohsxhbvqDKo4RGk5icZCpsc43wZCOvyWlJigIalx3k7cOmuS6ZAok3wTTWxbr1kkZChvHOZBixeZA67KZBHGGtPBXlZA5uM1Mw6gOdHLXkKmHfbsdqtEnoOCQ6ZBwzIZAIBag5XQHAfgZDZD";
	private final String USER_AGENT = "Mozilla/5.0";
	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(BulkRead.class);

	@GetMapping("/leads")
	public String getLeads(HttpServletRequest request) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			Enumeration<String> enumeration = request.getParameterNames();
			Map<String, Object> modelMap = new HashMap<>();
			while (enumeration.hasMoreElements()) {
				String parameterName = enumeration.nextElement();
				modelMap.put(parameterName, request.getParameter(parameterName));
			}
			log.info("Input query params {}",modelMap);
			String url = "https://graph.facebook.com/v3.2/277385353205976/leads";
			URL obj = new URL(url + "?access_token=" + access_token);
			HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
			con.setRequestProperty("User-Agent", USER_AGENT);

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
			ObjectWriter writer = mapper.writer(new DefaultPrettyPrinter());
			writer.writeValue(new File("D:/facebookwebhook/leadInfo.json"),
					mapper.readValue(content.toString(), new TypeReference<Map<String, Object>>() {
					}));
			log.info("testkdsglsd");
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return USER_AGENT;

	}
}
