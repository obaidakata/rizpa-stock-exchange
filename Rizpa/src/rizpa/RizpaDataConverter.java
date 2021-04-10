package rizpa;

import app.generated.RizpaStockExchangeDescriptor;
import app.generated.RseStock;
import app.generated.RseStocks;
import engine.Stock;
import engine.StockExchangeDescriptor;
import engine.Stocks;

import java.util.ArrayList;
import java.util.List;

public class RizpaDataConverter {

    public StockExchangeDescriptor convert(RizpaStockExchangeDescriptor descriptor) {
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

        Stocks stocks = new Stocks(stockList);

        return new StockExchangeDescriptor(stocks);
    }
}
