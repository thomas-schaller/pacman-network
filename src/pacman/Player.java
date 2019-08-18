
package pacman;

import java.awt.Point;
import java.io.Serializable;
public class Player implements Serializable {
  private static int number = 0;

  String name;

  Point position;

  int timeLeft = -1;

  int score = 0;

  int state = NORMAL;

  public static final int DEAD = 0;

  public static final int DISCONNECTED = 3;

  public static final int NORMAL = 1;

  public static final int INVINCIBLE = 2;

  public static final int UP = 1;

  public static final int DOWN = 2;

  public static final int LEFT = 3;

  public static final int RIGHT = 4;

  public static int getBoardGameStateEquivalent(Player p)
  {
		int result=Boardgame.EMPTY;
		switch(p.state)
		{
			case Player.DEAD:
				result=Boardgame.PACMAN_DEAD;
				break;
			case Player.INVINCIBLE:
				result=Boardgame.PACMAN_INVINCIBLE;
				break;
			case Player.NORMAL:
				result=Boardgame.PACMAN;
				break;
		}
		return result;
  }

  Player() {
		this(new Point());
  }

  public String getName() {
		return name;
  }

  public void setName(String name) {
		this.name = name;
  }

  public Point getPosition() {
		return position;
  }

  public void setPosition(Point position) {
		this.position = position;
  }

  public int getTimeLeft() {
		return timeLeft;
  }

  public void setTimeLeft(int timeLeft) {
		this.timeLeft = timeLeft;
  }

  public int getScore() {
		return score;
  }

  public void setScore(int score) {
		this.score = score;
  }

  public int getState() {
		return state;
  }

  public synchronized void setState(int state) {
		this.state = state;
  }

  Player(Point p) {
		name=""+number;
		position=p;
		number++;
  }

  @Override
  public String toString() {
			return name+"("+score+")";
  }

}
