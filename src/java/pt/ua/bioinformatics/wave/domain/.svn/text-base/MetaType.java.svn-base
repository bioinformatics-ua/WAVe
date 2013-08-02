package pt.ua.bioinformatics.wave.domain;

import java.sql.ResultSet;
import java.sql.SQLException;
import pt.ua.bioinformatics.wave.services.API;
import pt.ua.bioinformatics.wave.services.DB;

/**
 *
 * @author pedrolopes
 * @version 1.2, 2010-12-17
 */
public class MetaType {

    private DB db = API.getDb();
    private String name;
    private String description;
    private String shortname;
    private int id;

    public String getShortname() {
        return shortname;
    }

    public void setShortname(String shortname) {
        this.shortname = shortname;
    }

    public DB getDb() {
        return db;
    }

    public void setDb(DB db) {
        this.db = db;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MetaType() {
    }

    public MetaType(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public MetaType(String name, String description, String shortname) {
        this.name = name;
        this.description = description;
        this.shortname = shortname;
    }

    /**
     * Loads data for a metatype based on its name.
     * @param name
     */
    public MetaType(String shortname) {
        try {
            this.shortname = shortname;
            db.connect();
            ResultSet rs = db.getData("SELECT * FROM wave#build#_metatype WHERE shortname LIKE '" + shortname + "'");
            while (rs.next()) {
                this.id = rs.getInt("id");
                this.description = rs.getString("description");
                this.name = rs.getString("name");
            }
        } catch (SQLException ex) {
            db.close();
            System.out.println("[MetaType][DB] Unable to load " + name + " from WAVe DB\n\t" + ex.toString());
        } finally {
            db.close();
            System.out.println("[MetaType][DB] Loaded " + name + " from WAVe DB");
        }
    }

    /**
     * Adds MetaType to WAVe database.
     * @return
     */
    public boolean addToDB() {
        boolean success = false;
        try {
            db.connect();
            db.insert(name, "INSERT INTO wave#build#_metatype (name,description, shortname, timestamp) VALUES ('" + name + "','" + description + "', '" + shortname + "', NOW())");
            success = true;
        } catch (Exception ex) {
            System.out.println("[MetaType][DB] Unable to add " + name + " to WAVe DB\n\t" + ex.toString());
        } finally {
            db.close();
            System.out.println("[MetaType][DB] Added " + name + " to WAVe DB");
            return success;
        }
    }
}
