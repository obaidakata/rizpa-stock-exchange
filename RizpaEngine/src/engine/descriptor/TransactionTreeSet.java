package engine.descriptor;

import engine.Transaction;
import engine.command.Command;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.TreeSet;

@XmlRootElement(name = "TransactionTreeSet")
@XmlAccessorType(XmlAccessType.FIELD)
public class TransactionTreeSet {

    @XmlElement(name = "transactions")
    private TreeSet<Transaction> transactions;

    private TransactionTreeSet() {
        this.transactions = new TreeSet<>();
    }

    public TransactionTreeSet(TreeSet<Transaction> transactions) {
        this.transactions = transactions;
    }

    public TreeSet<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(TreeSet<Transaction> transactions) {
        this.transactions = transactions;
    }
}
