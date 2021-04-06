package app.rizpa;

import app.console.app.generated.RizpaStockExchangeDescriptor;
import app.console.app.generated.RseStock;
import app.console.app.generated.RseStocks;
import app.engine.Stock;
import app.engine.StockExchangeDescriptor;
import app.engine.Stocks;

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
