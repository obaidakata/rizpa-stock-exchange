package engine;

import engine.descriptor.User;
import engine.descriptor.Users;

public class UsersManager {

    private Users users;

    public UsersManager() {
        this.users = new Users();
    }

    public Users getUsers() {
        return users;
    }

    public User getUserByName(String userName){
        User user = null;
        for (User userToCheck : users) {
            if(userToCheck.getName().equals(userName))
            {
                user = userToCheck;
                break;
            }
        }

        return user;
    }
}
