package app.engine;

import java.util.*;

public class StockExchangeDescriptor {
    private Stocks stocks;
    private final HashMap<String, TreeSet<Command>> stockSymbol2BuyOffers;
    private final HashMap<String, TreeSet<Command>> stockSymbol2SellOffers;
    private final HashMap<String, TreeSet<Transaction>> stockSymbol2transactions;
    private final String SYMBOL_NOT_FOUND_EXCEPTION_MESSAGE = "Symbol not found";

    public StockExchangeDescriptor(Stocks stocks) {
        this.stocks = stocks;
        this.stockSymbol2BuyOffers = new HashMap<>();
        this.stockSymbol2SellOffers = new HashMap<>();
        this.stockSymbol2transactions = new HashMap<>();
        for(Stock stock : stocks.getStocks()) {
            String symbol = stock.getSymbol();
            this.stockSymbol2transactions.put(symbol, new TreeSet<>());
            this.stockSymbol2BuyOffers.put(symbol, new TreeSet<>(Command::compareBuy));
            this.stockSymbol2SellOffers.put(symbol, new TreeSet<>(Command::compareSell));
        }
    }

    public Stocks getStocks() {
        return stocks;
    }

    public void setStocks(Stocks stocks) {
        this.stocks = stocks;
    }

    public Collection<Transaction> getStockTransactions(String symbol) {
        TreeSet<Transaction> transactions = stockSymbol2transactions.get(symbol);
        if(transactions == null) {
            throw new RuntimeException(SYMBOL_NOT_FOUND_EXCEPTION_MESSAGE);
        }

        return transactions;
    }

    public void addBuyOffer(Command command) {
        TreeSet<Command> buyCommands =  stockSymbol2BuyOffers.get(command.getStockSymbol());
        if(buyCommands != null){
            buyCommands.add(command);
        }
        else {
            throw new RuntimeException(SYMBOL_NOT_FOUND_EXCEPTION_MESSAGE);
        }

    }

    public void addSellOffer(Command command) {
        String symbol = command.getStockSymbol();
        TreeSet<Command> sellOffers = stockSymbol2SellOffers.get(symbol);
        if(sellOffers != null) {
            sellOffers.add(command);
        }
        else {
            throw new RuntimeException(SYMBOL_NOT_FOUND_EXCEPTION_MESSAGE);
        }
    }

    public void committedTransaction(Transaction transactionToCommit){
        String symbol = transactionToCommit.getSymbol();
        TreeSet<Transaction> stockTransactions = stockSymbol2transactions.get(symbol);
        if(stockTransactions != null) {
           stockTransactions.add(transactionToCommit);
        }
        else{
            throw new RuntimeException(SYMBOL_NOT_FOUND_EXCEPTION_MESSAGE);
        }
    }

    public Collection<Command> getSellOffers(String symbol) {
        TreeSet<Command> sellOffers = stockSymbol2SellOffers.get(symbol);
        if(sellOffers != null){
            return  sellOffers;
        }

        throw new RuntimeException(SYMBOL_NOT_FOUND_EXCEPTION_MESSAGE);
    }

    public Collection<Command> getBuyOffers(String symbol) {
        TreeSet<Command> buyOffers = stockSymbol2BuyOffers.get(symbol);
        if(buyOffers != null) {
            return buyOffers;
        }

        throw new RuntimeException(SYMBOL_NOT_FOUND_EXCEPTION_MESSAGE);
    }

    @Override
    public String toString() {
        return "StockExchangeDescriptor{" +
                "stocks=" + stocks +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StockExchangeDescriptor that = (StockExchangeDescriptor) o;

        return stocks != null ? stocks.equals(that.stocks) : that.stocks == null;
    }

    @Override
    public int hashCode() {
        return stocks != null ? stocks.hashCode() : 0;
    }
}
