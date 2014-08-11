package com.hugong.lgl;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JPanel;

class StatisticPanel extends JPanel implements Runnable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public StatisticPanel(){
		SystemInfo.afterStatistic = false;
		
		for(int i = 0; i < SystemInfo.enemyTypeCount; ++i){
			et1[i] = new EnemyTank(outerPad+player1X,outerPad+innerPad+i*vgap,Direction.DIR_DOWN,SystemInfo.getTankTypeByInt(i));
			//构造函数中窗口尚未创造完毕，使用getWidth()会得到错误数据！
			//et2[i] = new EnemyTank(outerPad+player1X+SystemInfo.jfWidth/2,outerPad+innerPad+i*vgap,Direction.DIR_DOWN,SystemInfo.getTankTypeByInt(i));
			player1[i] = player2[i] = 0;
		}
	}
	
	
	public void paint(Graphics g){
		Color oldColor = g.getColor();

		g.setColor(Color.blue);
		g.fillRect(outerPad, outerPad, SystemInfo.jfWidth-2*outerPad, SystemInfo.jfHeight-2*outerPad);
		g.setFont(new Font("Serif",Font.BOLD,15));
		g.setColor(Color.red);
		g.drawRect(outerPad,outerPad,SystemInfo.jfWidth-2*outerPad,SystemInfo.jfHeight-2*outerPad);
		g.setColor(Color.white);
		g.fillRect(SystemInfo.jfWidth/2-seperateLineWidth/2, outerPad+innerPad/2, seperateLineWidth,SystemInfo.jfHeight-2*outerPad-innerPad);
		g.setColor(Color.green);
		g.drawString("player Ⅰ", outerPad+player1X+SystemInfo.tankWidth, outerPad+innerPad/2);
		g.setColor(Color.yellow);
		g.drawString("player Ⅱ", SystemInfo.jfWidth/2+player1X+2*SystemInfo.tankWidth, outerPad+innerPad/2);
		for(int i = 0; i < 4; ++i){
			et1[i].drawTank(g);
			g.setColor(Color.green);
			g.drawString("×  " + player1[i] + "  = " + player1[i]*SystemInfo.enemyPrice[i], outerPad+player1X+2*SystemInfo.tankWidth, outerPad+innerPad+i*vgap);
			et2[i] = new EnemyTank(outerPad+player1X+SystemInfo.jfWidth/2,outerPad+innerPad+i*vgap,Direction.DIR_DOWN,SystemInfo.getTankTypeByInt(i));
			et2[i].drawTank(g);
			g.setColor(Color.yellow);
			g.drawString("×  " + player2[i] + "  = " + player2[i]*SystemInfo.enemyPrice[i], SystemInfo.jfWidth/2+player1X+2*SystemInfo.tankWidth, outerPad+innerPad+i*vgap);
		}
		g.setColor(Color.green);
		g.drawString("total " + player1KilledNum + "  =  " + player1KilledGrade, outerPad+player1X, SystemInfo.jfHeight-outerPad-15);
		g.setColor(Color.yellow);
		g.drawString("total " + player2KilledNum + "  =  " + player2KilledGrade, outerPad + player1X+SystemInfo.jfWidth/2, SystemInfo.jfHeight-outerPad-15);
		g.setColor(oldColor);
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		//System.err.println("panel width:" + SystemInfo.jfWidth + ",heigth:" + SystemInfo.jfHeight);
		
		for(int i = 0; i < SystemInfo.enemyTypeCount;){
			if(player1[i] < Hero.getHero1Grade()[i] || player2[i] < Hero.getHero2Grade()[i]){
				if(player1[i] < Hero.getHero1Grade()[i]){
					++player1[i];
				}
				if(player2[i] < Hero.getHero2Grade()[i]){
					++player2[i];
				}
				repaint();
				SystemInfo.soundOperation.playSound("statistic.wav", 500);
				try {
					Thread.sleep(150);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else{
				++i;
			}
		}
		//计算本关总成绩(这里我们不加上本关附加分——英雄捡到加分宝物所得的分，因为这样做可能还会使用户迷惑)
		for(int i = 0; i < SystemInfo.enemyTypeCount; ++i){
			player1KilledNum += Hero.getHero1Grade()[i];
			player1KilledGrade += Hero.getHero1Grade()[i]*SystemInfo.enemyPrice[i];
			player2KilledNum += Hero.getHero2Grade()[i];
			player2KilledGrade += Hero.getHero2Grade()[i]*SystemInfo.enemyPrice[i];
			//System.err.println(i + " hero1:" + Hero.getHero1Grade()[i] + "  hero2:" + Hero.getHero2Grade()[i]);
		}
		repaint();
	
		if(!SystemInfo.afterStatistic)
		{
			synchronized(SystemInfo.statisticLock){
				try {
					SystemInfo.statisticLock.wait(2000);	//统计完后等2秒
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				SystemInfo.afterStatistic = true;
				SystemInfo.statisticLock.notify();
			}
		}

	}

	
	private EnemyTank[] et1 = new EnemyTank[SystemInfo.enemyTypeCount];
	private EnemyTank[]	et2 = new EnemyTank[SystemInfo.enemyTypeCount];
	private int[] player1 = new int[SystemInfo.enemyTypeCount];
	private int[] player2 = new int[SystemInfo.enemyTypeCount];
	private int player1KilledNum = 0;
	private int player2KilledNum = 0;
	private int player1KilledGrade = 0;
	private int player2KilledGrade = 0;
	private int player1X = 30+SystemInfo.tankWidth/2;
	private int vgap = 50+SystemInfo.tankHeight/2;
	private int innerPad = 100;
	private int outerPad = 10;
	private int seperateLineWidth = 4;

}
