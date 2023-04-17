package client.ClientFx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import server.models.Course;
import server.models.RegistrationForm;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class Controller {

    public static ObservableList<Course> afficherCours(String session) {
        ObservableList<Course> cours = FXCollections.observableArrayList();
        try {
            Socket clientSocket = new Socket("localhost", 1337);
            ObjectOutputStream writer = new ObjectOutputStream(clientSocket.getOutputStream());
            String commande = "CHARGER " + session;
            writer.writeObject(commande);
            ObjectInputStream reader = new ObjectInputStream(clientSocket.getInputStream());
            ArrayList<Course> coursSession = (ArrayList) reader.readObject();
            for (int i = 0; i < coursSession.size(); i++) {
                cours.add(coursSession.get(i));
            }

            writer.close();
            reader.close();

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return cours;
    }

    public static String testInscription(String args) {
        String[] inputUser = args.split(" ");
        String prenom = inputUser[0];
        String nom = inputUser[1];
        String email = inputUser[2];
        String matricule = inputUser[3];
        String code = inputUser[4];
        String session = inputUser[5];
        String name = inputUser[6];

        //Verification email
        if(! (email.contains("@") && email.contains("."))){
            return "Echec: impossible de s'inscrire au cours\n"+"Email invalide";
        }

        //Contrôle saisie de la matricule
        try{
            Integer.parseInt(matricule);
            if (matricule.length() != 8){
                throw new NumberFormatException();
            }
        }catch (NumberFormatException e){
            return "Echec: impossible de s'inscrire au cours\n"+"Matricule invalide";
            }

        //Contrôle saisie du code du cours
        try {
            FileReader fr = new FileReader("src/main/java/server/data/inscription.txt");
            BufferedReader reader = new BufferedReader(fr);
            String line;
            while ((line = reader.readLine()) != null) {
                String[] part = line.split("\t");
                String matriculeInscrite = part[2];
                String codeInscris = part[1];
                if (matriculeInscrite.equals(matriculeInscrite) && codeInscris.equals(code)) {
                    return "Echec: Vous êtes déjà inscris à ce cours";
                }
            }
            reader.close();
            Course cours = new Course(name, code, session);
            RegistrationForm inscription = new RegistrationForm(prenom, nom, email, matricule,cours);
            return miseAJourInscription(inscription);


        } catch (UnknownHostException ex) {
            throw new RuntimeException(ex);
        } catch (FileNotFoundException ex) {
            throw new RuntimeException(ex);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static String miseAJourInscription(RegistrationForm inscription){
        try {
            Socket clientSocket = new Socket("127.0.0.1", 1337);
            ObjectOutputStream writer = new ObjectOutputStream(clientSocket.getOutputStream());
            ObjectInputStream reader = new ObjectInputStream(clientSocket.getInputStream());

            String commande2 = "INSCRIRE";
            writer.writeObject(commande2);
            writer.writeObject(inscription);

            String validation = (String) reader.readObject();
            writer.close();
            reader.close();
            return "Felicitations! "+ validation;


        }catch (UnknownHostException e) {
            return "Echec: impossible de s'inscrire au cours";
        } catch (IOException e) {
            return "Echec: impossible de s'inscrire au cours";
        } catch (ClassNotFoundException e) {
            return "Echec: impossible de s'inscrire au cours";
        }

    }


}

