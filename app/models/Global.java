package models;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.mongodb.DB;
import com.mongodb.MongoClient;
import models.custom_mapper.MongoIdDeserializer;
import models.dao.MongoId;
import models.db.MyMongoClientFactory;
import org.jongo.Jongo;
import org.jongo.marshall.jackson.JacksonMapper;
import org.jongo.marshall.jackson.configuration.MapperModifier;
import play.Application;
import play.Configuration;
import play.GlobalSettings;
import play.Logger;
import play.libs.Json;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by n.diazgranados on 04/01/2015.
 */
public class Global extends GlobalSettings {

    private static DB mongoDB;
    private static Jongo jongoDB;
    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    public static final TimeZone DEFAULT_TIME_ZONE = TimeZone.getTimeZone("GMT-5");

    @Override
    public void onStart(Application app) {
        Logger.info("Story2Sound has started");
        ObjectMapper mapper = new ObjectMapper();
                //.enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS)
                //.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        // etc.
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        DateFormat dateFormat = new SimpleDateFormat(Global.DEFAULT_DATE_FORMAT);
        dateFormat.setTimeZone(Global.DEFAULT_TIME_ZONE);
        mapper.setLocale(Locale.forLanguageTag("es-Co"));
        mapper.setDateFormat(dateFormat);
        Json.setObjectMapper(mapper);
        initDB();
    }

    @Override
    public void onStop(Application app) {
        Logger.info("Story2Sound shutdown...");
    }
    public static Jongo getMongoDB() {
        return jongoDB;
    }

    private void initDB() {
        Configuration configuration = play.Configuration.root();
        MyMongoClientFactory mongoFactory = new MyMongoClientFactory(configuration);
        MongoClient mongoClient = null;
        try {
            mongoClient = mongoFactory.createClient();
        } catch (Exception e) {
            e.printStackTrace();
        }

        CustomObjectMapper customMapper=new CustomObjectMapper();
        mongoDB = mongoClient.getDB(mongoFactory.getDBName());
        jongoDB = new Jongo(mongoDB,new JacksonMapper.Builder().addModifier(customMapper).build());
    }


    public class CustomObjectMapper implements MapperModifier {

        @Override
        public void modify(ObjectMapper objectMapper) {
           /* SimpleModule module = new SimpleModule("ObjectIdmoduleSerialize");
            module.addSerializer(MongoId.class, new IdSerializer());
            objectMapper.registerModule(module);

            SimpleModule moduleDes = new SimpleModule("ObjectIdmoduleDeserialize");
            moduleDes.addDeserializer(MongoId.class, new IdDeserializer());
            objectMapper.registerModule(moduleDes);  */

            SimpleModule moduleMongoId = new SimpleModule();
            moduleMongoId.addDeserializer(MongoId.class, new MongoIdDeserializer());
            objectMapper.registerModule(moduleMongoId);
        }
    }
}
