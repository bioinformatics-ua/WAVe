package pt.ua.bioinformatics.wave.domain;

import java.io.Serializable;
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
public class Variant implements Serializable {

    private DB db = API.getDb();
    private Gene gene = new Gene();
    private Leaf source = new Leaf();
    public ArrayList<Leaf> sources = new ArrayList<Leaf>();
    private int id = 0;
    private String refseq = "";
    private String variant;
    private String date;
    private int changeType = 7;
    private int n = 0;
    private int numberOfSources = -1;
    private final Type type = API.getTypes().get("variantleaf");
    private final Type substitution = API.getTypes().get("sub");
    private final Type deletion = API.getTypes().get("del");
    private final Type inversion = API.getTypes().get("inv");
    private final Type conversion = API.getTypes().get("conv");
    private final Type insertion = API.getTypes().get("ins");
    private final Type duplication = API.getTypes().get("dup");
    private final Type delins = API.getTypes().get("delins");

    public String getRefseq() {
        return refseq;
    }

    public void setRefseq(String refseq) {
        this.refseq = refseq;
    }

    // constructors
    public Variant() {
    }

    /**
     * Instances a new Variant
     *
     * @param variantId The WAVe id of the variant
     * @param variant The variation (in HGVS normalized format).
     * @param changeType The variation change type
     * @param hgnc The gene HGNC to which the variation belongs.
     */
    public Variant(int variantId, String variant, int changeType, String hgnc) {
        this.id = variantId;
        this.variant = variant;
        this.changeType = changeType;
        this.gene = new Gene(hgnc);

    }

    public Variant(int variantId, String variant, int changeType) {
        this.id = variantId;
        this.variant = variant;
        this.changeType = changeType;

    }

    public Variant(int variantId, String variant, int changeType, Gene gene) {
        this.id = variantId;
        this.variant = variant;
        this.changeType = changeType;
        this.gene = gene;
    }

    public Variant(int n, int variantId, String variant, int changeType, Gene gene) {
        this.n = n;
        this.id = variantId;
        this.variant = variant;
        this.changeType = changeType;
        this.gene = gene;
    }

    public Variant(int n, int variantId, String variant, int changeType, Gene gene, Leaf source) {
        this.n = n;
        this.id = variantId;
        this.variant = variant;
        this.changeType = changeType;
        this.gene = gene;
        this.source = source;

    }

    // Getters and Setters
    public int getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
    }

    public ArrayList<Leaf> getSources() {
        return sources;
    }

    public void setSources(ArrayList<Leaf> sources) {
        this.sources = sources;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getNumberOfSources() {
        return numberOfSources;
    }

    public void setNumberOfSources(int numberOfSources) {
        this.numberOfSources = numberOfSources;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getVariant() {
        return variant;
    }

    public void setVariant(String variant) {
        this.variant = variant;
    }

    public int getChangeType() {
        return changeType;
    }

    public void setChangeType(int changeType) {
        this.changeType = changeType;
    }

    public DB getDb() {
        return db;
    }

    public void setDb(DB db) {
        this.db = db;
    }

    public Leaf getSource() {
        return source;
    }

    public void setSource(Leaf leaf) {
        this.source = leaf;
    }

    /**
     * @return the gene
     */
    public Gene getGene() {
        return gene;
    }

    /**
     * @param gene the gene to set
     */
    public void setGene(Gene gene) {
        this.gene = gene;
    }

    // operations
    /**
     * Adds a list of variants to WAVe database.
     * <p><b>Feature.</b>
     * Processes a given list of variants and adds each one of the variants to WAVe database, loading data into the variant and association tables.
     * </p>
     *
     * @param variants List of found gene variants.
     * @return Success of the operation.
     */
    @SuppressWarnings("finally")
    public boolean addVariantsToDB(ArrayList<String> variants) {
        boolean success = false;
        //System.out.println(variants.size());
        try {
            // startup database connection
            db.connect();
            changeType = 0;

            // variant insertion loop
            for (String v : variants) {
                String refs = "";
                try {
                    String[] tmp = v.split(":");
                    refs = tmp[0];
                    v = tmp[1];
                } catch (Exception ex) {
                }

                // test change type
                if (v.matches(".*con.*")) {
                    changeType = conversion.getId();
                }
                if (v.matches(".*inv.*")) {
                    changeType = inversion.getId();
                }
                if (v.matches(".*ins.*") || v.contains("+")) {
                    changeType = insertion.getId();
                }
                if (v.matches(".*del.*") || v.matches(".*-.*")) {
                    changeType = deletion.getId();
                }
                if (v.matches(".*delins.*") || v.matches(".*indel.*")) {
                    changeType = delins.getId();
                }
                if (v.matches(".*dup.*")) {
                    changeType = duplication.getId();
                }
                if (v.matches(".*>.*") || v.matches(".*&gt;.*")) {
                    changeType = substitution.getId();
                }

                //insert variant into database
                if (changeType == substitution.getId()) {
                    v = v.replace(">", "&gt;");
                }

                 //System.out.println("[Variant][DB] Adding Variant " + refs + ":" + v + " and Change Type " + changeType + " to " + gene.getHGNC() + " and leaf " + source.getValue());


                if (changeType != 0) {
                    db.connect();
                    db.insert("Variant " + v + " to " + gene.getHGNC(), "INSERT INTO wave#build#_variant (refseq,variant,refgeneid,refchangetypeid, timestamp)"
                            + "VALUES('" + refs + "','" + v + "', " + gene.getId() + " ," + changeType + ", NOW())");

                    db.insert("Variant Leaf " + v + " to " + source.getId(), "INSERT INTO wave#build#_association(a, b, reftypeid) VALUES( (SELECT @@identity)," + source.getId() + "," + type.getId() + ")");
                    db.close();
                }
                // show in console

                // reset change type
                changeType = 0;
            }
            success = true;
        } catch (Exception e) {
            System.out.println("[Variant][DB] Unable to add variants to WAVe database\n\t" + e.toString());
            Logger.getLogger(Variant.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            db.close();
            return success;
        }
    }
}
