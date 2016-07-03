package yxt.main;

import javax.swing.JTextField;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.mouse.NativeMouseEvent;
import org.jnativehook.mouse.NativeMouseInputListener;

public class NativeMouseDetector implements NativeMouseInputListener {
	private JTextField mouseLocField;
	public NativeMouseDetector(JTextField mouseLocField) {
		this.mouseLocField = mouseLocField;
	}
    public void nativeMouseClicked(NativeMouseEvent e) {
//        System.out.println("Mouse Clicked: " + e.getClickCount());
    }

    public void nativeMousePressed(NativeMouseEvent e) {
//        System.out.println("Mouse Pressed: " + e.getButton());
    }

    public void nativeMouseReleased(NativeMouseEvent e) {
//        System.out.println("Mouse Released: " + e.getButton());
    }

    public void nativeMouseMoved(NativeMouseEvent e) {
        mouseLocField.setText("当前鼠标位置: " + e.getX() + "," + e.getY());
    }

    public void nativeMouseDragged(NativeMouseEvent e) {
//        System.out.println("Mouse Dragged: " + e.getX() + ", " + e.getY());
    }
}