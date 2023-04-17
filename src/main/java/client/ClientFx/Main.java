package client.ClientFx;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Classe pour lancer l'application d'inscription au cours.
 */
public class Main extends Application {
	/**
	 * Méthode qui initialise l'interface graphique.
	 *
	 * @param stage écran d'affichage
	 */
	@Override
	public void start(Stage stage) {
		View view = new View();
		Scene scene = new Scene(view, 800, 600);
		stage.setScene(scene);
		stage.setTitle("Inscription UdeM");
		stage.show();
	}

	public static void main(String[] args) {
		launch();
	}
}
