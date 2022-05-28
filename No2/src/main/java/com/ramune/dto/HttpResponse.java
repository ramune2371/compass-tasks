package com.ramune.dto;

import java.nio.charset.StandardCharsets;


import com.ramune.util.HttpStatusEnum;

public class HttpResponse {

	private final String CRLF = "\r\n";
	private final String VERSION = "HTTP/1.1";
	private final String CONTENT_HEADER = "Content-Type: ";
	private final String CONTENT_LENGTH = "Content-Length: ";
	private String statusCode;
	private String status;
	private String contentType;
	private byte[] body;
	
	/**
	 * Constructor<br>
	 * set default response
	 */
	public HttpResponse() {
		this.statusCode = "200";
		this.status = "OK";
		this.contentType = "text/plain";
		this.body = "".getBytes(StandardCharsets.UTF_8);
	}

	public byte[] getBody() {
		return body;
	}

	public void setBody(byte[] body) {
		this.body = body;
	}
	
	private void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}
	
	private void setStatus(String status) {
		this.status = status;
	}
	
	public void setStatus(HttpStatusEnum statusEnum) {
		setStatus(statusEnum.getStatus());
		setStatusCode(statusEnum.getStatusCode());
	}
	
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	
	public String getStatus() {return status;}
	
	public String getStatusCode() {return statusCode;}
	
	/**
	 * Get bytes of all response data
	 * @return all bytes of response
	 */
	public byte[] getResponse() {
		StringBuilder builder = new StringBuilder();
		builder.append(this.VERSION).append(" ").append(this.statusCode).append(" ").append(this.status).append(CRLF);
		builder.append(this.CONTENT_HEADER).append(this.contentType).append(CRLF);
		builder.append(this.CONTENT_LENGTH).append(this.body.length).append(CRLF);
		
		builder.append(CRLF);
		
		byte[] headerBytes = builder.toString().getBytes(StandardCharsets.UTF_8);
		
		byte[] response = new byte[headerBytes.length + body.length];
		System.arraycopy(headerBytes, 0, response, 0, headerBytes.length);
		System.arraycopy(body, 0, response, headerBytes.length, body.length);
		
		return response;
	}
}
