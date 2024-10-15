package io.github.filipchrzescijanek.scoreboard.queries;

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

public class GetSummaryHandlerTest {

    @Test
    void testGettingMatchesSummary() {
        // given
        Match mexCan = new Match(
                UUID.randomUUID().toString(),
                "Mexico", "Canada",
                new Score(0, 5),
                Instant.now());
        Match espBra = new Match(
                UUID.randomUUID().toString(),
                "Spain", "Brazil",
                new Score(10, 2),
                mexCan.createdAt().plusMillis(1));
        Match gerFra = new Match(
                UUID.randomUUID().toString(),
                "Germany", "France",
                new Score(2, 2),
                espBra.createdAt().plusMillis(1));
        Match uruIta = new Match(
                UUID.randomUUID().toString(),
                "Uruguay", "Italy",
                new Score(6, 6),
                gerFra.createdAt().plusMillis(1));
        Match argAus = new Match(
                UUID.randomUUID().toString(),
                "Argentina", "Australia",
                new Score(3, 1),
                uruIta.createdAt().plusMillis(1));
        Map<String, Match> dataSource = new HashMap<>(Map.of(
                mexCan.id(), mexCan,
                espBra.id(), espBra,
                gerFra.id(), gerFra,
                uruIta.id(), uruIta,
                argAus.id(), argAus));
        MatchRepository matchRepository = new MatchRepository(dataSource);
        Scoreboard scoreboard = new Scoreboard(matchRepository);

        // when
        String summary = scoreboard.getSummary();

        // then
        assertThat(summary).isEqualToNormalizingNewlines("""
            1. Uruguay 6 - Italy 6
            2. Spain 10 - Brazil 2
            3. Mexico 0 - Canada 5
            4. Argentina 3 - Australia 1
            5. Germany 2 - France 2
            """);
    }

}
