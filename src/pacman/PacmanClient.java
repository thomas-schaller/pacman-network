
package pacman;

import java.net.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import javax.swing.*;


public class PacmanClient extends JFrame {
  BoardgameIHM gameField = null;

  String serverName = "localhost";

  int portNumber = 6666;

  Connexion c =  null;

  Boardgame bg;

  int[] direction =  new int [1];

  Player currentPlayer = new Player();

  Player bestPlayer =  new Player();

  String connexionText =  "Connect";

  String portText = "Port number";

  String serverText = "Server name";

  String[] connexionType =  { "TCP","UDP"};

  JComboBox jcbConnexionType;

  JLabel infoPlayer;

  JLabel infoHighScore;

  JLabel beCareful;

  JTextArea jtaServerName =  new JTextArea(serverName,1,10);

  JTextArea jtaPortNumber =  new JTextArea(new Integer(portNumber).toString(),1,4);

  PacmanClient() {
		super("Pacman Client");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		createConnexionChooser();

  }

  public void createConnexionChooser() {
//		create the combobox in order to choose the connection type (UDP or TCP)
		JButton connexion = new JButton(connexionText);
		jcbConnexionType = new JComboBox(connexionType);
		JPanel panel= new JPanel();
		JPanel networkInformationsPanel= new JPanel();

		JLabel jlPortNumber= new JLabel(portText+" :");
		JLabel jlServerName = new JLabel(serverText+" :");

		
		networkInformationsPanel.add(jlPortNumber);
		networkInformationsPanel.add(jtaPortNumber);
		networkInformationsPanel.add(jlServerName);
		networkInformationsPanel.add(jtaServerName);
		networkInformationsPanel.add(jcbConnexionType);
		connexion.addActionListener(new ActionListener()
		{

			public void actionPerformed(ActionEvent arg0) {

				serverName= jtaServerName.getText();
				portNumber= Integer.parseInt(jtaPortNumber.getText());
				if (jcbConnexionType.getSelectedIndex()== 0)
					etablishTCPConnexion();
				else
					etablishUDPConnexion();
				initializeGame();
				waitInformations();
			}
			
		}
		);
		getContentPane().add(networkInformationsPanel,BorderLayout.CENTER);
		getContentPane().add(connexion,BorderLayout.SOUTH);
		pack();
		setVisible(true);
		
  }

  public void waitInformations() {
		new Thread()
		{
		public void run()
		{
			Object message=null;
			while ( (message=c.receiveObject()) != null  )
			{
				bg=(Boardgame)message;
				gameField.setBoardgame(bg);
				bestPlayer=(Player)c.receiveObject();
				currentPlayer =(Player) c.receiveObject();
				gameField.setCurrentPlayer(currentPlayer);
				int timeLeft =currentPlayer.timeLeft*WorldManager.speed/1000;
				if (timeLeft<=10 && currentPlayer.timeLeft>=0)
					beCareful.setText("Warning !!! State Finish on "+(timeLeft+1));
				else if (currentPlayer.timeLeft==-1)
					beCareful.setText(" ");
				repaint();
				infoPlayer.setText("Player "+currentPlayer.name +": "+currentPlayer.score);
				infoHighScore.setText("High Score : " +bestPlayer);
				if (direction[0] !=0)
				{
					c.sendObject( new Integer(direction[0]));
					direction[0]=0;
				}
			}
			c.close();
			System.exit(0);
		}
		}.start();
  }

  public void initializeGame() {
		this.getContentPane().removeAll();
		setVisible(false);
		setSize(400,400);
		String message="";
		StringTokenizer st;
//		retrieve the boardgame
		bg = (Boardgame)c.receiveObject();
		System.out.println(bg.toString());
		//retrieve the player information
		direction[0]=0;
		currentPlayer= (Player)c.receiveObject();
		gameField = new BoardgameIHM(bg,currentPlayer);
		infoPlayer= new JLabel("Player: "+currentPlayer.toString());
		infoHighScore= new JLabel("Player: "+currentPlayer.toString());
		beCareful = new JLabel(" ");
		beCareful.setForeground(Color.RED);
		JPanel infoScore= new JPanel();
		infoScore.add(infoPlayer);
		infoScore.add(infoHighScore);
		getContentPane().add(beCareful,BorderLayout.NORTH);
		getContentPane().add(infoScore,BorderLayout.SOUTH);
		getContentPane().add(gameField,BorderLayout.CENTER);
		
		KeyboardManager pcm = new KeyboardManager(direction);
		this.addKeyListener(pcm);
		setVisible(true);

  }

  public void etablishUDPConnexion() {
		DatagramSocket socket=null;
	    InetAddress address=null;
	    try
	    {
			socket = new DatagramSocket();
			socket.setSoTimeout(5000);
			address = InetAddress.getByName(serverName);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		c = new UDPConnexion (socket,address,portNumber);
		c.sendObject("I want to connect");
  }

  public void etablishTCPConnexion() {
		//etablish connection
		try {
			c = new TCPConnexion( new Socket(serverName,portNumber));
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
  }

  public static void main(String[] arg)
  {
		new PacmanClient();
	
  }

}
