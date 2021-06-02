package app.controller;

import engine.command.Command;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class CommandsTableController{
    @FXML private Label title;
    @FXML private TableView<Command> commandsTable;
    @FXML private TableColumn<Command, String> timeStampColumn;
    @FXML private TableColumn<Command, String> typeColumn;
    @FXML private TableColumn<Command, String> amountColumn;
    @FXML private TableColumn<Command, String> priceColumn;
    @FXML private TableColumn<Command, String> usernameColumn;

    private ObservableList<Command> commands = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        timeStampColumn.setCellValueFactory(command -> new SimpleStringProperty(command.getValue().getDealData().getTimeStampValue()));
        typeColumn.setCellValueFactory(command -> new SimpleStringProperty(command.getValue().getType().toString()));
        amountColumn.setCellValueFactory(command -> new SimpleStringProperty(String.valueOf(command.getValue().getDealData().getAmount())));
        priceColumn.setCellValueFactory(command -> new SimpleStringProperty(String.valueOf(command.getValue().getDealData().getPrice())));
        usernameColumn.setCellValueFactory(command -> new SimpleStringProperty(String.valueOf(command.getValue().getUsername())));
    }

    public void setCommandsList(ObservableList<Command> commands) {
        this.commands = commands;
        commandsTable.setItems(commands);
    }

    public void setTitle(String title) {
        this.title.setText(title);
    }
}
