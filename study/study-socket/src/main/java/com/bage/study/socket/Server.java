package com.bage.study.socket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 服务端，负责接收请求
 * @author luruihua
 *
 */
public class Server {

	private int port = 8080;
	private ServerSocket server;
	
	public static void main(String[] args) {
		
		Server Server = new Server(8080);
		Server.start();
		
	}

	public Server() {
		super();
	}
	public Server(int port) {
		super();
		this.port = port;
	}

	public void start() {
		try {
			if(server == null) {
				server =  new ServerSocket(port);
			}
            while (true) {
                // 等待client的请求
                System.out.println("等待client的请求...");
                Socket socket = server.accept();
                // 接收客户端的数据
                DataInputStream in = new DataInputStream(socket.getInputStream());
                String string = in.readUTF();
                System.out.println(" 接收客户端的数据:" + string);
                // 发送给客户端数据
                DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                System.out.println(" 发送给客户端数据:" + string);
                out.writeUTF(string);
                socket.close();
            }
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}
}
