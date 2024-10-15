package io.github.filipchrzescijanek.scoreboard.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public record UpdateScore(String matchId, int homeTeamScore, int awayTeamScore) {

    public UpdateScore {
        List<String> errors = new ArrayList<>();
        if (Objects.isNull(matchId)) {
            errors.add("match ID cannot be null");
        } else if (matchId.isBlank()) {
            errors.add("match ID cannot be blank");
        }
        if (homeTeamScore < 0) {
            errors.add("home team score cannot be negative");
        }
        if (awayTeamScore < 0) {
            errors.add("away team score cannot be negative");
        }
        if (!errors.isEmpty()) {
            throw new IllegalArgumentException(String.format("Error(s): %s", String.join(", ", errors)));
        }
    }

}
