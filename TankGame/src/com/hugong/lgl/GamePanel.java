package com.hugong.lgl;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Vector;
import javax.swing.JPanel;

class GamePanel extends JPanel implements KeyListener,Runnable,Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -561213700433898454L;
	
	public GamePanel(){
		this.setBackground(Color.black); 
		this.setFocusable(true);
		this.addKeyListener(this);
		if(Hero.getHero1Num() > 0){
			if(null == hero1)
				hero1 = new Hero(SystemInfo.hero1Location.x,SystemInfo.hero1Location.y,
						Direction.DIR_UP,TankType.HERO,Color.green);
		}
	
		//hero1Grade = new int[SystemInfo.enemyTypeCount];
		if(!SystemInfo.oneMan && Hero.getHero2Num() > 0){
			if(null == hero2)
				hero2 = new Hero(SystemInfo.hero2Location.x,SystemInfo.hero2Location.y,
						Direction.DIR_UP,TankType.HERO,Color.yellow);
			//hero2Grade = new int[SystemInfo.enemyTypeCount];
		}
		
		SystemInfo.soundOperation.playSound("start.wav", 6000);
		
//		try {
//			if(gameover == null)
//				gameover = ImageIO.read(new File("src/images/gameover.jpg"));
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		this.h = 5;

	}
	public void paint(Graphics g){
		super.paint(g);			//һ��Ҫ�ǵõ��ø���JPanel��paint��׼������
		
		
		//�Ƴ������ӵ���̹�ˡ��ϰ��������Ч����(����+ͼ��).������δ����������
		
		//���ӵ�
		//System.err.println("bullet count:" + bullets.size());
		for(int i = bullets.size() - 1; i >= 0; --i){
			if(bullets.get(i).isLive()){
				bullets.get(i).drawBullet(g);
			}
			else{
				SystemInfo.imageOperation.drawImage(g, "bomb.jpg", new Point(bullets.get(i).getX(),bullets.get(i).getY()));
				synchronized(bullets){
					bullets.remove(i);
				}
			}
			//System.err.println("bullet" + i + " x:" + bullets.get(i).getX() + ",y:" + bullets.get(i).getY());
		}
		
		if(hero1 != null){
			if(!hero1.isLive()){
				Hero.setHero1Num(Hero.getHero1Num()-1);
				hero1 = null;
				if(Hero.getHero1Num() > 0){
					hero1 = new Hero(SystemInfo.hero1Location.x,SystemInfo.hero1Location.y,
							Direction.DIR_UP,TankType.HERO,Color.green);
					hero1.getWealth()[0] = 1;
				}
			}
			else{
				hero1.drawTank(g);
			}
		}

		if(!SystemInfo.oneMan && hero2 != null){	//˫����Ϸ
			if(!hero2.isLive()){
				Hero.setHero2Num(Hero.getHero2Num()-1);
				hero2 = null;
				if(Hero.getHero2Num() > 0){
					hero2 = new Hero(SystemInfo.hero2Location.x,SystemInfo.hero2Location.y,
							Direction.DIR_UP,TankType.HERO,Color.yellow);
					hero2.getWealth()[0] = 1;
				}
			}
			else{
				hero2.drawTank(g);
			}
		}
		
		
		//�������ֵо�̹�˵���ʾЧ��
		for(int i = 0; i < 3; ++i){
			if(this.enemyLocation[i] != null){
				
				g.drawLine(enemyLocation[i].x-SystemInfo.tankWidth/2, enemyLocation[i].y, enemyLocation[i].x+SystemInfo.tankWidth/2, 
						enemyLocation[i].y);
				g.drawLine(enemyLocation[i].x, enemyLocation[i].y-SystemInfo.tankHeight/2, enemyLocation[i].x, 
						enemyLocation[i].y+SystemInfo.tankHeight/2);
				if(arr[i] < 10 && arr[i] % 2 == 0)
					SystemInfo.imageOperation.drawImage(g, "cross1.jpg", new Point(enemyLocation[i].x-SystemInfo.tankWidth/2,enemyLocation[i].y-SystemInfo.tankHeight/2));
				else if(arr[i] % 2 == 0)
					SystemInfo.imageOperation.drawImage(g, "cross2.jpg", new Point(enemyLocation[i].x-SystemInfo.tankWidth/2,enemyLocation[i].y-SystemInfo.tankHeight/2));
			}
		}

		
		//���о�̹��
		//System.err.println("enemyTank count:" + enemyTanks.size());
		for(int i = enemyTanks.size() - 1; i >= 0; --i){
			if(enemyTanks.get(i).isLive())
			{
				enemyTanks.get(i).drawTank(g);
			}
			else{
				synchronized(enemyTanks){
					enemyTanks.remove(i);
				}
			}
			//System.err.println(enemyTanks.get(i).getX());
		}
		
		//���ϰ���
		for(int i = barriers.size() - 1; i >= 0; --i){
			//System.err.println(barriers.get(i).getBarrierType());
			if(barriers.get(i).isLive())
			{
				//�Ա�����԰��ǽ���⴦��(�����񵽱�����԰����Ч�ڹ�������ָ�����ǽ)
				if(SystemInfo.isHomeProtectBarrier(barriers.get(i)) && 
						BarrierType.BOTH_FORBIN2 == barriers.get(i).getBarrierType())
				{
					boolean flag = false;
					if(SystemInfo.oneMan){	//������Ϸ
						if(hero1 != null && hero1.getWealth()[1] <= 0)
							flag = true;
					}
					else{		//˫����Ϸ
						if(hero1 == null){   //Ӣ��һ������
							if(hero2 != null && hero2.getWealth()[1] <= 0)
								flag = true;
						}
						else if(hero2 == null){ //Ӣ�۶�������
							if(hero1 != null && hero1.getWealth()[1] <= 0)
								flag = true;
						}
						else  //Ӣ��һ����������
						{
							//��Ȼ��ʱhero1,hero2������Ϊ�գ������Ӳ�Ϊnull�ж������ֺ�ϰ��
							if(hero1 != null && hero1.getWealth()[1] <= 0 && hero2 != null && hero2.getWealth()[1] <= 0)
								flag = true;
						}
					}
					if(flag)
					{
						barriers.get(i).setBarrierType(BarrierType.BOTH_FORBIN1);
					}
				
				}
				barriers.get(i).drawBarrier(g);
			}
			else{
				synchronized(barriers){
					barriers.remove(i); 
				}		 	
			}
		}
		
		if(SystemInfo.fail){
			SystemInfo.imageOperation.drawImage(g, "deathhome.jpg", new Point((SystemInfo.wh/2-1)*SystemInfo.unitLength,
					(SystemInfo.wh-2)*SystemInfo.unitLength));
			
			//gameover ͼ�����⴦��
			g.drawImage(ImageOperation.gameover.getImage(), SystemInfo.plWidth/3, SystemInfo.plHeight/3, ImageOperation.gameover.getImage().getWidth(SystemInfo.mjpGame),
					h,SystemInfo.mjpGame);
			if(this.h < ImageOperation.gameover.getImage().getHeight(SystemInfo.mjpGame))
				this.h += 5;
			
		}
		else{
			SystemInfo.imageOperation.drawImage(g, "livehome.jpg", new Point((SystemInfo.wh/2-1)*SystemInfo.unitLength,
					(SystemInfo.wh-2)*SystemInfo.unitLength));
		}
				
		//�����ػ棬Ӧ����󻭱����������ܲ��ֻ�ȫ��������
		for(int i =  wealths.size() - 1; i >= 0; --i){
			if(wealths.get(i).isShow()){
				if(Math.random() <= 0.8){
					wealths.get(i).drawWealth(g);
				}
		
			}
			else{
				wealths.remove(i);
			}
		}
	
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		//����Ӣ��̹�����˹��������ʵ����·�����󣬲������Ƿ񿨵���Ӧ�ı䷽��
		int kc = e.getKeyCode();
		if(Hero.getHero1Num() > 0){
			switch(kc){
			case KeyEvent.VK_W:
				SystemInfo.keyCode1[0] = KeyEvent.VK_W;
				break;
			case KeyEvent.VK_S:
				SystemInfo.keyCode1[1] = KeyEvent.VK_S;
				break;
			case KeyEvent.VK_A:
				SystemInfo.keyCode1[2] = KeyEvent.VK_A;
				break;
			case KeyEvent.VK_D:
				SystemInfo.keyCode1[3] = KeyEvent.VK_D;
				break;
			case KeyEvent.VK_J:		//Ϊ�˷�����̨ʽ������
			case KeyEvent.VK_V:
				SystemInfo.keyCode1[4] = KeyEvent.VK_V;
				break;
			default:
				break;
			}
		}
	
		
		if(!SystemInfo.oneMan && Hero.getHero2Num() > 0)		//˫����Ϸ
		{
			switch(kc){
			case KeyEvent.VK_UP:
				SystemInfo.keyCode2[0] = KeyEvent.VK_UP;
				break;
			case KeyEvent.VK_DOWN:
				SystemInfo.keyCode2[1] = KeyEvent.VK_DOWN;
				break;
			case KeyEvent.VK_LEFT:
				SystemInfo.keyCode2[2] = KeyEvent.VK_LEFT;
				break;
			case KeyEvent.VK_RIGHT:
				SystemInfo.keyCode2[3] = KeyEvent.VK_RIGHT;
				break;
			case KeyEvent.VK_NUMPAD0:      //Ϊ�˷�����̨ʽ������,ע��С����0�������!
			case KeyEvent.VK_L:
				SystemInfo.keyCode2[4] = KeyEvent.VK_L;
				break;
			default:
				break;
			}
		}
		
		if(KeyEvent.VK_SPACE == kc)	//�ո��λ��ͣ/�����л���
		{
			if(SystemInfo.stop)	
			{
				synchronized(SystemInfo.lock){
					SystemInfo.stop = false;
					SystemInfo.lock.notifyAll();
				}
			}
			else
			{
				SystemInfo.stop = true;
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
	/*	// TODO Auto-generated method stub
		if(hero1 != null)
			hero1.ajustLocation();
		if(!SystemInfo.oneMan && hero2 != null)
			hero2.ajustLocation();*/
		int kc = e.getKeyCode();
		switch(kc){
		case KeyEvent.VK_W:
			SystemInfo.keyCode1[0] = 0;
			break;
		case KeyEvent.VK_S:
			SystemInfo.keyCode1[1] = 0;
			break;
		case KeyEvent.VK_A:
			SystemInfo.keyCode1[2] = 0;
			break;
		case KeyEvent.VK_D:
			SystemInfo.keyCode1[3] = 0;
			break;
		case KeyEvent.VK_J:		//Ϊ�˷�����̨ʽ������	
		case KeyEvent.VK_V:
			SystemInfo.keyCode1[4] = 0;
			break;
		default:
			break;
		}
		switch(kc){
		case KeyEvent.VK_UP:
			SystemInfo.keyCode2[0] = 0;
			break;
		case KeyEvent.VK_DOWN:
			SystemInfo.keyCode2[1] = 0;
			break;
		case KeyEvent.VK_LEFT:
			SystemInfo.keyCode2[2] = 0;
			break;
		case KeyEvent.VK_RIGHT:
			SystemInfo.keyCode2[3] = 0;
			break;
		case KeyEvent.VK_NUMPAD0:      //Ϊ�˷�����̨ʽ������,ע��С����0�������!
		case KeyEvent.VK_L:
			SystemInfo.keyCode2[4] = 0;
			break;
		default:
			break;
		}
		
	}
	
	public Hero getHero1() {
		return hero1;
	}
	
	public Hero getHero2(){
		return hero2;
	}
	

	public Vector<Bullet> getBullets() {
		return bullets;
	}
	public Vector<EnemyTank> getEnemyTanks() {
		return enemyTanks;
	}
	public Vector<Barrier> getBarriers() {
		return barriers;
	}
	
	@Override
	public void run(){
		// TODO Auto-generated method stub
		EnemyTank enemy;
		int i,j,k,m;
		while(true){
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if(SystemInfo.rank.getCount() <= 0 || SystemInfo.fail)
			{
				if(SystemInfo.fail){
					SystemInfo.soundOperation.playSound("gameover.wav", 2500);
					synchronized(SystemInfo.passRankLock){
						try {
							SystemInfo.passRankLock.wait(4000);	//��Ϸ����ʱ�ȴ�4s
							SystemInfo.passRankLock.notify();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					if(SystemInfo.oneMan)			//������Ϸ
						SystemInfo.fileOperation.insertRank1();
					else							//˫����Ϸ
						SystemInfo.fileOperation.insertRank2();
				}
				break;
			}
			//�ʵ����������ս���о�����
			for(k = 0; k < 3 && enemyLocation[k] != null; ++k)
				;
			if(k < 3 && enemyTanks.size() < SystemInfo.rank.getBattleEnemyCount() && enemyTanks.size() < SystemInfo.rank.getCount())
			{
				do{
					i = (int)(Math.random()*SystemInfo.enemyTypeCount);
				}while(SystemInfo.rank.getEnemyTankNum()[i] <= 0);
				j = (int)(Math.random()*3);
				enemyLocation[k] = SystemInfo.enmyLocation[j];
				for(m = 0; m < 30; ++m)
				{
					arr[k] = m;
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				enemy = new EnemyTank(SystemInfo.enmyLocation[j].x,SystemInfo.enmyLocation[j].y,
						Direction.DIR_DOWN,SystemInfo.getTankTypeByInt(i));
				--SystemInfo.rank.getEnemyTankNum()[i];	//���ٺ󷽸�����̹������
				int r = (int)(Math.random()*SystemInfo.rank.getCount());
				if(SystemInfo.rank.getWealthNum() > 0 && r % 4 == 0 && SystemInfo.rank.getEnemyTankNum()[i] % 2 == 0)
				{
					int wealthType = (int)(Math.random()*SystemInfo.wealthNum);  //�������һ�ֱ�������
					enemy.setEnemyWealth(wealthType); 
					SystemInfo.rank.setWealthNum(SystemInfo.rank.getWealthNum()-1);
				}
				
				enemyTanks.add(enemy);
				enemyLocation[k] = null;
				new Thread(enemy).start();
			}
			processWealth();
		}
			
	}
	
	public void processWealth(){
		int i,j;
		//0��heroprotect,1:homeprotect,2:mine,3:stoptime(�о���ͣ),4:addtank,5:star(�ӷ�)
		for(i = 0; i < SystemInfo.wealthNum; ++i){
			switch(i){
			case 0:	//Ӣ���޵б���
				if(hero1 != null && hero1.getWealth()[i] > 0){
					hero1.setState(StateType.UNDEATH);
					synchronized(hero1){
						try {
							hero1.wait(SystemInfo.wealthValidTime);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						//hero1.notify();
						hero1.setState(StateType.NORMAL);
						--hero1.getWealth()[i];
					}
				}
				if(!SystemInfo.oneMan && hero2 != null && hero2.getWealth()[i] > 0){
					hero2.setState(StateType.UNDEATH);
					synchronized(hero2){
						try {
							hero2.wait(SystemInfo.wealthValidTime);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						//hero2.notify();
						hero2.setState(StateType.NORMAL);
						--hero2.getWealth()[i];
					}
				}
				break;
			case 1:		//������԰����
				if(hero1 != null && hero1.getWealth()[i] > 0 || 
				(!SystemInfo.oneMan && hero2 != null && hero2.getWealth()[i] > 0))
				{
					//�񵽼ӹ̼�԰�����ı�ʱ����԰���ʯ��ǽ(����ɾ������ǽ)
					for(j = barriers.size() - 1; j >= 0; --j){
						if(SystemInfo.isHomeProtectBarrier(barriers.get(j))){
							barriers.get(j).setLive(false);	
						}
					}
					
					for(j = 0; j < SystemInfo.homeProtectBarriersNum; ++j){
						SystemInfo.mjpGame.getBarriers().add(new Barrier(SystemInfo.homeProtectBarriers[j].x,
								SystemInfo.homeProtectBarriers[j].y,BarrierType.BOTH_FORBIN2));
					}
					
					synchronized(SystemInfo.homeProtectLock){
						try {
							SystemInfo.homeProtectLock.wait(SystemInfo.wealthValidTime);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						//System.err.println("protect home");
						//SystemInfo.homeProtectLock.notify();
						if(hero1 != null && hero1.getWealth()[i] > 0 )
							--hero1.getWealth()[i];
						if(!SystemInfo.oneMan && hero2 != null && hero2.getWealth()[i] > 0)
							--hero2.getWealth()[i];
					}
				}
		
				break;
			case 2:		//����
				if(hero1 != null && hero1.getWealth()[i] > 0)
				{
					//this.enemyTanks.clear();	//����û�йر��̣߳��ǲ��е�
					for(j = this.enemyTanks.size() - 1; j >= 0; --j){
						Hero.addHeroKilledEnemyOfType(hero1,this.enemyTanks.get(j).getTankType());
						this.enemyTanks.get(j).setLive(false);
					}
					SystemInfo.soundOperation.playSound("bomb.wav", 1000);
					if(hero1 != null && hero1.getWealth()[i] > 0 )
						--hero1.getWealth()[i];	
					SystemInfo.soundOperation.playSound("enemydeath.wav", 1000);
				}
				
				if(!SystemInfo.oneMan && hero2 != null && hero2.getWealth()[i] > 0)
				{
					for(j = this.enemyTanks.size() - 1; j >= 0; --j){
						Hero.addHeroKilledEnemyOfType(hero2,this.enemyTanks.get(j).getTankType());
						this.enemyTanks.get(j).setLive(false);
					}
					if(!SystemInfo.oneMan && hero2 != null && hero2.getWealth()[i] > 0)
						--hero2.getWealth()[i];
					SystemInfo.soundOperation.playSound("enemydeath.wav", 1000);
				}
				
				break;
			case 3: 	//�о���ͣ
				//�������õ��̣߳����ұ������ö���Ϊ�о�����˴����뱦�����õ�Ӣ��ʱ��ͬ����������
				//������Ϊ�˱�֤�о���ͣһ��ʱ���һ������������
				if(hero1 != null && hero1.getWealth()[i] <= 0 &&
				(!SystemInfo.oneMan && hero2 != null && hero2.getWealth()[i] <= 0))
				{
					synchronized(SystemInfo.enemyStopLock){
						SystemInfo.enemyStopLock.notifyAll();
					}
				}
		
				break;
			case 4:		//����Ӣ��̹��������
				if(hero1 != null && hero1.getWealth()[i] > 0)
				{
					Hero.setHero1Num(Hero.getHero1Num()+1);
					--hero1.getWealth()[i];
				}
				if(!SystemInfo.oneMan && hero2 != null && hero2.getWealth()[i] > 0)
				{
					Hero.setHero2Num(Hero.getHero2Num()+1);
					--hero2.getWealth()[i];
				}
				break;
			case 5:		//��һ�������ӵ��ٶȣ���2�����Ͽ��Դ���ǽ
				/*	//�ӷֱ���	
		   		if(hero1 != null && hero1.getWealth()[i] > 0)
				{
					Hero.setHero1SumGrade(Hero.getHero1SumGrade()+SystemInfo.enemyPrice[SystemInfo.enemyTypeCount-1]+100);
					--hero1.getWealth()[i];
				}
				if(!SystemInfo.oneMan && hero2 != null && hero2.getWealth()[i] > 0)
				{
					Hero.setHero2SumGrade(Hero.getHero2SumGrade()+SystemInfo.enemyPrice[SystemInfo.enemyTypeCount-1]+100);
					--hero2.getWealth()[i];
				}*/
				
				break;
			case 6:		//�񵽴��ˣ�*��δ��Ӹù��ܣ�
				break;	
			case 7:		//������(*��δ��Ӹù���)
			default:
				break;
			}
		}

	}
	
	public Vector<Wealth> getWealths(){
		return wealths;
	}

        
	//private static Image gameover = null;
	private int h = 5;
	private int[] arr = new int[3];
	private Point[] enemyLocation = new Point[3];
	private Hero hero1 = null;
	private Hero hero2 = null;
	private Vector<EnemyTank> enemyTanks = new Vector<EnemyTank>();
	private Vector<Bullet> bullets = new Vector<Bullet>();
	private Vector<Barrier> barriers = new Vector<Barrier>();
	private Vector<Wealth> wealths = new Vector<Wealth>();

}












