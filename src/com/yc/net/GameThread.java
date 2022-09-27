package com.yc.net;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

import fiveNet.Piece;

public class GameThread implements Runnable {

	boolean loop = true;
	Socket socket = null;
	private ArrayList<GameThread> list; // 客户端线程集合
	ObjectOutputStream oos;
	// private ObjectInputStream ois;
	DataCenterThread dataThread;
	private boolean flag = true;// 标记 是否行动
	int i;
	String name;
	int image;
	public GameThread(Socket socket, ObjectOutputStream oos, DataCenterThread dataThread,
			ArrayList<GameThread> roomList, String name, int image) {
		super();
		this.socket = socket;
		this.oos = oos;
		this.dataThread = dataThread;
		this.name = name;
		this.image = image;
		this.list = roomList;
		roomList.add(this); // 把当前线程加入list中
	}

	@Override
	public void run() { // 接收 然后 传送
		GameThread otherSide = null;
		for (i = 0; i < list.size(); i++) {
			otherSide = list.get(i);
		}

		try {
			
			Message message = new Message();                   //向房间的人发送自己的姓名头像
			message.setRequest("对手信息");
			message.setName(name);
			message.setText( image +"");
			int size = list.size();
			for (i = 0; i < size; i++) {
				otherSide = list.get(i);
				if (otherSide.socket != socket) { 			//给对手发自己信息
					otherSide.oos.writeObject(message);
					message.setName(otherSide.name);					//顺便保存对手信息
					message.setText(otherSide.image+"");
				}else if(size==2){										//将对手信息发自己
					try {
						Thread.sleep(590);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					otherSide.oos.writeObject(message);
				}
			}
			
			
			// ois = new ObjectInputStream(socket.getInputStream()); //接收
			// oos = new ObjectOutputStream(socket.getOutputStream()); //发送流
			while (loop) { // 客户端发给服务端的消息，把他写到其它套接字中去
				// Piece piece = (Piece) ois.readObject(); //接收
				Piece piece = null;
				while (loop) { // 这里循环 直到数据线程收到message消息 标志位修改
					if (dataThread.pieceFlag == 1) {
						piece = dataThread.piece;
						dataThread.pieceFlag = 0;
						break;
					}
				}

				size = list.size();
				for (i = 0; i < size; i++) {
					otherSide = list.get(i);
					if (otherSide.socket != socket) { // 调另一个线程发送 user对方
						otherSide.oos.writeObject(piece);
					}
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
