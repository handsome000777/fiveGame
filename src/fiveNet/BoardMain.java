package fiveNet;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Date;
import java.util.Stack;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;

import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.wb.swt.SWTResourceManager;

import com.yc.net.Constans;
import com.yc.net.ConstansClient;
import com.yc.net.Message;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.events.DragDetectListener;
import org.eclipse.swt.events.DragDetectEvent;
import org.eclipse.swt.widgets.Text;

public class BoardMain {

	protected Shell shell;
	protected Display display;

	// 传输方面的变量
	// Client client = null;
	Piece sendPiece;
	int accept_color;
	int accept_x;
	int accept_y;

	int shoudao = 0; // 1表示线程收到棋子

	// 盘面变量
	int qx = 20, qy = 40, qw = 490, qh = 490;// 棋盘位置、宽高
	int x = 0, y = 0;// 保存棋子坐标
	int[][] SaveGame = new int[15][15];// 保存每个棋子
	Stack<Piece> stack = new Stack<Piece>(); // 用栈保存每一步下的棋
	protected Canvas canvas;
	Paint paint;
	protected Label turnLabel; // 游戏状态到谁下棋
	Button btnNewButton_4; // 开始/准备
	Label timeLabel; // 倒计时
	// 控制方面的变量
	int state = 0; // 游戏状态 0结束 1落子态 2等待态 3处理请求态
	String request; // 向服务器及对方请求 “准备” “传子” “结果,五子”
	public int ready = -1; // 准备人数
	int color = 0; // 记录你要下什么棋 白棋=2，黑棋=1
	int refresh = 0; // 1时双击鼠标刷新界面
	boolean back = false; // 是否同意悔棋
	volatile int time = 90; // 倒计时
	volatile boolean loop = true; // 控制两个线程
	private Text text;
	public Text text_1;
	private Composite composite_1;
	private Label lblNewLabel_1;
	private Button btnNewButton_3;
	public Label opLabel;
	public Composite composite_2;
	// 构造方法 程序创建的同时 创建客服端并连接服务端

	public BoardMain() {
		super();
		// TODO Auto-generated constructor stub
		try {
//			Socket socket = new Socket(InetAddress.getLocalHost(), 10086); // 与服务端的地址和端口号一致
//			// TODO 构建输出输入流,输入输出流问题，先输出后输入
//			ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
//			ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
//			client = new Client(socket, ois, oos); // 创建客户端

		} catch (Exception e) {
			System.out.println("服务器拒绝连接！");
		}

	}

