package client.ClientFx;

import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import server.models.Course;

/**
 * Classe qui construit l'aspect visuel de l'application, c'est-à-dire l'interface graphique.
 * De plus, cette classe détécte des actions éffectuées par l'utilisateur et les
 * envoies au Controlleur pour traiter de manière appropriée.
 */
public class View extends HBox {

    private final TableView<Course> tableCours = new TableView<>();

    /**
     * Méthode qui créer l'affichage des cours sous la forme d'une table avec
     * une colonne dédiée au nom du cours, et une autre dédiée au code du cours.
     *
     * @param table table qui affiche les cours disponibles
     */
    public static void createTable (TableView<Course> table){
        TableColumn<Course, String> code = new TableColumn<>("Code");
        TableColumn<Course, String> titre = new TableColumn<>("Cours");
        code.setCellValueFactory(new PropertyValueFactory<>("code"));
        titre.setCellValueFactory(new PropertyValueFactory<>("name"));
        code.setPrefWidth(150); titre.setPrefWidth(250);
        table.setMaxSize(370, 600);
        table.getColumns().add(code);
        table.getColumns().add(titre);
    }

    /**
     * Méthode qui créer une boite permettant la selection d'une session avec un bouton "charger".
     * Ceci est fait pour ensuite premettre d'afficher la liste des cours en fonction de la session choisie.
     *
     * @param box        boîte horizontale pour contenir la ChoiceBox et le Button
     * @param choiceBox  boîte de selection
     * @param loadButton boutton pour charger les cours d'une session
     */
    public static void createBox (HBox box, ChoiceBox<String> choiceBox, Button loadButton){
        choiceBox.setValue("Hiver");
        choiceBox.getItems().add("Hiver");
        choiceBox.getItems().add("Automne");
        choiceBox.getItems().add("Ete");
        choiceBox.setPrefSize(100, 25);
        box.getChildren().add(choiceBox);
        box.getChildren().add(loadButton);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(25, 0, 0, 0));
    }

    /**
     * Méthode qui créé le formulaire d'inscription.
     * Il s'agit de créer une grille (gridpane) et de placer les éléments du formulaire dedans.
     *
     * @param pane la grille du formulaire
     * @param prenomField espace pour écrire son prénom
     * @param nomField espace pour écrire son nom
     * @param emailField espace pour écrire son email
     * @param matriculeField espace pour écrire sa matricule
     */
    public static void createGridpane (GridPane pane, TextField prenomField, TextField nomField,
                                       TextField emailField, TextField matriculeField){
        Label prenom = new Label("Prénom");
        Label nom = new Label("Nom");
        Label email = new Label("Email");
        Label matricule = new Label("Matricule");
        prenom.setFont(Font.font("Arial", FontWeight.MEDIUM, 16));
        nom.setFont(Font.font("Arial", FontWeight.MEDIUM, 16));
        email.setFont(Font.font("Arial", FontWeight.MEDIUM, 16));
        matricule.setFont(Font.font("Arial", FontWeight.MEDIUM, 16));
        pane.add(prenom, 0, 0);
        pane.add(prenomField,1,0);
        pane.add(nom, 0,1);
        pane.add(nomField,1,1);
        pane.add(email,0,2);
        pane.add(emailField,1,2);
        pane.add(matricule,0,3);
        pane.add(matriculeField,1,3);
        pane.setHgap(25); pane.setVgap(10);
        pane.setAlignment(Pos.CENTER);
        pane.setPadding(new Insets(0, 0, 30, 0));
    }

    /**
     * Méthode principale qui construit l'ensemble de l'interface graphique et définit aussi des évènements
     * lorsque l'utilisateur intéragit avec l'interface (via clavier, boutton, etc...).
     * On retrouve dans cette méthode 2 fonctionnalités: (1) l'affichage des cours d'une session dans la
     * table, et (2) L'inscription au cours d'un étudiant ayant rempli correctement le formulaire.
     *
     * Finalement, on affiche une alerte de confirmation d'inscription ou d'erreur à l'inscription.
     */
    public View() {

        //Création des 2 parties de l'interface: le côté avec l'affichage des cours et celui avec le formulaire.
        VBox lside = new VBox();
        VBox rside = new VBox();
        lside.setPrefWidth(400); rside.setPrefWidth(400);
        lside.setStyle("-fx-background-color: #f0ece0;"); rside.setStyle("-fx-background-color: #f0ece0;");
        lside.setAlignment(Pos.CENTER); rside.setAlignment(Pos.TOP_CENTER);
        this.getChildren().add(lside);
        this.getChildren().add(new Separator());
        this.getChildren().add(rside);

        //Les 2 titres pour chaque côté
        Label titreCours = new Label("Liste des Cours");
        Label titreFormulaire = new Label("Formulaire d'inscription");
        titreCours.setFont(Font.font("Arial", FontWeight.MEDIUM, 24));
        titreCours.setAlignment(Pos.TOP_CENTER);
        titreCours.setPadding(new Insets(0, 0, 40, 0));
        titreFormulaire.setFont(Font.font("Arial", FontWeight.MEDIUM, 24));
        titreFormulaire.setPadding(new Insets(44, 0, 40, 0));
        rside.getChildren().add(titreFormulaire);
        lside.getChildren().add(titreCours);

        //Ajout du tableau qui affiche les cours
        createTable(tableCours);
        lside.getChildren().add(tableCours);

        //Ajout de la boite horizontale contenant la sélection des cours et le bouton pour charger les cours
        HBox box = new HBox();
        ChoiceBox<String> choiceBox = new ChoiceBox<>();
        Button loadButton = new Button("charger");
        createBox(box, choiceBox, loadButton);
        lside.getChildren().add(box);

        //FONCTIONALITÉ (1): Lorsque l'utilisateur appuie sur le bouton "charger", on va chercher les cours de cette session
        //et on les affichent sur le tableau de cours.
        loadButton.setOnAction(event -> {
             ObservableList<Course> listeCours  = Controller.afficherCours(choiceBox.getValue());
            tableCours.setItems(listeCours);
        });

        //Ajout des champs du formulaire d'inscription (déscription & espace pour écrire)
        GridPane pane = new GridPane();
        TextField prenomField = new TextField(); prenomField.setMaxSize(200,25);
        TextField nomField = new TextField(); nomField.setMaxSize(200,25);
        TextField emailField = new TextField(); emailField.setMaxSize(200,25);
        TextField matriculeField = new TextField(); matriculeField.setMaxSize(200,25);
        createGridpane(pane, prenomField, nomField, emailField, matriculeField);
        rside.getChildren().add(pane);

        //Ajout du bouton "envoyer" pour envoyer l'input de l'utilisateur au controlleur
        Button sendButton = new Button("envoyer");
        sendButton.setMaxSize(100,50);
        rside.getChildren().add(sendButton);

        //FONCTIONALITÉ (2): L'inscription au cours de l'étudiant
        sendButton.setOnAction(event -> {
            try {
                //Verification préliminaire si les champs sont vides.
                if (prenomField.getText().equals("") || nomField.getText().equals("") || emailField.getText().equals("")
                        || matriculeField.getText().equals("")
                ){
                    throw new NullPointerException();}
                Course coursSelect = tableCours.getSelectionModel().getSelectedItem();
                String inputUser = prenomField.getText() + " " + nomField.getText() + " " +
                        emailField.getText() + " " + matriculeField.getText() + " "
                        + coursSelect.getCode() + " " + coursSelect.getSession()+" "+coursSelect.getName();

                //Verification que l'inscription est possible & inscription de l'étudiant
                String messageAlert = Controller.testInscription(inputUser);

                //Message de succes ou echec
                Alert alert;
                if (messageAlert.contains("Felicitations")){
                    alert = new Alert(Alert.AlertType.CONFIRMATION);
                }else{
                    alert = new Alert(Alert.AlertType.ERROR);
                }
                alert.setHeaderText(messageAlert);
                alert.show();
            }catch(NullPointerException E){
                Alert alert2 = new Alert(Alert.AlertType.ERROR);
                alert2.setHeaderText("Erreur: Veuillez séléctionner un cours et/ou remplir le formulaire entièrement");
                alert2.show();
            }
        });
    }

}