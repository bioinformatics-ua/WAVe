package pt.ua.bioinformatics.wave.services;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author pedrolopes
 * @version 1.2, 2010-12-17
 */
public class Settings {

    static SimpleXML settingsFile = null;
    static boolean loaded = false;  // Settings load status
    static String version = "";     // WAVe version
    static String dbServer = "";    // Database connection server for Services.DB
    static String dbUser = "";      // Database username for Services.DB
    static String dbPassword = "";  // Database username password for Services.DB
    static String dbBuild = "";     // Database build number for Services.DB
    static String host = "";        // WAVe host for API
    static String index = "";       // index nodes
    static HashMap<String, Boolean> modules = new HashMap();
    static String path = "";

    public static String getPath() {
        return path;
    }

    public static void setPath(String path) {
        Settings.path = path;
    }

    public static String getIndex() {
        return index;
    }

    public static void setIndex(String index) {
        Settings.index = index;
    }

    public static String getHost() {
        return host;
    }

    public static void setHost(String host) {
        Settings.host = host;
    }

    public static HashMap<String, Boolean> getModules() {
        return modules;
    }

    public static void setModules(HashMap<String, Boolean> modules) {
        Settings.modules = modules;
    }

    public static String getDbPassword() {
        return dbPassword;
    }

    public static void setDbPassword(String dbPassword) {
        Settings.dbPassword = dbPassword;
    }

    public static String getDbServer() {
        return dbServer;
    }

    public static void setDbServer(String dbServer) {
        Settings.dbServer = dbServer;
    }

    public static String getDbUser() {
        return dbUser;
    }

    public static void setDbUser(String dbUser) {
        Settings.dbUser = dbUser;
    }

    public static boolean isLoaded() {
        return loaded;
    }

    public static void setLoaded(boolean loaded) {
        Settings.loaded = loaded;
    }

    public static SimpleXML getSettingsFile() {
        return settingsFile;
    }

    public static void setSettingsFile(SimpleXML settingsFile) {
        Settings.settingsFile = settingsFile;
    }

    public static String getDbBuild() {
        return dbBuild;
    }

    public static void setDbBuild(String dbBuild) {
        Settings.dbBuild = dbBuild;
    }

    public static String getVersion() {
        return version;
    }

    public static void setVersion(String version) {
        Settings.version = version;
    }

    public static boolean load() {
        try {
            path = Settings.class.getResource("/").getPath();  
            settingsFile = new SimpleXML(path + "settings.xml");
            version = settingsFile.selectNode("//version");
            dbServer = settingsFile.selectNode("//database/connection/server");
            dbUser = settingsFile.selectNode("//database/connection/username");
            dbPassword = settingsFile.selectNode("//database/connection/password");
            dbBuild = settingsFile.selectNode("//database/build");
            index = settingsFile.selectNode("//database/index");
            host = settingsFile.selectNode("//host");

            NodeList moduleList = settingsFile.selectNodes("//module");
            for (int i = 0; i < moduleList.getLength(); i++) {
                Node n = moduleList.item(i);
                // what a mess to get a module name and enabled...
                String name = n.getChildNodes().item(0).getChildNodes().item(0).getNodeValue();
                boolean enabled = Boolean.parseBoolean(n.getChildNodes().item(1).getChildNodes().item(0).getNodeValue());
                modules.put(name, enabled);
            }
            loaded = true;
        } catch (Exception ex) {
            System.out.println("[Settings] unable to load WAVe Settings " + ex.toString());
        }
        return loaded;
    }

    public static int getVariants() {
        int v = 0;
        try {
            DB db = API.getDb();
            db.connect();
            ResultSet rs = db.getData("SELECT COUNT(*) AS num FROM wave#build#_variant");
            while (rs.next()) {
                v = rs.getInt("num");
            }
        } catch (SQLException ex) {
            Logger.getLogger(Settings.class.getName()).log(Level.SEVERE, null, ex);
        }
        return v;
    }

    public static int getGenes() {
        int v = 0;
        try {
            DB db = API.getDb();
            db.connect();
            ResultSet rs = db.getData("SELECT COUNT(*) AS num FROM wave#build#_genelist");
            while (rs.next()) {
                v = rs.getInt("num");
            }
        } catch (SQLException ex) {
            Logger.getLogger(Settings.class.getName()).log(Level.SEVERE, null, ex);
        }
        return v;
    }

    public static int getPointers() {
        int v = 0;
        try {
            DB db = API.getDb();
            db.connect();
            ResultSet rs = db.getData("SELECT COUNT(*) AS num FROM wave#build#_leaf");
            while (rs.next()) {
                v = rs.getInt("num");
            }
        } catch (SQLException ex) {
            Logger.getLogger(Settings.class.getName()).log(Level.SEVERE, null, ex);
        }
        return v;
    }
}
