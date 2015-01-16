package models.custom_mapper;

/**
 * Created by n.diazgraados on 09/01/2015.
 */

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import models.dao.MongoId;
import play.Logger;

import java.io.IOException;

public class MongoIdDeserializer extends JsonDeserializer<MongoId> {

    @Override
    public MongoId deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException {
        JsonNode node = jp.getCodec().readTree(jp);
        Logger.info("Deserialize MongoId: "+node.asText());
        return new MongoId(node.asText());
    }
}
