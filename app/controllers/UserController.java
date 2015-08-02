package controllers;

import models.dao.MongoId;
import models.dao.User;
import models.util.DateUtil;
import org.bson.types.ObjectId;
import org.jongo.MongoCollection;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Http;
import play.mvc.Result;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static models.db.JongoManager.RetrieveOperator;
import static models.db.JongoManager.getCollection;

/**
 * Created by n.diazgranados on 06/01/2015.
 */
public class UserController extends BaseController {

    private static MongoCollection collection= getCollection(User.COLLECTION_NAME);

    public static Result getAll()
    {
        Iterator<User> all = collection.find().as(User.class).iterator();
        return all == null ? notFound() : ok(Json.toJson(all));
    }

    public static Result get(String id, String name, String timestamp,
                             String dateOp, String textOp){
        Iterator<User> all = null;
        if (id != null) {
            User user = collection.findOne(new ObjectId(id)).as(User.class);
            return user == null ? notFound() : ok(Json.toJson(user));
        } else {
            Map<String, String> paramListString = new HashMap<>();
            Map<String,Object> paramListObject = new HashMap<>();

            //1.  Find specific operators
            Map<String,RetrieveOperator> paramListOperator = createListParamOperators(dateOp,textOp);

            //2. Build Dates
            if (timestamp != null) {
                paramListString.put(User.COL_TIMESTAMP, timestamp);
                paramListObject.put(User.COL_TIMESTAMP, DateUtil.buildISO8601Date(timestamp));
            }

            //3. Build other parameters
            if (name != null) {
                paramListString.put(User.COL_NAME, name);
                paramListObject.put(User.COL_NAME, name);
            }
            all = User.get(collection, User.class,
                    paramListString, paramListObject, paramListOperator);
        }
        return all == null ? notFound() : ok(Json.toJson(all));
    }

    @BodyParser.Of(BodyParser.Json.class)
    public static Result create()
    {
        Http.RequestBody body = request().body();
        User newUser = Json.fromJson(body.asJson(), User.class);
        ObjectId userId = (ObjectId) newUser.insert();
        return  userId == null ? ok("ok, I received CREATE data. That's all...") : created(Json.toJson(newUser));
    }

    @BodyParser.Of(BodyParser.Json.class)
    public static Result update(String id)
    {
        User objectToUpdate = Json.fromJson(request().body().asJson(), User.class);
        objectToUpdate .update(id);
        return ok(Json.toJson(objectToUpdate));
    }

    public static Result delete(String id)
    {
        User user = new User();
        user.set_id(new MongoId(id));
        user.remove();
        return ok("ok, I received DELETE data. That's all..."); // http://stackoverflow.com/a/2342589/1415732
    }
}
