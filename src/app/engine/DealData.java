package app.engine;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DealData implements Comparable<DealData>{
    private static long idGenerator = 0;
    private final long id;
    private int dealPrice;
    private final String symbol;
    private final int price;
    private int amount;
    private final Date timeStamp;
    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss:SSS");

    public DealData(String symbol, int price, int amount, Date timeStamp) {
        this.id = idGenerator++;
        this.symbol = symbol;
        this.price = price;
        this.amount = amount;
        this.timeStamp = timeStamp;
        this.dealPrice = this.amount * this.price;
    }

    public int getDealPrice() {
        return dealPrice;
    }

    public String getSymbol() {
        return symbol;
    }

    public int getPrice() {
        return price;
    }

    public int getAmount() {
        return amount;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DealData dealData = (DealData) o;

        if (id != dealData.id) return false;
        if (dealPrice != dealData.dealPrice) return false;
        if (price != dealData.price) return false;
        if (amount != dealData.amount) return false;
        if (symbol != null ? !symbol.equals(dealData.symbol) : dealData.symbol != null) return false;
        return timeStamp != null ? timeStamp.equals(dealData.timeStamp) : dealData.timeStamp == null;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + dealPrice;
        result = 31 * result + (symbol != null ? symbol.hashCode() : 0);
        result = 31 * result + price;
        result = 31 * result + amount;
        result = 31 * result + (timeStamp != null ? timeStamp.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "StockData{" +
                ", currentStockPrice=" + dealPrice +
                ", symbol='" + symbol + '\'' +
                ", price=" + price +
                ", amount=" + amount +
                ", timeStamp=" + timeStamp +
                '}';
    }

    public void commit(int amount) {
        if(this.amount >= amount) {
            this.amount -= amount;
            dealPrice = this.amount * this.price;
        }
    }

    @Override
    public int compareTo(DealData o) {
        return Long.compare(o.id, id);
    }
}
