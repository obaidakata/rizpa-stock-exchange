package app.rizpa.engine;

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

        return dealData != null ? dealData.equals(that.dealData) : that.dealData == null;
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
        return dealData.getTimeStamp().compareTo(o.dealData.getTimeStamp());
    }

    public String getSymbol() {
        return dealData.getSymbol();
    }
}
