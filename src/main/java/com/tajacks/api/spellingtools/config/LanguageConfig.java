package com.tajacks.api.spellingtools.config;


import com.tajacks.api.spellingtools.language.Alphabet;
import com.tajacks.api.spellingtools.language.Dictionary;
import com.tajacks.api.spellingtools.language.Word;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Configuration
public class LanguageConfig {

    @Value ("${language.alphabet}")
    private String alphabet;

    @Value ("${language.wordsource}")
    private String words;

    @Bean
    public Alphabet alphabet() {
        Set<Character> sources = Arrays.stream(alphabet.split(","))
                                       .map(str -> str.charAt(0))
                                       .collect(Collectors.toSet());
        return new Alphabet(sources);
    }

    @Bean
    @DependsOn ("alphabet")
    public Dictionary dictionary(Alphabet alphabet) throws IOException {
        try (Stream<String> lines = Files.lines(Path.of(words))) {
            return new Dictionary(lines.map(Word::new).collect(Collectors.toSet()), alphabet);
        }
    }
}
