package com.tajacks.api.spellingtools.client;

import com.tajacks.api.spellingtools.model.Validates;
import org.springframework.stereotype.Component;

@Component
public class SpellingToolValidator {

    public SpellingToolValidator() {
    }

    public void validate(Validates validates) {
        validates.validate();
    }
}
