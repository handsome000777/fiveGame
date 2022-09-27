package com.yc.net;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.SocketException;

import fiveNet.Piece;

public class DataCenterThread extends Thread {
	ObjectInputStream ois;
	Socket socket;
	boolean loop =true;
	Object obj = null;
	Message message = null;
	Piece piece = null;
	Integer flag = 0;
	public volatile int messageFlag = 0; // 控制接收 为1时接收 只能一个线程接收
	public volatile int pieceFlag = 0;

	public Message getMessage() {
		return message;
	}

	public Piece getPiece() {
		return piece;
	}

	public DataCenterThread(ObjectInputStream ois, Socket socket) {
		super();
		this.ois = ois;
		this.socket = socket;
	}

	@Override
	public void run() {

		while (loop) { // 这里循环 直到标志位修改 从接收修改？
			try {
				flag = 0;
				obj = ServerAccept(ois);
				if (flag == 1) {
					message = (Message) obj;
					messageFlag = 1;
				} else if (flag == 2) {
					piece = (Piece) obj;
					pieceFlag = 1;
				}

			} catch (ClassNotFoundException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public Object ServerAccept(ObjectInputStream ois) throws IOException, ClassNotFoundException {
		Message message = null;
		Piece piece = null;
		try {
			Object obj = ois.readObject();
			if (obj instanceof Message) {
				message = (Message) obj;
				flag = 1;
				return message;

			}
			if (obj instanceof Piece) {
				piece = (Piece) obj;
				flag = 2;
				return piece;
			}
		} catch (SocketException e) {   //用户断开连接 就删除相应的服务端处理线程
			for(int i =0; i<Constans.RoomListList.size(); i++) {         //删除GameThread
				for(int j = 0 ; j <Constans.RoomListList.get(i).size() ;j++) {
					GameThread my = Constans.RoomListList.get(i).get(j);
					if (my.socket == socket) { // 删自己
						System.out.println("处理用户游戏的服务线程  房间大小为 " + Constans.RoomListList.get(i).size());
						//my.oos.writeObject(new Message(0,0,0,"异常退出"));
						Constans.RoomListList.get(i).remove(my);
						my.loop=false;
						System.out.println("已经删除了一个处理用户游戏的服务线程  房间大小为 " + Constans.RoomListList.get(i).size());
						if(Constans.RoomListList.get(i).size()==1) {
							Constans.RoomListList.get(i).get(0).oos.writeObject(new Piece(0,0,0,"对方掉线"));
						}
					}
				}
			}
			
			for (int i = 0; i < Server.userList.size(); i++) {           //删除UserThread
				UserThread my = Server.userList.get(i);
				if (my.socket == socket) { // 删自己
					Server.userList.remove(my);
					my.loop=false;
					System.out.println("已经删除了一个处理用户的服务线程  userlist大小为 " + Server.userList.size());
				}
			}
			//e.printStackTrace();
			loop=false;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return obj;
	}
}
