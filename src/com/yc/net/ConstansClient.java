package com.yc.net;

import java.net.Socket;
import java.util.ArrayList;

public class ConstansClient {       //服务端常量
	public static String name = "";
	public static String headImg = "";
	
	public static String opponentName = null;    //对手信息
	public static String opponentHeadImg =null;
	public static int seat = 0;
	public static Client   client   = null;  //todo
	public static volatile int messageFlag=0;
	public static volatile int pieceFlag=0;
	
}
