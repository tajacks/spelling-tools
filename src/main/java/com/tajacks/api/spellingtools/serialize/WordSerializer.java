package com.tajacks.api.spellingtools.serialize;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.tajacks.api.spellingtools.language.Word;

import java.io.IOException;

public class WordSerializer extends StdSerializer<Word> {

    public WordSerializer() {
        this(null);
    }

    public WordSerializer(Class<Word> w) {
        super(w);
    }

    @Override
    public void serialize(Word word, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeString(word.getStringRepresentation());
    }
}
