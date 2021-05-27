package engine.descriptor;

public class Item {
    private String symbol;
    private int quantity;

    public Item(String symbol, int quantity) {
        this.symbol = symbol;
        this.quantity = quantity;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void commit(int amount) {
        if(quantity >= amount)
        {
            quantity -= amount;
        }
    }

    public void addQuantity(int quantityToAdd) {
        quantity += quantityToAdd;
    }
}
