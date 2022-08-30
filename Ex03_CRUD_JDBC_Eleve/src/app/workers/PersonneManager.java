/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.workers;

import app.beans.Personne;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author HenchozN
 */
public class PersonneManager {

    private int index = 0;
    private List<Personne> listePersonnes;

    public PersonneManager() {
        this.listePersonnes = new ArrayList<>();
    }

    public Personne courantPersonne() {
        return listePersonnes.get(index);
    }

    public Personne debutPersonne() {
        return listePersonnes.get(index = 0);
    }

    public Personne finPersonne() {
        return listePersonnes.get(index = listePersonnes.size() - 1);
    }

    public Personne precedentPersonne() {
        return listePersonnes.get(index > 0 ? --index : index);
    }

    public Personne suivantPersonne() {
        return listePersonnes.get(index < listePersonnes.size() - 1 ? ++index : index);
    }

    public void setPersonnes(List<Personne> listePersonne) {
        this.listePersonnes = listePersonne;
    }

}
