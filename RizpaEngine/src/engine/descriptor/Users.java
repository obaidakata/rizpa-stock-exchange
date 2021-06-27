package engine.descriptor;

import java.util.ArrayList;

public class Users extends ArrayList<User> {

    public User getUserByName(String username) {
        return stream()
                .filter(user -> user.getName().equalsIgnoreCase(username))
                .findFirst()
                .orElse(null);
    }
}
