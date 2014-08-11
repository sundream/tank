package com.hugong.lgl;

import java.awt.Point;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


class SystemInfo {
	//单元长度(理论上本程序要求改值为2,3,4,6的公倍数，但是这样12太小，36太大，故本程序折中处理，让其为2,3,4的公倍数)
	//而且是6的公倍数仅仅印象齿轮上的履带
	public static final int wh = 26;		//棋盘宽高
	public static final int unitLength = 16;			
	public static final int tankStepLength = unitLength / 4;
	public static final int bulletStepLength = unitLength;
	public static final int heroSumNum = 4;
	public static final int playKeyNum = 5;
	public static final int keyCode1[] = new int [playKeyNum];
	public static final int keyCode2[] = new int [playKeyNum];
	//本质上mul应为tankStepLength > unitLength ? 1 : unitLength/tankStepLength; 但unitLength,tankStepLength固定后其应该固定了
	public static final int mul = unitLength/tankStepLength;	
	public static final int bulletSize = 4;			//子弹大小
	public static final int bulletRefreshInterval = 70;	//子弹刷新间隔为70ms
	public static final int tankRefreshInterval = 100;	//坦克刷新间隔为100ms
	public static final int tankWidth = 2 * unitLength;	//坦克宽度
	public static final int tankHeight = 2 * unitLength;	//坦克高度		
	public static final int plWidth = wh*unitLength;
	public static final int plHeight = wh*unitLength;
	public static final int jfWidth = (wh+8)*unitLength;
	public static final int jfHeight = plHeight;
	public static final int jfX = 200;
	public static final int jfY = 200;
	public static final int enemyTypeCount = 4;
	public static final int[] enemyPrice = new int[]{100,200,300,400};
	public static final Point hero1Location = new Point((wh/2-4)*unitLength,plHeight-tankHeight/2);
	public static final Point hero2Location = new Point((wh/2+4)*unitLength,plHeight-tankHeight/2);
	public static final Point enemyLocation1 = new Point(unitLength,tankHeight/2);	//==(tankWidth/2,tankHeight/2)
	public static final Point enemyLocation2 = new Point(wh/2*unitLength,tankHeight/2);
	public static final Point enemyLocation3 = new Point((wh-1)*unitLength,tankHeight/2);
	public static final Point enmyLocation[] = new Point[]{enemyLocation1,enemyLocation2,enemyLocation3};
	public static final int rankSumNum = 100;
	public static final int wealthNum = 6;	//宝物类型个数
	public static final int wealthWidth = 16;
	public static final int wealthHeight = 16;
	public static final int wealthShowTime = 6000;	//6s
	public static final int wealthValidTime = 6000;	//6s
	public static final int HeightOffset = 80;
	public static final int alignmentWidth = 16;
	public static final int rankShowNum = 10;		//成绩排名最大个数
	public static final String gameFile = "D:/tankgame/";	//保存游戏的目录（排名文件也在其中）
	public static final int gradeAddHeroTank = 10000;	//每增加10000分对应英雄坦克数加1
	
	public static final int homeProtectBarriersNum = 8;
	public static Point[] homeProtectBarriers = new Point[homeProtectBarriersNum];
	//一定要放到后面，应为他们的构造函数部分用到了SystemInfo信息
	public static GamePanel mjpGame; //= new GamePanel();
	public static PromptPanel proptPanel;// = new PromptPanel();
	public static ImageOperation imageOperation = new ImageOperation();
	public static SoundOperation soundOperation = new SoundOperation();
	public static FileOperation fileOperation = new FileOperation();
	
	//各种锁
	public static String homeProtectLock = new String();
	public static String enemyStopLock = new String();
	public static String startLock = new String();
	public static String afterModeSelectLock = new String();
	public static String passRankLock = new String();
	public static String statisticLock = new String();
	public static String lock = new String();	//共享锁
	
	//各种标志变量
	public static boolean start = false;
	public static boolean afterModeSelect = false;
	public static boolean afterStatistic = false;
	public static boolean stop = false;	
	public static boolean fail = false;
	public static boolean oneMan = true;	//默认为单机游戏
	public static  Rank rank = new Rank();	//当前所处关数（应该将关写成一个类，包含难度情况，如敌机数量、速度等等）

	//系统信息初始化
	public static void initSystemInfo(){
		fail = false;
		stop = false;
		afterStatistic = false;
		afterModeSelect = false;
		start = false;
		oneMan = true;
		rank = new Rank();
		homeProtectBarriers[0] = new Point((wh/2-2)*unitLength,(wh-1)*unitLength);
		homeProtectBarriers[1] = new Point((wh/2-2)*unitLength,(wh-2)*unitLength);
		homeProtectBarriers[2] = new Point((wh/2-2)*unitLength,(wh-3)*unitLength);
		homeProtectBarriers[3] = new Point((wh/2-1)*unitLength,(wh-3)*unitLength);
		homeProtectBarriers[4] = new Point((wh/2)*unitLength,(wh-3)*unitLength);
		homeProtectBarriers[5] = new Point((wh/2+1)*unitLength,(wh-3)*unitLength);
		homeProtectBarriers[6] = new Point((wh/2+1)*unitLength,(wh-2)*unitLength);
		homeProtectBarriers[7] = new Point((wh/2+1)*unitLength,(wh-1)*unitLength);
		
		Hero.setHero1SumGrade(0);
		Hero.setHero2SumGrade(0);
		Hero.setTenThousandNum1(0);
		Hero.setTenThousandNum2(0);
	}
	
