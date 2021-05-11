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
        RizpaFacade rizpaModel = new RizpaFacade();

        primaryStage.setTitle("Rizpa Stock Exchange");

        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource("RizpaFxApp.fxml");
        fxmlLoader.setLocation(url);
        Parent root = fxmlLoader.load(url.openStream());

        RizpaController rizpaController = fxmlLoader.getController();
        rizpaController.setModel(rizpaModel);
        rizpaController.setPrimaryStage(primaryStage);

        Scene scene = new Scene(root, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(RizpaFX.class);
    }
}