package org.sundeep.cricbuzz.models.matchtype;

public class OdiMatchType implements MatchType {
    @Override
    public int totalNumberOfOvers() {
        return 50;
    }

    @Override
    public int maxAllowedOversPerBowler() {
        return 10;
    }
}
