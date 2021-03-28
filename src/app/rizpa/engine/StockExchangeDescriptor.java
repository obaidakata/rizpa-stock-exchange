package app.rizpa.engine;

import java.util.HashMap;
import java.util.List;

public class StockExchangeDescriptor {
    private Stocks stocks;
    private HashMap<String, List<Transaction>> transactions;

    public StockExchangeDescriptor(Stocks stocks) {
        this.stocks = stocks;
        this.transactions = new HashMap<>();
    }

    public Stocks getStocks() {
        return stocks;
    }

    public void setStocks(Stocks stocks) {
        this.stocks = stocks;
    }

    public List<Transaction> getStockTransactions(String symbol) {
        return transactions.get(symbol);
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
