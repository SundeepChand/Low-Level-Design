package org.sundeep.cricbuzz.models;

public class BowlingScoreCard {
    private int ballsDelivered;
    private int runsGiven;
    private int wicketsTaken;
    private int noBallCount;
    private int wideCount;

    public BowlingScoreCard(int ballsDelivered, int runsGiven, int wicketsTaken, int noBallCount, int wideCount) {
        this.ballsDelivered = ballsDelivered;
        this.runsGiven = runsGiven;
        this.wicketsTaken = wicketsTaken;
        this.noBallCount = noBallCount;
        this.wideCount = wideCount;
    }

    public int getBallsDelivered() {
        return ballsDelivered;
    }

    public int getRunsGiven() {
        return runsGiven;
    }

    public int getWicketsTaken() {
        return wicketsTaken;
    }

    public int getNoBallCount() {
        return noBallCount;
    }

    public int getWideCount() {
        return wideCount;
    }

    public void updateNewBallEvent(BallEvent ballEvent) {
        this.ballsDelivered++;
        switch (ballEvent.getBallType()) {
            case BallType.NORMAL:
                this.runsGiven += ballEvent.getRunScored();
                break;
            case BallType.NO_BALL:
                this.noBallCount++;
                break;
            case BallType.WIDE_BALL:
                this.wideCount++;
                break;
            case BallType.WICKET:
                this.wicketsTaken++;
        }
    }

    @Override
    public String toString() {
        return "BowlingScoreCard{" +
                "ballsDelivered=" + ballsDelivered +
                ", runsGiven=" + runsGiven +
                ", wicketsTaken=" + wicketsTaken +
                ", noBallCount=" + noBallCount +
                ", wideCount=" + wideCount +
                '}';
    }
}
