package com.smartdocs.gpt.document.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DocumentGetService {

	@Autowired
	private SmartstoreService smartStoreConfiguratorService;
	
	
	public String buildURL( String documentId, String method,String fileName) {
		String url = "";
		String expiration = "";
		try {
			expiration = convertLocalTimeToUTCPlus(10);
		} catch (Exception e) {
			log.error("Exception : " + e);
		}
		var smartStoreConfigurator =smartStoreConfiguratorService.getSmartStoreDetails();
		url = smartStoreConfigurator.getServerURL();
		url = url + "" + smartStoreConfigurator.getSystem()+(StringUtils.isNotEmpty(fileName)?("/"+fileName):"") + "?" + method;

		url = url + "&pVersion=" + smartStoreConfigurator.getPVersion();
		url = url + "&contRep=" + smartStoreConfigurator.getContRep();
		url = url + "&docId=" + documentId;
		url = url + "&compId=" + smartStoreConfigurator.getCompId();
		url = url + "&docProt=rud";
		if (method.equals("create")) {
			url = url + "&accessMode=c";
		} else {
			url = url + "&accessMode=r";
		}

		url = url + "&authId=" + smartStoreConfigurator.getAuthId();
		url = url + "&expiration=" + expiration;
		url = url + "&sp=true";
		url = url + "&secKey=" + smartStoreConfigurator.getSecKey();
		return url;
	}

	public static String convertLocalTimeToUTCPlus(int minuts) {
		final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
		return sdf.format(addHoursToJavaUtilDate(new Date(), minuts));
	}

	public static String getFileExtension(String fileName) {
		if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
			return "." + fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
		else
			return "";
	}

	public static Date addHoursToJavaUtilDate(Date date, int minuts) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MINUTE, minuts);
		return calendar.getTime();
	}

	public byte[] getBytesByDocId(String docId, String fileName) throws IOException {
		String url = this.buildURL(docId, "get", fileName);
	    URL urlObject = new URL(url);

	    HttpURLConnection connection = (HttpURLConnection) urlObject.openConnection();
	    connection.setRequestMethod("GET");
	    connection.setDoInput(true);

	    try (InputStream inputStream = connection.getInputStream();
	         ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
	        byte[] buffer = new byte[1024];
	        int bytesRead;
	        while ((bytesRead = inputStream.read(buffer)) != -1) {
	            byteArrayOutputStream.write(buffer, 0, bytesRead);
	        }
	        return byteArrayOutputStream.toByteArray();
	    } finally {
	        connection.disconnect();
	    }
		
	}
}
