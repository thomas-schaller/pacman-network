
package pacman;

import java.awt.*;
import java.util.*;

public class WorldManager extends Thread {
  Boardgame bg;

  Ghost[] ghosts;

  static final int speed = 200;

  Vector<ClientInstance> ci;

  int timeRespawn = 100;

  int timePlayerState = 5000/speed;

  public static int NR_DEFAULT_GHOST = 10;

  Player bestPlayer = null;

  Vector<Player> offlinePlayersList = new Vector <Player>();

  Vector<Player> playersStateChanged = new Vector <Player>();

  class Respawn {
    int timeLeft;

    int type;

    Point position;

    Respawn(Point positionType, int type, int nbRounds) {
		timeLeft=nbRounds;
		this.type=type;
		position=positionType;
    }

  }

  Vector<WorldManager.Respawn> removedType =  new Vector<Respawn>();

  void getBestPlayer() {
		if (ci.size()>0)
		{
			if (bestPlayer == null)
			{
				bestPlayer=ci.get(0).getPlayer();
			}
			for (int i=0;i<ci.size();i++)
			{
				if (bestPlayer.score < ci.get(i).getPlayer().score)
					bestPlayer=ci.get(i).getPlayer();
			}
			for (int i=0;i<offlinePlayersList.size();i++)
			{
				if (bestPlayer.score < offlinePlayersList.get(i).score)
					bestPlayer=offlinePlayersList.get(i);
			}
		}
  }

  public WorldManager(Boardgame b, Vector<ClientInstance> ciList) {
			bg=b;
			ci=ciList;
			ghosts=new Ghost[NR_DEFAULT_GHOST];
			for (int i=0; i<ghosts.length;i++)
			{
				Point startPoint = b.getAvailablePosition();
				ghosts[i]= new HunterGhost(startPoint.x,startPoint.y,bg);
				bg.addType(ghosts[i].getPosition().x, ghosts[i].getPosition().y, Boardgame.GHOST);
			}
  }

  public void sendInformation() {
			checkOnlinePlayer();
			getBestPlayer();
			for (int i=0;i<ci.size();i++)
			{
				ClientInstance client=ci.get(i);
				client.getConnexion().sendObject(bg);
				client.getConnexion().sendObject(bestPlayer);
				client.getConnexion().sendObject(client.getPlayer());
			}
  }

  public void moveGhost() {
			checkOnlinePlayer();
			for (int i=0;i<ghosts.length;i++)
			{
				
				bg.removeType(ghosts[i].getPosition().x, ghosts[i].getPosition().y, Boardgame.GHOST);
				if (i<ci.size() && ci.get(i).getPlayer().state == Player.NORMAL)
					ghosts[i].setTarget(ci.get(i).getPlayer().position);
				else
					ghosts[i].setTarget(null);
				ghosts[i].move();
				bg.addType(ghosts[i].getPosition().x, ghosts[i].getPosition().y, Boardgame.GHOST);
			}
  }

  @Override
  public void run() {
			while (true)
			{
				checkOnlinePlayer();
				while(ci.size()>0)
				{
				try {
					Thread.sleep(speed);
				}
				catch (InterruptedException e) {
				e.printStackTrace();
				}
				checkBoard();
				moveGhost();
				checkBoard();
				checkTemporyStatement();
				checkPlayerStatement();
				sendInformation();

				}
			}

			
  }

  protected void checkOnlinePlayer() {
			
			for (int i=0;i<ci.size();i++)
			{
				ClientInstance client= ci.get(i);
				if (client.getPlayer().state == Player.DISCONNECTED 
						|| !client.getConnexion().isActive())
				{
					offlinePlayersList.add(client.getPlayer());
					ci.remove(client);
					System.out.println(client.getPlayer().getName() +" is deconnected.");
					if (client.getPlayer().state != Player.DISCONNECTED)
					{
						bg.removeType(client.getPlayer().getPosition().x,
								client.getPlayer().getPosition().y, 
								Player.getBoardGameStateEquivalent(client.getPlayer()));
						client.getPlayer().state = Player.DISCONNECTED;
						System.out.println(client.getPlayer().getName() +" was not active.");
					}
				}
			}

  }

  protected void checkPlayerStatement() {
			for (int i=0;i<ci.size();i++)
			{
				Player p=ci.get(i).getPlayer();
				if (p.timeLeft>=0)
				{
					if (p.timeLeft==0)
					{
						if (p.state != Player.DISCONNECTED)
						{
							Point pos = (Point)p.position.clone();
							bg.removeType(pos.x, pos.y, Player.getBoardGameStateEquivalent(p));
							p.state = Player.NORMAL;
							bg.addType(pos.x, pos.y, Player.getBoardGameStateEquivalent(p));
						}
					}
				p.timeLeft--;
				}
			}
  }

  protected void checkTemporyStatement() {
			for (int i=0;i<removedType.size();i++)
			{
				Respawn temp = removedType.get(i);
				if (temp.timeLeft<=0)
				{
					removedType.remove(i);
					bg.addType(temp.position.x, temp.position.y, temp.type);
					
				}
				else
					temp.timeLeft--;
			}
  }

  protected void changePlayerState(Player player, int newState) {
			Point pos=(Point)player.position.clone();
			bg.removeType(pos.x, pos.y, Player.getBoardGameStateEquivalent(player));
			player.state=newState;
			player.timeLeft=timePlayerState;
			bg.addType(pos.x, pos.y, Player.getBoardGameStateEquivalent(player));
  }

  protected int getAleaTime(int minimumTime) {
			return (int)(Math.random()*minimumTime)+minimumTime;
  }

  protected void checkBoard() {
	checkOnlinePlayer();
			for (int i=0;i<ci.size();i++)
			{
				Player p = ci.get(i).getPlayer();
				if (bg.testType(p.position.x, p.position.y, Boardgame.COIN)
						&& p.state != Player.DEAD)
				{
					p.score+=1;
					bg.removeType(p.position.x, p.position.y, Boardgame.COIN);
					removedType.add(new Respawn((Point)p.position.clone(), Boardgame.COIN,getAleaTime(timeRespawn)));
				}
				if (bg.testType(p.position.x, p.position.y, Boardgame.CHERRY)
						&& p.state == Player.NORMAL)
				{
					p.score+=5;
					bg.removeType(p.position.x, p.position.y, Boardgame.CHERRY);
					removedType.add(new Respawn((Point)p.position.clone(), Boardgame.CHERRY,getAleaTime(timeRespawn*2)));
					changePlayerState(p,Player.INVINCIBLE);
				}
				if (bg.testType(p.position.x, p.position.y, Boardgame.GHOST) )
				{
					if (p.state== Player.NORMAL)
					{
						p.score-=20;
						changePlayerState(p,Player.DEAD);
					}
					else if (p.state == Player.INVINCIBLE)
					{
						Point pos=(Point)p.position.clone();
						for (int j=0;j<ghosts.length;j++)
						{
							if (pos.equals(ghosts[j].getPosition()))
							{
								p.score +=10;
								bg.removeType(pos.x,pos.y, Boardgame.GHOST);
								ghosts[j].position=bg.getAvailablePosition();
								bg.addType(ghosts[j].position.x, ghosts[j].position.y, Boardgame.GHOST);
							}	
						}
					}
					
				}
			}
  }

}
