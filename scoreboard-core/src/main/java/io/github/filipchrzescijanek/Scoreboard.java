package io.github.filipchrzescijanek;

public class Scoreboard implements MatchesContainer, AddMatchHandler {

    @Override
    public MatchDetails getById(String id) {
        throw new UnsupportedOperationException("Unimplemented method 'getById'");
    }

    @Override
    public String handle(AddMatch addMatch) {
        throw new UnsupportedOperationException("Unimplemented method 'handle'");
    }

}
