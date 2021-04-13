package engine.descriptor;

import engine.Transaction;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.HashMap;
import java.util.TreeSet;

@XmlRootElement(name = "Transactions")
@XmlAccessorType(XmlAccessType.FIELD)
public class Transactions {

    @XmlElement(name = "transactions")
    private HashMap<String, TransactionTreeSet> transactions;


    public Transactions() {
        this.transactions = new HashMap<>();
    }

    public void put(String symbol, TreeSet<Transaction> transactionsToAdd) {
        transactions.put(symbol, new TransactionTreeSet(transactionsToAdd));
    }

    public TreeSet<Transaction> get(String symbol) {
        return transactions.get(symbol).getTransactions();
    }

}
