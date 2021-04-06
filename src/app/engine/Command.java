package app.engine;

import java.util.Date;

public  class Command{
    private DealData dealData;
    private final CommandDirection direction;
    private final CommandType type;

    public Command(String stockSymbol,
                   CommandDirection direction,
                   CommandType type,
                   int stocksAmount,
                   int offerPrice,
                   Date timeStamp) {
        dealData = new DealData(stockSymbol,
                offerPrice,
                stocksAmount,
                timeStamp);
        this.direction = direction;
        this.type = type;
    }

    public DealData getDealData() {
        return dealData;
    }

    public String getStockSymbol() {
        return dealData.getSymbol();
    }

    public CommandDirection getDirection() {
        return direction;
    }

    public CommandType getType() {
        return type;
    }

    public int getStocksAmount() {
        return dealData.getAmount();
    }

    public int getOfferPrice() {
        return dealData.getPrice();
    }

    public Date getTimeStamp() {
        return dealData.getTimeStamp();
    }

    public int getCurrentStockPrice() {
        return dealData.getDealPrice();
    }

    public void commit(int amount) {
        dealData.commit(amount);

    }

    public static int compareBuy(Command first, Command second) {
        return compareSell(second, first);
    }

    public static int compareSell(Command first, Command second) {
        int result = Integer.compare(first.getOfferPrice(), second.getOfferPrice());
        if(result == 0){
            result = second.dealData.getTimeStamp().compareTo(first.dealData.getTimeStamp());;
        }

        return result;
    }

    public boolean canCommitPurchase(Command toCompare){
        boolean canCommitPurchase  = getStocksAmount() > 0 && toCompare.getStocksAmount() > 0;
        canCommitPurchase =  canCommitPurchase && getStockSymbol().equals(toCompare.getStockSymbol());
        if(direction == CommandDirection.Buy && toCompare.getDirection() == CommandDirection.Sell) {
            canCommitPurchase = canCommitPurchase && getOfferPrice() >= toCompare.getOfferPrice();
        }
        else {
            canCommitPurchase = canCommitPurchase && getOfferPrice() <= toCompare.getOfferPrice();
        }
        return canCommitPurchase;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Command command = (Command) o;

        if (dealData != null ? !dealData.equals(command.dealData) : command.dealData != null) return false;
        if (direction != command.direction) return false;
        return type == command.type;
    }

    @Override
    public int hashCode() {
        int result = dealData != null ? dealData.hashCode() : 0;
        result = 31 * result + (direction != null ? direction.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Command{" +
                dealData +
                ", direction=" + direction +
                ", type=" + type +
                '}';
    }
}
