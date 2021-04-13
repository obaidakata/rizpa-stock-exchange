package rizpa;

import engine.descriptor.Stock;
import engine.descriptor.StockExchangeDescriptor;
import engine.descriptor.Stocks;
import rizpa.generated.RizpaStockExchangeDescriptor;
import rizpa.generated.RseStock;
import rizpa.generated.RseStocks;

import java.util.ArrayList;
import java.util.List;

public class RizpaDataConverter {

    public StockExchangeDescriptor convert(RizpaStockExchangeDescriptor descriptor) {
        List<Stock> stockList = new ArrayList<>();
        if (descriptor != null) {
            RseStocks rseStocks = descriptor.getRseStocks();
            if (rseStocks != null) {
                List<RseStock> rseStocksList = rseStocks.getRseStock();
                if (rseStocksList != null) {
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
