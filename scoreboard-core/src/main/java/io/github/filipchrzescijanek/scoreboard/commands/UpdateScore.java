package io.github.filipchrzescijanek.scoreboard.commands;

public record UpdateScore(String matchId, int homeTeamScore, int awayTeamScore) {}
