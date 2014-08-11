package com.hugong.lgl;

import java.awt.*;
import java.io.IOException;

import javax.imageio.ImageIO;

class ImageOperation {
	
	public ImageOperation(){
		cross1 = new MyImage("/images/cross1.jpg");
		cross2 = new MyImage("/images/cross2.jpg");
		bomb = new MyImage("/images/bomb.jpg");
		heroprotect = new MyImage("/images/heroprotect.jpg");
		homeprotect = new MyImage("/images/homeprotect.jpg");
		addtank = new MyImage("/images/addtank.jpg");
		mine = new MyImage("/images/mine.jpg");
		star = new MyImage("/images/star.jpg");
		stoptime = new MyImage("/images/stoptime.jpg");
		livehome = new MyImage("/images/livehome.jpg");
		deathhome = new MyImage("/images/deathhome.jpg");
		
		gameover = new MyImage("/images/gameover.jpg");
		
		
	}

	public void drawImage(Graphics g,String filename,Point loc){
		Image image = null;
		int width,height;
		//子弹爆炸图片特殊处理，因为传进来的loc是子弹基准坐标
		if(bomb.getFilename().indexOf(filename) != -1){
			image = bomb.getImage();
			width = image.getWidth(SystemInfo.mjpGame);
			height = image.getHeight(SystemInfo.mjpGame);
			g.drawImage(image, loc.x-width/2, loc.y-height/2,width ,height,SystemInfo.mjpGame);
		}
		else
		{
			if(cross1.getFilename().indexOf(filename) != -1){
				image = cross1.getImage();
			}
			if(cross2.getFilename().indexOf(filename) != -1){
				image = cross2.getImage();
			}
			
			if(heroprotect.getFilename().indexOf(filename) != -1){
				image = heroprotect.getImage();
			}
			
			if(homeprotect.getFilename().indexOf(filename) != -1){
				image = homeprotect.getImage();
			}
			
			if(addtank.getFilename().indexOf(filename) != -1){
				image = addtank.getImage();
			}
			
			if(mine.getFilename().indexOf(filename) != -1){
				image = mine.getImage();
			}
			
			if(star.getFilename().indexOf(filename) != -1){
				image = star.getImage();
			}
			
			if(stoptime.getFilename().indexOf(filename) != -1){
				image = stoptime.getImage();
			}
			if(livehome.getFilename().indexOf(filename) != -1){
				image = livehome.getImage();
			}
			
			if(deathhome.getFilename().indexOf(filename) != -1){
				image = deathhome.getImage();
			}
			
			width = image.getWidth(SystemInfo.mjpGame);
			height = image.getHeight(SystemInfo.mjpGame);
			g.drawImage(image, loc.x, loc.y,width ,height,SystemInfo.mjpGame);
		}

		
	}
	
	
	private MyImage cross1;
	private MyImage cross2;
	private MyImage bomb;
	private MyImage heroprotect;
	private MyImage homeprotect;
	private MyImage addtank;
	private MyImage mine;
	private MyImage star;
	private MyImage stoptime;
	private MyImage livehome;
	private MyImage deathhome;
	
	//gameover图片特殊处理
	public static MyImage gameover;
}

class MyImage{
	public MyImage(String filename){
		this.filename = filename;
		try {
		//	image = ImageIO.read(new File(filename));
			image = ImageIO.read(getClass().getResource(filename));
			//System.err.println("imagefile:" + getClass().getResource(filename));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public Image getImage() {
		return image;
	}
	public String getFilename() {
		return filename;
	}
	
	private Image image;
	private String filename;
}
