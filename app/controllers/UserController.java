package controllers;

import models.Global;
import models.dao.User;
import org.bson.types.ObjectId;
import org.jongo.MongoCollection;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

import java.util.Date;
import java.util.Iterator;

/**
 * Created by n.diazgranados on 06/01/2015.
 */
public class UserController extends Controller {

    private static MongoCollection collection= Global.getMongoDB().getCollection(User.COLLECTION_NAME);

    public static Result getUsers()
    {
        Iterator<User> all = collection.find().as(User.class).iterator();
        return all == null ? notFound() : ok(Json.toJson(all));
    }

    public static Result getUser(String id, String name, String date)
    {
        Iterator<User> all=null;
        if (id!=null){
            User user = collection.findOne(new ObjectId(id)).as(User.class);
            return user == null ? notFound() : ok(Json.toJson(user));
        }else if(date!=null) {
            all = collection.find("{"+User.COL_TIMESTAMP+": {$lt : #}}", new Date(Long.valueOf(date))).as(User.class).iterator();
        }else if(name!=null) {
            all = collection.find("{"+User.COL_NAME+":#}",name).as(User.class).iterator();

        }
        return all == null ? notFound() : ok(Json.toJson(all));
    }

    @BodyParser.Of(BodyParser.Json.class)
    public static Result createUser()
    {
        Http.RequestBody body = request().body();
        User newUser = Json.fromJson(body.asJson(), User.class);
        ObjectId userId = (ObjectId) newUser.insert();
        return  userId == null ? ok("ok, I received CREATE data. That's all...") : created(Json.toJson(newUser));
    }

    @BodyParser.Of(BodyParser.Json.class)
    public static Result updateUser(String id)
    {
        User updatedUser = Json.fromJson(request().body().asJson(), User.class);
        collection.update("{"+User.COL_ID+":#}", new ObjectId(id)).upsert().multi().with(updatedUser);
        return ok(Json.toJson(updatedUser));
    }

    public static Result deleteUser(String id)
    {
        collection.remove("{"+User.COL_ID+":#}", new ObjectId(id));
        return ok("ok, I received DELETE data. That's all..."); // http://stackoverflow.com/a/2342589/1415732
    }
}
