package app;

import engine.DealData;
import engine.descriptor.Stock;
import rizpa.RizpaFacade;
import rizpa.generated.*;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;

@XmlRootElement
public class RizpaConsoleApplication {
    private final HashMap<String, MenuItem> menu;
    private final Scanner scanner;
    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss:SSS");
    private final String DEAL_DATA_FORMAT = "| %-12s | %10d | %10d | %10d |%n";
    private final String TITLE_FORMAT = DEAL_DATA_FORMAT.replace("d", "s");

    private boolean isExit = false;
    private boolean isReturn = false;
    private boolean isFileValid = false;
    private RizpaFacade rizpaFacade;



    public RizpaConsoleApplication() {
        menu = new HashMap<>();
        scanner = new Scanner(System.in);
        rizpaFacade = new RizpaFacade();
        initMenu();
    }

    public void run() {
        do {
            printMenu(menu);
            MenuItem menuItem = getUserChoiceForMenu(menu);
            if(menuItem != null) {
                System.out.println();
                menuItem.getAction().run();
                System.out.println();
            }
            else if(!isFileValid){
                System.out.println("Operation not available, please load system data first.");
            }
        }while(!isExit);
        System.out.println("Bye Bye!");
    }

    HashMap<String , MenuItem> readFileMenu = new HashMap<>();

    private void initMenu() {
        menu.put("1", new MenuItem("Load system data", this::loadSystemData, true));
        readFileMenu.put("1", new MenuItem("Load new data from XML file", this::loadXML, true));
        readFileMenu.put("2", new MenuItem("Load previous data", this::loadPreviousData, true));
        readFileMenu.put("3", new MenuItem("Return", this::needToReturn, true));

        menu.put("2", new MenuItem("Show all stocks", this::showAllStocks));
        menu.put("3", new MenuItem("Show data about one stock", this::printOneStockDetailsBasedOnUserChoice));
        menu.put("4", new MenuItem("Do transaction", this::doTransaction));
        menu.put("5", new MenuItem("Show all actions", this::showALlActions));
        menu.put("6", new MenuItem("Save all data to file", this::saveAllData, false));
        menu.put("7", new MenuItem("Exit", this::needToExit, true));
    }

