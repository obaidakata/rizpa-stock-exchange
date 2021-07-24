package engine;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TransactionRecord implements Comparable<TransactionRecord>{
    private final TransactionType transactionType;
    private final String timeStamp;
    private final Date timeStampDate;
    private final int price;
    private final int balanceBefore;
    private final int balanceAfter;
    private String symbol;

    public TransactionRecord(int price, int userBalance, TransactionType transactionType, String symbol) {
        this(price, userBalance, transactionType);
        this.symbol = symbol;
    }

    public TransactionRecord(int price, int userBalance, TransactionType transactionType) {
        this.price = price;
        this.timeStampDate = new Date();
        this.timeStamp = new SimpleDateFormat("HH:mm:ss:SSS").format(this.timeStampDate);
        this.balanceBefore = userBalance;
        this.balanceAfter = balanceBefore + price;
        this.transactionType = transactionType;
        this.symbol = "N/A";
    }

    public int getPrice() {
        return price;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public int getBalanceBefore() {
        return balanceBefore;
    }

    public int getBalanceAfter() {
        return balanceAfter;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public Date getTimeStampDate() {
        return timeStampDate;
    }

    public String getSymbol() {
        return symbol;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "price=" + price +
                ", timeStamp=" + timeStamp +
                ", balanceBefore=" + balanceBefore +
                ", balanceAfter=" + balanceAfter +
                ", transactionType=" + transactionType +
                '}';
    }

    @Override
    public int compareTo(TransactionRecord o) {
        return timeStampDate.compareTo(o.timeStampDate);
    }
}
