package com.hugong.lgl;

import java.awt.*;
import java.io.Serializable;
import java.util.Vector;

enum Direction{DIR_UP,DIR_RIGHT,DIR_DOWN,DIR_LEFT,}
enum TankType{UNDEFINED,HERO,LOW_ARMOUR,MIDDLE_ARMOUR,FAST_SPEED,HIGH_ARMOUR,}

class Tank implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7963350668404904867L;
	
	public Tank(int x, int y,  Direction direct,TankType tankType, Color color,
			boolean canShot, boolean live, int width, int height, int stepLength) {
		super();
		this.x = x;
		this.y = y;
		this.direct = direct;
		this.tankType = tankType;
		this.color = color;
		this.canShot = canShot;
		this.live = live;
		this.width = width;
		this.height = height;
		this.stepLength = stepLength;
	}

	public Tank(int x, int y, Direction direct, TankType tankType) {
		super();
		this.x = x;
		this.y = y;
		this.direct = direct;
		this.tankType = tankType;
	}
	
	public Tank(int x, int y, Direction direct, TankType tankType,Color color) {
		super();
		this.x = x;
		this.y = y;
		this.color = color;
		this.direct = direct;
		this.tankType = tankType;
	}

	public void drawTank(Graphics g){
		Color oldColor = g.getColor();
		g.setColor(color);
		switch(direct){
		case DIR_UP:
			g.fill3DRect(x-width/2, y-height/2, width/4, height, false);	//�����
//			g.drawLine(x-width/2, y-height/3, x-width/4, y-height/3);
//			g.drawLine(x-width/2, y, x-width/4, y);
//			g.drawLine(x-width/2, y+height/3, x-width/4, y+height/3);
			g.fill3DRect(x-width/2, y-height/3, width/4, 2, true);
			g.fill3DRect(x-width/2, y-height/6, width/4, 2, true);
			g.fill3DRect(x-width/2, y, width/4, 2, true);
			g.fill3DRect(x-width/2, y+height/6, width/4, 2,true);
			g.fill3DRect(x-width/2, y+height/3, width/4, 2,true);
			
			g.fill3DRect(x+width/4,y-height/2,width/4,height,false);		//�ҳ���
//			g.drawLine(x+width/2, y-height/3, x+width/4, y-height/3);
//			g.drawLine(x+width/2, y, x+width/4, y);
//			g.drawLine(x+width/2, y+height/3, x+width/4, y+height/3);
			g.fill3DRect(x+width/4, y-height/3, width/4, 2, true);
			g.fill3DRect(x+width/4, y-height/6, width/4, 2, true);
			g.fill3DRect(x+width/4, y, width/4, 2, true);
			g.fill3DRect(x+width/4, y+height/6, width/4, 2, true);
			g.fill3DRect(x+width/4, y+height/3, width/4, 2,true);
			
			g.fill3DRect(x-width/4, y-height/3, width/2, 2*height/3, false);	//����
			g.fillOval(x-width/4, y-height/6, width/2, height/3);			//Բ�����
			//g.drawLine(x, y, x, y-height/2);			//�ڹ�
			if(enemyWealth != -1)
				g.setColor(Color.red);
			g.fill3DRect(x-width/20, y-height/2, width/10, height/2, false); //�ڹ�	
			break;
		case DIR_RIGHT:
			g.fill3DRect(x-height/2, y-width/2, height, width/4, false);	//�����
			g.fill3DRect(x-height/3, y-width/2, 2, width/4, true);
			g.fill3DRect(x-height/6, y-width/2, 2, width/4, true);
			g.fill3DRect(x, y-width/2, 2, width/4, true);
			g.fill3DRect(x+height/6, y-width/2, 2, width/4, true);
			g.fill3DRect(x+height/3, y-width/2, 2, width/4, true);

			g.fill3DRect(x-height/2,y+width/4,height,width/4,false);		//�ҳ���
			g.fill3DRect(x-height/3, y+width/4, 2, width/4, true);
			g.fill3DRect(x-height/6, y+width/4, 2, width/4, true);
			g.fill3DRect(x, y+width/4, 2, width/4, true);
			g.fill3DRect(x+height/6, y+width/4, 2, width/4, true);
			g.fill3DRect(x+height/3, y+width/4, 2, width/4, true);
			
			g.fill3DRect(x-height/3, y-width/4, 2*height/3, width/2, false);	//����
			g.fillOval(x-height/6, y-width/4, height/3, width/2);			//Բ�����
			//g.drawLine(x, y, x+height/2, y); 								//�ڹ�
			if(enemyWealth != -1)
				g.setColor(Color.red);
			g.fill3DRect(x, y-width/20, height/2, width/10, false); //�ڹ�	
			break;
		case DIR_DOWN:
			g.fill3DRect(x-width/2, y-height/2, width/4, height, false);	//�����
			g.fill3DRect(x-width/2, y-height/3, width/4, 2, true);
			g.fill3DRect(x-width/2, y-height/6, width/4, 2, true);
			g.fill3DRect(x-width/2, y, width/4, 2, true);
			g.fill3DRect(x-width/2, y+height/6, width/4, 2,true);
			g.fill3DRect(x-width/2, y+height/3, width/4, 2,true);
			
			g.fill3DRect(x+width/4,y-height/2,width/4,height,false);		//�ҳ���
			g.fill3DRect(x+width/4, y-height/3, width/4, 2, true);
			g.fill3DRect(x+width/4, y-height/6, width/4, 2, true);
			g.fill3DRect(x+width/4, y, width/4, 2, true);
			g.fill3DRect(x+width/4, y+height/6, width/4, 2, true);
			g.fill3DRect(x+width/4, y+height/3, width/4, 2,true);
			
			g.fill3DRect(x-width/4, y-height/3, width/2, 2*height/3, false);	//����
			g.fillOval(x-width/4, y-height/6, width/2, height/3);			//Բ�����
			//g.drawLine(x, y, x, y+height/2);								//�ڹ�
			if(enemyWealth != -1)
				g.setColor(Color.red);
			g.fill3DRect(x-width/20, y, width/10, height/2, false); //�ڹ�	
			break;
		case DIR_LEFT:
			g.fill3DRect(x-height/2, y-width/2, height, width/4, false);	//�����
			g.fill3DRect(x-height/3, y-width/2, 2, width/4, true);
			g.fill3DRect(x-height/6, y-width/2, 2, width/4, true);
			g.fill3DRect(x, y-width/2, 2, width/4, true);
			g.fill3DRect(x+height/6, y-width/2, 2, width/4, true);
			g.fill3DRect(x+height/3, y-width/2, 2, width/4, true);
			
			g.fill3DRect(x-height/2,y+width/4,height,width/4,false);		//�ҳ���
			g.fill3DRect(x-height/3, y+width/4, 2, width/4, true);
			g.fill3DRect(x-height/6, y+width/4, 2, width/4, true);
			g.fill3DRect(x, y+width/4, 2, width/4, true);
			g.fill3DRect(x+height/6, y+width/4, 2, width/4, true);
			g.fill3DRect(x+height/3, y+width/4, 2, width/4, true);
			
			g.fill3DRect(x-height/3, y-width/4, 2*height/3, width/2, false);	//����
			g.fillOval(x-height/6, y-width/4, height/3, width/2);			//Բ�����
			//g.drawLine(x, y, x-height/2, y);								//�ڹ�
			if(enemyWealth != -1)
				g.setColor(Color.red);
			g.fill3DRect(x-height/2, y-width/20, height/2, width/10, false); 	//�ڹ�	
			break;
		default :
			break;
		}
		g.setColor(oldColor);
	}
	
	public void ajustLocation(){
		//����tank�����꣬��֤��ΪSystemInfo.unitLength�ı���
		int remainder = 0;
		switch(this.direct){
		case DIR_UP:
			remainder = this.getY()%SystemInfo.unitLength ;
			if(remainder != 0){
				this.setY(this.getY()-remainder);
			}
			break;

		case DIR_RIGHT:
			remainder = this.getX()%SystemInfo.unitLength;
			if(remainder != 0){
				this.setX(this.getX()+SystemInfo.unitLength-remainder);
			}
			break;
		case DIR_DOWN:
			remainder = this.getY()%SystemInfo.unitLength ;
			if(remainder != 0){
				this.setY(this.getY()+SystemInfo.unitLength-remainder);
			}
			break;

		case DIR_LEFT:
			remainder = this.getX()%SystemInfo.unitLength;
			if(remainder != 0){
				this.setX(this.getX()-remainder);
			}
			break;

		default:
			break;
		}
	}
	
	public boolean isTouchBorderAtGo(){
		boolean ret = false;
		switch(direct){
		case DIR_UP:
			if(y-height/2 <= 0)
				ret = true;
			break;
		case DIR_RIGHT:
			if(x+height/2 >= SystemInfo.plWidth)
				ret = true;
			break;
		case DIR_DOWN:
			if(y+height/2 >= SystemInfo.plHeight)
				ret = true;
			break;
		case DIR_LEFT:
			if(x-height/2 <= 0)
				ret = true;
			break;
		default:
			break;
		}
		return ret;
	}
	
	//ǰ�������Ƿ�Ӵ��ϰ���
	public boolean isTouchBarrierAtGo(){
		boolean ret = false;
		synchronized(SystemInfo.mjpGame.getBarriers()){
			Vector<Barrier> barriers = SystemInfo.mjpGame.getBarriers();
			for(int i = 0; i < barriers.size(); ++i){
				if(barriers.get(i).isLive() && BarrierType.BOTH_GO != barriers.get(i).getBarrierType() && isTouchOfBarrier(barriers.get(i)))
				{
					ret = true;
					break;
				}
			}
		}
		return ret;
	}
	
	//ǰ�������Ƿ�Ӵ�����̹��
	public boolean isTouchOtherTankAtGo(){
		boolean ret = false;
		Tank hero1 = SystemInfo.mjpGame.getHero1(); 	//Hero ����Ҳ��
		Tank hero2 = SystemInfo.mjpGame.getHero2();
		EnemyTank enemy = null;
		Vector<EnemyTank> vt = SystemInfo.mjpGame.getEnemyTanks();
		//���¼���x,y��ȫ��ȳ��ֽӴ���Ϊ����Ϊ�˱���̹�˳�ʼ����ʱλ���ѱ�����̹��ռ��
		if(this == hero1){		//Ӣ��1
			if(!SystemInfo.oneMan &&  hero2 != null && hero2.isLive() && this.isTouchOfTank(hero2)){
				if(this.x != hero2.getX() || this.y != hero2.getY())
					ret = true;
				//System.err.println("hero1 touch hero2 x:" + hero2.getX() + ",y:" + hero2.getY() + ",direct:" +hero2.getDirect());
			}
			else{
				synchronized(SystemInfo.mjpGame.getEnemyTanks()){
					vt = SystemInfo.mjpGame.getEnemyTanks();
					for(int i = 0; i < vt.size(); ++i){
						enemy = vt.get(i);
						if(enemy.isLive() && this.isTouchOfTank(enemy)){
							if(this.x != enemy.getX() || this.y != enemy.getY()){
								ret = true;
								break;
							}
							//System.err.println("hero1 touch enemy x:" + enemy.getX() + ",y:" + enemy.getY() + ",direct:" +enemy.getDirect());	
						}
					}
				}
		
			}
		}
		else if(!SystemInfo.oneMan && hero2 == this){ 	//Ӣ��2
			if(hero1 != null && hero1.isLive() && this.isTouchOfTank(hero1)){
				if(this.x != hero1.getX() || this.y != hero1.getY())
					ret = true;	
				//System.err.println("hero2 touch hero1 x:" + hero1.getX() + ",y:" + hero1.getY() + ",direct:" +hero1.getDirect());
			}
			else{
				synchronized(SystemInfo.mjpGame.getEnemyTanks()){
					vt = SystemInfo.mjpGame.getEnemyTanks();
					for(int i = 0; i < vt.size(); ++i){
						enemy = vt.get(i);
						if(enemy.isLive() && this.isTouchOfTank(enemy)){
							if(this.x != enemy.getX() || this.y != enemy.getY()){
								ret = true;
								break;
							}
						}
					}
				}
	
			}
		}
		else{		//�л�
			if(hero1 != null && hero1.isLive() && this.isTouchOfTank(hero1)){
				if(this.x != hero1.getX() || this.y != hero1.getY()){
						ret = true;
				}
					
			}	
			else if(!SystemInfo.oneMan &&  hero2 != null && hero2.isLive() && this.isTouchOfTank(hero2)){
				if(this.x != hero2.getX() || this.y != hero2.getY()){
						ret = true;
				}
			}
				
			else{
				synchronized(SystemInfo.mjpGame.getEnemyTanks()){
					vt = SystemInfo.mjpGame.getEnemyTanks();
					//Ϊ��ֹ�о��໥֮���ڳ���λ�����ұ���ס�����ǽ��ڳ���λ�õĵо��������ϰ���
					if(!this.isAtTankBorthLoctation()){
						for(int i = 0; i < vt.size(); ++i){
							enemy = vt.get(i);
							if(!enemy.isAtTankBorthLoctation() && this != enemy && enemy.isLive() && this.isTouchOfTank(enemy)){
								if(this.x != enemy.getX() || this.y != enemy.getY())
								{
									ret = true;
									break;
								}
							}
						}
					}
					
				}
			}
		}
		return ret;
	}
	
	//�ж�̹���Ƿ���̹�˳�ʼ����λ��
	public boolean isAtTankBorthLoctation(){
		boolean ret = false;
//		if(this.x == SystemInfo.hero1Location.x && this.y == SystemInfo.hero1Location.y)
//			return true;
//		if(this.x == SystemInfo.hero2Location.x && this.y == SystemInfo.hero2Location.y)
//			return true;
		
		for(int i = 0; i < 3; ++i){
			if(this.x == SystemInfo.enmyLocation[i].x && this.y == SystemInfo.enmyLocation[i].y){
				ret = true;
				break;
			}
		}
		
		return ret;
	}
	
	//ǰ�������Ƿ��ָ��̹�˽Ӵ�
	private boolean isTouchOfTank(Tank other){
		boolean ret = false;
		Point p1 = new Point();
		Point p2 = new Point();
		Point pCannon = this.getCannonPipeLocation();
		//����̹��Ϊ�����Σ�������Ӵ��ж��뱻�Ӵ�̹�˷����޹��ˣ�ֻ����̹���ƶ������й�
		switch(this.direct){
		case DIR_UP:
			p1.x = x - width / 2 + this.stepLength;
			p1.y = y - height / 2;
			p2.x = x + width / 2 - this.stepLength;
			p2.y = y - height / 2;
			break;
		case DIR_RIGHT:
			p1.x = x + height / 2;
			p1.y = y - width / 2 + this.stepLength;
			p2.x = x + height / 2;
			p2.y = y + width / 2 - this.stepLength;
			break;
		case DIR_DOWN:
			p1.x = x - width / 2 + this.stepLength;
			p1.y = y + height / 2;
			p2.x = x + width / 2 - this.stepLength;
			p2.y = y + height / 2;
			break;
		case DIR_LEFT:
			p1.x = x - height / 2;
			p1.y = y - width / 2 + this.stepLength;
			p2.x = x - height / 2;
			p2.y = y + width / 2 - this.stepLength;
			break;
		default:
			break;
		}
		//�ж��������Ƿ���ָ��̹�˷�Χ��
		int wh = other.getWidth() / 2;
		if(other.getX()-wh <= p1.x && p1.x <= other.getX()+wh && other.getY()-wh <= p1.y && p1.y <= other.getY()+wh){
			ret = true;
		}
		else if(other.getX()-wh <= p2.x && p2.x <= other.getX()+wh && other.getY()-wh <= p2.y && p2.y <= other.getY()+wh){
			ret = true;
		}
		else if(other.getX()-wh <= pCannon.x && pCannon.x <= other.getX()+wh && other.getY()-wh <= pCannon.y && pCannon.y <= other.getY()+wh){
			ret = true;
		}
		return ret;
	}
	

	//ǰ�������Ƿ��ָ���ϰ���Ӵ����ϰ���Ļ�׼������̹�˲�ͬ���ж�ʱ��Щ��仯��
	private boolean isTouchOfBarrier(Barrier barrier){
		boolean ret = false;
		Point p1 = new Point();
		Point p2 = new Point();
		Point pCannon = this.getCannonPipeLocation();
		switch(this.direct){
		case DIR_UP:
			p1.x = x - width / 2 + this.stepLength;
			p1.y = y - height / 2;
			p2.x = x + width / 2 - this.stepLength;
			p2.y = y - height / 2;
			break;
		case DIR_RIGHT:
			p1.x = x + height / 2;
			p1.y = y - width / 2 + this.stepLength;
			p2.x = x + height / 2;
			p2.y = y + width / 2 - this.stepLength;
			break;
		case DIR_DOWN:
			p1.x = x - width / 2 + this.stepLength;
			p1.y = y + height / 2;
			p2.x = x + width / 2 - this.stepLength;
			p2.y = y + height / 2;
			break;
		case DIR_LEFT:
			p1.x = x - height / 2;
			p1.y = y - width / 2 + this.stepLength;
			p2.x = x - height / 2;
			p2.y = y + width / 2 - this.stepLength;
			break;
		default:
			break;
		}
		int wh = barrier.getWidth();	//�ϰ�����
		
		if(barrier.getX() <= p1.x && p1.x <= barrier.getX()+wh && barrier.getY()<= p1.y && p1.y <= barrier.getY()+wh){
			ret = true;
		}
		else if(barrier.getX() <= p2.x && p2.x <= barrier.getX()+wh && barrier.getY()<= p2.y && p2.y <= barrier.getY()+wh){
			ret = true;
		}
		else if(barrier.getX() <= pCannon.x && pCannon.x <= barrier.getX()+wh && barrier.getY() <= pCannon.y && pCannon.y <= barrier.getY()+wh){
			ret = true;
		}
		return ret;
	}
	
