import engine.descriptor.User;
import engine.descriptor.Users;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import rizpa.RizpaFacade;

import java.io.File;
import java.net.URL;

public class RizpaController {
    @FXML private Label fileStatus;
    @FXML private Stage primaryStage;

    @FXML private Button loadFileButton;
    @FXML private TabPane usersPanel;


    private RizpaFacade rizpaModel;

    public void setModel(RizpaFacade rizpaModel) {
        this.rizpaModel = rizpaModel;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    @FXML
    public void initialize() {
        System.out.println("Inside runnable init 2");
        System.out.println();

    }

    @FXML
    protected void loadFile(ActionEvent event){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select words file");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("xml file" +
                "", "*.xml"));
        File selectedFile = fileChooser.showOpenDialog(primaryStage);
        if (selectedFile == null) {
            return;
        }

        String absolutePath = selectedFile.getAbsolutePath();
        try {
            rizpaModel.loadNewData(absolutePath);
            fileStatus.setText("File loaded successfully");
            showUsers();
        } catch (Exception e) {
            fileStatus.setText("File failed to load");
        }
    }

    private void showUsers() {
        Users users = rizpaModel.getUsers();
        if(users != null)
        {
            if(usersPanel.getTabs().size() > 1) {
                usersPanel.getTabs().remove(1);
            }


            try {
//                GridPane gridPane = ;
                for (User user : users) {
                    FXMLLoader fxmlLoader = new FXMLLoader();
                    URL url = getClass().getResource("UserFX.fxml");
                    fxmlLoader.setLocation(url);
                    Tab newUserTab = new Tab(user.getName());
                    newUserTab.setContent(fxmlLoader.load(url.openStream()));
                    usersPanel.getTabs().add(newUserTab);
                }
            } catch (Exception e) {
                System.out.println("Resource not valid ");
            }

        }

    }
}
