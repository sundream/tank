package com.hugong.lgl;

import java.awt.Color;
import java.awt.Point;

class EnemyTank extends Tank implements Runnable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4757003047605659446L;
	
	public EnemyTank(int x, int y,Direction direct,TankType tankType) {
		super(x, y, direct, tankType);
		// TODO Auto-generated constructor stub
		switch(tankType){
		case HIGH_ARMOUR:
			this.setColor(Color.gray);
			break;
		case MIDDLE_ARMOUR:
			this.setColor(Color.cyan); 
			break;
		case LOW_ARMOUR:
			this.setColor(Color.pink);
			break;
		case FAST_SPEED:
			this.setColor(Color.white);
			this.setStepLength(SystemInfo.tankStepLength*2);	//SystemInfo.unitLength == 4*SystemInfo.tankStepLength;
			this.setRefreshInterval(2*SystemInfo.tankRefreshInterval/3);
			break;
		default:
			break;
		}
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		Hero hero1,hero2;
		while(true){
			if(!this.isLive() || SystemInfo.fail)
				break;
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
			
			
			//英雄捡到定时宝物时，所有敌军坦克都定时SystemInfo.wealthValidTime秒
			hero1 = SystemInfo.mjpGame.getHero1();
			hero2 = SystemInfo.mjpGame.getHero2();
			synchronized(SystemInfo.enemyStopLock){
				while(hero1 != null && hero1.getWealth()[3] > 0 ||
						(!SystemInfo.oneMan && hero2 != null && hero2.getWealth()[3] > 0))
				{
					try {
						SystemInfo.enemyStopLock.wait(SystemInfo.wealthValidTime);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if(hero1 != null && hero1.getWealth()[3] > 0 )
						--hero1.getWealth()[3];
					if(!SystemInfo.oneMan && hero2 != null && hero2.getWealth()[3] > 0)
						--hero2.getWealth()[3];
				}
			}
			
			Bullet bullet = null;
			Point pos = null;
			//乘以mul是为了保证坦克每次行走距离尽量为SystemInfo.unitLength的整数倍
			//因为坦克遇到障碍物后不会移动
			int mul = SystemInfo.unitLength / this.getStepLength();
			int scope = 3 + (int)(Math.random()*8);
			int cnt = mul*scope;	//每次坦克沿它方向走mul*(3-10)步
			int shot = mul*(int)(Math.random()*scope);	
			int sum,maybe;
			//由于线程的调度，进入这里并不一定意味着坦克还活着，为了防止坦克挂了还行走或发子弹，在以下每个
			//循环中加一判断：this.isLive().当然由于程序刷新频率很快，即使坦克挂了后移动的一小步我们也很难看出
			switch(this.getDirect()){
			case DIR_UP:		//Direction.DIR_UP error??
				sum = 0;
				maybe = 1;
				for(int i = 0; this.isLive() && i < cnt; ++i){
					//为了在我军捡到暂停器时让敌军坦克立即暂停，增加条件判断
					if(hero1 != null && hero1.getWealth()[3] > 0 ||
						(!SystemInfo.oneMan && hero2 != null && hero2.getWealth()[3] > 0))
						break;
					if(!isTouchBorderAtGo() && !this.isTouchBarrierAtGo() && !this.isTouchOtherTankAtGo()){
							this.moveUp();
							++sum;
					}
					else if(this.isTouchBarrierAtGo())	
						maybe = i % (cnt/3);
					else if(this.isTouchOtherTankAtGo()) 
						maybe = i % (cnt/2);
					try {
						Thread.sleep(refreshInterval);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if(this.isCanShot() && ((sum == shot) || (0==maybe && sum % mul == 0))){
						this.setCanShot(false);
						maybe = 1;
						pos = this.getCannonPipeLocation();
						bullet = new Bullet(this,pos.x,pos.y,this.getDirect());
						SystemInfo.mjpGame.getBullets().add(bullet);
						new Thread(bullet).start();
					}
				}
				//System.err.println(Thread.currentThread().getName() + " Move Up");
				break;
			case DIR_RIGHT:
				sum = 0;
				maybe = 1;
				for(int i = 0; this.isLive() && i < cnt;++i){
					//为了在我军捡到暂停器时让敌军坦克立即暂停，增加条件判断
					if(hero1 != null && hero1.getWealth()[3] > 0 ||
						(!SystemInfo.oneMan && hero2 != null && hero2.getWealth()[3] > 0))
						break;
					if(!isTouchBorderAtGo() && !this.isTouchBarrierAtGo() && !this.isTouchOtherTankAtGo()){
						this.moveRight();
						++sum;
					}
						
					else if(this.isTouchBarrierAtGo())	
						maybe = i % (cnt/3);
					else if(this.isTouchOtherTankAtGo()) 
						maybe = i % (cnt/2);
					try {
						Thread.sleep(refreshInterval);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if(this.isCanShot() && ((sum == shot) || (0==maybe && sum % mul == 0))){
						this.setCanShot(false);
						maybe = 1;
						pos = this.getCannonPipeLocation();
						bullet = new Bullet(this,pos.x,pos.y,this.getDirect());
						SystemInfo.mjpGame.getBullets().add(bullet);
						new Thread(bullet).start();
					}
				}
				//System.err.println(Thread.currentThread().getName() + " Move Right");
				break;
			case DIR_DOWN:
				sum = 0;
				maybe = 1;
				for(int i = 0; this.isLive() && i < cnt; ++i){
					//为了在我军捡到暂停器时让敌军坦克立即暂停，增加条件判断
					if(hero1 != null && hero1.getWealth()[3] > 0 ||
						(!SystemInfo.oneMan && hero2 != null && hero2.getWealth()[3] > 0))
						break;
					if(!isTouchBorderAtGo() && !this.isTouchBarrierAtGo() && !this.isTouchOtherTankAtGo()){
						this.moveDown();
						++sum;
					}
					else if(this.isTouchBarrierAtGo())	
						maybe = i % (cnt/3);
					else if(this.isTouchOtherTankAtGo()) 
						maybe = i % (cnt/2);
					try {
						Thread.sleep(refreshInterval);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if(this.isCanShot() && ((sum == shot) || (0==maybe && sum % mul == 0))){
						this.setCanShot(false);
						maybe = 1;
						pos = this.getCannonPipeLocation();
						bullet = new Bullet(this,pos.x,pos.y,this.getDirect());
						SystemInfo.mjpGame.getBullets().add(bullet);
						new Thread(bullet).start();
					}
				}
				//System.err.println(Thread.currentThread().getName() + " Move Down");
				break;
			case DIR_LEFT:
				sum = 0;
				maybe = 1;
				for(int i = 0; this.isLive() && i < cnt; ++i){
					//为了在我军捡到暂停器时让敌军坦克立即暂停，增加条件判断
					if(hero1 != null && hero1.getWealth()[3] > 0 ||
						(!SystemInfo.oneMan && hero2 != null && hero2.getWealth()[3] > 0))
						break;
					if(!isTouchBorderAtGo() && !this.isTouchBarrierAtGo() && !this.isTouchOtherTankAtGo())
					{
						this.moveLeft();
						++sum;
					}
					else if(this.isTouchBarrierAtGo())	
						maybe = i % (cnt/3);
					else if(this.isTouchOtherTankAtGo()) 
						maybe = i % (cnt/2);
					try {
						Thread.sleep(refreshInterval);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if(this.isCanShot() && ((sum == shot) || (0==maybe && sum % mul == 0))){
						this.setCanShot(false);
						maybe = 1;
						pos = this.getCannonPipeLocation();
						bullet = new Bullet(this,pos.x,pos.y,this.getDirect());
						SystemInfo.mjpGame.getBullets().add(bullet);
						new Thread(bullet).start();
					}
				}
				//System.err.println(Thread.currentThread().getName() + " Move Left");
				break;
			default:
				//System.err.println("Direction error!");
				break;
			}
			this.ajustLocation();
			//这段代码有误，当双人游戏时某一方无人时，这是敌军永远得不到转向了
//			if(hero1 != null && hero1.getWealth()[3] <= 0 &&
//				(!SystemInfo.oneMan && hero2 != null && hero2.getWealth()[3] <= 0))
//			{
//				this.setDirect(SystemInfo.randomDirect());
//			}
			//正确逻辑如下
			if(SystemInfo.oneMan){	//单人游戏
				if(hero1 != null && hero1.getWealth()[3] <= 0)
					this.setDirect(SystemInfo.randomDirect());
			}
			else{		//双人游戏
				if(hero1 == null){   //英雄一无人了
					if(hero2 != null && hero2.getWealth()[3] <= 0)
						this.setDirect(SystemInfo.randomDirect());
				}
				else if(hero2 == null){ //英雄而无人了
					if(hero1 != null && hero1.getWealth()[3] <= 0)
						this.setDirect(SystemInfo.randomDirect());
				}
				else  //英雄一、二都有人
				{
					//虽然此时hero1,hero2都不会为空，但增加不为null判断总是种好习惯
					if(hero1 != null && hero1.getWealth()[3] <= 0 && hero2 != null && hero2.getWealth()[3] <= 0)
						this.setDirect(SystemInfo.randomDirect());
				}
			}
		
		}
	}
	public int getRefreshInterval() {
		return refreshInterval;
	}

	public void setRefreshInterval(int refreshInterval) {
		this.refreshInterval = refreshInterval;
	}
	private static final int DEFAULT_REFRESH_INTERVAL = SystemInfo.tankRefreshInterval;
	private int refreshInterval = DEFAULT_REFRESH_INTERVAL;		//刷新间隔
	
}
