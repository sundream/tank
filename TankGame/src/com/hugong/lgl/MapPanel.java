package com.hugong.lgl;

import java.awt.*;
import java.awt.event.*;
import java.io.*;


import javax.swing.JPanel;

class MapPanel extends JPanel implements MouseListener,MouseMotionListener,KeyListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public MapPanel(){
		for(int i = 0; i < 3; ++i){
			enemys[i] = new EnemyTank(SystemInfo.enmyLocation[i].x,SystemInfo.enmyLocation[i].y,
					Direction.DIR_DOWN,TankType.FAST_SPEED);
		}
		for(int i = 0; i < SystemInfo.homeProtectBarriersNum; ++i){
			this.arrMap[SystemInfo.homeProtectBarriers[i].x/SystemInfo.unitLength]
					[SystemInfo.homeProtectBarriers[i].y/SystemInfo.unitLength] = 3;	//��ǽ
		}
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		this.addKeyListener(this);
	}
	
	public void paint(Graphics g){
		Color oldColor = g.getColor();
		
		if(begin != null && end != null){
			g.setColor(new Color(128,128,255));
			g.fillRect(begin.x, begin.y, end.x-begin.x, end.y-begin.y);	 //��ѡ���������ɫ
			
		}
		g.setColor(Color.white);
		for(int i = 0; i <= SystemInfo.wh ; ++i){	//��
			g.drawLine(0,i*SystemInfo.unitLength, SystemInfo.wh*SystemInfo.unitLength,i*SystemInfo.unitLength);
		}
		for(int j = 0; j <= SystemInfo.wh; ++j){	//��
			g.drawLine(j*SystemInfo.unitLength,0, j*SystemInfo.unitLength,SystemInfo.wh*SystemInfo.unitLength);
		}
		g.setColor(oldColor);
		
		for(int i = 0; i < 3; ++i)
			enemys[i].drawTank(g);
		hero1.drawTank(g);
		hero2.drawTank(g);
		for(int i = 0; i < SystemInfo.homeProtectBarriersNum; ++i)
		{
			new Barrier(SystemInfo.homeProtectBarriers[i].x,SystemInfo.homeProtectBarriers[i].y
					,BarrierType.BOTH_FORBIN1).drawBarrier(g);
		}
		SystemInfo.imageOperation.drawImage(g, "livehome.jpg", new Point((SystemInfo.wh/2-1)*SystemInfo.unitLength,
					(SystemInfo.wh-2)*SystemInfo.unitLength));
		for(int i = 0; i < SystemInfo.wh; ++i){
			for(int j = 0; j < SystemInfo.wh; ++j){
				if(this.arrMap[i][j] > 0){
					new Barrier(i*SystemInfo.unitLength,j*SystemInfo.unitLength,
							SystemInfo.getBarrierTypeByInt(arrMap[i][j])).drawBarrier(g);
				}
			}
		}
		if(begin != null && end != null){
			g.setColor(Color.red);
			//ע��java���������ͱ�����Ϊ����ֵ������ֱ��Point temp1 = begin
			Point temp1 = new Point(begin);
			Point temp2 = new Point(end);
			this.ajustToRegularRectangle(temp1, temp2);
			if(temp1.x != temp2.x && temp1.y != temp2.y)	//��ֹ�������һ��ֱ��
			{
				
				g.drawRect(temp1.x,temp1.y,temp2.x-temp1.x,temp2.y-temp1.y);	//��ѡ������߿�
				if(copy && copyBegin != null && copyEnd != null && Math.random() <= 0.6)	//copy����
					g.drawRect(copyBegin.x,copyBegin.y,copyEnd.x-copyBegin.x,copyEnd.y-copyBegin.y);	//��ѡ������߿�
			}
		}
		
	}

	public int[][] getArrMap() {
		return arrMap;
	}

	public void setArrMap(int[][] arrMap) {
		this.arrMap = arrMap;
	}
	
	public boolean saveMap(String filename){
		boolean ret = false;
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(filename);
			for(int i = 0; i < SystemInfo.wh; ++i){
				for(int j = 0; j < SystemInfo.wh; ++j){
					fos.write(arrMap[j][i] + 48);	//��ΪarrMap�����������ж�������ת90�ȵ��ӽ�
					if(j != SystemInfo.wh-1)
					{
						 fos.write(',');
					}
					else	
					{
						//д��س���
						fos.write(0x0D);
						fos.write(0x0A);
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			try{
				if(fos != null)
					fos.close();
			}catch (Exception e) {
				e.printStackTrace();
			}
		
		}
		
		return ret;
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		if(MouseEvent.BUTTON3 == e.getButton()){	
			if(1 == e.getClickCount()){
				//�����Ҽ���ȡ����������
				copy = false;	
				copyBegin = copyEnd = null;
				if(this.isAtSelectedRegion(e.getPoint())){
					//ѡ�����ϰ���
					if(1 <= this.selectBarrierType && this.selectBarrierType <= 4 && begin != null && end != null){	 
						this.copyArrMap = this.copyMap(arrMap,SystemInfo.wh,SystemInfo.wh);	
						for(int x = begin.x; x < end.x; x += SystemInfo.unitLength){
							for(int y = begin.y; y < end.y; y += SystemInfo.unitLength){
								if(!this.isAtFixedRegion(new Point(x,y)))	//���ڹ̶�����Χ��
									this.arrMap[x/SystemInfo.unitLength][y/SystemInfo.unitLength] = 
										this.selectBarrierType;
							}
						}
					}
				}
				
			}
			else if(2 == e.getClickCount()){	
				if(this.isAtSelectedRegion(e.getPoint())){
					if(begin != null && end != null){
						this.copyArrMap = this.copyMap(arrMap,SystemInfo.wh,SystemInfo.wh);
						for(int x = begin.x; x < end.x; x += SystemInfo.unitLength){
							for(int y = begin.y; y < end.y; y += SystemInfo.unitLength){
								this.arrMap[x/SystemInfo.unitLength][y/SystemInfo.unitLength] = 0;
							}
						}
					}
				}

			}
			else{
				
			}
			this.getParent().repaint();
			
		}
	
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		//ÿ�ε����������뽹��
		this.setFocusable(true);
		this.requestFocus();
		if(MouseEvent.BUTTON3 != e.getButton())
		{
			flag = true;
			end = null;
			begin = e.getPoint();
			begin.x = begin.x - begin.x % SystemInfo.unitLength;
			begin.y = begin.y - begin.y % SystemInfo.unitLength;		
			//System.err.println("begin:" + begin);
		}

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		if(MouseEvent.BUTTON3 != e.getButton())
		{
			flag = false;
			end = e.getPoint();
			end.x = ((end.x + SystemInfo.unitLength-1)/SystemInfo.unitLength)*SystemInfo.unitLength;
			end.y = ((end.y + SystemInfo.unitLength-1)/SystemInfo.unitLength)*SystemInfo.unitLength;
			//��þ��α�׼����
			if(begin != null && end != null)	//��ȫ���
			{
				this.ajustToRegularRectangle(begin, end);
			}
			//һ��Ҫ�ñ��������������ػ棬�������ֲ���Ч��
			//this.repaint();
			this.getParent().repaint();
			//System.err.println("end:" + end);
		}
		
	}
	/*private void ajustBeginPointAtDrag(Point begin,Point end){
		if(begin != null && end != null){
			System.err.println("old end:" + end);
			System.err.println("old begin:" + begin);
			//��4�����ȷ��begin
			if(end.x >= begin.x && end.y >= begin.y){
				begin.x = begin.x - begin.x % SystemInfo.unitLength;
				begin.y = begin.y - begin.y % SystemInfo.unitLength;	
				end.x = ((end.x + SystemInfo.unitLength-1)/SystemInfo.unitLength)*SystemInfo.unitLength;
				end.y = ((end.y + SystemInfo.unitLength-1)/SystemInfo.unitLength)*SystemInfo.unitLength;
			}
			else if(end.x >= begin.x && end.y < begin.y){
				begin.x = begin.x - begin.x % SystemInfo.unitLength;
				begin.y = ((begin.y+SystemInfo.unitLength-1)/SystemInfo.unitLength)*SystemInfo.unitLength;
				end.x = ((end.x + SystemInfo.unitLength-1)/SystemInfo.unitLength)*SystemInfo.unitLength;
				end.y = end.y - end.y % SystemInfo.unitLength;
			}
			else if(end.x < begin.x && end.y >= begin.y){
				begin.x = ((begin.x+SystemInfo.unitLength-1)/SystemInfo.unitLength)*SystemInfo.unitLength;
				begin.y = begin.y - begin.y % SystemInfo.unitLength;
				end.x = end.x - end.x % SystemInfo.unitLength;
				end.y = ((end.y + SystemInfo.unitLength-1)/SystemInfo.unitLength)*SystemInfo.unitLength;
			}
			else //if(end.x < begin.x && end.y < begin.y)
			{
				begin.x = ((begin.x+SystemInfo.unitLength-1)/SystemInfo.unitLength)*SystemInfo.unitLength;
				begin.y = ((begin.y+SystemInfo.unitLength-1)/SystemInfo.unitLength)*SystemInfo.unitLength;
				end.x = end.x - end.x % SystemInfo.unitLength;
				end.y = end.y - end.y % SystemInfo.unitLength;
			}
			
			System.err.println("new begin:" + begin);
			System.err.println("new end:" + end);
		}
		//return new Point(begin);
	}
	*/
	
	private int[][] copyMap(int [][] map,int w,int h){
		int temp[][] = new int[w][h];
		for(int i = 0; i < w; ++i){
			for(int j = 0; j < h; ++j){
				temp[i][j] = map[i][j];
			}
		}
		return temp;
	}
		
	private void ajustToRegularRectangle(Point temp1,Point temp2){
		int t;
		if(temp2.x < temp1.x)
		{	
			t = temp1.x;
			temp1.x = temp2.x;
			temp2.x = t;
		}
		if(temp2.y < temp1.y)
		{
			t = temp1.y;
			temp1.y = temp2.y;
			temp2.y = t;
		}
		//��ֹ�������곬����ͼ��Χ
		temp1.x = 0 > temp1.x ? 0 : temp1.x;
		temp2.x = SystemInfo.wh*SystemInfo.unitLength < temp2.x ? SystemInfo.wh*SystemInfo.unitLength : temp2.x;
		temp1.y = 0 > temp1.y ? 0 : temp1.y;
		temp2.y = SystemInfo.wh*SystemInfo.unitLength < temp2.y ? SystemInfo.wh*SystemInfo.unitLength : temp2.y;
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		//if(MouseEvent.BUTTON3 != e.getButton())	//����mouseDragged�������ԣ�e.getButton()������
		if(flag)
		{
			end = e.getPoint();
	
			end.x = ((end.x + SystemInfo.unitLength-1)/SystemInfo.unitLength)*SystemInfo.unitLength;
			end.y = ((end.y + SystemInfo.unitLength-1)/SystemInfo.unitLength)*SystemInfo.unitLength;
			this.getParent().repaint();
			//System.err.println("end:" + end);
		}
		//System.err.println(MouseEvent.BUTTON1 == e.getButton());
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public int getSelectBarrierType() {
		return selectBarrierType;
	}

	public void setSelectBarrierType(int selectBarrierType) {
		this.selectBarrierType = selectBarrierType;
	}
	//�жϵ��Ƿ���ѡ��������
	private boolean isAtSelectedRegion(Point p){
		boolean ret = false;
		if(begin != null && end != null){
			if(begin.x <= p.x && p.x < end.x && begin.y <= p.y && p.y < end.y)
				ret = true;
		}
		return ret;
	}
	
	//�жϵ��Ƿ��ڹ̶������ڣ�ͨ���жϵ��Ӧ�����±��Ƿ��ڹ̶�������ʵ�֣�
	private boolean isAtFixedRegion(Point p){
		boolean ret = false;
		//ע��̹�˻�׼����ϰ����׼�㲻һ����̹����������Ϊ��׼�㣬�ϰ����������Ͻ�����Ϊ��׼��
		//�Ƿ��ڵо�̹�˳���λ��
		for(int i = 0; i < 3; ++i){
			if(SystemInfo.enmyLocation[i].x-SystemInfo.tankWidth/2<= p.x 
					&& p.x < SystemInfo.enmyLocation[i].x + SystemInfo.tankWidth/2
					&& SystemInfo.enmyLocation[i].y-SystemInfo.tankHeight/2 <= p.y
					&& p.y < SystemInfo.enmyLocation[i].y+SystemInfo.tankHeight/2)
			{
				ret = true;
				break;
			}
		}
		if(!ret){	
			//�Ƿ���Ӣ�۳���λ��
			if(SystemInfo.hero1Location.x-SystemInfo.tankWidth/2<= p.x 
					&& p.x < SystemInfo.hero1Location.x + SystemInfo.tankWidth/2
					&& SystemInfo.hero1Location.y-SystemInfo.tankHeight/2 <= p.y
					&& p.y < SystemInfo.hero1Location.y+SystemInfo.tankHeight/2)
			{
				ret = true;
			}
			else if(SystemInfo.hero2Location.x-SystemInfo.tankWidth/2<= p.x 
					&& p.x < SystemInfo.hero2Location.x + SystemInfo.tankWidth/2
					&& SystemInfo.hero2Location.y-SystemInfo.tankHeight/2 <= p.y
					&& p.y < SystemInfo.hero2Location.y+SystemInfo.tankHeight/2)
			{
				ret = true;
			}
		}
		if(!ret){
			//�Ƿ��ڼ�԰����λ��
			if(SystemInfo.homeProtectBarriers[2].x <= p.x 
					&& p.x <= SystemInfo.homeProtectBarriers[7].x 
					&& SystemInfo.homeProtectBarriers[2].y <= p.y
					&& p.y <=  SystemInfo.homeProtectBarriers[7].y )
			{
				ret = true;
			}
		}
		return ret;
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		if(KeyEvent.VK_C == e.getKeyCode() && e.isControlDown())
		{
			copy = true;
			if(begin != null && end != null){
				//Ϊ�Է��û���ѡ������Ϊ�ͷ���������°��¸��ư�ťCtrl+C,���ǽ���������(begin,end)�������������
				this.ajustToRegularRectangle(begin, end);
				copyBegin = new Point(begin);
				copyEnd = new Point(end);
			}
		}
		else if(KeyEvent.VK_V == e.getKeyCode() && e.isControlDown()){	//Ctrl+V
			if(copy && begin != null && end != null){
				this.copyArrMap = this.copyMap(arrMap,SystemInfo.wh,SystemInfo.wh);
				this.ajustToRegularRectangle(begin, end);
				int x,y,dx,dy;
				Point myEnd = new Point(end);
				Point myCopyEnd = new Point(copyEnd);
				//ճ������endֵ����beginֵȷ��(��Ȼ����Խ��)
				myEnd.x = SystemInfo.wh*SystemInfo.unitLength < (begin.x+copyEnd.x-copyBegin.x) ? 
						SystemInfo.wh*SystemInfo.unitLength : (begin.x+copyEnd.x-copyBegin.x);
				myEnd.y = SystemInfo.wh*SystemInfo.unitLength < (begin.y+copyEnd.y-copyBegin.y) ? 
						SystemInfo.wh*SystemInfo.unitLength : (begin.y+copyEnd.y-copyBegin.y);	
				//��������endֵ��ճ������begin��endֵȷ��(��Ȼ����Խ��)
				myCopyEnd.x = copyBegin.x + myEnd.x - begin.x;
				myCopyEnd.y = copyBegin.y + myEnd.y - begin.y;
				if(begin.y <= copyBegin.y){
					for(x = copyBegin.x,dx = begin.x; x < myCopyEnd.x && dx < myEnd.x; x += SystemInfo.unitLength,dx += SystemInfo.unitLength){
						for(y = copyBegin.y,dy = begin.y; y < myCopyEnd.y && dy < myEnd.y; y += SystemInfo.unitLength,dy += SystemInfo.unitLength){
							if(!this.isAtFixedRegion(new Point(x,y)) && !this.isAtFixedRegion(new Point(dx,dy)))	//���ڹ̶�����Χ��
							{
								arrMap[dx/SystemInfo.unitLength][dy/SystemInfo.unitLength]
										= arrMap[x/SystemInfo.unitLength][y/SystemInfo.unitLength];
							}
						}
					}
				}
				else{
					for(x = myCopyEnd.x-SystemInfo.unitLength,dx = myEnd.x-SystemInfo.unitLength; x >= copyBegin.x && dx >= begin.x; x -= SystemInfo.unitLength,dx -= SystemInfo.unitLength){
						for(y = myCopyEnd.y-SystemInfo.unitLength,dy = myEnd.y-SystemInfo.unitLength; y >= copyBegin.y && dy >= begin.y; y -= SystemInfo.unitLength,dy -= SystemInfo.unitLength){
							if(!this.isAtFixedRegion(new Point(x,y)) && !this.isAtFixedRegion(new Point(dx,dy)))	//���ڹ̶�����Χ��
							{
								arrMap[dx/SystemInfo.unitLength][dy/SystemInfo.unitLength]
										= arrMap[x/SystemInfo.unitLength][y/SystemInfo.unitLength];
							}
						}
					}
				}
			}
		}
		else if(KeyEvent.VK_Z== e.getKeyCode() && e.isControlDown()){	//Ctrl+Z
			int temp[][] =  this.copyMap(arrMap,SystemInfo.wh,SystemInfo.wh);
			arrMap = copyArrMap;
			copyArrMap = temp;
			cltrz = true;
			//System.err.println("ctlr+z");
		}
		else if(KeyEvent.VK_R == e.getKeyCode() && e.isControlDown()){	//Ctrl+R
			if(cltrz)	//�����г���ʱ���лָ�
			{
				int temp[][] =  this.copyMap(arrMap,SystemInfo.wh,SystemInfo.wh);
				arrMap = copyArrMap;
				copyArrMap = temp;
				cltrz = false;
				//System.err.println("ctlr+r");
			}
		}
		else{
			
		}
		this.getParent().repaint();
		//System.err.println("key down");
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
	}
	
	private int arrMap[][] = new int[SystemInfo.wh][SystemInfo.wh];
	private int copyArrMap[][] = new int[SystemInfo.wh][SystemInfo.wh];
	private Hero hero1  = new Hero(SystemInfo.hero1Location.x,SystemInfo.hero1Location.y,
			Direction.DIR_UP,TankType.HERO,Color.green);
	private Hero hero2 = new Hero(SystemInfo.hero2Location.x,SystemInfo.hero2Location.y,
			Direction.DIR_UP,TankType.HERO,Color.yellow);
	private EnemyTank enemys[] = new EnemyTank[3];
	private Point begin;
	private Point end;
	private int selectBarrierType = -1;
	private boolean flag = false;
	private boolean copy = false;
	private boolean cltrz = false;
	private Point copyBegin = new Point();
	private Point copyEnd = new Point();

}
