package com.tajacks.api.spellingtools.model.api.request;

import com.tajacks.api.spellingtools.model.Validates;
import lombok.Data;

import java.util.Set;

@Data
public abstract class SpellingRequest implements Validates {
    protected int         resultLimit         = 100;
    protected int         lowerCharacterBound = 4;
    protected int         upperCharacterBound = 25;
    protected String      requiredCharacter;
    protected Set<String> words;
    protected Set<String> alphabet;

    public boolean hasWords() {
        return this.words != null && !this.words.isEmpty();
    }

    public boolean hasAlphabet() {
        return this.alphabet != null && !this.alphabet.isEmpty();
    }

    public boolean hasRequiredCharacter() {
        return this.requiredCharacter != null && !this.requiredCharacter.isEmpty();
    }
}
