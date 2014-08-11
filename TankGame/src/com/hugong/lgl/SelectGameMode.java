package com.hugong.lgl;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


class SelectGameMode extends JPanel implements MouseListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public SelectGameMode(){
		SystemInfo.afterModeSelect = false;
		jb1.addMouseListener(this);
		jb2.addMouseListener(this);
		jlab.setIcon(new ImageIcon(getClass().getResource("/images/tank_logo.gif")));
		
		this.setBackground(Color.black);
		add(jlab);
		add(jb1);
		add(jb2);
		this.addMouseListener(this);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == jb1){
			SystemInfo.oneMan = true;
			Hero.setHero1Num(SystemInfo.heroSumNum);
			Hero.setHero2Num(0);
			SystemInfo.afterModeSelect = true;
			//System.err.println("player 1");
		}
		else if(e.getSource() == jb2){
			SystemInfo.oneMan = false;
			Hero.setHero1Num(SystemInfo.heroSumNum);
			Hero.setHero2Num(SystemInfo.heroSumNum);
			SystemInfo.afterModeSelect = true;
			//System.err.println("player 2");
		}
		//System.err.println(SystemInfo.afterModeSelect);
		if(SystemInfo.afterModeSelect){
			synchronized(SystemInfo.afterModeSelectLock){	
				SystemInfo.afterModeSelectLock.notify();
			}
		}
	}
	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == jb1)
			jb1.setEnter(true);
		if(e.getSource() == jb2)
			jb2.setEnter(true);
	}
	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == jb1)
			jb1.setEnter(false);
		if(e.getSource() == jb2)
			jb2.setEnter(false);
	}
	
	private MyButton jb1 = new MyButton("1 P L A Y E R",Color.green,Color.red);  //棕色 Color(128,64,0)
	private MyButton jb2 = new MyButton("2 P L A Y E R", Color.green,Color.red);
	private JLabel  jlab = new JLabel();
}

class MyButton extends JButton{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public MyButton(String str,Color enterColor,Color exitColor){
		super(str);
		this.str = str;
		this.enterColor = enterColor;
		this.exitColor = exitColor;
	}
	//重写父类的paintComponent
	protected void paintComponent(Graphics g){
		Color oldColor = g.getColor();
		if(enter)
			g.setColor(enterColor);
		else
			g.setColor(exitColor);
	
		g.drawString(str, this.getWidth()/8, 2*this.getHeight()/3);
		g.setColor(oldColor);
	}
	
	public boolean isEnter() {
		return enter;
	}
	public void setEnter(boolean enter) {
		this.enter = enter;
	}
	
	private boolean enter = false;
	private String str;
	private Color enterColor = Color.red;
	private Color exitColor = Color.blue;
}