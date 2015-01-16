package models.custom_mapper;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.bson.types.ObjectId;
import play.Logger;

import java.io.IOException;

/**
 * Created by n.diazgranados on 07/01/2015.
 */

//http://www.baeldung.com/jackson-custom-serialization
public class IdSerializer extends JsonSerializer<String> {

    @Override
    public void serialize(String value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        Logger.info("Serialize"+  value);
        Logger.info("Serialize" + jgen);
        jgen.writeObject(new ObjectId(value));
    }
}


