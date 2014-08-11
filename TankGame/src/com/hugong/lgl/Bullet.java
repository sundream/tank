package com.hugong.lgl;

import java.awt.Color;
import java.awt.Graphics;
import java.io.Serializable;
import java.util.Vector;

class Bullet implements Runnable,Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 892163153734441402L;
	
	public Bullet(Tank belong,int x, int y, Direction direct) {
		super();
		this.belong = belong;
		this.x = x;
		this.y = y;
		this.direct = direct;
		//this.stepLength = DEFAULT_STEP_LENGTH;
		//this.refreshInterval = DEFAULT_REFRESH_INTERVAL;
	}
	
	public Bullet(Tank belong,int x, int y, Direction direct, int stepLength) {
		super();
		this.belong = belong;
		this.x = x;
		this.y = y;
		this.direct = direct;
		this.stepLength = stepLength;
		//this.refreshInterval = DEFAULT_REFRESH_INTERVAL;
	}
	
	public Bullet(Tank belong,int x, int y, Direction direct, int stepLength,
			int refreshInterval) {
		super();
		this.belong = belong;
		this.x = x;
		this.y = y;
		this.direct = direct;
		this.stepLength = stepLength;
		this.refreshInterval = refreshInterval;
	}
	
	public void drawBullet(Graphics g){
		Color oldColor = g.getColor();
		g.setColor(color);
		g.fillOval(x-bulletSize/2, y-bulletSize/2, bulletSize, bulletSize);
		g.setColor(oldColor);
	}
	public boolean isTouchBorderAtGo(){
		boolean ret = false;
		switch(direct){
		case DIR_UP:
			if(y-bulletSize/2 <= 0)
				ret = true;
			break;
		case DIR_RIGHT:
			if(x+bulletSize/2 >= SystemInfo.plWidth)
				ret = true;
			break;
		case DIR_DOWN:
			if(y+bulletSize/2 >= SystemInfo.plHeight)
				ret = true;
			break;
		case DIR_LEFT:
			if(x-bulletSize/2 <= 0)
				ret = true;
			break;
		default:
			break;
		}
		return ret;
	}
	
	//�Ƿ�Ӵ��ϰ���(�ӵ����Դ����������ϰ�����ӵ��������ϰ����м�ʱ)
	public Vector<Barrier> getTouchBarrierAtGo(){
		Vector<Barrier> ret = new Vector<Barrier>();
		synchronized(SystemInfo.mjpGame.getBarriers()){
			Vector<Barrier> barriers = SystemInfo.mjpGame.getBarriers();
			for(int i = barriers.size()-1; i >= 0; --i){
				if(isTouchOfBarrier(barriers.get(i)))
				{
					ret.add(barriers.get(i));
				}
			}
		}
		return ret;
	}
	
	//�Ƿ�Ӵ�̹��
	public Tank getTouchEnemyTankAtGo(){
		Tank ret = null;
		Tank hero1 = SystemInfo.mjpGame.getHero1();		//Hero����Ҳ��
		Tank hero2 = SystemInfo.mjpGame.getHero2();
		EnemyTank enemy = null;
		if(hero1 != null && hero1.isLive() && this.isTouchEnemyTank(hero1)){
			ret = hero1;
		}
		else if(!SystemInfo.oneMan && hero2 != null && hero2.isLive() && this.isTouchEnemyTank(hero2)){
			ret = hero2;
		}
		else	//�ж�����̹��
		{
			synchronized(SystemInfo.mjpGame.getEnemyTanks()){	//
				Vector<EnemyTank> vt = SystemInfo.mjpGame.getEnemyTanks();
				for(int i = vt.size() - 1; i >= 0; --i){
					enemy = vt.get(i);
					if(enemy.isLive() && this.isTouchEnemyTank(enemy))
					{
						ret = enemy;
						break;
					}
				}
			}
		}
		//System.err.println(ret + " " + this);
		return ret;
	}
	//�Ƿ�������ӵ��Ӵ�
	public Bullet getTouchEnemyBulletAtGo(){
		Bullet bullet = null;
		synchronized(SystemInfo.mjpGame.getBullets()){	
			Vector<Bullet> vt = SystemInfo.mjpGame.getBullets();
			for(int i = 0; i < vt.size(); ++i){
				if(vt.get(i).isLive && this.isTouchEnemyBullet(vt.get(i)))
				{
					bullet = vt.get(i);
					break;
				}
			}
		}
		return bullet;
	}
	//�ӵ��Ƿ�򵽼�԰
	public boolean isTouchHome(){
		boolean ret = false;
		int x = (SystemInfo.wh/2-1)*SystemInfo.unitLength;
		int y = (SystemInfo.wh-2)*SystemInfo.unitLength;
		int wh = SystemInfo.unitLength * 2;
		if(this.isLive && x <= this.x && this.x <= x + wh && y <= this.y && this.y <= y + wh)
		{
			ret = true;
		}
		return ret;
	}
	
	//�Ƿ��ָ���ϰ���Ӵ����ϰ���Ļ�׼������̹�˲�ͬ���ж�ʱ��Щ��仯��
	private boolean isTouchOfBarrier(Barrier barrier){
		boolean ret = false;
		int wh = barrier.getWidth();	//�ϰ�����
		//ֻ���ж��ӵ��Ƿ��ڲ�����ͨ���ӵ���barrier�ķ�Χ��(�����߽�)
		BarrierType bt = barrier.getBarrierType();
		if(bt != BarrierType.BOTH_GO && bt != BarrierType.ONLY_BULLET){
			if(this.isLive() && barrier.getX() <= this.x && this.x <= barrier.getX()+wh && barrier.getY()<= this.y && this.y <= barrier.getY()+wh){
				ret = true;
			}
		}
//		if(ret != false){
//			//System.err.println(barrier.getBarrierType() + " x:" + barrier.getX() + ",y:" + barrier.getY() + ",wh:" + barrier.getWidth());
//		}
		return ret;
	}
	
	//�Ƿ��ָ��̹�˽Ӵ�
	private boolean isTouchEnemyTank(Tank other){
		boolean ret = false;
		int wh = other.getWidth() / 2;	//̹�˿�ߵ�һ��
		//ֻ���ж��ӵ��Ƿ���other�ķ�Χ��(�����߽�)
		//һ��Ҫ�ų��ӵ������ߵ�ͬ�࣬������ӵ������Լ���ͬ��
		if(this.isLive() && !this.isFriend(other) && other.getX()-wh <= this.x && this.x <= other.getX()+wh && other.getY()-wh <= this.y && this.y <= other.getY()+wh){
			ret = true;
		}
//		if(ret)
//			System.err.println(other.getTankType() + " x:" + other.getX() + ",y:" + other.getY());
		return ret;
	}
	
	//�Ƿ�Ӵ�ָ���ӵ�
	private boolean isTouchEnemyBullet(Bullet other){
		boolean ret = false;
		//�ų����������ӵ�����������ײ
		if(this != other && this.isLive() && !this.isFriend(other.belong) && this.x == other.getX() && this.y == other.getY())
			ret = true;
		return ret;
	}
	
	private boolean isFriend(Tank other){
		boolean ret = false;
		if(this.belong.getTankType() == TankType.HERO && other.getTankType() == TankType.HERO)
			ret = true;
		else if(this.belong.getTankType() != TankType.HERO && other.getTankType() != TankType.HERO)
			ret = true;
		return ret;	
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
	public Direction getDirect() {
		return direct;
	}
	public void setDirect(Direction direct) {
		this.direct = direct;
	}
	public int getStepLength() {
		return stepLength;
	}
	public void setStepLength(int stepLength) {
		this.stepLength = stepLength;
	}
	public int getRefreshInterval() {
		return refreshInterval;
	}
	public void setRefreshInterval(int refreshInterval) {
		this.refreshInterval = refreshInterval;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
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
			
			try {
				Thread.sleep(refreshInterval);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
			Vector<Barrier> barriers = null;
			barriers = this.getTouchBarrierAtGo();
			if(barriers.size() > 0){
				for(int i = barriers.size()-1; i >= 0; --i){
					if((this.belong instanceof Hero)){	//Ӣ��̹�˵�������
						Hero hero = (Hero)this.belong;
						if(hero.getWealth()[5] >= 2)	//��Ӣ��ӵ��2�����ϵ����Ǳ�����Դ����ˣ�
						{
							barriers.get(i).setLive(false);
						}
						else{
							if(BarrierType.BOTH_FORBIN1 == barriers.get(i).getBarrierType())//�ӵ�ֻ�ܴ����ǽ�ϰ���
							{
								barriers.get(i).setLive(false);
							}
						}
					}
					else{							//�о�̹��
						if(BarrierType.BOTH_FORBIN1 == barriers.get(i).getBarrierType())//�ӵ�ֻ�ܴ����ǽ�ϰ���
						{
							barriers.get(i).setLive(false);
						}
					}
			
				}
				this.setLive(false);
				this.belong.setCanShot(true);
				break;
			}
			Bullet bullet = this.getTouchEnemyBulletAtGo();
			if(bullet != null){
				//System.err.println( "touch bullet x:" + bullet.getX() + ",y:" + bullet.getY());
				bullet.setLive(false);
				bullet.getBelong().setCanShot(true);  //���������䣬���������̵߳��ȿ����������ӵ��������߲����ٿ�ǹ
				this.setLive(false);
				this.belong.setCanShot(true);
				break;
			}
			Tank tank = this.getTouchEnemyTankAtGo();
			if(tank != null){
				//System.err.println(tank.getTankType() + " x:" + tank.getX() + ",y:" + tank.getY());
				//tank.setLive(false);
				tank.minusLife();
				if((tank instanceof EnemyTank) && tank.isLive())	//�о��ֵ�������ӵ�
				{
					SystemInfo.soundOperation.playSound("defense.wav", 1000);
				}
				if((tank instanceof Hero) && !tank.isLive())	//Ӣ�۹���
				{
					SystemInfo.soundOperation.playSound("herodeath.wav", 1000);
				}
				this.setLive(false);
				this.belong.setCanShot(true);
				if(this.belong instanceof Hero)	//Hero̹��
				{
					if(!tank.isLive())	//�Ҿ����������̹��
					{
						Hero.addHeroKilledEnemyOfType((Hero)this.belong,tank.getTankType());
						SystemInfo.soundOperation.playSound("enemydeath.wav", 1000);
					}
				}
				break;
			}
			if(this.isTouchBorderAtGo()){
				//System.err.println("touch border");
				this.setLive(false);
				this.belong.setCanShot(true);
				break;
			}
			if(this.isTouchHome()){
				SystemInfo.fail = true;
				break;
			}
			
			switch(direct){
			case DIR_UP:
				y -= stepLength;
				break;
			case DIR_RIGHT:
				x += stepLength;
				break;
			case DIR_DOWN:
				y += stepLength;
				break;
			case DIR_LEFT:
				x -= stepLength;
				break;
			default :
				break;
			}
			//System.err.println(this.belong + " bullet's location x:" + x + ",y:" + y);

		}
	}

	public Tank getBelong() {
		return belong;
	}

	public void setBelong(Tank belong) {
		this.belong = belong;
	}

	public boolean isLive() {
		return isLive;
	}

	public void setLive(boolean isLive) {
		this.isLive = isLive;
	}
	
	private static final int DEFAULT_BULLET_SIZE = SystemInfo.bulletSize;
	private static final int DEFAULT_REFRESH_INTERVAL = SystemInfo.bulletRefreshInterval;
	private static final int DEFAULT_STEP_LENGTH = SystemInfo.bulletStepLength;
	private Tank belong = null;		//�ӵ���������̹��
	private int x;
	private int y;
	private Direction direct;
	private boolean isLive = true;
	private Color color = Color.white;
	private int bulletSize = DEFAULT_BULLET_SIZE;
	private int stepLength = DEFAULT_STEP_LENGTH;
	private int refreshInterval = DEFAULT_REFRESH_INTERVAL;

}
