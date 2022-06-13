package com.tajacks.api.spellingtools.service;

import com.tajacks.api.spellingtools.client.SpellingToolGenerator;
import com.tajacks.api.spellingtools.client.SpellingToolSolver;
import com.tajacks.api.spellingtools.client.SpellingToolValidator;
import com.tajacks.api.spellingtools.exception.SpellingToolException;
import com.tajacks.api.spellingtools.model.GameSet;
import com.tajacks.api.spellingtools.model.api.request.SpellingGenerateRequest;
import com.tajacks.api.spellingtools.model.api.request.SpellingSolveRequest;
import com.tajacks.api.spellingtools.model.api.response.GeneratedResponse;
import com.tajacks.api.spellingtools.model.api.response.SolvedResponse;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class SpellingToolService {
    private final SpellingToolSolver    solver;
    private final SpellingToolGenerator generator;
    private final SpellingToolValidator validator;

    public SpellingToolService(SpellingToolSolver solver, SpellingToolGenerator generator, SpellingToolValidator validator) {
        this.solver    = solver;
        this.generator = generator;
        this.validator = validator;
    }

    public SolvedResponse solve(SpellingSolveRequest request) throws SpellingToolException {
        validator.validate(request);
        return new SolvedResponse(solver.solve(request));
    }

    public GeneratedResponse generate(SpellingGenerateRequest request) {
        validator.validate(request);
        Map<Character, Map<Long, List<List<Character>>>> puzzlesPerCharacter = generator.generate(request);

        List<GameSet> gameSets = new ArrayList<>();

        for (Map.Entry<Character, Map<Long, List<List<Character>>>> entry : puzzlesPerCharacter.entrySet()) {
            for (Map.Entry<Long, List<List<Character>>> nextEntryLevel : entry.getValue().entrySet()) {
                nextEntryLevel.getValue()
                              .stream()
                              .map(chars -> new GameSet(entry.getKey(), new HashSet<>(chars), nextEntryLevel.getKey()))
                              .forEach(gameSets::add);
            }
        }

        return new GeneratedResponse(gameSets.stream().sorted(Comparator.comparingLong(GameSet::numberOfSolutions).reversed()).limit(request.getResultLimit()).collect(Collectors.toList()));
    }
}
