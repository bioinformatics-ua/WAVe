package pt.ua.bioinformatics.wave.varcrawler;

import au.com.bytecode.opencsv.CSVReader;
import java.io.FileReader;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import pt.ua.bioinformatics.wave.domain.Gene;
import pt.ua.bioinformatics.wave.domain.LSDB;
import pt.ua.bioinformatics.wave.domain.Leaf;
import pt.ua.bioinformatics.wave.domain.Node;
import pt.ua.bioinformatics.wave.domain.Type;
import pt.ua.bioinformatics.wave.domain.Variant;
import pt.ua.bioinformatics.wave.services.API;
import pt.ua.bioinformatics.wave.services.DB;
import pt.ua.bioinformatics.wave.services.GeneList;
import pt.ua.bioinformatics.wave.services.Settings;

/**
 *
 * @author pedrolopes
 * @version 1.2, 2010-12-17
 */
public class Builder {

    /**
     * Loads Locus Specific DataBase data from configuration file and gets
     * variants for each gene.
     * <p><b>Feature:</b><br />
     * Loads data from local CSV LSDB listing file and process each gene-LSDB
     * combination reading variants and adding them to WAVe database.<br />
     * <b>Operation:</b>
     * <ul>
     * <li>Load LSDB listing CSV configuration file</li>
     * <li>Read all data to memory and parse each gene-LSDB according to its
     * type</li>
     * <li>IDBases
     * <ul>
     * <li>Found if address contains 'bioinf.uta.fi'</li>
     * <li>Instances the crawler and loads Arabella</li>
     * <li>Reads variants and adds them to WAVe database</li>
     * </ul>
     * </li>
     * <li>UMD
     * <ul>
     * <li>Found if address contains 'umd.be'</li>
     * <li>Modifies the crawling address for local UMD address (no direct
     * crawler access to UMD)</li>
     * <li>Instances the crawler and loads Arabella</li>
     * <li>Reads variants and adds them to WAVe database</li>
     * </ul>
     * </li>
     * <li>LOVD
     * <ul>
     * <li>Found if address contains 'select_db'</li>
     * <li>Reads LOVD version</li>
     * <li>If bigger than '2.0-24' loads FeedReads and reads the variants</li>
     * <li>If otherwise (or if fails to load feed data) instances the crawler
     * and loads Arabella</li>
     * </ul>
     * </li>
     * <li>Generic
     * <ul>
     * <li>If LSDB address does not match any of the previous cases</li>
     * <li>Tests additional 'info'</li>
     * <li>If it contains an address, instances the crawler and loads Arabella
     * with that address</li>
     * <li>If not, instances the crawler and loads Arabella with the main LSDB
     * address</li>
     * <li>Reads variants and adds them to WAVe database</li>
     * </ul>
     * </li>
     * </ul>
     * </p>
     *
     * @return
     */
    public boolean loadLSDBs() {
        boolean success = false;
        try {
            CSVReader reader = new CSVReader(new FileReader(Settings.getPath() + "lsdb_list.csv "), ',', '"', 0);
            Crawler crawl;
            Leaf leaf;
            List<String[]> lsdbs = reader.readAll();
            for (String[] lsdb : lsdbs) {
                
                String hgnc = lsdb[0];
                String address = lsdb[1];
                String info = lsdb[3];
                String lsdbType = lsdb[2];
                //System.out.println("»»» " + hgnc + " @ " + address + " i:" + info);
                Gene gene = new Gene(hgnc);
                if (lsdbType.equals("LOVD")) {
                    String[] lovdtmp = address.split("home.php");
                    String[] splitgene = address.split("=");
                    String g = splitgene[1];
                    info = lovdtmp[0] + "variants.php?select_db=" + g + "&action=view_unique&order=Variant/DNA,ASC&hide_col=&show_col=&limit=1000";
                }
                if (info.equals("")) {
                    info = address;
                }
                
                
                LSDB locusdb = new LSDB(new Node(lsdbType), gene, address, info);
                locusdb.addToDB();
                
                leaf = new Leaf(address);

                //System.out.println("Leaf " + leaf.getId() + ":" + leaf.getName() + ":" + leaf.getValue());

                if (lsdbType.equals("MUTbase")) {

                    //System.out.println("[Builder] " + hgnc + " " + lsdbType + " at " + info);
                    crawl = new Crawler(gene, lsdbType);
                    if (crawl.readVariants(info, leaf)) {
                        //System.out.println("[Builder][" + lsdbType + "] Read variants for " + hgnc + " at " + info);
                    } else {
                        //System.out.println("[Builder][" + lsdbType + "] Unable to read variants for " + hgnc + " at " + info);
                    }
                    
                    
                } else if (lsdbType.equals("UMD")) {
                    //System.out.println("[Builder] " + hgnc + " " + lsdbType);
                    crawl = new Crawler(gene, lsdbType);
                    String newAddress = "http://localhost:80/UMD/" + hgnc + ".htm";
                    if (crawl.readVariants(newAddress, leaf)) {
                        System.out.println("[Builder][" + lsdbType + "] Read variants for " + hgnc + " at " + newAddress);
                    } else {
                        System.out.println("[Builder][" + lsdbType + "] Unable to read variants for " + hgnc + " at " + newAddress);
                    }
                    
                } else if (lsdbType.equals("LOVD")) {
                    //System.out.println("[Builder] " + hgnc + " " + lsdbType);
                    info = lsdb[3];
                    String[] lovdtmp = address.split("home.php");
                    String[] splitgene = address.split("=");
                    String g = splitgene[1];

                    // only last version have feeds!
                    boolean lastVersion = true;
                    if (info.equals("2.0-24") || info.equals("2.0-25") || info.equals("2.0-26") || info.equals("2.0-27") || info.equals("2.0-28") || info.equals("2.0-29") || info.equals("2.0-30") || info.equals("2.0-31") || info.equals("2.0-32") || info.equals("3.0-alpha-03") || info.equals("2.0-32b")) {
                        lastVersion = true;
                    } else {
                        lastVersion = false;
                    }

                    // if last version just read feed, else crawl
                    if (lastVersion) {
                        String feedAddress = lovdtmp[0] + "api/rest.php/variants/" + g;
                        try {
                            LOVDFeedReader feedReader = new LOVDFeedReader(gene, feedAddress, lsdbType);
                            lastVersion = feedReader.readVariants(leaf);
                        } catch (Exception ex) {
                            lastVersion = false;
                        }
                    }
                    if (!lastVersion) {
                        String urlAddress = lovdtmp[0] + "variants.php?select_db=" + g + "&action=view_unique&order=Variant/DNA,ASC&hide_col=&show_col=&limit=1000";
                        crawl = new Crawler(gene, lsdbType);
                        if (crawl.readVariants(urlAddress, leaf)) {
                            //System.out.println("[Builder][" + lsdbType + "] Crawled variants for " + hgnc + " at " + urlAddress);
                        } else {
                            //System.out.println("[Builder][" + lsdbType + "] Unable to crawl variants for " + hgnc + " at " + urlAddress);
                        }
                        
                    }
                } else {
                    lsdbType = "Unknown";
                    crawl = new Crawler(gene, lsdbType);
                    
                    if (info.equals("")) {
                        if (crawl.readVariants(address, leaf)) {
                            //    System.out.println("[Builder][" + lsdbType + "] Read variants for " + hgnc + " at " + address);
                        } else {
                            //     System.out.println("[Builder][" + lsdbType + "] Unable to read variants for " + hgnc + " at " + address);
                        }
                    } else {
                        if (crawl.readVariants(info, leaf)) {
                            //     System.out.println("[Builder][" + lsdbType + "] Read variants for " + hgnc + " at " + info);
                        } else {
                            //      System.out.println("[Builder][" + lsdbType + "] Unable to read variants for " + hgnc + " at " + info);
                        }
                    }
                    
                }
                // LSDB locusdb = new LSDB(new Node(lsdbType), gene, address);
                //locusdb.addToDB();
                //gene.enable();*/
                gene.enable();
            }
            success = true;
            System.out.println("[Builder] Loaded LSDBs from file");
        } catch (Exception e) {
            System.out.println("[Builder] Unable to load LSDBs from file\n\t");
            Logger.getLogger(Builder.class.getName()).log(Level.SEVERE, null, e);
        }
        return success;
    }

