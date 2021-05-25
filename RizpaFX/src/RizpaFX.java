import controller.RizpaController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import rizpa.RizpaFacade;

import java.net.URL;

public class RizpaFX extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Rizpa Stock Exchange");

        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource("fxml/RizpaFxApp.fxml");
        fxmlLoader.setLocation(url);
        Parent root = fxmlLoader.load(url.openStream());

        RizpaController rizpaController = fxmlLoader.getController();
        rizpaController.setPrimaryStage(primaryStage);

        Scene scene = new Scene(root, 1000, 1000);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(RizpaFX.class);
    }
}
