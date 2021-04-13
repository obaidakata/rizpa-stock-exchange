package engine.descriptor;

import engine.command.Command;
import engine.command.CommandTressSet;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.HashMap;
import java.util.TreeSet;

@XmlRootElement(name = "Commands")
@XmlAccessorType(XmlAccessType.FIELD)
public class Commands {

    @XmlElement(name = "HashMap")
    private HashMap<String, CommandTressSet> symbol2Commands;


    public Commands() {
        this.symbol2Commands = new HashMap<>();
    }

    public void put(String symbol, TreeSet<Command> commands) {
        symbol2Commands.put(symbol, new CommandTressSet(commands));
    }

    public TreeSet<Command> getSellOffers(String symbol) {
        return symbol2Commands.computeIfAbsent(symbol, v -> new CommandTressSet(new TreeSet<>(Command::compareSell))).getCommands();
    }

    public TreeSet<Command> getBuyOffers(String symbol) {
        return symbol2Commands.computeIfAbsent(symbol, v -> new CommandTressSet(new TreeSet<>(Command::compareBuy))).getCommands();
    }

    public HashMap<String, CommandTressSet> getSymbol2Commands() {
        return symbol2Commands;
    }

    public void setSymbol2Commands(HashMap<String, CommandTressSet> symbol2Commands) {
        this.symbol2Commands = symbol2Commands;
    }
}