    /**
     * Loads gene information (number of LSDB, number of Variants) to genelist
     * table for performance improvement.
     *
     * @return success of the operation
     */
    public boolean cacheGenes() {
        boolean success = false;
        DB db = new DB();
        DB dbx = new DB();
        Type t = API.getTypes().get("geneleaf");
        API.load();
        Gene gene = null;
        
        try {
            db.connect();
            //System.out.println("Loading gene information");

            ResultSet rs = db.getData("SELECT G.id, G.hgnc, G.enabled, COUNT(A.id) AS lsdbs "
                    + "FROM wave#build#_gene AS G "
                    + "INNER JOIN wave#build#_association AS A ON A.a = G.id "
                    + "INNER JOIN wave#build#_leaf AS L ON A.b = L.id "
                    + "WHERE G.enabled = TRUE AND A.reftypeid = " + t.getId() + " AND L.refnodeid IN " + API.getLsdbs() + " GROUP BY G.id");
            
            while (rs.next()) {
                gene = new Gene(rs.getInt("id"), rs.getString("hgnc"), rs.getBoolean("enabled"));
                gene.setNumberOfVariants();
                dbx.connect();
                dbx.insert("Gene " + rs.getString("hgnc"), "UPDATE wave#build#_genelist SET lsdb = " + rs.getString("lsdbs") + ", variant = " + gene.getNumberOfVariants() + " WHERE id = " + rs.getString("id"));
                dbx.close();
            }
            System.out.println("[Stats] Loaded all genes");
            db.close();
            success = true;
        } catch (Exception ex) {
            System.out.println("[Stats] Unable to update gene statistics\n\t" + ex.toString());
        }
        return success;
    }
    
