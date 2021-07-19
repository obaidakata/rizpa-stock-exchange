package engine.descriptor;

import engine.UserRole;

public class User {
    private String name;
    private final UserRole userRole;
    private Holdings holdings;

    public User(String name, UserRole userRole){
        this.name = name;
        this.userRole = userRole;
        this.holdings = new Holdings();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Holdings getHoldings() {
        return holdings;
    }

    public void addAll(Holdings holdingsToAdd) {
        if(holdingsToAdd == null) {
            return;
        }

        for (Item item : holdingsToAdd) {
            this.holdings.addItem(item);
        }
    }

    public void setHoldings(Holdings holdings) {
        this.holdings = holdings;
    }

    public boolean isTrader() {
        return this.userRole == UserRole.Trader;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", userRole=" + userRole +
                ", holdings=" + holdings +
                '}';
    }
}
