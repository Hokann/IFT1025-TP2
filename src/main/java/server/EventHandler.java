package server;
/**
 * Interface fonctionnelle pour gérer les différents évènements
 */
@FunctionalInterface

public interface EventHandler {
    void handle(String cmd, String arg);
}
