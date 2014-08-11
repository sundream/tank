package com.hugong.lgl;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.Vector;

enum StateType{NORMAL,UNDEATH,}

class Hero extends Tank{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8275108095176518704L;
	
	public Hero(int x, int y, Direction direct, TankType tankType,Color color,
			boolean canShot, boolean live, int width, int height, int stepLength)
	{
		super(x, y, direct, tankType,color, canShot, live, width, height, stepLength);
		// TODO Auto-generated constructor stub
		for(int i = 0; i < SystemInfo.wealthNum; ++i)
			wealth[i] = 0;
	}

	public Hero(int x, int y, Direction direct, TankType tankType, Color color) {
		super(x, y, direct, tankType,color);
		// TODO Auto-generated constructor stub
		for(int i = 0; i < SystemInfo.wealthNum; ++i)
			wealth[i] = 0;
	}
	
	public void drawTank(Graphics g){
		super.drawTank(g);
		Color oldColor = g.getColor();
		if(StateType.UNDEATH == this.state) //英雄出于无敌状态，画一保护圈
		{
			/*
			 //无敌状态太丑了
			int wh = (int)Math.sqrt(this.getWidth()*this.getWidth()+this.getHeight()*this.getHeight());
			Color oldColor = g.getColor();
			g.setColor(Color.white);
			g.drawOval(this.getX()-wh/2, this.getY()-wh/2,wh, wh);
			g.setColor(oldColor);*/
			int wh = this.getWidth() + 2;
			
			g.setColor(Color.white);
			if(Math.random() <= 0.8)	//防护圈闪烁显示
				g.drawRect(this.getX()-wh/2, this.getY()-wh/2,wh, wh);
			
		}
		if(this.wealth[5] >= 1){		//捡到星星宝物
			if(1 == this.wealth[5])		  //捡到一个星星圆形入口为粉红色
				g.setColor(Color.pink);	  
			else if(this.wealth[5] >= 2)  //捡到2个以上星星圆形入口为红色
				g.setColor(Color.red);
			if(Direction.DIR_LEFT == this.getDirect() || Direction.DIR_RIGHT == this.getDirect())
				g.fillOval(getX()-getHeight()/6, getY()-getWidth()/4, getHeight()/3, getWidth()/2);			//圆形入口
			else if(Direction.DIR_UP == this.getDirect() || Direction.DIR_DOWN == this.getDirect())
				g.fillOval(getX()-getWidth()/4, getY()-getHeight()/6, getWidth()/2, getHeight()/3);	//圆形入口
			else{
				
			}
		}
//		if(this.wealth[6] >= 1){	//捡到船了
//			
//		}
		g.setColor(oldColor);
	}
	
	public static void addHero1KilledEnemyOfType(TankType tt){
		//LOW_ARMOUR,MIDDLE_ARMOUR,FAST_SPEED,HIGH_ARMOUR
		int idx = -1;
		switch(tt){
		case LOW_ARMOUR:
			idx = 0;
			break;
		case MIDDLE_ARMOUR:
			idx = 1;
			break;
		case FAST_SPEED:
			idx = 2;
			break;
		case HIGH_ARMOUR:
			idx = 3;
			break;
		default:
			//System.err.println("Unkowned enemyType");
			break;
		}
		if(idx != -1)
			++hero1Grade[idx];
	}
	
	public static void addHero2KilledEnemyOfType(TankType tt){
		//LOW_ARMOUR,MIDDLE_ARMOUR,FAST_SPEED,HIGH_ARMOUR
		int idx = -1;
		switch(tt){
		case LOW_ARMOUR:
			idx = 0;
			break;
		case MIDDLE_ARMOUR:
			idx = 1;
			break;
		case FAST_SPEED:
			idx = 2;
			break;
		case HIGH_ARMOUR:
			idx = 3;
			break;
		default:
			//System.err.println("Unkowned enemyType");
			break;
		}
		if(idx != -1)
			++hero2Grade[idx];
	}
	
	public static int getHero1Num() {
		return hero1Num;
	}

	public static void setHero1Num(int hero1Num) {
		Hero.hero1Num = hero1Num;
	}

	public static int getHero2Num() {
		return hero2Num;
	}
	
	public static void setHero2Num(int hero2Num) {
		Hero.hero2Num = hero2Num;
	}
	
