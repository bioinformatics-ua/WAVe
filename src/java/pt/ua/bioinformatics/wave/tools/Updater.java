/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.ua.bioinformatics.wave.tools;

import au.com.bytecode.opencsv.CSVReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import pt.ua.bioinformatics.wave.domain.Gene;
import pt.ua.bioinformatics.wave.domain.Leaf;
import pt.ua.bioinformatics.wave.domain.Node;
import pt.ua.bioinformatics.wave.domain.Type;
import pt.ua.bioinformatics.wave.domain.Variant;
import pt.ua.bioinformatics.wave.services.API;
import pt.ua.bioinformatics.wave.services.DB;
import pt.ua.bioinformatics.wave.services.Settings;
import pt.ua.bioinformatics.wave.varcrawler.Creator;

/**
 *
 * @author pedrolopes
 */
public class Updater {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
       // addDbSNPVariants();
        cacheVariants();
    }

    static private void addDbSNPVariants() {
        //DB db = API.getDb();
        if (!API.isLoaded()) {
            API.load();
        }
        DB db = new DB("wave10", "jdbc:mysql://localhost:3306/wave10?user=root&password=telematica");

        String s = "LAMA2";
        //for (String s : API.getJedis().smembers("wave:genelist")) {
        try {
            db.connectX();
            ResultSet dbsnp_vars = db.getData("SELECT * FROM dbsnp WHERE hgnc LIKE '" + s + "';");

            while (dbsnp_vars.next()) {
                Variant variant = new Variant();
                ArrayList<String> variants = new ArrayList<String>();
                Gene gene = new Gene(s);
                Leaf source = new Leaf();
                source.setNode(new Node(101));
                source.setName("http://www.ncbi.nlm.nih.gov/projects/SNP/snp_ref.cgi?rs=" + dbsnp_vars.getString("dbsnp"));
                source.setValue("http://www.ncbi.nlm.nih.gov/projects/SNP/snp_ref.cgi?rs=" + dbsnp_vars.getString("dbsnp"));
                source.updateLeafByValue("http://www.ncbi.nlm.nih.gov/projects/SNP/snp_ref.cgi?rs=" + dbsnp_vars.getString("dbsnp"));
                String ref_seq = dbsnp_vars.getString("refseq");
                String hgvs = dbsnp_vars.getString("variant");
                String url = "http://www.ncbi.nlm.nih.gov/projects/SNP/snp_ref.cgi?rs=" + dbsnp_vars.getString("dbsnp");
                String v = ref_seq + ":" + hgvs;

                //System.out.println(ref_seq + ":" + hgvs + " @ " + url);
                try {
                    if (!API.rejectVariant(v)) {
                        variants.add(v);
                        //   System.out.println("[Crawler][" + crawlType + "][" + gene.getHGNC() + "] Read variant " + v);
                    }
                } catch (Exception e) {
                    System.out.println("[AllGenes][AddToDB] Unable to add variants for " + " to WAVe\n\t" + e.toString());
                    Logger.getLogger(Creator.class.getName()).log(Level.SEVERE, null, e);
                }

                variant.setGene(gene);
                variant.setSource(source);
                variant.addVariantsToDB(variants);
            }
        } catch (Exception ex) {
        }
        System.out.println("[WAVe][Updated] Added variants for " + s);
        //}
    }

    static private void cacheVariants() {

        if (!API.isLoaded()) {
            API.load();
        }
        DB db = API.getDb();
        Type t = API.getTypes().get("variantleaf");
        String s = "COL3A1";
        // for (String s : j.smembers("wave:genelist")) {
        Gene gene = new Gene(s);
        gene.loadInfoForCache();
        try {

            ArrayList<Variant> variants = new ArrayList<Variant>();
            if (gene.getNumberOfVariants() > 0) {
                System.out.println("\n[Builder] caching variants for " + gene.getHGNC());
                try {
                    db.connect();
                    Variant var;
                    Leaf l;
                    ResultSet rs = db.getData("SELECT COUNT(wave#build#_variant.variant) AS n, wave#build#_variant.id AS vid, wave#build#_variant.refseq, wave#build#_variant.variant, wave#build#_leaf.id AS lid, wave#build#_leaf.name,wave#build#_leaf.value, wave#build#_variant.refchangetypeid"
                            + " FROM wave#build#_variant INNER JOIN wave#build#_association ON wave#build#_association.a = wave#build#_variant.id"
                            + " INNER JOIN wave#build#_leaf ON wave#build#_leaf.id = wave#build#_association.b"
                            + " WHERE wave#build#_variant.refgeneid = " + gene.getId() + " AND wave#build#_association.reftypeid = " + t.getId()
                            + " GROUP BY wave#build#_variant.variant, wave#build#_variant.refseq"
                            + " ORDER BY n DESC");
                    while (rs.next()) {
                        var = new Variant(rs.getInt("n"), rs.getInt("vid"), rs.getString("variant"), rs.getInt("refchangetypeid"), gene);
                        var.setRefseq(rs.getString("refseq"));

                        if (gene.getNumberOfLsdbs() > 1) {
                            if (var.getN() == 1) {
                                l = new Leaf(rs.getInt("lid"), rs.getString("name"), rs.getString("value"), 1);
                                var.sources.add(l);
                                var.setNumberOfSources(1);
                                variants.add(var);
                            } else if (var.getN() > 1) {
                                DB dbx = new DB();
                                dbx.connect();
                                ResultSet sources = dbx.getData("SELECT COUNT(wave#build#_leaf.name) AS n, wave#build#_leaf.id AS lid, wave#build#_leaf.name AS name, wave#build#_leaf.value AS value FROM wave#build#_variant INNER JOIN wave#build#_association ON wave#build#_association.a = wave#build#_variant.id INNER JOIN wave#build#_leaf ON wave#build#_leaf.id = wave#build#_association.b WHERE wave#build#_association.reftypeid = " + t.getId() + " AND wave#build#_variant.refgeneid = " + gene.getId() + " AND wave#build#_variant.variant LIKE '" + var.getVariant() + "' GROUP BY wave#build#_leaf.name ORDER BY n ASC");

                                int i = 0;
                                Leaf source;
                                while (sources.next()) {
                                    source = new Leaf(sources.getInt("lid"), sources.getString("name"), sources.getString("value"), sources.getInt("n"));
                                    var.sources.add(source);
                                    i++;
                                }
                                dbx.close();
                                var.setNumberOfSources(i);
                                variants.add(var);
                            }
                        } else {
                            l = new Leaf(rs.getInt("lid"), rs.getString("name"), rs.getString("value"), 1);
                            var.sources.add(l);
                            var.setNumberOfSources(1);
                            variants.add(var);
                        }
                    }
                    db.close();

                    for (Variant v : variants) {
                        String source = "";
                        if (v.getNumberOfSources() > 1) {
                            for (Leaf ls : v.getSources()) {
                                source += ls.getN() + "%" + ls.getValue() + "##";
                            }
                            source = source.substring(0, source.length() - 2);
                        } else {
                            for (Leaf ls : v.getSources()) {
                                source = ls.getValue();
                            }
                        }
                        //System.out.println(v.getId() + ":" + v.getVariant() + ":" + v.getN() + ":" + v.getNumberOfSources() + ":" + source + ":" + v.getGene().getId() + ":" + v.getChangeType());

                        db.connect();
                        String query = "INSERT INTO wave.wave#build#_variantlist (id, refseq, variant,n,lsdb, source, refgeneid, timestamp,refchangetypeid)"
                                + " VALUES(" + v.getId() + ", '" + v.getRefseq() + "','" + v.getVariant() + "'," + v.getN() + "," + v.getNumberOfSources() + ",'" + source + "'," + v.getGene().getId() + ",NOW()," + v.getChangeType() + ")";
                        db.insert(v.getVariant(), query);
                        db.close();
                    }
                    db.close();
                } catch (Exception e) {
                    System.out.println("[Builder] unable to cache variants for " + gene.getHGNC() + "\n\t" + e.toString());
                    Logger.getLogger(API.class.getName()).log(Level.SEVERE, null, e);
                }
            }
        } catch (Exception e) {
        }
    }

    static private void updateGenes() throws IOException {
        String query;
        String query_genelist;
        String[] nextLine;
        String hgnc;
        String name;
        String locus;
        String id;
        DB db = API.getDb();
        CSVReader reader = null;

        try {

            reader = new CSVReader(new FileReader("hgnc_path"), '\t');
        } catch (Exception e) {

            System.out.println("[AllGenes][AddToDB] Unable to add genes to WAVe\n\t" + e.toString());
            Logger.getLogger(Creator.class.getName()).log(Level.SEVERE, null, e);

        }

        while ((nextLine = reader.readNext()) != null) {
            String[] tmp = nextLine[0].split(":");
            id = tmp[1];
            hgnc = nextLine[1];

            try {
                name = nextLine[2];
            } catch (Exception e) {
                System.out.println("[AllGenes][AddToDB] Unable to load " + nextLine[1] + " name\n\t" + e.toString());
                name = "";
            }
            try {
                locus = nextLine[3];
            } catch (Exception e) {
                System.out.println("[AllGenes][AddToDB] Unable to add " + nextLine[1] + " locus\n\t" + e.toString());
                Logger.getLogger(Creator.class.getName()).log(Level.SEVERE, null, e);
                locus = "";
            }

            try {
                query = "INSERT INTO wave#build#_gene (id, hgnc, name, locus, enabled, timestamp) VALUES(" + id + ", '" + hgnc + "','" + name.replace("'", " ").replace("-", " ").replace(",", " ") + "', '" + locus + "',1, NOW())";
                query_genelist = "INSERT INTO wave#build#_genelist (id, symbol, name) VALUES(" + id + ",'" + hgnc + "','" + name.replace("'", " ") + "')";

                db.connect();
                db.insert(hgnc, query);
                db.insert(hgnc, query_genelist);
                db.close();
            } catch (Exception e) {
                System.out.println("[AllGenes][AddToDB] Unable to add " + hgnc + " to WAVe\n\t" + e.toString());
                Logger.getLogger(Creator.class.getName()).log(Level.SEVERE, null, e);

            } finally {
                // show in console
                //System.out.println("[AllGenes][AddToDB] Gene " + gene + " added to database");
            }
        }
        db.close();
        System.out.println("[AllGenes][AddToDB] All genes added to WAVe\n");
    }
}
