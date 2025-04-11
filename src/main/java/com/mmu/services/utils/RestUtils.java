package com.mmu.services.utils;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

public class RestUtils {

	public static String getWithHeaders(String url, MultiValueMap<String, String> headers) {
		try {
			RestTemplate restTemplate = new RestTemplate();
			restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
			HttpEntity<?> request = new HttpEntity<>(headers);
			ResponseEntity<String> responseEntity = (ResponseEntity<String>) restTemplate.exchange(url, HttpMethod.GET,
					request, String.class, HttpStatus.OK);
			if (responseEntity.getStatusCode() == HttpStatus.OK) {
				String reSponse = responseEntity.getBody();
				return reSponse;
			}
		} catch (HttpStatusCodeException exception) {
			int statusCode = exception.getStatusCode().value();
			String getMssg = exception.getMessage();
			String getStatustext = exception.getStatusText();
			// logger.info("Exception in hitToWalletApi Api:" + statusCode + "|" + getMssg +
			// "|" + getStatustext);
			return ProjectUtils.getErrorMssg(0, "EXP101", getMssg);
		} catch (Exception e) {
			e.printStackTrace();
			// logger.info("Exception in calling hitToWalletApi api.."+e.toString());
			return ProjectUtils.getErrorMssg(0, "EXP102", "Error in processing request !");
		}
		return "";
	}

	public static String postWithHeaders(String url, MultiValueMap<String, String> requestHeaders,
			String requestPayload) {
		try {

			RestTemplate restTemplate = new RestTemplate();
			restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
			requestHeaders.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
			HttpEntity<?> request = new HttpEntity<>(requestPayload.toString(), requestHeaders);
			String response = restTemplate.postForObject(url, request, String.class);
			ResponseEntity<String> responseEntity = new ResponseEntity<>(response, HttpStatus.OK);
			if (responseEntity.getStatusCode() == HttpStatus.OK) {
				String reSponse = responseEntity.getBody();
				return reSponse;
			}
		} catch (HttpStatusCodeException exception) {
			int statusCode = exception.getStatusCode().value();
			String getMssg = exception.getMessage();
			String getStatustext = exception.getStatusText();
			return ProjectUtils.getErrorMssg(0, "EXP101", getMssg);
		} catch (Exception e) {
			e.printStackTrace();
			return ProjectUtils.getErrorMssg(0, "EXP102", "Error in processing request !");
		}
		return "";
	}

	public static String puttWithHeaders(String url, MultiValueMap<String, String> requestHeaders,
			String requestPayload) {
		try {
			RestTemplate restTemplate = new RestTemplate();
			restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
			requestHeaders.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
			HttpEntity<?> request = new HttpEntity<>(requestPayload.toString(), requestHeaders);
			ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.PUT, request, String.class);
			// ResponseEntity<String> responseEntity = new ResponseEntity<>(response,
			// HttpStatus.OK);
			if (responseEntity.getStatusCode() == HttpStatus.OK) {
				String reSponse = responseEntity.getBody();
				return reSponse;
			}
		} catch (HttpStatusCodeException exception) {
			int statusCode = exception.getStatusCode().value();
			String getMssg = exception.getMessage();
			String getStatustext = exception.getStatusText();
			// logger.info("Exception in hitToWalletApi Api:" + statusCode + "|" + getMssg +
			// "|" + getStatustext);
			return ProjectUtils.getErrorMssg(0, "EXP101", getMssg);
		} catch (Exception e) {
			e.printStackTrace();
			// logger.info("Exception in calling hitToWalletApi api.."+e.toString());
			return ProjectUtils.getErrorMssg(0, "EXP102", "Error in processing request !");
		}
		return "";
	}

	private static JSONObject processFailData(int statusCod, String msg) {
		try {
			String status_cd = "0";
			int statusCode = statusCod;
			String mssg = msg;
			return new JSONObject().put("status_cd", status_cd).put("status", statusCode).put("err_mssg", mssg);
		} catch (Exception e) {
		}
		return null;

	}

	private static JSONObject resPonseSuccessData(JSONObject responseEntity) {
		try {
			String status_cd = responseEntity.optString("status_cd");
			String data = responseEntity.optString("data");
			String rek = responseEntity.optString("rek");
			String hmac = responseEntity.optString("hmac");
			return new JSONObject().put("status_cd", status_cd).put("data", data).put("rek", rek).put("hmac", hmac);
		} catch (Exception e) {
		}
		return null;
	}

	public static void main(String[] args) throws JSONException {
		String json = "{\n" + "  \"status_cd\": \"1\",\n"
				+ "  \"data\": \"eyJzdGpDZCI6IiIsImR0eSI6IlJlZ3VsYXIiLCJsZ25tIjoiU1VHQUwgQU5EIERBTUFOSSBVVElMSVRZIFNFUlZJQ0VTIFBSSVZBVEUgTElNSVRFRCIsImFkYWRyIjpbXSwiY3hkdCI6IiIsImdzdGluIjoiMDNBQUlDUzIyNzRCMVpTIiwibmJhIjpbIlNlcnZpY2UgUHJvdmlzaW9uIl0sImxzdHVwZHQiOiIxOC8wNC8yMDE4IiwiY3RiIjoiUHJpdmF0ZSBMaW1pdGVkIENvbXBhbnkiLCJyZ2R0IjoiMDEvMDcvMjAxNyIsInByYWRyIjp7ImFkZHIiOnsiYm5tIjoiIiwibG9jIjoiQ0xPQ0sgVE9XRVIgTFVESElBTkEiLCJzdCI6IlNBTlQgREFTUyBTVFJFRVQiLCJibm8iOiIxNTU1IiwiZHN0IjoiIiwic3RjZCI6IlB1bmphYiIsImNpdHkiOiIiLCJmbG5vIjoiMk5EIEZMT09SIiwibHQiOiIiLCJwbmNkIjoiMTQxMDA4IiwibGciOiIifSwibnRyIjoiU2VydmljZSBQcm92aXNpb24ifSwic3RzIjoiQWN0aXZlIiwiY3RqQ2QiOiJaRDAyMDEiLCJ0cmFkZU5hbSI6IlNVR0FMIEFORCBEQU1JTkkgVVRJTElUWSBTRVJWSUNFUyBQUklWQVRFIExJTUlURUQiLCJjdGoiOiJSQU5HRS1JIn0=\",\n"
				+ "  \"rek\": \"\",\n" + "  \"hmac\": \"\"\n" + "}";
		JSONObject response = new JSONObject(json);
		System.out.println(response.optString("status_cd"));
	}

}
