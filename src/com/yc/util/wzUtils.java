package com.yc.util;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

public class wzUtils {
	
	/*
	 * 窗口居中
	 * 
	 */
	//屏幕宽高
	public static void center(Shell shell){
		//屏幕宽高
		int screenWidth=Display.getCurrent().getBounds().width;
		int screenHeight=Display.getCurrent().getBounds().height;
		//窗口宽高
		int shellWidth=shell.getSize().x;
		int shellHeight=shell.getSize().y;
		//计算窗口左上角坐标
		int x=(screenWidth-shellWidth)/2;
		int y=(screenHeight-shellHeight)/2;
		shell.setLocation(x, y);
	}
	
	/**
	 * 提示框
	 * @param shell
	 * @param title
	 * @param messge
	 */
	public static void showMessage(Shell shell, String title, String messge) {
		// 弹框提示
		MessageBox mb = new MessageBox(shell, SWT.NONE);
		mb.setText(title);
		mb.setMessage(messge);
		mb.open();
	}
	
	public static void showMessage2(Shell shell, String title, String messge) {
		// 弹框提示
		MessageBox mb = new MessageBox(shell, SWT.YES|SWT.NO );
		mb.setText(title);
		mb.setMessage(messge);
		mb.open();
	}

}
