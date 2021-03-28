package app.rizpa;

import app.rizpa.console.app.generated.RizpaStockExchangeDescriptor;
import app.rizpa.console.app.generated.RseStock;
import app.rizpa.engine.RizpaEngine;
import app.rizpa.engine.Stock;
import app.rizpa.engine.StockExchangeDescriptor;
import app.rizpa.engine.Transaction;

import java.util.List;
import java.util.stream.Collectors;

public class RizpaFacade {
    private final String SYMBOLS_ARE_NOT_UNIQUE = "Symbols are not unique";
    private final String COMPANIES_NAMES_ARE_NOT_UNIQUE = "Companies names are not unique";
    private final RizpaDataConverter converter;
    private final RizpaEngine rizpaEngine;

    public RizpaFacade() {
        this.converter = new RizpaDataConverter();
        this.rizpaEngine = new RizpaEngine();
    }

    public List<Transaction> getTransactions(String symbol) {
        return rizpaEngine.getStockTransactions(symbol);
    }

    public List<Stock> getAllStocks() {
        return rizpaEngine.getAllStocks();
    }

    public void loadData(RizpaStockExchangeDescriptor rizpaStockExchangeDescriptor) throws Exception {
        checkData(rizpaStockExchangeDescriptor);
        StockExchangeDescriptor descriptor = converter.convert(rizpaStockExchangeDescriptor);
        rizpaEngine.loadData(descriptor);
    }

    private void checkData(RizpaStockExchangeDescriptor rizpaStockExchangeDescriptor) throws Exception {
        if(!isAllStocksSymbolUnique(rizpaStockExchangeDescriptor)){
            throw new Exception(SYMBOLS_ARE_NOT_UNIQUE);
        }
        else if(!isAllCompaniesNamesUnique(rizpaStockExchangeDescriptor)) {
            throw new Exception(COMPANIES_NAMES_ARE_NOT_UNIQUE);
        }
    }

    // TODO :Code replications
    private boolean isAllStocksSymbolUnique(RizpaStockExchangeDescriptor rizpaStockExchangeDescriptor) {
        boolean isAllUnique = true;
        try {
            List<String> symbols = rizpaStockExchangeDescriptor
                    .getRseStocks()
                    .getRseStock()
                    .stream()
                    .map(RseStock::getRseSymbol)
                    .collect(Collectors.toList());

            if(symbols.stream().distinct().count() < symbols.size()) {
                isAllUnique = false;
            }
        } catch (NullPointerException ignored) { }

        return isAllUnique;
    }

    private boolean isAllCompaniesNamesUnique(RizpaStockExchangeDescriptor rizpaStockExchangeDescriptor) {
        boolean isAllUnique = true;
        try {
            List<String> companiesNames = rizpaStockExchangeDescriptor
                    .getRseStocks()
                    .getRseStock()
                    .stream()
                    .map(RseStock::getRseCompanyName)
                    .collect(Collectors.toList());

            if(companiesNames.stream().distinct().count() < companiesNames.size()) {
                isAllUnique = false;
            }
        } catch (NullPointerException ignored) { }

        return isAllUnique;
    }
}
