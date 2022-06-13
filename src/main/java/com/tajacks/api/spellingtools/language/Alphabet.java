package com.tajacks.api.spellingtools.language;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@EqualsAndHashCode
@ToString
public class Alphabet {
    private final TreeSet<Character> alphabet;

    public Alphabet(Collection<Character> alphabet) {
        this.alphabet = new TreeSet<>(alphabet);
    }

    public TreeSet<Character> getSortedAlphabet() {
        return alphabet;
    }

    public Set<Character> returnRemainderAfterRemoving(Character c, Collection<Character> otherCharacters) {
        return returnRemainderAfterRemoving(Stream.concat(Stream.of(c), otherCharacters.stream()).collect(Collectors.toSet()));
    }

    public Set<Character> returnRemainderAfterRemoving(Collection<Character> toBeRemoved) {
        Set<Character> results = new HashSet<>(alphabet);
        results.removeAll(toBeRemoved);
        return results;
    }
}
