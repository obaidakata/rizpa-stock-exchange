import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TabPane;
import rizpa.RizpaFacade;

public class RizpaController {

    @FXML public Button loadFileButton;
    @FXML public TabPane usersPanel;

    private RizpaFacade rizpaModel;

    public void setModel(RizpaFacade rizpaModel) {
        this.rizpaModel = rizpaModel;
    }

    @FXML
    public void initialize() {
        System.out.println("Inside runnable init 2");
    }

    @FXML
    protected void loadFile(ActionEvent event) {
        System.out.println("loadFile");
    }
}
