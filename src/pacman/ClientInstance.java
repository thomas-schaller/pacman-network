
package pacman;

public class ClientInstance {
  /**
   * 
   * Class integrating a player and a connexion
   * 
   */
  private Player p;

  private Connexion c;

  public Player getPlayer() {
		return p;
  }

  public Connexion getConnexion() {
		return c;
  }

  ClientInstance(Player player, Connexion connexion) {
		p=player;
		c=connexion;
  }

}
