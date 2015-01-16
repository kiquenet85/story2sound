package models.dao;

import com.mongodb.WriteResult;
import models.Global;
import org.jongo.MongoCollection;

import java.util.Date;

/**
 * Created by n.diazgranados on 04/01/2015.
 */
public class User {

    public static final String COLLECTION_NAME= "users";
    public static final String COL_ID= "_id";
    public static final String COL_PASS= "password";
    public static final String COL_NAME= "name";
    public static final String COL_TIMESTAMP= "timestamp";

    public static MongoCollection users() {
        return Global.getMongoDB().getCollection(COLLECTION_NAME);
    }

    private MongoId _id;
    private String name;
    private Date timestamp;
    private String password;

    public User(String name, String password) {
        this.name = name;
        this.password=password;
    }

    public User (){}

    public Object insert() {
        WriteResult result = users().save(this);
        if (result.isUpdateOfExisting()) {
            return result.getUpsertedId();
        }
        return null;
    }

    public void remove() {  users().remove(this._id.get$oid());  }

    public MongoId get_id() { return _id; }

    public void set_id(MongoId _id) { this._id = _id; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public static User findByName(String name) {
        return users().findOne("{"+COL_NAME+": #}", name).as(User.class);
    }
}
