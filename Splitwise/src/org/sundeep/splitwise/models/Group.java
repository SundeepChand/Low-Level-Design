package org.sundeep.splitwise.models;

import java.util.List;
import java.util.UUID;

public class Group {
    private String id;
    private String name;
    private List<User> users;

    public Group(String name, List<User> users) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.users = users;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<User> getUsers() {
        return users;
    }

    public void addUser(User user) {
        users.add(user);
    }
}
