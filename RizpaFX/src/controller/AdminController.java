package controller;

import appManeger.AppManager;
import engine.Transaction;
import engine.command.Command;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import rizpa.RizpaFacade;

import java.util.Collection;

public class AdminController {

    private RizpaFacade rizpaFacade;
    @FXML private ListView<String> stocksList;

    private final ObservableList<Command> sellCommands = FXCollections.observableArrayList();
    @FXML private BorderPane sellCommandsTable;
    @FXML private CommandsTableController sellCommandsTableController;

    private final ObservableList<Command> buyCommands = FXCollections.observableArrayList();
    @FXML private BorderPane buyCommandsTable;
    @FXML private CommandsTableController buyCommandsTableController;

    private final ObservableList<Transaction> transactions = FXCollections.observableArrayList();
    @FXML private BorderPane transactionsTable;
    @FXML private TransactionsTableController transactionsTableController;

    @FXML
    public void initialize() {
        this.rizpaFacade = AppManager.getInstance().getRizpaFacade();
        sellCommandsTableController.setTitle("Sell Commands");

        sellCommandsTableController.setCommandsList(sellCommands);


        buyCommandsTableController.setTitle("Buy Commands");
        buyCommandsTableController.setCommandsList(buyCommands);

        transactionsTableController.setTransactionsList(transactions);
    }

    public void showStocks() {
        stocksList.getItems().setAll(rizpaFacade.getAllSymbols());
    }

    public void stockSelected(MouseEvent mouseEvent) {
        onDataChanged();
    }

    public void onDataChanged(){
        String symbol = stocksList.getSelectionModel().getSelectedItem();

        if(symbol != null)
        {
            sellCommands.clear();
            sellCommands.addAll(rizpaFacade.getSellCommands(symbol));

            buyCommands.clear();
            buyCommands.addAll(rizpaFacade.getBuyCommands(symbol));

            transactions.clear();
            transactions.addAll(rizpaFacade.getTransactionsList(symbol));
        }
    }

    public void onSaveButton(ActionEvent actionEvent) {
        try {
            rizpaFacade.saveAllData("/Users/obaidakata/Downloads");
        } catch (Exception e) {
            System.out.println("Failed to save file");
        }
    }
}
