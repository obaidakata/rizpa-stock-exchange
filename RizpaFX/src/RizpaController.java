import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import rizpa.RizpaFacade;

import java.io.File;

public class RizpaController {
    public Label fileStatus;
    private Stage primaryStage;

    @FXML public Button loadFileButton;
    @FXML public TabPane usersPanel;



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
        } catch (Exception e) {
            fileStatus.setText("File failed to load");
        }
    }
}
