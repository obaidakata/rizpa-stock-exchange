package controller;

import engine.DealData;
import engine.TimeStamp;
import engine.command.Command;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import rizpa.RizpaFacade;


public class AdminController {

    private RizpaFacade rizpaModel;
    @FXML private ListView<String> stocksList;
    @FXML private TableView<Command> sellCommandTable;
    @FXML private TableColumn<Command, String> timeStampColumn;
    @FXML private TableColumn<Command, String> typeColumn;
    @FXML private TableColumn<Command, String> amountColumn;
    @FXML private TableColumn<Command, String> priceColumn;
    @FXML private TableColumn<Command, String> usernameColumn;

    private ObservableList<Command> sellCommands = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        sellCommandTable.setItems(sellCommands);
        timeStampColumn.setCellValueFactory(command -> new SimpleStringProperty(command.getValue().getDealData().getTimeStampValue()));
        typeColumn.setCellValueFactory(command -> new SimpleStringProperty(command.getValue().getType().toString()));
        amountColumn.setCellValueFactory(command -> new SimpleStringProperty(String.valueOf(command.getValue().getDealData().getAmount())));
        priceColumn.setCellValueFactory(command -> new SimpleStringProperty(String.valueOf(command.getValue().getDealData().getPrice())));
        usernameColumn.setCellValueFactory(command -> new SimpleStringProperty(String.valueOf(command.getValue().getUsername())));
    }

    public void setModel(RizpaFacade rizpaModel) {
        this.rizpaModel = rizpaModel;
    }

    public void showStocks() {
        stocksList.getItems().addAll(rizpaModel.getAllSymbols());
    }

    public void stockSelected(MouseEvent mouseEvent) {
        String symbol = stocksList.getSelectionModel().getSelectedItem();
        sellCommands.clear();
        sellCommands.addAll(rizpaModel.getSellCommands(symbol));
    }
}
