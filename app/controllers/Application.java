package controllers;

import models.dao.User;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;

public class Application extends Controller {

    public static Result index() {
        String message = flash("message");
        if(message == null) {
            message = "Nes Dupier";
        }
        return ok(index.render(message));
    }

    public static Result testMongoInsert(){

        User user=new User("Nestor");
        user.insert();

        user=new User("Mike");
        user.insert();

        user=new User("Tati");
        user.insert();

        user=User.findByName("Mike");

        flash("message", user.getName());
        return redirect(controllers.routes.Application.index());
    }

}
