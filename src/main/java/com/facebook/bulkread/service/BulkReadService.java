package com.facebook.bulkread.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

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
			// configure the SSLContext with a TrustManager
			SSLContext ctx = SSLContext.getInstance("SSL");
			ctx.init(new KeyManager[0], new TrustManager[] { new DefaultTrustManager() }, new SecureRandom());
			SSLContext.setDefault(ctx);

			URL obj = new URL(url);
			HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
			// con.setRequestProperty("User-Agent", USER_AGENT)
			con.setHostnameVerifier(new HostnameVerifier() {
				@Override
				public boolean verify(String arg0, SSLSession arg1) {
					return true;
				}
			});
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
			log.info("Response data : {}", response);

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

class DefaultTrustManager implements X509TrustManager {

	@Override
	public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
	}

	@Override
	public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
	}

	@Override
	public X509Certificate[] getAcceptedIssuers() {
		return null;
	}
}
