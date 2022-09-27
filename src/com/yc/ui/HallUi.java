package com.yc.ui;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;

import org.eclipse.swt.SWT;
import org.eclipse.wb.swt.SWTResourceManager;

import com.yc.net.Constans;
import com.yc.net.ConstansClient;
import com.yc.net.Message;
import com.yc.ui.LoginUi.UserListen;

import fiveNet.BoardMain;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Combo;

public class HallUi extends Composite {
	private Text text;
	private Text text_1;
	ArrayList<Button> buttonList = new ArrayList<>(); // 一个<按钮数组>
	static int seatTable;
	Thread t2 = new Thread(new HallListen(), "大厅监听器2"); // 监听大厅情况等 // 线程名
	BoardMain window;

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */
	public HallUi(Composite parent, int style) {
		super(parent, style);
		t2.setDaemon(true); // 设为守护线程
		t2.start();
		ConstansClient.client.setDaemon(true);
		ConstansClient.client.start();

		Composite composite = new Composite(this, SWT.NONE);
		composite.setBounds(23, 71, 44, 44);
		Random r = new Random();
		int index = r.nextInt(8) + 1;
		composite.setBackgroundImage(SWTResourceManager.getImage(HallUi.class, "/images/head" + index + ".png"  ));
		ConstansClient.headImg = "/images/head" + index + ".png";
		Label lblNewLabel_3 = new Label(this, SWT.NONE);
		lblNewLabel_3.setFont(SWTResourceManager.getFont("宋体", 10, SWT.BOLD));
		lblNewLabel_3.setBounds(74, 85, 49, 16);
		lblNewLabel_3.setText(ConstansClient.name);
		
		setFont(SWTResourceManager.getFont("Microsoft YaHei UI", 9, SWT.NORMAL));

		Label lblNewLabel = new Label(this, SWT.NONE);
		lblNewLabel.setForeground(SWTResourceManager.getColor(SWT.COLOR_DARK_CYAN));
		lblNewLabel.setFont(SWTResourceManager.getFont("隶书", 25, SWT.BOLD));
		lblNewLabel.setAlignment(SWT.CENTER);
		lblNewLabel.setBounds(168, 35, 160, 34);
		lblNewLabel.setText("游戏大厅");
		setBackgroundImage(SWTResourceManager.getImage(HallUi.class, "/images/hall.png"));

		Label lblNewLabel_1 = new Label(this, SWT.NONE);
		lblNewLabel_1.setForeground(SWTResourceManager.getColor(255, 255, 0));
		lblNewLabel_1.setBackground(SWTResourceManager.getColor(255, 222, 173));
		lblNewLabel_1.setAlignment(SWT.CENTER);
		lblNewLabel_1.setFont(SWTResourceManager.getFont("宋体", 22, SWT.NORMAL));
		lblNewLabel_1.setBounds(77, 135, 102, 44);
		lblNewLabel_1.setText("1号桌");

		Button btnNewButton2 = new Button(this, SWT.NONE);
		btnNewButton2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (ConstansClient.seat == 0) {
					Message message = new Message();
					message.setName(ConstansClient.name);
					message.setRequest("请求坐下");
					message.setSeat(2);
					ConstansClient.seat = 2;
					message.setText(   index+""   );
					try {
						ConstansClient.client.send(message);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}else {
					MessageBox mb = new MessageBox(getShell(),SWT.NONE);
					mb.setText("提示");
					mb.setMessage("不能进入多个房间！");
					mb.open();
				}
			}
		});
		btnNewButton2.setText("坐下");
		btnNewButton2.setBounds(137, 185, 61, 28);

		Button btnNewButton1 = new Button(this, SWT.NONE);
		btnNewButton1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (ConstansClient.seat == 0) {
					Message message = new Message();
					message.setName(ConstansClient.name);
					message.setRequest("请求坐下");
					message.setSeat(1);
					message.setText(   index+""   );
					ConstansClient.seat = 1;
					try {
						ConstansClient.client.send(message);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}else {
					MessageBox mb = new MessageBox(getShell(),SWT.NONE);
					mb.setText("提示");
					mb.setMessage("不能进入多个房间！");
					mb.open();
				}
			}
		});
		btnNewButton1.setBounds(52, 185, 61, 28);
		btnNewButton1.setText("坐下");

		Label lblNewLabel_2 = new Label(this, SWT.NONE);
		lblNewLabel_2.setFont(SWTResourceManager.getFont("Microsoft YaHei UI", 10, SWT.NORMAL));
		lblNewLabel_2.setAlignment(SWT.CENTER);
		lblNewLabel_2.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLUE));
		lblNewLabel_2.setBackground(SWTResourceManager.getColor(255, 239, 213));
		lblNewLabel_2.setBounds(52, 185, 61, 28);
		lblNewLabel_2.setText("有人");

		Label lblNewLabel_2_1 = new Label(this, SWT.NONE);
		lblNewLabel_2_1.setText("有人");
		lblNewLabel_2_1.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLUE));
		lblNewLabel_2_1.setFont(SWTResourceManager.getFont("Microsoft YaHei UI", 10, SWT.NORMAL));
		lblNewLabel_2_1.setBackground(SWTResourceManager.getColor(255, 239, 213));
		lblNewLabel_2_1.setAlignment(SWT.CENTER);
		lblNewLabel_2_1.setBounds(137, 185, 61, 28);

		Label lblNewLabel_1_1 = new Label(this, SWT.NONE);
		lblNewLabel_1_1.setText("2号桌");
		lblNewLabel_1_1.setForeground(SWTResourceManager.getColor(255, 255, 0));
		lblNewLabel_1_1.setFont(SWTResourceManager.getFont("宋体", 22, SWT.NORMAL));
		lblNewLabel_1_1.setBackground(SWTResourceManager.getColor(255, 222, 173));
		lblNewLabel_1_1.setAlignment(SWT.CENTER);
		lblNewLabel_1_1.setBounds(317, 135, 102, 44);

		Button btnNewButton4 = new Button(this, SWT.NONE);
		btnNewButton4.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (ConstansClient.seat == 0) {
					Message message = new Message();
					message.setName(ConstansClient.name);
					message.setRequest("请求坐下");
					message.setSeat(4);
					message.setText(   index+""   );
					ConstansClient.seat = 4;
					try {
						ConstansClient.client.send(message);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}else {
					MessageBox mb = new MessageBox(getShell(),SWT.NONE);
					mb.setText("提示");
					mb.setMessage("不能进入多个房间！");
					mb.open();
				}
			}
		});
		btnNewButton4.setText("坐下");
		btnNewButton4.setBounds(373, 185, 61, 28);

		Button btnNewButton3 = new Button(this, SWT.NONE);
		btnNewButton3.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (ConstansClient.seat == 0) {
					Message message = new Message();
					message.setName(ConstansClient.name);
					message.setRequest("请求坐下");
					message.setSeat(3);
					message.setText(   index+""   );
					ConstansClient.seat = 3;
					try {
						ConstansClient.client.send(message);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}else {
					MessageBox mb = new MessageBox(getShell(),SWT.NONE);
					mb.setText("提示");
					mb.setMessage("不能进入多个房间！");
					mb.open();
				}
			}
		});
		btnNewButton3.setText("坐下");
		btnNewButton3.setBounds(293, 185, 61, 28);

		Label lblNewLabel_2_2 = new Label(this, SWT.NONE);
		lblNewLabel_2_2.setText("有人");
		lblNewLabel_2_2.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLUE));
		lblNewLabel_2_2.setFont(SWTResourceManager.getFont("Microsoft YaHei UI", 10, SWT.NORMAL));
		lblNewLabel_2_2.setBackground(SWTResourceManager.getColor(255, 239, 213));
		lblNewLabel_2_2.setAlignment(SWT.CENTER);
		lblNewLabel_2_2.setBounds(293, 185, 61, 28);

		Label lblNewLabel_2_1_1 = new Label(this, SWT.NONE);
		lblNewLabel_2_1_1.setText("有人");
		lblNewLabel_2_1_1.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLUE));
		lblNewLabel_2_1_1.setFont(SWTResourceManager.getFont("Microsoft YaHei UI", 10, SWT.NORMAL));
		lblNewLabel_2_1_1.setBackground(SWTResourceManager.getColor(255, 239, 213));
		lblNewLabel_2_1_1.setAlignment(SWT.CENTER);
		lblNewLabel_2_1_1.setBounds(373, 185, 61, 28);

		Label lblNewLabel_1_2 = new Label(this, SWT.NONE);
		lblNewLabel_1_2.setText("3号桌");
		lblNewLabel_1_2.setForeground(SWTResourceManager.getColor(255, 255, 0));
		lblNewLabel_1_2.setFont(SWTResourceManager.getFont("宋体", 22, SWT.NORMAL));
		lblNewLabel_1_2.setBackground(SWTResourceManager.getColor(255, 222, 173));
		lblNewLabel_1_2.setAlignment(SWT.CENTER);
		lblNewLabel_1_2.setBounds(77, 260, 102, 44);

		Button btnNewButton5 = new Button(this, SWT.NONE);
		btnNewButton5.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (ConstansClient.seat == 0) {
					Message message = new Message();
					message.setName(ConstansClient.name);
					message.setRequest("请求坐下");
					message.setSeat(5);
					message.setText(   index+""   );
					ConstansClient.seat = 5;
					try {
						ConstansClient.client.send(message);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}else {
					MessageBox mb = new MessageBox(getShell(),SWT.NONE);
					mb.setText("提示");
					mb.setMessage("不能进入多个房间！");
					mb.open();
				}
			}
		});
		btnNewButton5.setText("坐下");
		btnNewButton5.setBounds(52, 310, 61, 28);

		Button btnNewButton6 = new Button(this, SWT.NONE);
		btnNewButton6.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (ConstansClient.seat == 0) {
					Message message = new Message();
					message.setName(ConstansClient.name);
					message.setRequest("请求坐下");
					message.setSeat(6);
					message.setText(   index+""   );
					ConstansClient.seat = 6;
					try {
						ConstansClient.client.send(message);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}else {
					MessageBox mb = new MessageBox(getShell(),SWT.NONE);
					mb.setText("提示");
					mb.setMessage("不能进入多个房间！");
					mb.open();
				}
			}
		});
		btnNewButton6.setText("坐下");
		btnNewButton6.setBounds(137, 310, 61, 28);

		Label lblNewLabel_2_3 = new Label(this, SWT.NONE);
		lblNewLabel_2_3.setText("有人");
		lblNewLabel_2_3.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLUE));
		lblNewLabel_2_3.setFont(SWTResourceManager.getFont("Microsoft YaHei UI", 10, SWT.NORMAL));
		lblNewLabel_2_3.setBackground(SWTResourceManager.getColor(255, 239, 213));
		lblNewLabel_2_3.setAlignment(SWT.CENTER);
		lblNewLabel_2_3.setBounds(52, 310, 61, 28);

		Label lblNewLabel_2_1_2 = new Label(this, SWT.NONE);
		lblNewLabel_2_1_2.setText("有人");
		lblNewLabel_2_1_2.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLUE));
		lblNewLabel_2_1_2.setFont(SWTResourceManager.getFont("Microsoft YaHei UI", 10, SWT.NORMAL));
		lblNewLabel_2_1_2.setBackground(SWTResourceManager.getColor(255, 239, 213));
		lblNewLabel_2_1_2.setAlignment(SWT.CENTER);
		lblNewLabel_2_1_2.setBounds(137, 310, 61, 28);

		Label lblNewLabel_1_3 = new Label(this, SWT.NONE);
		lblNewLabel_1_3.setText("4号桌");
		lblNewLabel_1_3.setForeground(SWTResourceManager.getColor(255, 255, 0));
		lblNewLabel_1_3.setFont(SWTResourceManager.getFont("宋体", 22, SWT.NORMAL));
		lblNewLabel_1_3.setBackground(SWTResourceManager.getColor(255, 222, 173));
		lblNewLabel_1_3.setAlignment(SWT.CENTER);
		lblNewLabel_1_3.setBounds(318, 260, 102, 44);

		Button btnNewButton8 = new Button(this, SWT.NONE);
		btnNewButton8.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (ConstansClient.seat == 0) {
					Message message = new Message();
					message.setName(ConstansClient.name);
					message.setRequest("请求坐下");
					message.setSeat(8);
					message.setText(   index+""   );
					ConstansClient.seat = 8;
					try {
						ConstansClient.client.send(message);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}else {
					MessageBox mb = new MessageBox(getShell(),SWT.NONE);
					mb.setText("提示");
					mb.setMessage("不能进入多个房间！");
					mb.open();
				}
			}
		});
		btnNewButton8.setText("坐下");
		btnNewButton8.setBounds(373, 310, 61, 28);

		Button btnNewButton7 = new Button(this, SWT.NONE);
		btnNewButton7.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (ConstansClient.seat == 0) {
					Message message = new Message();
					message.setName(ConstansClient.name);
					message.setRequest("请求坐下");
					message.setSeat(7);
					message.setText(   index+""   );
					ConstansClient.seat = 7;
					try {
						ConstansClient.client.send(message);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}else {
					MessageBox mb = new MessageBox(getShell(),SWT.NONE);
					mb.setText("提示");
					mb.setMessage("不能进入多个房间！");
					mb.open();
				}
			}
		});
		btnNewButton7.setText("坐下");
		btnNewButton7.setBounds(293, 310, 61, 28);

		Label lblNewLabel_2_4 = new Label(this, SWT.NONE);
		lblNewLabel_2_4.setText("有人");
		lblNewLabel_2_4.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLUE));
		lblNewLabel_2_4.setFont(SWTResourceManager.getFont("Microsoft YaHei UI", 10, SWT.NORMAL));
		lblNewLabel_2_4.setBackground(SWTResourceManager.getColor(255, 239, 213));
		lblNewLabel_2_4.setAlignment(SWT.CENTER);
		lblNewLabel_2_4.setBounds(293, 310, 61, 28);

		Label lblNewLabel_2_1_3 = new Label(this, SWT.NONE);
		lblNewLabel_2_1_3.setText("有人");
		lblNewLabel_2_1_3.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLUE));
		lblNewLabel_2_1_3.setFont(SWTResourceManager.getFont("Microsoft YaHei UI", 10, SWT.NORMAL));
		lblNewLabel_2_1_3.setBackground(SWTResourceManager.getColor(255, 239, 213));
		lblNewLabel_2_1_3.setAlignment(SWT.CENTER);
		lblNewLabel_2_1_3.setBounds(373, 310, 61, 28);

		text = new Text(this, SWT.BORDER | SWT.READ_ONLY | SWT.WRAP | SWT.V_SCROLL);
		text.setFont(SWTResourceManager.getFont("Microsoft YaHei UI", 10, SWT.NORMAL));
		text.setBounds(72, 374, 351, 96);

		text_1 = new Text(this, SWT.BORDER);
		text_1.setBounds(72, 488, 270, 28);

		Button btnNewButton_5 = new Button(this, SWT.NONE);
		btnNewButton_5.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (!"".equals(text_1.getText()) && text_1.getText() != null) {
					Message message = new Message();
					message.setRequest("大厅聊天");
					message.setText(text_1.getText());
					;
					try {
						ConstansClient.client.send(message);
						text_1.setText("");
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		btnNewButton_5.setBounds(358, 488, 61, 28);
		btnNewButton_5.setText("发送");

		Combo combo = new Combo(this, SWT.NONE);
		combo.setFont(SWTResourceManager.getFont("Microsoft YaHei UI", 8, SWT.NORMAL));
		combo.setBounds(377, 85, 72, 18);
		combo.setText("  \u5728\u7EBF\u73A9\u5BB6");
		combo.select(0);



		buttonList.add(btnNewButton1);
		buttonList.add(btnNewButton2);
		buttonList.add(btnNewButton3);
		buttonList.add(btnNewButton4);
		buttonList.add(btnNewButton5);
		buttonList.add(btnNewButton6);
		buttonList.add(btnNewButton7);
		buttonList.add(btnNewButton8);

		Button btnNewButton = new Button(this, SWT.NONE);
		btnNewButton.setFont(SWTResourceManager.getFont("Microsoft YaHei UI", 8, SWT.NORMAL));
		btnNewButton.setBounds(18, 535, 49, 21);
		btnNewButton.setText("<-\u8FD4\u56DE");
		for(int i =0 ; i<8 ;i++) {
			if((seatTable &0b1 <<i )   !=0 ) {      
				buttonList.get(i).setVisible(false);
			}
		}
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	class HallListen implements Runnable {
		boolean flag = true;

		@Override
		public void run() {
			// ConstansClient.client.start();
			// ConstansClient.client.setDaemon(true);
			while (flag) {
				if (ConstansClient.client != null) {
					try {
						// Message accept_message = (Message)ConstansClient.client.accept();
						Message accept_message = null;
						while (true) { // 这里循环 直到数据线程收到message消息 标志位修改
							if (ConstansClient.messageFlag == 1) {
								accept_message = ConstansClient.client.message;
								ConstansClient.messageFlag = 0;
								break;
							}
						}
						String accept_name = accept_message.getName();
						String accept_Text = accept_message.getText();
						String accept_Request = accept_message.getRequest();
						int seat = accept_message.getSeat();
						switch (accept_Request) {
						case "成功坐下": {
							Display.getDefault().asyncExec(() -> {
								buttonList.get(seat - 1).setVisible(false);
								window = new BoardMain();
								window.open();                   
							});

							break;
						}
						case "有人坐下": {
							Display.getDefault().asyncExec(() -> {
								buttonList.get(seat - 1).setVisible(false);
							});
							break;
						}
						case "对手信息": { // 进入游戏时 双人
							String opName = accept_message.getName();
							int opImage=Integer.parseInt( accept_message.getText()  )  ;
							Display.getDefault().asyncExec(() -> {
								window.opLabel.setText(opName);
								window.composite_2.setBackgroundImage(SWTResourceManager.getImage(BoardMain.class, "/images/head" + opImage + ".png"));
								window.ready=0;
							});
							break;
						}
						case "有人退房": {
							Display.getDefault().asyncExec(() -> {
								buttonList.get(seat - 1).setVisible(true);
							});
							break;
						}
						case "有人发言": { // 大厅中 所有人
							Display.getDefault().asyncExec(() -> {
								text.insert(accept_name + " : " + accept_Text + "\r\n");
							});
							break;
						}
						case "对方发言": { // 进入游戏时 双人
							Display.getDefault().asyncExec(() -> {
								window.text_1.insert(accept_name + " : " + accept_Text + "\r\n");
							});
							break;
						}

						}

					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
				}
			}
		}
	}

}
