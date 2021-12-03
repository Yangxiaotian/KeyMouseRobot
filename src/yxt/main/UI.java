package yxt.main;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;

public class UI extends Thread implements ActionListener{
	JFrame frame = new JFrame("按键鼠标机器人");
	JPanel panel = new JPanel();
	JTextField showField = new JTextField();
	JButton startBtn = new JButton("开始运行（快捷键F1）");
	KeyMouseBusy keyMouseBusy;
	boolean isWorking = false;
	public void run() {
		createUI();
	}
	public void createUI() {
		frame.setLocation(500,300);
		frame.setSize(300,200);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		panel.setLayout(new BorderLayout());
		
		
		startBtn.addActionListener(this);
		frame.getContentPane().add(panel);
		panel.add(startBtn, BorderLayout.CENTER);
		panel.add(showField, BorderLayout.SOUTH);
		NativeMouseDetector mDetector = new NativeMouseDetector(showField);
		try {
	            GlobalScreen.registerNativeHook();
        }
        catch (NativeHookException ex) {
            System.err.println("There was a problem registering the native hook.");
            System.err.println(ex.getMessage());

            System.exit(1);
        }
		GlobalScreen.addNativeMouseListener(mDetector);
	    GlobalScreen.addNativeMouseMotionListener(mDetector);
	    NativeKeyDetector kDetector = new NativeKeyDetector(this);
	    GlobalScreen.addNativeKeyListener(kDetector);
	    frame.setAlwaysOnTop(true);
		frame.setVisible(true);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		doOrNot();
	}
	public void doOrNot() {
		if (!isWorking) {
			isWorking = true;
			doWork();
		} else {
			try {
				isWorking = false;
				doRest();
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
	public void doWork() {
		if (keyMouseBusy == null || keyMouseBusy.isInterrupted()) {
			keyMouseBusy = new KeyMouseBusy();
		}
		keyMouseBusy.start();
		
	}
	public void doRest() throws InterruptedException {
		keyMouseBusy.interrupt();
	}
}

