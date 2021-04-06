package app.engine;

import java.util.Date;

public class Transaction implements Comparable<Transaction>{
    private final DealData dealData;


    public Transaction(String symbol,
                       int price,
                       int amount,
                       Date timeStamp) {
        dealData = new DealData(symbol, price, amount, timeStamp);
    }

    public DealData getDealData() {
        return dealData;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Transaction that = (Transaction) o;

        return dealData.equals(that.dealData);
    }

    @Override
    public int hashCode() {
        return dealData.hashCode();
    }

    @Override
    public String toString() {
        return dealData.toString();
    }

    @Override
    public int compareTo(Transaction o) {
        return dealData.compareTo(o.dealData);
    }

    public String getSymbol() {
        return dealData.getSymbol();
    }
}
