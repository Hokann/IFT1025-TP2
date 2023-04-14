package client.ClientFx;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application {

	@Override
	public void start(Stage stage) throws Exception {
		View view = new View();
		Scene scene = new Scene(view, 800, 600);
		stage.setScene(scene);
		//Controller controller = new Controller(model, view);
		//Model model = new Model();

		stage.setTitle("Inscription UdeM");
		stage.show();
	}
	public static void main(String[] args) {
		launch(args);
	}
}
