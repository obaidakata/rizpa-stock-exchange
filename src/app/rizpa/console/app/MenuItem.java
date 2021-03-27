package app.rizpa.console.app;

import java.util.function.Consumer;

public class MenuItem {
    private final String actionName;
    private final Runnable action;
    private boolean isAvailable = false;

    public MenuItem(String actionName, Runnable action) {
        this.actionName = actionName;
        this.action = action;
    }

    public MenuItem(String actionName, Runnable action, boolean isAvailable) {
        this.actionName = actionName;
        this.action = action;
        this.isAvailable = isAvailable;
    }

    public String getActionName() {
        return actionName;
    }

    public Runnable getAction() {
        return action;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    @Override
    public String toString() {
        return "MenuItem{" +
                "actionName='" + actionName + '\'' +
                ", action=" + action +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MenuItem menuItem = (MenuItem) o;

        if (actionName != null ? !actionName.equals(menuItem.actionName) : menuItem.actionName != null) return false;
        return action != null ? action.equals(menuItem.action) : menuItem.action == null;
    }

    @Override
    public int hashCode() {
        int result = actionName != null ? actionName.hashCode() : 0;
        result = 31 * result + (action != null ? action.hashCode() : 0);
        return result;
    }
}
