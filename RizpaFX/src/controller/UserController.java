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
import javafx.scene.input.MouseEvent;
import rizpa.RizpaFacade;

import java.util.stream.Collectors;

public class UserController {
    
    private User user;
    private RizpaFacade rizpaFacade;

    @FXML private Button submitButton;
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

    private final SpinnerValueFactory<Integer> spinnerIntegerMaxValue = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, 0);

    @FXML private Spinner<Integer> priceSpinner;

    @FXML
    public void initialize() {
        this.rizpaFacade = AppManager.getInstance().getRizpaFacade();
        initStocksPicker();
        initCommandDirectionChoiceBox();
        commandDirectionChoiceBox.getSelectionModel().selectedItemProperty().addListener(this::commandDirectionChanged);
        commandTypeChoiceBox.getSelectionModel().selectedItemProperty().addListener(this::commandTypeChanged);
        symbolColumn.setCellValueFactory(item -> new SimpleStringProperty(item.getValue().getSymbol()));
        amountColumn.setCellValueFactory(item -> new SimpleStringProperty(String.valueOf(item.getValue().getQuantity())));
        stocksPicker.setItems(stocksToChooseFrom);
        priceSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, 0));

    }


    private void commandTypeChanged(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
        if(newValue.equals("LMT")) {
            priceSpinner.setDisable(false);
        }
        else if(newValue.equals("MKT")) {
            priceSpinner.setDisable(true);
        }
    }


    private void commandDirectionChanged(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
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

    public void setUser(User user) {
        this.user = user;
        username.setText(user.getName());
        holdingStocksTable.getItems().addAll(user.getHoldings());
    }

    private void updateStocksPicker(String newValue) {
        if(newValue.equals("Buy")) {
            stocksToChooseFrom.clear();
            stocksToChooseFrom.addAll(rizpaFacade.getAllSymbols());
            amountSpinner.setValueFactory(spinnerIntegerMaxValue);
        }
        else if(newValue.equals("Sell")) {
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

        updateAmountSpinner();

    }

    private void updateAmountSpinner()
    {
        if(commandDirectionChoiceBox.getValue().equals("Sell")) {
            String stockSymbol = stocksPicker.getValue();
            int stockAmount = user.getHoldings().getStockAmount(stockSymbol);
            amountSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, stockAmount, 0));
        }
    }

    public void onSubmitButtonClicked(MouseEvent mouseEvent) {
        String type = commandTypeChoiceBox.getValue();
        if(type.equals("LMT")) {

            String symbol = stocksPicker.getValue();
            int amount = 5;
            rizpaFacade.doLimitCommand(user.getName(),
                    commandDirectionChoiceBox.getValue(),
                    symbol,
                    amount,
                    priceSpinner.getValue()
            );
//
//            if(commandDirectionChoiceBox.getValue().equalsIgnoreCase("Sell")) {
//                user.getHoldings().commit(symbol, amount);
//            }
        }
        else if(type.equals("MKT")) {
        }
//        System.out.println(username.getText());
//        System.out.println(stocksPicker.getValue());
//        System.out.println(commandDirectionChoiceBox.getValue());
//        System.out.println(commandTypeChoiceBox.getValue());
    }
}
