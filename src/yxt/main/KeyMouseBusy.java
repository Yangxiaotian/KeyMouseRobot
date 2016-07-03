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
				Thread.sleep(500);
				keyMouseWork(readConfig());
			}
		}catch(Exception e) {
			
		}
	}
	public String[] readConfig() throws Exception {
		String path = "config.txt";
		BufferedReader in = new BufferedReader(new FileReader(path));
	    String configStr = null;
	    configStr = in.readLine();
	    in.close();
		return configStr.split("#");
	}
	public void keyMouseWork(String[] config) throws AWTException {
		Robot robot = new Robot();
		for(String s:config) {
			if(s.startsWith("KEY:")) {
				String[] keyCodeArr = s.substring(3).split(",");
				for(String keyStr: keyCodeArr) {
					String[] sameTimeKeys = keyStr.split("|");
					for(String keyCodeStr: sameTimeKeys) {
						int keyInt = Integer.parseInt(keyCodeStr);
						robot.keyPress(keyInt);
					}
					for(int i = sameTimeKeys.length-1; i >= 0 ; i--) {
						String keyCodeStr = sameTimeKeys[i];
						int keyInt = Integer.parseInt(keyCodeStr);
						robot.keyRelease(keyInt);					
					}
					
				}
			}else if(s.startsWith("MOUSE:")) {
				String[] mouseLocationArr = s.substring(5).split("/");
				for(String actionStr: mouseLocationArr) {
					String actPart = actionStr.split("*")[0];
					String locPart = actionStr.split("*")[1];
					int x = Integer.parseInt(locPart.substring(1).split(",")[0]);
					int y = Integer.parseInt(locPart.substring(1).split(",")[1]);
					robot.mouseMove(x, y);
					String[] actArr = actPart.split("@");
					String actType = actArr[0];
					if(actPart.length()==1) {
						switch(actType) {
						case "SJ": robot.mousePress(InputEvent.BUTTON1_MASK);robot.mouseRelease(InputEvent.BUTTON1_MASK);
						case "DJ": robot.mousePress(InputEvent.BUTTON1_MASK);robot.mouseRelease(InputEvent.BUTTON1_MASK);break;
						case "YJ": robot.mousePress(InputEvent.BUTTON3_MASK);robot.mouseRelease(InputEvent.BUTTON3_MASK);break;
						}
					}else if(actPart.length() == 2) {
						long times = Long.parseLong(actArr[1]);
						if(times == -1) {
							while(true) {
								switch(actType) {
								case "SJ": robot.mousePress(InputEvent.BUTTON1_MASK);robot.mouseRelease(InputEvent.BUTTON1_MASK);
								case "DJ": robot.mousePress(InputEvent.BUTTON1_MASK);robot.mouseRelease(InputEvent.BUTTON1_MASK);break;
								case "YJ": robot.mousePress(InputEvent.BUTTON3_MASK);robot.mouseRelease(InputEvent.BUTTON3_MASK);break;
								}
							}
						}else {
							for(int i = 0; i < times; i++) {
								switch(actType) {
								case "SJ": robot.mousePress(InputEvent.BUTTON1_MASK);robot.mouseRelease(InputEvent.BUTTON1_MASK);
								case "DJ": robot.mousePress(InputEvent.BUTTON1_MASK);robot.mouseRelease(InputEvent.BUTTON1_MASK);break;
								case "YJ": robot.mousePress(InputEvent.BUTTON3_MASK);robot.mouseRelease(InputEvent.BUTTON3_MASK);break;
								}
							}
						}
					}
				}
			}else if(s.startsWith("WAIT:")) {
				long waitms = Long.parseLong(s.substring(4));
				try {
					new Thread().wait(waitms);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
	}
}
