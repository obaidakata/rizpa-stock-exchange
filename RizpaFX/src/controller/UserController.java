package controller;

import appManeger.AppManager;
import engine.descriptor.Item;
import engine.descriptor.User;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import rizpa.RizpaFacade;

import java.util.stream.Collectors;

public class UserController {
    private User user;
    private RizpaFacade rizpaFacade;

    @FXML private Label username;
    @FXML private Label userTotalStocks;

    @FXML private TableView<Item> holdingStocksTable;

    @FXML private TableColumn<Item, String> symbolColumn;
    @FXML private TableColumn<Item, String> amountColumn;
    @FXML private TableColumn<Item, String> currentPriceColumn;

    @FXML private ChoiceBox<String> commandTypeChoiceBox;

    @FXML private ChoiceBox<String> stocksPicker;
    @FXML private ChoiceBox<String> commandDirectionChoiceBox;

    private final ObservableList<String> stocksToChooseFrom = FXCollections.observableArrayList();

    @FXML private Spinner<Integer> amountSpinner;

    @FXML
    public void initialize() {
        this.rizpaFacade = AppManager.getInstance().getRizpaFacade();
        initStocksPicker();
        initCommandDirectionChoiceBox();
        commandDirectionChoiceBox.getSelectionModel().selectedItemProperty().addListener(this::commandTypeChanged);
        symbolColumn.setCellValueFactory(item -> new SimpleStringProperty(item.getValue().getSymbol()));
        amountColumn.setCellValueFactory(item -> new SimpleStringProperty(String.valueOf(item.getValue().getQuantity())));
        stocksPicker.setItems(stocksToChooseFrom);

    }

    private void commandTypeChanged(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
        updateStocksPicker(newValue);
    }

    private void initStocksPicker() {
        commandTypeChoiceBox.getItems().addAll("LMT", "MKT");
        commandTypeChoiceBox.setValue("LMT");
//        stocksPicker.setItems(stocksToChooseFrom);
//        stocksPicker.setValue(stocksToChooseFrom.get(0));
    }

    private void initCommandDirectionChoiceBox() {
        commandDirectionChoiceBox.getItems().addAll("Buy", "Sell");
        commandDirectionChoiceBox.setValue("Buy");
        updateStocksPicker("Buy");
    }

    private void initAmountSpinner()
    {
    }

    public void setUser(User user) {
        this.user = user;
        username.setText(user.getName());
        holdingStocksTable.getItems().addAll(user.getHoldings());
    }

    private void updateStocksPicker(String newValue) {
        if(newValue.equals("Buy"))
        {
            stocksToChooseFrom.clear();
            stocksToChooseFrom.addAll(rizpaFacade.getAllSymbols());
        }
        else if(newValue.equals("Sell"))
        {
            stocksToChooseFrom.clear();
            stocksToChooseFrom.addAll(user
                    .getHoldings()
                    .stream()
                    .map(Item::getSymbol)
                    .collect(Collectors.toList()));
        }

        if(stocksToChooseFrom.size() > 0) {
            stocksPicker.setValue(stocksToChooseFrom.get(0));
        }
    }
}
