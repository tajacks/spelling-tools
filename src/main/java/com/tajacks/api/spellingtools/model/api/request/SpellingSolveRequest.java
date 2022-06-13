package com.tajacks.api.spellingtools.model.api.request;

import com.tajacks.api.spellingtools.exception.SpellingParameterException;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;

@Data
@EqualsAndHashCode (callSuper = true)
public class SpellingSolveRequest extends SpellingRequest {
    private Set<String> possibleCharacters;

    @Override
    public void validate() throws SpellingParameterException {
        if (this.requiredCharacter == null || this.requiredCharacter.isEmpty()) {
            throw new SpellingParameterException("'requiredCharacter' cannot be empty or null");
        }

        if (this.possibleCharacters == null || this.possibleCharacters.isEmpty()) {
            throw new SpellingParameterException("'possibleCharacters' cannot be empty or null");
        }
    }
}
