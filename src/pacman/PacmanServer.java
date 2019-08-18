
package pacman;

import java.io.*;
import java.util.*;
public class PacmanServer {
  String hostname = "localhost";

  Boardgame bg = null;

  int size = 21;

  int port = 6666;

  Vector<ClientInstance> ci =  new Vector <ClientInstance>();

  PacmanServer() {
		bg= new Boardgame(size);
		bg.createLevel();
		new WorldManager(bg,ci).start();
  }

  PacmanServer(String hostname) {
		this();
		this.hostname=hostname;
  }

  public void waitConnection() throws IOException {
		new WaitTCPClient(bg ,ci,port,hostname).start();
		new WaitUDPClient(bg ,ci,port,hostname).start();
		
  }

  public static void main(String[] args) throws IOException
  {
		PacmanServer ps;
		if (args.length>0)
			ps= new PacmanServer(args[0]);
		else
			ps= new PacmanServer();
		ps.waitConnection();
		
  }

}
