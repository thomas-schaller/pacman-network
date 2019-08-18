
package pacman;

import java.io.*;
import java.net.*;
import java.util.Date;

public class UDPConnexion implements Connexion {
  private DatagramSocket dataSocket = null;

  private InetAddress address = null;

  private int portNumber;

  protected int bufferSize =  64000;

  private Date lastUpdate;

  public static int timeOut = 0;

  protected int type;

  public InetAddress getAddress() {
		return address;
  }

  public void update() {
		  lastUpdate=new Date();
  }

  public void setAddress(InetAddress address) {
		this.address = address;
  }

  public int getPortNumber() {
		return portNumber;
  }

  public void setPortNumber(int portNumber) {
		this.portNumber = portNumber;
  }

  @Override
  public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((address == null) ? 0 : address.hashCode());
		result = prime * result + portNumber;
		return result;
  }

  @Override
  public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UDPConnexion other = (UDPConnexion) obj;
		if (address == null) {
			if (other.address != null)
				return false;
		} else if (!address.equals(other.address))
			return false;
		if (portNumber != other.portNumber)
			return false;
		return true;
  }

  UDPConnexion(DatagramSocket socket, InetAddress addr, int port) {
		  dataSocket=socket;
		  address=addr;
		  portNumber=port;
		  lastUpdate= new Date();
  }

  public void sendObject(Object o) {
 
		  ByteArrayOutputStream bais  = new ByteArrayOutputStream();
		  ObjectOutputStream oos ;
		  byte message [];
		  
		  
		  try {
			oos =new ObjectOutputStream(bais);
			oos.writeObject(o);
			message=bais.toByteArray();
			DatagramPacket packet;
			packet = new DatagramPacket(message,message.length,address,portNumber);
			dataSocket.send(packet);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
  }

  public Object receiveObject() {
		  Object result = null;
		  byte buffer [] = new byte[bufferSize];
		  DatagramPacket packet = new DatagramPacket (buffer,buffer.length,address,portNumber);
		  ObjectInputStream ois;
		  ByteArrayInputStream bais ; 
		  try {

			dataSocket.receive(packet); //receive message
			bais= new ByteArrayInputStream(buffer);
			ois = new ObjectInputStream(bais);
			result = ois.readObject();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
  }

  @Override
  public boolean isActive() {
		return (timeOut<=0 || timeOut>(new Date().getTime()-lastUpdate.getTime()));
  }

  public void close() {
		  dataSocket.close();
  }

}
