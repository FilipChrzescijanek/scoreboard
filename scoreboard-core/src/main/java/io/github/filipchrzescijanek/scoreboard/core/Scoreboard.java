package io.github.filipchrzescijanek.scoreboard.core;

import io.github.filipchrzescijanek.scoreboard.domain.Match;
import io.github.filipchrzescijanek.scoreboard.commands.AddMatch;
import io.github.filipchrzescijanek.scoreboard.commands.AddMatchHandler;
import io.github.filipchrzescijanek.scoreboard.queries.GetMatchByIdHandler;

public class Scoreboard implements GetMatchByIdHandler, AddMatchHandler {

    private final MatchRepository repository = new MatchRepository();

    @Override
    public Match getById(String id) {
        return repository.findById(id);
    }

    @Override
    public String handle(AddMatch command) {
        Match match = Match.between(command.homeTeam(), command.awayTeam());
        repository.save(match);
        return match.id();
    };
}
