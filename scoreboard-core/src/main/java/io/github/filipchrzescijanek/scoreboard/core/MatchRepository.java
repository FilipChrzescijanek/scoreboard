package io.github.filipchrzescijanek.scoreboard.core;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import io.github.filipchrzescijanek.scoreboard.domain.Match;
import io.github.filipchrzescijanek.scoreboard.domain.Score;

public class MatchRepository {

    private final Map<String, Match> dataSource;

    public MatchRepository(Map<String, Match> dataSource) {
        this.dataSource = dataSource;
    }

    public MatchRepository() {
        this(new ConcurrentHashMap<>());
    }

    public Match findById(String id) {
        return dataSource.get(id);
    }

    public void save(Match match) {
        dataSource.putIfAbsent(match.id(), match);
    }

    public void update(String matchId, Score score) {
        dataSource.compute(matchId, (k, v) -> {
            if (Objects.isNull(v)) {
                throw new IllegalStateException("Error: match not found, can't update the score");
            } else {
                return v.withScore(score);
            }
        });
    }

    public void delete(String matchId) {
        dataSource.compute(matchId, (k, v) -> {
            if (Objects.isNull(v)) {
                throw new IllegalStateException("Error: match not found, can't finish it");
            } else {
                return null;
            }
        });
    }

    public Collection<Match> getAll() {
        return dataSource.values();
    }

}
