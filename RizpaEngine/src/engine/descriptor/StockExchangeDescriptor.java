package engine.descriptor;

import engine.Transaction;
import engine.command.Command;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.TreeSet;

public class StockExchangeDescriptor implements Serializable {
    private Users users;
    private Stocks stocks;
    private HashMap<String, TreeSet<Command>> stockSymbol2BuyOffers;
    private HashMap<String, TreeSet<Command>> stockSymbol2SellOffers;
    private HashMap<String, TreeSet<Transaction>> stockSymbol2transactions;
    private final String SYMBOL_NOT_FOUND_EXCEPTION_MESSAGE = "Symbol not found";

    public StockExchangeDescriptor(Stocks stocks, Users users) {
        this.users = users;
        this.stocks = stocks;
        this.stockSymbol2BuyOffers = new HashMap<>();
        this.stockSymbol2SellOffers = new HashMap<>();
        this.stockSymbol2transactions = new HashMap<>();
        for (Stock stock : stocks.getStocks()) {
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
        if (transactions == null) {
            throw new RuntimeException(SYMBOL_NOT_FOUND_EXCEPTION_MESSAGE);
        }

        return transactions;
    }

    public void committedTransaction(Transaction transactionToCommit) {
        String symbol = transactionToCommit.getSymbol();
        TreeSet<Transaction> stockTransactions = stockSymbol2transactions.get(symbol);
        if (stockTransactions != null) {
            User buyer = getUserByName(transactionToCommit.getBuyerName());
            User seller = getUserByName(transactionToCommit.getSellerName());
            Item item = new Item(symbol, transactionToCommit.getDealData().getAmount());
            buyer.getHoldings().addItem(item);
            seller.getHoldings().removeItem(item);

            stockTransactions.add(transactionToCommit);
        } else {
            throw new RuntimeException(SYMBOL_NOT_FOUND_EXCEPTION_MESSAGE);
        }
    }

    public Collection<Command> getSellOffers(String symbol) {
        return stockSymbol2SellOffers.get(symbol);
    }

    public Collection<Command> getBuyOffers(String symbol) {
        return stockSymbol2BuyOffers.get(symbol);
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

    public Users getUsers() {
        return users;
    }

    public User getUserByName(String userName){
        User user = null;
        for (User userToCheck : users) {
            if(userToCheck.getName().equals(userName))
            {
                user = userToCheck;
                break;
            }
        }

        return user;
    }
}
