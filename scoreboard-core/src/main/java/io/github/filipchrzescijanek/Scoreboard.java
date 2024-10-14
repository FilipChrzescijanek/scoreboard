package io.github.filipchrzescijanek;

public class Scoreboard implements MatchesContainer, AddMatchHandler {

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
