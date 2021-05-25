package controller;

import appManeger.AppManager;
import engine.command.Command;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import rizpa.RizpaFacade;


public class AdminController {

    private RizpaFacade rizpaFacade;
    @FXML private ListView<String> stocksList;

    private ObservableList<Command> sellCommands = FXCollections.observableArrayList();
    @FXML private BorderPane sellCommandsTable;
    @FXML private CommandsTableController sellCommandsTableController;

    private ObservableList<Command> buyCommands = FXCollections.observableArrayList();
    @FXML private BorderPane buyCommandsTable;
    @FXML private CommandsTableController buyCommandsTableController;

    @FXML
    public void initialize() {
        this.rizpaFacade = AppManager.getInstance().getRizpaFacade();
        sellCommandsTableController.setTitle("Sell Commands");
        sellCommandsTableController.setCommandsList(sellCommands);

        buyCommandsTableController.setTitle("Buy Commands");
        buyCommandsTableController.setCommandsList(buyCommands);
    }

    public void showStocks() {
        stocksList.getItems().addAll(rizpaFacade.getAllSymbols());
    }

    public void stockSelected(MouseEvent mouseEvent) {
        String symbol = stocksList.getSelectionModel().getSelectedItem();
        sellCommands.clear();
        sellCommands.addAll(rizpaFacade.getSellCommands(symbol));
        buyCommands.clear();
        buyCommands.addAll(rizpaFacade.getBuyCommands(symbol));
    }
}
