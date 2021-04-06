package app.rizpa.console.app;

import app.rizpa.RizpaFacade;
import app.rizpa.console.app.generated.RizpaStockExchangeDescriptor;
import app.rizpa.engine.DealData;
import app.rizpa.engine.Stock;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;

public class RizpaConsoleApplication {
    private final Parser parser;
    private final HashMap<String, MenuItem> menu;
    private boolean isExit = false;
    private final Scanner scanner;
    private final RizpaFacade rizpaFacade;
    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss:SSS");
    private RizpaStockExchangeDescriptor rizpaStockExchangeDescriptor = null;
    private final String DEAL_DATA_FORMAT = "| %-12s | %10d | %10d | %10d |%n";
    private final String TITLE_FORMAT = DEAL_DATA_FORMAT.replace("d", "s");


    public RizpaConsoleApplication() {
        parser = new RizpaXmlParser();
        menu = new HashMap<>();
        scanner = new Scanner(System.in);
        rizpaFacade = new RizpaFacade();
        initMenu();
        forTestOnly();
    }

    private void initMenu() {
        menu.put("1", new MenuItem("Read XML file", this::readFilePathUFromUser, true));
        menu.put("2", new MenuItem("Show all stocks", this::showAllStocks));
        menu.put("3", new MenuItem("Show data about one stock", this::getStockInputFromUser));
        menu.put("4", new MenuItem("Do transaction", this::doTransaction));
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
                System.out.println();
                menuItem.getAction().run();
                System.out.println();
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
        menuItem = menuItem != null && menuItem.isAvailable() ? menuItem : null;
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
            enableAllOperations();
            ArrayList<String> stocks = new ArrayList<>();
            stocks.add("googl");
            stocks.add("aapl");
            for (String stock : stocks) {
                rizpaFacade.doLimitCommand("sell", stock, 20, 100);
                rizpaFacade.doLimitCommand("sell", stock, 10, 110);
                rizpaFacade.doLimitCommand("sell", stock, 10, 90);
                rizpaFacade.doLimitCommand("buy", stock, 10, 90);
                rizpaFacade.doLimitCommand("buy", stock, 25, 120);;
                rizpaFacade.doLimitCommand("buy", stock, 30, 90);
                rizpaFacade.doLimitCommand("buy", stock, 20, 80);
                rizpaFacade.doLimitCommand("buy", stock, 10, 100);
                rizpaFacade.doLimitCommand("buy", stock, 5, 90);
                rizpaFacade.doLimitCommand("sell", stock, 5, 90);
                rizpaFacade.doLimitCommand("sell", stock, 25, 95);
                rizpaFacade.doLimitCommand("sell", stock, 20, 90);
                rizpaFacade.doLimitCommand("sell", stock, 12, 90);
                rizpaFacade.doLimitCommand("sell", stock, 13, 85);
            }
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

    private void showAllStocks() {
        List<Stock> stocks = rizpaFacade.getAllStocks();
        for (Stock stock : stocks) {
            printStockAndPrintStockTransactions(stock);
            // Print MAHAZOR ISKAOUT
        }
    }

    private void printStockAndPrintStockTransactions(Stock stockToPrint) {
        if(stockToPrint != null) {
            System.out.println(stockToPrint);
            Collection<DealData> transactions = rizpaFacade.getTransactions(stockToPrint.getSymbol());
            printDeals(transactions);
            System.out.println();
        }
    }

    private void ShowDataAboutOneStock(String requestedStock) {
        List<Stock> stocks = rizpaFacade.getAllStocks();
        boolean isExist = false;
        for (Stock stock: stocks)
        {
            if(requestedStock.equalsIgnoreCase(stock.getSymbol()))
            {
                printStockAndPrintStockTransactions(stock);
                isExist = true;
            }

            if(isExist)
            {
                break;
            }
        }

        //todo: Transactions

        if(!isExist)
        {
            System.out.println("Can't find stock symbol, please enter a valid one.");
        }
    }

    private void getStockInputFromUser()
    {
        System.out.println("Please enter the symbol of the stock");
        String stockSymbol = scanner.nextLine();
        ShowDataAboutOneStock(stockSymbol);
    }

    private boolean isInputPositiveInteger(String amountToCheck) {
        Integer amountAsInt = null;
        try {
            amountAsInt = Integer.parseInt(amountToCheck);
        } catch (NumberFormatException ignored) { }
        return amountAsInt != null && amountAsInt > 0 ;
    }

    private void doTransaction() {
        System.out.println("Please enter stock symbol");
        String symbol = getInputOfThePossibleOptions("symbol not found", rizpaFacade.getAllSymbols());

        List<String> directions = rizpaFacade.getDirections();
        System.out.println("Please enter \"buy\" for buy or \"sell\" for sell");
        String direction = getInputOfThePossibleOptions("direction should be \"buy\" or \"sell\" only ", directions);

        System.out.println("Please type the amount of stocks you want to " + direction);
        int amount =getIntegerFromUser("amount should be positive integer", this::isInputPositiveInteger);

        List<String> implementedCommand = new ArrayList<>();
        implementedCommand.add("LMT");
        System.out.println("Please enter \"LMT\" for LIMIT");
        String command = getInputOfThePossibleOptions("command should be \"LMT\" only ", implementedCommand);

        System.out.println("Please type " + direction + " limit");
        int limit = getIntegerFromUser("limit should be positive integer", this::isInputPositiveInteger);

        System.out.println("" + direction + "" + symbol + "" + amount + "" +limit);

        rizpaFacade.doLimitCommand(direction, symbol, amount, limit);
    }

    private int getIntegerFromUser(String errorMassage, Function<String, Boolean> checkStrategy) {
        boolean isOk = false;
        String inputAsString = "";
        do {
            inputAsString = scanner.nextLine();
            if(checkStrategy.apply(inputAsString)){
                isOk = true;
            }
            if(!isOk) {
                System.out.println("please try again, " + errorMassage);
            }
        } while (!isOk);

        return Integer.parseInt(inputAsString);
    }

    private String getInputOfThePossibleOptions(String errorMassage, List<String> options) {
        String input = null;
        do {
            String userInput = scanner.nextLine();
            for (String option : options) {
                if(option.equalsIgnoreCase(userInput)){
                    input = option;
                    break;
                }
            }
            if(input == null) {
                System.out.println("please try again, " + errorMassage);
            }
        } while (input == null);

        return input;
    }
    private void showALlActions() {
        for (String stockSymbol : rizpaFacade.getAllSymbols()) {
            System.out.println("Stock symbol " + stockSymbol);
            System.out.println("Buy offers list");
            printDeals(rizpaFacade.getBuyOffers(stockSymbol));

            System.out.println("Sell offers list");
            printDeals(rizpaFacade.getSellOffers(stockSymbol));

            System.out.println("Transactions list");
            printDeals(rizpaFacade.getTransactions(stockSymbol));
        }
    }

    private void printDeals(Collection<DealData> deals) {
        if (deals.size() > 0) {

            String s = String.join("", Collections.nCopies(55, "-"));
            System.out.println(s);
            System.out.printf(TITLE_FORMAT, "Time stamp", "amount", "price", "mahazor");
            System.out.println(s);
            int i = 1;
            for (DealData deal : deals) {
                printDealData(deal, i++);
            }
            System.out.println(s);
        } else {
            System.out.println("No data found");
        }
    }

    private void printDealData(DealData deal, int i){

        System.out.printf(DEAL_DATA_FORMAT,
                simpleDateFormat.format(deal.getTimeStamp()),
                deal.getAmount(),
                deal.getPrice(),
                deal.getCurrentStockPrice()
                );
    }
}
