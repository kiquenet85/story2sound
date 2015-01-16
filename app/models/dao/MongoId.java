package models.dao;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import models.custom_mapper.MongoIdDeserializer;

/**
 * Created by n.diazgranados on 09/01/2015.
 */

@JsonDeserialize(using = MongoIdDeserializer.class)
public class MongoId {

    private String $oid;
    public MongoId() {}

    public MongoId(String $oid) {
        this.$oid = $oid;
    }

    public String get$oid() {
        return $oid;
    }

    public void set$oid(String $oid) {
        this.$oid = $oid;
    }
}
