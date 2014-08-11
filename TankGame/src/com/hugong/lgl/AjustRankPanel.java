package com.hugong.lgl;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.*;


class AjustRankPanel extends JPanel implements MouseListener,KeyListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public AjustRankPanel(){
		SystemInfo.start = false;
		upPanel = new UpPanel();
		downPanel = new JPanel();
		this.setLayout(new GridLayout(2,1));
		
		jbMinus.setPreferredSize(new Dimension(30,30));
		jbMinus.setIcon(new ImageIcon(getClass().getResource("/images/minus.jpg")));
		//jbMinus.setMnemonic('A');
		jbMinus.addMouseListener(this);
		jbMinus.addKeyListener(this);
		jbAdd.setPreferredSize(new Dimension(30,30));
		jbAdd.setIcon(new ImageIcon(getClass().getResource("/images/add.jpg")));
		//jbAdd.setMnemonic('D');
		jbAdd.addMouseListener(this);
		jbAdd.addKeyListener(this);
		jbStart.setPreferredSize(new Dimension(30,30));
		jbStart.setIcon(new ImageIcon(getClass().getResource("/images/start.jpg")));
		//jbStart.setMnemonic(KeyEvent.VK_ENTER);
		jbStart.addMouseListener(this);
		jbStart.addKeyListener(this);
		downPanel.setBackground(Color.black);
		downPanel.setLayout(new FlowLayout(FlowLayout.CENTER,10,10));
		downPanel.add(jbMinus);
		downPanel.add(jbAdd);
		downPanel.add(jbStart);
		
		upPanel.setPreferredSize(new Dimension(this.getWidth(),this.getHeight()/2));
		add(upPanel);
		add(downPanel);
		this.setFocusable(true);	//Panel,JPanel要想响应键盘事件，必须设置可以获得焦点标志
		this.setBackground(Color.black);
		this.addKeyListener(this);
		this.addMouseListener(this);
	
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		switch(e.getKeyCode()){
		case KeyEvent.VK_A:
		case KeyEvent.VK_LEFT:
			SystemInfo.rank.minusRank();
			break;
		case KeyEvent.VK_D:
		case KeyEvent.VK_RIGHT:
			SystemInfo.rank.addRank();
			break;
		case KeyEvent.VK_ENTER:
		case KeyEvent.VK_SPACE:
			SystemInfo.start = true;
			break;
		default:
			break;
		}
		upPanel.repaint();
		if(SystemInfo.start){
			synchronized(SystemInfo.startLock){		
				SystemInfo.startLock.notify();
			}
		}
	}
	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == jbMinus)
			SystemInfo.rank.minusRank();
		else if(e.getSource() == jbAdd)
			SystemInfo.rank.addRank();
		else if(e.getSource() == jbStart)
			SystemInfo.start = true;
		this.repaint();	// upPanel.repaint();
		
		if(SystemInfo.start){
			synchronized(SystemInfo.startLock){		
				SystemInfo.startLock.notify();
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
		
	}
	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	private UpPanel upPanel;
	private JPanel downPanel;
	private JButton jbMinus = new JButton();
	private JButton jbAdd = new JButton();
	private JButton jbStart = new JButton();
}

class UpPanel extends JPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void paint(Graphics g){
		Color oldColor = g.getColor();
		g.setFont(new Font("SansSerif",Font.BOLD,50));
		g.setColor(Color.yellow);
		g.drawString("STAGE" + SystemInfo.rank.getCur_rank(), this.getWidth()/3, this.getHeight()-10);
		g.setColor(oldColor);
	}
}
