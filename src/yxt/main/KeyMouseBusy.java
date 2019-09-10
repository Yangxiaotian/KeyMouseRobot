package yxt.main;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class KeyMouseBusy extends Thread{
	public void run() {
		try {
			while(true) {
				keyMouseWork(readConfig());
				sleep(3000);
			}
			
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public String[] readConfig() throws Exception {
		BufferedReader in = new BufferedReader(new FileReader(Main.path));
	    String configStr = null;
	    configStr = in.readLine();
	    in.close();
		return configStr.split("#");
	}
	public double getRandomOffset() {
		double r = Math.random();
		double r1 = Math.random();
		double offset = 1+(r > 0.5 ? 1 : -1)*r1;
		return offset;
	}
	public void keyMouseWork(String[] config) throws AWTException {
		Robot robot = new Robot();
		for(String s:config) {
			if(s.startsWith("KEY:")) {
				String[] keyCodeArr = s.substring(4).split(",");
				for(String keyStr: keyCodeArr) {
					String[] sameTimeKeys = keyStr.split("\\|");
					for(String keyCodeStr: sameTimeKeys) {
						int keyInt = Integer.parseInt(keyCodeStr);
						robot.keyPress(keyInt);
					}
					robot.delay(100);
					for(int i = sameTimeKeys.length-1; i >= 0 ; i--) {
						String keyCodeStr = sameTimeKeys[i];
						int keyInt = Integer.parseInt(keyCodeStr);
						robot.keyRelease(keyInt);					
					}
					
				}
			}else if(s.startsWith("MOUSE:")) {
				int x0 = NativeMouseDetector.x;
				int y0 = NativeMouseDetector.y;
				if(x0 > 1365 || y0 > 768) {
					x0 = y0 = 0;
				}
				String[] mouseLocationArr = s.substring(6).split("/");
				for(String actionStr: mouseLocationArr) {
					String actPart = actionStr.split("\\*")[0];
					String locPart = actionStr.split("\\*")[1];
					int x = Integer.parseInt(locPart.split(",")[0]);
					int y = Integer.parseInt(locPart.split(",")[1]);
					int increment = 1;
					if(x0 > x) {
						increment = -1;
					}
					int posX = x0 + increment;
					double a = 1.0*(y-y0)/(x*x-x0*x0);
					double b = y0-a*x0*x0;
					int posY = y0;
					while(posX != x) {
						
						posY = (int)Math.round(a*posX*posX+b);
						
						
						robot.mouseMove(posX, posY);
//						robot.mouseMove(k, y1);
//						robot.mouseMove(x, y0);
						robot.delay(2);
						posX = posX + increment;
					}
					robot.delay(50);
					robot.mouseMove(posX + 1*increment, posY + 1*increment);
					robot.delay(8);
					robot.mouseMove(posX + -1*increment, posY + -1*increment);
					robot.delay(50);
					String[] actArr = actPart.split("@");
					String actType = actArr[0];
					if(actArr.length == 1) {
						switch(actType) {
						case "SJ": 
							robot.mousePress(InputEvent.BUTTON1_MASK);
							robot.delay((int)(10*getRandomOffset()));
							robot.mouseMove(posX, posY + 1);
							robot.mouseRelease(InputEvent.BUTTON1_MASK);
							robot.delay((int)(80*getRandomOffset()));
						case "DJ": 
							robot.mousePress(InputEvent.BUTTON1_MASK);
							robot.delay((int)(10*getRandomOffset()));
							robot.mouseMove(posX, posY + 1);
							robot.mouseRelease(InputEvent.BUTTON1_MASK);
							robot.delay((int)(80*getRandomOffset()));
						case "YJ": robot.mousePress(InputEvent.BUTTON3_MASK);robot.mouseRelease(InputEvent.BUTTON3_MASK);break;
						}
					}else if(actArr.length == 2) {
						long times = Long.parseLong(actArr[1]);
						if(times == -1) {
							while(true) {
								switch(actType) {
								case "SJ": 
									robot.mousePress(InputEvent.BUTTON1_MASK);
									robot.mouseRelease(InputEvent.BUTTON1_MASK);
								case "DJ": 
									robot.mousePress(InputEvent.BUTTON1_MASK);
									robot.mouseRelease(InputEvent.BUTTON1_MASK);break;
								case "YJ": robot.mousePress(InputEvent.BUTTON3_MASK);robot.mouseRelease(InputEvent.BUTTON3_MASK);break;
								}
							}
						}else {
							for(int i = 0; i < times; i++) {
								switch(actType) {
								case "SJ": 
									robot.mousePress(InputEvent.BUTTON1_MASK);
									robot.mouseRelease(InputEvent.BUTTON1_MASK);
								case "DJ": 
									robot.mousePress(InputEvent.BUTTON1_MASK);
									robot.mouseRelease(InputEvent.BUTTON1_MASK);
									break;
								case "YJ": robot.mousePress(InputEvent.BUTTON3_MASK);robot.mouseRelease(InputEvent.BUTTON3_MASK);break;
								}
								robot.delay(100);
							}
						}
					}
				}
			}else if(s.startsWith("WAIT:")) {
				long waitms = Long.parseLong(s.substring(5));
				try {
					Thread.sleep(waitms);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
	}
}
