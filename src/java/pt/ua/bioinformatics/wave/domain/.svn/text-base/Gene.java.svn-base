package pt.ua.bioinformatics.wave.domain;

import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import pt.ua.bioinformatics.wave.services.API;

import pt.ua.bioinformatics.wave.services.DB;

/**
 *
 * @author pedrolopes
 * @version 1.2, 2010-12-17
 */
public class Gene {

    private DB db = API.getDb();
    private int id = 0;
    private String HGNC;
    private String name;
    private String locus;
    private boolean enabled;
    private int numberOfVariants = -1;
    private int totalVariants = -1;
    private int numberOfLsdbs = 0;
    private int variantSub = -1;
    private int variantDel = -1;
    private int variantIns = -1;
    private int variantInv = -1;
    private int variantDup = -1;
    private int variantCon = -1;
    private int variantDelins = -1;
    private ArrayList<Gene> related = new ArrayList<Gene>();
    private ArrayList<LSDB> lsdbs = new ArrayList<LSDB>();
    private Leaf sequence = new Leaf();
    private Leaf protein = new Leaf();

    public int getTotalVariants() {
        return totalVariants;
    }

    public void setTotalVariants(int totalVariants) {
        this.totalVariants = totalVariants;
    }

    public Leaf getSequence() {
        return sequence;
    }

    public void setSequence(Leaf sequence) {
        this.sequence = sequence;
    }

    // Constructors
    public Gene() {
    }

    /**
     * Instances a new gene based on its HGNC symbol.
     *
     * @param hgnc Gene HGNC approved symbol.
     */
    public Gene(String hgnc) {
        HGNC = hgnc;
        try {
            this.updateGeneId(hgnc);
        } catch (Exception e) {
            System.out.println("[Gene] Unable to create gene with HGNC " + hgnc + "\n\t" + e.toString());
        }
    }

    /**
     * Instances a new gene based on its WAVe identifier.
     *
     * @param id WAVe gene identifier.
     */
    public Gene(int id) {
        this.id = id;
        try {
            updateGeneHGNC(id);
        } catch (Exception e) {
            System.out.println("[Gene] Unable to create gene with id " + id + "\n\t" + e.toString());
        }
    }

    /**
     * Instances a new gene based on its key features.
     *
     * @param id Gene WAVe identifier.
     * @param HGNC Gene HGNC approved symbol.
     * @param enabled Gene status in WAVe (enabled means that there is an LSDB available for the gene)
     */
    public Gene(int id, String HGNC, boolean enabled) {
        this.id = id;
        this.HGNC = HGNC;
        this.enabled = enabled;
    }

    public int getVariantDelins() {
        return variantDelins;
    }

    public void setVariantDelins(int variantDelins) {
        this.variantDelins = variantDelins;
    }

    // Getters and setters
    public int getNumberOfLsdbs() {
        return numberOfLsdbs;
    }

    public String getLocus() {
        return locus;
    }

    public void setLocus(String locus) {
        this.locus = locus;
    }

    public int getVariantCon() {
        return variantCon;
    }

    public void setVariantCon(int variantCon) {
        this.variantCon = variantCon;
    }

    public int getVariantDel() {
        return variantDel;
    }

    public void setVariantDel(int variantDel) {
        this.variantDel = variantDel;
    }

    public int getVariantDup() {
        return variantDup;
    }

    public void setVariantDup(int variantDup) {
        this.variantDup = variantDup;
    }

    public int getVariantIns() {
        return variantIns;
    }

    public void setVariantIns(int variantIns) {
        this.variantIns = variantIns;
    }

    public int getVariantInv() {
        return variantInv;
    }

    public void setVariantInv(int variantInv) {
        this.variantInv = variantInv;
    }

    public int getVariantSub() {
        return variantSub;
    }

    public void setVariantSub(int variantSub) {
        this.variantSub = variantSub;
    }

    public void setNumberOfLsdbs(int numerOfLsdbs) {
        this.numberOfLsdbs = numerOfLsdbs;
    }

    public ArrayList<LSDB> getLsdbs() {
        return lsdbs;
    }

    public void setLsdbs(ArrayList<LSDB> lsdbs) {
        this.lsdbs = lsdbs;
    }

