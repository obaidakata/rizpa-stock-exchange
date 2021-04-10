package engine;

import java.util.List;

public class Stocks{
    private List<Stock> stocks;

    public Stocks(List<Stock> stocks) {
        this.stocks = stocks;
    }

    public List<Stock> getStocks() {
        return stocks;
    }

    public void setStocks(List<Stock> stocks) {
        this.stocks = stocks;
    }

    @Override
    public String toString() {
        return "Stocks{" +
                "stocks=" + stocks +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Stocks stocks1 = (Stocks) o;

        return stocks != null ? stocks.equals(stocks1.stocks) : stocks1.stocks == null;
    }

    @Override
    public int hashCode() {
        return stocks != null ? stocks.hashCode() : 0;
    }
}
