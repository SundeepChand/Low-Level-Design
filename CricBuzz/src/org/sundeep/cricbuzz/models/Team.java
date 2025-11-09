package org.sundeep.cricbuzz.models;

import java.util.List;
import java.util.Queue;

public class Team {
    private String name;
    Queue<Player> player;
    List<Player> battingOrder;
    Player captain;

    public Team(String name, Queue<Player> player, List<Player> battingOrder, Player captain) {
        this.name = name;
        this.player = player;
        this.battingOrder = battingOrder;
        this.captain = captain;
    }

    public String getName() {
        return name;
    }

    public Queue<Player> getPlayer() {
        return player;
    }

    public List<Player> getBattingOrder() {
        return battingOrder;
    }

    public Player getCaptain() {
        return captain;
    }
}
