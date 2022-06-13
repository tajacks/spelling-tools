package com.tajacks.api.spellingtools.client;

import com.tajacks.api.spellingtools.language.Alphabet;
import com.tajacks.api.spellingtools.language.Dictionary;
import com.tajacks.api.spellingtools.language.Word;
import com.tajacks.api.spellingtools.model.api.request.SpellingSolveRequest;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Component
public class SpellingToolSolver {
    private final Dictionary dictionary;

    public SpellingToolSolver(Dictionary dictionary) {
        this.dictionary = dictionary;
    }

    public Set<Word> solve(SpellingSolveRequest request) {
        Dictionary dictionaryForSolve = this.dictionary;
        Alphabet   alphabetForSolve   = this.dictionary.getAlphabet();

        if (request.hasAlphabet()) {
            alphabetForSolve = new Alphabet(request.getAlphabet().stream().filter(Predicate.not(String::isEmpty)).map(s -> s.charAt(0)).collect(Collectors.toSet()));
        }

        if (request.hasWords()) {
            dictionaryForSolve = new Dictionary(request.getWords().stream().filter(Predicate.not(String::isEmpty)).map(Word::new).collect(Collectors.toSet()), alphabetForSolve);
        }

        Character      required = request.getRequiredCharacter().charAt(0);
        Set<Character> others   = request.getPossibleCharacters().stream().map(l -> l.charAt(0)).collect(Collectors.toSet());

        return getPossibleWords(required, others, dictionaryForSolve, request.getLowerCharacterBound(), request.getResultLimit());
    }

    private Set<Word> getPossibleWords(Character requiredCharacter, Collection<Character> otherUsableCharacters, Dictionary dictionary, int minLetters, int resultLimit) {
        Set<Character> unusableLetters = dictionary.getAlphabet()
                                                   .returnRemainderAfterRemoving(requiredCharacter, otherUsableCharacters);
        return dictionary.getWordsContainingLetter(requiredCharacter)
                         .stream()
                         .filter(word -> word.getStringRepresentation().length() >= minLetters)
                         .filter(word -> !word.containsAnyChar(unusableLetters))
                         .limit(resultLimit)
                         .collect(Collectors.toSet());
    }

    private Set<Word> getPossibleWords(Character requiredCharacter, Collection<Character> otherUsableCharacters, int minLetters, int resultLimit) {
        return getPossibleWords(requiredCharacter, otherUsableCharacters, this.dictionary, minLetters, resultLimit);
    }
}
