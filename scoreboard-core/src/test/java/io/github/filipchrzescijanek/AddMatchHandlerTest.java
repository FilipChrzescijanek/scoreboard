package io.github.filipchrzescijanek;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.*;

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
        MatchDetails matchDetails = scoreboard.getById(matchId);

        // then
        assertThat(matchDetails).isNotNull();
        assertThat(matchDetails).extracting(MatchDetails::score).isEqualTo(Score.initial());
        assertThat(matchDetails).extracting(MatchDetails::homeTeam).isEqualTo("home");
        assertThat(matchDetails).extracting(MatchDetails::awayTeam).isEqualTo("away");
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
