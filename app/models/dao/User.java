package models.dao;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import models.db.JongoManager;
import org.jongo.MongoCollection;

import java.util.Date;
import java.util.List;

/**
 * Created by n.diazgranados on 04/01/2015.
 */
//@JsonTypeInfo(use = JsonTypeInfo.Id.NONE
//@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class User extends BaseObject {

    public static final String COL_ID = "_id";
    public static final String COLLECTION_NAME= "users";
    public static final String COL_PASS= "password";
    public static final String COL_NAME= "name";
    public static final String COL_TIMESTAMP= "timestamp";

    private MongoId _id;
    private String name;
    //@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss.sss'Z'", timezone="UTC-5")
    private Date timestamp;
    private String password;
    private String email;
    private List<Integer> celNumbers;
    private int celNumber;
    private List<Integer> phoneNumbers;
    private int phoneNumber;
    private List<String> addresses;
    private String address;


    public User(String name, String password) {
        this.name = name;
        this.password=password;
    }

    public User (){
    }

    @Override
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

    @JsonIgnore public String getPassword() {
        return password;
    }

    public void setPasswword(String password) {
        this.password = password;
    }

    @Override
    public MongoCollection getCollection() {
        return JongoManager.getCollection(COLLECTION_NAME);
    }
}