    private void saveAllData() {
        System.out.println("Please type file path where you want to save the file");
        String filePath = scanner.nextLine();
        try {
            String path  = rizpaFacade.saveAllData(filePath);
            System.out.println("Data saved successfully path: " + path);
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    private void needToReturn() {
        isReturn = true;
    }

    //menu items

    private void loadPreviousData(){
        System.out.println("Please type file path");
        String filePath = scanner.nextLine();
        try {
            rizpaFacade.loadPreviousData(filePath);
            enableAllOperations();
            isFileValid = true;
            System.out.println("File loaded successfully");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void loadXML(){
        System.out.println("Please type file path");
        String filePath = scanner.nextLine();
        try {
            rizpaFacade.loadNewData(filePath);
            enableAllOperations();
            isFileValid = true;
            System.out.println("File loaded successfully");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void loadSystemData(){
        do {
            printMenu(readFileMenu);
            MenuItem menuItem = getUserChoiceForMenu(readFileMenu);
            if(menuItem != null) {
                System.out.println();
                menuItem.getAction().run();
                System.out.println();
            }
        }while(!(isReturn || isFileValid));
    }

    private void showAllStocks() {
        List<Stock> stocks = rizpaFacade.getAllStocks();
        int companyNameLength = Math.max(Stock.getLongestSymbolLength(), 12);
        int symbolLength = Math.max(Stock.getLongestSymbolLength(), 7);
        String stockFormat = "| %" + -symbolLength + "s |" +
                " %" + -companyNameLength + "s | %-10d | %-22d | %-25d |%n";
        String stockTitleFormat = stockFormat.replace("d", "s");
        int linLength = companyNameLength + symbolLength  + 72;
        String s = String.join("", Collections.nCopies(linLength, "-"));
        System.out.println(s);
        System.out.printf(stockTitleFormat,
                "Symbol",
                "Company name",
                "Price",
                "Number of transactions",
                "Sum of all transactions");
        System.out.println(s);
        for (Stock stock : stocks) {
            printStock(stockFormat, stock);
        }

        System.out.println(s);
    }

    private void printOneStockDetailsBasedOnUserChoice() {
        System.out.println("Please enter the symbol of the stock");
        Stock stock = rizpaFacade.getStockBySymbol(scanner.nextLine());
        if(stock != null){
            String titleFormat = "Symbol: %s, Company name: %s, Price: %d, Number of transactions: %d, Sum of all transactions: %d %n";
            printStock(titleFormat, stock);
            printDeals(rizpaFacade.getTransactions(stock.getSymbol()));
        }
        else {
            System.out.println("Can't find stock symbol, please enter a valid one.");
        }
    }

    private void doTransaction() {
        System.out.println("Please enter stock symbol");
        String symbol = getInputOfThePossibleOptions("symbol not found", rizpaFacade.getAllSymbols());
        if(symbol == null) {
            return;
        }
        List<String> directions = rizpaFacade.getDirections();
        System.out.println("Please enter \"buy\" for buy or \"sell\" for sell");
        String direction = getInputOfThePossibleOptions("direction should be \"buy\" or \"sell\" only ", directions);
        if(direction == null){
            return;
        }

        System.out.println("Please type the amount of stocks you want to " + direction);
        Integer amount = getIntegerFromUser("amount should be positive integer", this::isInputPositiveInteger);
        if(amount == null){
            return;
        }

        System.out.println("Please type " + direction + " limit");
        Integer limit = getIntegerFromUser("limit should be positive integer", this::isInputPositiveInteger);
        if(limit == null){
            return;
        }

        rizpaFacade.doLimitCommand(direction, symbol, amount, limit);
    }

    private void showALlActions() {
        int i = 1;
        for (String stockSymbol : rizpaFacade.getAllSymbols()) {
            System.out.printf("\t\t\t\t%d - Stock symbol \"%s\" \n", i++, stockSymbol);
            System.out.println("\nBuy offers list");
            printDeals(rizpaFacade.getBuyOffers(stockSymbol));
            printDataInBox("Buy offers Total", rizpaFacade.getSumBuyOffers(stockSymbol));

            System.out.println("\nSell offers list");
            printDeals(rizpaFacade.getSellOffers(stockSymbol));
            printDataInBox("Sell offers Total", rizpaFacade.getSumSellOffers(stockSymbol));

            System.out.println("\nTransactions list");
            printDeals(rizpaFacade.getTransactions(stockSymbol));
            printDataInBox("Transactions Total", rizpaFacade.getSumTransactions(stockSymbol));
        }
    }

    private void printDataInBox(String dataName, int data){
        int dataNameLength =  dataName.length();
        int width = dataNameLength + 17;
        String s = String.join("", Collections.nCopies(width, "-"));
        System.out.println(s);
        System.out.printf("| %"  +  -dataNameLength + "s : %10d |%n", dataName, data);
        System.out.println(s);
    }

    private void needToExit() {
        isExit = true;
    }

    //Other methods

    private void printMenu(HashMap<String, MenuItem> providedMenu) {
        for (String menuItem : providedMenu.keySet()){
            System.out.printf("%s) %s\n", menuItem, providedMenu.get(menuItem).getActionName());
        }
    }

    private MenuItem getUserChoiceForMenu(HashMap<String, MenuItem> providedMenu) {
        String userChoice = scanner.nextLine();
        MenuItem menuItem = providedMenu.get(userChoice);
        if(menuItem != null) {
            menuItem = menuItem.isAvailable() ? menuItem : null;
        }
        else {
            System.out.println("Type one of " + providedMenu.keySet());
        }

        return menuItem;
    }

    private void enableAllOperations() {
        for(MenuItem menuItem : menu.values()) {
            menuItem.setAvailable(true);
        }
    }

    private void printStock(String format, Stock stockToPrint) {
        if(stockToPrint != null) {
            System.out.printf(format,
                    stockToPrint.getSymbol(),
                    stockToPrint.getCompanyName(),
                    stockToPrint.getPrice(),
                    rizpaFacade.getTransactionsCount(stockToPrint.getSymbol()),
                    stockToPrint.getSumOfAllTransactions());
        }
    }

    //Move to facade
    private boolean isInputPositiveInteger(String amountToCheck) {
        Integer amountAsInt = null;
        try {
            amountAsInt = Integer.parseInt(amountToCheck);
        } catch (NumberFormatException ignored) { }
        return amountAsInt != null && amountAsInt > 0 ;
    }

    private Integer getIntegerFromUser(String errorMassage, Function<String, Boolean> checkStrategy) {
        Integer value = null;
        String inputAsString = scanner.nextLine();

        if(checkStrategy.apply(inputAsString)) {
            value = Integer.parseInt(inputAsString);
        }
        else{
            System.out.println("please try again, " + errorMassage);
        }

        return value;
    }

    private String getInputOfThePossibleOptions(String errorMassage, Collection<String> options) {
        String input = null;
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

        return input;
    }

    private void printDeals(Collection<DealData> deals) {
        String s = String.join("", Collections.nCopies(55, "-"));
        System.out.println(s);
        System.out.printf(TITLE_FORMAT, "Time stamp", "amount", "price", "value");
        System.out.println(s);
        for (DealData deal : deals) {
            printDealData(deal);
        }

        if(deals.size() == 0) {
            System.out.println("|\t\t\t\tNo data found");
        }

        System.out.println(s);
    }

    private void printDealData(DealData deal){
        System.out.printf(DEAL_DATA_FORMAT,
                simpleDateFormat.format(deal.getTimeStamp()),
                deal.getAmount(),
                deal.getPrice(),
                deal.getDealPrice());
    }

    @XmlElement
    public void setExit(boolean exit) {
        isExit = exit;
    }

    @XmlElement
    public void setReturn(boolean aReturn) {
        isReturn = aReturn;
    }

    @XmlElement
    public void setFileValid(boolean fileValid) {
        isFileValid = fileValid;
    }

    @XmlElement
    public void setRizpaFacade(RizpaFacade rizpaFacade) {
        this.rizpaFacade = rizpaFacade;
    }
}
