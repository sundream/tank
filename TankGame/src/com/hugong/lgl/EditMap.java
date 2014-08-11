package com.hugong.lgl;

import java.awt.*;
import java.awt.event.*;
import java.io.File;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

class EditMap extends JFrame implements MouseListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public EditMap(){
		super("��ͼ�༭");
		//map = new JPanel();
		map = new MapPanel();
		me = new JPanel();

		
		btnGrass = new JButton(new ImageIcon(getClass().getResource("/images/grass.jpg")));
		btnGrass.addMouseListener(this);
		btnRiver = new JButton(new ImageIcon(getClass().getResource("/images/river.jpg")));
		btnRiver.addMouseListener(this);
		btnWall = new JButton(new ImageIcon(getClass().getResource("/images/wall.jpg")));
		btnWall.addMouseListener(this);
		btnStoneWall = new JButton(new ImageIcon(getClass().getResource("/images/stonewall.jpg")));
		btnStoneWall.addMouseListener(this);
		jtbElement = new JToolBar();
		jtbElement.add(btnGrass);
		jtbElement.add(btnRiver);
		jtbElement.add(btnWall);
		jtbElement.add(btnStoneWall);
		jtbElement.setOrientation(SwingConstants.VERTICAL);
		jtbElement.setFloatable(false);
		jlabDemo = new JLabel(new ImageIcon(getClass().getResource("/images/none.jpg")));
		btnSaveMap = new JButton("�����ͼ");
		btnSaveMap.addMouseListener(this);

		JTextArea jta = new JTextArea();
		jta.setText("\n\nʹ��˵��:\n�Ҽ�����:���\n�Ҽ�˫��:�Ƴ�\n�Ҽ�����:ȡ����������\nCtlr+C:����\nCtrl+V:ճ��\nCtrl+Z:����\nCtrl+R:�ָ�\nע�������������1��");
		jta.setForeground(Color.red);
		jta.setEditable(false);
		//jta.setEnabled(false);	//����������ã���ôǰ��ɫ�ͻ�����ʧ��
		jta.setOpaque(false);	//����͸��

		me.add(jtbElement);
		me.add(jlabDemo);
		me.add(btnSaveMap);
		me.add(jta);
		me.setPreferredSize(new Dimension(SystemInfo.jfWidth-SystemInfo.plWidth,SystemInfo.jfHeight+SystemInfo.unitLength*2));
		
		this.add(map,BorderLayout.CENTER);
		this.add(me,BorderLayout.EAST);
		
		this.getContentPane().setBackground(Color.black);
		map.setPreferredSize(new Dimension(SystemInfo.plWidth,SystemInfo.plHeight));
		this.setBounds(SystemInfo.jfX,SystemInfo.jfY,SystemInfo.jfWidth+8,SystemInfo.jfHeight+SystemInfo.unitLength*2+2);
		this.setVisible(true);
		this.setResizable(false);
		this.addWindowListener(
				new WindowAdapter(){
					public void windowClosing(WindowEvent e){
						e.getWindow().dispose();
					}
				}
				);
		//����һ���ػ��߳�
		Thread t = new Thread(){
			public void run(){
				while(EditMap.this != null){
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					EditMap.this.repaint();
				}
			}
		};
		t.start();
	}
	

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == btnGrass){
			this.map.setSelectBarrierType(1);
			jlabDemo.setIcon(new ImageIcon(getClass().getResource("/images/grass.jpg")));
		}
		else if(e.getSource() == btnRiver){
			this.map.setSelectBarrierType(2);
			jlabDemo.setIcon(new ImageIcon(getClass().getResource("/images/river.jpg")));
		}
		else if(e.getSource() == btnWall){
			this.map.setSelectBarrierType(3);
			jlabDemo.setIcon(new ImageIcon(getClass().getResource("/images/wall.jpg")));
		}
		else if(e.getSource() == btnStoneWall){
			this.map.setSelectBarrierType(4);
			jlabDemo.setIcon(new ImageIcon(getClass().getResource("/images/stonewall.jpg")));
		}
		else if(e.getSource() == btnSaveMap){
			String filename;
			JFileChooser jfc = new JFileChooser();	
			File file = new File(SystemInfo.gameFile);
			if(!file.exists())  //��Ŀ¼������
			{
				file.mkdir();	
			}
			jfc.setCurrentDirectory(file);
			FileNameExtensionFilter filter = new FileNameExtensionFilter("��ͼ(*.lmj)","lmj");
			jfc.setFileFilter(filter);		//β׷����ѡ��������������ΪĬ��ѡ����
			jfc.setAcceptAllFileFilterUsed(false);
			int ret = jfc.showSaveDialog(this);
			if(JFileChooser.APPROVE_OPTION == ret){
				filename = jfc.getSelectedFile().getAbsolutePath();
				if(-1 == filename.indexOf('.'))
					filename += "." + ((FileNameExtensionFilter)jfc.getFileFilter()).getExtensions()[0];
				this.map.saveMap(filename);		//�����ͼ
			}
		}
		else{
			
		}
		jlabDemo.repaint();
		this.repaint();
	}
	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	private MapPanel map;
	private JPanel me;
	private JButton btnSaveMap;
	private JButton btnGrass;
	private JButton btnRiver;
	private JButton btnWall;
	private JButton btnStoneWall;
	private JLabel jlabDemo;
	private JToolBar jtbElement;

}

