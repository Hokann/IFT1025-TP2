package server;

import javafx.util.Pair;

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
     * @param port port du serveur
     * @throws IOException
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
     * @throws ClassNotFoundException
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
     * @throws IOException
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
     Lire un fichier texte contenant des informations sur les cours et les transofmer en liste d'objets 'Course'.
     La méthode filtre les cours par la session spécifiée en argument.
     Ensuite, elle renvoie la liste des cours pour une session au client en utilisant l'objet 'objectOutputStream'.
     La méthode gère les exceptions si une erreur se produit lors de la lecture du fichier ou de l'écriture de l'objet dans le flux.
     @param arg la session pour laquelle on veut récupérer la liste des cours
     */
    public void handleLoadCourses(String arg) {
        // TODO: implémenter cette méthode
    }

    /**
     Récupérer l'objet 'RegistrationForm' envoyé par le client en utilisant 'objectInputStream', l'enregistrer dans un fichier texte
     et renvoyer un message de confirmation au client.
     La méthode gére les exceptions si une erreur se produit lors de la lecture de l'objet, l'écriture dans un fichier ou dans le flux de sortie.
     */
    public void handleRegistration() {
        // TODO: implémenter cette méthode
    }
}

