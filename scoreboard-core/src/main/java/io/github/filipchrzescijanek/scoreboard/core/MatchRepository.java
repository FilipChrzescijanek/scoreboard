package io.github.filipchrzescijanek.scoreboard.core;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.github.filipchrzescijanek.scoreboard.domain.Match;

public class MatchRepository {

    private final Map<String, Match> dataSource = new ConcurrentHashMap<>();

    public void save(Match match) {
        dataSource.putIfAbsent(match.id(), match);
    }

    public Match findById(String id) {
        return dataSource.get(id);
    }

}
