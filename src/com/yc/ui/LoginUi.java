package com.yc.ui;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import com.yc.net.Client;
import com.yc.net.Constans;
import com.yc.net.ConstansClient;
import com.yc.net.Message;

public class LoginUi extends Composite {
	private Text text;
	private Text text_1;

	
	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 * @throws UnknownHostException
	 */
	public LoginUi(Composite parent, int style) throws UnknownHostException {
		super(parent, style);

		InetAddress ip = null;
		try {
			ip = InetAddress.getLocalHost();
		} catch (UnknownHostException e2) {
			e2.printStackTrace();
		}
		InputDialog id = new InputDialog(getShell(), "连接客户端", "输入客户端ip地址", ip.getHostAddress(), null);
		id.open();
		Socket socket;

		try {
			socket = new Socket(id.getValue(), 10086);
			ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
			ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
			ConstansClient.client = new Client(socket, ois, oos); // 创建客户端
			Thread t1 = new Thread(new UserListen(), "客户端监听器1"); //监听登录注册等   // 线程名
			t1.setDaemon(true);   //设为守护线程
			t1.start();
		} catch (UnknownHostException e2) {
			e2.printStackTrace();
		} catch (IOException e2) {
			e2.printStackTrace();
		} // 与服务端的地址和端口号一致

		Label label = new Label(this, SWT.NONE);
		label.setText("账号：");
		label.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		label.setFont(SWTResourceManager.getFont("隶书", 13, SWT.NORMAL));
		label.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_BACKGROUND_GRADIENT));
		label.setBounds(106, 264, 76, 26);

		text = new Text(this, SWT.BORDER);
		text.setText("小张");
		text.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_BACKGROUND_GRADIENT));
		text.setBounds(213, 264, 178, 26);

		text_1 = new Text(this, SWT.BORDER | SWT.PASSWORD);
		text_1.setText("1");
		text_1.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_BACKGROUND_GRADIENT));
		text_1.setBounds(213, 323, 178, 26);

		Label label_1 = new Label(this, SWT.NONE);
		label_1.setText("密码：");
		label_1.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		label_1.setFont(SWTResourceManager.getFont("隶书", 13, SWT.NORMAL));
		label_1.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_BACKGROUND_GRADIENT));
		label_1.setBounds(106, 323, 76, 26);

		Button button = new Button(this, SWT.NONE);
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) { // 登录事件
				Message message = new Message();
				// 发送
				message.setRequest("登录");
				message.setName(text.getText() );
				message.setPassword( text_1.getText() );
				if(message.getName()==null || "".equals(message.getName())) {
					MessageDialog.openInformation(getShell(), "登录失败!", "请输入用户名");
					return;
				}
				if(message.getPassword()==null || "".equals(message.getPassword())) {
					MessageDialog.openInformation(getShell(), "登录失败!", "请输入密码");
					return;
				}
				
				try {
					//Constans.client.send( new Piece(0,0,0,"测试") );
					ConstansClient.client.send(message);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}
		});
		button.setText("登录");
		button.setForeground(SWTResourceManager.getColor(SWT.COLOR_LINK_FOREGROUND));
		button.setFont(SWTResourceManager.getFont("华文隶书", 13, SWT.BOLD));
		button.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_BACKGROUND_GRADIENT));
		button.setBounds(121, 398, 84, 26);

		Button button_1 = new Button(this, SWT.NONE);
		button_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) { // 注册事件
				Message message = new Message();
				// 发送
				message.setRequest("注册");
				message.setName(text.getText() );
				message.setPassword( text_1.getText() );
				if(message.getName()==null || "".equals(message.getName())) {
					MessageDialog.openInformation(getShell(), "注册失败!", "请输入用户名");
					return;
				}
				if(message.getPassword()==null || "".equals(message.getPassword())) {
					MessageDialog.openInformation(getShell(), "注册失败!", "请输入密码");
					return;
				}
				
				try {
					ConstansClient.client.send(message);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}
		});
		button_1.setText("注册");
		button_1.setForeground(SWTResourceManager.getColor(SWT.COLOR_LINK_FOREGROUND));
		button_1.setFont(SWTResourceManager.getFont("华文隶书", 13, SWT.BOLD));
		button_1.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_BACKGROUND_GRADIENT));
		button_1.setBounds(274, 398, 84, 26);
		
		Button btnNewButton = new Button(this, SWT.NONE);
		btnNewButton.setText("<-\u8FD4\u56DE");
		btnNewButton.setFont(SWTResourceManager.getFont("Microsoft YaHei UI", 8, SWT.NORMAL));
		btnNewButton.setBounds(30, 511, 49, 21);

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	
	class UserListen implements Runnable{
		boolean flag = true;
		@Override
		public void run() {
			while (flag) {
				if (ConstansClient.client != null) {
					try {
						Message accept_message = (Message)ConstansClient.client.accept();
						String accept_Request = accept_message.getRequest();
						String accept_name = accept_message.getName();
						//String accept_password = accept_message.getPassword();
						
						switch (accept_Request) {
						case "登录成功": {
							Display.getDefault().asyncExec(() -> {
								ConstansClient.name=accept_name;
								HallUi.seatTable= Integer.parseInt( accept_message.getText()  );
								
								HallUi hu = new HallUi(getShell(), SWT.None);
								Start.s1.topControl = hu;
								getShell().layout(); // 重置布局 相当于刷新
								MessageBox mb = new MessageBox(getShell(),SWT.NONE);
								mb.setText("登录成功！");
								mb.setMessage("欢迎你"+accept_name);
								mb.open();
							});
							flag = false; //关闭该线程
							break;
						}
						case "登录失败": {
							Display.getDefault().asyncExec(() -> {
								MessageBox mb = new MessageBox(getShell(),SWT.NONE);
								mb.setText("登录失败！");
								mb.setMessage("用户名或密码错误！");
								mb.open();
							});
							break;
						}
						case "注册成功": {
							Display.getDefault().asyncExec(() -> {
								MessageBox mb = new MessageBox(getShell(),SWT.NONE);
								mb.setText("注册成功！");
								mb.setMessage(accept_name+",恭喜你注册成功");
								mb.open();
							});
							break;
						}
						case "注册失败": {
							Display.getDefault().asyncExec(() -> {
								MessageBox mb = new MessageBox(getShell(),SWT.NONE);
								mb.setText("注册失败！");
								mb.setMessage("用户名已注册！");
								mb.open();
							});
							break;
						}
						
						}
						
					}catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
				}else {
					return;
					}
				}
		}
		
	}
}
