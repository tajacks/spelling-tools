package com.tajacks.api.spellingtools.language;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.*;
import java.util.stream.Collectors;

@Getter
@EqualsAndHashCode
public class Dictionary {
    private final Set<Word>                 words;
    private final Map<Character, Set<Word>> wordsSortedByContainingLetter;
    private final Map<Integer, Set<Word>>   wordsSortedByLength;
    private final Alphabet                  alphabet;

    public Dictionary(Set<Word> words, Alphabet alphabet) {
        this.alphabet                      = alphabet;
        this.words                         = words;
        this.wordsSortedByLength           = words.stream().collect(Collectors.groupingBy(Word::length, Collectors.toSet()));
        this.wordsSortedByContainingLetter = new HashMap<>();

        this.alphabet.getSortedAlphabet()
                     .forEach(alphaChar ->
                                      words.stream()
                                           .filter(word -> word.containsChar(alphaChar))
                                           .forEach(word -> wordsSortedByContainingLetter.computeIfAbsent(alphaChar, k -> new HashSet<>()).add(word)));
    }

    public Set<Word> getWordsContainingLetter(Character letter) {
        return wordsSortedByContainingLetter.getOrDefault(letter, new HashSet<>());
    }

    public Set<Word> getWordsNotContainingLetter(Character letter) {
        return words.stream()
                    .filter(word -> !word.containsChar(letter))
                    .collect(Collectors.toSet());
    }

    public Set<Word> getWordsContainingLetters(Collection<Character> letters) {
        Set<Character> mustContain = new HashSet<>(letters);
        return words.stream().filter(word -> word.containsAllChars(mustContain)).collect(Collectors.toSet());
    }

    public long numberOfWordsThatContainAll(Collection<Character> letters) {
        Set<Character> mustContain = new HashSet<>(letters);
        return words.stream().filter(word -> word.containsAllChars(mustContain)).count();
    }
}
