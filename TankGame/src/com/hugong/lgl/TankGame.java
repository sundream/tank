/**
 * ���ܣ�̹�˴�ս��Ϸ
 * ���ߣ��ֹ���
 * ����ʱ�䣺 2012/5/24
 * �汾��1.0
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
			TankGame tg = new TankGame("̹�˴�ս��Ϸ");
			new Thread(tg).start();
			new Thread(new KeyBoarderThread()).start();		//���������߳�
			
			while(true){
				tg.jmenuSave.setEnabled(false);
				tg.jmenuContinue.setEnabled(true);
				tg.jmenuUseMap.setEnabled(false);
				
				if(!TankGame.isContinueFromFile()){
					SystemInfo.initSystemInfo();	//ϵͳ��Ϣ��ʼ��
					
					SelectGameMode selGamePanel = new SelectGameMode();
					tg.add(selGamePanel);
					tg.setVisible(true);
					tg.repaint();	//�������󣬿�����һ���̶�����������л�ʱ����˸Ч��
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
				//SystemInfo.rank.setCur_rank(0); //ϵͳ��Ϣ��ʼ��ʱĬ��Ϊ0�أ���˸þֿ���ȥ��
				while(!SystemInfo.fail)
				{
					if(!TankGame.isContinueFromFile()){
						//System.err.println(SystemInfo.rank.getCur_rank());
						if(null == TankGame.getMapFileName())	//ʹ�ñ�׼��ͼ���غ�Ӧ��������
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
						tg.setVisible(true);	//����������tg�ɼ�������ʾajustRankPanel���
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
						SystemInfo.fileOperation.generateChessBorder(SystemInfo.rank.getCur_rank());	//��ʼ������
					}
					else {
						//������ǰ�������Ϸʱ��ϵͳ����з����л������Զ�������Ϸ���
						//��ʱ���ش�����Ϸ���ͳ�ʼ����ͼ(��ͼ��״Ӧ���Ǳ�����Ϸʱ����״)
						//�ֶ������Ӵ��̶�������ĵо����ӵ��߳�
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
					//�ָ�Ӣ����ӵ�еı���
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
					//dkbfm.focusNextComponent(SystemInfo.mjpGame); //Ϊʲô����仹Ҫ��������С������󻯺���������ü������뽹��
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
					
					if(!SystemInfo.fail)	//������
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
				
					//��ռ��̻���ȥ
					KeyBoarderThread.clearKeyBoarderBuffer();
					//��������ǰ��Ϸ�����ϵͳ�ָ���������
					if(TankGame.isContinueFromFile())
						TankGame.setContinueFromFile(false);
					//��ʹ���Զ����ͼ����������Զ����ͼΪ��
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
					//ÿ������һ��(������ʧ�ܻ��ǳɹ�)����hero1,hero2ÿ��ɱ�и������
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
			if(!file.exists())  //��Ŀ¼������
			{
				file.mkdir();	//�½�������Ϸ��Ŀ¼
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

		jmenuSave = new JMenuItem("����(S)",'S');
		jmenuSave.addActionListener(this);
		jmenuContinue = new JMenuItem("����(C)",'C');
		jmenuContinue.addActionListener(this);
		jmenuRank = new JMenu("�鿴����(R)");
		jmenuRank.setMnemonic('R');
		jmenuRankOneMan = new JMenuItem("������Ϸ(S)",'S');
		jmenuRankTwoMan = new JMenuItem("˫����Ϸ(T)",'T');
		jmenuRankOneMan.addActionListener(this);
		jmenuRankTwoMan.addActionListener(this);
		jmenuRank.add(jmenuRankOneMan);
		jmenuRank.add(jmenuRankTwoMan);
		jmenuEditMap = new JMenuItem("�༭��ͼ(E)",'E');
		jmenuUseMap	= new JMenuItem("ʹ�õ�ͼ(U)",'U');
		jmenuEditMap.addActionListener(this);
		jmenuUseMap.addActionListener(this);
		jmenuMap = new JMenu("��ͼ(M)");
		jmenuMap.setMnemonic('M');
		jmenuMap.add(jmenuEditMap);
		jmenuMap.add(jmenuUseMap);
		jmenu = new JMenu("ϵͳ(X)");
		jmenu.setMnemonic('X');
		jmenu.add(jmenuSave);
		jmenu.add(jmenuContinue);
		jmenu.add(jmenuRank);
		jmenubar = new JMenuBar();
		jmenubar.add(jmenu);
		jmenubar.add(jmenuMap);
		this.setJMenuBar(jmenubar);
		
		jtb = new JToolBar();
		JLabel jlab = new JLabel("���һ:A,S,W,D.���:V/J  ��Ҷ�:��,��,��,��.���:L/0  ��ͣ/����:�ո�.��ʼ:�س� .����:A,D,��,��.");
		jtb.add(jlab);
		jtb.setFloatable(false);
		add(jtb,BorderLayout.SOUTH);
		setResizable(false);
		//��������jfX,jfY
		setBounds(SystemInfo.jfX,SystemInfo.jfY,SystemInfo.jfWidth,SystemInfo.jfHeight+SystemInfo.HeightOffset);
		//System.err.println("width:" + this.getWidth() + ",height:" + this.getHeight());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}
	
	public void paint(Graphics g){
		super.paint(g);
		//mjpGame.paint(g);		//���Բ�Ҫ��䣬��Ϊ�ػ������ʱ���������ϵ���������������ػ�
		//System.err.println("JFrame paint");
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true){
//			if(SystemInfo.fail)		//���̲߳�Ӧ�ñ��˳���ֻ�е����򱻹ر�ʱ�Ž������߳�
//				break;
			//��Ӧ������������м�break����Ϊ������仹Ҫ����SystemInfo.fail��ȡֵȥ���������̣߳���
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
		//�ļ���Ϣ���յо���Ӣ�ۡ��ϰ������˳����
		String filename = null;
		File file = null;
		try{
			file = new File(SystemInfo.gameFile);
			if(!file.exists())  //��Ŀ¼������
			{
				file.mkdir();	
			}
			if(e.getSource() == jmenuSave){
				JFileChooser jfc = new JFileChooser();	
				jfc.setCurrentDirectory(file);
				FileNameExtensionFilter filter = new FileNameExtensionFilter("̹����Ϸ(*.lgl)","lgl");
				jfc.setFileFilter(filter);		//β׷����ѡ��������������ΪĬ��ѡ����
				jfc.setAcceptAllFileFilterUsed(false);
				int ret = jfc.showSaveDialog(this);
				if(JFileChooser.APPROVE_OPTION == ret){
					filename = jfc.getSelectedFile().getAbsolutePath();
					if(-1 == filename.indexOf('.'))
						filename += "." + ((FileNameExtensionFilter)jfc.getFileFilter()).getExtensions()[0];
					//System.err.println("save game:" + filename);
					if(!SystemInfo.fileOperation.saveGame(filename)){
						JOptionPane.showMessageDialog(null, "������Ϸʧ��!");
					}
				}
			}
			else if(e.getSource() == jmenuContinue){
				JFileChooser jfc = new JFileChooser();	
				jfc.setCurrentDirectory(file);
				FileNameExtensionFilter filter = new FileNameExtensionFilter("̹����Ϸ(*.lgl)","lgl");
				jfc.setFileFilter(filter);		//β׷����ѡ��������������ΪĬ��ѡ����
				jfc.setAcceptAllFileFilterUsed(false);
				int ret = jfc.showOpenDialog(this);
				if(JFileChooser.APPROVE_OPTION == ret){
					if(SystemInfo.fileOperation.restoreGame(jfc.getSelectedFile().getAbsolutePath()))
					{
						continueFromFile = true;
					}
					else{
						continueFromFile = false;
						JOptionPane.showMessageDialog(null, "�ļ���ʽ����!");
					}
				}
				
			}
			else if(e.getSource() == jmenuRankOneMan) //������Ϸ
			{
				SystemInfo.fileOperation.showRank1();
			}
			else if(e.getSource() == jmenuRankTwoMan) //˫����Ϸ
			{
				SystemInfo.fileOperation.showRank2();
			}
			else if(e.getSource() == jmenuEditMap)	//�༭��ͼ
			{
				new EditMap();
			}
			else if(e.getSource() == jmenuUseMap)	//ʹ�õ�ͼ
			{
				JFileChooser jfc = new JFileChooser();	
				jfc.setCurrentDirectory(file);
				FileNameExtensionFilter filter = new FileNameExtensionFilter("��ͼ(*.lmj)","lmj");
				jfc.setFileFilter(filter);		//β׷����ѡ��������������ΪĬ��ѡ����
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