	/**
	 * Launch the application.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			BoardMain window = new BoardMain();
			window.open();

		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		display = Display.getDefault();

		createContents();

		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) { // 设备不用就休眠
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell();
		shell.setSize(876, 644);
		shell.setText("\u4E94\u5B50\u68CB");
		shell.setLayout(null);
		shell.setBackgroundImage(SWTResourceManager.getImage(BoardMain.class, "/fiveNet/five.png"));
		shell.setBackgroundMode(SWT.INHERIT_FORCE); // 是强制所有组件使用父窗口的背景色 实现透明
//		new Thread(() -> {
//			boolean flag = true;
//			while (flag) {
//				try {
//					Thread.sleep(1000);
//				} catch (InterruptedException e1) {
//					e1.printStackTrace();
//				}
//				Display.getDefault().asyncExec(() -> {
//					shell.setText(new Date().toGMTString());
//				});
//			}
//		}).start();
		Thread t1 = new Thread(new Listen(), "客户端监听器"); // 线程名
		t1.setDaemon(true); // 设为守护线程
		t1.start();

		Thread t2 = new Thread(() -> { // 倒计时线程
			// boolean flag = true;
			while (loop) {
				if (loop == false) {
					return;
				}
				while (time <= 89) {
					if (loop == false) {
						return;
					}
					try {
						Display.getDefault().asyncExec(() -> {
							timeLabel.setText("倒计时" + time + "");
						});
						Thread.sleep(1000);
						if (loop == false) {
							return;
						}
						time--;
						Display.getDefault().asyncExec(() -> {
							timeLabel.setText("倒计时" + time + "");
						});
					} catch (Exception e1) {
						// e1.printStackTrace();
					}
				}
			}
		}, "Ttime");
		t2.setDaemon(true);
		t2.start();
		canvas = new Canvas(shell, SWT.NONE);
		canvas.setBounds(10, 41, 520, 543);
		Font font = new Font(shell.getDisplay(), "华文行楷", 20, 50);
		canvas.setFont(font);

		timeLabel = new Label(canvas, SWT.NONE);
		timeLabel.setBounds(185, 10, 104, 24);
		timeLabel.setText("\u5012\u8BA1\u65F6\uFF1A");
		timeLabel.setFont(SWTResourceManager.getFont("华文行楷", 17, SWT.BOLD));
		timeLabel.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
		
		composite_1 = new Composite(canvas, SWT.NONE);
		composite_1.setBounds(15, 0, 39, 39);
		composite_1.setBackgroundImage(SWTResourceManager.getImage(BoardMain.class, ConstansClient.headImg));

		lblNewLabel_1 = new Label(canvas, SWT.BORDER);
		lblNewLabel_1.setAlignment(SWT.CENTER);
		lblNewLabel_1.setText(ConstansClient.name);
		lblNewLabel_1.setForeground(SWTResourceManager.getColor(0, 0, 0));
		lblNewLabel_1.setFont(SWTResourceManager.getFont("宋体", 10, SWT.BOLD));
		lblNewLabel_1.setBounds(65, 18, 49, 16);
		
		opLabel = new Label(canvas, SWT.BORDER);
		opLabel.setAlignment(SWT.CENTER);

		opLabel.setFont(SWTResourceManager.getFont("宋体", 10, SWT.BOLD));
		opLabel.setBounds(461, 18, 49, 16);
		
		composite_2 = new Composite(canvas, SWT.BORDER);
//		composite_2.setBackgroundImage(SWTResourceManager.getImage(BoardMain.class, ""));
//		composite_2.setBackgroundImage(null);
		composite_2.setBounds(411, 0, 39, 39);
		
		Label lblNewLabel_2_1 = new Label(canvas, SWT.NONE);
		lblNewLabel_2_1.setAlignment(SWT.CENTER);
		lblNewLabel_2_1.setText("\u5BF9\u624B\uFF1A");
		lblNewLabel_2_1.setFont(SWTResourceManager.getFont("微软雅黑", 10, SWT.BOLD));
		lblNewLabel_2_1.setBounds(338, 15, 49, 19);
		
		Composite composite = new Composite(shell, SWT.NONE);
		composite.setBounds(523, 0, 330, 584);
		Label lblNewLabel = new Label(composite, SWT.NONE);
		lblNewLabel.setLocation(33, 45);
		lblNewLabel.setSize(232, 84);
		lblNewLabel.setFont(SWTResourceManager.getFont("华文行楷", 55, SWT.BOLD));
		lblNewLabel.setText("\u4E94\u5B50\u68CB");
		Button btnNewButton_1 = new Button(composite, SWT.NONE);
		btnNewButton_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (state == 1) {
					state = 3;
					try {
						ConstansClient.client.send(new Piece(0, 0, 0, "悔棋"));
						turnLabel.setText("已发送悔棋请求,请等待...");
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		});
		btnNewButton_1.setLocation(94, 188);
		btnNewButton_1.setSize(105, 37);
		btnNewButton_1.setText("\u6094\u68CB");
		btnNewButton_1.setFont(SWTResourceManager.getFont("Microsoft Sans Serif", 16, SWT.NORMAL));
		Button btnNewButton_2 = new Button(composite, SWT.NONE);
		btnNewButton_2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (state == 1) {
					state = 3;
					if (MessageDialog.openConfirm(shell, "认输", "这就投了?")) {
						try {
							ConstansClient.client.send(new Piece(0, 0, 0, "认输"));
							turnLabel.setText("认输！");
							btnNewButton_4.setVisible(true);
							time = 91;
							state = 0;
							ready = 0;
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}

				}

			}
		});
		btnNewButton_2.setLocation(94, 241);
		btnNewButton_2.setSize(105, 37);
		btnNewButton_2.setText("\u8BA4\u8F93");
		btnNewButton_2.setFont(SWTResourceManager.getFont("Microsoft Sans Serif", 16, SWT.NORMAL));
		turnLabel = new Label(composite, SWT.BORDER | SWT.CENTER);
		turnLabel.setForeground(SWTResourceManager.getColor(SWT.COLOR_DARK_RED));
		turnLabel.setLocation(33, 345);
		turnLabel.setSize(242, 30);
		turnLabel.setFont(SWTResourceManager.getFont("华文琥珀", 17, SWT.NORMAL));
		turnLabel.setText("\u25CF\u7B49\u5F85\u5F00\u59CB");

		Label colorLabel = new Label(composite, SWT.BORDER);
		colorLabel.setFont(SWTResourceManager.getFont("华文新魏", 21, SWT.BOLD));
		colorLabel.setBounds(119, 296, 58, 30);

		colorLabel.setText("黑方");
		colorLabel.setBackground(SWTResourceManager.getColor(SWT.COLOR_INFO_BACKGROUND));
		colorLabel.setForeground(SWTResourceManager.getColor(SWT.COLOR_TITLE_FOREGROUND));

		btnNewButton_4 = new Button(composite, SWT.NONE);
		btnNewButton_4.setFont(SWTResourceManager.getFont("Microsoft Sans Serif", 16, SWT.NORMAL));
		btnNewButton_4.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (state == 0) {
					for (int i = 0; i < 15; i++) {
						for (int j = 0; j < 15; j++) {
							SaveGame[i][j] = 0;
						}
					}
					canvas.redraw(); // 准备前先清理上盘界面
				}
				if(ready==-1) {
					MessageDialog.openInformation(shell, "等待对手", "对手来了再准备！");
				}
				if (state == 0 && ready == 0) { // 先准备
					try {
						ConstansClient.client.send(new Piece(0, 0, 0, "准备"));
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					ready = 1;
					colorLabel.setText("黑方");
					colorLabel.setBackground(SWTResourceManager.getColor(SWT.COLOR_INFO_BACKGROUND));
					colorLabel.setForeground(SWTResourceManager.getColor(SWT.COLOR_TITLE_FOREGROUND));
					turnLabel.setText("已准备,等待对手准备...");
					btnNewButton_4.setVisible(false);

				} else if (state == 0 && ready == 1) { // 后准备
					try {
						ConstansClient.client.send(new Piece(0, 0, 0, "准备"));
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					ready = 2;
					state = 2; // 后准备的改变为等待态
					colorLabel.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_BACKGROUND_GRADIENT));
					colorLabel.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
					colorLabel.setText("白方");
					turnLabel.setText("等待对手落子...");
					btnNewButton_4.setVisible(false);
				}
			}
		});
		btnNewButton_4.setBounds(94, 135, 105, 37);
		btnNewButton_4.setText("\u5F00\u59CB/\u51C6\u5907");

		text = new Text(composite, SWT.BORDER);
		text.setBounds(31, 532, 208, 30);

		text_1 = new Text(composite, SWT.BORDER | SWT.V_SCROLL);
		text_1.setFont(SWTResourceManager.getFont("Microsoft YaHei UI", 10, SWT.BOLD));
		text_1.setBounds(33, 414, 263, 92);

		Button btnNewButton = new Button(composite, SWT.NONE);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (!"".equals(text.getText()) && text.getText() != null) {
					Message message = new Message();
					message.setRequest("双人聊天");
					message.setText(text.getText());
					;
					try {
						ConstansClient.client.send(message);
						text.setText("");
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		btnNewButton.setBounds(245, 532, 51, 30);
		btnNewButton.setText("\u53D1\u9001");

		btnNewButton_3 = new Button(composite, SWT.NONE);
		btnNewButton_3.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				loop=false;
				Message message = new Message();
				message.setRequest("退出房间");
				//message.setSeat(1);
				try {
					ConstansClient.client.send(message);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				ConstansClient.seat=0;
				shell.dispose();
			}
		});
		btnNewButton_3.setText("<-\u9000\u51FA");
		btnNewButton_3.setFont(SWTResourceManager.getFont("Microsoft YaHei UI", 9, SWT.NORMAL));
		btnNewButton_3.setBounds(258, 14, 55, 25);

		// 抽出来的绘图方法
		paint = new Paint(qx, qy, qw, qh);
		// 绘画监听事件 触发即从上执行全部代码 只要画面出现改变(事件) 即 被监听器发现 触发相应代码(处理)
		canvas.addPaintListener(new PaintListener() {
			public void paintControl(PaintEvent arg0) {
				// 绘制棋盘格线
				paint.board(arg0);
				switch (state) {
				case 0: {
					break;
				}
				case 1: { // 落子态绘制自己的棋
					if (color == 1) {
						paint.blackPiece(arg0, x, y);
					} else if (color == 2) {
						paint.whitePiece(arg0, x, y);
					}
					break;
				}
				case 2: { // 等待态绘制对手的棋
					if (accept_color == 1) {
						paint.blackPiece(arg0, accept_x, accept_y);
					} else if (accept_color == 2) {
						paint.whitePiece(arg0, accept_x, accept_y);
					}
					break;

				}
				}
				if (refresh == 1) {
					paint.allPiece(arg0, SaveGame);
				}

			}
		});
		canvas.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {

				// 判断是否已开始游戏
				if (state == 0 && ready == 0) {
					MessageDialog.openInformation(shell, "未准备", "准备了才能玩！");
				} else if (state == 0 && ready == 1) {
					MessageDialog.openInformation(shell, "准备中", "有人没准备！");
				}
				if (state == 1) { // 当前为落子状态
					// 获取鼠标点击位置
					x = e.x;
					y = e.y;
					if (!(x > qx && x < qx + qw && y > qy && y < qy + qh)) {
						return;
					}
					// 计算点击位置最近的点
					if ((x - qx) % 35 > 17) {
						x = (x - qx) / 35 + 1;
					} else {
						x = (x - qx) / 35;
					}
					if ((y - qy) % 35 > 17) {
						y = (y - qy) / 35 + 1;
					} else {
						y = (y - qy) / 35;
					}
					if (!(SaveGame[x][y] == 0)) {
						return;
					}

					SaveGame = callPaintPiece(color, x, y, SaveGame, canvas); // 传标准棋盘xy 同时修改棋盘数组

					// 弹出胜利对话框
					boolean wl = winLose.winLost(SaveGame, x, y);
					if (wl) {
						try {
							request = "结果五子"; // 判断是否赢
							sendPiece = new Piece(color, x, y, request);
							ConstansClient.client.send(sendPiece);
						} catch (IOException e1) {
							e1.printStackTrace();
						}
						MessageDialog.openInformation(shell, "游戏胜利！", "牛皮，你赢了！"); // 弹窗后也会重绘界面
						Display.getDefault().asyncExec(() -> {
							state = 0;
							time = 91;
							turnLabel.setText("赢！");
							btnNewButton_4.setVisible(true);
						});
						ready = 0;

					} else {
						try {
							request = "传子";
							sendPiece = new Piece(color, x, y, request);
							ConstansClient.client.send(sendPiece);
						} catch (IOException e1) {
							e1.printStackTrace();
						}
						Display.getDefault().asyncExec(() -> {
							state = 2; // 落完子后 改成等待态
							time = 91;
							turnLabel.setText("等待对手落子...");
						});

					}
				}

			}

			@Override
			public void mouseDoubleClick(MouseEvent e) { // 双击绘制所有棋子 恢复丢页面
				refresh = 1;
				canvas.redraw();
				Display.getDefault().asyncExec(() -> {
					refresh = 0;
				});
			}
		});

	}

	private int[][] callPaintPiece(int color, int x, int y, int[][] SaveGame, Canvas canvas) { // 标准位置下的 x y
		int sx = x * 35 + qx; // 实际绘制位置
		int sy = y * 35 + qy;
		if (color == 1 && SaveGame[x][y] == 0) { // 绘制黑子
			Display.getDefault().asyncExec(() -> {
				canvas.redraw(sx - 15, sy - 15, 30, 30, false); // 间接 触发 不是调用 没有传参
			});
			SaveGame[x][y] = 1;
			stack.push(new Piece(1, x, y, null));
		} else if (color == 2 && SaveGame[x][y] == 0) { // 绘制白子
			Display.getDefault().asyncExec(() -> {
				canvas.redraw(sx - 15, sy - 15, 30, 30, false); // 间接 触发 不是调用 没有传参
			});
			SaveGame[x][y] = 2;
			stack.push(new Piece(2, x, y, null));
		}
		return SaveGame;
	}

	class Listen implements Runnable { // 循环监听服务端的信息
		//private boolean flag = true;

		@Override
		public void run() {
			// TODO Auto-generated method stub

			while (loop) {
				if (ConstansClient.client != null) {
					try {
						// Piece accept_piece = (Piece) ConstansClient.client.accept();
						Piece accept_piece = null;
						while (loop) { // 这里循环 直到数据线程收到message消息 标志位修改
							if (ConstansClient.pieceFlag == 1) {
								accept_piece = ConstansClient.client.piece;
								ConstansClient.pieceFlag = 0;
								break;
							}
							if(loop==false) {
								return;
							}
						}

						String accept_Request = accept_piece.getRequest();
						accept_color = accept_piece.getColor();
						accept_x = accept_piece.getX();
						accept_y = accept_piece.getY();
						switch (accept_Request) {
						case "准备": {
							if (state == 0 && ready == 1) { // 自己已准备 收到对方准备
								ready = 2;
								state = 1; // 改状态为落子态
								color = 1; // 黑棋先行
								time = 89;
								Display.getDefault().asyncExec(() -> {
									turnLabel.setText("请落子...");
								});
							}
							if (state == 0 && ready == 0) { // 自己没准备 收到对方准备
								ready = 1;
								color = 2; // 白棋后行
								Display.getDefault().asyncExec(() -> {
									turnLabel.setText("对手已准备,请准备...");
								});
							}
							break;
						}

						case "传子": {

							SaveGame = callPaintPiece(accept_color, accept_x, accept_y, SaveGame, canvas);

							Display.getDefault().asyncExec(() -> {
								state = 1; // 接收子后改为落子态
								time = 89;
								turnLabel.setText("请落子...");
							});

							break;
						}
						case "悔棋": {
							// int temp = state;
							state = 3; // 收到请求 先置其他请求态
							Display.getDefault().syncExec(new Runnable() { // 同步 需要等待 back 返回结果
								@Override
								public void run() {
									back = MessageDialog.openConfirm(shell, "对方提出悔棋", "是否同意悔棋?");
								}
							});
							if (back) { // 同意
								Piece back_piece = stack.pop();
								Piece back_piece2 = stack.pop();
								SaveGame[back_piece.getX()][back_piece.getY()] = 0;
								SaveGame[back_piece2.getX()][back_piece2.getY()] = 0;
								refresh = 1;
								Display.getDefault().asyncExec(() -> {
									canvas.redraw();
								});
								Display.getDefault().asyncExec(() -> {
									refresh = 0;
									state = 2;
									try {
										ConstansClient.client.send(new Piece(back_piece2.getColor(), 0, 0, "结果悔棋")); // 传结果
																														// 有颜色即同意
									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								});

							} else { // 不同意
								state = 2;
								ConstansClient.client.send(new Piece(0, 0, 0, "结果悔棋")); // 0表示拒绝
							}

							break;
						}
						case "结果悔棋": {
							if (accept_color == 0) { // 0表示不同意
								Display.getDefault().asyncExec(() -> {
									MessageDialog.openInformation(shell, "悔棋失败", "对方拒绝了您的悔棋！");
									state = 1;
									turnLabel.setText("请落子...");
								});
							} else {
								Display.getDefault().asyncExec(() -> {
									MessageDialog.openInformation(shell, "悔棋成功", "对方同意了您的悔棋！");
								});
								Piece back_piece = stack.pop();
								Piece back_piece2 = stack.pop();
								SaveGame[back_piece.getX()][back_piece.getY()] = 0;
								SaveGame[back_piece2.getX()][back_piece2.getY()] = 0;
								refresh = 1;
								Display.getDefault().asyncExec(() -> {
									canvas.redraw();
								});
								Display.getDefault().asyncExec(() -> {
									refresh = 0;
									state = 1;
									turnLabel.setText("请落子...");
								});
							}
							break;
						}
						case "认输": {
							Display.getDefault().asyncExec(() -> {
								MessageDialog.openInformation(shell, "游戏胜利！", "对方打不过投降了！");
							});
							Display.getDefault().asyncExec(() -> {
								state = 0;
								time = 91;
								turnLabel.setText("赢咯！");
								btnNewButton_4.setVisible(true);
							});
							ready = 0;
							break;
						}
						case "结果五子": {
							SaveGame = callPaintPiece(accept_color, accept_x, accept_y, SaveGame, canvas);
							Display.getDefault().asyncExec(() -> {
								MessageDialog.openInformation(shell, "游戏失败！", "输了！");

							});
							Display.getDefault().asyncExec(() -> {
								state = 0;
								time = 91;
								turnLabel.setText("输！");
								btnNewButton_4.setVisible(true);
							});
							ready = 0;
							break;
						}
						case "对方掉线": {
							Display.getDefault().asyncExec(() -> {
								MessageDialog.openInformation(shell, "游戏胜利！", "对方逃跑了！");
							});
							Display.getDefault().asyncExec(() -> {
								state = 0;
								time = 91;
								ready=-1;
								opLabel.setText("");
								composite_2.setBackgroundImage(null);
								turnLabel.setText("对方逃跑了！");
								btnNewButton_4.setVisible(true);
							});
							ready = 0;
							break;
						}

						}
					} catch (Exception e) {
						// client = null;
					}
				} else {
					// flag = false;
				}
			}

		}
	}
}