	public static int[] getHero1Grade() {
		return hero1Grade;
	}

	public static int[] getHero2Grade() {
		return hero2Grade;
	}

	public static int getHero1SumGrade() {
		return hero1SumGrade;
	}

	public static void setHero1SumGrade(int hero1SumGrade) {
		Hero.hero1SumGrade = hero1SumGrade;
	}

	public static void addHero1SumGrade(TankType tankType){
		int idx = SystemInfo.getIntByTankType(tankType);
		if(idx != -1)
			Hero.hero1SumGrade += SystemInfo.enemyPrice[idx];
		int num = Hero.hero1SumGrade / SystemInfo.gradeAddHeroTank;
		if(num > 0 && num != Hero.tenThousandNum1)
		{
			++Hero.hero1Num;
			Hero.tenThousandNum1 = num;
		}
	}
	
	public static int getHero2SumGrade() {
		return hero2SumGrade;
	}
	
	public static void addHeroKilledEnemyOfType(Hero belong,TankType tankType){
		if(belong == SystemInfo.mjpGame.getHero1())
		{
			Hero.addHero1KilledEnemyOfType(tankType);
			Hero.addHero1SumGrade(tankType);
		}
		if(belong == SystemInfo.mjpGame.getHero2())
		{
			Hero.addHero2KilledEnemyOfType(tankType);
			Hero.addHero2SumGrade(tankType);
		}
		SystemInfo.rank.setCount(SystemInfo.rank.getCount()-1);		//敌军坦克数量减一
	}
	
	
	public static void addHero2SumGrade(TankType tankType){
		int idx = SystemInfo.getIntByTankType(tankType);
		if(idx != -1)
			Hero.hero2SumGrade += SystemInfo.enemyPrice[idx];
		int num = Hero.hero1SumGrade / SystemInfo.gradeAddHeroTank;
		if(num > 0 && num != Hero.tenThousandNum2)
		{
			++Hero.hero1Num;
			Hero.tenThousandNum2 = num;
		}
	}
	
	public static void setHero2SumGrade(int hero2SumGrade) {
		Hero.hero2SumGrade = hero2SumGrade;
	}

	public static void setHero1Grade(int[] hero1Grade) {
		Hero.hero1Grade = hero1Grade;
	}

	public static void setHero2Grade(int[] hero2Grade) {
		Hero.hero2Grade = hero2Grade;
	}
	
	public StateType getState() {
		return state;
	}

	public void setState(StateType state) {
		this.state = state;
	}

	public int[] getWealth() {
		return wealth;
	}
	
	public Wealth getTouchWealth(){
		Wealth ret = null;
		Vector<Wealth> wealths = SystemInfo.mjpGame.getWealths();
		
		for(int i = wealths.size() - 1; i >= 0; --i){
			if(wealths.get(i).isShow() && this.isTouchWealth(wealths.get(i)))
			{
				ret = wealths.get(i);
				break;
			}
		}
		if(ret != null){
			//捡到任何宝物都加分
			if(SystemInfo.mjpGame != null){
				if(this == SystemInfo.mjpGame.getHero1()){
					Hero.setHero1SumGrade(Hero.getHero1SumGrade()+SystemInfo.enemyPrice[SystemInfo.enemyTypeCount-1]+100);
				}
				else if(!SystemInfo.oneMan && this == SystemInfo.mjpGame.getHero2()){
					Hero.setHero2SumGrade(Hero.getHero2SumGrade()+SystemInfo.enemyPrice[SystemInfo.enemyTypeCount-1]+100);
				}
			}
		}
		return ret;
	}
	
