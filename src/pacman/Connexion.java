
package pacman;

interface Connexion {
  /**
   * public void sendMessage(String s);
   * public String receiveMessage();
   */
  void sendObject(Object o) ;

  Object receiveObject() ;

  void close() ;

  boolean isActive() ;

}
