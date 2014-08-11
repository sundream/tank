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
			
			
			//Ӣ�ۼ񵽶�ʱ����ʱ�����ео�̹�˶���ʱSystemInfo.wealthValidTime��
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
			//����mul��Ϊ�˱�֤̹��ÿ�����߾��뾡��ΪSystemInfo.unitLength��������
			//��Ϊ̹�������ϰ���󲻻��ƶ�
			int mul = SystemInfo.unitLength / this.getStepLength();
			int scope = 3 + (int)(Math.random()*8);
			int cnt = mul*scope;	//ÿ��̹������������mul*(3-10)��
			int shot = mul*(int)(Math.random()*scope);	
			int sum,maybe;
			//�����̵߳ĵ��ȣ��������ﲢ��һ����ζ��̹�˻����ţ�Ϊ�˷�ֹ̹�˹��˻����߻��ӵ���������ÿ��
			//ѭ���м�һ�жϣ�this.isLive().��Ȼ���ڳ���ˢ��Ƶ�ʺܿ죬��ʹ̹�˹��˺��ƶ���һС������Ҳ���ѿ���
			switch(this.getDirect()){
			case DIR_UP:		//Direction.DIR_UP error??
				sum = 0;
				maybe = 1;
				for(int i = 0; this.isLive() && i < cnt; ++i){
					//Ϊ�����Ҿ�����ͣ��ʱ�õо�̹��������ͣ�����������ж�
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
					//Ϊ�����Ҿ�����ͣ��ʱ�õо�̹��������ͣ�����������ж�
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
					//Ϊ�����Ҿ�����ͣ��ʱ�õо�̹��������ͣ�����������ж�
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
					//Ϊ�����Ҿ�����ͣ��ʱ�õо�̹��������ͣ�����������ж�
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
			//��δ������󣬵�˫����Ϸʱĳһ������ʱ�����ǵо���Զ�ò���ת����
//			if(hero1 != null && hero1.getWealth()[3] <= 0 &&
//				(!SystemInfo.oneMan && hero2 != null && hero2.getWealth()[3] <= 0))
//			{
//				this.setDirect(SystemInfo.randomDirect());
//			}
			//��ȷ�߼�����
			if(SystemInfo.oneMan){	//������Ϸ
				if(hero1 != null && hero1.getWealth()[3] <= 0)
					this.setDirect(SystemInfo.randomDirect());
			}
			else{		//˫����Ϸ
				if(hero1 == null){   //Ӣ��һ������
					if(hero2 != null && hero2.getWealth()[3] <= 0)
						this.setDirect(SystemInfo.randomDirect());
				}
				else if(hero2 == null){ //Ӣ�۶�������
					if(hero1 != null && hero1.getWealth()[3] <= 0)
						this.setDirect(SystemInfo.randomDirect());
				}
				else  //Ӣ��һ����������
				{
					//��Ȼ��ʱhero1,hero2������Ϊ�գ������Ӳ�Ϊnull�ж������ֺ�ϰ��
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
	private int refreshInterval = DEFAULT_REFRESH_INTERVAL;		//ˢ�¼��
	
}
