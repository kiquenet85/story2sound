package controllers;

import play.mvc.Controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static models.db.JongoManager.RetrieveOperator;
import static models.db.JongoManager.getOperator;

/**
 * Created by n.diazgranados on 01/08/2015.
 */
public abstract class BaseController extends Controller {

    public static Map<String, RetrieveOperator> createListParamOperators(String dateOp, String textOp){
        Map<String,RetrieveOperator> paramListOperator = new HashMap<>();

        if (dateOp != null){
            paramListOperator.put(Date.class.getName(), getOperator(dateOp));
        }

        if (textOp != null){
            paramListOperator.put(String.class.getName(), getOperator(textOp));
        }

        return paramListOperator;
    }
}
