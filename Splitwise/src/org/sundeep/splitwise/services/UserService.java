package org.sundeep.splitwise.services;

import org.sundeep.splitwise.models.User;

import java.util.ArrayList;
import java.util.List;

public class UserService {
    private final List<User> users;

    public UserService() {
        users = new ArrayList<>();
    }

    public void addUser(User user) {
        users.add(user);
    }

    public User getById(String userId) {
        for (User u : users) {
            if (u.getId().equals(userId)) {
                return u;
            }
        }
        return null;
    }
}
