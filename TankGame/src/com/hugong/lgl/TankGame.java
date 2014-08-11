/**
 * 功能：坦克大战游戏
 * 作者：林光亮
 * 开发时间： 2012/5/24
 * 版本：1.0
 */
package com.hugong.lgl;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.Vector;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class TankGame extends JFrame implements Runnable,ActionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @param args
	 * 
	 */
	public static void main(String[] args){
		// TODO Auto-generated method stub
		try{
			DefaultKeyboardFocusManager dkbfm = new DefaultKeyboardFocusManager();
			TankGame tg = new TankGame("坦克大战游戏");
			new Thread(tg).start();
			new Thread(new KeyBoarderThread()).start();		//开启键盘线程
			
			while(true){
				tg.jmenuSave.setEnabled(false);
				tg.jmenuContinue.setEnabled(true);
				tg.jmenuUseMap.setEnabled(false);
				
				if(!TankGame.isContinueFromFile()){
					SystemInfo.initSystemInfo();	//系统信息初始化
					
					SelectGameMode selGamePanel = new SelectGameMode();
					tg.add(selGamePanel);
					tg.setVisible(true);
					tg.repaint();	//加上这句后，可以在一定程度上消除面板切换时的闪烁效果
					synchronized(SystemInfo.afterModeSelectLock){
						while(!SystemInfo.afterModeSelect)
						{
							try {
								SystemInfo.afterModeSelectLock.wait();
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
					//System.err.println("after mode select");
					tg.remove(selGamePanel);
				}
				tg.jmenuContinue.setEnabled(false);
				
				
				SystemInfo.fail = false;	
				//SystemInfo.rank.setCur_rank(0); //系统信息初始化时默认为0关，因此该局可以去掉
				while(!SystemInfo.fail)
				{
					if(!TankGame.isContinueFromFile()){
						//System.err.println(SystemInfo.rank.getCur_rank());
						if(null == TankGame.getMapFileName())	//使用标准地图过关后应调整关数
						{
							if(SystemInfo.rank.getCur_rank() == SystemInfo.rankSumNum-1)
								SystemInfo.rank.setCur_rank(0);
							else
								SystemInfo.rank.addRank();
						}
			
						//SystemInfo.start = false;
						tg.jmenuUseMap.setEnabled(true);
						AjustRankPanel ajustRankPanel = new AjustRankPanel();
						tg.add(ajustRankPanel);
						tg.setVisible(true);	//必须重新让tg可见才能显示ajustRankPanel面板
						tg.repaint();
						dkbfm.focusNextComponent(ajustRankPanel);	
						//System.err.println("add ajustRankPanel");
						synchronized(SystemInfo.startLock){
							while(!SystemInfo.start)
							{
								try {
									SystemInfo.startLock.wait();
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}
						tg.remove(ajustRankPanel);
						tg.jmenuUseMap.setEnabled(false);
					}
			
				
					if(!TankGame.isContinueFromFile()){
						SystemInfo.mjpGame = new GamePanel();
						SystemInfo.fileOperation.generateChessBorder(SystemInfo.rank.getCur_rank());	//初始化棋盘
					}
					else {
						//继续以前保存的游戏时，系统会进行反串行化，会自动创建游戏面板
						//此时不必创建游戏面板和初始化地图(地图形状应该是保存游戏时的形状)
						//手动开启从磁盘读入进来的敌军和子弹线程
						Vector<EnemyTank> enemyTanks = SystemInfo.mjpGame.getEnemyTanks();
						Vector<Bullet> bullets = SystemInfo.mjpGame.getBullets();
						Vector<Wealth> wealths = SystemInfo.mjpGame.getWealths();
						for(int i = 0; i < enemyTanks.size(); ++i){
							new Thread(enemyTanks.get(i)).start();
						}
						for(int i = 0; i < bullets.size(); ++i){
							new Thread(bullets.get(i)).start();
						}
						for(int i = 0; i < wealths.size(); ++i){
							new Thread(wealths.get(i)).start();
						}
						
					}
					//恢复英雄所拥有的宝物
					if(SystemInfo.oneMan)	//one man's game
					{
						if(SystemInfo.mjpGame != null && SystemInfo.mjpGame.getHero1() != null)
							SystemInfo.mjpGame.getHero1().restoreHeroWealth(1);
					}
					else				// two man's game
					{
						if(SystemInfo.mjpGame != null && SystemInfo.mjpGame.getHero1() != null)
							SystemInfo.mjpGame.getHero1().restoreHeroWealth(1);
						if(SystemInfo.mjpGame != null && SystemInfo.mjpGame.getHero2() != null)
							SystemInfo.mjpGame.getHero2().restoreHeroWealth(0);
					}
					Hero.clearHero1Wealth();
					Hero.clearHero2Wealth();
					
					SystemInfo.proptPanel = new PromptPanel();
					new Thread(SystemInfo.mjpGame).start();
					
					
					SystemInfo.mjpGame.setPreferredSize(new Dimension(SystemInfo.plWidth+15,SystemInfo.plHeight+SystemInfo.HeightOffset));
					SystemInfo.proptPanel.setPreferredSize(new Dimension(SystemInfo.jfWidth-SystemInfo.plWidth-5,SystemInfo.jfHeight+SystemInfo.HeightOffset));
					tg.add(SystemInfo.mjpGame,BorderLayout.CENTER);
					tg.add(SystemInfo.proptPanel,BorderLayout.EAST);
					tg.setVisible(true);
					tg.repaint();
					
					//new Thread(new KeyBoarderThread()).start();	
					//dkbfm.focusNextComponent(SystemInfo.mjpGame); //为什么用这句还要将窗口最小化再最大化后才能让其获得键盘输入焦点
					dkbfm.focusNextComponent(tg);	
					tg.jmenuSave.setEnabled(true);
					
					synchronized(SystemInfo.passRankLock){
						while(SystemInfo.rank.getCount() > 0 && !SystemInfo.fail)
						{
							//System.err.println("wait  " + SystemInfo.rank.getCount() + "\t" + SystemInfo.fail);
							try {
								SystemInfo.passRankLock.wait();
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
					
					if(!SystemInfo.fail)	//过关了
					{
						if(SystemInfo.oneMan)	//one man's game
						{
							if(SystemInfo.mjpGame != null && SystemInfo.mjpGame.getHero1() != null)
								SystemInfo.mjpGame.getHero1().saveHeroWealth(1);
						}
						else				// two man's game
						{
							if(SystemInfo.mjpGame != null && SystemInfo.mjpGame.getHero1() != null)
								SystemInfo.mjpGame.getHero1().saveHeroWealth(1);
							if(SystemInfo.mjpGame != null && SystemInfo.mjpGame.getHero2() != null)
								SystemInfo.mjpGame.getHero2().saveHeroWealth(0);
						}
					}
					else{			//gameover
							Hero.clearHero1Wealth();
							Hero.clearHero2Wealth();
					}
					
					tg.remove(SystemInfo.mjpGame);
					tg.remove(SystemInfo.proptPanel);
				
					//清空键盘缓冲去
					KeyBoarderThread.clearKeyBoarderBuffer();
					//若继续以前游戏玩完后，系统恢复正常处理
					if(TankGame.isContinueFromFile())
						TankGame.setContinueFromFile(false);
					//若使用自定义地图玩完后，设置自定义地图为空
					if(TankGame.getMapFileName() != null)
						TankGame.setMapFileName(null);
		
					StatisticPanel statisticPanel = new StatisticPanel();
					tg.add(statisticPanel);
					Color oldColor = tg.getContentPane().getBackground();
					tg.getContentPane().setBackground(Color.black);
					tg.setVisible(true);
					tg.repaint();
					new Thread(statisticPanel).start();
					//SystemInfo.afterStatistic = false;
					synchronized(SystemInfo.statisticLock){
						try {
							while(!SystemInfo.afterStatistic)
								SystemInfo.statisticLock.wait();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					tg.getContentPane().setBackground(oldColor);
					//System.err.println("after statistic");
					tg.remove(statisticPanel);
					//每次玩完一关(不管是失败还是成功)都将hero1,hero2每关杀敌个数清空
					for(int i  = 0; i < SystemInfo.enemyTypeCount; ++i){
						Hero.getHero1Grade()[i] = Hero.getHero2Grade()[i] = 0;
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	
	}
	
	public TankGame(String str){
		super(str);
		File file;
		try {
			file = new File(SystemInfo.gameFile);
			if(!file.exists())  //该目录不存在
			{
				file.mkdir();	//新建保存游戏的目录
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

		jmenuSave = new JMenuItem("保存(S)",'S');
		jmenuSave.addActionListener(this);
		jmenuContinue = new JMenuItem("继续(C)",'C');
		jmenuContinue.addActionListener(this);
		jmenuRank = new JMenu("查看排名(R)");
		jmenuRank.setMnemonic('R');
		jmenuRankOneMan = new JMenuItem("单人游戏(S)",'S');
		jmenuRankTwoMan = new JMenuItem("双人游戏(T)",'T');
		jmenuRankOneMan.addActionListener(this);
		jmenuRankTwoMan.addActionListener(this);
		jmenuRank.add(jmenuRankOneMan);
		jmenuRank.add(jmenuRankTwoMan);
		jmenuEditMap = new JMenuItem("编辑地图(E)",'E');
		jmenuUseMap	= new JMenuItem("使用地图(U)",'U');
		jmenuEditMap.addActionListener(this);
		jmenuUseMap.addActionListener(this);
		jmenuMap = new JMenu("地图(M)");
		jmenuMap.setMnemonic('M');
		jmenuMap.add(jmenuEditMap);
		jmenuMap.add(jmenuUseMap);
		jmenu = new JMenu("系统(X)");
		jmenu.setMnemonic('X');
		jmenu.add(jmenuSave);
		jmenu.add(jmenuContinue);
		jmenu.add(jmenuRank);
		jmenubar = new JMenuBar();
		jmenubar.add(jmenu);
		jmenubar.add(jmenuMap);
		this.setJMenuBar(jmenubar);
		
		jtb = new JToolBar();
		JLabel jlab = new JLabel("玩家一:A,S,W,D.射击:V/J  玩家二:←,→,↑,↑.射击:L/0  暂停/继续:空格.开始:回车 .调关:A,D,←,→.");
		jtb.add(jlab);
		jtb.setFloatable(false);
		add(jtb,BorderLayout.SOUTH);
		setResizable(false);
		//这里须获得jfX,jfY
		setBounds(SystemInfo.jfX,SystemInfo.jfY,SystemInfo.jfWidth,SystemInfo.jfHeight+SystemInfo.HeightOffset);
		//System.err.println("width:" + this.getWidth() + ",height:" + this.getHeight());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}
	
	public void paint(Graphics g){
		super.paint(g);
		//mjpGame.paint(g);		//可以不要这句，因为重绘该容器时，该容器上的所有组件都将被重绘
		//System.err.println("JFrame paint");
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true){
//			if(SystemInfo.fail)		//该线程不应该被退出，只有当程序被关闭时才结束该线程
//				break;
			//不应该在以下语句中加break，因为后面语句还要根据SystemInfo.fail的取值去唤醒其他线程！！
			if(SystemInfo.oneMan && Hero.getHero1Num() <= 0 || (!SystemInfo.oneMan && Hero.getHero1Num() <= 0 && Hero.getHero2Num() <= 0)){
				SystemInfo.fail = true;
			}
	
			
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
				Thread.sleep(50);
				repaint();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if(SystemInfo.afterModeSelect){
				synchronized(SystemInfo.afterModeSelectLock){	
					SystemInfo.afterModeSelectLock.notify();
				}
			}
			if(SystemInfo.start){
				synchronized(SystemInfo.startLock){		
					SystemInfo.startLock.notify();
				}
			}
			
//			System.err.println(Hero.getHero1Num() + "\t" + Hero.getHero2Num());
//			System.err.println("count:" + SystemInfo.rank.getCount() + "\tfail:" + SystemInfo.fail);
			if(SystemInfo.rank.getCount() <= 0){
				synchronized(SystemInfo.passRankLock){
					SystemInfo.passRankLock.notify();
				}
			}
			
		}
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		//文件信息按照敌军、英雄、障碍物、宝物顺序存放
		String filename = null;
		File file = null;
		try{
			file = new File(SystemInfo.gameFile);
			if(!file.exists())  //该目录不存在
			{
				file.mkdir();	
			}
			if(e.getSource() == jmenuSave){
				JFileChooser jfc = new JFileChooser();	
				jfc.setCurrentDirectory(file);
				FileNameExtensionFilter filter = new FileNameExtensionFilter("坦克游戏(*.lgl)","lgl");
				jfc.setFileFilter(filter);		//尾追过滤选择器，但将他作为默认选择器
				jfc.setAcceptAllFileFilterUsed(false);
				int ret = jfc.showSaveDialog(this);
				if(JFileChooser.APPROVE_OPTION == ret){
					filename = jfc.getSelectedFile().getAbsolutePath();
					if(-1 == filename.indexOf('.'))
						filename += "." + ((FileNameExtensionFilter)jfc.getFileFilter()).getExtensions()[0];
					//System.err.println("save game:" + filename);
					if(!SystemInfo.fileOperation.saveGame(filename)){
						JOptionPane.showMessageDialog(null, "保存游戏失败!");
					}
				}
			}
			else if(e.getSource() == jmenuContinue){
				JFileChooser jfc = new JFileChooser();	
				jfc.setCurrentDirectory(file);
				FileNameExtensionFilter filter = new FileNameExtensionFilter("坦克游戏(*.lgl)","lgl");
				jfc.setFileFilter(filter);		//尾追过滤选择器，但将他作为默认选择器
				jfc.setAcceptAllFileFilterUsed(false);
				int ret = jfc.showOpenDialog(this);
				if(JFileChooser.APPROVE_OPTION == ret){
					if(SystemInfo.fileOperation.restoreGame(jfc.getSelectedFile().getAbsolutePath()))
					{
						continueFromFile = true;
					}
					else{
						continueFromFile = false;
						JOptionPane.showMessageDialog(null, "文件格式错误!");
					}
				}
				
			}
			else if(e.getSource() == jmenuRankOneMan) //单人游戏
			{
				SystemInfo.fileOperation.showRank1();
			}
			else if(e.getSource() == jmenuRankTwoMan) //双人游戏
			{
				SystemInfo.fileOperation.showRank2();
			}
			else if(e.getSource() == jmenuEditMap)	//编辑地图
			{
				new EditMap();
			}
			else if(e.getSource() == jmenuUseMap)	//使用地图
			{
				JFileChooser jfc = new JFileChooser();	
				jfc.setCurrentDirectory(file);
				FileNameExtensionFilter filter = new FileNameExtensionFilter("地图(*.lmj)","lmj");
				jfc.setFileFilter(filter);		//尾追过滤选择器，但将他作为默认选择器
				jfc.setAcceptAllFileFilterUsed(false);
				int ret = jfc.showOpenDialog(this);
				if(JFileChooser.APPROVE_OPTION == ret){
					TankGame.setMapFileName(jfc.getSelectedFile().getAbsolutePath());
				}
			}
			else
			{
				
			}
		}
		catch(Exception ex){
			ex.printStackTrace();
		}

	}
	public static boolean isContinueFromFile() {
		return continueFromFile;
	}

	public static void setContinueFromFile(boolean continueFromFile) {
		TankGame.continueFromFile = continueFromFile;
	}
	

	public static String getMapFileName() {
		return mapFileName;
	}

	public static void setMapFileName(String mapFileName) {
		TankGame.mapFileName = mapFileName;
	}



	private JMenuBar jmenubar;
	private JMenu jmenu;
	private JMenuItem jmenuSave;
	private JMenuItem jmenuContinue;
	private JMenu jmenuRank;
	private JMenuItem jmenuRankOneMan;
	private JMenuItem jmenuRankTwoMan;
	private JMenuItem jmenuEditMap;
	private JMenuItem jmenuUseMap;
	private JMenu jmenuMap;
	private JToolBar jtb;
    private static boolean continueFromFile = false;
    private static String mapFileName = null;



}

