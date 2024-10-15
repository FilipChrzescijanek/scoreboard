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

public class FinishMatchHandlerTest {

    @ParameterizedTest
    @MethodSource("provideInvalidArguments")
    void testFinishingMatchWithInvalidArguments(String matchId, String exceptionMessage) {
        // given
        Scoreboard scoreboard = new Scoreboard();

        // when
        IllegalArgumentException exception = catchIllegalArgumentException(
                () -> scoreboard.handle(new FinishMatch(matchId)));

        // then
        assertThat(exception).hasMessage(exceptionMessage);
    }

    @ParameterizedTest
    @MethodSource("provideInvalidIds")
    void testFinishingMatchWithValidArgumentsWhenMatchDoesNotExist(String matchId, String exceptionMessage) {
        // given
        Scoreboard scoreboard = new Scoreboard();

        // when
        IllegalStateException exception = catchIllegalStateException(
                () -> scoreboard.handle(new FinishMatch(matchId)));

        // then
        assertThat(exception).hasMessage(exceptionMessage);
    }

    @Test
    void testFinishingMatchWithValidArgumentsWhenMatchExists() {
        // given
        Match match = new Match(UUID.randomUUID().toString(), "home", "away", Score.initial(), Instant.now());
        Map<String, Match> dataSource = new HashMap<>(Map.of(match.id(), match));
        MatchRepository matchRepository = new MatchRepository(dataSource);
        Scoreboard scoreboard = new Scoreboard(matchRepository);

        // when
        scoreboard.handle(new FinishMatch(match.id()));
        Match deletedMatch = scoreboard.getById(match.id());

        // then
        assertThat(deletedMatch).isNull();
    }

    static Stream<Arguments> provideInvalidArguments() {
        return Stream.of(
                Arguments.of(null, "Error(s): match ID cannot be null"),
                Arguments.of("", "Error(s): match ID cannot be blank"),
                Arguments.of(" ", "Error(s): match ID cannot be blank"));
    }

    static Stream<Arguments> provideInvalidIds() {
        return Stream.of(
                Arguments.of("non-existing-id", "Error: match not found, can't finish it"),
                Arguments.of(UUID.randomUUID().toString(), "Error: match not found, can't finish it"));
    }

}
