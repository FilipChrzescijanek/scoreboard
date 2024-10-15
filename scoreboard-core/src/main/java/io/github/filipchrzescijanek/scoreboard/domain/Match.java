package io.github.filipchrzescijanek.scoreboard.domain;

import java.time.Instant;
import java.util.UUID;

public record Match(String id, String homeTeam, String awayTeam, Score score, Instant createdAt) {

    public Match withScore(Score score) {
        return new Match(this.id(), this.homeTeam(), this.awayTeam(), score, this.createdAt());
    }

    public static Match between(String homeTeam, String awayTeam) {
        String id = UUID.randomUUID().toString();
        Instant createdAt = Instant.now();
        Score score = Score.initial();
        return new Match(id, homeTeam, awayTeam, score, createdAt);
    }

}