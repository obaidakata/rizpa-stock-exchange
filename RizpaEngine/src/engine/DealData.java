package engine;


import java.io.Serializable;
import java.util.Date;

public class DealData implements Comparable<DealData>, Serializable {
    private String symbol;
    private Integer price;
    private int amount;
    private TimeStamp timeStamp;


    public DealData(String symbol, int  price, int amount, Date timeStamp) {
        this.symbol = symbol;
        this.price = price;
        this.amount = amount;
        this.timeStamp = new TimeStamp(timeStamp);
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
        return timeStamp.getValue();
    }

    public String getTimeStampValue() {
        return timeStamp.toString();
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
        return o.getTimeStamp().compareTo(timeStamp.getValue());
    }
}
