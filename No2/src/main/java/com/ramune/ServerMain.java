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
import com.ramune.util.ServerLogger;

public class ServerMain {
	public static void main(String[] args){
		
		int port = getPortFromArgs(args);
		
		ServerLogger.log("Server starting : port=" + port);
		
		while(true) {
			try(
				ServerSocket serverSocket = new ServerSocket(port);
		  		Socket socket = serverSocket.accept();
		  		BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		  		OutputStream outputStream = socket.getOutputStream();
			){	
				HttpRequest request = new HttpRequest(reader);
	  		
				ServerLogger.log("Received Request");
				ServerLogger.log(request.getRequestMethod() + " " + request.getRequestPath());
		  		
		  		HttpResponse response = createResponse(request);
		  		ServerLogger.log("Response " + response.getStatusCode() +" " + response.getStatus());
		  		outputStream.write(response.getResponse());
		  		outputStream.flush();
			} catch (IOException | InvalidAttributeValueException e) {
				ServerLogger.warn(e.toString());
				for(StackTraceElement element : e.getStackTrace()) {
					ServerLogger.warn(element.toString());
				}
				break;
			}
		}
	}
	
	/**
	 * Create Http Response from Request
	 * @param request Http Request
	 * @return Http Response
	 * @throws IOException
	 */
	private static HttpResponse createResponse(HttpRequest request) throws IOException {
		String requestPath = request.getRequestPath();
		String requestDirPath = System.getProperty("user.dir") + requestPath;
		ServerLogger.debug("Request dir : " + requestDirPath);
		HttpResponse response = new HttpResponse();
		if(!request.getRequestMethod().equals("GET")) {
			response.setStatus(HttpStatusEnum.MethodNotAllowed);
			return response;
		}
		
		// /pingへアクセスされた時の処理
		if(requestPath.equals("/ping")) {
			response.setContentType("text/plain");
			response.setBody("pong".getBytes(StandardCharsets.UTF_8));
			return response;
		}
		
		// /ping以外へアクセスされた時の処理
		try(FileInputStream inputStream = new FileInputStream(new File(requestDirPath))){
			String fileName = new File(requestDirPath).getName();
			String extension = fileName.substring(fileName.lastIndexOf(".")+1);
			response.setContentType(ContentTypeEnum.valueOf(extension.toUpperCase()).getContentType());
			response.setBody(inputStream.readAllBytes());			
		}catch(FileNotFoundException e) {
			ServerLogger.log("存在しないファイルへのリクエストのため404を返却します");
			response.setStatus(HttpStatusEnum.NotFound);
			return response;
		}catch(IllegalArgumentException e) {
			ServerLogger.log("許可されないファイル拡張子のため404を返却します");
			response.setStatus(HttpStatusEnum.NotFound);
			return response;
		}
		
		return response;
	}

	/**
	 * get port from runtime args
	 * @param args
	 * @return port(default 18080)
	 */
	private static int getPortFromArgs(String[] args) {
		int port = 18080;
		
		try{
			if(args.length == 0) {
				ServerLogger.log("Port指定なし");
				return port;
			}else if(args.length > 0){
				port = Integer.parseInt(args[0]);
				ServerLogger.log("指定Portで起動します");
				return port;
			}else {
				throw new NumberFormatException();
			}
		}catch(NumberFormatException e) {
			ServerLogger.log("実行引数が不正です:" + args[0]);
			ServerLogger.log("デフォルトポートで起動します");
			return port;
		}
	}
	
}
