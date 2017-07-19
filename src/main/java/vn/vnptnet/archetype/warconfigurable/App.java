package vn.vnptnet.archetype.warconfigurable;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.HierarchicalConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.XMLConfiguration;
import org.apache.commons.configuration2.builder.ConfigurationBuilderEvent;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.ReloadingFileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.convert.DefaultListDelimiterHandler;
import org.apache.commons.configuration2.event.ConfigurationEvent;
import org.apache.commons.configuration2.event.Event;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.configuration2.reloading.PeriodicReloadingTrigger;
import org.apache.commons.configuration2.tree.ImmutableNode;
import org.glassfish.jersey.server.ResourceConfig;
import vn.vnptnet.archetype.warconfigurable.db.DatabaseFactory;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by User on 6/23/2017.
 */
public class App extends ResourceConfig{
    private static ReloadingFileBasedConfigurationBuilder<PropertiesConfiguration> builder = null;
    public static ReloadingFileBasedConfigurationBuilder<PropertiesConfiguration> getBuilder(){
        return builder;
    }
    public App(){
        packages("vn.vnptnet.archetype.warconfigurable.rest");
        setupAppConfiguration();
        setupDatabaseConfiguration();
    }
    public void setupAppConfiguration(){
        System.out.println("ApplicationProperties "+this.getClass().getClassLoader().getResource("app.properties").getPath());
        //this.getClass().getResource("app.properties").getPath();
        String configFilePath = this.getClass().getClassLoader().getResource("app.properties").getPath();
        builder =
                new ReloadingFileBasedConfigurationBuilder<PropertiesConfiguration>(PropertiesConfiguration.class)
                        .configure(new Parameters().properties()
                                .setFileName(configFilePath)
                                .setThrowExceptionOnMissing(true)
                                .setListDelimiterHandler(new DefaultListDelimiterHandler('|'))
                                .setIncludesAllowed(false));
        //configuration = builder.getConfiguration();
        /*builder.addEventListener(ConfigurationBuilderEvent.CONFIGURATION_REQUEST,
                (Event event)->{
                    boolean needed = builder.getReloadingController().checkForReloading(null);
                    System.out.println(needed?"need reload":"no need reload");
                });*/
        PeriodicReloadingTrigger trigger = new PeriodicReloadingTrigger(builder.getReloadingController(),
                null, 1, TimeUnit.SECONDS);
        trigger.start();
    }
    public void setupDatabaseConfiguration(){
        String configFilePath = this.getClass().getClassLoader().getResource("databases.xml").getPath();
        Parameters params = new Parameters();
        FileBasedConfigurationBuilder<XMLConfiguration> builder =
                new FileBasedConfigurationBuilder<XMLConfiguration>(XMLConfiguration.class)
                        .configure(params.xml()
                                .setFileName(configFilePath));
        try
        {
            XMLConfiguration dbConfig = builder.getConfiguration();
            List<HierarchicalConfiguration<ImmutableNode>> databases = dbConfig.configurationsAt("database");
            //System.out.println("db count = "+databases.size());
            for (HierarchicalConfiguration sub : databases) {
                DatabaseFactory.getInstance().addDataSource(sub);
            }
        }
        catch(ConfigurationException cex)
        {
            // loading of the configuration file failed
            cex.printStackTrace();
        }
    }
}
