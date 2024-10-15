package io.github.filipchrzescijanek.scoreboard.core;

import io.github.filipchrzescijanek.scoreboard.domain.Match;
import io.github.filipchrzescijanek.scoreboard.domain.Score;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import io.github.filipchrzescijanek.scoreboard.commands.AddMatch;
import io.github.filipchrzescijanek.scoreboard.commands.AddMatchHandler;
import io.github.filipchrzescijanek.scoreboard.commands.FinishMatch;
import io.github.filipchrzescijanek.scoreboard.commands.FinishMatchHandler;
import io.github.filipchrzescijanek.scoreboard.commands.UpdateScore;
import io.github.filipchrzescijanek.scoreboard.commands.UpdateScoreHandler;
import io.github.filipchrzescijanek.scoreboard.queries.GetMatchByIdHandler;
import io.github.filipchrzescijanek.scoreboard.queries.GetSummaryHandler;

public class Scoreboard
        implements GetMatchByIdHandler, AddMatchHandler, UpdateScoreHandler, FinishMatchHandler, GetSummaryHandler {

    private final MatchRepository repository;

    public Scoreboard(MatchRepository repository) {
        this.repository = repository;
    }

    public Scoreboard() {
        this(new MatchRepository());
    }

    @Override
    public Match getById(String id) {
        return repository.findById(id);
    }

    @Override
    public String handle(AddMatch command) {
        Match match = Match.between(command.homeTeam(), command.awayTeam());
        repository.save(match);
        return match.id();
    }

    @Override
    public void handle(UpdateScore command) {
        repository.update(command.matchId(), new Score(command.homeTeamScore(), command.awayTeamScore()));
    }

    @Override
    public void handle(FinishMatch command) {
        repository.delete(command.matchId());
    }

    @Override
    public String getSummary() {
        List<String> matchSummaries = repository.getAll().stream()
                .sorted(getSummaryComparator())
                .map(Match::toString)
                .collect(Collectors.toUnmodifiableList());
        StringBuilder sb = new StringBuilder();
        int index = 0;
        for (String summaryLine : matchSummaries) {
            index++;
            sb.append(String.format("%d. %s%n", index, summaryLine));
        }
        return sb.toString();
    };

    private Comparator<Match> getSummaryComparator() {
        return (first, second) -> {
            int sumOfScoresOfFirstMatch = sumOfScores(first);
            int sumOfScoresOfSecondMatch = sumOfScores(second);
            if (sumOfScoresOfFirstMatch == sumOfScoresOfSecondMatch) {
                return second.createdAt().getNano() - first.createdAt().getNano();
            } else {
                return sumOfScoresOfSecondMatch - sumOfScoresOfFirstMatch;
            }
        };
    }

    private int sumOfScores(Match match) {
        return match.score().homeScore() + match.score().awayScore();
    }

}
