package models.dao;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mongodb.WriteResult;
import models.db.JongoManager;
import models.db.JongoManager.RetrieveOperator;
import org.jongo.MongoCollection;

import java.util.Iterator;
import java.util.Map;

import static models.db.JongoManager.JongoOperations;

/**
 * Created by n.diazgranados on 27/07/2015.
 */

//http://www.davismol.net/2015/03/05/jackson-json-deserialize-a-list-of-objects-of-subclasses-of-an-abstract-class/
/*@JsonTypeInfo(use= JsonTypeInfo.Id.NAME, include=JsonTypeInfo.As.PROPERTY, property="subClass")
@JsonSubTypes({
        @JsonSubTypes.Type(value = User.class, name = "user"),
        @JsonSubTypes.Type(value = Plan.class, name = "plan") })
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)*/
public abstract class BaseObject implements JongoOperations {

    @JsonIgnore public abstract MongoCollection getCollection();

    @Override
    public Object insert() {
        return JongoManager.insert(getCollection(), this);
    }

    @Override
    public WriteResult update(String id) {
        return JongoManager.update(getCollection(), this, id);
    }

    public static Iterator get(MongoCollection collection,Class objectClass,
                               Map<String, String> paramList,
                               Map<String, Object> paramListObject,
                               Map<String, RetrieveOperator> paramListOperator) {
        return JongoManager.get(collection, objectClass, paramList, paramListObject,paramListOperator );
    }

    @Override
    public WriteResult remove() {
        return JongoManager.remove(getCollection(), this);
    }
}
