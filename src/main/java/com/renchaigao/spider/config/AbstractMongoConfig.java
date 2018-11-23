//package com.renchaigao.spider.config;
//
//import com.mongodb.MongoClient;
//import com.mongodb.MongoCredential;
//import com.mongodb.ServerAddress;
//import lombok.Getter;
//import lombok.Setter;
//import org.springframework.data.mongodb.MongoDbFactory;
//import org.springframework.data.mongodb.core.MongoTemplate;
//import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
//
//import java.util.ArrayList;
//import java.util.List;
//@Getter
//@Setter
//public abstract class AbstractMongoConfig {
//    protected String host;
//    protected int port;
//    protected String username;
//    protected String password;
//    protected String database;
//    public MongoDbFactory mongoDbFactory(){
//        ServerAddress serverAddress = new ServerAddress(host, port);
//        List<MongoCredential> credentialsList = new ArrayList<MongoCredential>();
//        credentialsList.add(MongoCredential.createCredential(username, database, password.toCharArray()));
//        return new SimpleMongoDbFactory(new MongoClient(serverAddress, credentialsList),database);
//    }
//    public abstract MongoTemplate getMongoTemplate();
//}
