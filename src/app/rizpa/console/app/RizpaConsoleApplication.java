package app.rizpa.console.app;

import app.rizpa.RizpaFacade;
import app.rizpa.console.app.generated.RizpaStockExchangeDescriptor;
import app.rizpa.engine.RizpaEngine;
import app.rizpa.engine.Stock;
import app.rizpa.engine.Transaction;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class RizpaConsoleApplication {
    private final Parser parser;
    private final HashMap<String, MenuItem> menu;
    private boolean isExit = false;
    private final Scanner scanner;
    private RizpaStockExchangeDescriptor rizpaStockExchangeDescriptor = null;
    private final RizpaFacade rizpaFacade;

    public RizpaConsoleApplication() {
        parser = new RizpaXmlParser();
        menu = new HashMap<>();
        scanner = new Scanner(System.in);
        rizpaFacade = new RizpaFacade();
        initMenu();
//        forTestOnly();
    }

    private void initMenu() {
        menu.put("1", new MenuItem("Read XML file", this::readFilePathUFromUser, true));
        menu.put("2", new MenuItem("Show all stocks", this::showAllStocks));
        menu.put("3", new MenuItem("Show data about one stock", this::getStockInputFromUser));
        menu.put("4", new MenuItem("Do action", this::doAction));
        menu.put("5", new MenuItem("Show all actions", this::showALlActions));
        menu.put("6", new MenuItem("Exit", this::needToExit, true));
    }

    private void printMenu() {
        for (String menuItem : menu.keySet()){
            System.out.printf("%s) %s\n", menuItem, menu.get(menuItem).getActionName());
        }
    }

    public void run() {
        do {
            printMenu();
            MenuItem menuItem = getUserChoice();
            if(menuItem != null) {
                System.out.println(menuItem.getActionName());
                menuItem.getAction().run();
            }
            else {
                System.out.println("Operation not available, please provide XML file first.");
            }

        }while(!isExit);
        System.out.println("Bye Bye!");
    }

    private MenuItem getUserChoice() {
        String userChoice = scanner.nextLine();
        MenuItem menuItem = menu.get(userChoice);
        menuItem = menuItem.isAvailable() ? menuItem : null;
        return menuItem;
    }

    private void needToExit() {
        isExit = true;
    }

    private void forTestOnly() {
        try {
            RizpaStockExchangeDescriptor newRizpaStockExchangeDescriptor = parser.parse("src/resources/ex1-small.xml");
            rizpaStockExchangeDescriptor = newRizpaStockExchangeDescriptor != null ? newRizpaStockExchangeDescriptor : rizpaStockExchangeDescriptor;
            rizpaFacade.loadData(rizpaStockExchangeDescriptor);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void readFilePathUFromUser(){
        System.out.println("Please type file path");
        String filePath = scanner.nextLine();
        try {
            RizpaStockExchangeDescriptor newRizpaStockExchangeDescriptor = parser.parse(filePath);
            rizpaStockExchangeDescriptor = newRizpaStockExchangeDescriptor != null ? newRizpaStockExchangeDescriptor : rizpaStockExchangeDescriptor;
            rizpaFacade.loadData(rizpaStockExchangeDescriptor);
            enableAllOperations();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void enableAllOperations() {
        for(MenuItem menuItem : menu.values()) {
            menuItem.setAvailable(true);
        }
    }

    //(2)
    private void showAllStocks() {
        List<Stock> stocks = rizpaFacade.getAllStocks();
        for (Stock stock : stocks) {
            System.out.println(stock);
            List<Transaction> stockTransactions = rizpaFacade.getTransactions(stock.getSymbol());
            System.out.println(stockTransactions == null ? "No transactions" : stockTransactions.toString());
            // Print MAHAZOR ISKAOUT
        }
    }

    //(3) - Need to do the transactions part.
    private void ShowDataAboutOneStock(String requestedStock) {
        List<Stock> stocks = rizpaFacade.getAllStocks();
        Boolean isExist = false;
        for (Stock stock: stocks)
        {
            if(requestedStock.toUpperCase().equals(stock.getSymbol()))
            {
                System.out.println(stock);
                isExist = true;
            }

            if(isExist == true)
            {
                break;
            }
        }

        //todo: Transactions

        if(isExist == false)
        {
            System.out.println("Can't find stock symbol, please enter a valid one.");
        }
    }

    private void getStockInputFromUser()
    {
        System.out.println("Please enter the symbol of the stock");
        Scanner sc = new Scanner(System.in);
        String stockSymbol = sc.nextLine();
        ShowDataAboutOneStock(stockSymbol);
    }

    private void doAction() {
    }

    private void showALlActions() {
    }
}
