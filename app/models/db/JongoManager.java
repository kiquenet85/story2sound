package models.db;

import com.mongodb.WriteResult;
import models.Global;
import models.dao.MongoId;
import models.util.DateUtil;
import org.bson.types.ObjectId;
import org.jongo.MongoCollection;
import play.Logger;

import java.util.*;

/**
 * Created by n.diazgranados on 27/07/2015.
 */
public abstract class JongoManager {

    public static final String AVOID_EMPTY_QUERY =  "{LASDFJKERFI : '09087ASDJIFJKSD'}";
     /*
     * The Entities that implements this interface will be able to
     * access simple Mongo operations as Create, Retrieve, Update, Delete.
     */
    public interface JongoOperations {
        String COL_ID= "_id";
        Object insert();
        WriteResult remove();
        WriteResult update(String id);
        MongoId get_id();
    }

    public static MongoCollection getCollection(String name) {
        return Global.getMongoDB().getCollection(name);
    }

    public static Object insert(MongoCollection collection, JongoOperations object) {
        Logger.info("collection: "+ collection.getName());
        WriteResult result = collection.save(object);
        if (result.isUpdateOfExisting()) {
            Logger.info("UPSERTED ON  INSERT" + result.getUpsertedId() + " AFFECTED RECORDS: " + result.getN());
            return result.getUpsertedId();
        }
        Logger.info("INSERT AFFECTED RECORDS: " + result.getN());
        return null;
    }

    public static WriteResult update(MongoCollection collection, JongoOperations object, String id) {
        Logger.info("UPDATE"+"{" + object.COL_ID + ":#}"+ id);
        return collection.update("{" + object.COL_ID + ":#}", new ObjectId(id)).upsert().multi().with(object);
    }


    public static WriteResult remove(MongoCollection collection, JongoOperations object) {
        Logger.info("REMOVE"+"{" + object.COL_ID + ":#}"+ object.get_id().get$oid());
        return collection.remove("{" + object.COL_ID + ":#}", new ObjectId(object.get_id().get$oid()));
    }


    public enum RetrieveOperator {
        TEXT("COL? : { $regex : VAR? }","TEXT"),
        EXACT_VALUE_TEXT("COL? : 'VAR?'","EXACT_VALUE"),
        EXACT_VALUE_NUM("COL? : VAR?","EXACT_VALUE"),
        LESS_THAN("COL? : { $lt : VAR?}","LESS_THAN"),
        GREATER_OR_EQUAL_THAN("COL? : { $gte : VAR?}","GREATER_OR_EQUAL_THAN"),
        RANGE("COL? : { $gte : VAR?, $lt : VAR?}","RANGE");

        private String stringFormat;
        private String name;

        RetrieveOperator(String stringFormat, String name){
            this.stringFormat = stringFormat;
            this.name = name;
        }

        public String getStringFormat(String column, String var){
            String formattedString = new String(stringFormat);
            formattedString = formattedString.replace("COL?", column);
            formattedString = formattedString.replaceAll("VAR\\?", var);
            return formattedString;
        }

        public String getName(){
           return name;
        }
    }

    public static RetrieveOperator getOperator(String name){
        for (RetrieveOperator operator : RetrieveOperator.values()){
            if (operator.getName().equals(name)) return operator;
        }
        return null;
    }

    public static Iterator get(MongoCollection collection,Class objectClass,
                               Map<String,String> paramList,
                               Map<String,Object> paramListObject,
                               Map<String,RetrieveOperator> paramListOperator) {

        Map<String, String> formattedValues = new HashMap<>();
        List<Date> dateList = new ArrayList<>();
        List<String> patternList = new ArrayList<>();

        for (String columnValue : paramList.keySet()) {
            String stringValue = paramList.get(columnValue);
            Object objectValue = paramListObject.get(columnValue);
            RetrieveOperator retrieveOperator = getParamRetrieveOperator(objectValue, paramListOperator);
            if (objectValue instanceof Date) {
                //Plan.COL_INIT_DATE" : { "$gte" : new ISODate("2014-07-02T00:00:00Z") } does not work on Jongo  0.8
                formattedValues.put(columnValue, retrieveOperator.getStringFormat(columnValue, "#"));
                dateList.add((Date) objectValue);
                if (retrieveOperator.equals(RetrieveOperator.RANGE))// Adding The end Date range +10 millsec
                    dateList.add(DateUtil.addMillisecond((new Date(((Date) objectValue).getTime())),10000));
            }else if (objectValue instanceof String) {
                formattedValues.put(columnValue, retrieveOperator.getStringFormat(columnValue, "#"));
                patternList.add((String) objectValue);
            }else {//Values as Integers are set directly on query
                formattedValues.put(columnValue, retrieveOperator.getStringFormat(columnValue, stringValue));
            }
        }

        String mongoQueryString = createQueryStringInOrder(formattedValues, paramListObject);
        return executeQueryInOrder(collection,objectClass, mongoQueryString, dateList,patternList);
    }