	public static boolean isHomeProtectBarrier(Barrier barrier){
		boolean ret = false;
		for(int i = 0; i < homeProtectBarriersNum; ++i){
			if(barrier.getX() == homeProtectBarriers[i].x && barrier.getY() == homeProtectBarriers[i].y){
				ret = true;
				break;
			}
		}
		return ret;
	}
	
	public static int getIntByTankType(TankType tankType){
		int ret = -1;
		switch(tankType){
		case LOW_ARMOUR:
			ret = 0;
			break;
		case MIDDLE_ARMOUR:
			ret = 1;
			break;

		case FAST_SPEED:
			ret = 2;
			break;
		case HIGH_ARMOUR:
			ret = 3;
			break;
		default:
			break;
		}
		return ret;
	}
	
	public static TankType getTankTypeByInt(int n){
		TankType ret = TankType.UNDEFINED;
		switch(n){
		case 0:
			ret = TankType.LOW_ARMOUR;
			break;
		case 1:
			ret = TankType.MIDDLE_ARMOUR;
			break;
		case 2:
			ret = TankType.FAST_SPEED;
			break;
		case 3:
			ret = TankType.HIGH_ARMOUR;
			break;
		case 4:
			ret = TankType.HERO;
			break;
		default:
			break;
		}
		return ret;
	}
	public static TankType randomTankType(){
		TankType ret = TankType.UNDEFINED;
		int r = (int)(Math.random()*SystemInfo.enemyTypeCount);
		switch(r){
		case 0:
			ret = TankType.LOW_ARMOUR;
			break;
		case 1:
			ret = TankType.MIDDLE_ARMOUR;
			break;
		case 2:
			ret = TankType.FAST_SPEED;
			break;
		case 3:
			ret = TankType.HIGH_ARMOUR;
			break;
		default:
			break;
		}
		return ret;
	}
	
	public static Direction randomDirect(){
		Direction direct = null;
		int r = (int)(Math.random()*4);
		switch(r){
		case 0:
			direct = Direction.DIR_UP;
			break;
		case 1:
			direct = Direction.DIR_RIGHT;
			break;
		case 2:
			direct = Direction.DIR_DOWN;
			break;
		case 3:
			direct = Direction.DIR_LEFT;
			break;
		default:
			break;
		}
		return direct;
	}
	
	public static BarrierType getBarrierTypeByInt(int n){
		BarrierType ret = BarrierType.UNDEFINED;
		switch(n){
		case 0:
			ret = BarrierType.NONE;
			break;
		case 1:
			ret = BarrierType.BOTH_GO;
			break;
		case 2:
			ret = BarrierType.ONLY_BULLET;
			break;
		case 3:
			ret = BarrierType.BOTH_FORBIN1;
			break;
		case 4:
			ret = BarrierType.BOTH_FORBIN2;
		default:
			break;
		}
		return ret;
	}
	
	public static boolean restoreGame(String filename){
		boolean ret = false;
		FileInputStream is = null;
		ObjectInputStream ois = null;
		try {
			is = new FileInputStream(filename);
			ois = new ObjectInputStream(is);
			SystemInfo.mjpGame = (GamePanel)ois.readObject();
		
			ois = new ObjectInputStream(is);
			SystemInfo.start = ois.readBoolean();
			ois = new ObjectInputStream(is);
			SystemInfo.afterModeSelect = ois.readBoolean();
			ois = new ObjectInputStream(is);
			SystemInfo.afterStatistic = ois.readBoolean();
			ois = new ObjectInputStream(is);
			SystemInfo.stop = ois.readBoolean();
			ois = new ObjectInputStream(is);
			SystemInfo.fail = ois.readBoolean();
			ois = new ObjectInputStream(is);
			SystemInfo.oneMan = ois.readBoolean();
			ois = new ObjectInputStream(is);
			SystemInfo.rank = (Rank)ois.readObject();
			
			ret = true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally{
			try{
				if(is != null)
					is.close();
				if(ois != null)
					ois.close();
			}
			catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	
		}

		return ret;
	}
	
	public static boolean saveGame(String filename){
		boolean ret = false;
		FileOutputStream os = null;
		ObjectOutputStream oos = null;
		try {
			os = new FileOutputStream(filename);
			oos = new ObjectOutputStream(os);
			if(SystemInfo.mjpGame != null){
				oos.writeObject(SystemInfo.mjpGame);
			}
			oos = new ObjectOutputStream(os);
			oos.writeBoolean(SystemInfo.start);
			oos = new ObjectOutputStream(os);
			oos.writeBoolean(SystemInfo.afterModeSelect);
			oos = new ObjectOutputStream(os);
			oos.writeBoolean(SystemInfo.afterStatistic);
			oos = new ObjectOutputStream(os);
			oos.writeBoolean(SystemInfo.stop);
			oos = new ObjectOutputStream(os);
			oos.writeBoolean(SystemInfo.fail);
			oos = new ObjectOutputStream(os);
			oos.writeBoolean(SystemInfo.oneMan);
			oos = new ObjectOutputStream(os);
			oos.writeObject(SystemInfo.rank);
			
			ret = true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally{
			try{
				if(os != null)
					os.close();
				if(oos != null)
					oos.close();
			}
			catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	
		}

		return ret;
	}
}

