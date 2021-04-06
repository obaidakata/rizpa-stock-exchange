package app.engine;

import app.console.app.generated.RizpaStockExchangeDescriptor;
import app.console.app.generated.RseStock;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
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
        else{
            stocks = new ArrayList<>();
        }

        return stocks;
    }

    public Collection<Transaction> getStockTransactions(String symbol) {
        return descriptor.getStockTransactions(symbol);
    }

    public void doLimitCommand(CommandDirection direction, String symbol, int amount, int limit) {
        String stockSymbol = getSymbol(symbol);
        Command newCommand = new Command(stockSymbol, direction, CommandType.LMT, amount, limit, new Date());

        Collection<Command> buyCommands = descriptor.getBuyOffers(stockSymbol);
        Collection<Command> sellCommands = descriptor.getSellOffers(stockSymbol);
        Collection<Command> matchWith = null;
        Collection<Command> toAddTo = null;

        if(direction == CommandDirection.Buy) {
            matchWith = sellCommands;
            toAddTo = buyCommands;
        }
        else{
            matchWith = buyCommands;
            toAddTo = sellCommands;
        }

        matchCommand(newCommand, matchWith, toAddTo);
    }

    private void matchCommand(Command commandToMatch, Collection<Command> commands, Collection<Command> whereToTheNewCommand){
        String symbol = commandToMatch.getStockSymbol();
        for (Command command : commands) {
            if(commandToMatch.canCommitPurchase(command)) {
                int transactionStockAmount = Math.min(command.getStocksAmount(), commandToMatch.getStocksAmount());
                command.commit(transactionStockAmount);
                commandToMatch.commit(transactionStockAmount);
                int dealPrice = command.getOfferPrice();
                Transaction deal = new Transaction(symbol, dealPrice, transactionStockAmount, new Date());
                descriptor.committedTransaction(deal);
                Stock stock = getStockBySymbol(symbol);
                stock.commitDeal(dealPrice, transactionStockAmount);
            }
            else if(commandToMatch.getStocksAmount() == 0) {
                break;
            }
        }
        commands.removeIf( command -> command.getStocksAmount() == 0);
        if(commandToMatch.getStocksAmount() > 0){
            whereToTheNewCommand.add(commandToMatch);
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
        boolean isSymbolsUnique = true;
        try {
            List<String> symbols = rizpaStockExchangeDescriptor
                    .getRseStocks()
                    .getRseStock()
                    .stream()
                    .map(RseStock::getRseSymbol)
                    .collect(Collectors.toList());

            isSymbolsUnique = areStringsUnique(symbols);
        } catch (NullPointerException ignored) { }

        return isSymbolsUnique;
    }

    public boolean isAllCompaniesNamesUnique(RizpaStockExchangeDescriptor rizpaStockExchangeDescriptor) {
        boolean isNamesUnique = true;
        try {
            List<String> companiesNames = rizpaStockExchangeDescriptor
                    .getRseStocks()
                    .getRseStock()
                    .stream()
                    .map(RseStock::getRseCompanyName)
                    .collect(Collectors.toList());

            isNamesUnique = areStringsUnique(companiesNames);
        } catch (NullPointerException ignored) { }

        return isNamesUnique;
    }

    private boolean areStringsUnique(List<String> strings) {
        return strings != null && strings.stream().distinct().count() == strings.size();
    }

    public Collection<DealData> getBuyOffers(String stockSymbol) {
        return descriptor
                .getBuyOffers(stockSymbol)
                .stream()
                .map(Command::getDealData)
                .collect(Collectors.toList());
    }

    public List<DealData> getSellOffers(String stockSymbol) {
        return descriptor
                .getSellOffers(stockSymbol)
                .stream()
                .map(Command::getDealData)
                .collect(Collectors.toList());
    }

    public Stock getStockBySymbol(String symbol) {
        return getAllStocks()
                .stream()
                .filter(stock -> stock.getSymbol().equalsIgnoreCase(symbol))
                .findFirst()
                .orElse(null);
    }

    public Collection<String> getAllSymbols() {
        return  getAllStocks()
                .stream()
                .map(Stock::getSymbol)
                .collect(Collectors.toList());
    }

    public int getSumSellOffers(String stockSymbol) {
        return getSellOffers(stockSymbol)
                .stream()
                .map(DealData::getDealPrice)
                .reduce(0, Integer::sum);
    }

    public int getSumBuyOffers(String stockSymbol) {
        return getBuyOffers(stockSymbol)
                .stream()
                .map(DealData::getDealPrice)
                .reduce(0, Integer::sum);
    }

    public int getSumTransactions(String stockSymbol) {
        return getStockTransactions(stockSymbol)
                .stream()
                .map(transaction -> transaction.getDealData().getDealPrice())
                .reduce(0, Integer::sum);
    }
}