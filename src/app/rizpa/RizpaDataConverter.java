package app.rizpa;

import app.rizpa.console.app.generated.RizpaStockExchangeDescriptor;
import app.rizpa.console.app.generated.RseStock;
import app.rizpa.console.app.generated.RseStocks;
import app.rizpa.engine.StockExchangeDescriptor;

import java.util.List;

public class RizpaDataConverter {

    public StockExchangeDescriptor convert(RizpaStockExchangeDescriptor descriptor) {
        StockExchangeDescriptor stockExchangeDescriptor = null;
        if(descriptor != null) {
            RseStocks rseStocks = descriptor.getRseStocks();
            if(rseStocks != null) {
                List<RseStock> stocks =  rseStocks.getRseStock();

            }
        }


        return stockExchangeDescriptor;
    }
}
