package io.github.filipchrzescijanek.scoreboard;

import java.time.Instant;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;

import io.github.filipchrzescijanek.scoreboard.commands.AddMatch;
import io.github.filipchrzescijanek.scoreboard.commands.FinishMatch;
import io.github.filipchrzescijanek.scoreboard.commands.UpdateScore;
import io.github.filipchrzescijanek.scoreboard.core.Scoreboard;
import io.github.filipchrzescijanek.scoreboard.domain.Match;
import io.github.filipchrzescijanek.scoreboard.domain.Score;

public class Demo {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        Scoreboard scoreboard = new Scoreboard();

        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            var producer = CompletableFuture.runAsync(runWorldCup(scoreboard, executor), executor);
            var consumer = CompletableFuture.runAsync(printLiveScores(scoreboard), executor);
            CompletableFuture.allOf(producer, consumer).join();
        }
    }

    private static Runnable runWorldCup(Scoreboard scoreboard, ExecutorService executor) {
        String[] countries = Locale.getISOCountries();
        return () -> {
            int counter = 0;
            while (true) {
                int chanceOfNewMatch = ThreadLocalRandom.current().nextInt(0, 100);
                if (chanceOfNewMatch > 90) {
                    String matchId = scoreboard.handle(new AddMatch(countries[counter % countries.length],
                            countries[(counter + 1) % countries.length]));
                    counter += 2;

                    CompletableFuture.runAsync(runMatch(scoreboard, matchId), executor);
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ignored) {
                }
            }
        };
    }

    private static Runnable runMatch(Scoreboard scoreboard, String matchId) {
        return () -> {
            Match match = scoreboard.getById(matchId);
            do {
                try {
                    Thread.sleep(ThreadLocalRandom.current().nextInt(90, 110));
                } catch (InterruptedException ignored) {
                }
                int chanceOfGoal = ThreadLocalRandom.current().nextInt(0, 100);
                if (chanceOfGoal > 80) {
                    Score currentScore = match.score();
                    Score newScore;
                    if (chanceOfGoal % 2 == 0) {
                        newScore = new Score(currentScore.homeScore() + 1, currentScore.awayScore());
                    } else {
                        newScore = new Score(currentScore.homeScore(), currentScore.awayScore() + 1);
                    }
                    scoreboard.handle(new UpdateScore(matchId, newScore.homeScore(), newScore.awayScore()));
                }
                match = scoreboard.getById(matchId);
            } while (match.createdAt().isAfter(Instant.now().minusSeconds(9)));
            scoreboard.handle(new FinishMatch(matchId));
        };
    }

    private static Runnable printLiveScores(Scoreboard scoreboard) {
        return () -> {
            while (true) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ignored) {
                }
                System.out.print("\033[H\033[2J\033[3J");
                System.out.flush();
                System.out.println(scoreboard.getSummary());
            }
        };
    }

}
