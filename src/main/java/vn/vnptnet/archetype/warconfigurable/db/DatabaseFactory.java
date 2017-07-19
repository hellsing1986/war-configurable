package vn.vnptnet.archetype.warconfigurable.db;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.commons.configuration2.Configuration;
import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.Handle;

import java.util.HashMap;
import java.util.List;

/**
 * Created by User on 5/30/2017.
 */
public class DatabaseFactory {
    private static DatabaseFactory instance = new DatabaseFactory();
    public static DatabaseFactory getInstance(){
        return instance;
    }
    //private HikariDataSource hikariDataSource;
    //private GroupedProperties props = null;
    private HashMap<String, HikariDataSource> mapDatasource = new HashMap<String, HikariDataSource>();

    private DatabaseFactory(){  }
    public void addDataSource(Configuration dbProperties){
        addDataSource(dbProperties.getString("name"),dbProperties);
    }
    public void addDataSource(String dbName, Configuration dbProperties){
        String type = dbProperties.getString("type");
        System.out.println("init db "+dbName);
        if(type.equalsIgnoreCase("OracleDataSource")){
            HikariConfig config = new HikariConfig();
            config.setMaximumPoolSize(dbProperties.getInt("maximumPoolSize"));
            config.setDataSourceClassName(dbProperties.getString("className"));
            config.addDataSourceProperty("driverType",dbProperties.getString("driverType"));
            config.addDataSourceProperty("serverName",dbProperties.getString("serverName"));
            config.addDataSourceProperty("portNumber", dbProperties.getString("portNumber"));
            config.addDataSourceProperty("databaseName", dbProperties.getString("databaseName"));
            config.addDataSourceProperty("user", dbProperties.getString("user"));
            config.addDataSourceProperty("password", dbProperties.getString("password"));
            HikariDataSource hikariDataSource = new HikariDataSource(config);
            mapDatasource.put(dbName.toUpperCase(),hikariDataSource);
        }else if(type.equalsIgnoreCase("OracleDriver")){
            HikariConfig config = new HikariConfig();
            config.setDriverClassName(dbProperties.getString("className"));
            config.setJdbcUrl(dbProperties.getString("url"));
            config.setUsername(dbProperties.getString("user"));
            config.setPassword(dbProperties.getString("password"));
            config.setMaximumPoolSize(dbProperties.getInt("maximumPoolSize"));
            HikariDataSource hikariDataSource = new HikariDataSource(config);
            mapDatasource.put(dbName.toUpperCase(),hikariDataSource);
        }
    }

    public DBI getDBI(String dbName){
        if(mapDatasource.containsKey(dbName.toUpperCase())) {
            DBI dbi = new DBI(mapDatasource.get(dbName.toUpperCase()));
            return dbi;
        }else{
            return null;
        }
    }

    public Handle openHandler(String dbName){
        if(mapDatasource.containsKey(dbName.toUpperCase())) {
            DBI dbi = new DBI(mapDatasource.get(dbName.toUpperCase()));
            return dbi.open();
        }else{
            return null;
        }
    }
}
