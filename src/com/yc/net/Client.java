package com.yc.net;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import fiveNet.Piece;

public class Client extends Thread {
	public Message message = null;
	public Piece piece = null;
	Object obj = null;
	private Socket socket;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	Integer flag = 0;
	boolean loop = true; // 循环标志

	public Client(Socket socket, ObjectInputStream ois, ObjectOutputStream oos) {
		super();
		this.socket = socket;
		this.ois = ois;
		this.oos = oos;
	}

	@Override
	public void run() {
		while (loop) { // 这里循环 直到标志位修改 从接收修改？
			try {
				flag = 0;
				obj = ServerAccept(ois);
				if (flag == 1) {
					message = (Message) obj;
//					if ("对方发言".equals(message.getRequest()) ) {
//						System.out.println("客户端收到双人==");
//						ConstansClient.messageFlag = 2;
//					} else {
						ConstansClient.messageFlag = 1;
//					}
				} else if (flag == 2) {
					piece = (Piece) obj;
					ConstansClient.pieceFlag = 1;
				}

			} catch (ClassNotFoundException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				loop = false;
				return;
			}
		}

	}

	public void send(Message message) throws IOException {
		oos.writeObject(message);

	}

	public void send(Piece piece) throws IOException {
		oos.writeObject(piece);

	}

	public Object accept() throws IOException, ClassNotFoundException {
		Object obj = ois.readObject();
		try {
			if (obj instanceof Message) {
				message = (Message) obj;
				return message;
			}
			if (obj instanceof Piece) {
				piece = (Piece) obj;
				return piece;
			}
			// message = (Message) ois.readObject();
		} catch (Exception e) {
			return null;
		}
		return obj;
	}

	public Object ServerAccept(ObjectInputStream ois) throws IOException, ClassNotFoundException {
		Message message = null;
		Piece piece = null;
		Object obj = ois.readObject();
		try {
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
		} catch (Exception e) {
			return null;
		}
		return obj;
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public ObjectInputStream getOis() {
		return ois;
	}

	public void setOis(ObjectInputStream ois) {
		this.ois = ois;
	}

	public ObjectOutputStream getOos() {
		return oos;
	}

	public void setOos(ObjectOutputStream oos) {
		this.oos = oos;
	}
}
