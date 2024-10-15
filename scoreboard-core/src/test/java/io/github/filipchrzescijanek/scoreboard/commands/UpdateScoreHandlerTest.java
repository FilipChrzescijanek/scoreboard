package io.github.filipchrzescijanek.scoreboard.commands;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import io.github.filipchrzescijanek.scoreboard.core.MatchRepository;
import io.github.filipchrzescijanek.scoreboard.core.Scoreboard;
import io.github.filipchrzescijanek.scoreboard.domain.Match;
import io.github.filipchrzescijanek.scoreboard.domain.Score;

import static org.assertj.core.api.Assertions.*;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;

public class UpdateScoreHandlerTest {

    @ParameterizedTest
    @MethodSource("provideInvalidArguments")
    void testUpdatingScoreWithInvalidArguments(String matchId, int homeTeamScore, int awayTeamScore, String exceptionMessage) {
        // given
        Scoreboard scoreboard = new Scoreboard();

        // when
        IllegalArgumentException exception = catchIllegalArgumentException(
                () -> scoreboard.handle(new UpdateScore(matchId, homeTeamScore, awayTeamScore)));

        // then
        assertThat(exception).hasMessage(exceptionMessage);
    }

    @ParameterizedTest
    @MethodSource("provideInvalidIds")
    void testUpdatingScoreWithValidArgumentsWhenMatchDoesNotExist(String matchId, String exceptionMessage) {
        // given
        Scoreboard scoreboard = new Scoreboard();

        // when
        IllegalStateException exception = catchIllegalStateException(
                () -> scoreboard.handle(new UpdateScore(matchId, 0, 0)));

        // then
        assertThat(exception).hasMessage(exceptionMessage);
    }

    @Test
    void testUpdatingScoreWithValidArgumentsWhenMatchExists() {
        // given
        Match match = new Match(UUID.randomUUID().toString(), "home", "away", Score.initial(), Instant.now());
        Map<String, Match> dataSource = new HashMap<>(Map.of(match.id(), match));
        MatchRepository matchRepository = new MatchRepository(dataSource);
        Scoreboard scoreboard = new Scoreboard(matchRepository);

        // when
        scoreboard.handle(new UpdateScore(match.id(), 1, 1));
        Match updatedMatch = scoreboard.getById(match.id());

        // then
        assertThat(updatedMatch).isNotNull();
        assertThat(updatedMatch).extracting(Match::score).isEqualTo(new Score(1, 1));
    }

    static Stream<Arguments> provideInvalidArguments() {
        return Stream.of(
                Arguments.of(null, -1, -1,
                        "Error(s): match ID cannot be null, home team score cannot be negative, away team score cannot be negative"),
                Arguments.of(null, -1, 0, "Error(s): match ID cannot be null, home team score cannot be negative"),
                Arguments.of(null, 0, -1, "Error(s): match ID cannot be null, away team score cannot be negative"),
                Arguments.of(null, 0, 0, "Error(s): match ID cannot be null"),
                Arguments.of("", 0, 0, "Error(s): match ID cannot be blank"),
                Arguments.of(" ", 0, 0, "Error(s): match ID cannot be blank"));
    }

    static Stream<Arguments> provideInvalidIds() {
        return Stream.of(
                Arguments.of("non-existing-id", "Error: match not found, can't update the score"),
                Arguments.of(UUID.randomUUID().toString(), "Error: match not found, can't update the score"));
    }

}
