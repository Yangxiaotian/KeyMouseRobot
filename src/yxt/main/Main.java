package yxt.main;

import java.io.File;
import java.io.IOException;

public class Main {
	public static void main(String[] args) throws IOException {
		String path = "config.txt";
		File file = new File(path);
		if(!file.exists()) {
			file.createNewFile();
		}
		new UI().start();
	}
}