    public static RetrieveOperator getParamRetrieveOperator(Object param, Map<String, RetrieveOperator> specificOperator){
        if (param instanceof String){
            return (specificOperator.get(String.class.getName()) == null)
                    ? RetrieveOperator.TEXT : specificOperator.get(String.class.getName());
        }else if (param instanceof Date ) {
            return (specificOperator.get(Date.class.getName()) == null)
                    ? RetrieveOperator.RANGE : specificOperator.get(Date.class.getName());
        }else if (param instanceof Integer ) {
            return (specificOperator.get(Integer.class.getName()) == null)
                    ? RetrieveOperator.EXACT_VALUE_NUM : specificOperator.get(Date.class.getName());
        }else {
            Logger.info("NO TYPE FOUND");
        }
        return null;  //TODO Create exception for not supportedTypes
    }

    /*
    * Order the query on this way:
    * 1. Date types.
    * 2. String Types.
    *
    * When creating dynamic Jongo query is possible to send several types as parameters creating
    * collections Collection<E> of each type, this will means that also parameters must be send in the correct order
    * when executing the query. See {@link MongoManager#executeQueryInOrder}
    *
    */
    public static String createQueryStringInOrder (Map<String,String> values,Map<String,Object> paramListObject){
        String mongoQueryString = "{";
        String queryDate = "", queryText = "";
        for (String key: values.keySet()){
            if (paramListObject.get(key) instanceof Date) {
                queryDate += (queryDate.length() > 1) ? "," + values.get(key) : values.get(key);
            }else{
                queryText += (queryText.length() > 1) ? "," + values.get(key) : values.get(key);
            }
        }
        mongoQueryString += (queryDate.length()>0 && queryText.length()>0)
                            ? queryDate+","+queryText : (queryDate.length()>0) ? queryDate : queryText;
        mongoQueryString += "}";
        Logger.info("QUERY:" + mongoQueryString);

        return mongoQueryString;
    }


    /*
    * Execute a order query created with {@link MongoManager#createQueryStringInOrder()}
    *
    * When creating dynamic Jongo query is possible to send several types as parameters creating
    * collections Collection<E> of each type, this will means that also parameters must be send in the correct order
    * when executing the query. See {@link MongoManager#executeQueryInOrder}
    *
    */
    public static Iterator executeQueryInOrder(MongoCollection collection,Class objectClass,
                                               String mongoQueryString,
                                               List<Date> dateList,List<String> patternList ){

        //1. First parameter are Dates DateList
        //2. Second parameter are String(the text will act as REGEX patterns) patternList

        // collection.find({}) is equivalent to collection.find() DANGER!!
        if (mongoQueryString.equals("{}")){
            mongoQueryString = AVOID_EMPTY_QUERY;
        }

        //Its crucial to call collection.toArray() on each parameter list.
        if (dateList.isEmpty() && patternList.isEmpty()) {
            return collection.find(mongoQueryString).as(objectClass).iterator();
        } else if (!dateList.isEmpty() && !patternList.isEmpty()) {
            return collection.find(mongoQueryString,dateList.toArray(),patternList.toArray()).as(objectClass).iterator();
        } else if (!dateList.isEmpty()) {
            return collection.find(mongoQueryString,dateList.toArray()).as(objectClass).iterator();
        } else if (!patternList.isEmpty()) {
            return collection.find(mongoQueryString,patternList.toArray()).as(objectClass).iterator();
        }
         return null;
    }
}
