package engine;

import engine.command.Command;
import engine.command.CommandDirection;
import engine.command.CommandType;
import engine.descriptor.*;

import java.util.*;
import java.util.stream.Collectors;

public class RizpaEngine {
    private StockExchangeDescriptor descriptor;

    public void loadNewData(StockExchangeDescriptor descriptor) {
        this.descriptor = descriptor;
    }

    public List<Stock> getAllStocks() {
        List<Stock> stocks = null;
        if (descriptor != null) {
            Stocks stocks1 = descriptor.getStocks();
            if (stocks1 != null) {
                stocks = descriptor.getStocks().getStocks();
            }
        }

        if (stocks == null) {
            stocks = new ArrayList<>();
        }

        return stocks;
    }

    public Collection<Transaction> getStockTransactions(String symbol) {
        Collection<Transaction> transactions = null;
        if (descriptor != null) {
            transactions = descriptor.getStockTransactions(symbol);
        }

        if (transactions == null) {
            transactions = new ArrayList<>();
        }

        return transactions;
    }


    public List<Transaction> doMKTCommand(String username, CommandDirection direction, String symbol, int amount){
        String stockSymbol = getSymbol(symbol);
        Stock stock = getStockBySymbol(stockSymbol);
        Collection<Command> buyCommands = descriptor.getBuyOffers(stockSymbol);
        Collection<Command> sellCommands = descriptor.getSellOffers(stockSymbol);
        Collection<Command> matchWith;
        Collection<Command> toAddTo;

        if (direction == CommandDirection.Buy) {
            matchWith = sellCommands;
            toAddTo = buyCommands;
        } else {
            matchWith = buyCommands;
            toAddTo = sellCommands;
        }

        List<Transaction> transactionsMade = new ArrayList<>();
        for (Command command : matchWith) {
            if(command.canCommitPurchase(amount, symbol))
            {
                int transactionStockAmount = Math.min(command.getStocksAmount(), amount);
                command.commit(transactionStockAmount);
                amount -= transactionStockAmount;
                int dealPrice = command.getOfferPrice();
                String buyerName = direction == CommandDirection.Buy ? username : command.getUsername();
                String sellerName = direction == CommandDirection.Sell ? username : command.getUsername();
                Transaction deal = new Transaction(symbol, dealPrice, transactionStockAmount, new Date(), buyerName, sellerName, CommandType.MKT);
                stock.commitDeal(dealPrice, transactionStockAmount);
                descriptor.committedTransaction(deal);
                transactionsMade.add(deal);
            }
            else if (amount == 0) {
                break;
            }
        }

        matchWith.removeIf(command -> command.getStocksAmount() == 0);

        if(amount > 0) {
            toAddTo.add(new Command(username, stockSymbol, direction, CommandType.MKT, amount, stock.getPrice()));
        }

        return transactionsMade;
    }

    public List<Transaction> doLMTCommand(String username, CommandDirection direction, String symbol, int amount, int limit) throws Exception {
        String stockSymbol = getSymbol(symbol);
        Command newCommand = new Command(username, stockSymbol, direction, CommandType.LMT, amount, limit);

        Collection<Command> buyCommands = descriptor.getBuyOffers(stockSymbol);
        Collection<Command> sellCommands = descriptor.getSellOffers(stockSymbol);
        Collection<Command> matchWith;
        Collection<Command> toAddTo;

        if (direction == CommandDirection.Buy) {
            matchWith = sellCommands;
            toAddTo = buyCommands;
        } else {
            matchWith = buyCommands;
            toAddTo = sellCommands;
        }

        return matchCommand(newCommand, matchWith, toAddTo);
    }

    private void checkIfUserCanCommitPurchase(Command commandToMatch) throws Exception {
        String symbol = commandToMatch.getStockSymbol();
        if (commandToMatch.getDirection() == CommandDirection.Sell) {
            String username = commandToMatch.getUsername();
            User seller = descriptor.getUserByName(username);

            int actualUserHoldingsAmount = seller.getHoldings().getStockAmount(symbol) - userAmountSellBefore(symbol, username);

            if (actualUserHoldingsAmount < commandToMatch.getStocksAmount()) {
                throw new Exception(String.format("%s can't sell  %d %s stocks because the user have only %s",
                        username,
                        commandToMatch.getStocksAmount(),
                        commandToMatch.getStockSymbol(),
                        actualUserHoldingsAmount
                ));
            }
        }
    }

    private List<Transaction> matchCommand(Command commandToMatch, Collection<Command> commands, Collection<Command> whereToTheNewCommand) throws Exception {
        checkIfUserCanCommitPurchase(commandToMatch);

        List<Transaction> transactionsMade = new ArrayList<>();
        for (Command command : commands) {
            if (commandToMatch.canCommitPurchaseBasedOnPrice(command)) {
                int transactionStockAmount = Math.min(command.getStocksAmount(), commandToMatch.getStocksAmount());
                command.commit(transactionStockAmount);
                commandToMatch.commit(transactionStockAmount);
                int dealPrice = command.getOfferPrice();
                String symbol = commandToMatch.getStockSymbol();
                String buyerName = commandToMatch.getDirection() == CommandDirection.Buy ? commandToMatch.getUsername() : command.getUsername();
                String sellerName = commandToMatch.getDirection() == CommandDirection.Sell ? commandToMatch.getUsername() : command.getUsername();
                Transaction deal = new Transaction(symbol, dealPrice, transactionStockAmount, new Date(), buyerName, sellerName, CommandType.LMT);
                Stock stock = getStockBySymbol(symbol);
                stock.commitDeal(dealPrice, transactionStockAmount);
                descriptor.committedTransaction(deal);
                transactionsMade.add(deal);
            } else if (commandToMatch.getStocksAmount() == 0) {
                break;
            }
        }
        commands.removeIf(command -> command.getStocksAmount() == 0);
        if (commandToMatch.getStocksAmount() > 0) {
            whereToTheNewCommand.add(commandToMatch);
        }

        return transactionsMade;
    }

