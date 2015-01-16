package controllers;

import models.dao.User;
import play.data.DynamicForm;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;

import java.util.Date;

import static play.data.Form.form;

public class Application extends Controller {

    public static Result index() {
        String message = flash("message");
        if(message == null) {
            message = "Nes Dupier";
        }
        return ok(index.render(message));
    }

    public static Result loginSubmit(){

        DynamicForm dynamicForm = form().bindFromRequest();
        User user= new User(dynamicForm.get("username"),dynamicForm.get("password"));
        user.setTimestamp(new Date());
        user.insert();
        return ok("ok, I received POST data. That's all...");
    }

}
