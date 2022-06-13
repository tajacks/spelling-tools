package com.tajacks.api.spellingtools.client;

import com.tajacks.api.spellingtools.language.Alphabet;
import com.tajacks.api.spellingtools.language.Dictionary;
import com.tajacks.api.spellingtools.language.Word;
import com.tajacks.api.spellingtools.model.api.request.SpellingGenerateRequest;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Component
public class SpellingToolGenerator {
    private final Dictionary                                       dictionary;
    private final Map<Character, Map<Long, List<List<Character>>>> cache;

    public SpellingToolGenerator(Dictionary dictionary) {
        this.dictionary = dictionary;
        this.cache      = new HashMap<>();
    }

    public Map<Character, Map<Long, List<List<Character>>>> generate(final SpellingGenerateRequest request) {
        Dictionary dictionaryForSolve = this.dictionary;
        Alphabet   alphabetForSolve   = this.dictionary.getAlphabet();

        if (request.hasAlphabet()) {
            alphabetForSolve = new Alphabet(request.getAlphabet().stream().filter(Predicate.not(String::isEmpty)).map(s -> s.charAt(0)).collect(Collectors.toSet()));
        }

        if (request.hasWords()) {
            dictionaryForSolve = new Dictionary(request.getWords().stream().filter(Predicate.not(String::isEmpty)).map(Word::new).collect(Collectors.toSet()), alphabetForSolve);
        }

        Set<Word> wordsForSolve = dictionaryForSolve.getWords();

        if (request.hasRequiredCharacter()) {
            wordsForSolve = dictionary.getWordsContainingLetter(request.getRequiredCharacter().charAt(0));
        }
        Map<Character, Map<Long, List<List<Character>>>> finalResults = new HashMap<>();
        Set<Character> uniqueChars = wordsForSolve.stream()
                                                  .flatMap(Word::getUniqueCharacters)
                                                  .collect(Collectors.toSet());

        Set<Character> requiredCharacters = request.hasRequiredCharacter() ? Set.of(request.getRequiredCharacter().charAt(0)) : uniqueChars;

        for (Character requiredCharacter : requiredCharacters) {
            boolean useCache = !request.hasWords() && cache.get(requiredCharacter) != null;
            finalResults.put(requiredCharacter, returnComputedResults(uniqueChars, requiredCharacter, dictionaryForSolve, useCache, request.getPuzzleSize(), request.getLowerCharacterBound()));
            if (!useCache) {
                cache.putAll(finalResults);
            }
        }

        return finalResults;
    }

    private Map<Long, List<List<Character>>> returnComputedResults(final Set<Character> uniqueCharacters, final Character requiredCharacter, final Dictionary dictionaryForSolve, final boolean useCache, final int puzzleSize, final int minWordSize) {
        if (useCache) {
            return cache.get(requiredCharacter);
        }

        final Set<Character> charactersMinusRequiredCharacter = new HashSet<>(uniqueCharacters);
        charactersMinusRequiredCharacter.remove(requiredCharacter);

        final List<List<Character>> possibilities = getUniqueLetterCombinations(charactersMinusRequiredCharacter, puzzleSize);
        return possibilities.stream().collect(Collectors.groupingBy(chars -> getNumberOfPossibleWords(requiredCharacter, chars, dictionaryForSolve, minWordSize)));
    }

    private List<List<Character>> getUniqueLetterCombinations(final Set<Character> charactersToChooseFrom, final int sizeOfCombination) {
        if (charactersToChooseFrom.size() <= sizeOfCombination) {
            return List.of(new ArrayList<>(charactersToChooseFrom));
        }

        final Queue<Character>      sortedAvailableCharacters = new LinkedList<>(new TreeSet<>(charactersToChooseFrom));
        final List<List<Character>> results                   = new ArrayList<>();

        final List<Character> workingSet = new ArrayList<>();

        while (!sortedAvailableCharacters.isEmpty()) {
            while (workingSet.size() != (sizeOfCombination - 1)) {
                workingSet.add(sortedAvailableCharacters.poll());
            }

            Character finalCharacter = sortedAvailableCharacters.peek();
            if (finalCharacter != null) {
                for (Character c : sortedAvailableCharacters) {
                    List<Character> tempList = new ArrayList<>(workingSet);
                    tempList.add(c);
                    results.add(tempList);
                }

                workingSet.remove(0);
                workingSet.add(finalCharacter);
                sortedAvailableCharacters.poll();
            }
        }
        return results;
    }

    private long getNumberOfPossibleWords(final Character requiredCharacter, final Collection<Character> otherUsableCharacters, final Dictionary dictionaryToUse, final int minLetters) {
        final Set<Character> lettersWeHave = new HashSet<>(otherUsableCharacters);
        lettersWeHave.add(requiredCharacter);
        final Set<Character> unusableLetters = dictionaryToUse.getAlphabet().returnRemainderAfterRemoving(lettersWeHave);
        return dictionaryToUse.getWordsContainingLetter(requiredCharacter)
                              .stream()
                              .filter(word -> !word.containsAnyChar(unusableLetters))
                              .filter(word -> word.length() >= minLetters)
                              .count();
    }

}
