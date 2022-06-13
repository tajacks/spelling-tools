package com.tajacks.api.spellingtools.model.api.request;

import com.tajacks.api.spellingtools.exception.SpellingParameterException;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode (callSuper = true)
public class SpellingGenerateRequest extends SpellingRequest {
    private int puzzleSize = 6;

    @Override
    public void validate() throws SpellingParameterException {

    }
}
