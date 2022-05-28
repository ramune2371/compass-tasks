package com.ramune.util;


public enum HttpStatusEnum {
	OK("200","OK"),
	NotFound("404","Not Found"),
	MethodNotAllowed("405","Method Not Allowed"),
	InternalServerError("500","Internal Server Error");
	
	private String statusCode;
	private String status;
	
	private HttpStatusEnum(String statusCode, String status) {
		this.statusCode = statusCode;
		this.status = status;
	}
	
	public String getStatusCode() {return statusCode; }
	
	public String getStatus() { return status;}

}
