package com.hugong.lgl;



import java.io.IOException;

import javax.sound.sampled.*;



class SoundOperation{
	
	public SoundOperation(){
		this.shot = new MyAudioInputStream("/sounds/shot.wav");
		this.defense = new MyAudioInputStream("/sounds/defense.wav");
		this.enemydeath = new MyAudioInputStream("/sounds/enemydeath.wav");
		this.herodeath = new MyAudioInputStream("/sounds/herodeath.wav");
		this.showwealth = new MyAudioInputStream("/sounds/showwealth.wav");
		this.getwealth = new MyAudioInputStream("/sounds/getwealth.wav");
		this.start = new MyAudioInputStream("/sounds/start.wav");
		this.statistic = new MyAudioInputStream("/sounds/statistic.wav");
		this.gameover = new MyAudioInputStream("/sounds/gameover.wav");
		
	}
	
	public void playSound(final String filename,final long timeout){
	 	AudioInputStream stream = null;
	 	Clip clip = null;
	 	if(this.shot.getFilename().indexOf(filename) != -1)
	 		stream = this.shot.getStream();
	 	if(this.defense.getFilename().indexOf(filename) != -1)
	 		stream = this.defense.getStream();
	 	if(this.enemydeath.getFilename().indexOf(filename) != -1)
	 		stream = this.enemydeath.getStream();
	 	if(this.herodeath.getFilename().indexOf(filename) != -1)
	 		stream = this.herodeath.getStream();
	 	if(this.showwealth.getFilename().indexOf(filename) != -1)
	 		stream = this.showwealth.getStream();
	 	if(this.getwealth.getFilename().indexOf(filename) != -1)
	 		stream = this.getwealth.getStream();
	 	if(this.start.getFilename().indexOf(filename) != -1)
	 		stream = this.start.getStream();
	 	if(this.statistic.getFilename().indexOf(filename) != -1)
	 		stream = this.statistic.getStream();
	 	if(this.gameover.getFilename().indexOf(filename) != -1)
	 		stream = this.gameover.getStream();
	 	// Start playing
	 	if(stream != null)
	 	{
	 	   // At present, ALAW and ULAW encodings must be converted
	           // to PCM_SIGNED before it can be played
	           AudioFormat format = stream.getFormat();
	           if (format.getEncoding() != AudioFormat.Encoding.PCM_SIGNED) {
	               format = new AudioFormat(
	                       AudioFormat.Encoding.PCM_SIGNED, format
	                               .getSampleRate(), format
	                               .getSampleSizeInBits() * 2, format
	                               .getChannels(),
	                       format.getFrameSize() * 2, format
	                               .getFrameRate(), true); // big
	               // endian
	               stream = AudioSystem
	                       .getAudioInputStream(format, stream);
	           }
	        // Create the AudioInputStream
	           DataLine.Info info = new DataLine.Info(Clip.class, stream
	                   .getFormat(),
	                   ((int) stream.getFrameLength() * format
	                           .getFrameSize()));
			try {
				clip = (Clip) AudioSystem.getLine(info);
	 			// This method does not return until the audio file is
	            // completely
		 		// loaded
				if(clip != null){
					  clip.open(stream);
				 	  clip.start();
				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			final AudioInputStream ais = stream;	//函数中的内部类只能使用函数的常变量
			Thread td = new Thread() {
	            public void run() {
	                synchronized (this) {
	                    try {
	                        wait(timeout);
	                    } catch (InterruptedException e) {
	                        e.printStackTrace();
	                    }
	                    if(ais != null)
							try {
								ais.close();	//一定要记得关闭流
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
	                }
	               
	            }
	        };
	        td.setDaemon(false);
	        td.start();
	 	} 

	}
	
	
	private MyAudioInputStream shot;
	private MyAudioInputStream defense;
	private MyAudioInputStream enemydeath;
	private MyAudioInputStream herodeath;
	private MyAudioInputStream showwealth;
	private MyAudioInputStream getwealth;
	private MyAudioInputStream start;
	private MyAudioInputStream statistic;
	private MyAudioInputStream gameover;
	
}

//以下类仅仅是为了得到这个流所打开的文件
class MyAudioInputStream{
	public MyAudioInputStream(String filename){
		this.filename = filename;
	}
	
	
	public AudioInputStream getStream() {
		AudioInputStream stream = null;
		try{
			// From URL
			//stream = AudioSystem.getAudioInputStream(new File(filename));
            stream = AudioSystem.getAudioInputStream(getClass().getResource(filename));
		}catch(Exception e){
			e.printStackTrace();
		}
		return stream;
	}
	public String getFilename() {
		return filename;
	}
	
	private String filename;
}
