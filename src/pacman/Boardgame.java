
package pacman;

import java.awt.*;
import java.io.Serializable;

public class Boardgame implements Serializable {
  int[][] field;

  public static final int EMPTY = 1;

  public static final int CHERRY = 2;

  public static final int COIN = 3;

  public static final int PACMAN_DEAD = 5;

  public static final int PACMAN = 7;

  public static final int PACMAN_INVINCIBLE = 11;

  public static final int GHOST = 13;

  public static final int WALL = 17;

  Boardgame(int size) {
 this(new Dimension(size,size));
  }

  Boardgame(Dimension size) {
	field = new int[size.width][size.height];
	 for (int i=0;i<size.width;i++)
		 for (int j=0;j<size.height;j++)
		 {
			 field[i][j]=COIN;
		 }
		setWallEnd();
  }

  public Dimension getSize() {
	return new Dimension(field.length,field[0].length);
  }

  private synchronized void setCase(int i, int j, int value) {
	field[i][j]=value;
  }

  protected int getCase(int i, int j) {
	return field[i][j];
  }

  public Point getAvailablePosition() {
	Point result= new Point();
	boolean isUnavailable=false;
    do
    {
    	result.x =(int)(Math.random()*(getSize().width-3))+1;
    	result.y = (int)(Math.random()*(getSize().height-3))+1;
    	isUnavailable = isObstacle(result) || testType(result.x, result.y, GHOST)
    	||testType(result.x, result.y, PACMAN_INVINCIBLE) || testType(result.x, result.y, PACMAN)
    	|| testType(result.x-1, result.y, GHOST) || testType(result.x, result.y-1, GHOST)
    	|| testType(result.x, result.y+1, GHOST) || testType(result.x+1, result.y, GHOST);
    }
    while(isUnavailable);
    return result;
  }

  public void addType(int x, int y, int typeValue) {
	setCase(x,y,getCase(x,y)*typeValue);
  }

  public synchronized void removeType(int x, int y, int typeValue) {
	if (testType(x,y,typeValue))
		setCase(x,y,getCase(x,y)/typeValue);
  }

  public boolean testType(int x, int y, int typeValue) {
	return (getCase(x,y)%typeValue==0);
  }

  public static boolean isObstacle(int caseValue)
  {
	return caseValue == WALL;
  }

  public boolean isObstacle(Point p) {
	return isObstacle(p.x, p.y);
  }

  public boolean isObstacle(int x, int y) {
	
	return x>=0 && x< field.length && y >=0 && y< field[x].length 
	&& isObstacle(getCase(x,y));
  }

  public void setWallEnd() {
	for (int i=0;i<field.length;i++)
	{
		setCase(i,0,WALL);
		setCase(0,i,WALL);
		setCase(field.length-1,i,WALL);
		setCase(i,field.length-1,WALL);
	}
  }

  public void createLevel() {
	Dimension s = getSize();
	for (int i=0;i<2;i++ )
	{
		this.setCase(i+2, 2, WALL);
		this.setCase(s.width-2-1-i, 2, WALL);
		
		this.setCase(i+2, s.height-3, WALL);
		this.setCase(s.width-2-1-i, s.height-3, WALL);
	}
	for (int i=0;i<5;i++ )
	{
		this.setCase(i+5, 2, WALL);
		this.setCase(s.width-6-i, 2, WALL);
		
		this.setCase(i+5, s.height-3, WALL);
		this.setCase(s.width-6-i, s.height-3, WALL);
		
		for (int j=2;j<s.height-1;j=j+6)
		{
			this.setCase(j+i, s.height/2, WALL);
		}
		
		for (int j=1;j<s.height;j=j+2)
		{
			this.setCase(j, i+4, WALL);
			this.setCase(j, s.height-5-i, WALL);
		
			this.setCase(s.width-j-1, i+4, WALL);
			this.setCase(s.width-j-1, s.height-5-i, WALL);
		}
		
		
	}
//	place cherry
	setCase(1,1,CHERRY);
	setCase(s.width-2,1,CHERRY);
	setCase(s.width-2,s.height-2,CHERRY);
	setCase(1,s.height-2,CHERRY);
	
  }

  @Override
  public String toString() {
	StringBuffer sb = new StringBuffer();
	sb.append('[');
	for (int i=0; i<field.length;i++)
	{
		sb.append('[');
		for (int j=0; j <field[i].length;j++)
		{
			if (j>0)
				sb.append(',');
			sb.append(field[i][j]);
		}
	sb.append("]\n");
	}
	sb.append(']');
	return sb.toString();
  }

}
