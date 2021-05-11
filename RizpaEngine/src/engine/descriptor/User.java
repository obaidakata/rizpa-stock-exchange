package engine.descriptor;

public class User {
    private String name;
    private Holdings holdings;

    public User(String name, Holdings holdings) {
        this.name = name;
        this.holdings = holdings;
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

    public void setHoldings(Holdings holdings) {
        this.holdings = holdings;
    }
}
