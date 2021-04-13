package engine.descriptor;

import engine.command.Command;
import engine.Transaction;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.*;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class StockExchangeDescriptor {
    private Stocks stocks;
    private Commands stockSymbol2BuyOffers;
    private Commands stockSymbol2SellOffers;
    private Transactions stockSymbol2transactions;
    private final String SYMBOL_NOT_FOUND_EXCEPTION_MESSAGE = "Symbol not found";

    private StockExchangeDescriptor() {
    }

    public StockExchangeDescriptor(Stocks stocks) {
        this.stocks = stocks;
        this.stockSymbol2BuyOffers = new Commands();
        this.stockSymbol2SellOffers = new Commands();
        this.stockSymbol2transactions = new Transactions();
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
        return stockSymbol2SellOffers.getSellOffers(symbol);
    }

    public Collection<Command> getBuyOffers(String symbol) {
        return stockSymbol2BuyOffers.getBuyOffers(symbol);
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

    public Commands get() {
        return stockSymbol2BuyOffers;
    }
}
