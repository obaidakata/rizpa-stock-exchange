package engine;

import engine.descriptor.User;
import engine.descriptor.Users;

public class UsersManager {

    private final Users users;

    public UsersManager() {
        this.users = new Users();
    }

    public synchronized Users getUsers() {
        return users;
    }

    public boolean isUserExists(String username) {
        boolean isExist = false;
        for (User userToCheck : users) {
            if(userToCheck.getName().equals(username))
            {
                isExist = true;
                break;
            }
        }

        return isExist;
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

    public synchronized void addUser(User user){
        users.add(user);
    }

    public synchronized void removeUser(User user){
        users.remove(user);
    }

    public boolean isUserTrader(String username) {
        User user = getUserByName(username);
        System.out.println(user);
        return user != null && user.isTrader();
    }
}