	private boolean isTouchWealth(Wealth wealth){
		boolean ret = false;
		Point p1 = new Point();
		Point p2 = new Point();
		Point pCannon = this.getCannonPipeLocation();
		switch(this.getDirect()){
		case DIR_UP:
			p1.x = this.getX() - this.getWidth() / 2;
			p1.y = this.getY() - this.getHeight() / 2;
			p2.x = this.getX() + this.getWidth() / 2;
			p2.y = this.getY() - this.getHeight() / 2;
			break;
		case DIR_RIGHT:
			p1.x = this.getX() + this.getHeight() / 2;
			p1.y = this.getY() - this.getWidth() / 2;
			p2.x = this.getX() + this.getHeight() / 2;
			p2.y = this.getY() + this.getWidth() / 2;
			break;
		case DIR_DOWN:
			p1.x = this.getX() - this.getWidth() / 2;
			p1.y = this.getY() + this.getHeight() / 2;
			p2.x = this.getX() + this.getWidth() / 2;
			p2.y = this.getY() + this.getHeight() / 2;
			break;
		case DIR_LEFT:
			p1.x = this.getX() - this.getHeight() / 2;
			p1.y = this.getY() - this.getWidth() / 2;
			p2.x = this.getX() - this.getHeight() / 2;
			p2.y = this.getY() + this.getWidth() / 2;
			break;
		default:
			break;
		}
		int wh = wealth.getWidth();	//障碍物宽高
		//三点只要有一点在宝物范围内就算捡到宝物了
		if((wealth.getX() <= p1.x && p1.x <= wealth.getX()+wh && wealth.getY()<= p1.y && p1.y <= wealth.getY()+wh)
				|| (wealth.getX() <= p2.x && p2.x <= wealth.getX()+wh && wealth.getY()<= p2.y && p2.y <= wealth.getY()+wh)
				|| (wealth.getX() <= pCannon.x && pCannon.x <= wealth.getX()+wh && wealth.getY() <= pCannon.y && pCannon.y <= wealth.getY()+wh) )
		{
			ret = true;
		}
		return ret;
	}
	public static int getTenThousandNum1() {
		return tenThousandNum1;
	}

	public static void setTenThousandNum1(int tenThousandNum1) {
		Hero.tenThousandNum1 = tenThousandNum1;
	}

	public static int getTenThousandNum2() {
		return tenThousandNum2;
	}

	public static void setTenThousandNum2(int tenThousandNum2) {
		Hero.tenThousandNum2 = tenThousandNum2;
	}

	public static void clearHero1Wealth(){
		for(int i = 0; i < SystemInfo.wealthNum; ++i)
			hero1Wealth[i] = 0;
	}
	
	public static void clearHero2Wealth(){
		for(int i = 0; i < SystemInfo.wealthNum; ++i)
			hero2Wealth[i] = 0;
	}
	
	//保存指定英雄的宝物
	public void saveHeroWealth(int who){
		if(1 == who){		//save hero1's wealth
			for(int i = 0; i < SystemInfo.wealthNum; ++i){
				hero1Wealth[i] = this.wealth[i];
			}
		}
		else if(0 == who){	//save hero2's wealth
			for(int i = 0; i < SystemInfo.wealthNum; ++i){
				hero2Wealth[i] = this.wealth[i];
			}
		}
		else{
			
		}
	}
	

	
	//回复指定英雄的宝物
	public void restoreHeroWealth(int who){
		if(1 == who){		//save hero1's wealth
			for(int i = 0; i < SystemInfo.wealthNum; ++i){
				this.wealth[i] = hero1Wealth[i];
			}
		}
		else if(0 == who){	//save hero2's wealth
			for(int i = 0; i < SystemInfo.wealthNum; ++i){
				this.wealth[i] = hero2Wealth[i];
			}
		}
		else{
			
		}
	}
	
	private StateType state = StateType.NORMAL;
	private int[] wealth = new int[SystemInfo.wealthNum];
	//为了保证英雄过关后(因为游戏面板会被删除,伴随的英雄也会回到初始状态，我们每当过关后便
	//英雄拥有的宝物，当其挂了后便清空)
	private static int[] hero1Wealth = new int[SystemInfo.wealthNum];
	private static int[] hero2Wealth = new int[SystemInfo.wealthNum];
	//hero1,hero2每关杀的不同类型敌军个数
	private static int hero1Grade[] = new int[SystemInfo.enemyTypeCount];	
	private static int hero2Grade[] = new int[SystemInfo.enemyTypeCount];
	//hero1,hero2本次玩游戏的总分数
	private static int hero1SumGrade = 0;
	private static int hero2SumGrade = 0;
	//总分有几个万，主要是为了上万后增加英雄坦克数
	private static int tenThousandNum1 = 0;	
	private static int tenThousandNum2 = 0;
	private static int hero1Num	= SystemInfo.heroSumNum;
	private static int hero2Num	= 0;
}
