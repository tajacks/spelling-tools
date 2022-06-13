package com.tajacks.api.spellingtools.model.api.response;

import com.tajacks.api.spellingtools.language.Word;

import java.util.Set;

public record SolvedResponse(Set<Word> results) {
}
