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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class BulkRead {

	public final String access_token = "EAAFNsBlN4joBANQO80CWkQhkdRZBPFTGQYSHF2qAKyqwom7ZAJbvV3bqwmNis2ZBv0OoqQZBKp9EFUlS7wWpttsfjZCbY6yZBqjVdi195iZAwWXRfRKVLle5GhlZAkH90d81pNyzHr4zCzj0cl263odWv7WefZANrqc8ZD";
	private final String USER_AGENT = "Mozilla/5.0";
	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(BulkRead.class);

	@GetMapping("/{formId}/leads")
	public Map<String,Object> getLeads(HttpServletRequest request, @PathVariable("formId") String formId) {
		ObjectMapper mapper = new ObjectMapper();
		Map<String,Object> map = new HashMap<String, Object>();
		try {
			Enumeration<String> enumeration = request.getParameterNames();
			Map<String, Object> modelMap = new HashMap<>();
			while (enumeration.hasMoreElements()) {
				String parameterName = enumeration.nextElement();
				modelMap.put(parameterName, request.getParameter(parameterName));
			}
			log.info("Input query params {}", modelMap);
			log.info("Input path variable {}",formId);
			String url = "https://graph.facebook.com/v3.2/"+formId+"/leads";
			StringBuilder inputParams = new StringBuilder();
			inputParams.append("?access_token=" + access_token);
			if (modelMap.containsKey("limit")) inputParams.append("&limit="+modelMap.get("limit"));
			if (modelMap.containsKey("since")) inputParams.append("&since="+modelMap.get("since"));
			if (modelMap.containsKey("until")) inputParams.append("&until="+modelMap.get("until"));
			
			URL obj = new URL(url + inputParams.toString());
			HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
			con.setRequestProperty("User-Agent", USER_AGENT);

			int responseCode = con.getResponseCode();
			log.info("\nSending 'GET' request to URL : {}", url + inputParams.toString());
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
			map = mapper.readValue(content.toString(), new TypeReference<Map<String,Object>>(){});

			Map<String,Object> paging = (Map<String, Object>) map.get("paging");
			if (paging.containsKey("next")) {
				
			}
			/*
			 * ObjectWriter writer = mapper.writer(new DefaultPrettyPrinter());
			 * writer.writeValue(new File("D:/facebookwebhook/leadInfo.json"),
			 * mapper.readValue(content.toString(), new TypeReference<Map<String, Object>>()
			 * { })); log.info("testkdsglsd");
			 */

		} catch (MalformedURLException e1) {
			log.error("URL Exception while connecting to service {}",e1.getMessage());
			e1.printStackTrace();
		} catch (IOException e) {
			log.error("IO Exception while connecting to service {}",e.getMessage());
			e.printStackTrace();
		}
		return map;
	}
	
}
