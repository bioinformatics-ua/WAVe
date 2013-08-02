package pt.ua.bioinformatics.wave.domain;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import pt.ua.bioinformatics.wave.services.API;
import pt.ua.bioinformatics.wave.services.DB;

/**
 *
 * @author pedrolopes
 * @version 1.2, 2010-12-17
 */
public class Type {

    private int id;
    private String name;
    private String description;
    private String shortname;
    private int metaType;
    private DB db = API.getDb();
    private ArrayList<Node> nodes = new ArrayList<Node>();
    private int size = 0;

    Type() {
    }

    public Type(String name, String description, int metaType) {
        this.name = name;
        this.description = description;
        this.metaType = metaType;
    }

    public Type(String name, String description, String shortname, int metaType) {
        this.name = name;
        this.shortname = shortname;
        this.description = description;
        this.metaType = metaType;
    }

    public Type(String name, String description) {
        this.name = name;
        this.description = description;
    }

    /**
     * Instances a new Type and, based on its name, gets all its data from WAVe database.
     *
     * @param shortname the Type shortname.
     */
    public Type(String shortname) {
        try {
            this.shortname = shortname;
            db.connect();
            ResultSet rs = db.getData("SELECT * FROM wave#build#_type WHERE shortname LIKE '" + shortname + "'");
            while (rs.next()) {
                this.id = rs.getInt("id");
                this.description = rs.getString("description");
                this.metaType = rs.getInt("refmetatypeid");
                this.name = rs.getString("name");
            }

        } catch (SQLException ex) {
            System.out.println("[Type][DB] Unable to load " + name + " from WAVe DB\n\t" + ex.toString());
        } finally {
            db.close();
            //System.out.println("[Type][DB] Loaded " + name + " from WAVe DB");
        }
    }

    /**
     * Instances a new Type by its id, loading all known information from WAVe database.
     *
     * @param id the Type id
     */
    public Type(int id) {
        try {
            db.connect();

            ResultSet rs = db.getData("SELECT * FROM wave#build#_type WHERE id = " + id);

            while (rs.next()) {
                this.id = id;
                this.description = rs.getString("description");
                this.name = rs.getString("name");
                this.metaType = rs.getInt("refmetatypeid");
                this.shortname = rs.getString("shortname");
            }
        } catch (Exception e) {
            System.out.println("[Type] Unable to load type\n\t" + e.toString());
        } finally {
            db.close();
        }
    }

    public Type(int id, String name, String description, int metatype) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.metaType = metatype;
    }

    public Type(int id, String name, String shortname, String description, int metatype) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.metaType = metatype;
        this.shortname = shortname;
    }

    public String getShortname() {
        return shortname;
    }

    public void setShortname(String shortname) {
        this.shortname = shortname;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public ArrayList<Node> getNodes() {
        return nodes;
    }

    public void setNodes(ArrayList<Node> nodes) {
        this.nodes = nodes;
    }

    public int getMetaType() {
        return metaType;
    }

    public void setMetaType(int metaType) {
        this.metaType = metaType;
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

    /**
     * Adds type to WAVe database.
     * 
     * @return success of the operation.
     */
    public boolean addToDB() {
        boolean success = false;
        try {
            db.connect();
            db.insert(name, "INSERT INTO wave#build#_type (name, shortname, description, timestamp, refmetatypeid) VALUES ('" + name + "', '" + shortname + "','" + description + "', NOW(), " + metaType + ")");
            success = true;
        } catch (Exception ex) {
            db.close();
            System.out.println("[Type][DB] Unable to add " + name + " to WAVe DB\n\t" + ex.toString());
        } finally {
            db.close();
            System.out.println("[Type][DB] Added " + name + " to meta type " + metaType + " in WAVe DB");
            return success;
        }
    }

    /**
     * Loads info about the data type: loads all nodes availabe for this datatype.
     * 
     * @return Success of the operaion.
     */
    public boolean loadInfo() {
        boolean success = false;

        if (!nodes.isEmpty()) {
            success = true;
        } else {
            try {
                db.connect();
                ResultSet rs = db.getData("SELECT DISTINCT * FROM wave#build#_node WHERE reftypeid = " + id);
                while (rs.next()) {
                    nodes.add(new Node(rs.getInt("id"), rs.getString("name"), rs.getString("description"), rs.getString("shortname"), rs.getString("method"), rs.getString("value"), new Node(rs.getInt("parent")), this, rs.getString("ua")));
                }
                success = true;
            } catch (Exception e) {
                System.out.println("[Type][" + name + "] Unable to load node information\n\t" + e.toString());
            } finally {
                db.close();
            }
        }
        return success;
    }
}
