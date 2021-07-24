package engine;


import java.io.Serializable;
import java.util.Date;

public class DealData implements Comparable<DealData>, Serializable {
    private final String symbol;
    private Integer price;
    private int amount;
    private final TimeStamp timeStampValue;
    private final String timeStamp;


    public DealData(String symbol, int  price, int amount, Date timeStamp) {
        this.symbol = symbol;
        this.price = price;
        this.amount = amount;
        this.timeStampValue = new TimeStamp(timeStamp);
        this.timeStamp = this.timeStampValue.toString();
    }

    public int getDealPrice() {
        return amount * price;
    }

    public String getSymbol() {
        return symbol;
    }

    public Integer getPrice() {
        return price;
    }

    public int getAmount() {
        return amount;
    }

    public Date getTimeStamp() {
        return timeStampValue.getValue();
    }

    public String getTimeStampValue() {
        return timeStampValue.toString();
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void commit(int amount) {
        if (this.amount >= amount) {
            this.amount -= amount;
        }
    }

    @Override
    public int compareTo(DealData o) {
        return o.getTimeStamp().compareTo(timeStampValue.getValue());
    }
}
