package com.ramune;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;


import javax.management.InvalidAttributeValueException;


import com.ramune.dto.HttpRequest;
import com.ramune.dto.HttpResponse;
import com.ramune.util.ContentTypeEnum;
import com.ramune.util.HttpStatusEnum;


import jdk.jfr.ContentType;

public class ServerMain {
	/**
	 * 
	 * @param args 0:port 1-:ignore
	 * @throws IOException
	 * @throws InvalidAttributeValueException
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws IOException, InvalidAttributeValueException, InterruptedException{
		
		int port = 18080;
		
		while(true) {
			try(
				ServerSocket serverSocket = new ServerSocket(port);
		  		Socket socket = serverSocket.accept();
		  		BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		  		OutputStream outputStream = socket.getOutputStream();
			){	
				HttpRequest request = new HttpRequest(reader);
	  		
		  		System.out.println("-- Reqeust --");
		  		System.out.println("request method : " + request.getRequestMethod());
		  		System.out.println("request Path : " + request.getRequestPath());
		  
		  		System.out.println("-- body start --");
		  		System.out.println(request.getBody());
		  		System.out.println("-- body end --");
		  		
		  		HttpResponse response = handle(request);
		  		//System.out.println(new String(response.getResponse()));
		  		outputStream.write(response.getResponse());
		  		outputStream.flush();
			} catch (IOException | InvalidAttributeValueException e) {
				throw e;
			}
		}
	}
	
	private static HttpResponse handle(HttpRequest request) throws IOException {
		String requestPath = request.getRequestPath();
		String requestDirPath = System.getProperty("user.dir") + requestPath;
		System.out.println("Request path : " + requestPath);
		System.out.println("Request dir : " + requestDirPath);
		HttpResponse response = new HttpResponse();
		if(!request.getRequestMethod().equals("GET")) {
			response.setStatus(HttpStatusEnum.MethodNotAllowed);
			return response;
		}
		
		if(requestPath.equals("/ping")) {
			response.setContentType("text/plain");
			response.setBody("pong".getBytes(StandardCharsets.UTF_8));
			return response;
		}
		
		try(FileInputStream inputStream = new FileInputStream(new File(requestDirPath))){
			String fileName = new File(requestDirPath).getName();
			String extension = fileName.substring(fileName.lastIndexOf(".")+1);
			response.setContentType(ContentTypeEnum.valueOf(extension.toUpperCase()).getContentType());
			response.setBody(inputStream.readAllBytes());			
		}catch(FileNotFoundException e) {
			e.printStackTrace();
			response.setStatus(HttpStatusEnum.NotFound);
			return response;
		}
		
		return response;
	}

}
