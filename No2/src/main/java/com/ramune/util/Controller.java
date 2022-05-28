package com.ramune.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;


import com.ramune.dto.HttpRequest;
import com.ramune.dto.HttpResponse;

public class Controller {
	
	/**
	 * Process request-response
	 * @param socket accepted server socket
	 */
	public static void handle(Socket socket) {
		
		// request受信処理(response用にtry-with-resourceにしない)
		HttpRequest request;
		BufferedReader reader;
		try{
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			request = HttpRequest.readRequest(reader);
		} catch (IOException e) {
			ServerLogger.warn("正常にリクエストを受信できませんでした", e);
			return;
		}

		// requestがnullの時はRequestが正常に読み取れなかった時なので、処理を終了する
		if (request == null) {
			return;
		}
		
		ServerLogger.log("Received Request");
		ServerLogger.log(request.getRequestMethod() + " " + request.getRequestPath());
  		
		// response処理
  		HttpResponse response = createResponse(request);
  		ServerLogger.log("Response " + response.getStatusCode() +" " + response.getStatus());
		try(OutputStream outputStream = socket.getOutputStream()){			
			outputStream.write(response.getResponse());
			outputStream.flush();
		} catch (IOException e) {
			ServerLogger.warn("レスポンスを返却できませんでした", e);
		}
	}

	/**
	 * Create Http Response from Request
	 * @param request Http Request
	 * @return Http Response
	 * @throws IOException
	 */
	private static HttpResponse createResponse(HttpRequest request){
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
		} catch (IOException e) {
			ServerLogger.warn("ファイル読み込み時にエラー");
			response.setStatus(HttpStatusEnum.InternalServerError);
			// ファイル読み込み前にcontent-typeを変更しているためtext/plainに戻す。
			response.setContentType("text/plain");
		}
		
		return response;
	}
}
