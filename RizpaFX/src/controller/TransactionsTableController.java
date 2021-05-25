package controller;

import engine.Transaction;
import engine.command.Command;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class TransactionsTableController {

    @FXML private TableView<Transaction> transactionsTable;
    @FXML private TableColumn<Transaction, String> timeStampColumn;
    @FXML private TableColumn<Transaction, String> typeColumn;
    @FXML private TableColumn<Transaction, String> amountColumn;
    @FXML private TableColumn<Transaction, String> priceColumn;
    @FXML private TableColumn<Transaction, String> buyerNameColumn;
    @FXML private TableColumn<Transaction, String> sellerNameColumn;

    private ObservableList<Transaction> transactions = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        timeStampColumn.setCellValueFactory(transaction -> new SimpleStringProperty(transaction.getValue().getDealData().getTimeStampValue()));
        amountColumn.setCellValueFactory(transaction -> new SimpleStringProperty(String.valueOf(transaction.getValue().getDealData().getAmount())));
        priceColumn.setCellValueFactory(transaction -> new SimpleStringProperty(String.valueOf(transaction.getValue().getDealData().getPrice())));
        buyerNameColumn.setCellValueFactory(transaction -> new SimpleStringProperty(String.valueOf(transaction.getValue().getBuyerName())));
        sellerNameColumn.setCellValueFactory(transaction -> new SimpleStringProperty(String.valueOf(transaction.getValue().getSellerName())));
    }

    public void setTransactionsList(ObservableList<Transaction> transactions) {
        this.transactions = transactions;
        transactionsTable.setItems(transactions);
    }
}
