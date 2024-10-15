# scoreboard

`Live Football World Cup Scoreboard` Java library

## requirements

JDK version 21 or later

## build

Run `./gradlew build`

## run demo

Run `./gradlew run`

## comments

- country names can be random, just cannot be blank and cannot be the same
- updating score to a lower one is allowed, e.g. to handle score being reverted after a VAR goal review
- throwing exceptions if caller is trying to update or finish a match that is not currently in progress
- demo added just for fun, it is not of the best quality

## directory structure

### scoreboard-core

Contains core library sources

### scoreboard-demo

Contains a demo application that uses the core library
