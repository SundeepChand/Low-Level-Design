package org.sundeep.cricbuzz.models;

import org.sundeep.cricbuzz.models.matchtype.MatchType;

import java.time.ZonedDateTime;

public class Match {
    private final Team teamA, teamB;
    private final Innings[] innings;
    private final ZonedDateTime matchTime;
    private final String venue;
    private final MatchType matchType;
    private final Team tossWinner;

    public Match(Team teamA, Team teamB, ZonedDateTime matchTime, String venue, MatchType matchType, Team tossWinner) {
        this.teamA = teamA;
        this.teamB = teamB;
        this.matchTime = matchTime;
        this.venue = venue;
        this.matchType = matchType;
        this.tossWinner = tossWinner;
        this.innings = new Innings[2];
    }

    public Team getTeamA() {
        return teamA;
    }

    public Team getTeamB() {
        return teamB;
    }

    public Innings[] getInnings() {
        return innings;
    }

    public ZonedDateTime getMatchTime() {
        return matchTime;
    }

    public String getVenue() {
        return venue;
    }

    public MatchType getMatchType() {
        return matchType;
    }

    public Team getTossWinner() {
        return tossWinner;
    }
}
