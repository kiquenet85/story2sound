package controllers;

import models.Global;
import models.dao.User;
import org.bson.types.ObjectId;
import org.jongo.MongoCollection;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

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

    public static Result getUser(String name)
    {
        Iterator<User> all = collection.find("{name:#}",name).as(User.class).iterator();
        return all == null ? notFound() : ok(Json.toJson(all));
    }

    public static Result createUser()
    {
        User newUser = Json.fromJson(request().body().asJson(), User.class);
        ObjectId userId = (ObjectId) newUser.insert();
        return  userId == null ? notFound() : created(Json.toJson(newUser));
    }

    public static Result updateUser(Long id)
    {
        User updatedUser = Json.fromJson(request().body().asJson(), User.class);
        collection.update("{_id:#}", new ObjectId(String.valueOf(id))).upsert().multi().with(updatedUser);
        return ok(Json.toJson(updatedUser));
    }

    public static Result deleteUser(Long id)
    {
        collection.remove("{_id:#}", new ObjectId(String.valueOf(id)));
        return noContent(); // http://stackoverflow.com/a/2342589/1415732
    }
}
