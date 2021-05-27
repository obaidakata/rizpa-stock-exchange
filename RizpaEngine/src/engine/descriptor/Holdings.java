package engine.descriptor;

import java.util.ArrayList;

public class Holdings extends ArrayList<Item>{
    public void commit(String symbol, int amount) {
        for (Item item : this) {
            if(item.getSymbol().equalsIgnoreCase(symbol))
            {
                item.commit(amount);
                break;
            }
        }
    }

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
        }

    }
}
