/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.ua.bioinformatics.wave.tools;

import com.sun.syndication.io.FeedException;
import java.io.IOException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import pt.ua.bioinformatics.wave.domain.Gene;
import pt.ua.bioinformatics.wave.domain.Leaf;
import pt.ua.bioinformatics.wave.domain.Node;
import pt.ua.bioinformatics.wave.domain.Type;
import pt.ua.bioinformatics.wave.domain.Variant;
import pt.ua.bioinformatics.wave.services.API;
import pt.ua.bioinformatics.wave.services.DB;
import pt.ua.bioinformatics.wave.services.VariantCompare;
import redis.clients.jedis.Jedis;

/**
 *
 * @author pedrolopes
 */
public class Indexer {

    static Jedis j = new Jedis("localhost");

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        cacheGeneList();
        cacheGeneInfo();
        //cacheGeneData();
        //cacheVariantData();
        //cacheVariantFeed();
        //cacheGeneFeed();

    }

    /**
     * Store WAVe gene list as an array on Redis cache
     */
    public static void cacheGeneList() {
        DB db = API.getDb();
        try {
            db.connect();
            ResultSet rs;
            Gene gene = null;
            rs = db.getData("SELECT G.id, G.hgnc "
                    + "FROM wave#build#_gene AS G "
                    + "INNER JOIN wave#build#_genelist AS GL ON GL.id = G.id "
                    + "ORDER BY G.hgnc ASC");

            while (rs.next()) {
                j.sadd("wave:genelist", rs.getString("hgnc"));
            }
            db.close();
            db = null;
        } catch (Exception e) {
            System.out.println("[WAVe][Indexer] unable to cache gene list to Redis");
            Logger.getLogger(Indexer.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    /**
     * Store individual gene info into Redis cache. Iterates through
     * wave:genelist array, connects to DB and loads data.
     */
    public static void cacheGeneInfo() {
        if (!API.isLoaded()) {
            API.load();
        }

        for (String s : j.smembers("wave:genelist")) {
            try {
               
                API.getDb().connect();
                ResultSet rs;
                Gene gene = null;
                rs = API.getDb().getData("SELECT G.id, G.name, G.locus, G.hgnc, G.enabled, GL.lsdb, GL.variant "
                        + "FROM wave#build#_gene AS G "
                        + "INNER JOIN wave#build#_genelist AS GL ON GL.id = G.id "
                        + "WHERE G.hgnc LIKE '" + s + "';");
                while (rs.next()) {
                    gene = new Gene(rs.getInt("id"), rs.getString("hgnc"), rs.getBoolean("enabled"));

                    gene.setNumberOfVariants();
                    gene.loadSequence();
                    j.hset("wave:gene:" + s, "id", rs.getString("id"));
                    j.hset("wave:gene:" + s, "name", rs.getString("name"));
                    j.hset("wave:gene:" + s, "locus", rs.getString("locus"));
                    j.hset("wave:gene:" + s, "lsdb", rs.getString("lsdb"));
                    j.hset("wave:gene:" + s, "variant", rs.getString("variant"));
                    j.hset("wave:gene:" + s, "totalVariant", Integer.toString(gene.getTotalVariants()));
                    j.hset("wave:gene:" + s, "del", Integer.toString(gene.getVariantDel()));
                    j.hset("wave:gene:" + s, "sub", Integer.toString(gene.getVariantSub()));
                    j.hset("wave:gene:" + s, "ins", Integer.toString(gene.getVariantIns()));
                    j.hset("wave:gene:" + s, "inv", Integer.toString(gene.getVariantInv()));
                    j.hset("wave:gene:" + s, "con", Integer.toString(gene.getVariantCon()));
                    j.hset("wave:gene:" + s, "dup", Integer.toString(gene.getVariantDup()));
                    j.hset("wave:gene:" + s, "delins", Integer.toString(gene.getVariantDelins()));
                    j.hset("wave:gene:" + s, "sequence_id", Integer.toString(gene.getSequence().getId()));
                    try {
                        j.hset("wave:gene:" + s, "sequence_info", gene.getSequence().getInfo());
                    } catch (Exception ex) {
                    }
                    try {
                        j.hset("wave:gene:" + s, "sequence_name", gene.getSequence().getName());
                    } catch (Exception ex) {
                    }
                }
                
                //System.out.println("[WAVe][Indexer] cached gene " + s + " data to Redis");
            } catch (Exception e) {
                System.out.println("[WAVe][Indexer] unable to cache gene " + s + " to Redis");
                //Logger.getLogger(Indexer.class.getName()).log(Level.SEVERE, null, e);
                API.getDb().close();
            }
            API.getDb().close();
        }
    }

    /**
     * Store individual gene data (tree) in Redis cache.
     *
     * @throws IOException
     */
    public static void cacheGeneData() throws IOException {
        for (String s : j.smembers("wave:genelist")) {
            try {
                Gene g = new Gene(Integer.parseInt(j.hget("wave:gene:" + s, "id")), s, true);
                ArrayList<Type> data = g.loadInfoForCache();
                JSONObject data_obj = new JSONObject();
                JSONArray types = new JSONArray();
                for (Type t : data) {
                    JSONObject type_obj = new JSONObject();
                    type_obj.put("id", t.getId());
                    type_obj.put("metatype", t.getMetaType());
                    type_obj.put("name", t.getName());
                    type_obj.put("shortname", t.getShortname());
                    type_obj.put("description", t.getDescription());
                    type_obj.put("size", t.getSize());
                    JSONArray nodes = new JSONArray();
                    for (Node n : t.getNodes()) {
                        JSONObject node_obj = new JSONObject();
                        node_obj.put("id", n.getId());
                        node_obj.put("description", n.getDescription());
                        node_obj.put("name", n.getName());
                        node_obj.put("shortname", n.getShortname());
                        node_obj.put("parent", n.getParent().getId());
                        node_obj.put("method", n.getMethod());
                        node_obj.put("ua", n.getUa());
                        node_obj.put("value", n.getValue());

                        JSONArray leafs = new JSONArray();
                        for (Leaf l : n.getLeafs()) {
                            JSONObject leaf_obj = new JSONObject();
                            leaf_obj.put("id", l.getId());
                            leaf_obj.put("name", l.getName());
                            leaf_obj.put("info", l.getInfo());
                            leaf_obj.put("value", l.getValue());
                            leaf_obj.put("n", l.getN());

                            leafs.add(leaf_obj);
                        }
                        node_obj.put("leafs", leafs);
                        nodes.add(node_obj);
                    }
                    type_obj.put("nodes", nodes);
                    types.add(type_obj);
                }
                data_obj.put("data", types);
                j.hset("wave:gene:" + s, "data", data_obj.toJSONString());
                System.out.println("[WAVe][Indexer] Cached >> " + s);
            } catch (Exception e) {
                System.out.println("[WAVe][Indexer] unable to cache tree gene " + s + " to Redis");
                Logger.getLogger(Indexer.class.getName()).log(Level.SEVERE, null, e);
            }
        }
    }

    /**
     * Store individual gene variant data in Redis cache. Data are cached for
     * all variants and specific variant types.
     */
    static void cacheVariantData() {
        //String s = "COL3A1";
        for (String s : j.smembers("wave:genelist")) {
            try {
                cacheAllVariantsForGene(s);
                cacheVariantsForGene(s, "sub");
                cacheVariantsForGene(s, "del");
                cacheVariantsForGene(s, "dup");
                cacheVariantsForGene(s, "ins");
                cacheVariantsForGene(s, "inv");
                cacheVariantsForGene(s, "con");
                cacheVariantsForGene(s, "delins");
            } catch (Exception e) {
                System.out.println("[WAVe][Indexer] unable to cache variant data for gene " + s + " to Redis");
                Logger.getLogger(Indexer.class.getName()).log(Level.SEVERE, null, e);
            }
        }
    }

    /**
     * Store all variants for individual gene in Redis cache. Content is stored
     * as DataTables JSON list.
     *
     * @param s
     */
    static void cacheAllVariantsForGene(String s) {
        ArrayList<Variant> variants;
        JSONObject stream = new JSONObject();
        JSONArray variantList = new JSONArray();
        if (!API.isLoaded()) {
            API.load();
        }
        try {
            Gene gene = new Gene(s);

            variants = gene.getVariants("all");
            Collections.sort(variants, new VariantCompare());
            for (Variant v : variants) {
                if (v.getVariant().startsWith(":")) {
                    v.setVariant(v.getVariant().replace(":", ""));
                }
                JSONArray vList = new JSONArray();
                String vars = "";
                if (v.getNumberOfSources() == 1) {
                    for (Leaf l : v.getSources()) {
                        if (l.getName().contains("home.php")) {
                            String[] tmp = l.getName().split("home.php");
                            String[] getgene = l.getName().split("=");
                            String lsdb_gene = getgene[1];
                            l.setName(tmp[0] + "variants.php?select_db=" + lsdb_gene + "&action=search_all&search_Variant/DNA=" + v.getVariant());
                        }
                        if (!v.getRefseq().equals("")) {
                            vList.add("<a class=\"var\" href=\"" + l.getName() + "\" title=\"All at " + l.getValue().substring(0, 20) + "...\">" + v.getRefseq() + ":" + v.getVariant() + "</a>");
                        } else {
                            vList.add("<a class=\"var\" href=\"" + l.getName() + "\" title=\"All at " + l.getValue().substring(0, 20) + "...\">" + v.getVariant() + "</a>");

                        }
                    }
                } else if (v.getNumberOfSources() > 1) {
                    vars += "<a class='var' rel='source' href='#' title='";
                    for (Leaf l : v.getSources()) {
                        if (l.getName().contains("home.php")) {
                            String[] tmp = l.getName().split("home.php");
                            String[] getgene = l.getName().split("=");
                            String lsdb_gene = getgene[1];
                            l.setName(tmp[0] + "variants.php?select_db=" + lsdb_gene + "&action=search_all&search_Variant/DNA=" + v.getVariant());
                        }
                        vars += "<a href=\"" + l.getName() + "\">" + l.getN() + " at " + l.getValue().substring(0, 20) + "...</a><br />";
                    }
                    if (!v.getRefseq().equals("")) {
                        vars += "'>" + v.getRefseq() + ":" + v.getVariant() + "</a>";
                    } else {
                        vars += "'>" + v.getVariant() + "</a>";
                    }
                    vList.add(vars);
                }
                boolean found = false;
                for (Type t : API.getTypes().values()) {
                    if (v.getChangeType() == t.getId()) {
                        vList.add(t.getName());
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    vList.add("Unknown");
                } else {
                    found = false;
                }
                vList.add(String.valueOf(v.getNumberOfSources()));
                vList.add(String.valueOf(v.getN()));
                variantList.add(vList);
            }
            stream.put("aaData", variantList);
            j.set("wave:variant:all:" + s, stream.toJSONString());
            System.out.println("[WAVe][Indexer] cached all variants for " + s);
        } catch (Exception e) {
            System.out.println("[WAVe][Indexer] unable to all variant data for gene " + s + " to Redis");
            Logger.getLogger(Indexer.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    /**
     * Store specific variant types for individual gene in Redis cache. Content
     * is stored as DataTables JSON list.
     *
     * @param s
     */
    static void cacheVariantsForGene(String s, String type) {
        ArrayList<Variant> variants;
        JSONObject stream = new JSONObject();
        JSONArray variantList = new JSONArray();
        Type t = API.getTypes().get(type);
        Gene gene = new Gene(s);
        variants = gene.getVariants(type);
        Collections.sort(variants, new VariantCompare());
        try {
            for (Variant v : variants) {
                JSONArray vList = new JSONArray();
                String vars = "";
                if (v.getNumberOfSources() == 1) {
                    for (Leaf l : v.getSources()) {
                        if (l.getName().contains("home.php")) {
                            String[] tmp = l.getName().split("home.php");
                            String[] getgene = l.getName().split("=");
                            String lsdb_gene = getgene[1];
                            l.setName(tmp[0] + "variants.php?select_db=" + lsdb_gene + "&action=search_all&search_Variant/DNA=" + v.getVariant());
                        }
                        if (!v.getRefseq().equals("")) {
                            vList.add("<a class=\"var\" href=\"" + l.getName() + "\" title=\"All at " + l.getValue().substring(0, 20) + "...\">" + v.getRefseq() + ":" + v.getVariant() + "</a>");
                        } else {
                            vList.add("<a class=\"var\" href=\"" + l.getName() + "\" title=\"All at " + l.getValue().substring(0, 20) + "...\">" + v.getVariant() + "</a>");

                        }
                    }
                } else if (v.getNumberOfSources() > 1) {
                    vars += "<a class='var' rel='source' href='#' title='";
                    for (Leaf l : v.getSources()) {
                        if (l.getName().contains("home.php")) {
                            String[] tmp = l.getName().split("home.php");
                            String[] getgene = l.getName().split("=");
                            String lsdb_gene = getgene[1];
                            l.setName(tmp[0] + "variants.php?select_db=" + lsdb_gene + "&action=search_all&search_Variant/DNA=" + v.getVariant());
                        }
                        vars += "<a href=\'" + l.getName() + "\'>" + l.getN() + " at " + l.getValue().substring(0, 20) + "...</a><br />";
                    }
                    if (!v.getRefseq().equals("")) {
                        vars += "'>" + v.getRefseq() + ":" + v.getVariant() + "</a>";
                    } else {
                        vars += "'>" + v.getVariant() + "</a>";
                    }
                    vList.add(vars);
                }
                vList.add(t.getName());
                vList.add(String.valueOf(v.getNumberOfSources()));
                vList.add(String.valueOf(v.getN()));
                variantList.add(vList);
            }
            stream.put("aaData", variantList);
            j.set("wave:variant:" + type + ":" + s, stream.toJSONString());
            System.out.println("[WAVe][Indexer] cached " + type + " variants for " + s);
        } catch (Exception e) {
            System.out.println("[WAVe][Indexer] unable to cache " + type + " variant data for gene " + s + " to Redis");
            Logger.getLogger(Indexer.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    /**
     * Store the variant feed (Atom format) for each individual gene.
     */
    private static void cacheVariantFeed() {
        // String s = "DMD";
        if (!API.isLoaded()) {
            API.load();
        }
        for (String s : j.smembers("wave:genelist")) {
            try {

                API.getJedis().set("wave:variant:feed:" + s, API.variantFeed(new Gene(s), "atom_1.0"));
                System.out.println("[WAVe][Indexer] cached variant feed for " + s);

            } catch (IOException ex) {
                System.out.println("[WAVe][Indexer] unable to cache variant feed for gene " + s + " to Redis");
                Logger.getLogger(Indexer.class.getName()).log(Level.SEVERE, null, ex);
            } catch (FeedException ex) {
                System.out.println("[WAVe][Indexer] unable to cache variant feed for gene " + s + " to Redis");
                Logger.getLogger(Indexer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Store the gene feed (Atom format) for each individual gene.
     */
    static void cacheGeneFeed() {
        // String s = "COL3A1";
        if (!API.isLoaded()) {
            API.load();
        }
        for (String s : j.smembers("wave:genelist")) {
            try {
                API.getJedis().hset("wave:gene:" + s, "feed", API.geneFeed(new Gene(s), "atom_1.0"));
                System.out.println("[WAVe][Indexer] cached variant feed for " + s);
            } catch (FeedException ex) {
                System.out.println("[WAVe][Indexer] unable to cache gene feed for " + s + " to Redis");
                Logger.getLogger(Indexer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}