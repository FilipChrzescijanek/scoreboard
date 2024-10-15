package io.github.filipchrzescijanek.scoreboard.core;

import io.github.filipchrzescijanek.scoreboard.domain.Match;
import io.github.filipchrzescijanek.scoreboard.domain.Score;

import java.util.Objects;

import io.github.filipchrzescijanek.scoreboard.commands.AddMatch;
import io.github.filipchrzescijanek.scoreboard.commands.AddMatchHandler;
import io.github.filipchrzescijanek.scoreboard.commands.UpdateScore;
import io.github.filipchrzescijanek.scoreboard.commands.UpdateScoreHandler;
import io.github.filipchrzescijanek.scoreboard.queries.GetMatchByIdHandler;

public class Scoreboard implements GetMatchByIdHandler, AddMatchHandler, UpdateScoreHandler {

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
        Match match = repository.findById(command.matchId());
        if (Objects.isNull(match)) {
            throw new IllegalStateException("Error: match not found, can't update the score");
        } else {
            repository.update(match.withScore(new Score(command.homeTeamScore(), command.awayTeamScore())));
        }
    };

}
