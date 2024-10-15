package io.github.filipchrzescijanek.scoreboard.commands;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import io.github.filipchrzescijanek.scoreboard.core.Scoreboard;
import io.github.filipchrzescijanek.scoreboard.domain.Match;
import io.github.filipchrzescijanek.scoreboard.domain.Score;

import static org.assertj.core.api.Assertions.*;

import java.time.Instant;
import java.util.UUID;
import java.util.stream.Stream;

public class AddMatchHandlerTest {

    @ParameterizedTest
    @MethodSource("provideInvalidArguments")
    void testAddingNewMatchWithInvalidArguments(String homeTeam, String awayTeam, String exceptionMessage) {
        // given
        Scoreboard scoreboard = new Scoreboard();

        // when
        IllegalArgumentException exception = catchIllegalArgumentException(
                () -> scoreboard.handle(new AddMatch(homeTeam, awayTeam)));

        // then
        assertThat(exception).hasMessage(exceptionMessage);
    }

    @Test
    void testAddingNewMatchWithValidArguments() {
        // given
        Scoreboard scoreboard = new Scoreboard();

        // when
        String matchId = scoreboard.handle(new AddMatch("home", "away"));
        Match match = scoreboard.getById(matchId);

        // then
        assertThat(match).isNotNull();
        assertThat(match).extracting(Match::id).isEqualTo(matchId);
        assertThatNoException().isThrownBy(() -> UUID.fromString(matchId));
        assertThat(match).extracting(Match::score).isEqualTo(Score.initial());
        assertThat(match).extracting(Match::createdAt).isNotNull().isInstanceOf(Instant.class);
        assertThat(match).extracting(Match::homeTeam).isEqualTo("home");
        assertThat(match).extracting(Match::awayTeam).isEqualTo("away");
    }

    static Stream<Arguments> provideInvalidArguments() {
        return Stream.of(
                Arguments.of(null, null,
                        "Error(s): home team name cannot be null, away team name cannot be null, team names cannot be the same"),
                Arguments.of("", null,
                        "Error(s): home team name cannot be blank, away team name cannot be null"),
                Arguments.of(null, "",
                        "Error(s): home team name cannot be null, away team name cannot be blank"),
                Arguments.of("", "",
                        "Error(s): home team name cannot be blank, away team name cannot be blank, team names cannot be the same"),
                Arguments.of("  ", "  ",
                        "Error(s): home team name cannot be blank, away team name cannot be blank, team names cannot be the same"),
                Arguments.of("team", null, "Error(s): away team name cannot be null"),
                Arguments.of("team", " ", "Error(s): away team name cannot be blank"),
                Arguments.of(null, "team", "Error(s): home team name cannot be null"),
                Arguments.of(" ", "team", "Error(s): home team name cannot be blank"),
                Arguments.of("team", "team", "Error(s): team names cannot be the same"));
    }

}
