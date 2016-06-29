package io.summerx.framework.data.mongodb;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

public class MorphiaDatastoreFactoryBean implements FactoryBean<Datastore>, InitializingBean {

    // mongodb://username:password@10.6.54.227:7030/mongodb?xxx=xxx
    private String mongoUrl;

    private String dbname;

    private Datastore datastore;

    @Override
    public void afterPropertiesSet() throws Exception {
        Morphia morphia = new Morphia();
        MongoClient mongoClient = new MongoClient(new MongoClientURI(mongoUrl));
        datastore = morphia.createDatastore(mongoClient, dbname);
    }

    @Override
    public Datastore getObject() throws Exception {
        return datastore;
    }

    @Override
    public Class<?> getObjectType() {
        return Datastore.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    public void setMongoUrl(String mongoUrl) {
        this.mongoUrl = mongoUrl;
    }

    public void setDbname(String dbname) {
        this.dbname = dbname;
    }
}
