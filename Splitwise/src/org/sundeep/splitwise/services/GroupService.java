package org.sundeep.splitwise.services;

import org.sundeep.splitwise.models.Group;
import org.sundeep.splitwise.models.User;

import java.util.HashMap;
import java.util.Map;

public class GroupService {
    private final UserService userService;

    private final Map<String, Group> groups;

    public GroupService(UserService userService) {
        groups = new HashMap<>();
        this.userService = userService;
    }

    public void createGroup(Group group) {
        groups.put(group.getId(), group);
    }

    public void addUserToGroup(String groupId, User user) throws IllegalArgumentException {
        if (groups.get(groupId) == null) {
            throw new IllegalArgumentException("group with groupId: " + groupId + " does not exist");
        }
        groups.get(groupId).addUser(user);
    }

    public Group getById(String groupId) {
        return groups.get(groupId);
    }
}
