package io.github.filipchrzescijanek.scoreboard.queries;

import io.github.filipchrzescijanek.scoreboard.domain.Match;

public interface GetMatchByIdHandler {

    Match getById(String id);

}
