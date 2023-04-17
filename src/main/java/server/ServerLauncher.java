package server;

/**
 * Classe pour initialiser et démarrer un serveur
 */
public class ServerLauncher {
    /**
     * Constante qui représente le port où le serveur sera connecté
     */
    public final static int PORT = 1337;
    /**
     * Méthode main pour démarrer le serveur.
     *
     * @param args un tableau de strings qui représentent la/les lignes de commandes du client
     */
    public static void main(String[] args) {
        Server server;
        try {
            server = new Server(PORT);
            System.out.println("Server is running...");
            server.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}