
package pacman;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.Vector;

public class WaitTCPClient extends Thread {
  int port;

  String hostname;

  Vector<ClientInstance> clientList;

  Boardgame bg;

  WaitTCPClient(Boardgame board, Vector<ClientInstance> ci, int portNumber, String hostname) {
		port=portNumber;
		clientList=ci;
		bg=board;
		this.hostname=hostname;
  }

  @Override
  public void run() {
		ServerSocket sSocket=null;
		try {
			
			sSocket=new ServerSocket(port,0,InetAddress.getByName(hostname));
			
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		while (true)
		{
			Connexion c=null;
			try {
				c = new TCPConnexion(sSocket.accept());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			new PacmanWorker(bg,clientList,c).start();
		}
		
  }

}
