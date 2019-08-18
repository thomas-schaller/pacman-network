
package pacman;

import java.awt.*;
import javax.swing.*;
public class BoardgameIHM extends JComponent {
  Player currentPlayer;

  Boardgame bg;

  private static final Color CHERRY =  new Color(200,0,100);

  private static final Color OTHER_PACMEN_DEAD =  new Color(155,0,155);

  BoardgameIHM(Boardgame b, Player currentPlayer) {
		bg=b;
		this.currentPlayer=currentPlayer;
		setPreferredSize(new Dimension (300,300));
		setMinimumSize(new Dimension(200,200));
  }

  public Player getCurrentPlayer() {
		return currentPlayer;
  }

  public void setCurrentPlayer(Player currentPlayer) {
		this.currentPlayer = currentPlayer;
  }

  public Boardgame getBoardgame() {
		return bg;
  }

  public void setBoardgame(Boardgame b) {
		bg=b;
  }

  @Override
  public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Dimension nbCases= bg.getSize();
		Dimension caseSize=new Dimension();
		caseSize.height=getSize().height/nbCases.height;
		caseSize.width=getSize().width/nbCases.width;
		g.setColor(Color.BLACK);
		for (int i=0;i<nbCases.width;i++ )
		{

			g.drawLine(0, i*caseSize.height, nbCases.width*caseSize.width, i*caseSize.height);
			g.drawLine(i*caseSize.width,0,  i*caseSize.width,nbCases.height*caseSize.height);
		}
		
		//drawing each case
		for (int x=0;x<nbCases.width;x++ )
		{
			for (int y=0;y<nbCases.height;y++ )
			{
				if	(bg.testType(x,y,Boardgame.COIN))
				{
					g.setColor(Color.GREEN);
					g.fillOval(x*caseSize.width+caseSize.width/4, y*caseSize.height+caseSize.height/4, caseSize.width/2, caseSize.height/2);	
				}
				if	(bg.testType(x,y,Boardgame.CHERRY))
				{
					g.setColor(CHERRY);
					g.fillOval(x*caseSize.width+caseSize.width/4, y*caseSize.height+caseSize.height/4, caseSize.width/2, caseSize.height/2);	
				}
				if (x==currentPlayer.position.x && y == currentPlayer.position.y)
				{
//					if the current player is on this case, it draws only this pacman.
//					but it must test the state of the player
					switch (currentPlayer.state)
					{
					case Player.DEAD:
						g.setColor(Color.DARK_GRAY);
						break;
					case Player.INVINCIBLE:
						g.setColor(Color.CYAN);
						break;
						default:
							g.setColor(Color.YELLOW);	
					}
					g.fillOval(x*caseSize.width, y*caseSize.height, caseSize.width, caseSize.height);
				}
				else
				{
					if ( bg.testType(x,y,Boardgame.PACMAN_DEAD))
					{
						g.setColor(OTHER_PACMEN_DEAD);
						g.fillOval(x*caseSize.width, y*caseSize.height, caseSize.width, caseSize.height);
					}
					if ( bg.testType(x,y,Boardgame.PACMAN))
					{
						g.setColor(Color.RED);
						g.fillOval(x*caseSize.width, y*caseSize.height, caseSize.width, caseSize.height);
					}
					if ( bg.testType(x,y,Boardgame.PACMAN_INVINCIBLE))
					{
						g.setColor(Color.MAGENTA);
						g.fillOval(x*caseSize.width, y*caseSize.height, caseSize.width, caseSize.height);
					}
					if	(bg.testType(x,y,Boardgame.GHOST))
					{
						g.setColor(Color.BLUE);
						g.fillRect(x*caseSize.width,y*caseSize.height,caseSize.width,caseSize.height);
					}					
				}

				if	(bg.testType(x,y,Boardgame.WALL))
				{
					g.setColor(Color.BLACK);
					g.fillRect(x*caseSize.width, y*caseSize.height, caseSize.width+1, caseSize.height+1);
				}
			}
		}
  }

}
