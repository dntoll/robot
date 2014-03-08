package daniel.robot.glWindow.view;

import java.awt.event.KeyEvent;

public class Input {
	private static final int maxKey = 1024;
	private boolean[] keyPressed= new boolean[maxKey];
	private boolean[] keyClicked= new boolean[maxKey];
	
	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();
		
		keyPressed[keyCode] = true;
	}

	public void keyReleased(KeyEvent e) {
		int keyCode = e.getKeyCode();
		
		keyClicked[keyCode] = true; 
		
		keyPressed[keyCode] = false; 
		
	}

	public boolean wasClicked(int vk) {
		if (keyClicked[vk]) {
			
			keyClicked[vk] = false;
			return true;
		}
		
		return false;
	}

	

}
