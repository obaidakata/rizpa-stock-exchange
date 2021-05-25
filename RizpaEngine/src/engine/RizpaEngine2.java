package engine;

import engine.command.Command;
import engine.descriptor.StockExchangeDescriptor;
import engine.descriptor.User;
import engine.descriptor.Users;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class RizpaEngine2 extends RizpaEngine{
    private StockExchangeDescriptor descriptor;
    @Override
    public void loadNewData(StockExchangeDescriptor descriptor) {
        this.descriptor = descriptor;
        super.loadNewData(descriptor);
    }

    public Collection<Command> getSellCommands(String symbol)
    {
        return descriptor != null ? descriptor.getSellOffers(symbol) : new ArrayList<>();
    }

    public Collection<Command> getBuyCommands(String symbol)
    {
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
//        Users users = descriptor.getUsers();
//        for (User user: users){
//
//            Holdings userHoldings = user.getHoldings();
//        }
        return isStocksExists;
    }

    private boolean areStringsUnique(List<String> strings) {
        return strings != null && strings.stream().distinct().count() == strings.size();
    }
}
