package models.custom_mapper;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import org.bson.types.ObjectId;
import play.Logger;

import java.io.IOException;

/**
 * Created by n.diazgranados on 08/01/2015.
 */

//http://www.baeldung.com/jackson-deserialization
public class IdDeserializer extends JsonDeserializer<String> {
    @Override
    public String deserialize(JsonParser jp, DeserializationContext context)
            throws IOException {
        JsonNode node = jp.getCodec().readTree(jp);
        Logger.info("deserialize"+node.asText());
        if (!ObjectId.isValid(node.asText())) throw context.mappingException("invalid ObjectId " + node.asText());
        return node.asText();
    }
}
