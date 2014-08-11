package com.hugong.lgl;
import java.awt.*;
//本省宝物类型最好用一个枚举类型表示，此处简化处理，用整型表示
//0：heroprotect,1:homeprotect,2:mine,3:stoptime(敌军暂停),4:addtank,5:star(加分)
import java.io.Serializable;

class Wealth implements Runnable,Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1896550886759851809L;
	public Wealth(int x,int y , int wealthType){
		this.x = x;
		this.y = y;
		this.wealthType = wealthType;
	}
	
	public void drawWealth(Graphics g){
		String filename = null;
		switch(this.wealthType){
		case 0:
			filename = "heroprotect.jpg";
			break;
		case 1:
			filename = "homeprotect.jpg";
			break;
		case 2:
			filename = "mine.jpg";
			break;
		case 3:
			filename = "stoptime.jpg";
			break;
		case 4:
			filename = "addtank.jpg";
			break;
		case 5:
			filename = "star.jpg";
			break;
		default:
			break;
		}
		if(filename != null)
			SystemInfo.imageOperation.drawImage(g, filename, new Point(x,y));
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
	public int getWealthType() {
		return wealthType;
	}
	public void setWealthType(int wealthType) {
		this.wealthType = wealthType;
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
	public boolean isShow() {
		return show;
	}
	public void setShow(boolean show) {
		this.show = show;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		synchronized(this){
			if(this.show){
				try {
					this.wait(SystemInfo.wealthShowTime);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	//宝物出现时间为6s
				//this.notify();
				this.show = false;

			}
		}
	}	
	

	//wealth的基准坐标以左上角坐标为准
	private int x;
	private int y;
	private int wealthType =  -1;
	private int width = SystemInfo.wealthWidth;
	private int height = SystemInfo.wealthHeight;
	private boolean show = true;


}
