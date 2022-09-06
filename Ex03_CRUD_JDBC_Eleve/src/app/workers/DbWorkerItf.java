package app.workers;

import app.beans.Personne;
import app.exceptions.MyDBException;
import java.util.List;

public interface DbWorkerItf {

  void connecterBdMySQL( String nomDB ) throws MyDBException;
  void connecterBdHSQLDB( String nomDB ) throws MyDBException;
  void connecterBdAccess( String nomDB ) throws MyDBException;
  void deconnecter() throws MyDBException; 
  
  List<Personne> lirePersonnes() throws MyDBException;
  
  void creer(Personne p) throws MyDBException;
  Personne lire(int PK) throws MyDBException;
  void modifier(Personne p) throws MyDBException;
  void effacer(Personne p) throws MyDBException;

}
