
package pacman;

import java.awt.event.*;

public class KeyboardManager extends KeyAdapter {
  int[] direction;

  KeyboardManager(int[] direc) {
	direction=direc;
  }

  @Override
  public void keyPressed(KeyEvent key) {
	switch (key.getKeyCode())
	{
	case KeyEvent.VK_LEFT:
		direction[0]=Player.LEFT;
		break;
	case KeyEvent.VK_RIGHT:
		direction[0]=Player.RIGHT;
		break;
	case KeyEvent.VK_UP:
		direction[0]=Player.UP;
		break;
	case KeyEvent.VK_DOWN:
		direction[0]=Player.DOWN;
		break;
	}
  }

}
