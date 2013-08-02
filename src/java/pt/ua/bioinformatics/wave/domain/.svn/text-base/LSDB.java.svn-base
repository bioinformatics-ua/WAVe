package pt.ua.bioinformatics.wave.domain;

import pt.ua.bioinformatics.wave.services.API;
import pt.ua.bioinformatics.wave.services.DB;

/**
 *
 * @author pedrolopes
 * @version 1.2, 2010-12-17
 */
public class LSDB {

    private MetaType metatype;
    private Type type;
    private String name;
    private String value;
    private int id;
    private DB db = API.getDb();
    private Gene gene = new Gene();
    private Node node = new Node();

    public LSDB(Node node, Gene gene, String address, String info) {
        this.node = node;
        this.gene = gene;
        this.value = address;
        this.name = info;
    }

    public String getAddress() {
        return value;
    }

    public void setAddress(String address) {
        this.value = address;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public MetaType getMetatype() {
        return metatype;
    }

    public void setMetatype(MetaType metatype) {
        this.metatype = metatype;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public DB getDb() {
        return db;
    }

    public void setDb(DB db) {
        this.db = db;
    }

    public Gene getGene() {
        return gene;
    }

    public void setGene(Gene gene) {
        this.gene = gene;
    }

    // operations
    /**
     * Adds an LSDB to WAVe database.
     *
     * @return Success of the operation.
     */
    public boolean addToDB() {
        boolean success = false;
        // try to add the leaf to the database
        type = API.getTypes().get("geneleaf");
        try {
            db.connect();

            db.insert("LSDB for " + gene.getHGNC() + ":" + value + " of type " + node.getName(), "INSERT INTO wave#build#_leaf (name, value, refnodeid, timestamp)"
                    + "VALUES ('" + name + "','" + value + "','" + node.getId() + "', NOW())");

            db.insert("Gene Leaf for LSDB " + value, "INSERT INTO wave#build#_association (a, b, reftypeid) VALUES(" + gene.getId() + ",(SELECT @@identity), " + type.getId() + ")");

            success = true;
        } catch (Exception e) {
            System.out.println("[LSDB][DB] LSDB adding failed\n\t" + e.toString());
            success = false;
        } finally {
            // System.out.println("[LSDB][DB] Added LSDB with value " + value);
            db.close();
            return success;
        }
    }
}
