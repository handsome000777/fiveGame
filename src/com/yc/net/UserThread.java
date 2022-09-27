package com.yc.net;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.yc.dao.DBHelper;

import fiveNet.Piece;

class UserThread implements Runnable {
	Socket socket = null;
	ObjectOutputStream oos;
	// ObjectInputStream ois;
	boolean loop = true;
	private boolean flag = true;// 标记 是否行动
	DataCenterThread dataThread;
	String name;
	int image=0;     //头像为第几张照片
	
	Integer roomNum = -1;
	int seat;

	public UserThread(Socket socket) {
		super();
		this.socket = socket;
		// this.dataThread = dataThread;
	}

	@Override
	public void run() {
		try {
			ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
			dataThread = new DataCenterThread(ois, socket);
			dataThread.start();

			// ois = new ObjectInputStream(socket.getInputStream()); // 接收
			oos = new ObjectOutputStream(socket.getOutputStream()); // 发送流
			while (loop) {

				Message message = null;
				while (loop) { // 这里循环 直到数据线程收到message消息 标志位修改
					if (dataThread.messageFlag == 1) {
						message = dataThread.message;
						dataThread.messageFlag = 0;
						break;
					}

				}
				if (loop == false) {
					return;
				}
				// message = (Message) ois.readObject(); // 接收
				switch (message.getRequest()) {
				case "注册": {
					String uname = message.getName();
					String pwd = message.getPassword();
					String sql = "insert into five(cname,pwd) values( ?,?) ";
					DBHelper db = new DBHelper();

					int result = db.doUpdete(sql, uname, pwd);

					if (result > 0) {
						Message send_message = new Message();
						send_message.setName(uname);
						send_message.setRequest("注册成功");
						oos.writeObject(send_message);
					} else {
						Message send_message = new Message();
						send_message.setRequest("注册失败");
						oos.writeObject(send_message);
					}
					break;
				}
				case "登录": {
					name = message.getName();
					String pwd = message.getPassword();
					String sql = "select *from five where cname=? and pwd=?";
					DBHelper db = new DBHelper();
					List<Map<String, Object>> list = db.select(sql, name, pwd);
					if (list != null && list.size() > 0) {
						Message send_message = new Message();
						send_message.setName(name);
						send_message.setRequest("登录成功");
						send_message.setText( Integer.toString(Constans.seatTable)   );        //这里TEXT发送座位表
						oos.writeObject(send_message);
					} else {
						Message send_message = new Message();
						send_message.setRequest("登录失败");
						oos.writeObject(send_message);
					}
					break;
				}
				case "大厅聊天": {
					new Thread(new MassSendText(name, message.getText(), socket)).start();
					break;
				}
				case "双人聊天": {
					new Thread(new SendText(name, roomNum, message.getText(), socket)).start();
					break;
				}
				case "请求坐下": {
					// 大厅线程
					image = Integer.parseInt(message.getText() ) ;
					seat = message.getSeat();
					roomNum = (seat - 1) / 2;
					Constans.seatTable = Constans.seatTable | 0b1 << seat - 1;
					new Thread(new MassSend(seat, socket)).start();
					new Thread(new GameThread(socket, oos, dataThread, Constans.RoomListList.get(roomNum),name,image  )).start();
					break;
				}
				case "退出房间": {
					// 大厅线程
					// 删除GameThread
					for (int j = 0; j < Constans.RoomListList.get(roomNum).size(); j++) {
						GameThread my = Constans.RoomListList.get(roomNum).get(j);
						if (my.socket == socket) { // 删自己
							// my.oos.writeObject(new Message(0,0,0,"异常退出"));
							Constans.RoomListList.get(roomNum).remove(my);
							my.loop = false; // 关闭线程
							if (Constans.RoomListList.get(roomNum).size() == 1) { // 通知另一方
								Constans.RoomListList.get(roomNum).get(0).oos.writeObject(new Piece(0, 0, 0, "对方掉线"));
							}
						}
					}
					// 通知其他人
					new Thread(new MassSend(seat, socket, "退房转发")).start();

					Constans.seatTable = Constans.seatTable & ~0b1 << seat - 1;
					seat = 0;
					roomNum = -1;

				}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception

			e.printStackTrace();
		}
	}
}

class MassSend implements Runnable { // 群发线程 坐下时发送座位
	private Socket socket = null; //
	int seat;
	String string; // 转发要求 默认进房

	public MassSend(int seat, Socket socket) {
		super();
		this.seat = seat;
		this.socket = socket;
	}

	public MassSend(int seat, Socket socket, String string) {
		super();
		this.seat = seat;
		this.socket = socket;
		this.string = string;
	}

	@Override
	public void run() { // 接收 然后 传送

		UserThread otherSide = null;
		try {
			Message message = new Message();
			message.setSeat(seat);

			for (int i = 0; i < Server.userList.size(); i++) {
				otherSide = Server.userList.get(i);
				if ("退房转发".equals(string)) {
					message.setRequest("有人退房");
				} else {
					message.setRequest("有人坐下");
					if (otherSide.socket == socket) {
						message.setRequest("成功坐下");
					}
				}

				otherSide.oos.writeObject(message);
				

			}
		} catch (SocketException e) { // 接收方掉线
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}

class MassSendText implements Runnable { // 群发线程 大厅聊天
	private Socket socket = null; //
	String text;
	String name;

	public MassSendText(String name, String text, Socket socket) {
		super();
		this.text = text;
		this.socket = socket;
		this.name = name;
	}

	@Override
	public void run() { // 接收 然后 传送

		UserThread otherSide = null;
		try {
			Message message = new Message();
			message.setName(name);
			message.setRequest("有人发言");
			message.setText(text);

			for (int i = 0; i < Server.userList.size(); i++) {
				otherSide = Server.userList.get(i);
				otherSide.oos.writeObject(message);

			}
		} catch (SocketException e) { // 接收方掉线
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}

class SendText implements Runnable { // 转发线程 发送双人聊天
	private Socket socket = null; //
	String text;
	String name;
	Integer roomNum;

	public SendText(String name, Integer roomNum, String text, Socket socket) {
		super();
		this.text = text;
		this.socket = socket;
		this.name = name;
		this.roomNum = roomNum;
	}

	@Override
	public void run() { // 接收 然后 传送

		GameThread otherSide = null;
		try {
			Message message = new Message();
			message.setName(name);
			message.setRequest("对方发言");
			message.setText(text);

			for (int i = 0; i < Constans.RoomListList.get(roomNum).size(); i++) {
				otherSide = Constans.RoomListList.get(roomNum).get(i);
				otherSide.oos.writeObject(message);
			}
		} catch (SocketException e) { // 接收方掉线
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
