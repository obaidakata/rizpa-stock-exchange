import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

public class Test {

    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        ObservableList<String> strings = FXCollections.observableArrayList(list);

        strings.addListener((ListChangeListener<String>) change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    System.out.println(change.getAddedSubList().get(0)
                            + " was added to the list!");
                } else if (change.wasRemoved()) {
                    System.out.println(change.getRemoved().get(0)
                            + " was removed from the list!");
                }
            }
        });

        list.add("One");
        list.add("Two");
        list.add("Three");




        list.add("Dogs");
        list.remove("Two");
    }
}