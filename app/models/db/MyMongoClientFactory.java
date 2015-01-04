package models.db;

/**
 * Created by n.diazgranados on 03/01/2015.
 */

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;
import play.Configuration;
import uk.co.panaxiom.playjongo.MongoClientFactory;

import java.util.Arrays;

public class MyMongoClientFactory extends MongoClientFactory {
    private Configuration config;

    public MyMongoClientFactory(Configuration config) {
        super(config);
        this.config = config;
    }

    public MongoClient createClient() throws Exception {
        MongoClientOptions options = MongoClientOptions.builder()
                .connectionsPerHost(100)
                .maxConnectionIdleTime(60000)
                .build();

        return new MongoClient(Arrays.asList(
                new ServerAddress("localhost", 27017)/*,
                new ServerAddress("localhost", 27018),
                new ServerAddress("localhost", 27019)*/),
                options);
    }

    public String getDBName() {
        return config.getString("myappconfig.dbname");
    }

}