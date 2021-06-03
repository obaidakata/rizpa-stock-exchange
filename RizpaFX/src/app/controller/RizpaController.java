package app.controller;

import app.appManeger.AppManager;
import engine.descriptor.User;
import engine.descriptor.Users;
import javafx.beans.binding.Bindings;
import javafx.concurrent.Task;
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

public class RizpaController extends Task<Boolean>{
    @FXML private  ListView<String> messagesList;

    @FXML private Label fileStatus;
    @FXML private Stage primaryStage;

    @FXML private Button loadFileButton;
    @FXML private TabPane usersPanel;

    @FXML private GridPane adminPage;
    @FXML private AdminController adminPageController;

    private RizpaFacade rizpaFacade;

    private final long SLEEP_TIME = 80;
    Task<Boolean> currentRunningTask;
    Thread thread;
    @FXML private Label progressPercentLabel;
    @FXML // fx:id="taskProgressBar"
    private ProgressBar taskProgressBar; // Value injected by FXMLLoader

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
    public void loadFile(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Rizpa file");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("xml file", "*.xml"));
        File selectedFile = fileChooser.showOpenDialog(primaryStage);
        if (selectedFile == null) {
            return;
        }

        String absolutePath = selectedFile.getAbsolutePath();
        try {
            loadXML();
            rizpaFacade.loadNewData(absolutePath);
            resetData();
            showStocks();
            showUsers();
        } catch (Exception e) {
            log(e.getMessage());
        }
    }

    private void resetData() {
        if(usersPanel.getTabs().size() > 1) {
            Tab adminTab = usersPanel.getTabs().get(0);
            usersPanel.getTabs().clear();
            usersPanel.getTabs().add(adminTab);
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
            URL url = getClass().getResource("/app/fxml/UserFX.fxml");
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
                }
            } catch (Exception e) {
                System.out.println("Resource not valid ");
            }
        }
    }

    @FXML
    @Override
    protected Boolean call() throws Exception {
        updateProgress(0, 100);

        for (int i = 0; i < 10; i++) {
            Thread.sleep(SLEEP_TIME);
            updateProgress(i + 1, 10);
        }

        return Boolean.TRUE;
    }

    public void loadXML() {
        currentRunningTask = this;
        bindTaskToUIComponents(currentRunningTask);
        thread = new Thread(currentRunningTask);
        thread.start();
    }


    public void bindTaskToUIComponents(Task<Boolean> aTask) {
        // task progress bar
        taskProgressBar.progressProperty().bind(aTask.progressProperty());

        // task percent label
        progressPercentLabel.textProperty().bind(
                Bindings.concat(
                        Bindings.format(
                                "%.0f",
                                Bindings.multiply(
                                        aTask.progressProperty(),
                                        100)),
                        " %"));

        // task cleanup upon finish
        aTask.valueProperty().addListener((observable, oldValue, newValue) -> {
            onTaskFinished();
        });
    }

    private void onTaskFinished() {
        this.progressPercentLabel.textProperty().unbind();
        this.taskProgressBar.progressProperty().unbind();
    }


    public void loadFileButton(ActionEvent actionEvent) {
        loadFile();
    }
}
