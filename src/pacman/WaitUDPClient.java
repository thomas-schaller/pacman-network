
package pacman;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import java.util.Vector;

public class WaitUDPClient extends Thread {
  int port;

  public static int gamePort = 6667;

  Vector<ClientInstance> ci;

  Boardgame bg;

  String hostname;

  int timeOut = 10000;

  WaitUDPClient(Boardgame board, Vector<ClientInstance> clientsList, int portNumber, String hostname) {
		bg=board;
		ci=clientsList;
		port=portNumber;
		hostname = this.hostname;
		UDPConnexion.timeOut=timeOut;
  }

  private DatagramSocket sendGamePort(DatagramSocket socket, InetAddress address, int clientPort) {
		byte [] message = new Integer(gamePort).toString().getBytes();

		DatagramSocket dataSocket=null;
        
        DatagramPacket initPacket= new DatagramPacket(message,message.length,address,clientPort);
        try {
			socket.send(initPacket);
			dataSocket = new DatagramSocket(gamePort++);
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("port number="+gamePort);
			dataSocket=sendGamePort(socket,address,clientPort);
		}
		return dataSocket;
  }

  @Override
  public void run() {
		DatagramSocket socket=null;
        try {
			socket = new DatagramSocket(port, InetAddress.getByName(hostname));
		} catch (Exception e) {
			e.printStackTrace();
			socket.close();
			System.err.println("try again...");
			this.run();
			return;
		}
        byte[] buf = new byte[64000];
        ObjectInputStream ois=null;
        Object message = null; 
        ByteArrayInputStream bais ;
        DatagramPacket packet = new DatagramPacket(buf, buf.length);
		bais= new ByteArrayInputStream(buf);
		 while (true) {
			 boolean hasError=false;
          
           // receive all connection : new UDP clients and UDP clients already connected.
		try
		{	
			 socket.receive(packet);
			 ois = new ObjectInputStream(bais);
			 message = ois.readObject();

		}
		 catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				socket.close();
				System.exit(1);
			}
			catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
		        InetAddress address = packet.getAddress();
		        int clientPort = packet.getPort(); 
		        
				if (message instanceof String)
					// a new client want to connect
				{
					Connexion c= new UDPConnexion(socket,address,clientPort);
					PacmanWorker.etablishNewClient(bg, ci, c);
					System.out.println(message);
				}
				else if (hasError ==false)
				{
					ClientInstance c= searchClient(address, clientPort);
					((UDPConnexion)c.getConnexion()).update();
					PacmanWorker.manageDirection(bg, c.getPlayer(), message);

				}

			 bais.reset();
		 }
	         
	
	
  }

  public ClientInstance searchClient(InetAddress address, int clientPort) {
		ClientInstance result=null;
	for (int i=0;i<ci.size() && result==null;i++)
	{
		if (ci.get(i).getConnexion() instanceof UDPConnexion )
		{
			UDPConnexion u=(UDPConnexion)ci.get(i).getConnexion();
			if (address.equals(u.getAddress()) && clientPort == u.getPortNumber() )
				result=ci.get(i);


		}
	}
	return result;
  }

}
