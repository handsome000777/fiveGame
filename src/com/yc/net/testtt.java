package com.yc.net;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import fiveNet.BoardMain;

public class testtt {
	public static ArrayList<GameThread> roomList = new ArrayList<>(); // 存放两人socket 数组  （表示一个房间）
	public static ArrayList<GameThread> roomList2 = new ArrayList<>(); // 
	public static ArrayList<GameThread> roomList3 = new ArrayList<>(); // 
	public static ArrayList<GameThread> roomList4 = new ArrayList<>(); // 
	
	public static List<ArrayList<GameThread>> arrays =    Arrays.asList(roomList, roomList2,roomList3,roomList4)     ;
	public static List RoomListList = new ArrayList(arrays);
	
	//ArrayList<String> list = new ArrayList<String>(Arrays.asList("o1", "o2"));
	public static void main(String[] args) {
//		if(roomList!=null) {
//			System.out.println(arrays.size());
//			System.out.println(arrays.get(0));
//			System.out.println(RoomListList.size());
//		}
		System.out.println(0/2);
	}

}
