package rizpa;

import engine.descriptor.*;
import rizpa.generated.*;

import java.util.ArrayList;
import java.util.List;

public class RizpaDataConverter {

    public StockExchangeDescriptor convert(RizpaStockExchangeDescriptor descriptor) {
        List<Stock> stockList = getStocks(descriptor);
        Users users = getUsers(descriptor);

        Stocks stocks = new Stocks(stockList);
        return new StockExchangeDescriptor(stocks, users);
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

    private Users getUsers(RizpaStockExchangeDescriptor descriptor) {
        Users users = new Users();
        if (descriptor != null) {
            RseUsers rseUsers = descriptor.getRseUsers();
            if(rseUsers != null) {
                List<RseUser> temporaryUsers = rseUsers.getRseUser();
                if(temporaryUsers != null) {
                    for(RseUser rseUser : temporaryUsers) {
                        RseHoldings rseHoldings = rseUser.getRseHoldings();
                        Holdings holdings = new Holdings();
                        if(rseHoldings != null) {
                            for (RseItem rseItem : rseHoldings.getRseItem()) {
                                holdings.add(new Item(
                                        rseItem.getSymbol(),
                                        rseItem.getQuantity()));
                            }
                        }

                        users.add(new User(rseUser.getName(), holdings));
                    }
                }
            }
        }

        return users;
    }
}
