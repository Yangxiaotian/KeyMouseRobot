package yxt.main;

import com.jinvoke.NativeStruct;
import com.jinvoke.win32.structs.Point;

@NativeStruct
public class MouseHookStruct {//MSLLHOOKSTRUCT
 public Point pt = new Point();
 public int mouseData;
 public int flags;
 public int time;
 public int dwExtraInfo;
}
