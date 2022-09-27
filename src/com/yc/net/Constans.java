package com.yc.net;

import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Constans {       //服务端常量
	//public static String  name = "";     //用户名
	
	public static ArrayList<GameThread> roomList0 = new ArrayList<>(); // 存放两人socket 数组  （表示一个房间）
	public static ArrayList<GameThread> roomList1 = new ArrayList<>(); // 
	public static ArrayList<GameThread> roomList2 = new ArrayList<>(); // 
	public static ArrayList<GameThread> roomList3 = new ArrayList<>(); // 
	
	public static List<ArrayList<GameThread>> RoomListList =    Arrays.asList(roomList0, roomList1,roomList2,roomList3);
	//public static List RoomListList2 = new ArrayList(RoomListList);
	
//	public static volatile int messageFlag=0;            //控制接收 为1时接收    只能一个线程接收 
//	public static volatile int pieceFlag=0;
	public static int seatTable =0; // 
}
