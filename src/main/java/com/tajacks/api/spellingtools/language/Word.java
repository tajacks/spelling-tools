package com.tajacks.api.spellingtools.language;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.tajacks.api.spellingtools.serialize.WordSerializer;
import lombok.Data;

import java.util.Collection;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Data
@JsonSerialize (using = WordSerializer.class)
public class Word implements Comparable<Word> {
    private final String stringRepresentation;
    private final char[] charRepresentation;

    public Word(String stringRepresentation) {
        this.stringRepresentation = stringRepresentation.toLowerCase();
        this.charRepresentation   = this.stringRepresentation.toCharArray();
    }

    public boolean containsAnyChar(Collection<Character> chars) {
        return chars.stream().anyMatch(this::containsChar);
    }

    public boolean containsChar(Character c) {
        for (char existingChar : charRepresentation) {
            if (existingChar == c) {
                return true;
            }
        }
        return false;
    }

    public boolean containsAllChars(Collection<Character> chars) {
        return chars.stream().allMatch(this::containsChar);
    }

    public int length() {
        return stringRepresentation.length();
    }

    public Stream<Character> getUniqueCharacters() {
        return IntStream.range(0, charRepresentation.length)
                        .mapToObj(i -> charRepresentation[i])
                        .distinct();
    }

    @Override
    public String toString() {
        return stringRepresentation;
    }

    @Override
    public int compareTo(Word otherWord) {
        return this.stringRepresentation.compareTo(otherWord.stringRepresentation);
    }
}
