package com.ramune;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


import com.ramune.util.Controller;
import com.ramune.util.ServerLogger;

public class ServerMain {
	public static void main(String[] args){
		
		int port = getPortFromArgs(args);
		
		ServerLogger.log("Server starting : port=" + port);
		
		while(true) {
			// Socket open
			try(
				ServerSocket serverSocket = new ServerSocket(port);
		  		Socket socket = serverSocket.accept();
			){	
				
				Controller.handle(socket);
			} catch (IOException e) {
				ServerLogger.warn("起動時にエラーが発生しました",e);
				break;
			} catch (Exception e) {
				ServerLogger.warn("システム障害が発生しました",e);
			}
		}
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
