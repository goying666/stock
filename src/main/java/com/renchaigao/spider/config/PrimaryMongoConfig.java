//package com.renchaigao.spider.config;
//
//import com.mongodb.client.MongoClient;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;
//import org.springframework.data.mongodb.MongoDbFactory;
//import org.springframework.data.mongodb.core.MongoTemplate;
//import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
//
//@Configuration
//@ConfigurationProperties(prefix="spring.data.mongodb.primary")
//public class PrimaryMongoConfig extends AbstractMongoConfig {
//
//    protected static final String MONGO_TEMPLATE = "primaryMongoTemplate";
//
//    /**
//     * 本地mongodb不需要密码，用这个
//     */
//    public MongoDbFactory mongoDbFactory(){
//        return new SimpleMongoDbFactory(new MongoClient(host, port), database);
//    }
//
//    @Primary
//    @Bean(name="primaryMongoTemplate")
//    @Override
//    public MongoTemplate getMongoTemplate() {
//        return new MongoTemplate(mongoDbFactory());
//    }
//}
