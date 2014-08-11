package com.hugong.lgl;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.io.IOException;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

class PromptPanel extends JPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public PromptPanel(){
		tankY[0] = 100;
		tankY[1] = 140;
		tankY[2] = 180;
		tankY[3] = 220;
		tankY[4] = 320;
		tankY[5] = 360;
		enemys[0] = new EnemyTank(30,tankY[0],Direction.DIR_UP,TankType.LOW_ARMOUR);
		enemys[1] = new EnemyTank(30,tankY[1],Direction.DIR_UP,TankType.MIDDLE_ARMOUR);
		enemys[2] = new EnemyTank(30,tankY[2],Direction.DIR_UP,TankType.FAST_SPEED);
		enemys[3] = new EnemyTank(30,tankY[3],Direction.DIR_UP,TankType.HIGH_ARMOUR);
		hero1 = new Hero(30,tankY[4],Direction.DIR_UP,TankType.HERO,Color.green);
		hero2 = new Hero(30,tankY[5],Direction.DIR_UP,TankType.HERO,Color.yellow);
		
		try {
			//redFlag = ImageIO.read(new File("src/images/flag.jpg"));
			redFlag = ImageIO.read(getClass().getResource("/images/flag.jpg"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void paint(Graphics g){
		g.drawString("ⅠP  " + Hero.getHero1SumGrade(), 30-SystemInfo.tankWidth/2, 20);
		g.drawString("ⅡP  " + Hero.getHero2SumGrade(),  30-SystemInfo.tankWidth/2, 40);
		g.drawString("Enemy    Left", 30-SystemInfo.tankWidth/2, 70);
		
		for(int i = 0; i < SystemInfo.enemyTypeCount; ++i)
			battleEnemyNum[i] = 0;
		
		if(SystemInfo.mjpGame != null)
		{
			Vector<EnemyTank> battleEnemyTanks = SystemInfo.mjpGame.getEnemyTanks();
			for(int i = 0; i < battleEnemyTanks.size(); ++i){
				int idx = SystemInfo.getIntByTankType(battleEnemyTanks.get(i).getTankType());
				if(idx != -1)
					++battleEnemyNum[idx];
			}
				
		}
		for(int i = 0; i < SystemInfo.enemyTypeCount; ++i){
			enemys[i].drawTank(g);
			//各类型坦克数等于前方和后方相应类型坦克数总和
			g.drawString(String.valueOf(SystemInfo.rank.getEnemyTankNum()[i]+battleEnemyNum[i]), 30+SystemInfo.tankWidth/2+20, tankY[i]);
		}
		g.drawString("Hero", 30-SystemInfo.tankWidth/2, 290);
		hero1.drawTank(g);
		g.drawString(String.valueOf(Hero.getHero1Num()), 30+SystemInfo.tankWidth/2+20, tankY[4]);
		hero2.drawTank(g);
		g.drawString(String.valueOf(Hero.getHero2Num()), 30+SystemInfo.tankWidth/2+20, tankY[5]);
		g.drawImage(redFlag, 30-SystemInfo.tankWidth/2, tankY[5] + SystemInfo.tankHeight/2 + 5, 31, 34,this);
		String str_rank = String.valueOf(SystemInfo.rank.getCur_rank());
		if(TankGame.getMapFileName() != null)
			str_rank = "*";
		g.drawString(str_rank,30+SystemInfo.tankWidth/2+20,tankY[5]+SystemInfo.tankHeight/2 + 20);
	}
	
	private int[] battleEnemyNum = new int[SystemInfo.enemyTypeCount];	//战场各类型坦克数
	private EnemyTank[] enemys = new EnemyTank[SystemInfo.enemyTypeCount];
	private int tankY[] = new int[SystemInfo.enemyTypeCount+2];
	private Hero hero1 = null;
	private Hero hero2 = null;
	private Image redFlag = null;
}