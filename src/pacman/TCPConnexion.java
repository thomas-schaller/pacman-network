
package pacman;

import java.io.*;
import java.net.*;


public class TCPConnexion implements Connexion {
  private OutputStream out =  null;

  private InputStream in =  null;

  private boolean isActive = true;

  TCPConnexion(Socket s) {
		  try {
			in = s.getInputStream();
			out = s.getOutputStream();
		} catch (IOException e) {

			e.printStackTrace();
		}
  
  }

  @Override
  public boolean isActive() {
		return isActive;
  }

  public void sendObject(Object o) {
		  try {
			  ObjectOutputStream oos = new ObjectOutputStream(out);
			oos.flush();
			oos.reset();
			oos.writeObject(o);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		
		}
  }

  public Object receiveObject() {
		  Object result=null;
		  try {
			  ObjectInputStream ois = new ObjectInputStream(in);
			result=ois.readObject();
			ois.reset();
		  }
		  catch (IOException e)
		  {  
		  } catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  return result;
  }

  public void close() {
		  isActive=false;
		  try {
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
  }

}
