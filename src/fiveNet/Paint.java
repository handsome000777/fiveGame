package fiveNet;

import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Canvas;

public class Paint {
	int qx = 20, qy = 40, qw = 490, qh = 490;// 棋盘位置、宽高

	public Paint(int qx, int qy, int qw, int qh) {
		super();
		this.qx = qx;
		this.qy = qy;
		this.qw = qw;
		this.qh = qh;
	}

	public void board(PaintEvent arg0) {

		arg0.gc.setBackground(new Color(255, 250, 205)); // 设置棋盘背景色
		arg0.gc.fillRectangle(qx, qy, qw, qh);// 绘制棋盘背景矩形

		for (int x = 0; x <= qw; x += 35) {
			arg0.gc.drawLine(qx, x + qy, qw + qx, x + qy);// 绘制一条横线
			arg0.gc.drawLine(x + qx, qy, x + qx, qh + qy);// 绘制一条竖线
		}
		arg0.gc.setBackground(new Color(50, 44, 44)); // 棋盘中的标注位点
		for (int i = 3; i <= 11; i += 4) {
			for (int y = 3; y <= 11; y += 4) {
				arg0.gc.fillOval(35 * i + qx - 3, 35 * y + qy - 3, 6, 6);// 绘制实心圆
			}
		}

	}

	public void blackPiece(PaintEvent arg0, int x, int y) {
		// 绘制黑棋
		int sx = x * 35 + qx;
		int sy = y * 35 + qy;
		arg0.gc.setBackground(new Color(50, 44, 44));
		arg0.gc.fillOval(sx - 13, sy - 13, 26, 26);// 绘制实心圆

	}

	public void whitePiece(PaintEvent arg0, int x, int y) {
		// 绘制白棋
		int sx = x * 35 + qx;
		int sy = y * 35 + qy;
		arg0.gc.setBackground(new Color(250, 244, 244));

		arg0.gc.fillOval(sx - 13, sy - 13, 26, 26);// 绘制实心圆
		arg0.gc.setForeground(new Color(50, 44, 44));
		arg0.gc.drawOval(sx - 13, sy - 13, 26, 26);// 绘制空心圆

	}

	public void allPiece(PaintEvent arg0, int[][] SaveGame) {
		// 绘制棋子
		for (int i = 0; i < 15; i++) {
			for (int j = 0; j < 15; j++) {

				if (SaveGame[i][j] == 1)// 黑子
				{
					int sx = i * 35 + qx;
					int sy = j * 35 + qy;
					arg0.gc.setBackground(new Color(50, 44, 44));
					arg0.gc.fillOval(sx - 13, sy - 13, 26, 26);// 绘制实心圆
				}
				if (SaveGame[i][j] == 2)// 白子
				{
					int sx = i * 35 + qx;
					int sy = j * 35 + qy;
					arg0.gc.setBackground(new Color(250, 244, 244));

					arg0.gc.fillOval(sx - 13, sy - 13, 26, 26);// 绘制实心圆
					arg0.gc.setForeground(new Color(50, 44, 44));
					arg0.gc.drawOval(sx - 13, sy - 13, 26, 26);// 绘制空心圆
				}
			}
		}
	}

}
