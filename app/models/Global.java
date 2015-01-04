package models;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import models.db.MyMongoClientFactory;
import org.jongo.Jongo;
import play.Application;
import play.Configuration;
import play.GlobalSettings;
import play.Logger;

/**
 * Created by user on 04/01/2015.
 */
public class Global extends GlobalSettings {

    private static DB mongoDB;
    private static Jongo jongoDB;
    public static final String DB_NAME="s2s";

    @Override
    public void onStart(Application app) {
        Logger.info("Application has started");
        initDB();
    }

    @Override
    public void onStop(Application app) {
        Logger.info("Application shutdown...");
    }
    public static Jongo getMongoDB() {
        return jongoDB;
    }

    private static void initDB() {
        Configuration configuration = play.Configuration.root();
        MyMongoClientFactory mongoFactory = new MyMongoClientFactory(configuration);
        MongoClient mongoClient = null;
        try {
            mongoClient = mongoFactory.createClient();
        } catch (Exception e) {
            e.printStackTrace();
        }

        mongoDB = mongoClient.getDB(DB_NAME);
        jongoDB = new Jongo(mongoDB);
    }
}
