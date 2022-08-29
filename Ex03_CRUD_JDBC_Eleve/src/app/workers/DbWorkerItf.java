package app.workers;

import app.beans.Personne;
import app.exceptions.MyDBException;
import java.util.List;

public interface DbWorkerItf {

  void connecterBdMySQL( String nomDB ) throws MyDBException;
  void connecterBdHSQLDB( String nomDB ) throws MyDBException;
  void connecterBdAccess( String nomDB ) throws MyDBException;
  void deconnecter() throws MyDBException; 
  void creer(Personne personne);
  void effacer(Personne personne);
  Personne lire(int nb);
  List<Personne> lirePersonnes() throws MyDBException;
  void modifier(Personne personne);

}