    public Leaf getProtein() {
        return protein;
    }

    public void setProtein(Leaf protein) {
        this.protein = protein;
    }

    public DB getDb() {
        return db;
    }

    public void setDb(DB db) {
        this.db = db;
    }

    public String getHGNC() {
        return HGNC;
    }

    public void setHGNC(String HGNC) {
        this.HGNC = HGNC;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
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

    public ArrayList<Gene> getRelated() {
        return related;
    }

    public void setRelated(ArrayList<Gene> related) {
        this.related = related;
    }

    public int getNumberOfVariants() {
        return numberOfVariants;
    }

    public void setNumberOfVariants(int numberOfVariants) {
        this.numberOfVariants = numberOfVariants;
    }

    /**
     * Enables a Gene in WAVe database (when a LSDB is added).
     * <p><b>Feature:</b><br />
     * WAVe contains a list of all genes approved by HGNC. However, only some of those genes have a locus-specific database available.
     * In WAVe only genes with one or more LSDBs available are considered. Thus, a gene is enabled in the system when data is loaded for its LSDB.
     * </p>
     * @return success of the operation (true if gene is enabled, false if fails to enable gene).
     */
    public boolean enable() {
        try {
            db.connect();

            db.update(HGNC, "UPDATE wave#build#_gene SET enabled = true WHERE hgnc LIKE '" + HGNC + "'");

            this.enabled = true;
            // System.out.println("[Gene][DB] " + HGNC + " enabled");
        } catch (Exception ex) {
            System.out.println("[Gene][DB] Unable to enable gene " + HGNC + "in WAVe database\n\t" + ex.toString());

        } finally {
            db.close();
            return enabled;
        }
    }

    /**
     * Sets the number of variants found for this gene.
     * <p><b>Feature:</b><br />
     * Loads the number of variants and each respective change for this gene. These counts are required to a correct display of data in
     * the gene navigation tree.
     * </p>
     * @return success of the operation (true if can count all variants and change types, false if fails to count).
     */
    public boolean setNumberOfVariants() {
        boolean success = false;
        ResultSet rs = null;

        if (!API.isTypesLoaded()) {
            API.loadTypes();
        }

        if (id != 0) {
            try {
                db.connect();

                if (this.numberOfVariants == -1) {
                    rs = db.getData("SELECT COUNT(DISTINCT variant) AS n FROM wave#build#_variant WHERE refgeneid =" + id);

                    while (rs.next()) {
                        this.numberOfVariants = rs.getInt("n");
                    }
                }

                if (this.totalVariants == -1) {
                    rs = db.getData("SELECT COUNT(variant) AS n FROM wave#build#_variant WHERE refgeneid =" + id);

                    while (rs.next()) {
                        this.totalVariants = rs.getInt("n");
                    }
                }

                if (this.variantDel == -1) {
                    // variantDel
                    Type t = API.getTypes().get("del");

                    rs = db.getData("SELECT COUNT(DISTINCT variant) AS n FROM wave#build#_variant WHERE refgeneid = " + id + " AND refchangetypeid = " + t.getId());
                    while (rs.next()) {
                        variantDel = rs.getInt(1);
                    }
                }

                if (this.variantSub == -1) {
                    // variantSub
                    Type t = API.getTypes().get("sub");
                    rs = db.getData("SELECT COUNT(DISTINCT variant) AS n FROM wave#build#_variant WHERE refgeneid = " + id + " AND refchangetypeid = " + t.getId());
                    while (rs.next()) {
                        this.variantSub = rs.getInt(1);
                    }
                }

                if (this.variantIns == -1) {
                    // variantIns
                    Type t = API.getTypes().get("ins");
                    rs = db.getData("SELECT COUNT(DISTINCT variant) AS n FROM wave#build#_variant WHERE refgeneid = " + id + " AND refchangetypeid = " + t.getId());
                    while (rs.next()) {
                        this.variantIns = rs.getInt(1);
                    }
                }

                if (this.variantInv == -1) {
                    // variantInv
                    Type t = API.getTypes().get("inv");
                    rs = db.getData("SELECT COUNT(*) AS n FROM wave#build#_variant WHERE refgeneid = " + id + " AND refchangetypeid = " + t.getId());
                    while (rs.next()) {
                        this.variantInv = rs.getInt(1);
                    }
                }

                if (this.variantCon == -1) {
                    // variantCon
                    Type t = API.getTypes().get("con");
                    rs = db.getData("SELECT COUNT(DISTINCT variant) AS n FROM wave#build#_variant WHERE refgeneid = " + id + " AND refchangetypeid = " + t.getId());
                    while (rs.next()) {
                        this.variantCon = rs.getInt(1);
                    }
                }

                if (this.variantDup == -1) {
                    // variantDup
                    Type t = API.getTypes().get("dup");
                    rs = db.getData("SELECT COUNT(DISTINCT variant) AS n FROM wave#build#_variant WHERE refgeneid = " + id + " AND refchangetypeid = " + t.getId());
                    while (rs.next()) {
                        this.variantDup = rs.getInt(1);
                    }
                }

                if (this.variantDelins == -1) {
                    // variantDelins
                    Type t = API.getTypes().get("delins");
                    rs = db.getData("SELECT COUNT(DISTINCT variant) AS n FROM wave#build#_variant WHERE refgeneid = " + id + " AND refchangetypeid = " + t.getId());
                    while (rs.next()) {
                        this.variantDelins = rs.getInt(1);
                    }
                }
                success = true;
            } catch (Exception e) {
                System.out.println("[Gene][DB] Unable to count " + HGNC + " variants\n\t" + e.toString());
                Logger.getLogger(API.class.getName()).log(Level.SEVERE, null, e);
            } finally {
                db.close();
            }
        }
        return success;
    }

    /**
     * Sets the number of locus-specific databases for this gene.
     * <p><b>Feature:</b><br />
     * Counts the number of available LSDBs for this gene in order to present them in the full gene name search interface.
     * </p>
     * @return success of the operation (true if counts LSDBs, false if fails).
     */
    public boolean setNumberOfLsdbs() {
        boolean success = false;

        if (id != 0) {
            try {
                db.connect();
                ResultSet rs = db.getData("SELECT COUNT(*) FROM wave#build#_association INNER JOIN wave#build#_leaf ON wave#build#_association.b = wave#build#_leaf.id "
                        + "WHERE wave#build#_association.a =" + id + " AND wave#build#_association.reftypeid = 18 AND wave#build#_leaf.refnodeid IN " + API.getLsdbs());

                while (rs.next()) {
                    this.numberOfLsdbs = rs.getInt(1);
                }
                success = true;

            } catch (Exception e) {
                System.out.println("[Gene][DB] Unable to count " + HGNC + " LSDBs\n\t" + e.toString());
            } finally {
                db.close();
            }
        }
        return success;
    }

    /**
     * Loads entire variant information for associated gene.
     * <p><ul>
     *  <li>Get all variants and variant counts from DB</li>
     *  <li>Check number of LSDBs for gene and variant</li>
     *  <li>Check leaf sources for each variant</li>
     * </ul></p>
     * @param change
     * @return
     */
    public ArrayList<Variant> getVariants(String change) {
        ArrayList<Variant> variants = new ArrayList<Variant>();
        if (change.equals("all")) {
            try {
                db.connect();
                Leaf l;
                ResultSet rs = db.getData("SELECT * FROM wave#build#_variantlist WHERE refgeneid = " + this.id);
                while (rs.next()) {
                    Variant var = new Variant(rs.getInt("n"), rs.getInt("id"), rs.getString("variant"), rs.getInt("refchangetypeid"), this);
                    var.setRefseq(rs.getString("refseq"));
                    if (numberOfLsdbs > 1) {
                        if (var.getN() == 1) {
                            l = new Leaf();
                            l.setValue(rs.getString("source"));
                            l.setName(rs.getString("source"));
                            var.sources.add(l);
                            var.setNumberOfSources(1);
                            variants.add(var);
                        } else if (var.getN() > 1) {
                            int lol = rs.getInt("lsdb");

                            if (lol == 1) {
                                l = new Leaf();
                                l.setValue(rs.getString("source"));
                                l.setName(rs.getString("source"));
                                var.sources.add(l);
                                var.setNumberOfSources(1);
                                variants.add(var);
                            } else {
                                String[] leafs = rs.getString("source").split("##");
                                for (String leaf : leafs) {
                                    Leaf source = new Leaf();
                                    String[] info = leaf.split("%");
                                    //System.out.println(leaf);
                                    source.setName(info[1]);
                                    source.setValue(info[1]);
                                    source.setN(Integer.parseInt(info[0]));
                                    var.sources.add(source);
                                }
                                var.setNumberOfSources(lol);
                                variants.add(var);
                            }

                        }
                    } else {
                        l = new Leaf();
                        l.setName(rs.getString("source"));
                        l.setValue(rs.getString("source"));
                        l.setN(rs.getInt("n"));
                        var.sources.add(l);
                        var.setNumberOfSources(1);
                        variants.add(var);
                    }
                }
            } catch (Exception e) {
                System.out.println("[Gene][DB] Unable to find variants for " + HGNC + "\n\t" + e.toString());
                Logger.getLogger(API.class.getName()).log(Level.SEVERE, null, e);
            } finally {
                db.close();
            }
        } else {
            try {
                Type type = API.getTypes().get(change);
                db.connect();
                Leaf l;
                ResultSet rs = db.getData("SELECT * FROM wave#build#_variantlist WHERE refgeneid = " + this.id + " AND refchangetypeid = " + type.getId());
                while (rs.next()) {
                    Variant var = new Variant(rs.getInt("n"), rs.getInt("id"), rs.getString("variant"), rs.getInt("refchangetypeid"), this);
                    var.setRefseq(rs.getString("refseq"));
                    if (numberOfLsdbs > 1) {
                        if (var.getN() == 1) {
                            l = new Leaf();
                            l.setValue(rs.getString("source"));
                            l.setName(rs.getString("source"));
                            var.sources.add(l);
                            var.setNumberOfSources(1);
                            variants.add(var);
                        } else if (var.getN() > 1) {
                            int lol = rs.getInt("lsdb");

                            if (lol == 1) {
                                l = new Leaf();
                                l.setValue(rs.getString("source"));
                                l.setName(rs.getString("source"));
                                var.sources.add(l);
                                var.setNumberOfSources(1);
                                variants.add(var);
                            } else {
                                String[] leafs = rs.getString("source").split("##");
                                for (String leaf : leafs) {
                                    Leaf source = new Leaf();
                                    String[] info = leaf.split("%");
                                    // System.out.println(leaf);
                                    source.setName(info[1]);
                                    source.setValue(info[1]);
                                    source.setN(Integer.parseInt(info[0]));
                                    var.sources.add(source);
                                }
                                var.setNumberOfSources(lol);
                                variants.add(var);
                            }

                        }
                    } else {
                        l = new Leaf();
                        l.setName(rs.getString("source"));
                        l.setValue(rs.getString("source"));
                        l.setN(rs.getInt("n"));
                        var.sources.add(l);
                        var.setNumberOfSources(1);
                        variants.add(var);
                    }
                }
            } catch (Exception e) {
                System.out.println("[Gene][DB] Unable to find variants for " + HGNC + "\n\t" + e.toString());
                Logger.getLogger(API.class.getName()).log(Level.SEVERE, null, e);
            } finally {
                db.close();
            }
        }

        return variants;
    }

    /**
     * Updates the gene HGNC based on its WAVe id.
     * <p><b>Feature:</b><br />
     * Loads data from WAVe for a given gene based on its WAVe id.
     * </p>
     *
     * @param id The Gene WAVe id.
     * @return Success of the operation.
     */
    private boolean updateGeneHGNC(int id) {
        boolean success = false;
        try {
            db.connect();
            ResultSet rs = db.getData("SELECT hgnc,name,locus FROM wave#build#_gene WHERE id LIKE '" + id + "'");
            while (rs.next()) {
                this.HGNC = rs.getString("hgnc");
                this.name = rs.getString("name");
                this.locus = rs.getString("locus");
            }

            success = true;
        } catch (Exception e) {
            System.out.println("[Gene] Unable read gene with id " + id + "\n\t" + e.toString());
        } finally {
            db.close();
            //System.out.println("[Gene] " + HGNC + " has WAVe id " + id);
            return success;
        }
    }

    /**
     * Updates the Gene WAVe id based on the gene HGNC.
     * <p><b>Feature:</b><br />
     * Loads data from WAVe for a given gene based on its HGNC approved symbol.
     * </p>
     *
     * @param HGNC The gene HGNC symbol.
     * @return Success of the operation.
     */
    private boolean updateGeneId(String HGNC) {
        boolean success = false;
        try {
            db.connect();

            ResultSet rs = db.getData("SELECT id, name, locus FROM wave#build#_gene WHERE hgnc LIKE '" + HGNC + "'");
            while (rs.next()) {
                this.id = rs.getInt("id");
                this.name = rs.getString("name");
                this.locus = rs.getString("locus");
            }

            if (this.id == 0) {
                db.insert(HGNC, "INSERT INTO wave#build#_gene(hgnc, name, enabled) VALUES('" + HGNC + "','',0)");
                updateGeneId(HGNC);
            }
            success = true;
        } catch (Exception e) {
            System.out.println("[Gene] Unable read gene with HGNC " + HGNC + "\n\t" + e.toString());
            Logger.getLogger(API.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            db.close();
            //System.out.println("[Gene] " + HGNC + " has WAVe id " + id);
            return success;
        }
    }

    /**
     * Loads reference sequence information for Gene.
     * 
     * @return
     */
    public boolean loadSequence() {
        boolean success = false;

        try {
            Node n = API.getNodes().get("lrg");

            db.connect();
            ResultSet rs = db.getData("SELECT L.id, L.name, L.value, L.info FROM wave#build#_leaf L INNER JOIN wave#build#_association A ON A.b = L.id WHERE A.a = " + this.id + " AND L.refnodeid = " + n.getId());

            while (rs.next()) {
                sequence = new Leaf(rs.getInt("id"), rs.getString("name"), rs.getString("value"), n, rs.getString("info"));
            }

            success = true;
            db.close();
        } catch (Exception e) {
            //System.out.println("[Gene] unable to load LRG info\n\t" + e.toString());
            // Logger.getLogger(API.class.getName()).log(Level.SEVERE, null, e);
        }

        return success;
    }

    /**
     * Reads available gene information for the gene navigation tree.
     * <p><b>Feature:</b><br />
     * Loads information from WAVe database required to populate the gene navigation tree. This inclueds data for all data types, nodes for each data type
     * and leafs for each node.
     * </p>
     *
     * @return success if the operation.
     */
    public ArrayList<Type> loadInfo() {
        setNumberOfVariants();
        loadSequence();
        this.updateGeneHGNC(id);
        ArrayList<Type> datatypes = new ArrayList<Type>();
        try {
            db.connect();
            ResultSet rs = db.getData("SELECT wave#build#_type.id, wave#build#_type.name, wave#build#_type.description, wave#build#_type.refmetatypeid FROM wave#build#_type INNER JOIN wave#build#_metatype ON wave#build#_type.refmetatypeid = wave#build#_metatype.id WHERE wave#build#_metatype.shortname LIKE 'data'");
            while (rs.next()) {
                datatypes.add(new Type(rs.getInt("id"), rs.getString("name"), rs.getString("description"), rs.getInt("refmetatypeid")));
            }
            db.close();
            for (Type t : datatypes) {
                if (t.loadInfo()) {
                    for (Node n : t.getNodes()) {
                        if (n.getType().getName().equals("LSDB") || !n.getMethod().equals("direct")) {
                            n.loadInfo(this);
                        }
                        if (n.getSize() > t.getSize()) {
                            t.setSize(n.getSize());
                        }
                    }
                }
            }

            if (numberOfVariants == -1) {
                setNumberOfVariants();
            }
        } catch (Exception e) {
            System.out.println("[Gene][" + HGNC + "] Unable to load gene info\n\t" + e.toString());
        }

        return datatypes;
    }

    /**
     * Overrides the toString() returning valuable information about the gene.<br/><p>
     * <b>Feature:</b><br/>
     * Returns a simple definition of the instanced Gene containing its HGNC Id and
     * its internal WAVe Id</p>
     *
     * @return Gene WAVe identifiers
     */
    @Override
    public String toString() {
        return "[Gene] " + HGNC + " has WAVe ID " + id + " and " + numberOfVariants + " variants";
    }
}
