package app.rizpa.engine;

import java.util.List;

public class RizpaEngine {
    private StockExchangeDescriptor descriptor;

    public void loadData(StockExchangeDescriptor descriptor) {
        this.descriptor = descriptor;
    }

    public List<Stock> getAllStocks() {
        List<Stock> stocks = null;
        if (descriptor != null && descriptor.getStocks() != null) {
            stocks = descriptor.getStocks().getStocks();
        }
        return stocks;
    }

    public List<Transaction> getStockTransactions(String symbol) {
        return descriptor.getStockTransactions(symbol);
    }
}
