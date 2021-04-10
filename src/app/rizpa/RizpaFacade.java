package app.rizpa;

import app.console.app.generated.RizpaStockExchangeDescriptor;
import app.engine.*;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RizpaFacade {
    private final RizpaDataConverter converter;
    private final RizpaEngine rizpaEngine;

    public RizpaFacade() {
        this.converter = new RizpaDataConverter();
        this.rizpaEngine = new RizpaEngine();
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

    public void loadData(RizpaStockExchangeDescriptor rizpaStockExchangeDescriptor) throws Exception {
        checkData(rizpaStockExchangeDescriptor);
        StockExchangeDescriptor descriptor = converter.convert(rizpaStockExchangeDescriptor);
        rizpaEngine.loadData(descriptor);
    }

    private void checkData(RizpaStockExchangeDescriptor rizpaStockExchangeDescriptor) throws Exception {
        if(!rizpaEngine.isAllStocksSymbolUnique(rizpaStockExchangeDescriptor)){
            String SYMBOLS_ARE_NOT_UNIQUE = "Symbols are not unique";
            throw new Exception(SYMBOLS_ARE_NOT_UNIQUE);
        }
        else if(!rizpaEngine.isAllCompaniesNamesUnique(rizpaStockExchangeDescriptor)) {
            String COMPANIES_NAMES_ARE_NOT_UNIQUE = "Companies names are not unique";
            throw new Exception(COMPANIES_NAMES_ARE_NOT_UNIQUE);
        }
    }

    public void doLimitCommand(String direction, String symbol, int amount, int limit) {
        CommandDirection commandDirection = direction.equalsIgnoreCase("Buy") ? CommandDirection.Buy : CommandDirection.Sell;
        rizpaEngine.doLimitCommand(commandDirection, symbol, amount, limit);
    }

    public Collection<String> getAllSymbols(){
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
        return transactions != null? transactions.size() : 0;
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
}
