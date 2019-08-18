
package pacman;

import java.awt.Dimension;
import java.awt.Point;
import java.util.Random;

public class Ghost {
  Point position;

  Point direction =  new Point();

  int blockSize = 1;

  int mvtLeft = blockSize;

  Random r =  new Random();

  Boardgame bg;

  Ghost(int posX, int posY, Boardgame b) {
		position=new Point(posX,posY);
		bg=b;
		randomDirection();

  }

  public void setTarget(Point p) {
  }

  private void randomDirection() {
		int alea;
		do
		{
			alea= r.nextInt(4);
			switch(alea)
			{
			case 0:
				direction.x=0;
				direction.y=1;
				break;
			case 1:
				direction.x=0;
				direction.y=-1;
				break;
			case 2:
				direction.x=1;
				direction.y=0;
				break;
			case 3:
				direction.x=-1;
				direction.y=0;
				break;
			}
		}
		while(bg.isObstacle(getNextPosition()));
		
  }

  public Point getPosition() {
		return position;
  }

  public Point getNextPosition() {
		Dimension d = bg.getSize();
		return new Point((position.x+direction.x)%d.width,(position.y+direction.y)%d.height);
  }

  public void changePosition() {
		Point nextPosition=getNextPosition();
		if (bg.isObstacle(nextPosition))
			changeDirection();
		else
		{
//			it tests if the ghost can turn
			if (direction.x==0)
			{
				if ( !bg.isObstacle(position.x-1, position.y) || !bg.isObstacle(position.x+1, position.y))
				{
					if (r.nextBoolean())
						changeDirection();
				}
			}
			else
			{
				if ( !bg.isObstacle(position.x, position.y-1) || !bg.isObstacle(position.x, position.y+1))
				{
					if (r.nextBoolean())
						changeDirection();
				}
			}
				changeDirection();
		}
		position=getNextPosition();
  }

  public void move() {
		if (mvtLeft==0)
		{
			changePosition();
			mvtLeft=blockSize;
		}
		else
			mvtLeft--;
  }

  public void setPosition(int x, int y) {
		position.x=x;
		position.y=y;
  }

  public int getDirectionX() {
		return direction.x;
  }

  public int getDirectionY() {
		return direction.y;
  }

  public void changeDirection() {
		mvtLeft=blockSize;
		Point actualDirection = new Point(direction.x,direction.y);
		int mvt=0;
		do
		{
			switch(mvt)
			{
			case 0:
				if (r.nextBoolean())
				{
					direction.x=actualDirection.y;
					direction.y=actualDirection.x;
				}
				else
				{
					direction.x=-actualDirection.y;
					direction.y=-actualDirection.x;
				}
			break;
			case 1:
				direction.x=-direction.x;
				direction.y=-direction.y;
			break;
			case 2:
				direction.x=actualDirection.x;
				direction.y=actualDirection.y;
			break;
			case 3:
				direction.x=-actualDirection.x;
				direction.y=-actualDirection.y;
			default:
				direction.x=0;
				direction.y=0;
			}
			mvt++;
		}
		while(bg.isObstacle(getNextPosition())&& mvt<=4);
  }

  @Override
  public String toString() {

		return position.toString()+" direction x="+direction.x+" direction y="+direction.y;
  }

}
