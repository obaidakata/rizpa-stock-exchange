package app.controller;

import app.appManeger.AppManager;
import engine.Transaction;
import engine.descriptor.Holdings;
import engine.descriptor.Item;
import engine.descriptor.User;
import javafx.beans.Observable;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.util.StringConverter;
import rizpa.RizpaFacade;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UserController {


    private User user;
    private RizpaFacade rizpaFacade;
    private final List<Runnable> runnableList = new ArrayList<>();

    @FXML
    private Label username;
    @FXML
    private Label userTotalStocks;

    @FXML
    private Button submitButton;

    @FXML
    private TableView<Item> holdingStocksTable;

    @FXML
    private TableColumn<Item, String> symbolColumn;
    @FXML
    private TableColumn<Item, String> amountColumn;
    @FXML
    private TableColumn<Item, String> currentPriceColumn;

    @FXML
    private ChoiceBox<String> commandTypeChoiceBox;

    @FXML
    private ChoiceBox<String> stocksPicker;
    @FXML
    private ChoiceBox<String> commandDirectionChoiceBox;

    private final ObservableList<String> stocksToChooseFrom = FXCollections.observableArrayList();

    @FXML
    private Spinner<Integer> amountSpinner;

    private final SpinnerValueFactory<Integer> spinnerIntegerMaxValue = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, 0);

    @FXML
    private Spinner<Integer> priceSpinner;

    @FXML
    public void initialize() {
        this.rizpaFacade = AppManager.getInstance().getRizpaFacade();
        initStocksPicker();
        initCommandDirectionChoiceBox();
        commandDirectionChoiceBox.getSelectionModel().selectedItemProperty().addListener(this::commandDirectionChanged);
        commandTypeChoiceBox.getSelectionModel().selectedItemProperty().addListener(this::commandTypeChanged);
        symbolColumn.setCellValueFactory(item -> new SimpleStringProperty(item.getValue().getSymbol()));
        amountColumn.setCellValueFactory(item -> new SimpleStringProperty(String.valueOf(item.getValue().getQuantity())));
        currentPriceColumn.setCellValueFactory(item -> new SimpleStringProperty(String.valueOf(rizpaFacade.getStockBySymbol(item.getValue().getSymbol()).getPrice())));
        stocksPicker.setItems(stocksToChooseFrom);
        stocksPicker.getSelectionModel().selectedItemProperty().addListener(this::updateAmountSpinner);
        priceSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, 0));

        amountSpinner.focusedProperty().addListener((s, ov, nv) -> {
            if (nv) return;
            commitEditorText(amountSpinner);
        });

        priceSpinner.focusedProperty().addListener((s, ov, nv) -> {
            if (nv) return;
            commitEditorText(priceSpinner);
        });
    }

    private void updateAmountSpinner(Observable observable) {
        updateAmountSpinner();
    }

    private <T> void commitEditorText(Spinner<T> spinner) {
        if (!spinner.isEditable()) return;
        String text = spinner.getEditor().getText();
        SpinnerValueFactory<T> valueFactory = spinner.getValueFactory();
        if (valueFactory != null) {
            StringConverter<T> converter = valueFactory.getConverter();
            if (converter != null && isNumeric(text)) {
                T value = converter.fromString(text);
                valueFactory.setValue(value);
            }
            else
            {
                spinner.getEditor().setText("0");
            }
        }
    }

    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }


    private void commandTypeChanged(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
        if (newValue.equals("LMT")) {
            priceSpinner.setDisable(false);
        } else if (newValue.equals("MKT")) {
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
        user.getHoldings().addOnChangeListener(this::onHoldingsChanged);
        updateUserTotalStocks();
    }

    private void updateStocksPicker(String newValue) {
        if (newValue.equals("Buy")) {
            stocksToChooseFrom.clear();
            stocksToChooseFrom.addAll(rizpaFacade.getAllSymbols());
            amountSpinner.setValueFactory(spinnerIntegerMaxValue);
        } else if (newValue.equals("Sell")) {
            stocksToChooseFrom.clear();
            stocksToChooseFrom.addAll(user
                    .getHoldings()
                    .stream()
                    .map(Item::getSymbol)
                    .collect(Collectors.toList()));

        }

        if (stocksToChooseFrom.size() > 0) {
            stocksPicker.setValue(stocksToChooseFrom.get(0));
        }

        updateAmountSpinner();

    }

    private void updateAmountSpinner() {
        if (commandDirectionChoiceBox.getValue().equals("Sell")) {
            String stockSymbol = stocksPicker.getSelectionModel().getSelectedItem();
            int stockAmount = user.getHoldings().getStockAmount(stockSymbol);
            amountSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, stockAmount, 0));
        }
    }

    public void onSubmitButtonClicked(MouseEvent mouseEvent) {
        String type = commandTypeChoiceBox.getValue();
        String symbol = stocksPicker.getValue();
        String username = user.getName();
        String commandDirection = commandDirectionChoiceBox.getValue();
        int amount = amountSpinner.getValue();

        String message = String.format("%s wants to %s %d %s stocks", username, commandDirection, amount, symbol);

        List<Transaction> newTransactions = null;
        try {
            if (type.equals("LMT")) {
                int price = priceSpinner.getValue();
                message += String.format(", each stock for %d", price);
                newTransactions = rizpaFacade.doLMTCommand(username, commandDirection, symbol, amount, price);
            } else if (type.equals("MKT")) {
                newTransactions = rizpaFacade.doMKTCommand(username, commandDirection, symbol, amount);
            }
        } catch (Exception e) {
            message = e.getMessage();
        }

        for (Runnable runnable : runnableList) {
            runnable.run();
        }

        AppManager.getInstance().log(message);
        logNewTransaction(newTransactions);

        if (newTransactions != null && newTransactions.size() > 0) {
            holdingStocksTable.getItems().clear();
            holdingStocksTable.getItems().addAll(user.getHoldings());
        }
    }

    private void logNewTransaction(List<Transaction> newTransactions) {
        if (newTransactions != null) {
            for (Transaction newTransaction : newTransactions) {
                String transactionDetails = String.format("%s sold to %s %d %s, for a total of %d",
                        newTransaction.getSellerName(),
                        newTransaction.getBuyerName(),
                        newTransaction.getDealData().getAmount(),
                        newTransaction.getSymbol(),
                        newTransaction.getDealData().getDealPrice());
                AppManager.getInstance().log(transactionDetails);
            }
            if(newTransactions.size() == 0)
            {
                AppManager.getInstance().log("No Transactions has been made.");
            }
        }
    }

    public void addOnSubmitListener(Runnable runnable) {
        runnableList.add(runnable);
    }

    private void onHoldingsChanged() {
        Holdings userHoldings = user.getHoldings();
        holdingStocksTable.getItems().clear();
        holdingStocksTable.getItems().addAll(userHoldings);
        if (userHoldings.getStockAmount(stocksPicker.getValue()) == 0) {
            updateStocksPicker(commandDirectionChoiceBox.getValue());
        }

        updateUserTotalStocks();
    }

    private void updateUserTotalStocks() {
        Holdings userHoldings = user.getHoldings();
        int totalSum = userHoldings
                .stream()
                .map(item -> rizpaFacade.getStockBySymbol(item.getSymbol()).getPrice() * item.getQuantity())
                .reduce(Integer::sum)
                .orElse(0);
        userTotalStocks.setText(String.valueOf(totalSum));
    }
}
