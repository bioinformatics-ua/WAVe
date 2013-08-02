package pt.ua.bioinformatics.wave.domain;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import pt.ua.bioinformatics.wave.services.API;
import pt.ua.bioinformatics.wave.services.DB;

/**
 *
 * @author pedrolopes
 * @version 1.2, 2010-12-17
 */
public class Node {

    private DB db = API.getDb();
    private int id;
    private String name;
    private String description;
    private String shortname;
    private String value;
    private String method;
    private String ua;
    private Node parent;
    public ArrayList<Leaf> leafs = new ArrayList<Leaf>();
    private Type type = new Type();
    private boolean loaded = false;
    private int size = 0;

    // constructors
    /**
     * Instances a new Node based on its WAVe id.
     *
     * @param nodeId The WAVe id of the node.
     */
    public Node(int nodeId) {
        setId(nodeId);
    }

    /**
     * Instances a new node based on its Data Type.
     *
     * @param t The node data type
     */
    public Node(Type t) {
        this.type = t;
    }

    public Node(String name, String description, String value, Node parent, Type type) {
        this.name = name;
        this.description = description;
        this.value = value;
        this.parent = parent;
        this.type = type;
    }

    public Node(int id, String name, String description, String value, Node parent, Type type) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.value = value;
        this.parent = parent;
        this.type = type;
    }

    public Node(int id, String name, String description, String shortname, String method, String value, Node parent, Type type, String ua) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.value = value;
        this.parent = parent;
        this.type = type;
        this.shortname = shortname;
        this.method = method;
        this.ua = ua;
    }

    public Node(String name, String description, String shortname, String method, String value, Node parent, Type type, String ua) {

        this.name = name;
        this.description = description;
        this.value = value;
        this.parent = parent;
        this.type = type;
        this.shortname = shortname;
        this.method = method;
        this.ua = ua;
    }

    public Node(int id, String name, String description, String value, String shortname, Node parent) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.value = value;
        this.parent = parent;
        this.shortname = shortname;
    }

    public Node(int id, String name, String value, String ua) {
        this.id = id;
        this.name = name;
        this.ua = ua;
        this.value = value;
    }

    /**
     * Instances a new Node based on its value.
     *
     * @param name The value of of the node.
     */
    public Node(String name) {
        try {
            db.connect();

            ResultSet rs = db.getData("SELECT * FROM wave#build#_node WHERE shortname LIKE '" + name + "'");
            while (rs.next()) {
                this.id = rs.getInt("id");
                this.type.setId(rs.getInt("reftypeid"));
                this.name = rs.getString("name");
                this.value = rs.getString("value");
                this.description = rs.getString("description");
                this.parent = new Node(rs.getInt("parent"));
                this.shortname = name;
                this.method = rs.getString("method");

            }
        } catch (SQLException ex) {
            System.out.println("[Node][DB] Unable to find node with name " + name + "\n\t" + ex.toString());
            db.close();
        } finally {
            db.close();
        }
    }

    public String getUa() {
        return ua;
    }

    public void setUa(String ua) {
        this.ua = ua;
    }

    public Node() {
    }

    public Node(int id, String name, String description, String shortname, String method, String value, Node parent) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.value = value;
        this.shortname = shortname;
        this.method = method;
    }

    // getters and setters
    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getShortname() {
        return shortname;
    }

    public void setShortname(String shortname) {
        this.shortname = shortname;
    }

    public Node getParent() {
        return parent;
    }

    public boolean getLoaded() {
        return loaded;
    }

    public void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }

    public void setParent(Node parent) {
        this.parent = parent;
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

    public void setDescription(String nodeDescription) {
        this.description = nodeDescription;
    }

    public int getId() {
        return id;
    }

    /**
     * Loads Node data from WAVe based on its WAVe identifier.
     *
     * @param id The Node id.
     */
    private void setId(int id) {

        try {
            db.connect();

            ResultSet rs = db.getData("SELECT * FROM wave#build#_node WHERE id = " + id);
            while (rs.next()) {
                this.id = id;
                this.type.setId(rs.getInt("reftypeid"));
                this.name = rs.getString("name");
                this.value = rs.getString("value");
                this.description = rs.getString("description");
                this.parent = new Node(rs.getInt("parent"));
                this.method = rs.getString("method");
                this.shortname = rs.getString("shortname");
                this.ua = rs.getString("ua");
            }
        } catch (SQLException ex) {
            System.out.println("[Node][DB]: Unable to find node with id " + id + "\n\t" + ex.toString());
        } finally {
            db.close();
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String nodeName) {
        this.name = nodeName;
    }

    public String getValue() {
        return value;
    }

    /**
     * Loads Node data from WAVe based on its value (usually the address).
     *
     * @param value The Node value.
     */
    public void setValue(String value) {
        try {
            db.connect();

            ResultSet rs = db.getData("SELECT * FROM wave#build#_node WHERE value like '" + value + "'");
            while (rs.next()) {
                this.id = rs.getInt("id");
                this.type.setId(rs.getInt("reftypeid"));
                this.name = rs.getString("name");
                this.value = value;
                this.description = rs.getString("description");
                this.parent = new Node(rs.getInt("parent"));
                this.method = rs.getString("method");
                this.shortname = rs.getString("shortname");
                this.ua = rs.getString("ua");
            }
        } catch (SQLException ex) {
            db.close();
            System.out.println("[Node][DB]: Unable to find node with value " + value + "\n\t" + ex.toString());
        } finally {
            db.close();
        }
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public List<Leaf> getLeafs() {
        return leafs;
    }

    public void setLeafs(ArrayList<Leaf> leafs) {
        this.leafs = leafs;
    }

    /**
     * Adds a Node to WAVe database.
     * 
     * @return success of the operation.
     */
    public boolean addToDB() {
        boolean success = false;
        try {
            db.connect();
            db.insert("Node " + name, "INSERT INTO wave#build#_node (name, description, shortname, method, value, parent, reftypeid, ua, timestamp) "
                    + "VALUES('" + name + "','" + description + "','" + shortname + "','" + method + "', '" + value + "'," + parent.getId() + "," + type.getId() + ", '" + ua + "', NOW())");
            success = true;
        } catch (Exception ex) {
            System.out.println("[Node][DB] Unable to add node " + name + " to WAVe database\n\t" + ex.toString());
        } finally {
            System.out.println("[Node][DB] Node " + name + ":" + value + " added to WAVe database");
            db.close();
            return success;
        }

    }

    /**
     * Loads Node information, that is, loads all leafs for this node regarding a given gene.
     * 
     * @param gene The gene about we are loading information.
     * @return Success of the operation.
     */
    public boolean loadInfo(Gene gene) {
        boolean success = false;
        Type t = API.getTypes().get("geneleaf");
        try {
            db.connect();
            ResultSet rs = db.getData("SELECT wave#build#_leaf.value, wave#build#_leaf.id, wave#build#_leaf.name FROM wave#build#_leaf INNER JOIN wave#build#_association ON wave#build#_leaf.id = wave#build#_association.b "
                    + "WHERE wave#build#_association.a = " + gene.getId() + " AND wave#build#_association.reftypeid = " + t.getId() + " AND wave#build#_leaf.refnodeid = " + id + " GROUP BY wave#build#_leaf.value LIMIT 50");
            while (rs.next()) {
                Leaf l = new Leaf(rs.getInt("id"), rs.getString("name"), rs.getString("value"), this);
                leafs.add(l);

                if (this.shortname.equals("SwissProt")) {
                    gene.setProtein(l);
                }
                setLoaded(true);
            }
            setSize(leafs.size());
            db.close();
            success = true;
        } catch (Exception e) {
            System.out.println("[Node] Unable to load leafs\n\t" + e.toString());
        }
        return success;

    }
}
