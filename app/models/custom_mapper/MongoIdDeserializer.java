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
//http://www.davismol.net/2015/03/05/jackson-json-deserialize-a-list-of-objects-of-subclasses-of-an-abstract-class/
public class MongoIdDeserializer extends JsonDeserializer<MongoId> {

    @Override
    public MongoId deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException {
        JsonNode node = jp.getCodec().readTree(jp);
        Logger.info("Deserialize MongoId: "+node.asText());
        return new MongoId(node.asText());
    }
}
