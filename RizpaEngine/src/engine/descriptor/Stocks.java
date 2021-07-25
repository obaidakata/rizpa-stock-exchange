package engine.descriptor;

import java.util.ArrayList;
import java.util.List;

public class Stocks {
    private List<Stock> stocks;

    public Stocks(List<Stock> stocks) {
        this.setStocks(stocks);
    }

    public List<Stock> getStocks() {
        return stocks;
    }

    public void setStocks(List<Stock> stocks) {
        this.stocks = stocks != null ? stocks : new ArrayList<>();
    }

    public void addAll(List<Stock> stocks) {
        this.stocks.addAll(stocks);
    }

    public void addAll(Stocks stocks) {
        if(stocks != null) {
            if(stocks.stocks != null) {
                for (Stock stockAdd : stocks.stocks) {
                    if (!this.stocks.contains(stockAdd)) {
                        this.stocks.add(stockAdd);
                    }
                }
            }
            else {
                setStocks(null);
            }
        }
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

    public void add(Stock stock) throws Exception {
        if(stocks.contains(stock)) {
            throw new Exception("Stock already exists");
        }
        stocks.add(stock);
    }
}
