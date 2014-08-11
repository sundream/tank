package com.hugong.lgl;

import java.io.*;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JOptionPane;



class FileOperation {
	public boolean saveGame(String filename){
		boolean ret = false;
		//FileOutputStream os = null;
		DataOutputStream os = null;
		ObjectOutputStream oos = null;
		try {
			//os = new FileOutputStream(filename);
			os = new DataOutputStream(new FileOutputStream(filename));
			os.writeInt(520520);		//д���ļ�ͷ����Ϣ�Ա�ʾ���ļ���ʽ
			oos = new ObjectOutputStream(os);
			if(SystemInfo.mjpGame != null){
				oos.writeObject(SystemInfo.mjpGame);
			}

			os.writeBoolean(SystemInfo.start);
			os.writeBoolean(SystemInfo.afterModeSelect);
			os.writeBoolean(SystemInfo.afterStatistic);
			os.writeBoolean(SystemInfo.stop);
			os.writeBoolean(SystemInfo.fail);
			os.writeBoolean(SystemInfo.oneMan);
			oos = new ObjectOutputStream(os);
			oos.writeObject(SystemInfo.rank);
			for(int i = 0; i < SystemInfo.enemyTypeCount; ++i)
			{
				os.writeInt(Hero.getHero1Grade()[i]);
			}

			os.writeInt(Hero.getHero1SumGrade());
			os.writeInt(Hero.getHero1Num());		
			for(int i = 0; i < SystemInfo.enemyTypeCount; ++i)
			{
				os.writeInt(Hero.getHero2Grade()[i]);
			}
			os.writeInt(Hero.getHero2SumGrade());
			os.writeInt(Hero.getHero2Num());
			
			ret = true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally{
			try{
				if(oos != null)
					oos.close();
				if(os != null)
					os.close();
			}
			catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	
		}

		return ret;
	}
	
	public boolean restoreGame(String filename){
		boolean ret = false;
		int fileHeader;
		DataInputStream is = null;
		ObjectInputStream ois = null;
		try {
			//is = new FileInputStream(filename);
			is = new DataInputStream(new FileInputStream(filename));
			if(is.available() <= 0)		//���ļ�
				return ret;
			fileHeader = is.readInt();
			if(fileHeader != 520520)
			{
				return ret;
			}
			
			ois = new ObjectInputStream(is);
			SystemInfo.mjpGame = (GamePanel)ois.readObject();
		
			SystemInfo.start = is.readBoolean();
			SystemInfo.afterModeSelect = is.readBoolean();
			SystemInfo.afterStatistic = is.readBoolean();
			SystemInfo.stop = is.readBoolean();
			SystemInfo.fail = is.readBoolean();
			SystemInfo.oneMan = is.readBoolean();
			//System.err.println("afterModeSelect:" + SystemInfo.afterModeSelect + "\tstart:" +SystemInfo.start );
			//System.err.println("one man:" + SystemInfo.oneMan + "\tfail:" + SystemInfo.fail + "\tstop:" + SystemInfo.stop);
			ois = new ObjectInputStream(is);
			SystemInfo.rank = (Rank)ois.readObject();
			
			for(int i = 0; i < SystemInfo.enemyTypeCount; ++i)
			{
				Hero.getHero1Grade()[i] = is.readInt();
			}
			Hero.setHero1SumGrade(is.readInt());
			Hero.setHero1Num(is.readInt());
			
			for(int i = 0; i < SystemInfo.enemyTypeCount; ++i)
			{
				Hero.getHero2Grade()[i] = is.readInt();
			}
			Hero.setHero2SumGrade(is.readInt());
			Hero.setHero2Num(is.readInt());
			
			ret = true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally{
			try{
				if(ois != null)
					ois.close();
				if(is != null)
					is.close();
	
			}
			catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	
		}

		return ret;
	}
	
	public  boolean generateChessBorder(int rank){
		boolean ret = false;
		String filename;
		DataInputStream dis = null;
		boolean fileExist =  false;
		if(null == TankGame.getMapFileName())		//ʹ�ñ�׼��ͼ
		{
			filename = "/data/chessborder" + rank + ".lmj";
			if(getClass().getResource(filename) != null)
				fileExist = true;
		}
		else										//ʹ���Զ����ͼ
		{
			filename = TankGame.getMapFileName();
			//System.err.println(filename);
			if(new File(filename).exists())
				fileExist = true;
			
		}
		
		try {
			//url = getClass().getResource(filename);
			if(fileExist){
				if(null == TankGame.getMapFileName())		//ʹ�ñ�׼��ͼ
				{
					dis = new DataInputStream(getClass().getResourceAsStream(filename));
			
				}
				else									//ʹ���Զ����ͼ
				{
					dis = new DataInputStream(new FileInputStream(filename));
				}
				//System.err.println(dis == null);
				for(int i = 0; i < SystemInfo.wh; ++i){
					for(int j = 0; j < SystemInfo.wh; ++j){
						
						Barrier barrier = null;
						BarrierType barrierType = SystemInfo.getBarrierTypeByInt( dis.readByte()-48);
						//System.err.print(barrierType + " ");
						if(BarrierType.NONE != barrierType){
							barrier = new Barrier(j*SystemInfo.unitLength,i*SystemInfo.unitLength,
									barrierType);
							SystemInfo.mjpGame.getBarriers().add(barrier);
						}
						dis.skipBytes(1);
						if(j == SystemInfo.wh-1)	//��ĩ�س���ռ�����ֽ�
							dis.skipBytes(1);
					}
					//System.err.println();
				}
			}
			else{
				JOptionPane.showMessageDialog(null, "�õ�ͼ������!");
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			try {
				if(dis != null)
					dis.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return ret;
	}
	
	public void showRank1(){
		String message = "";
		long grade,time;
		int minute,r;
		String filename = SystemInfo.gameFile + "rank1.txt";
		Calendar cal = Calendar.getInstance();
		File file = null;
		DataInputStream dis = null;
		try {
			message = formatData("����") + formatData("�ɼ�") + formatData("����") + "\n\r";
			file = new File(filename);
			if(file.exists()){
				dis = new DataInputStream(new FileInputStream(file));
				r = 0;
				while(dis.available() > 0){
					++r;
					grade = dis.readLong();
					time = dis.readLong();
					cal.setTime(new Date(time));
					minute = cal.get(Calendar.MINUTE);
					message += formatData(String.valueOf(r)) + formatData(String.valueOf(grade))
						+ formatData("") + cal.get(Calendar.YEAR) + "/" + cal.get(Calendar.MONTH) + "/" + cal.get(Calendar.DAY_OF_MONTH) + " " 
					    + cal.get(Calendar.HOUR_OF_DAY) + ":" +  (minute < SystemInfo.rankShowNum ? "0" + minute : minute)+ "\n\r";
				}
			}
			JOptionPane.showMessageDialog(null, message,"������Ϸ����",JOptionPane.PLAIN_MESSAGE);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			try{
				if(dis != null)
					dis.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	//��������
	public void insertRank1(){
		DataInputStream dis = null;
		DataOutputStream dos = null;
		File file = null;
		String filename = SystemInfo.gameFile + "rank1.txt";
		try {
			int r = 0;
			long arr[][] = new long[SystemInfo.rankShowNum][2];
			boolean one = true;
			
			file = new File(filename);
			if(file.exists()){
				dis = new DataInputStream(new FileInputStream(file));
				for(r = 0; r < SystemInfo.rankShowNum && dis.available() > 0;++r){
					arr[r][0] = dis.readLong();
					arr[r][1] = dis.readLong();
					if(one && Hero.getHero1SumGrade() > arr[r][0])
					{
						//����û��ɼ��պñ����һ���������жϾͻ�Խ��
						if(r+1 < SystemInfo.rankShowNum){
							arr[r+1][0] = arr[r][0];
							arr[r+1][1] = arr[r][1];
						}
					
						arr[r][0] = Hero.getHero1SumGrade();
						arr[r][1] = new Date().getTime();
						//��Ϊ������ܻ��õ�r����������ж��Ǳ���ģ�Ҳ�Ƿ����߼���
						if(r+1 < SystemInfo.rankShowNum) //�Ᵽ֤��r <= SystemInfo.rankShowNum
							++r;
						one = false;
					}
				}
			}

			if(r < SystemInfo.rankShowNum && one && Hero.getHero1SumGrade() > 0){
				//0�ֲ�������û����
				arr[r][0] = Hero.getHero1SumGrade();
				arr[r][1] = new Date().getTime();
				++r;
				one  = false;
			}
			
			if(!one)	//�óɼ����������ˣ����ļ��е�������Ҫ�޸ģ�
			{
				if(dis != null)
					dis.close();
				if(file.exists())
					file.delete();
				//System.err.println("rank2 new file:" + file.getAbsolutePath());
				file.createNewFile();
				dos =   new DataOutputStream(new FileOutputStream(file));
				for(int i = 0; i < r; ++i){
					dos.writeLong(arr[i][0]);
					dos.writeLong(arr[i][1]);
				}
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			try{
				if(dis != null)
					dis.close();
				if(dos != null)
					dos.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	public void showRank2(){
		String message = "";
		String filename = SystemInfo.gameFile + "rank2.txt";
		long sumgrade,grade1,grade2,time;
		int minute,r;
		Calendar cal = Calendar.getInstance();
		File file = null;
		DataInputStream dis = null;
		try {
			message = formatData("����") + formatData("�ܳɼ�") + formatData("Hero1�ɼ�") + 
					formatData("Hero2�ɼ�") + formatData("����") + "\n\r";
			file = new File(filename);
			if(file.exists()){
				dis = new DataInputStream(new FileInputStream(file));
				r = 0;
				while(dis.available() > 0){
					++r;
					sumgrade = dis.readLong();
					grade1 = dis.readLong();
					grade2 = dis.readLong();
					time = dis.readLong();
					cal.setTime(new Date(time));
					minute = cal.get(Calendar.MINUTE);
					message += formatData(String.valueOf(r)) + formatData(String.valueOf(sumgrade)) +
							formatData(String.valueOf(grade1)) + formatData(String.valueOf(grade2))
						  + formatData("") + cal.get(Calendar.YEAR) + "/" + cal.get(Calendar.MONTH) + "/" 
							+ cal.get(Calendar.DAY_OF_MONTH) + " " + cal.get(Calendar.HOUR_OF_DAY) + ":" +  (minute < SystemInfo.rankShowNum ? "0" + minute : minute)+ "\n\r";
				}
			}
			JOptionPane.showMessageDialog(null, message,"˫����Ϸ����",JOptionPane.PLAIN_MESSAGE);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			try{
				if(dis != null)
					dis.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	//��������(˫����Ϸ�������ܷ�����)
	public void insertRank2(){
		DataInputStream dis = null;
		DataOutputStream dos = null;
		File file = null;
		String filename = SystemInfo.gameFile + "rank2.txt";
	
		try {
			int r = 0;
			long arr[][] = new long[SystemInfo.rankShowNum][4];
			boolean one = true;
			file = new File(filename);
			if(file.exists()){
				dis = new DataInputStream(new FileInputStream(file));
				for(r = 0; r < SystemInfo.rankShowNum && dis.available() > 0;++r){
					//System.err.println("rank:" + r);
					arr[r][0] = dis.readLong();
					arr[r][1] = dis.readLong();
					arr[r][2] = dis.readLong();
					arr[r][3] = dis.readLong();
					if(one && Hero.getHero1SumGrade()+Hero.getHero2SumGrade() > arr[r][0])
					{
						//����û��ɼ��պñ����һ���������жϾͻ�Խ��
						if(r+1 < SystemInfo.rankShowNum){
							arr[r+1][0] = arr[r][0];
							arr[r+1][1] = arr[r][1];
							arr[r+1][2] = arr[r][2];
							arr[r+1][3] = arr[r][3];
						}
				
						arr[r][0] = Hero.getHero1SumGrade() + Hero.getHero2SumGrade();
						arr[r][1] = Hero.getHero1SumGrade();
						arr[r][2] = Hero.getHero2SumGrade();
						arr[r][3] = new Date().getTime();
						//��Ϊ������ܻ��õ�r����������ж��Ǳ���ģ�Ҳ�Ƿ����߼���
						if(r+1 < SystemInfo.rankShowNum) //�Ᵽ֤��r <= SystemInfo.rankShowNum
							++r;
						one = false;
					}
				}
			}

			if(r < SystemInfo.rankShowNum && one && Hero.getHero1SumGrade()+Hero.getHero2SumGrade() > 0){
				//0�ֲ�������û����
				arr[r][0] = Hero.getHero1SumGrade() + Hero.getHero2SumGrade();
				arr[r][1] = Hero.getHero1SumGrade();
				arr[r][2] = Hero.getHero2SumGrade();
				arr[r][3] = new Date().getTime();
				++r;
				one  = false;
			}
			
			if(!one)	//�óɼ����������ˣ����ļ��е�������Ҫ�޸ģ�
			{
				if(dis != null){
					dis.close();
				}
				if(file.exists())
					file.delete();
				//System.err.println("rank2 new file:" + file.getAbsolutePath());
				file.createNewFile();
				dos = new DataOutputStream(new FileOutputStream(file));
				for(int i = 0; i < r; ++i){
					dos.writeLong(arr[i][0]);
					dos.writeLong(arr[i][1]);
					dos.writeLong(arr[i][2]);
					dos.writeLong(arr[i][3]);
				}
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			try{
				if(dis != null)
					dis.close();
				if(dos != null)
					dos.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	private String formatData(String str){
		String ret = "";
		for(int i = str.length(); i < SystemInfo.alignmentWidth; ++i)
			ret += ' ';
		ret += str;
		//System.err.println(ret);
		return ret;
	}
}

