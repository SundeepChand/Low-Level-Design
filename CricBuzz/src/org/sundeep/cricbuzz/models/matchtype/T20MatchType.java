package org.sundeep.cricbuzz.models.matchtype;

public class T20MatchType implements MatchType {
    @Override
    public int totalNumberOfOvers() {
        return 20;
    }

    @Override
    public int maxAllowedOversPerBowler() {
        return 4;
    }
}
