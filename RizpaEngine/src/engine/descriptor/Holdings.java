package engine.descriptor;

import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

public class Holdings extends ArrayList<Item> {
    private final List<Runnable> onChangeListeners = new ArrayList<>();

    public int getStockAmount(String stockSymbol) {
        int amount = 0;
        for (Item item : this) {
            if(item.getSymbol().equalsIgnoreCase(stockSymbol))
            {
                amount = item.getQuantity();
                break;
            }
        }

        return amount;
    }

    private void invokeAllOnChangeListeners() {
        for (Runnable onChangeListener : onChangeListeners) {
            onChangeListener.run();
        }
    }

    public void addItem(Item item) {
        Item existsItem = null;
        for (Item itemToLookFor : this) {
            if(itemToLookFor.getSymbol().equalsIgnoreCase(item.getSymbol()))
            {
                existsItem = itemToLookFor;
                break;
            }
        }

        if(existsItem != null)
        {
            existsItem.addQuantity(item.getQuantity());
        }
        else
        {
            super.add(item);
        }

        invokeAllOnChangeListeners();
    }

    public void removeItem(Item item) {
        Item existsItem = null;
        for (Item itemToLookFor : this) {
            if(itemToLookFor.getSymbol().equalsIgnoreCase(item.getSymbol()))
            {
                existsItem = itemToLookFor;
                break;
            }
        }

        if(existsItem != null)
        {
            existsItem.setQuantity(existsItem.getQuantity() -  item.getQuantity());
            if(existsItem.getQuantity() == 0)
            {
                super.remove(existsItem);
            }

            invokeAllOnChangeListeners();
        }
    }

    public void addOnChangeListener(Runnable runnable) {
        onChangeListeners.add(runnable);
    }


}
