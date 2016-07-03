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
	JButton startBtn = new JButton("Start");
	KeyMouseBusy keyMouseBusy;
	public void run() {
		createUI();
	}
	public void createUI() {
		frame.setLocation(500,300);
		frame.setSize(200,200);
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
	    NativeKeyDetector kDetector = new NativeKeyDetector(showField);
	    GlobalScreen.addNativeKeyListener(kDetector);
	    frame.setAlwaysOnTop(true);
		frame.setVisible(true);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if("Start".equals(startBtn.getText())) {
			startBtn.setText("Stop");
			keyMouseBusy = new KeyMouseBusy();
			keyMouseBusy.start();
		}else {
			startBtn.setText("Start");
			keyMouseBusy.interrupt();
		}
	}
}

