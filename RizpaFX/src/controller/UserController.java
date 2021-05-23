package controller;

import engine.descriptor.User;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class UserController {

    @FXML private Label userTotalStocks;
    @FXML private Label username;

    private User user;

    public void setUser(User user) {
        this.user = user;
        username.setText(user.getName());
        userTotalStocks.setText("0000");
    }
}