    private int userAmountSellBefore(String symbol, String username) {
        int sum = 0;
        Collection<Command> userSellAmount = getSellCommands(symbol);

        for (Command command : userSellAmount) {
            if (command.getUsername().equalsIgnoreCase(username)) {
                sum += command.getStocksAmount();
            }
        }

        return sum;
    }

    private boolean userHaveEnoughHoldings(Command command) {
        return true;
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

    public boolean isAllStocksSymbolUnique(StockExchangeDescriptor rizpaStockExchangeDescriptor) {
        boolean isSymbolsUnique = true;
        try {
            List<String> symbols = rizpaStockExchangeDescriptor
                    .getStocks()
                    .getStocks()
                    .stream()
                    .map(Stock::getSymbol)
                    .collect(Collectors.toList());

            isSymbolsUnique = areStringsUnique(symbols);
        } catch (NullPointerException ignored) {
        }

        return isSymbolsUnique;
    }

    public boolean isAllCompaniesNamesUnique(StockExchangeDescriptor rizpaStockExchangeDescriptor) {
        boolean isNamesUnique = true;
        try {
            List<String> companiesNames = rizpaStockExchangeDescriptor
                    .getStocks()
                    .getStocks()
                    .stream()
                    .map(Stock::getCompanyName)
                    .collect(Collectors.toList());

            isNamesUnique = areStringsUnique(companiesNames);
        } catch (NullPointerException ignored) {
        }

        return isNamesUnique;
    }

    private boolean areStringsUnique(List<String> strings) {
        return strings != null && strings.stream().distinct().count() == strings.size();
    }

    public Collection<DealData> getBuyOffers(String stockSymbol) {
        Collection<Command> buyOffers = descriptor.getBuyOffers(stockSymbol);
        Collection<DealData> buyOffersDeals;
        if (buyOffers == null) {
            buyOffersDeals = new ArrayList<>();
        } else {
            buyOffersDeals = buyOffers
                    .stream()
                    .map(Command::getDealData)
                    .collect(Collectors.toList());
        }
        return buyOffersDeals;
    }

    public List<DealData> getSellOffers(String stockSymbol) {
        Collection<Command> sellOffers = descriptor.getSellOffers(stockSymbol);
        List<DealData> sellOffersDeals;
        if (sellOffers == null) {
            sellOffersDeals = new ArrayList<>();
        } else {
            sellOffersDeals = sellOffers
                    .stream()
                    .map(Command::getDealData)
                    .collect(Collectors.toList());
        }
        return sellOffersDeals;
    }

    public Stock getStockBySymbol(String symbol) {
        return getAllStocks()
                .stream()
                .filter(stock -> stock.getSymbol().equalsIgnoreCase(symbol))
                .findFirst()
                .orElse(null);
    }

    public Collection<String> getAllSymbols() {
        return getAllStocks()
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

    public StockExchangeDescriptor getDescriptor() {
        return descriptor;
    }

    public void setDescriptor(StockExchangeDescriptor descriptor) {
        this.descriptor = descriptor;
    }

    public Collection<Command> getSellCommands(String symbol) {
        return descriptor != null ? descriptor.getSellOffers(symbol) : new ArrayList<>();
    }

    public Collection<Command> getBuyCommands(String symbol) {
        return descriptor != null ? descriptor.getBuyOffers(symbol) : new ArrayList<>();
    }

    public Users getAllUsers() {
        return descriptor.getUsers();
    }

    public boolean isAllUsersNamesUnique(StockExchangeDescriptor descriptor) {
        boolean isNamesUnique = true;
        try {
            List<String> usersNames = descriptor
                    .getUsers()
                    .stream()
                    .map(User::getName)
                    .collect(Collectors.toList());

            isNamesUnique = areStringsUnique(usersNames);
        } catch (NullPointerException ignored) {
        }

        return isNamesUnique;
    }

    public boolean isAllUserStockExists(StockExchangeDescriptor descriptor) {
        boolean isStocksExists = true;
        HashSet<String> stocks = descriptor.getStocks().getStocks().stream().map(Stock::getSymbol).collect(Collectors.toCollection(HashSet::new));
        Users users = descriptor.getUsers();
        for (User user : users) {
            Holdings userHoldings = user.getHoldings();
            for (Item item : userHoldings) {
                String stockSymbol = item.getSymbol();
                if (!stocks.contains(stockSymbol)) {
                    isStocksExists = false;
                    break;
                }
            }
        }

        return isStocksExists;
    }
}
