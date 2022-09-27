package com.yc.util;

import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class SwtUtils {

	public static void centerShell(Display display, Shell shell) {
		Rectangle displayBounds = display.getPrimaryMonitor().getBounds();
		Rectangle shellBounds = shell.getBounds();
		
		int x = displayBounds.x + (displayBounds.width - shellBounds.width) >> 1;
		int y = displayBounds.y + (displayBounds.height - shellBounds.height) >> 1;
		shell.setLocation(x, y);
	}
	
	
	public static void centerShell(Shell shell) {   //相对父窗口居中
	Rectangle parentBounds = shell.getParent().getBounds(); 
	Rectangle shellBounds = shell.getBounds(); 
	
	shell.setLocation(parentBounds.x + (parentBounds.width - shellBounds.width)/2, 
					parentBounds.y + (parentBounds.height - shellBounds.height)/2); 
	}

}
