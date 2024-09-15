package fr.saurfort.migrator.v211;

import fr.saurfort.Craby;
import fr.saurfort.config.ConfigLoader;
import fr.saurfort.config.ConfigUpdater;
import fr.saurfort.migrator.v211.database.mysql.MySQLUpdater;

public class V211Updater {
    private ConfigLoader config = Craby.getConfig();

    public V211Updater() {
        new MySQLUpdater(config.getProperty("db.username"), config.getProperty("db.password"), config.getProperty("db.address"), config.getProperty("db.port"), config.getProperty("db.name"));
        new ConfigUpdater().updateVersion("2.2.0");
    }
}
