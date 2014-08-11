package com.hugong.lgl;

import java.awt.Point;
import java.util.Vector;


class KeyBoarderThread implements Runnable{

	//清空键盘缓冲区
	public static void clearKeyBoarderBuffer(){
		for(int i = 0; i < SystemInfo.playKeyNum; ++i){
			SystemInfo.keyCode1[i] = SystemInfo.keyCode2[i] = 0;
		}
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub

		while(true){
//			if(SystemInfo.rank.getCount() <= 0 || SystemInfo.fail)
//			{
//				for(int i = 0; i < SystemInfo.playKeyNum; ++i){
//					SystemInfo.keyCode1[i] = SystemInfo.keyCode2[i] = 0;
//				}
//				//break;
//			}
			synchronized(SystemInfo.lock){
				while(SystemInfo.stop){
					try {
						SystemInfo.lock.wait();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
				
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
			if(SystemInfo.mjpGame != null){
				int i;
				Point pos = null;
				Bullet bullet = null;
				Vector<Bullet> bullets = SystemInfo.mjpGame.getBullets();
				if(Hero.getHero1Num() > 0){
					if(SystemInfo.mjpGame.getHero1() != null){
						Wealth wealth = SystemInfo.mjpGame.getHero1().getTouchWealth();
						if(wealth != null){
							//System.err.println("welth type:" + wealth.getWealthType());
							if(wealth.getWealthType() != -1)
							{
								++SystemInfo.mjpGame.getHero1().getWealth()[wealth.getWealthType()];
								SystemInfo.soundOperation.playSound("getwealth.wav", 2000);
							}
							wealth.setShow(false);
							synchronized(wealth){
								wealth.notify();
							}
						}
					}
					if(SystemInfo.keyCode1[SystemInfo.playKeyNum-1] != 0)	//选手一发射子弹键按下
					{
						if(SystemInfo.mjpGame.getHero1() != null && SystemInfo.mjpGame.getHero1().isCanShot()){
							SystemInfo.mjpGame.getHero1().setCanShot(false);
							pos = SystemInfo.mjpGame.getHero1().getCannonPipeLocation();
							bullet = new Bullet(SystemInfo.mjpGame.getHero1(),pos.x,pos.y,SystemInfo.mjpGame.getHero1().getDirect());
							if(SystemInfo.mjpGame.getHero1().getWealth()[5] > 0)	//该英雄拥有星星宝物
							{
								bullet.setRefreshInterval(bullet.getRefreshInterval()/2);	//子弹加速
							}
							bullets.add(bullet);
							new Thread(bullet).start();
							SystemInfo.soundOperation.playSound("shot.wav", 1000);
						}
						
						//System.err.println("player1 shot");
					}
					
					for(i = 0; i < SystemInfo.playKeyNum-1 && 0 == SystemInfo.keyCode1[i]; ++i)
						;
					switch(i){
					case 0:			//Up
						if(SystemInfo.mjpGame.getHero1() != null && !SystemInfo.mjpGame.getHero1().isTouchBorderAtGo() && !SystemInfo.mjpGame.getHero1().isTouchBarrierAtGo() && !SystemInfo.mjpGame.getHero1().isTouchOtherTankAtGo()){
							SystemInfo.mjpGame.getHero1().moveUp();
						}
						if(SystemInfo.mjpGame.getHero1() != null )
							SystemInfo.mjpGame.getHero1().setDirect(Direction.DIR_UP);
						//System.err.println("player1 Up");
						break;
					case 1:			//Down
						if(SystemInfo.mjpGame.getHero1() != null && !SystemInfo.mjpGame.getHero1().isTouchBorderAtGo() && !SystemInfo.mjpGame.getHero1().isTouchBarrierAtGo() && !SystemInfo.mjpGame.getHero1().isTouchOtherTankAtGo())
							SystemInfo.mjpGame.getHero1().moveDown();
						if(SystemInfo.mjpGame.getHero1() != null )
							SystemInfo.mjpGame.getHero1().setDirect(Direction.DIR_DOWN);
						//System.err.println("player1 Down");
						break;
					case 2:			//Left
						if(SystemInfo.mjpGame.getHero1() != null && !SystemInfo.mjpGame.getHero1().isTouchBorderAtGo() && !SystemInfo.mjpGame.getHero1().isTouchBarrierAtGo() && !SystemInfo.mjpGame.getHero1().isTouchOtherTankAtGo())
							SystemInfo.mjpGame.getHero1().moveLeft();
						if(SystemInfo.mjpGame.getHero1() != null )
							SystemInfo.mjpGame.getHero1().setDirect(Direction.DIR_LEFT);
						//System.err.println("player1 Left");
						break;
					case 3:			//Right
						if(SystemInfo.mjpGame.getHero1() != null && !SystemInfo.mjpGame.getHero1().isTouchBorderAtGo() && !SystemInfo.mjpGame.getHero1().isTouchBarrierAtGo() && !SystemInfo.mjpGame.getHero1().isTouchOtherTankAtGo())
							SystemInfo.mjpGame.getHero1().moveRight();
						if(SystemInfo.mjpGame.getHero1() != null )
							SystemInfo.mjpGame.getHero1().setDirect(Direction.DIR_RIGHT);
						//System.err.println("player1 Right");
						break;
					default:
						break;
					}
				}
				
				if(!SystemInfo.oneMan && Hero.getHero2Num() > 0)	//双人游戏
				{
					if(SystemInfo.mjpGame.getHero2() != null){
						Wealth wealth = SystemInfo.mjpGame.getHero2().getTouchWealth();
						if(wealth != null){
							if(wealth.getWealthType() != -1)
							{
								++SystemInfo.mjpGame.getHero2().getWealth()[wealth.getWealthType()];
								SystemInfo.soundOperation.playSound("getwealth.wav", 2000);
							}
							wealth.setShow(false);
							synchronized(wealth){
								wealth.notify();
							}
						}
					}
					
					if(SystemInfo.keyCode2[SystemInfo.playKeyNum-1] != 0)	//选手二发射子弹键按下
					{
						if(SystemInfo.mjpGame.getHero2() != null && SystemInfo.mjpGame.getHero2().isCanShot()){
							SystemInfo.mjpGame.getHero2().setCanShot(false);
							pos = SystemInfo.mjpGame.getHero2().getCannonPipeLocation();
							bullet = new Bullet(SystemInfo.mjpGame.getHero2(),pos.x,pos.y,SystemInfo.mjpGame.getHero2().getDirect());
							if(SystemInfo.mjpGame.getHero2().getWealth()[5] > 0)	//该英雄拥有星星宝物
							{
								bullet.setRefreshInterval(bullet.getRefreshInterval()/2);	//子弹加速
							}
							bullets.add(bullet);
							new Thread(bullet).start();
							SystemInfo.soundOperation.playSound("shot.wav", 1000);	
						}
					
						//System.err.println("player2 shot");
					}
					
					for(i = 0; i < SystemInfo.playKeyNum-1 && 0 == SystemInfo.keyCode2[i]; ++i)
						;
					switch(i){
					case 0:			//Up
						if(SystemInfo.mjpGame.getHero2() != null && !SystemInfo.mjpGame.getHero2().isTouchBorderAtGo() && !SystemInfo.mjpGame.getHero2().isTouchBarrierAtGo() && !SystemInfo.mjpGame.getHero2().isTouchOtherTankAtGo())
							SystemInfo.mjpGame.getHero2().moveUp();
						if(SystemInfo.mjpGame.getHero2() != null)
							SystemInfo.mjpGame.getHero2().setDirect(Direction.DIR_UP);
						//System.err.println("player2 Up");
						break;
					case 1:			//Down
						if(SystemInfo.mjpGame.getHero2() != null && !SystemInfo.mjpGame.getHero2().isTouchBorderAtGo() && !SystemInfo.mjpGame.getHero2().isTouchBarrierAtGo() && !SystemInfo.mjpGame.getHero2().isTouchOtherTankAtGo())
							SystemInfo.mjpGame.getHero2().moveDown();
						if(SystemInfo.mjpGame.getHero2() != null)
							SystemInfo.mjpGame.getHero2().setDirect(Direction.DIR_DOWN);
						//System.err.println("player2 Down");
						break;
					case 2:			//Left
						if(SystemInfo.mjpGame.getHero2() != null && !SystemInfo.mjpGame.getHero2().isTouchBorderAtGo() && !SystemInfo.mjpGame.getHero2().isTouchBarrierAtGo() && !SystemInfo.mjpGame.getHero2().isTouchOtherTankAtGo())
							SystemInfo.mjpGame.getHero2().moveLeft();
						if(SystemInfo.mjpGame.getHero2() != null)
							SystemInfo.mjpGame.getHero2().setDirect(Direction.DIR_LEFT);
						//System.err.println("player2 Left");
						break;
					case 3:			//Right
						if(SystemInfo.mjpGame.getHero2() != null && !SystemInfo.mjpGame.getHero2().isTouchBorderAtGo() && !SystemInfo.mjpGame.getHero2().isTouchBarrierAtGo() && !SystemInfo.mjpGame.getHero2().isTouchOtherTankAtGo())
							SystemInfo.mjpGame.getHero2().moveRight();
						if(SystemInfo.mjpGame.getHero2() != null)
							SystemInfo.mjpGame.getHero2().setDirect(Direction.DIR_RIGHT);
						//System.err.println("player2 Right");
						break;
					default:
						break;
					}
				}
				if(SystemInfo.mjpGame.getHero1()!= null)
					SystemInfo.mjpGame.getHero1().ajustLocation();
				if(!SystemInfo.oneMan && SystemInfo.mjpGame.getHero2() != null)
					SystemInfo.mjpGame.getHero2().ajustLocation();		
			}
		}
		
	}
}
