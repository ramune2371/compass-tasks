package com.ramune.dto;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


import javax.management.InvalidAttributeValueException;


import com.ramune.util.ServerLogger;

public class HttpRequest {

	private Request request;
	private Map<String,String> headers;
	private String body;
	
	public static HttpRequest readRequest(BufferedReader reader) {
		try {
			return new HttpRequest(reader);
		} catch (IOException | InvalidAttributeValueException e) {
			ServerLogger.warn("Request読み取り時にエラー",e);
			return null;
		}
	}
	
	/**
	 * Private Constructor<br>
	 * Read Http Request info
	 * @param reader HttpRequest buffered reader
	 * @throws IOException
	 * @throws InvalidAttributeValueException
	 */
	private HttpRequest(BufferedReader reader) throws IOException, InvalidAttributeValueException {
		this.headers = new HashMap<>();
		String line = reader.readLine();
		this.request = new Request(line);
		line = reader.readLine();
		while(line != null && !line.isEmpty()) {
			String[] split = line.split(": ", 2);
			headers.put(split[0], split[1]);
			line = reader.readLine();
		}
		
		readBody(reader);
	}

	public String getBody() {return body;}
	
	public String getRequestMethod() {return request.getMethod();}
	
	public String getRequestPath() {return request.getPath();}
	
	public String getRequestVersion() {return request.getVersion();}
	
	public Map<String,String> getHeaders(){return headers;}

	private void readBody(BufferedReader reader) throws IOException {
		char[] bodyChar = new char[getContentLength()];
		reader.read(bodyChar);
		this.body = new String(bodyChar);
	}
	
	private int getContentLength() {
		String contentLength = headers.get("Content-Length");
		if (contentLength == null) {
			return 0;
		}
		return 	Integer.parseInt(contentLength);
	}
	
	/**
	 * Store info of top of http request header 
	 */
	private class Request {
		private String method;
		private String path;
		private String version;
		
		/**
		 * Constructor
		 * @param req top of http request header
		 * @throws InvalidAttributeValueException at top of http request header length is not 3
		 */
		public Request(String req) throws InvalidAttributeValueException{
			String[] reqs = req.split(" ");
			if(reqs.length != 3) 
				throw new InvalidAttributeValueException("不正なリクエストヘッダーです。");
			this.method = reqs[0];
			this.path = reqs[1];
			this.version = reqs[2];
		}
		public String getMethod() {return method;}
		public String getPath() {return path;}
		public String getVersion() {return version;}
	}
}
