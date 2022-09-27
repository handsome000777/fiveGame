package com.yc.net;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
public class HallThread  implements Runnable {
	private Socket socket = null;
	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	private boolean flag = true;// 标记 是否行动
	
	volatile int intoRoom =0 ;     // 
	public HallThread() {
		super();
	}
	
	@Override
	public void run() {
		try {
	          ois = new ObjectInputStream(socket.getInputStream());         //接收
	          oos = new ObjectOutputStream(socket.getOutputStream());			//发送流
	          
	          
			 while(true){		           							//大厅检测
				 
//				 if(Constans.roomList.size()==2) {
//					 
//				 }
//				 if(Constans.roomList.size()==2) {
//					 
//				 }
//				 if(Constans.roomList.size()==2) {
//	 
//				 }
//				 if(Constans.roomList.size()==2) {
//	 
//				 }	

				 
				 
			  Message message = (Message) ois.readObject(); //接收
			  switch (message.getRequest()) {
				case "进入房间": {
					break;
				}
			  }
			 
	       }
			}catch (IOException e) {
				// TODO: handle exception
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
	}
	

}