/*	
	//ǰ�������Ƿ��ָ���ϰ���Ӵ����ϰ���Ļ�׼������̹�˲�ͬ���ж�ʱ��Щ��仯��
	private boolean isTouchOfBarrier(Barrier barrier){
		boolean ret = false;
		Point pCannon = this.getCannonPipeLocation();
		int wh = barrier.getWidth();	//�ϰ�����
		//ֻ���ж���Ͳ���Ƿ���other�ķ�Χ��(�����߽�)
		if(barrier.getX() <= pCannon.x && pCannon.x <= barrier.getX()+wh && barrier.getY()<= pCannon.y && pCannon.y <= barrier.getY()+wh){
			ret = true;
		}
		return ret;
	}
	
	
	//ǰ�������Ƿ��ָ��̹�˽Ӵ�
	private boolean isTouchOfTank(Tank other){
		boolean ret = false;
		Point pCannon = this.getCannonPipeLocation();
		int wh = other.getWidth() / 2;	//̹�˿�ߵ�һ��
		//ֻ���ж���Ͳ���Ƿ���other�ķ�Χ��(�����߽�),������ȷ����ż�������
		if(other.getX()-wh <= pCannon.x && pCannon.x <= other.getX()+wh && other.getY()-wh <= pCannon.y && pCannon.y <= other.getY()+wh){
			ret = true;
		}
		return ret;
	}*/
	
	public Point getCannonPipeLocation(){
		Point ret = new Point();
		switch(direct){
		case DIR_UP:
			ret.x = x;
			ret.y = y - height / 2;
			break;
		case DIR_RIGHT:
			ret.x = x + height / 2;
			ret.y = y;
			break;
		case DIR_DOWN:
			ret.x = x;
			ret.y = y + height / 2;
			break;
		case DIR_LEFT:
			ret.x = x - height / 2;
			ret.y = y;
			break;
		default :
			break;
		}
		return ret;
	}
	
	public void minusLife(){
		if(TankType.HIGH_ARMOUR == this.tankType)
		{
			if(this.color.equals(Color.gray))
				this.color = Color.cyan;
			else if(this.color.equals(Color.cyan))
				this.color = Color.pink;
			else if(this.color.equals(Color.pink))
				this.live = false;
		}
		else if(TankType.MIDDLE_ARMOUR == this.tankType)
		{
			
			if(this.color.equals(Color.cyan))
			{
				this.color = Color.pink;
			}
			else if(this.color.equals(Color.pink))
				this.live = false;
		}
		else if((this instanceof Hero) && StateType.UNDEATH == ((Hero)this).getState()) 
		{
			//�����޵�״̬��Ӣ�۲���������
		}
		else	//LOW_ARMOUR,FAST_SPEED,HERO̹��������һǹ��ֱ�ӹҵ�
		{
			this.live = false;
		}

		if(this.enemyWealth >= 0){
			int x = (int)(Math.random()*(SystemInfo.plWidth-SystemInfo.wealthWidth));
			int y = (int)(Math.random()*(SystemInfo.plHeight-SystemInfo.wealthHeight));
			Wealth wealth = new Wealth(x,y,this.enemyWealth);
			SystemInfo.mjpGame.getWealths().add(wealth);
			new Thread(wealth).start();
			SystemInfo.soundOperation.playSound("showwealth.wav", 2000);
			this.enemyWealth = -1;		//Я������ĵо�̹�˴�һǹ��(���ֱ���)��û������
		}
		
	}
	
	public void moveUp(){
		if(direct == Direction.DIR_UP)
			setY(getY()-getStepLength());
		setDirect(Direction.DIR_UP);
	}
	public void moveRight(){
		if(direct == Direction.DIR_RIGHT)
			setX(getX()+getStepLength());
		setDirect(Direction.DIR_RIGHT);
	}
	
	public void moveDown(){
		if(direct == Direction.DIR_DOWN)
			setY(getY()+getStepLength());
		setDirect(Direction.DIR_DOWN);
	}
	
	public void moveLeft(){
		if(direct == Direction.DIR_LEFT)
			setX(getX()-getStepLength());
		setDirect(Direction.DIR_LEFT);
	}
	
	public boolean isLive() {
		return live;
	}

	public void setLive(boolean live) {
		this.live = live;
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

	public int getStepLength() {
		return stepLength;
	}

	public void setStepLength(int stepLength) {
		this.stepLength = stepLength;
	}
	public Direction getDirect() {
		return direct;
	}

	public void setDirect(Direction direct) {
		this.direct = direct;
	}

	public TankType getTankType() {
		return tankType;
	}


	public void setTankType(TankType tankType) {
		this.tankType = tankType;
	}
	public boolean isCanShot() {
		return canShot;
	}

	public void setCanShot(boolean canShot) {
		this.canShot = canShot;
	}
	


	public int getEnemyWealth() {
		return enemyWealth;
	}

	public void setEnemyWealth(int enemyWealth) {
		this.enemyWealth = enemyWealth;
	}



	private static final int DEFAULT_STEP_LENGTH = SystemInfo.tankStepLength;
	private int enemyWealth = -1;	//��̹���Ƿ�Я������
	private int x,y;
	private Color color;
	private Direction direct;		//0:up,1:right,2:down,3:left
	private TankType tankType = TankType.UNDEFINED;
	private boolean canShot = true;
	private boolean live = true;
	private int width = SystemInfo.tankWidth;
	private int height = SystemInfo.tankHeight;
	private int stepLength = DEFAULT_STEP_LENGTH;		//default set to SystemInfo.unitLength
}

