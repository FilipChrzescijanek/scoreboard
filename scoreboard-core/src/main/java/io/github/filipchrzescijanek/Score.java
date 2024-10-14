package io.github.filipchrzescijanek;

public record Score(int homeScore, int awayScore) {

    private static final Score INITIAL_SCORE = new Score(0, 0);

    public static Score initial() {
        return INITIAL_SCORE;
    }

}