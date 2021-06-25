package rizpa;

import engine.descriptor.*;
import rizpa.generated.*;

import java.util.ArrayList;
import java.util.List;

public class RizpaDataConverter {

    public StocksManager convert(RizpaStockExchangeDescriptor descriptor) {
        List<Stock> stockList = getStocks(descriptor);
        Stocks stocks = new Stocks(stockList);
        return new StocksManager(stocks);
    }

    private List<Stock> getStocks(RizpaStockExchangeDescriptor descriptor) {
        List<Stock> stocksList = new ArrayList<>();
        if (descriptor != null) {
            RseStocks rseStocks = descriptor.getRseStocks();
            if (rseStocks != null) {
                List<RseStock> rseStocksList = rseStocks.getRseStock();
                if (rseStocksList != null) {
                    for (RseStock stock : rseStocksList) {
                        stocksList.add(new Stock(
                                stock.getRseSymbol(),
                                stock.getRseCompanyName(),
                                stock.getRsePrice()));
                    }
                }
            }
        }

        return stocksList;
    }
}
