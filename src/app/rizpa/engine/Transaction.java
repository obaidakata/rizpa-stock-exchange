package app.rizpa.engine;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Transaction {
    private String stockSymbol;
    private TransactionDirection direction;
    private TransactionType type;
    private int stocksAmount;
    private int offerPrice;
    private String timeStamp; //


    public Transaction(String stockSymbol,
                       TransactionDirection direction,
                       TransactionType type,
                       int stocksAmount,
                       int offerPrice,
                       Date date) {
        this.stockSymbol = stockSymbol;
        this.direction = direction;
        this.type = type;
        this.stocksAmount = stocksAmount;
        this.offerPrice = offerPrice;
        this.timeStamp = new SimpleDateFormat("HH:mm:ss:SSS").format(date);
    }

    public String getStockSymbol() {
        return stockSymbol;
    }

    public TransactionDirection getDirection() {
        return direction;
    }

    public TransactionType getType() {
        return type;
    }

    public int getStocksAmount() {
        return stocksAmount;
    }

    public int getOfferPrice() {
        return offerPrice;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Transaction that = (Transaction) o;

        if (stocksAmount != that.stocksAmount) return false;
        if (offerPrice != that.offerPrice) return false;
        if (stockSymbol != null ? !stockSymbol.equals(that.stockSymbol) : that.stockSymbol != null) return false;
        if (direction != that.direction) return false;
        if (type != that.type) return false;
        return timeStamp != null ? timeStamp.equals(that.timeStamp) : that.timeStamp == null;
    }

    @Override
    public int hashCode() {
        int result = stockSymbol != null ? stockSymbol.hashCode() : 0;
        result = 31 * result + (direction != null ? direction.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + stocksAmount;
        result = 31 * result + offerPrice;
        result = 31 * result + (timeStamp != null ? timeStamp.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "stockSymbol='" + stockSymbol + '\'' +
                ", direction=" + direction +
                ", type=" + type +
                ", stocksAmount=" + stocksAmount +
                ", offerPrice=" + offerPrice +
                ", timeStamp='" + timeStamp + '\'' +
                '}';
    }
}
