package engine;

import java.util.Date;

public class Transaction implements Comparable<Transaction> {
    private final DealData dealData;

    private final String buyerName;
    private final String sellerName;

    public Transaction(String symbol,
                       int price,
                       int amount,
                       Date timeStamp) {
        dealData = new DealData(symbol, price, amount, timeStamp);
        this.buyerName = "Admin";
        this.sellerName = "Admin";
    }

    public Transaction(String symbol,
                       int price,
                       int amount,
                       Date timeStamp,
                       String buyerName,
                       String sellerName) {
        dealData = new DealData(symbol, price, amount, timeStamp);
        this.sellerName = sellerName;
        this.buyerName = buyerName;
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
        return "Transaction{" +
                "dealData=" + dealData +
                ", buyerName='" + buyerName + '\'' +
                ", sellerName='" + sellerName + '\'' +
                '}';
    }

    @Override
    public int compareTo(Transaction o) {
        return dealData.compareTo(o.dealData);
    }

    public String getSymbol() {
        return dealData.getSymbol();
    }

    public String getBuyerName() {
        return buyerName;
    }

    public String getSellerName() {
        return sellerName;
    }
}
