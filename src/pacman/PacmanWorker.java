
package pacman;


import java.util.*;
public class PacmanWorker extends Thread {
  Boardgame bg;

  static String delimiter = ";";

  Vector<ClientInstance> ciList;

  Connexion connexion;

  PacmanWorker(Boardgame b, Vector<ClientInstance> clientsList, Connexion c) {
	   bg=b;
	   ciList=clientsList;
	   connexion=c;
  }

  /**
   * public Object receiveMessage()
   * {
   * return connexion.receiveObject();
   * }
   */
  public static Player etablishNewClient(Boardgame bg, Vector<ClientInstance> ciList, Connexion connexion)
  {
		  Player p=new Player();
		    p.position=bg.getAvailablePosition();
		    bg.addType(p.position.x, p.position.y, Boardgame.PACMAN);
		    ClientInstance ci = new ClientInstance(p,connexion);
		    ciList.add(ci);
		    //send the boardgame
		    connexion.sendObject(bg);
		  //send the Player
		   connexion.sendObject(p);
		   return p;
  }

  public static boolean manageDirection(Boardgame bg, Player p, Object message)
  {
		  int direction=0;
		  bg.removeType(p.position.x, p.position.y, Player.getBoardGameStateEquivalent(p));
		  try
		  {
			direction=((Integer)message).intValue();
		  }
		  catch (NumberFormatException nfe)
		  {
			  nfe.printStackTrace();
			return false;
		  }
		  switch(direction)
		  {
			case Player.UP:
				if ( !bg.isObstacle(p.position.x, p.position.y-1) )
					p.position.y--;
				break;
			case Player.DOWN:
				if ( !bg.isObstacle(p.position.x, p.position.y+1) )
					p.position.y++;
				break;
			case Player.LEFT:
				if ( !bg.isObstacle(p.position.x-1, p.position.y) )
					p.position.x--;
				break;
			case Player.RIGHT:
				if ( !bg.isObstacle(p.position.x+1, p.position.y) )
					p.position.x++;
				break;
			}
			bg.addType(p.position.x, p.position.y, Player.getBoardGameStateEquivalent(p));
			return true;
  }

  public static void removeClient(Boardgame bg, Connexion connexion, Player p)
  {
			bg.removeType(p.position.x, p.position.y, Player.getBoardGameStateEquivalent(p));
			p.state=Player.DISCONNECTED;
			System.out.println("player "+ p +"has left the game.");
			connexion.close();
  }

  public void run() {
		  
		  Player p=etablishNewClient(bg,ciList,connexion);

	    // waiting for receiving the pacman direction from client 
	    Object message=null;
		while((message=connexion.receiveObject()) != null )
		{
			manageDirection(bg, p, message);
		}
		removeClient(bg,connexion,p);
  }

}
