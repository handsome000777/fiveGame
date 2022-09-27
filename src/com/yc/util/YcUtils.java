package com.yc.util;

import java.security.MessageDigest;

public class YcUtils {

static char[] hex = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'}; 
	
	public static String transferSize(long size) {
		if(size/1024/1024/1024/1024>0) {
			return size/1024/1024/1024/1024+"T";
		}else if(size/1024/1024/1024>0) {
			return size/1024/1024/1024+"G";
		}else if(size/1024/1024>0) {
			return size/1024/1024+"M";
		}else if(size/1024>0) {
			return size/1024+"K";
		}else {
			return size+"B";
		}
	
	}


	public static void main(String[] ars) {
		//YcUtils.encrypt2ToMD5( YcUtils.encrypt2ToMD5("a") ) //加密两次
		String s=YcUtils.encrypt2ToMD5( "a" ); //加密一次
		System.out.println(  "a"+  "\t"+ s );
		String s2=YcUtils.encrypt2ToMD5("b");
		System.out.println(  "b"+"\t"+s2);
	}

	/**
	 * 给一个字符串   str , 通过这个ＭＤ５方法转成３２位的１６进制数
	 * @param str
	 * @return
	 */
	public static String encrypt2ToMD5(String str) {
		// 加密后的16进制字符串
		String hexStr = "";
		try {
			// 此 MessageDigest 类为应用程序提供信息摘要算法的功能
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			// 转换为MD5码
			byte[] digest = md5.digest(str.getBytes("utf-8"));
			hexStr = byte2str(digest);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return hexStr;
	}
	
	private static String byte2str(byte []bytes){
	    int len = bytes.length;   
	    StringBuffer result = new StringBuffer();    
	    for (int i = 0; i < len; i++) {   
	        byte byte0 = bytes[i];   
	        result.append(hex[byte0 >>> 4 & 0xf]);   
	        result.append(hex[byte0 & 0xf]);   
	    }
	    return result.toString();
	}

	
	
}
