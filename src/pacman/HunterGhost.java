
package pacman;

import java.awt.Point;

public class HunterGhost extends Ghost {
  Point targetPosition =  new Point();

  HunterGhost(int posX, int posY, Boardgame bg) {
		super(posX,posY,bg);
		blockSize=1;
  }

  public void setTarget(Point p) {
		targetPosition=p;
  }

  @Override
  public void changePosition() {
	
		if (targetPosition == null)
		{
			super.changePosition();
		}
		else
		{
			if (bg.isObstacle(getNextPosition()) )
			{
				changeDirection();
			}
			else
			{
				Point actualDirection = (Point)direction.clone();
				if (direction.x ==0 
						&& Integer.signum(direction.y) != Integer.signum(targetPosition.y-position.y)
						&& targetPosition.x!=position.x )
				{
					direction.x=Integer.signum(targetPosition.x-position.x);
					direction.y=0;
				}
				else if (direction.y ==0
						&& Integer.signum(direction.x) != Integer.signum(targetPosition.x-position.x)
						&& Integer.signum(targetPosition.y-position.y)!=0)
				{
					direction.x=0;
					direction.y=Integer.signum(targetPosition.y-position.y);
				}
				if (bg.isObstacle(getNextPosition()))
					direction=actualDirection;
			}
		position=getNextPosition();
		}	
  }

  @Override
  public void changeDirection() {
		if (targetPosition != null)
		{
			Point actualDirection = new Point(direction.x,direction.y);
			int mvt=0;
			do
			{
				switch (mvt)
				{
				case 0:
					if (actualDirection.x==0)
					{
						direction.x=Integer.signum(targetPosition.x-position.x);
						direction.y=0;	
					}
				else
				{
					direction.y=Integer.signum(targetPosition.y-position.y);
					direction.x=0;	
				}
				break;
			case 1:
				if (actualDirection.x==0)
				{
					direction.y=Integer.signum(targetPosition.y-position.y);
					direction.x=0;
				}
				else
				{
					direction.x=Integer.signum(targetPosition.x-position.x);
					direction.y=0;	

				}					
				break;
			case 2:
				direction=actualDirection;
				super.changeDirection();
				break;
			}
			mvt++;
		}
		while ( ( bg.isObstacle(getNextPosition())|| (direction.x==0 && direction.y==0) )
				&& mvt<3   );
			
				
		}
		else
			super.changeDirection();
  }

}
