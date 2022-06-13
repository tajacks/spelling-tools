package com.tajacks.api.spellingtools.model;

import com.tajacks.api.spellingtools.exception.SpellingParameterException;

public interface Validates {
    void validate() throws SpellingParameterException;
}
