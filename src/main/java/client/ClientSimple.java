package client;

import server.models.Course;
import server.models.RegistrationForm;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;
public class ClientSimple {
    public static void main(String[] args) {
        System.out.println("*** Bienvenue au portail d'inscription de cours de l'UDEM ***");
            try {
                choixSession();
            } catch (Exception e) {
                System.out.println("Erreur, fermeture de l'application");
            }
    }
    private static void choixSession() {
        System.out.println("Veuillez choisir la session pour laquelle vous voulez consulter la liste des cours:");
        System.out.println("1. Automne");
        System.out.println("2. Hiver");
        System.out.println("3. Ete");
        System.out.print("> Choix: ");
        try{
            Scanner scanner = new Scanner(System.in);
            int choix = Integer.parseInt(scanner.nextLine());
            switch (choix) {
                case 1:
                affichageCours("Automne");
                break;
                case 2:
                affichageCours("Hiver");
                break;
                case 3:
                affichageCours("Ete");
                default: throw new IllegalArgumentException();
            }
        }catch (IllegalArgumentException e){
            System.out.println("Choix invalide, fermeture de l'application");
        }
    }
    private static void affichageCours (String session){
        try {
            Socket clientSocket = new Socket("localhost", 1337);
            System.out.println("Les cours offerts pendant la session d'"+session.toLowerCase()+" sont:");

            ObjectOutputStream writer = new ObjectOutputStream(clientSocket.getOutputStream());
            String commande = "CHARGER "+session;
            writer.writeObject(commande);

            ObjectInputStream reader = new ObjectInputStream(clientSocket.getInputStream());
            ArrayList<Course> coursSession = (ArrayList) reader.readObject();

            for (int i = 0; i<coursSession.size();i++){
                System.out.println(i+1+". "+coursSession.get(i).getCode()+"\t"+coursSession.get(i).getName());}

            System.out.println("> Choix: ");
            System.out.println("1. Consulter les cours offerts pour une autre session");
            System.out.println("2. Inscription à un cours"); System.out.print("> Choix: ");

        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try{
            Scanner scanner2 = new Scanner(System.in);
            int choix2 = Integer.parseInt(scanner2.nextLine());
            switch (choix2) {
                case 1:
                choixSession(); //Récurrence, on retourne à l'affichage des choix de sessions
                break;
                case 2:
                inscriptionCours(session);
                break;
                default: throw new IllegalArgumentException();
            }
        }catch (IllegalArgumentException e){
            System.out.println("Choix invalide, fermeture de l'application");
        }
    }
    private static void inscriptionCours(String session) {
        boolean valide = true;
        Scanner scanner3 = new Scanner(System.in);
        System.out.println("Veuillez saisir votre prénom: ");
        String prenom = scanner3.nextLine();
        System.out.println("Veuillez saisir votre nom: ");
        String nom = scanner3.nextLine();
        System.out.println("Veuillez saisir votre email: ");
        String email = scanner3.nextLine();
        if ( ! email.contains("@") && email.contains(".")){
            System.out.println("Email invalide");
            throw new IllegalArgumentException();
        }
        System.out.println("Veuillez saisir votre matricule: ");
        String matricule = scanner3.nextLine();
        if (matricule.length() != 8){
            System.out.println("Matricule invalide");
            throw new IllegalArgumentException();
        }

        System.out.println("Veuillez saisir le code du cours: ");
        String code = scanner3.nextLine();
        try {
            Socket clientSocket = new Socket("localhost", 1337);
            ObjectOutputStream writer = new ObjectOutputStream(clientSocket.getOutputStream());
            String commande = "CHARGER " + session;
            writer.writeObject(commande);
            ObjectInputStream reader = new ObjectInputStream(clientSocket.getInputStream());
            ArrayList<Course> coursSession = (ArrayList) reader.readObject();

            for (int i = 0; i<coursSession.size();i++) {
                if (code.equals(coursSession.get(i).getCode())){
                    Course cours = new Course(coursSession.get(i).getName(), coursSession.get(i).getCode(), session);
                    RegistrationForm inscription = new RegistrationForm(prenom, nom, email, matricule,cours);
                    miseAJourInscription(inscription);
                    break; }
            }

            System.out.println("Le code du cours n'est pas valide");
            throw new IllegalArgumentException();

        }catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    private static void miseAJourInscription(RegistrationForm inscription) {
    try {
        Socket clientSocket = new Socket("127.0.0.1", 1337);
        ObjectOutputStream writer = new ObjectOutputStream(clientSocket.getOutputStream());
        ObjectInputStream reader = new ObjectInputStream(clientSocket.getInputStream());

        String commande2 = "INSCRIRE";
        writer.writeObject(commande2);
        writer.writeObject(inscription);

        String validation = (String) reader.readObject();
        System.out.println("Felicitations! "+ validation);
        writer.close();
        reader.close();
        
    }catch (UnknownHostException e) {
    // TODO Auto-generated catch block
    e.printStackTrace();
    } catch (IOException e) {
    // TODO Auto-generated catch block
    e.printStackTrace();
    } catch (ClassNotFoundException e) {
        // TODO
    }

    }

}
