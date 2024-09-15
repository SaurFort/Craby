package fr.saurfort.migrator;

import fr.saurfort.migrator.v211.V211Updater;

public class MigratorController {
    public MigratorController(String version) {
        if(version.equals(VersionHistory.v2_1_1.getTextVersion())) {
            new V211Updater();
        }

        System.out.println("Program updated to the next version, please restart.");
        System.exit(0);
    }
}
