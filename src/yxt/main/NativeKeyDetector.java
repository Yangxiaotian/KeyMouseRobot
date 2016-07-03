package yxt.main;

import javax.swing.JTextField;

import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

public class NativeKeyDetector implements NativeKeyListener{
	public JTextField keyTextField;
	public NativeKeyDetector(JTextField keyTextField) {
		this.keyTextField = keyTextField;
	}
	@Override
	public void nativeKeyPressed(NativeKeyEvent e) {
		// TODO Auto-generated method stub
		keyTextField.setText(""+e.getRawCode());
	}

	@Override
	public void nativeKeyReleased(NativeKeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void nativeKeyTyped(NativeKeyEvent e) {
		// TODO Auto-generated method stub
		
	}

}
