package com.hugong.lgl;

import java.awt.Color;
import java.awt.Graphics;
import java.io.Serializable;

//草丛坦克子弹均允许过：BOTH_GO,河流不允许坦克但允许子弹过：ONLY_BULLET，石墙和泥墙坦克子弹均不允许过：BOTH_FORBIN
//泥墙为BOTH_FORBIN1,石墙为BOTH_FORBIN2
enum BarrierType{NONE,BOTH_GO,ONLY_BULLET,BOTH_FORBIN1,BOTH_FORBIN2,UNDEFINED,}

class Barrier implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3977553345211565923L;
	
	public Barrier(int x, int y, BarrierType barrierType,int width, int height) {
		super();
		this.x = x;
		this.y = y;
		this.barrierType = barrierType;
		this.width = width;
		this.height = height;
		this.setColorByBarrierType(this.barrierType);
	}
	
	public Barrier(int x, int y,BarrierType barrierType) {
		super();
		this.x = x;
		this.y = y;
		this.barrierType = barrierType;
		this.setColorByBarrierType(this.barrierType);
	}
	
	public void drawBarrier(Graphics g){
		Color oldColor = g.getColor();
		g.setColor(color);
		//g.fill3DRect(x, y, width, height, false);
		switch(this.getBarrierType()){
		case BOTH_GO:		//草丛
			g.fill3DRect(x, y, width/2-1, height/2-1, false);
			g.fill3DRect(x+width/2, y, width/2, height/2-1, false);
			g.fill3DRect(x, y+height/2, width/2-1, height/2, false);
			g.fill3DRect(x+width/2, y+height/2, width/2, height/2, false);
			break;
		case ONLY_BULLET:	//河流
			g.fill3DRect(x, y, width, height, false);
			if(Math.random() <= 0.9)
			{
				Color clr = g.getColor();
				g.setColor(Color.white);
				g.drawLine(x+1,y+height/2,x+width-2,y+height/2);
				g.setColor(clr);
			}
			break;
		case BOTH_FORBIN1:	//泥墙
			g.fill3DRect(x, y, width, height/2, false);
			g.fill3DRect(x, y+height/2, width, height/2, false);
			if(Math.random() <= 0.5)
				g.drawLine(x+width/2, y+height/2, x+width/2, y+height-1);
			break;
		case BOTH_FORBIN2:	//石墙
			g.fill3DRect(x, y, width, height, false);
			//g.fill3DRect(x+width/8, y+height/8, width-width/4, height-height/4, true);
			g.fill3DRect(x+width/6, y+height/6, width-width/3, height-height/3, true);
			break;
		default:
			break;
		}
		g.setColor(oldColor);
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
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	
	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public BarrierType getBarrierType() {
		return barrierType;
	}

	public void setBarrierType(BarrierType barrierType) {
		this.barrierType = barrierType;
		this.setColorByBarrierType(this.barrierType);
	}
	
	private void setColorByBarrierType(BarrierType barrierType){
		switch(this.barrierType){
		case NONE:
			color = Color.black;
			break;
		case BOTH_GO:
			color = Color.green;
			break;
		case ONLY_BULLET:
			color = Color.blue;		//蓝色
			break;
		case BOTH_FORBIN1:
			//color = Color.orange;	
			color = new Color(255,80,0);	//浅棕色
			break;
		case BOTH_FORBIN2:
			//color = Color.lightGray;
			color = Color.white;
		default:
			break;
		}
	}
	public boolean isLive() {
		return live;
	}

	public void setLive(boolean live) {
		this.live = live;
	}
	//障碍物的坐标以左上角顶点为基准
	private int x;
	private int y;
	private Color color = Color.black;
	private BarrierType barrierType = BarrierType.UNDEFINED;
	private boolean live = true;
	private int width = SystemInfo.unitLength;
	private int height = SystemInfo.unitLength;
}
