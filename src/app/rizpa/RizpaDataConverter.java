package app.rizpa;

import app.rizpa.console.app.generated.RizpaStockExchangeDescriptor;
import app.rizpa.console.app.generated.RseStock;
import app.rizpa.console.app.generated.RseStocks;
import app.rizpa.engine.Stock;
import app.rizpa.engine.StockExchangeDescriptor;
import app.rizpa.engine.Stocks;

import java.util.ArrayList;
import java.util.List;

public class RizpaDataConverter {

    public StockExchangeDescriptor convert(RizpaStockExchangeDescriptor descriptor) {
        StockExchangeDescriptor stockExchangeDescriptor = null;
        Stocks stocks = null;
        List<Stock> stockList = new ArrayList<>();
        if(descriptor != null) {
            RseStocks rseStocks = descriptor.getRseStocks();
            if(rseStocks != null) {
                List<RseStock> rseStocksList =  rseStocks.getRseStock();
                if(rseStocksList != null) {
                    for (RseStock stock : rseStocksList) {
                        stockList.add(new Stock(
                                stock.getRseSymbol(),
                                stock.getRseCompanyName(),
                                stock.getRsePrice()));
                    }
                }
            }
        }

        stocks = new Stocks(stockList);
        stockExchangeDescriptor = new StockExchangeDescriptor(stocks);

        return stockExchangeDescriptor;
    }
}
