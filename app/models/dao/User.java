package models.dao;

import com.mongodb.WriteResult;
import models.Global;
import org.bson.types.ObjectId;
import org.jongo.MongoCollection;
import org.jongo.marshall.jackson.oid.Id;

/**
 * Created by user on 04/01/2015.
 */
public class User {

    public static final String COLLECTION_NAME= "users";
    public static MongoCollection users() {
        return Global.getMongoDB().getCollection(COLLECTION_NAME);
    }

    @Id
    private ObjectId id;

    private String name;

    public User(String name) {
        this.name = name;
    }

    public User(ObjectId id) {
        this.id = id;
    }

    public User (){}

    public Object insert() {
        WriteResult result = users().save(this);
        if (result.isUpdateOfExisting())
            return result.getUpsertedId();
        return null;
    }

    public void remove() {
        users().remove(this.id);
    }

    public static User findByName(String name) {
        return users().findOne("{name: #}", name).as(User.class);
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
