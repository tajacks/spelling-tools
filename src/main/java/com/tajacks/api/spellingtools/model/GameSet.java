package com.tajacks.api.spellingtools.model;

import java.util.Set;

public record GameSet(Character requiredCharacter, Set<Character> otherCharacters, long numberOfSolutions) implements Comparable<GameSet> {

    @Override
    public int compareTo(GameSet o) {
        return Long.compare(numberOfSolutions, o.numberOfSolutions);
    }
}
