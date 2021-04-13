package engine.descriptor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Stock {

    @XmlElement
    private String symbol;
    @XmlElement
    private String companyName;
    @XmlElement
    private int price;
    @XmlElement
    private long sumOfAllTransactions = 0;
    @XmlElement
    private static int longestSymbolLength = 0;
    @XmlElement
    private static int longestCompanyLength = 0;

    private Stock() {
    }

    public Stock(String symbol, String companyName, int price) {
        this.symbol = symbol;
        this.companyName = companyName;
        this.price = price;
        longestSymbolLength = Math.max(longestSymbolLength, symbol.length());
        longestCompanyLength = Math.max(longestCompanyLength, companyName.length());
    }

    public String getSymbol() {
        return symbol;
    }

    public String getCompanyName() {
        return companyName;
    }

    public int getPrice() {
        return price;
    }

    public long getSumOfAllTransactions() {
        return sumOfAllTransactions;
    }



    public void commitDeal(int price, int amount) {
        this.price = price;
        sumOfAllTransactions += (long) price * amount;
    }

    @Override
    public String toString() {
        return "Stock{" +
                "symbol='" + symbol + '\'' +
                ", companyName='" + companyName + '\'' +
                ", price=" + price +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Stock stock = (Stock) o;

        if (price != stock.price) return false;
        if (symbol != null ? !symbol.equals(stock.symbol) : stock.symbol != null) return false;
        return companyName != null ? companyName.equals(stock.companyName) : stock.companyName == null;
    }

    @Override
    public int hashCode() {
        int result = symbol != null ? symbol.hashCode() : 0;
        result = 31 * result + (companyName != null ? companyName.hashCode() : 0);
        result = 31 * result + price;
        return result;
    }

    public static int getLongestSymbolLength() {
        return longestSymbolLength;
    }

    public static int getLongestCompanyLength() {
        return longestCompanyLength;
    }


}
