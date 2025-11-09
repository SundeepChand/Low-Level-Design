package org.sundeep.cricbuzz.models;

public class Player {
    private Person person;
    private PlayerType playerType;
    private BattingScoreCard battingScoreCard;
    private BowlingScoreCard bowlingScoreCard;

    public Player(Person person, PlayerType playerType, BattingScoreCard battingScoreCard, BowlingScoreCard bowlingScoreCard) {
        this.person = person;
        this.playerType = playerType;
        this.battingScoreCard = battingScoreCard;
        this.bowlingScoreCard = bowlingScoreCard;
    }

    public Person getPerson() {
        return person;
    }

    public PlayerType getPlayerType() {
        return playerType;
    }

    public BattingScoreCard getBattingScoreCard() {
        return battingScoreCard;
    }

    public BowlingScoreCard getBowlingScoreCard() {
        return bowlingScoreCard;
    }
}
