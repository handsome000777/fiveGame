package com.yc.net;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import fiveNet.Piece;

public class Server {
	//static ArrayList<Socket> socketList = new ArrayList<>(); // 一个<socket数组>保存客户端
	static ArrayList<UserThread> userList = new ArrayList<>(); // 一个<线程数组>保存客户端处理的线程
	public static void main(String[] args) {
		ExecutorService es = Executors.newFixedThreadPool(8); //创建线程池(执行服务)  固定大小的线程池只支持八个线程，用来处理客户端
		try {
			ServerSocket server = new ServerSocket(10086); // 创建一个服务端接口server ServerSocket(10086) //监听10086端口
			//HallThread hall = new HallThread();  //大厅信息线程
			while (true) {
				// 接收客户端的Socket，如果没有客户端连接就一直卡在这里
				Socket socket = server.accept();                    //不断监听接收socket  取得连接 (同时创造反向soket)
				//socketList.add(socket);
				
				System.out.println(socket);
				// 每来一个用户就创建一个线程
				
//				ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
//				DataCenterThread dataThread =new DataCenterThread(  ois ,socket );
//				dataThread.setDaemon(true);
//				dataThread.start();
				UserThread user = new UserThread(socket );   //创建线程
				userList.add(user);
				// 开启线程
				es.execute(user);  //执行线程

				
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	//服务端接收方法

}