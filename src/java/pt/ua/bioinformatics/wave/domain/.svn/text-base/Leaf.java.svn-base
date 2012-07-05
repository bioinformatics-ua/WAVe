package pt.ua.bioinformatics.wave.domain;

import java.sql.ResultSet;
import pt.ua.bioinformatics.wave.services.API;

import pt.ua.bioinformatics.wave.services.DB;

/**
 *
 * @author pedrolopes
 * @version 1.2, 2010-12-17
 */
public class Leaf {

    private DB db = API.getDb();
    private int id = 0;
    private int n;          // number of variants associated with this Leaf (for variant counts)
    private String name;
    private String value;
    private String timestamp;
    private Node node = new Node();
    private String info;

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    // constructors
    public Leaf() {
    }

    public Leaf(String value) {
        this.value = value;
        updateLeafByValue(value);
    }

    public Leaf(String name, String value) {
        this.name = name;
        this.value = value;
        updateLeafByValue(value);
    }

    public Leaf(int id, String name, String value, int n) {
        this.id = id;
        this.name = name;
        this.value = value;
        this.n = n;
    }

    public Leaf(String name, String value, int n) {
        this.name = name;
        this.value = value;
        this.n = n;
    }

    public Leaf(String name, String value, Node node) {
        this.name = name;
        this.value = value;
        this.node = node;
        updateLeafByValue(value);
    }

    public Leaf(int id, String name, String value, Node node) {
        this.id = id;
        this.name = name;
        this.value = value;
        this.node = node;

    }

    public Leaf(int id, String name, String value, Node node, String info) {
        this.id = id;
        this.name = name;
        this.value = value;
        this.node = node;
        this.info = info;

    }

    // getters and setters
    public String getDate() {
        return timestamp;
    }

    public int getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
    }

    public void setDate(String date) {
        this.timestamp = date;
    }

    public DB getDb() {
        return db;
    }

    public void setDb(DB db) {
        this.db = db;
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

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    // operations
    /**
     * Updates Leaf information based on its value.
     * <p><b>Feature:</b><br />
     * When Leaf information is not complete, loads information from WAVe database
     * matching this information with the Leaf value (should be unique).
     * </p>
     *
     * @param value
     * @return
     */
    private int updateLeafByValue(String value) {
        try {
            db.connect();
            ResultSet rs = db.getData("SELECT * FROM wave#build#_leaf WHERE value LIKE '" + value + "'");
            while (rs.next()) {
                this.id = rs.getInt("id");
                this.timestamp = rs.getString("timestamp");
                this.name = rs.getString("name");
                this.value = rs.getString("value");
                this.node = new Node(rs.getInt("refnodeid"));
            }
        } catch (Exception e) {
            System.out.println("[Leaf][DB] Unable to update leaf " + value + "\n\t" + e.toString());

        } finally {
            db.close();
        }
        try {
            if (this.id == 0) {
                addToDB();
            }
        } catch (Exception e) {
            System.out.println("[Leaf][DB] Unable to add leaf " + value + " to WAVe database\n\t" + e.toString());
        } finally {
            db.close();
            //System.out.println("[Leaf] Leaf " + name + ":" + value + " with WAVe id " + id);
            return id;
        }
    }

    /**
     * Adds instanced leaf to WAVe database.
     * <p><b>Feature:</b><br />
     * Adds the current leaf to WAVe database and updates current instance information on memory.
     * </p>
     *
     * @return Success of the operation (true if leaf is added to database, false if fails to add)
     */
    public boolean addToDB() {
        boolean success = false;
        try {
            db.connect();

            db.insert("Leaf " + name + ":" + value + " of type " + node.getId(), "INSERT INTO wave#build#_leaf (name,value,refnodeid, timestamp)"
                    + "VALUES ('" + name + "','" + value + "','" + node.getId() + "', NOW())");

            //System.out.println("[Leaf][DB] Added leaf " + " with value " + value);
            if (updateLeafByValue(value) > 0) {
                success = true;
            }
        } catch (Exception e) {
            System.out.println("[Leaf][DB] Leaf adding failed " + e.toString());
        } finally {
            db.close();
            return success;
        }
    }
}
