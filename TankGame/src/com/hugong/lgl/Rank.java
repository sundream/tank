package com.hugong.lgl;

import java.io.Serializable;

class Rank implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7943983152908357058L;
	
	public Rank(){
		cur_rank =  0;
		randomEnemy();
	}
	
	private void randomEnemy(){
		count = 14 + (int)(Math.random()*7);	//Ĭ�ϲ���14-20��̹��
		//LOW_ARMOUR,MIDDLE_ARMOUR,FAST_SPEED,HIGH_ARMOUR 4������̹�˴�Ű�4/9,2/9,1/6,1/6�ķ���
		//ʣ��δ����̹����������HIGH_ARMOUR��FAST_SPEED̹��
		enemyTankNum[0] = 4 * count / 9;
		enemyTankNum[1] = 2 *count / 9;
		enemyTankNum[2] = count / 6;
		enemyTankNum[3] = count / 6;
		int left = count - enemyTankNum[0] - enemyTankNum[1] - enemyTankNum[2] - enemyTankNum[3];
		if(left % 2 == 0)
			enemyTankNum[0] += left;
		else
			enemyTankNum[3] += left;
		setWealthNum(2 + (int)(Math.random()*3));	//ÿ�س���2-4������
	}
	
	//�Ӽ���������0ֵ���⴦�������ֶ�����ʱ����0�أ����һ�أ�
	public void addRank(){
		randomEnemy();
		//System.err.println("count:" + count);
		this.cur_rank = (this.cur_rank+1) % SystemInfo.rankSumNum;
		if(0 == this.cur_rank)
			this.cur_rank = 1;
		//this.battleEnemyCount = 4 + this.cur_rank / 5;
	}
	public void minusRank(){
		randomEnemy();
		this.cur_rank = (this.cur_rank-1) % SystemInfo.rankSumNum;
		if(0 == this.cur_rank)
			this.cur_rank = SystemInfo.rankSumNum - 1 ;
		//this.battleEnemyCount = 4 + this.cur_rank / 5;
	}
	
	public int getCur_rank() {
		return cur_rank;
	}
	public void setCur_rank(int cur_rank) {
		this.cur_rank = cur_rank;
		randomEnemy();
		//this.battleEnemyCount = 4 + this.cur_rank / 5;
		
	}
	
	public int getBattleEnemyCount() {
		return battleEnemyCount;
	}
	
	public void setBattleEnemyCount(int battleEnemyCount) {
		this.battleEnemyCount = battleEnemyCount;
	}
	
	public void setEnemyTankNum(int[] enemyTankNum) {
		this.enemyTankNum = enemyTankNum;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	//������������������ͬ����������Ϊ���ڶ���ӵ��߳�ͨ��minusEnemyNumOfType����enemyTankNum
	public synchronized int[] getEnemyTankNum() {
		return enemyTankNum;
	}
	
	
	public int getWealthNum() {
		return wealthNum;
	}

	public void setWealthNum(int wealthNum) {
		this.wealthNum = wealthNum;
	}


	private int battleEnemyCount = 4;
	private int wealthNum = 2;		//���س��ֱ������(���ﲢ��һ����ȫ������)
	private int cur_rank = 0;
	private int count = 20;
	//LOW_ARMOUR,MIDDLE_ARMOUR,FAST_SPEED,HIGH_ARMOUR
	private int[] enemyTankNum = new int[SystemInfo.enemyTypeCount];  	//�ùغ���Ӧ����̹����

	
}

