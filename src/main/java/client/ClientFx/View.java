package client.ClientFx;

import javafx.collections.FXCollections;
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
import javafx.scene.text.Text;
import server.models.Course;
import client.ClientFx.Controller;
public class View extends HBox {

    private TableView<Course> tableCours = new TableView<>();

    public static TableView<Course> createTable (TableView table){
        TableColumn<Course, String> code = new TableColumn<>("Code");
        TableColumn<Course, String> titre = new TableColumn<>("Cours");
        code.setCellValueFactory(new PropertyValueFactory<Course, String>("code"));
        titre.setCellValueFactory(new PropertyValueFactory<Course, String>("name"));
        code.setPrefWidth(150); titre.setPrefWidth(250);
        table.setMaxSize(370, 600);
        table.getColumns().add(code);
        table.getColumns().add(titre);
        return table;
    }
    public static HBox createBox (HBox box, ChoiceBox choiceBox, Button loadButton){
        choiceBox.setValue("Hiver");
        choiceBox.getItems().add("Hiver");
        choiceBox.getItems().add("Automne");
        choiceBox.getItems().add("Ete");
        choiceBox.setPrefSize(100, 25);
        box.getChildren().add(choiceBox);
        box.getChildren().add(loadButton);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(25, 0, 0, 0));
        return box;
    }
    public static GridPane createGridpane (GridPane pane, TextField prenomField, TextField nomField,
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
        return pane;
    }
    public View() {

        //Les 2 parties de l'interface: l'affichage des cours, et le formulaire d'inscription
        VBox lside = new VBox();
        VBox rside = new VBox();
        lside.setPrefWidth(400); rside.setPrefWidth(400);
        lside.setStyle("-fx-background-color: #f0ece0;"); rside.setStyle("-fx-background-color: #f0ece0;");
        lside.setAlignment(Pos.CENTER); rside.setAlignment(Pos.TOP_CENTER);
        this.getChildren().add(lside);
        this.getChildren().add(new Separator());
        this.getChildren().add(rside);

        //Les 2 titres de chaque partie
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

        //Boite horizontale avec 2 boutons
        HBox box = new HBox();
        ChoiceBox choiceBox = new ChoiceBox();
        Button loadButton = new Button("charger");
        createBox(box, choiceBox, loadButton);
        lside.getChildren().add(box);
        //TODO event
        loadButton.setOnAction(event -> {
            ObservableList<Course> listeCours  = Controller.afficherCours(choiceBox.getValue().toString());
            tableCours.setItems(listeCours);
        });

        //Ajout des champs du formulaire d'inscription (description & espace pour ecrire)
        GridPane pane = new GridPane();
        TextField prenomField = new TextField(); prenomField.setMaxSize(200,25);
        TextField nomField = new TextField(); nomField.setMaxSize(200,25);
        TextField emailField = new TextField(); emailField.setMaxSize(200,25);
        TextField matriculeField = new TextField(); matriculeField.setMaxSize(200,25);
        createGridpane(pane, prenomField, nomField, emailField, matriculeField);
        rside.getChildren().add(pane);

        //Ajout du bouton envoyer pour envoyer l'input de l'utilisateur au controlleur
        Button sendButton = new Button("envoyer");
        sendButton.setMaxSize(100,50);
        rside.getChildren().add(sendButton);

        //TODO event
        sendButton.setOnAction(event -> {
            try {

                if (prenomField.getText().equals("") || nomField.getText().equals("") || emailField.getText().equals("")
                        || matriculeField.getText().equals("")
                ){
                    throw new NullPointerException();}
                Course coursSelect = tableCours.getSelectionModel().getSelectedItem();
                String inputUser = prenomField.getText() + " " + nomField.getText() + " " +
                        emailField.getText() + " " + matriculeField.getText() + " "
                        + coursSelect.getCode() + " " + coursSelect.getSession()+" "+coursSelect.getName();
                //Verification inscription possible & inscription si oui
                String messageAlert = Controller.testInscription(inputUser);

                //Message de succes ou echec
                if (messageAlert.contains("Felicitations")){
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setHeaderText(messageAlert);
                    alert.show();
                }else{
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText(messageAlert);
                    alert.show();
                }
            }catch(NullPointerException E){
                Alert alert2 = new Alert(Alert.AlertType.ERROR);
                alert2.setHeaderText("Erreur: Veuillez séléctionner un cours et/ou remplir le formulaire entièrement");
                alert2.show();
            }
        });
    }
}