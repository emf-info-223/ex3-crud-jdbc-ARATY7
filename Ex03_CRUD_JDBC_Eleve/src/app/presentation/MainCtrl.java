package app.presentation;

import app.beans.Personne;
import app.exceptions.MyDBException;
import app.helpers.*;
import app.workers.DbWorker;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import java.io.File;
import app.workers.*;
import java.time.LocalDate;
import java.util.Date;
import javafx.application.Platform;

/**
 *
 * @author PA/STT
 */
public class MainCtrl implements Initializable {

    // DBs à tester
    private enum TypesDB {
        MYSQL, HSQLDB, ACCESS
    };

    // DB par défaut
    final static private TypesDB DB_TYPE = TypesDB.MYSQL;

    private PersonneManager manPers;
    private DbWorkerItf dbWrk;
    private boolean modeAjout;

    @FXML
    private TextField txtNom;
    @FXML
    private TextField txtPrenom;
    @FXML
    private TextField txtPK;
    @FXML
    private TextField txtNo;
    @FXML
    private TextField txtRue;
    @FXML
    private TextField txtNPA;
    @FXML
    private TextField txtLocalite;
    @FXML
    private TextField txtSalaire;
    @FXML
    private CheckBox ckbActif;
    @FXML
    private Button btnDebut;
    @FXML
    private Button btnPrevious;
    @FXML
    private Button btnNext;
    @FXML
    private Button btnEnd;
    @FXML
    private Button btnSauver;
    @FXML
    private Button btnAnnuler;
    @FXML
    private DatePicker dateNaissance;

    /*
     * METHODES NECESSAIRES A LA VUE
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        dbWrk = new DbWorker();
        manPers = new PersonneManager();
        ouvrirDB();
    }

    @FXML
    public void actionPrevious(ActionEvent event) {
        try {
            afficherPersonne(manPers.precedentPersonne());
        } catch (Exception ex) {
            JfxPopup.displayError("ERREUR", "Une erreur s'est produite", ex.getMessage());
        }
    }

    @FXML
    public void actionNext(ActionEvent event) {
        try {
            afficherPersonne(manPers.suivantPersonne());
        } catch (Exception ex) {
            JfxPopup.displayError("ERREUR", "Une erreur s'est produite", ex.getMessage());
        }
    }

    @FXML
    private void actionEnd(ActionEvent event) {
        try {
            afficherPersonne(manPers.finPersonne());
        } catch (Exception ex) {
            JfxPopup.displayError("ERREUR", "Une erreur s'est produite", ex.getMessage());
        }
    }

    @FXML
    private void debut(ActionEvent event) {
        try {
            afficherPersonne(manPers.debutPersonne());
        } catch (Exception ex) {
            JfxPopup.displayError("ERREUR", "Une erreur s'est produite", ex.getMessage());
        }
    }

    @FXML
    private void menuAjouter(ActionEvent event) {
        effacerContenuChamps();
        rendreVisibleBoutonsDepl(false);
        modeAjout = true;

    }

    @FXML
    private void menuModifier(ActionEvent event) {
        rendreVisibleBoutonsDepl(false);
        modeAjout = false;
    }

    @FXML
    private void menuEffacer(ActionEvent event) {
        try {
            dbWrk.effacer(manPers.courantPersonne());
            manPers.setPersonnes(dbWrk.lirePersonnes());
            afficherPersonne(manPers.debutPersonne());

            rendreVisibleBoutonsDepl(true);
            btnAnnuler.setVisible(true);
            btnSauver.setVisible(true);

        } catch (MyDBException ex) {
            JfxPopup.displayError("ERREUR", "Une erreur s'est produite", ex.getMessage());
        }

    }

    @FXML
    private void menuQuitter(ActionEvent event) {
        try {
            dbWrk.deconnecter(); // ne pas oublier !!!
        } catch (MyDBException ex) {
            System.out.println(ex.getMessage());
        }
        Platform.exit();
    }

    @FXML
    private void annulerPersonne(ActionEvent event) {
        rendreVisibleBoutonsDepl(true);
        btnSauver.setVisible(true);
        btnAnnuler.setVisible(true);
    }

    @FXML
    private void sauverPersonne(ActionEvent event) {

        
        String nom = txtNom.getText();
        String prenom = txtPrenom.getText();
        Date dateNai = java.sql.Date.valueOf(dateNaissance.getValue());
        int num = Integer.valueOf(txtNo.getText());
        String rue = txtRue.getText();
        int npa = Integer.valueOf(txtNPA.getText());
        String localite = txtLocalite.getText();
        boolean actif = ckbActif.isSelected();
        double salaire = Double.valueOf(txtSalaire.getText());
        Date date = new Date();

        if (modeAjout) {

            try {
                dbWrk.creer(new Personne(nom, prenom, dateNai, num, rue, npa, localite, actif, salaire, date));
            } catch (MyDBException ex) {
                JfxPopup.displayError("ERREUR", "Une erreur s'est produite", ex.getMessage());
            }

        } else {

            try {
                int pk = Integer.valueOf(txtPK.getText());
                dbWrk.modifier(new Personne(pk, nom, prenom, dateNai, num, rue, npa, localite, actif, salaire, date));
            } catch (MyDBException ex) {
                JfxPopup.displayError("ERREUR", "Une erreur s'est produite", ex.getMessage());
            }

        }

        try {
            manPers.setPersonnes(dbWrk.lirePersonnes());
        } catch (MyDBException ex) {
            JfxPopup.displayError("ERREUR", "Une erreur s'est produite", ex.getMessage());
        }

        afficherPersonne(manPers.courantPersonne());

        rendreVisibleBoutonsDepl(true);
        btnAnnuler.setVisible(true);
        btnSauver.setVisible(true);
    }

    public void quitter() {
        try {
            dbWrk.deconnecter(); // ne pas oublier !!!
        } catch (MyDBException ex) {
            System.out.println(ex.getMessage());
        }
        Platform.exit();
    }

    private LocalDate convertToLocalDateViaSqlDate(Date dateToConvert) {
        return new java.sql.Date(dateToConvert.getTime()).toLocalDate();
    }

    /*
     * METHODES PRIVEES 
     */
    private void afficherPersonne(Personne p) {
        if (p != null) {
            txtPK.setText(String.valueOf(p.getPkPers()));
            txtPrenom.setText(p.getPrenom());
            txtNom.setText(p.getNom());
            dateNaissance.setValue(convertToLocalDateViaSqlDate(p.getDateNaissance()));
            txtNo.setText(String.valueOf(p.getNoRue()));
            txtRue.setText(p.getRue());
            txtNPA.setText(String.valueOf(p.getNpa()));
            txtLocalite.setText(p.getLocalite());
            ckbActif.setSelected(p.isActif());
            txtSalaire.setText(Double.toString(p.getSalaire()));
        }
    }

