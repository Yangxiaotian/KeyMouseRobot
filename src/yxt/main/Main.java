package yxt.main;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Enumeration;

public class Main {
	public static String path = "config.txt";
	private static void InitGlobalFont(Font font) {
		FontUIResource fontRes = new FontUIResource(font);
		for (Enumeration<Object> keys = UIManager.getDefaults().keys(); keys.hasMoreElements();) {
			Object key = keys.nextElement();
			Object value = UIManager.get(key);
			if (value instanceof FontUIResource) {
				UIManager.put(key, fontRes);
			}
		}
	}
	public static void main(String[] args) throws IOException {
		File file = new File(path);
		if(!file.exists()) {
			file.createNewFile();
		}
		InitGlobalFont(new Font("宋体", Font.PLAIN, 12));  //统一设置字体
		new UI().start();
	}
}
