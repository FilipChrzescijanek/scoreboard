package io.github.filipchrzescijanek;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public record AddMatch(String homeTeam, String awayTeam) {

    public AddMatch {
        List<String> errors = new ArrayList<>();
        if (Objects.isNull(homeTeam)) {
            errors.add("home team name cannot be null");
        } else if (homeTeam.isBlank()) {
            errors.add("home team name cannot be blank");
        }
        if (Objects.isNull(awayTeam)) {
            errors.add("away team name cannot be null");
        } else if (awayTeam.isBlank()) {
            errors.add("away team name cannot be blank");
        }
        if (Objects.equals(homeTeam, awayTeam)) {
            errors.add("team names cannot be the same");
        }
        if (!errors.isEmpty()) {
            throw new IllegalArgumentException(String.format("Error(s): %s", String.join(", ", errors)));
        }
    }

}
