package io.github.filipchrzescijanek.scoreboard.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public record FinishMatch(String matchId) {

    public FinishMatch {
        List<String> errors = new ArrayList<>();
        if (Objects.isNull(matchId)) {
            errors.add("match ID cannot be null");
        } else if (matchId.isBlank()) {
            errors.add("match ID cannot be blank");
        }
        if (!errors.isEmpty()) {
            throw new IllegalArgumentException(String.format("Error(s): %s", String.join(", ", errors)));
        }
    }

}