    public boolean cacheVariants() {
        boolean success = false;
        GeneList genelist = GeneList.INSTANCE;
        ArrayList<Gene> genes = genelist.getEnabled();
        if (!API.isLoaded()) {
            API.load();
        }
        DB db = API.getDb();
        Type t = API.getTypes().get("variantleaf");
        // for (Gene gene : genes) {
        Gene gene = new Gene("DMD");
        gene.setId(2928);
        ArrayList<Variant> variants = new ArrayList<Variant>();
        if (true) { //gene.getNumberOfVariants() > 0) {
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
                    System.out.println(v.getId() + ":" + v.getVariant() + ":" + v.getN() + ":" + v.getNumberOfSources() + ":" + source + ":" + v.getGene().getId() + ":" + v.getChangeType());

                    db.connect();
                    String query = "INSERT INTO wave.wave#build#_variantlist (id, refseq, variant,n,lsdb, source, refgeneid, timestamp,refchangetypeid)"
                            + " VALUES(" + v.getId() + ", '" + v.getRefseq() + "','" + v.getVariant() + "'," + v.getN() + "," + v.getNumberOfSources() + ",'" + source + "'," + v.getGene().getId() + ",NOW()," + v.getChangeType() + ")";
                    db.insert(v.getVariant(), query);
                    db.close();
                }
                success = true;
                db.close();
            } catch (Exception e) {
                System.out.println("[Builder] unable to cache variants for " + gene.getHGNC() + "\n\t" + e.toString());
                Logger.getLogger(API.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        // }
        return success;
    }
    
    public boolean buildIndex() {
        boolean success = false;
        DB db = API.getDb();
        DB dbx = new DB();
        GeneList gl = GeneList.INSTANCE;
        Settings.load();
        String[] indexed = Settings.getIndex().split("\\|");
        try {
            for (String s : indexed) {
                if (s.equals("hgnc")) {
                    db.connect();
                    ResultSet rs = db.getData("SELECT symbol AS v FROM  wave.wave#build#_genelist");
                    while (rs.next()) {
                        dbx.connect();
                        dbx.insert(rs.getString("v"), "INSERT INTO wave.wave#build#_index (v, refnodeid,gene) VALUES('" + rs.getString("v") + "',1,'" + rs.getString("v") + "')");
                        dbx.close();
                    }
                    db.close();
                    db.connect();
                    rs = db.getData("SELECT id AS v FROM  wave.wave#build#_genelist");
                    while (rs.next()) {
                        dbx.connect();
                        dbx.insert(rs.getString("v"), "INSERT INTO wave.wave#build#_index (v, refnodeid,gene) VALUES('" + rs.getString("v") + "',1,'" + rs.getString("v") + "')");
                        dbx.close();
                    }
                    db.close();
                    System.out.println("[Builder] indexed all gene HGNC");
                } else {
                    db.connect();
                    ResultSet rs = db.getData("SELECT L.name AS v, G.hgnc AS g FROM wave#build#_leaf L INNER JOIN wave#build#_association A ON L.id = A.b INNER JOIN wave#build#_gene AS G ON A.a = G.id WHERE L.refnodeid = " + API.getNodes().get(s).getId());
                    while (rs.next()) {
                        dbx.connect();
                        dbx.insert(rs.getString("v"), "INSERT INTO wave.wave#build#_index (v, refnodeid,gene)VALUES('" + rs.getString("v") + "'," + API.getNodes().get(s).getId() + ",'" + rs.getString("g") + "')");
                        dbx.close();
                    }
                    db.close();
                    System.out.println("[Builder] indexed all " + s + API.getNodes().get(s).getId());
                }
            }
            success = true;
        } catch (Exception ex) {
            System.out.println("[Builder] Unable to build WAVe index " + ex.toString());
            Logger.getLogger(Builder.class.getName()).log(Level.SEVERE, null, ex);
        }
        return success;
    }
    
    public boolean correctGenes() {
        boolean success = false;
        DB db = API.getDb();
        DB dbx = new DB();
        //HashMap<String, String> correct = new HashMap();
        //correct.put("FANCD1", "BRCA2");
        //correct.put("DMD_d", "DMD");

        try {
            db.connect();
            ResultSet rs = db.getData("SELECT * FROM wave#build#_gene");
            
            while (rs.next()) {
                Gene gene = new Gene(rs.getInt("id"), rs.getString("hgnc"), rs.getBoolean("enabled"));
                gene.setName(rs.getString("name"));
                //gene.setLocus(rs.getString("locus"));
                //gene.setNumberOfLsdbs(rs.getInt("lsdbs"));
                //gene.setNumberOfVariants();
                // System.out.println("[Stats] Inserting gene " + gene.getHGNC());
                dbx.connect();
                dbx.insert("Gene " + rs.getString("hgnc"), "INSERT INTO wave#build#_genelist (id, symbol,name,lsdb,variant) VALUES(" + gene.getId() + ", '" + gene.getHGNC() + "','" + gene.getName() + "', 0, 0)");
                dbx.close();
            }
            
            db.close();
            
            success = true;
        } catch (Exception ex) {
            System.out.println("[Stats] Unable to update gene statistics\n\t" + ex.toString());
        }
        return success;
    }
}
