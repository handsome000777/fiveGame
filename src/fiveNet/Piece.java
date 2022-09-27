package fiveNet;

import java.io.Serializable;

//存储棋子的相关信息
public class Piece implements Serializable {

/**
	 * 
	 */
	private static final long serialVersionUID = -3344495629230153223L;

//private int radius = 16;
	private String request = "传子"; // 
	
	private int color = 0;
	private int x;
	private int y;

	public Piece(int color, int x, int y, String request) {
		super();
		this.color = color;
		this.x = x;
		this.y = y;
		this.request = request;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public String getRequest() {
		return request;
	}

	public void setRequest(String request) {
		this.request = request;
	}

}