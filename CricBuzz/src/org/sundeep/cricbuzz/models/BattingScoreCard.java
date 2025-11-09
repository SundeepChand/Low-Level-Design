package org.sundeep.cricbuzz.models;

public class BattingScoreCard {
    private int totalRun;
    private int totalBallsPlayed;
    private int totalFours;
    private int totalSixes;
    private float strikeRate;

    public BattingScoreCard(int totalRun, int totalBallsPlayed, int totalFours, int totalSixes, float strikeRate) {
        this.totalRun = totalRun;
        this.totalBallsPlayed = totalBallsPlayed;
        this.totalFours = totalFours;
        this.totalSixes = totalSixes;
        this.strikeRate = strikeRate;
    }

    public int getTotalRun() {
        return totalRun;
    }

    public int getTotalBallsPlayed() {
        return totalBallsPlayed;
    }

    public int getTotalFours() {
        return totalFours;
    }

    public int getTotalSixes() {
        return totalSixes;
    }

    public float getStrikeRate() {
        return strikeRate;
    }

    public void updateNewBallEvent(BallEvent ballEvent) {
        totalRun += ballEvent.getRunScored();
        totalBallsPlayed++;
        if (ballEvent.getRunScored() == 4) {
            totalFours++;
        }
        if (ballEvent.getRunScored() == 6) {
            totalSixes++;
        }
        strikeRate = ((float) totalRun * 100) / ((float) totalBallsPlayed);
    }

    @Override
    public String toString() {
        return "BattingScoreCard{" +
                "totalRun=" + totalRun +
                ", totalBallsPlayed=" + totalBallsPlayed +
                ", totalFours=" + totalFours +
                ", totalSixes=" + totalSixes +
                ", strikeRate=" + strikeRate +
                '}';
    }
}
