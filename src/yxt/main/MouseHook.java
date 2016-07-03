package yxt.main;

import static com.jinvoke.win32.WinConstants.*;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.jinvoke.Callback;
import com.jinvoke.JInvoke;
import com.jinvoke.NativeImport;
import com.jinvoke.Util;
import com.jinvoke.win32.User32;
import com.jinvoke.win32.structs.Msg;

public class MouseHook extends JPanel{
 static {
  JInvoke.initialize();
 } 
 
 @NativeImport(library = "user32")
 public native static int SetWindowsHookEx (int idHook, Callback hookProc, int hModule, int dwThreadId);
 
 @NativeImport(library = "user32")
 public native static int UnhookWindowsHookEx (int idHook);
 
 public static final int WH_MOUSE_LL = 14;
 static JFrame frame;
 
 static TextArea mouseEventArea = new TextArea();
 static JButton setHookBtn;
 static JButton removeHookBtn;
 
 public MouseHook() {
        super(new BorderLayout());

  mouseEventArea.setText("1) Click the \"Set Mouse Hook\" button.\n" +
    "2) Start clicking anywhere on the desktop.  Mouse clicks will be captured here.\n" +
    "3) Stop the mouse hook by clicking the \"Remove Mouse Hook\" button.\n\n");
  
     JScrollPane MouseEventPane = new JScrollPane(mouseEventArea);
     
        add(MouseEventPane, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        
        setHookBtn = new JButton("Set Mouse Hook");
        setHookBtn.addActionListener(new ActionListener() {
   public void actionPerformed(ActionEvent arg0) {
    setMouseHook();
   }} );
        
        removeHookBtn = new JButton("Remove Mouse Hook");
        removeHookBtn.addActionListener(new ActionListener() {
   public void actionPerformed(ActionEvent arg0) {
    unsetMouseHook();
   }} );
        removeHookBtn.setEnabled(false);
        buttonPanel.add(setHookBtn);         
        buttonPanel.add(removeHookBtn);      
        add(buttonPanel, BorderLayout.SOUTH);
 }
 
 private void setMouseHook() {
  setHookBtn.setEnabled(false);
  removeHookBtn.setEnabled(true);
  
  // This hook is called in the context of the thread that installed it. 
  // The call is made by sending a message to the thread that installed the hook.
  // Therefore, the thread that installed the hook must have a message loop. 
  //
  // We crate a new thread as we don't want the AWT Event thread to be stuck running a message pump
  // nor do we want the main thread to be stuck in running a message pump
  Thread hookThread = new Thread(new Runnable(){

   public void run() {
    if (MouseProc.hookHandle == 0) {
     int hInstance = User32.GetWindowLong(Util.getWindowHandle(frame), GWL_HINSTANCE);
        
     MouseProc.hookHandle = SetWindowsHookEx(WH_MOUSE_LL, 
         new Callback(MouseProc.class, "lowLevelMouseProc"), 
         hInstance, 
         0);

// Standard message dispatch loop (message pump)
     Msg msg = new Msg();
     while (User32.GetMessage(msg, 0, 0, 0)) {
      User32.TranslateMessage(msg);
      User32.DispatchMessage(msg);
     }
     
    } else {
     mouseEventArea.append("The Hook is already installed.\n");
    }
   }});
  hookThread.start();
  }

 private void unsetMouseHook() {
  setHookBtn.setEnabled(true);
  removeHookBtn.setEnabled(false);
  UnhookWindowsHookEx(MouseProc.hookHandle);
  MouseProc.hookHandle = 0;
 }

private static void createAndShowGUI() {
        //Create and set up the window.
        frame = new JFrame("Mouse Hook");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        MouseHook MouseEventsWindow = new MouseHook();
        MouseEventsWindow.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        //Add content to the window.
        frame.add(MouseEventsWindow, BorderLayout.CENTER);
   
        //Display the window.
        frame.pack();
         
        frame.setBounds(300, 200, 750, 600);
        frame.setVisible(true);
    }
 
 public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });

 }
}
  
class MouseProc {
 static int hookHandle;
 
 @NativeImport(library = "user32")
 public native static int CallNextHookEx (int idHook, int nCode, int wParam, int lParam);
 
 static {
  JInvoke.initialize();
 } 
 
 public static int lowLevelMouseProc(int nCode, int wParam, int lParam ) {
  if (nCode < 0)
   return CallNextHookEx(hookHandle, nCode, wParam, lParam);

if (nCode == HC_ACTION) {
     MouseHookStruct mInfo = Util.ptrToStruct(lParam, MouseHookStruct.class);
        String message = "Mouse pt: (" + mInfo.pt.x + ", " + mInfo.pt.y + ") ";
        switch (wParam) {
        
        case WM_LBUTTONDOWN:
         message += "Left button down";
         break;
        case WM_LBUTTONUP:
         message += "Left button up";
         break;
        case WM_MOUSEMOVE:
         message += "Mouse moved";
         break;
        case WM_MOUSEWHEEL:
         message += "Mouse wheel rotated";
         break;
        case WM_RBUTTONDOWN:
         message += "Right button down";
         break;
        case WM_RBUTTONUP:
         message += "Right button down";
         break;
        }
        System.out.println(message); 
//        MouseHook.mouseEventArea.append(message+"\n");
      }
    
  return CallNextHookEx(hookHandle, nCode, wParam, lParam);
 }
} 

