package app.rizpa.engine;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DealData {
    private static long idGenerator = 0;
    private final long id;
    private final int currentStockPrice;
    private final String symbol;
    private final int price;
    private int amount;
    private final Date timeStamp;
    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss:SSS");

    public DealData(String symbol, int price, int amount, Date timeStamp) {
        this.id = idGenerator++;
        this.symbol = symbol;
        this.price = price;
        this.amount = amount;
        this.timeStamp = timeStamp;
        this.currentStockPrice = this.amount * this.price;
    }

    public int getCurrentStockPrice() {
        return currentStockPrice;
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
        if (currentStockPrice != dealData.currentStockPrice) return false;
        if (price != dealData.price) return false;
        if (amount != dealData.amount) return false;
        if (symbol != null ? !symbol.equals(dealData.symbol) : dealData.symbol != null) return false;
        if (timeStamp != null ? !timeStamp.equals(dealData.timeStamp) : dealData.timeStamp != null) return false;
        return simpleDateFormat != null ? simpleDateFormat.equals(dealData.simpleDateFormat) : dealData.simpleDateFormat == null;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + currentStockPrice;
        result = 31 * result + (symbol != null ? symbol.hashCode() : 0);
        result = 31 * result + price;
        result = 31 * result + amount;
        result = 31 * result + (timeStamp != null ? timeStamp.hashCode() : 0);
        result = 31 * result + simpleDateFormat.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "StockData{" +
                ", currentStockPrice=" + currentStockPrice +
                ", symbol='" + symbol + '\'' +
                ", price=" + price +
                ", amount=" + amount +
                ", timeStamp=" + timeStamp +
                '}';
    }

    public void commit(int amount) {
        if(this.amount >= amount) {
            this.amount -= amount;
        }
    }
}
