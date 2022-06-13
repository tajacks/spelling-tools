package com.tajacks.api.spellingtools.controller;

import com.tajacks.api.spellingtools.exception.SpellingParameterException;
import com.tajacks.api.spellingtools.model.api.request.SpellingGenerateRequest;
import com.tajacks.api.spellingtools.model.api.request.SpellingSolveRequest;
import com.tajacks.api.spellingtools.service.SpellingToolService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class SpellingToolsRESTController {
    SpellingToolService spellingService;

    public SpellingToolsRESTController(SpellingToolService spellingService) {
        this.spellingService = spellingService;
    }

    @PostMapping (value = "/spelling/solve", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> solvePuzzle(@RequestBody SpellingSolveRequest request) {
        try {
            return ResponseEntity.ok(spellingService.solve(request));
        } catch (SpellingParameterException spe) {
            return ResponseEntity.badRequest().body(Map.of("error", spe.getMessage()));
        }
    }

    @PostMapping (value = "/spelling/generate", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> generatePuzzle(@RequestBody SpellingGenerateRequest request) {
        try {
            return ResponseEntity.ok(spellingService.generate(request));
        } catch (SpellingParameterException spe) {
            return ResponseEntity.badRequest().body(Map.of("error", spe.getMessage()));
        }
    }

}
