package rizpa;

import engine.DealData;
import engine.RizpaEngine;
import engine.Transaction;
import engine.command.Command;
import engine.command.CommandDirection;
import engine.descriptor.*;
import rizpa.generated.RizpaStockExchangeDescriptor;

import java.io.InputStream;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RizpaFacade {
    private final RizpaDataConverter converter;
    private final RizpaXmlParser parser;

    private final RizpaEngine rizpaEngine;

    public RizpaFacade() {
        this.converter = new RizpaDataConverter();
        this.rizpaEngine = new RizpaEngine();
        this.parser = new RizpaXmlParser();
    }

    public Collection<DealData> getTransactions(String symbol) {
        return rizpaEngine.getStockTransactions(symbol)
                .stream()
                .map(Transaction::getDealData)
                .collect(Collectors.toList());
    }

    public List<Stock> getAllStocks() {
        return rizpaEngine.getAllStocks();
    }

    public void loadNewData(String filePath) throws Exception {
        RizpaStockExchangeDescriptor newRizpaStockExchangeDescriptor = parser.parseOldData(filePath);
        StocksManager descriptor = converter.getStockManager(newRizpaStockExchangeDescriptor);
        checkData(descriptor);
        rizpaEngine.loadNewData(descriptor);
    }

    public void loadNewData(String username, InputStream inputStream) throws Exception {
        RizpaStockExchangeDescriptor newRizpaStockExchangeDescriptor = parser.parseInputStream(inputStream);
        StocksManager descriptor = converter.getStockManager(newRizpaStockExchangeDescriptor);
        Holdings holdings = converter.getUserHoldings(newRizpaStockExchangeDescriptor);

        checkData(descriptor);
//        System.out.println("---------------------------------------------------------------------------------------------");
//        System.out.println(descriptor);
//        System.out.println(holdings);
//        System.out.println(username);
//        System.out.println("---------------------------------------------------------------------------------------------");
        rizpaEngine.loadNewData(descriptor);
        rizpaEngine.addHoldingToUser(username, holdings);
    }

    private void checkData(StocksManager descriptor) throws Exception {
        if (!rizpaEngine.isAllStocksSymbolUnique(descriptor)) {
            String SYMBOLS_ARE_NOT_UNIQUE = "Symbols are not unique";
            throw new Exception(SYMBOLS_ARE_NOT_UNIQUE);
        } else if (!rizpaEngine.isAllCompaniesNamesUnique(descriptor)) {
            String COMPANIES_NAMES_ARE_NOT_UNIQUE = "Companies names are not unique";
            throw new Exception(COMPANIES_NAMES_ARE_NOT_UNIQUE);
        } else if(!rizpaEngine.isAllUsersNamesUnique(descriptor)) {
            throw new Exception("Users names are not unique");
        }
    }

    public List<Transaction> doLMTCommand(String username, String direction, String symbol, int amount, int limit) throws Exception {
        CommandDirection commandDirection = direction.equalsIgnoreCase("Buy") ? CommandDirection.Buy : CommandDirection.Sell;
        return rizpaEngine.doLMTCommand(username, commandDirection, symbol, amount, limit);
    }

    public List<Transaction> doMKTCommand(String username, String direction, String symbol, int amount) throws Exception {
        CommandDirection commandDirection = direction.equalsIgnoreCase("Buy") ? CommandDirection.Buy : CommandDirection.Sell;
        return rizpaEngine.doMKTCommand(username, commandDirection, symbol, amount);
    }

    public Collection<String> getAllSymbols() {
        return rizpaEngine.getAllSymbols();
    }

    public List<String> getDirections() {
        return Stream.of(CommandDirection.values())
                .map(Enum::name)
                .collect(Collectors.toList());
    }

    public List<DealData> getSellOffers(String stockSymbol) {
        return rizpaEngine.getSellOffers(stockSymbol);
    }

    public Collection<DealData> getBuyOffers(String stockSymbol) {
        return rizpaEngine.getBuyOffers(stockSymbol);
    }

    public int getTransactionsCount(String symbol) {
        Collection<Transaction> transactions = rizpaEngine.getStockTransactions(symbol);
        return transactions != null ? transactions.size() : 0;
    }

    public Stock getStockBySymbol(String symbol) {
        return rizpaEngine.getStockBySymbol(symbol);
    }

    public int getSumSellOffers(String stockSymbol) {
        return rizpaEngine.getSumSellOffers(stockSymbol);
    }

    public int getSumBuyOffers(String stockSymbol) {
        return rizpaEngine.getSumBuyOffers(stockSymbol);
    }

    public int getSumTransactions(String stockSymbol) {
        return rizpaEngine.getSumTransactions(stockSymbol);
    }

    public Users getUsers(){
        return rizpaEngine.getAllUsers();
    }

    public Collection<Command> getSellCommands(String symbol)
    {
        return rizpaEngine.getSellCommands(symbol);
    }

    public Collection<Command> getBuyCommands(String symbol)
    {
        return rizpaEngine.getBuyCommands(symbol);
    }

    public Collection<Transaction> getTransactionsList(String symbol) {
        return rizpaEngine.getStockTransactions(symbol);
    }

    public boolean isUserExists(String username) {
        return rizpaEngine.isUserExists(username);
    }

    public void addUser(String username, String userRole) {
        rizpaEngine.addUser(username, userRole);
    }

    @Override
    public String toString() {
        return "RizpaFacade{}";
    }

    public boolean isUserTrader(String username) {
        return rizpaEngine.isUserTrader(username);
    }

    public User getUserByName(String username) {
        return rizpaEngine.getAllUsers().getUserByName(username);
    }
}
