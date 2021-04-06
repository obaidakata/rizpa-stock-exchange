package app.rizpa.engine;

import app.rizpa.console.app.generated.RizpaStockExchangeDescriptor;
import app.rizpa.console.app.generated.RseStock;

import java.util.*;
import java.util.stream.Collectors;

public class RizpaEngine {
    private StockExchangeDescriptor descriptor;

    public void loadData(StockExchangeDescriptor descriptor) {
        this.descriptor = descriptor;
    }

    public List<Stock> getAllStocks() {
        List<Stock> stocks = null;
        if (descriptor != null) {
            Stocks stocks1 = descriptor.getStocks();
            if(stocks1 != null) {
                stocks = descriptor.getStocks().getStocks();
            }
        }
        return stocks;
    }

    public Collection<Transaction> getStockTransactions(String symbol) {
        return descriptor.getStockTransactions(symbol);
    }

    public void doLimitCommand(CommandDirection direction, String symbol, int amount, int limit) {
        String stockSymbol = getSymbol(symbol);
        Command newCommand = new Command(stockSymbol, direction, CommandType.LMT, amount, limit, new Date());

        if(direction == CommandDirection.Buy) {
            descriptor.addBuyOffer(newCommand);
            matchCommand(newCommand, descriptor.getSellOffers(stockSymbol));
        }
        else if(direction == CommandDirection.Sell) {
            descriptor.addSellOffer(newCommand);
            matchCommand(newCommand, descriptor.getBuyOffers(stockSymbol));
        }
    }

    private void matchCommand(Command commandToMatch, Collection<Command> commands){
        for (Command command : commands) {
            if(commandToMatch.canCommitPurchase(command)) {
                int transactionStockAmount = Math.min(command.getStocksAmount(), commandToMatch.getStocksAmount());
                command.commit(transactionStockAmount);
                commandToMatch.commit(transactionStockAmount);
                Transaction deal = new Transaction(command.getStockSymbol(), command.getOfferPrice(), transactionStockAmount, new Date());
                descriptor.committedTransaction(deal);
            }
            else if(commandToMatch.getStocksAmount() == 0) {
                break;
            }
        }
    }

    private String getSymbol(String stockSymbol) {
        return descriptor
                .getStocks()
                .getStocks()
                .stream()
                .map(Stock::getSymbol)
                .filter(symbol -> symbol.equalsIgnoreCase(stockSymbol))
                .findFirst()
                .orElse(null);
    }

    public boolean isAllStocksSymbolUnique(RizpaStockExchangeDescriptor rizpaStockExchangeDescriptor) {
        boolean isSymbolsUniqe = true;
        try {
            List<String> symbols = rizpaStockExchangeDescriptor
                    .getRseStocks()
                    .getRseStock()
                    .stream()
                    .map(RseStock::getRseSymbol)
                    .collect(Collectors.toList());

            isSymbolsUniqe = areStringsUnique(symbols);
        } catch (NullPointerException ignored) { }

        return isSymbolsUniqe;
    }

    public boolean isAllCompaniesNamesUnique(RizpaStockExchangeDescriptor rizpaStockExchangeDescriptor) {
        boolean isNamesUniqe = true;
        try {
            List<String> companiesNames = rizpaStockExchangeDescriptor
                    .getRseStocks()
                    .getRseStock()
                    .stream()
                    .map(RseStock::getRseCompanyName)
                    .collect(Collectors.toList());

            isNamesUniqe = areStringsUnique(companiesNames);
        } catch (NullPointerException ignored) { }

        return isNamesUniqe;
    }

    private boolean areStringsUnique(List<String> strings) {
        return strings != null && strings.stream().distinct().count() == strings.size();
    }

    public Collection<Command> getBuyOffers(String stockSymbol) {
        return descriptor.getBuyOffers(stockSymbol);
    }

    public Collection<Command> getSellOffers(String stockSymbol) {
        return descriptor.getSellOffers(stockSymbol);
    }
}
