package controller;

import appManeger.AppManager;
import engine.descriptor.User;
import engine.descriptor.Users;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import rizpa.RizpaFacade;

import java.io.File;
import java.net.URL;
import java.util.HashMap;

public class RizpaController {
    @FXML private  ListView<String> messagesList;

    @FXML private Label fileStatus;
    @FXML private Stage primaryStage;

    @FXML private Button loadFileButton;
    @FXML private TabPane usersPanel;

    @FXML private GridPane adminPage;
    @FXML private AdminController adminPageController;



    private RizpaFacade rizpaFacade;

    private HashMap<String, controller.UserController> userName2UserController = new HashMap<>();


    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    @FXML
    public void initialize() {
        AppManager.getInstance().setLoggerMethod(this::log);
        this.rizpaFacade = AppManager.getInstance().getRizpaFacade();
    }

    private void log(String toLog)
    {
        messagesList.getItems().add(toLog);
    }

    @FXML
    public void loadFile(ActionEvent event){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select words file");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("xml file", "*.xml"));
        File selectedFile = fileChooser.showOpenDialog(primaryStage);
        if (selectedFile == null) {
            return;
        }

        String absolutePath = selectedFile.getAbsolutePath();
        try {
            rizpaFacade.loadNewData(absolutePath);
            fileStatus.setText("File loaded successfully");
            showStocks();
            showUsers();
        } catch (Exception e) {
            fileStatus.setText("File failed to load");
        }
    }

    private void showStocks() {
        if(adminPageController != null)
        {
            adminPageController.showStocks();
        }
    }

    private void showUsers() {
        Users users = rizpaFacade.getUsers();
        if(users != null)
        {
            if(usersPanel.getTabs().size() > 1) {
                usersPanel.getTabs().remove(1);
            }

            URL url = getClass().getResource("/fxml/UserFX.fxml");
            try {
                for (User user : users) {
                    FXMLLoader fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(url);
                    Tab newUserTab = new Tab(user.getName());
                    newUserTab.setContent(fxmlLoader.load(url.openStream()));
                    usersPanel.getTabs().add(newUserTab);
                    UserController userController = fxmlLoader.getController();
                    userController.setUser(user);
                    userController.addOnSubmitListener(adminPageController::onDataChanged);
                    userName2UserController.put(user.getName(), userController);
                }
            } catch (Exception e) {
                System.out.println("Resource not valid ");
            }
        }
    }
}
