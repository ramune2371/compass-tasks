package com.ramune.util;


public enum ContentTypeEnum {
	HTML("html","text/html"),
	PNG("png","image/png"),
	JPEG("jpeg","image/jpeg");
	
	
	private String extension;
	private String contentType;
	
	private ContentTypeEnum(String extension,String contentType) {
		this.extension = extension;
		this.contentType = contentType;
	}
	
	public String getContentType() { return this.contentType;}
}