    private void ouvrirDB() {
        try {
            switch (DB_TYPE) {
                case MYSQL:
                    dbWrk.connecterBdMySQL("223_personne_1table");
                    break;
                case HSQLDB:
                    dbWrk.connecterBdHSQLDB("../data" + File.separator + "223_personne_1table");
                    break;
                case ACCESS:
                    dbWrk.connecterBdAccess("../data" + File.separator + "223_Personne_1table.accdb");
                    break;
                default:
                    System.out.println("Base de données pas définie");
            }
            System.out.println("------- DB OK ----------");
            manPers.setPersonnes(dbWrk.lirePersonnes());
            afficherPersonne(manPers.precedentPersonne());
        } catch (MyDBException ex) {
            JfxPopup.displayError("ERREUR", "Une erreur s'est produite", ex.getMessage());
            System.exit(1);
        }
    }

    private void rendreVisibleBoutonsDepl(boolean b) {
        btnDebut.setVisible(b);
        btnPrevious.setVisible(b);
        btnNext.setVisible(b);
        btnEnd.setVisible(b);
        btnAnnuler.setVisible(!b);
        btnSauver.setVisible(!b);
    }

    private void effacerContenuChamps() {
        txtNom.setText("");
        txtPrenom.setText("");
        txtPK.setText("");
        txtNo.setText("");
        txtRue.setText("");
        txtNPA.setText("");
        txtLocalite.setText("");
        txtSalaire.setText("");
        dateNaissance.setValue(convertToLocalDateViaSqlDate(new Date(0)));
        ckbActif.setSelected(false);
    }

}
