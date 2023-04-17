package server;

import javafx.util.Pair;
import server.models.Course;
import server.models.RegistrationForm;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Classe qui permet d'accéder au différentes fonctionalitées possibles du serveur, dont notamment la connexion
 * du serveur et la manipulation des commandes.
 */
public class Server {
    /**
     * Commande pour inscrire un étudiant a des cours
     */
    public final static String REGISTER_COMMAND = "INSCRIRE";
    /**
     * Commande pour affiché les cours disponibles
     */
    public final static String LOAD_COMMAND = "CHARGER";
    private final ServerSocket server;
    private Socket client;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;
    private final ArrayList<EventHandler> handlers;

    /**
     * Le constructeur de la classe Server. Il permet d'initialiser un serveur (un objet de type Server) avec sa liste
     * de gestionnaires d'évènements (EventHandler).
     *
     * @param port Port du serveur
     * @throws IOException Exception lors de l'input/l'output
     */
    public Server(int port) throws IOException {
        this.server = new ServerSocket(port, 1);
        this.handlers = new ArrayList<EventHandler>();
        this.addEventHandler(this::handleEvents);
    }

    /**
     * Méthode pour ajouter des EventHandler.
     *
     * @param h le EventHandler à ajouté
     */
    public void addEventHandler(EventHandler h) {
        this.handlers.add(h);
    }

    /**
     * Méthode pour appeler un EventHandler se trouvant dans la liste de tous les EventHandler.
     *
     * @param cmd commande
     * @param arg argument
     */
    private void alertHandlers(String cmd, String arg) {
        for (EventHandler h : this.handlers) {
            h.handle(cmd, arg);
        }
    }

    /**
     * Méthode qui met en route le serveur. Une fois le serveur connecté au client, on va chercher les commandes du client.
     * Lorsqu'il n'y a plus de commandes, on déconnecte le client du serveur.
     */
    public void run() {
        while (true) {
            try {
                client = server.accept();
                System.out.println("Connecté au client: " + client);
                objectInputStream = new ObjectInputStream(client.getInputStream());
                objectOutputStream = new ObjectOutputStream(client.getOutputStream());
                listen();
                disconnect();
                System.out.println("Client déconnecté!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Méthode qui parcourt les commandes du client (obtenues avec le Stream) et les traites une par une.
     *
     * @throws IOException Exception lorsque la commande est erronée (mauvais format par exemple)
     * @throws ClassNotFoundException Exception lorsque la classe n'a pas été trouvée
     */
    public void listen() throws IOException, ClassNotFoundException {
        String line;
        if ((line = this.objectInputStream.readObject().toString()) != null) {
            Pair<String, String> parts = processCommandLine(line);
            String cmd = parts.getKey();
            String arg = parts.getValue();
            this.alertHandlers(cmd, arg);
        }
    }

    /**
     * Méthode qui parcourt une ligne de commande donnée et la sépare en 2 parties: la "commande" cmd (inscrire ou charcher)
     * et "l'argument" arg (les informations de la commande).
     *
     * @param line la ligne de commande qu'on traite
     * @return une classe Pair qui contient 2 valeurs: cmd et arg
     */
    public Pair<String, String> processCommandLine(String line) {
        String[] parts = line.split(" ");
        String cmd = parts[0];
        String args = String.join(" ", Arrays.asList(parts).subList(1, parts.length));
        return new Pair<>(cmd, args);
    }

    /**
     * Méthode pour déconnecter le client du serveur.
     *
     * @throws IOException Erreur lors de l'output.
     */
    public void disconnect() throws IOException {
        objectOutputStream.close();
        objectInputStream.close();
        client.close();
    }

    /**
     * Méthode qui gère les évènements, va faire appel soit à une méthode qui gère l'inscription, soit
     * à une méthode qui gère les cours. Cette distinction est faite en fonction de la commande reçue
     *
     * @param cmd la commande reçue (soit REGISTER_COMMAND, soit LOAD_COMMAND)
     * @param arg le string rattaché à la commande, c'est l'argument
     */
    public void handleEvents(String cmd, String arg) {
        if (cmd.equals(REGISTER_COMMAND)) {
            handleRegistration();
        } else if (cmd.equals(LOAD_COMMAND)) {
            handleLoadCourses(arg);
        }
    }

    /**
     * Méthode qui récupère les cours dans cours.txt, et les convertis en type Course (avec leur informations
     * correspondantes: titre, numeéro, session). Puis, les cours de la session donnée en arguments sont mis dans
     * un tableau. Finalement, ce tableau est renvoyé au client.
     *
     * @param arg la session pour laquelle on veut récupérer la liste des cours
     */
    public void handleLoadCourses(String arg) {
        try{
            ArrayList<Course> coursSession = new ArrayList<Course>();

            FileReader fr = new FileReader("src/main/java/server/data/cours.txt");
            BufferedReader reader = new BufferedReader(fr);
            String line;
            while ((line = reader.readLine()) != null)
            {
                String[] part = line.split("\t");
                String numero = part[0];
                String titre = part[1];
                String session = part[2];
                Course cours = new Course(titre, numero, session);
                //Vérification de la session spécifiée
                if (cours.getSession().equals(arg))
                {
                    coursSession.add(cours); //Ajout du cours à la liste
                }
            }
            reader.close();

            objectOutputStream.writeObject(coursSession);
        }catch (IOException ex){
            System.out.println("Erreur lors de la lecture ou l'écriture du fichier");
        }
    }

    /**
     * Méthode pour effectuer une inscription d'un étudiant à un cours. Cette méthode écrit dans le fichier inscription.txt
     * la session, le code du cours, et matricule, prénom, nom, et email de l'étudiant.
     * Si l'inscription est un succès, on renvoie au client un string qui confirme l'inscription.
     */
    public void handleRegistration() {
        try{
            //Création d'un writer pour écrire dans le fichier inscription.txt
            FileWriter fw = new FileWriter("src/main/java/server/data/inscription.txt", true);
            BufferedWriter writer = new BufferedWriter(fw);

            RegistrationForm inscription = (RegistrationForm)objectInputStream.readObject();
            writer.write(inscription.getCourse().getSession()+"\t"+ inscription.getCourse().getCode()+"\t"+
            inscription.getMatricule()+"\t"+ inscription.getPrenom()+"\t"+
            inscription.getNom()+"\t"+ inscription.getEmail()+"\n");
            writer.close();

            objectOutputStream.writeObject("Inscription réussie de "+ inscription.getPrenom() + " au cours de " +
                    inscription.getCourse().getCode() + ".");
        } catch (IOException ex){
            System.out.println("Erreur lors de la lecture ou l'écriture du fichier");
        } catch (ClassNotFoundException ex){
            System.out.println("Erreur: classe introuvable");
        }
    }
}

