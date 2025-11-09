package org.sundeep.cricbuzz.models;

public class BallEvent {
    private int ballNo;
    private int runScored;
    private BallType ballType;
    private Player playedBy;
    private Player bowledBy;

    public BallEvent(int ballNo, int runScored, BallType ballType, Player playedBy, Player bowledBy) {
        this.ballNo = ballNo;
        this.runScored = runScored;
        this.ballType = ballType;
        this.playedBy = playedBy;
        this.bowledBy = bowledBy;
    }

    public int getBallNo() {
        return ballNo;
    }

    public int getRunScored() {
        return runScored;
    }

    public BallType getBallType() {
        return ballType;
    }

    public Player getPlayedBy() {
        return playedBy;
    }

    public Player getBowledBy() {
        return bowledBy;
    }
}
