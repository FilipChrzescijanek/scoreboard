package io.github.filipchrzescijanek;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MatchRepository {

    private final Map<String, Match> dataSource = new ConcurrentHashMap<>();

    public void save(Match match) {
        dataSource.putIfAbsent(match.id(), match);
    }

    public Match findById(String id) {
        return dataSource.get(id);
    }

}
