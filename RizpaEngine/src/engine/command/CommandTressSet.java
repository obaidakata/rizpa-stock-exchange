package engine.command;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.TreeSet;

@XmlRootElement(name = "CommandTressSet")
@XmlAccessorType(XmlAccessType.FIELD)
public class CommandTressSet {

    @XmlElement(name = "commands")
    private TreeSet<Command> commands;

    private CommandTressSet() {
    }

    public CommandTressSet(TreeSet<Command> commands) {
        this.commands = commands;
    }

    public TreeSet<Command> getCommands() {
        return commands;
    }

    public void setCommands(TreeSet<Command> commands) {
        this.commands = commands;
    }
}
