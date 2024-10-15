package io.github.filipchrzescijanek.scoreboard.core;

import io.github.filipchrzescijanek.scoreboard.domain.Match;

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
        throw new UnsupportedOperationException("Unimplemented method 'handle(UpdateScore)'");
    };
}
