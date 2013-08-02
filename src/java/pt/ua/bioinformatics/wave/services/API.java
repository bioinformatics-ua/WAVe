package pt.ua.bioinformatics.wave.services;

import com.sun.syndication.feed.synd.SyndCategory;
import com.sun.syndication.feed.synd.SyndCategoryImpl;
import com.sun.syndication.feed.synd.SyndContent;
import com.sun.syndication.feed.synd.SyndContentImpl;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndEntryImpl;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.feed.synd.SyndFeedImpl;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedOutput;
import java.io.IOException;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import pt.ua.bioinformatics.wave.domain.Gene;
import pt.ua.bioinformatics.wave.domain.Leaf;
import pt.ua.bioinformatics.wave.domain.Node;
import pt.ua.bioinformatics.wave.domain.Type;
import pt.ua.bioinformatics.wave.domain.Variant;
import redis.clients.jedis.Jedis;

/**
 *
 * @author pedrolopes
 * @version 1.2, 2010-12-17
 */
public class API {

    public final static API INSTANCE = new API();
    static final DateFormat DATE_PARSER = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    static HashMap<String, Node> nodes = new HashMap();
    static HashMap<String, Integer> methods = new HashMap();
    static HashMap<String, Type> types = new HashMap();
    static boolean nodesLoaded = false;
    static boolean methodsLoaded = false;
    static boolean typesLoaded = false;
    static boolean loaded = false;
    static DB db = new DB();
    static Jedis jedis = null;      // Redis cache client

    public static Jedis getJedis() {
        return jedis;
    }

    public static void setJedis(Jedis jedis) {
        API.jedis = jedis;
    }

    public static DB getDb() {
        return db;
    }

    public static void setDb(DB db) {
        API.db = db;
    }

    public static boolean isLoaded() {
        return loaded;
    }

    public static void setLoaded(boolean loaded) {
        API.loaded = loaded;
    }

    public static HashMap<String, Integer> getMethods() {
        return methods;
    }

    public static void setMethods(HashMap<String, Integer> methods) {
        API.methods = methods;
    }

    public static boolean isMethodsLoaded() {
        return methodsLoaded;
    }

    public static void setMethodsLoaded(boolean methodsLoaded) {
        API.methodsLoaded = methodsLoaded;
    }

    public static HashMap<String, Node> getNodes() {
        return nodes;
    }

    public static void setNodes(HashMap<String, Node> nodes) {
        API.nodes = nodes;
    }

    public static boolean isNodesLoaded() {
        return nodesLoaded;
    }

    public static void setNodesLoaded(boolean nodesLoaded) {
        API.nodesLoaded = nodesLoaded;
    }

    public static HashMap<String, Type> getTypes() {
        return types;
    }

    public static void setTypes(HashMap<String, Type> types) {
        API.types = types;
    }

    public static boolean isTypesLoaded() {
        return typesLoaded;
    }

    public static void setTypesLoaded(boolean typesLoaded) {
        API.typesLoaded = typesLoaded;
    }

    /*
     * Loads all default info for correct API usage
     */
    public static boolean load() {
        try {
            if (!nodesLoaded) {
                loadNodes();
            }
            if (!methodsLoaded) {
                loadMethods();
            }
            if (!typesLoaded) {
                loadTypes();
            }
            jedis = new Jedis("localhost");
            loaded = true;
        } catch (Exception e) {
            System.out.println("[API] unable to load all data\n\t" + e.toString());
        }
        return loaded;
    }

    /**
     * Registers a new API call in WAVe's database.
     *
     * @param caller the Gene from wher dataList was requested
     * @param method the API method used
     * @return
     */
    public static boolean register(int caller, int method) {
        boolean success = false;
        try {
            db.connect();
            db.insert("API Call Gene Atom", "INSERT INTO wave#build#_api (method, caller, timestamp) VALUES(" + method + "," + caller + ",NOW())");
            db.close();
            success = true;
        } catch (Exception ex) {
            System.out.println("[API] unable to register API call");
        }
        return success;
    }

    /**
     * Loads information regarding WAVe gene tree Nodes for Universal Access
     * API.
     *
     * @return
     */
    public static boolean loadNodes() {
        if (!nodesLoaded) {
            if (!Settings.loaded) {
                Settings.load();
            }

            if (Settings.modules.get("api")) {
                try {
                    db.connect();

                    ResultSet rs = db.getData("SELECT * FROM wave#build#_node");
                    while (rs.next()) {
                        nodes.put(rs.getString("ua"), new Node(rs.getInt("id"), rs.getString("name"), rs.getString("value"), rs.getString("ua")));
                    }
                    nodesLoaded = true;
                } catch (Exception ex) {
                    System.out.println("[UniversalAccess] unable to load node information\n" + ex.toString());
                } finally {
                    db.close();
                }
            }
        }
        return nodesLoaded;
    }

    /**
     * Loads information regarding WAVe types for Universal Access API.
     *
     * @return
     */
    public static boolean loadTypes() {
        if (!typesLoaded) {
            if (!Settings.loaded) {
                Settings.load();
            }

            if (Settings.modules.get("api")) {
                try {
                    db.connect();

                    ResultSet rs = db.getData("SELECT * FROM wave#build#_type");
                    while (rs.next()) {
                        types.put(rs.getString("shortname"), new Type(rs.getInt("id"), rs.getString("name"), rs.getString("shortname"), rs.getString("description"), rs.getInt("refmetatypeid")));
                        //System.out.println("\t" + rs.getString("shortname") + "\t" + types.get(rs.getString("shortname")).getId());
                    }
                    typesLoaded = true;
                } catch (Exception ex) {
                    System.out.println("[UniversalAccess] unable to load type information\n" + ex.toString());
                    Logger.getLogger(API.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    db.close();
                }
            }
        }
        /* for (Type t : types.values()) {
         System.out.println("\t" + t.getShortname() + ":" + t.getId());
         }
         System.out.println(types.get("delins").getShortname() + types.get("delins").getId());*/
        return typesLoaded;
    }

    /**
     * Loads information regarding WAVe API methods for API usage.
     *
     * @return
     */
    private static boolean loadMethods() {
        if (!methodsLoaded) {
            if (!Settings.loaded) {
                Settings.load();
            }

            if (Settings.modules.get("api")) {
                try {
                    db.connect();
                    ResultSet rs = db.getData("SELECT * FROM wave#build#_type WHERE refmetatypeid = 14");
                    while (rs.next()) {
                        methods.put(rs.getString("shortname"), rs.getInt("id"));
                    }
                    methodsLoaded = true;
                } catch (Exception ex) {
                    System.out.println("[UniversalAccess] unable to load method information\n" + ex.toString());
                } finally {
                    db.close();
                }
            }
        }
        return methodsLoaded;
    }

    /**
     * Hanlder for calls to WAVe Universal Access API.
     * <p>
     * The Universal Access API enables direct access to integrated pages within
     * WAVe gene context and using a single address location.<br /><b>Sample</b>
     * <ul>
     * <li>http://bioinformatics.ua.pt/WAVe/gene/BRCA2/uniprot:P51587</li>
     * </ul>
     * <br />Available identifiers are all Nodes (check configuration file for
     * UA field)
     * </p>
     *
     * @param query
     * @param caller
     * @return
     */
    public static String getUA(String query, int caller) {
        if (!nodesLoaded) {
            loadNodes();
        }
        if (!methodsLoaded) {
            loadMethods();
        }
        //System.out.println(query);
        String[] full = query.split(":");
        full[0] = full[0].toLowerCase();
        //register(caller, methods.get("ua"));
        //System.out.println(nodes.get(full[0]).getValue());
        if (full.length > 2) {
            return nodes.get(full[0]).getValue().replace("#replaceme#", full[1] + ":" + full[2]);
        } else {
            if (full[0].equals("lsdb")) {
                return getLsdb(full[1]);
            }
            return nodes.get(full[0]).getValue().replace("#replaceme#", full[1]);
        }
    }

    /**
     * API handler for Gene Navigation Tree API requests.
     *
     * @param gene the Gene
     * @param method the API method (corresponds to output format)
     * @return
     */
    public static String getGeneTree(Gene gene, String method) {
        String response = "";
        if (!methodsLoaded) {
            loadMethods();
        }
        if (method.equals("atom_1.0")) {
            try {
                response = geneFeed(gene, method);
                //   register(gene.getId(), methods.get("geneatom"));
            } catch (FeedException ex) {
                Logger.getLogger(API.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (method.equals("json")) {
            try {
                response = geneFeed(gene, method);
                // register(gene.getId(), methods.get("genejson"));
            } catch (Exception ex) {
                Logger.getLogger(API.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return response;
    }

    /**
     * API handler for Gene Variant list API requests.
     *
     * @param gene the Gene
     * @param method the API method (corresponds to output format)
     * @return
     */
    public static String getGeneVariants(Gene gene, String method) {
        String response = "";
        if (!methodsLoaded) {
            loadMethods();
        }
        if (method.equals("atom_1.0")) {
            try {
                response = variantFeed(gene, method);
                //  register(gene.getId(), methods.get("variantatom"));
            } catch (Exception ex) {
                Logger.getLogger(API.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("[API][Variant] unable to call API for gene variants " + method + "\n" + ex.toString());
            }
        } else if (method.equals("json")) {
            //  register(gene.getId(), methods.get("variantjson"));
        }
        return response;
    }

    /**
     * API handler for Gene Browse API requests.
     *
     * @param gene the Gene
     * @param method the API method (corresponds to output format)
     * @return
     */
    public static String getGenes(ArrayList<Gene> genes, String method) {
        String response = "";
        if (!methodsLoaded) {
            loadMethods();
        }

        if (method.equals("atom_1.0")) {
            try {
                response = browseFeed(genes, method);
                //register(0, methods.get("browseatom"));
            } catch (Exception ex) {
                Logger.getLogger(API.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (method.equals("json")) {
            try {
                response = browseFeed(genes, method);
                //  register(0, methods.get("browsejson"));
            } catch (Exception ex) {
                Logger.getLogger(API.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return response;
    }

    /**
     * Loads all genes in feed format
     *
     * @param feedType
     * @return
     */
    static String browseFeed(ArrayList<Gene> genes, String feedType) throws FeedException {
        SyndFeed feed = new SyndFeedImpl();
        feed.setFeedType(feedType);
        feed.setTitle("All genes | WAVe");
        feed.setLink("http://bioinformatics.ua.pt/WAVe");
        feed.setDescription("WAVe | Web Analysis of the Variome variant listing for all genes.");
        feed.setAuthor("http://bioinformatics.ua.pt");
        feed.setCopyright("WAVe (c) UA.PT Bioinformatics & Computational Biology 2013");
        List entries = new ArrayList();
        try {
            for (Gene g : genes) {
                SyndEntry entry;
                SyndContent description;
                entry = new SyndEntryImpl();
                entry.setTitle(g.getHGNC());
                entry.setLink(Settings.getHost() + "WAVe/gene/" + g.getHGNC());
                entry.setPublishedDate(new java.util.Date());
                description = new SyndContentImpl();
                description.setType("text/plain");
                description.setValue(g.getName() + " at " + g.getLocus());
                entry.setDescription(description);
                entries.add(entry);
            }
            feed.setEntries(entries);
        } catch (Exception ex) {
            Logger.getLogger(API.class.getName()).log(Level.SEVERE, null, ex);
        }
        SyndFeedOutput output = new SyndFeedOutput();

        return output.outputString(feed);
    }

    /**
     * Loads gene variant dataList in feed format.
     * <p><b>Feature:</b><br />
     * Builds a generic String containing the feed structure that is passed to
     * the web API.<br /><br/>
     * <b>Operation:</b>
     * <ul>
     * <li>Initiate document definition</li>
     * <li>Read variants to Feed</li>
     * <li>Finalize Feed String and return it</li>
     * </ul>
     * </p>
     *
     * @param g the gene
     * @param feedType Output feed type (available types: rss_2.0, atom_1.0)
     * @return A string containing the complete gene RSS feed.
     */
    public static String variantFeed(Gene g, String feedType) throws IOException, FeedException {
        SyndFeed feed = new SyndFeedImpl();
        feed.setFeedType(feedType);
        feed.setTitle("Variant list | WAVe");
        feed.setLink("http://bioinformatics.ua.pt/WAVe");
        feed.setDescription("WAVe | Web Analysis of the Variome variant listing for " + g.getHGNC() + " gene.");
        feed.setAuthor("http://bioinformatics.ua.pt");
        feed.setCopyright("WAVe (c) UA.PT Bioinformatics & Computational Biology 2013");
        List entries = new ArrayList();

        try {

            for (Variant v : g.getVariants("all")) {
                if (v.getVariant().startsWith(":")) {
                    v.setVariant(v.getVariant().replace(":", ""));
                }
                for (Leaf l : v.getSources()) {

                    SyndEntry entry;
                    SyndContent description;

                    entry = new SyndEntryImpl();
                    entry.setTitle(v.getVariant().replace("&gt;", ">"));
                    entry.setLink(l.getValue());
                    entry.setPublishedDate(new java.util.Date());
                    description = new SyndContentImpl();
                    description.setType("text/plain");
                    if (!v.getRefseq().equals("")) {
                        description.setValue(v.getRefseq() + ":" + v.getVariant().replace("&gt;", ">"));
                    } else {
                        description.setValue(v.getVariant().replace("&gt;", ">"));
                    }
                    entry.setDescription(description);
                    entries.add(entry);
                }
            }

            feed.setEntries(entries);

        } catch (Exception e) {
            System.out.println("[FeedGenerator] error " + e.toString());
        }

        SyndFeedOutput output = new SyndFeedOutput();

        return output.outputString(feed);
    }

    /**
     * Loads the gene navigation tree dataList in feed format.
     * <p><b>Feature:</b><br />
     * Builds a generic String containing the feed structure that is passed to
     * the web API.<br /><br/>
     * <b>Operation:</b>
     * <ul>
     * <li>Initiate document definition</li>
     * <li>Read node list and add node information to Feed</li>
     * <li>Read leaf list for each node and add leaf information to Feed</li>
     * <li>Finalize Feed String and return it</li>
     * </ul>
     * </p>
     *
     * @param g the gene
     * @param feedType Output feed type (available types: rss_2.0, atom_1.0)
     * @return A string containing the complete gene RSS feed.
     */
    public static String geneFeed(Gene g, String feedType) throws FeedException {

        if (feedType.equals("json")) {

            JSONObject obj = new JSONObject();
            obj.put("status", "1");
            obj.put("gene", g.getHGNC());
            JSONArray dataList = new JSONArray();

            ArrayList<Type> datatypes = g.loadInfo();

            try {
                JSONObject typesObj = new JSONObject();
                for (Type t : datatypes) {
                    if (t.getName().equals("LSDB")) {
                        JSONArray nodesList = new JSONArray();
                        JSONObject nodesObj = new JSONObject();
                        for (Node node : t.getNodes()) {
                            for (Leaf leaf : node.getLeafs()) {
                                String url = Settings.getHost() + "WAVe/gene/" + g.getHGNC() + "/" + leaf.getId();
                                JSONObject link = new JSONObject();
                                link.put(node.getShortname(), url);
                                nodesList.add(link);
                            }
                        }
                        nodesObj.put("lsdb", nodesList);
                        dataList.add(nodesObj);
                    } else if (t.getName().equals("Variation") && (g.getNumberOfVariants() > 0)) {
                        JSONArray variants = new JSONArray();
                        // TODO: deal with variants
                    } else {
                        JSONArray type = new JSONArray();
                        JSONObject nodeObj = new JSONObject();
                        for (Node node : t.getNodes()) {

                            JSONArray nodeList = new JSONArray();
                            // check Static types (Static has value and has leafs)
                            JSONObject leafObj = new JSONObject();
                            if (!node.getMethod().equals("direct")) {
                                if (!node.getLeafs().isEmpty()) {

                                    for (Leaf leaf : node.getLeafs()) {
                                        String title = node.getUa() + ":" + leaf.getValue();
                                        String link = "";
                                        if (leaf.getValue().contains(":")) {
                                            link = Settings.getHost() + "WAVe/gene/" + g.getHGNC() + "/" + leaf.getValue();
                                            if (leaf.getValue().contains("hsa:")) {
                                                link = Settings.getHost() + "WAVe/gene/" + g.getHGNC() + "/" + node.getUa() + ":" + leaf.getValue();
                                            }
                                        } else {
                                            link = Settings.getHost() + "WAVe/gene/" + g.getHGNC() + "/" + node.getUa() + ":" + leaf.getValue();
                                        }
                                        // set JSON object for leaf
                                        leafObj.put(title, link);
                                    }
                                }
                                // check Dynamic types (method direct or onload)
                            } else {
                                String title = node.getUa() + ":" + g.getHGNC();
                                String link = Settings.getHost() + "WAVe/gene/" + g.getHGNC() + "/" + node.getUa() + ":" + g.getHGNC();
                                // Set JSON object for leaf
                                leafObj.put(title, link);
                            }
                            nodeList.add(leafObj);
                            nodeObj.put(node.getShortname(), nodeList);
                            type.add(nodeList);
                        }

                        dataList.add(type);
                    }
                    typesObj.put(t.getName(), dataList);
                }

            } catch (Exception e) {
                System.out.println("[API][JSON] Unable to load JSON feed\n\t" + e.toString());
            }

            obj.put("data", dataList);
            return obj.toJSONString();
        } else {

            SyndFeed feed = new SyndFeedImpl();
            feed.setFeedType(feedType);
            feed.setTitle(g.getHGNC() + " Navigation Tree | WAVe");
            feed.setLink("http://bioinformatics.ua.pt/WAVe");
            feed.setDescription("WAVe | Web Analysis of the Variome navigation tree for " + g.getHGNC() + " gene.");
            feed.setAuthor("http://bioinformatics.ua.pt");
            feed.setCopyright("WAVe (c) UA.PT Bioinformatics & Computational Biology 2013");

            ArrayList<Type> datatypes = g.loadInfoForCache();

            List entries = new ArrayList();
            try {
                for (Type t : datatypes) {
                    if (t.getName().equals("LSDB")) {
                        List<SyndCategory> lsdbs = new ArrayList<SyndCategory>();
                        SyndCategory lsdb = new SyndCategoryImpl();
                        lsdb.setName("LSDB");
                        lsdbs.add(lsdb);

                        for (Node node : t.getNodes()) {
                            for (Leaf leaf : node.getLeafs()) {
                                String link = Settings.getHost() + "WAVe/gene/" + g.getHGNC() + "/lsdb:" + leaf.getId();
                                String desc = "<a href=\"" + link + "\">" + link + "</a>";

                                SyndEntry entry;
                                SyndContent description;

                                entry = new SyndEntryImpl();
                                entry.setTitle(node.getName() + ":" + node.getShortname().toUpperCase());
                                entry.setLink(link);
                                entry.setPublishedDate(new java.util.Date());
                                description = new SyndContentImpl();
                                description.setType("text/plain");
                                description.setValue(desc);
                                entry.setDescription(description);
                                entry.setAuthor("WAVe | Web Analysis of the Variome");
                                entry.setUri(link);
                                entry.setCategories(lsdbs);
                                entries.add(entry);
                            }
                        }
                    } else if (t.getName().equals("Variation") && g.getNumberOfVariants() > 0) {
                        List<SyndCategory> variants = new ArrayList<SyndCategory>();
                        SyndCategory vars = new SyndCategoryImpl();
                        vars.setName("Variation");
                        variants.add(vars);
                        SyndEntry entry;
                        SyndContent description;
                        entry = new SyndEntryImpl();
                        entry.setTitle("Variation:All");
                        entry.setLink("http://bioinformatics.ua.pt/WAVe/gene/" + g.getHGNC() + "/variantall:" + g.getHGNC());
                        entry.setPublishedDate(new java.util.Date());
                        description = new SyndContentImpl();
                        description.setType("text/plain");
                        description.setValue("<a href=\"http://bioinformatics.ua.pt/WAVe/gene/" + g.getHGNC() + "/variantall:" + g.getHGNC() + "\">http://bioinformatics.ua.pt/WAVe/gene/" + g.getHGNC() + "/variantall:" + g.getHGNC() + "</a>");
                        entry.setDescription(description);
                        entry.setAuthor("WAVe | Web Analysis of the Variome");
                        entry.setUri("http://bioinformatics.ua.pt/WAVe/gene/" + g.getHGNC() + "/variantall:" + g.getHGNC());
                        entry.setCategories(variants);
                        entries.add(entry);

                        if (g.getVariantSub() > 0) {
                            entry = new SyndEntryImpl();
                            entry.setTitle("Variation:Substitution");
                            entry.setLink("http://bioinformatics.ua.pt/WAVe/gene/" + g.getHGNC() + "/variantsub:" + g.getHGNC());
                            entry.setPublishedDate(new java.util.Date());
                            description = new SyndContentImpl();
                            description.setType("text/plain");
                            description.setValue("<a href=\"http://bioinformatics.ua.pt/WAVe/gene/" + g.getHGNC() + "/variantsub:" + g.getHGNC() + "\">http://bioinformatics.ua.pt/WAVe/gene/" + g.getHGNC() + "/variantsub:" + g.getHGNC() + "</a>");
                            entry.setDescription(description);
                            entry.setAuthor("WAVe | Web Analysis of the Variome");
                            entry.setUri("http://bioinformatics.ua.pt/WAVe/gene/" + g.getHGNC() + "/variantsub:" + g.getHGNC());
                            entry.setCategories(variants);
                            entries.add(entry);
                        }
                        if (g.getVariantDel() > 0) {
                            entry = new SyndEntryImpl();
                            entry.setTitle("Variation:Deletion");
                            entry.setLink("http://bioinformatics.ua.pt/WAVe/gene/" + g.getHGNC() + "/variantdel:" + g.getHGNC());
                            entry.setPublishedDate(new java.util.Date());
                            description = new SyndContentImpl();
                            description.setType("text/plain");
                            description.setValue("<a href=\"http://bioinformatics.ua.pt/WAVe/gene/" + g.getHGNC() + "/variantdel:" + g.getHGNC() + "\">http://bioinformatics.ua.pt/WAVe/gene/" + g.getHGNC() + "/variantdel:" + g.getHGNC() + "</a>");
                            entry.setDescription(description);
                            entry.setAuthor("WAVe | Web Analysis of the Variome");
                            entry.setUri("http://bioinformatics.ua.pt/WAVe/gene/" + g.getHGNC() + "/variantdel:" + g.getHGNC());
                            entry.setCategories(variants);
                            entries.add(entry);
                        }
                        if (g.getVariantDup() > 0) {
                            entry = new SyndEntryImpl();
                            entry.setTitle("Variation:Duplication");
                            entry.setLink("http://bioinformatics.ua.pt/WAVe/gene/" + g.getHGNC() + "/variantdup:" + g.getHGNC());
                            entry.setPublishedDate(new java.util.Date());
                            description = new SyndContentImpl();
                            description.setType("text/plain");
                            description.setValue("<a href=\"http://bioinformatics.ua.pt/WAVe/gene/" + g.getHGNC() + "/variantdup:" + g.getHGNC() + "\">http://bioinformatics.ua.pt/WAVe/gene/" + g.getHGNC() + "/variantdup:" + g.getHGNC() + "</a>");
                            entry.setDescription(description);
                            entry.setAuthor("WAVe | Web Analysis of the Variome");
                            entry.setUri("http://bioinformatics.ua.pt/WAVe/gene/" + g.getHGNC() + "/variantdup:" + g.getHGNC());
                            entry.setCategories(variants);
                            entries.add(entry);
                        }
                        if (g.getVariantIns() > 0) {
                            entry = new SyndEntryImpl();
                            entry.setTitle("Variation:Insertion");
                            entry.setLink("http://bioinformatics.ua.pt/WAVe/gene/" + g.getHGNC() + "/variantins:" + g.getHGNC());
                            entry.setPublishedDate(new java.util.Date());
                            description = new SyndContentImpl();
                            description.setType("text/plain");
                            description.setValue("<a href=\"http://bioinformatics.ua.pt/WAVe/gene/" + g.getHGNC() + "/variantins:" + g.getHGNC() + "\">http://bioinformatics.ua.pt/WAVe/gene/" + g.getHGNC() + "/variantins:" + g.getHGNC() + "</a>");
                            entry.setDescription(description);
                            entry.setAuthor("WAVe | Web Analysis of the Variome");
                            entry.setUri("http://bioinformatics.ua.pt/WAVe/gene/" + g.getHGNC() + "/variantins:" + g.getHGNC());
                            entry.setCategories(variants);
                            entries.add(entry);
                        }
                        if (g.getVariantInv() > 0) {
                            entry = new SyndEntryImpl();
                            entry.setTitle("Variation:Inversion");
                            entry.setLink("http://bioinformatics.ua.pt/WAVe/gene/" + g.getHGNC() + "/variantinv:" + g.getHGNC());
                            entry.setPublishedDate(new java.util.Date());
                            description = new SyndContentImpl();
                            description.setType("text/plain");
                            description.setValue("<a href=\"http://bioinformatics.ua.pt/WAVe/gene/" + g.getHGNC() + "/variantinv:" + g.getHGNC() + "\">http://bioinformatics.ua.pt/WAVe/gene/" + g.getHGNC() + "/variantinv:" + g.getHGNC() + "</a>");
                            entry.setDescription(description);
                            entry.setAuthor("WAVe | Web Analysis of the Variome");
                            entry.setUri("http://bioinformatics.ua.pt/WAVe/gene/" + g.getHGNC() + "/variantinv:" + g.getHGNC());
                            entry.setCategories(variants);
                            entries.add(entry);
                        }
                        if (g.getVariantCon() > 0) {
                            entry = new SyndEntryImpl();
                            entry.setTitle("Variation:Conversion");
                            entry.setLink("http://bioinformatics.ua.pt/WAVe/gene/" + g.getHGNC() + "/variantcon:" + g.getHGNC());
                            entry.setPublishedDate(new java.util.Date());
                            description = new SyndContentImpl();
                            description.setType("text/plain");
                            description.setValue("<a href=\"http://bioinformatics.ua.pt/WAVe/gene/" + g.getHGNC() + "/variantcon:" + g.getHGNC() + "\">http://bioinformatics.ua.pt/WAVe/gene/" + g.getHGNC() + "/variantcon:" + g.getHGNC() + "</a>");
                            entry.setDescription(description);
                            entry.setAuthor("WAVe | Web Analysis of the Variome");
                            entry.setUri("http://bioinformatics.ua.pt/WAVe/gene/" + g.getHGNC() + "/variantcon:" + g.getHGNC());
                            entry.setCategories(variants);
                            entries.add(entry);
                        }
                        if (g.getVariantDelins() > 0) {
                            entry = new SyndEntryImpl();
                            entry.setTitle("Variation:Deletion/Insertion");
                            entry.setLink("http://bioinformatics.ua.pt/WAVe/gene/" + g.getHGNC() + "/variantdelins:" + g.getHGNC());
                            entry.setPublishedDate(new java.util.Date());
                            description = new SyndContentImpl();
                            description.setType("text/plain");
                            description.setValue("<a href=\"http://bioinformatics.ua.pt/WAVe/gene/" + g.getHGNC() + "/variantdelins:" + g.getHGNC() + "\">http://bioinformatics.ua.pt/WAVe/gene/" + g.getHGNC() + "/variantdelins:" + g.getHGNC() + "</a>");
                            entry.setDescription(description);
                            entry.setAuthor("WAVe | Web Analysis of the Variome");
                            entry.setUri("http://bioinformatics.ua.pt/WAVe/gene/" + g.getHGNC() + "/variantdelins:" + g.getHGNC());
                            entry.setCategories(variants);
                            entries.add(entry);
                        }
                    } else {
                        List<SyndCategory> categories = new ArrayList<SyndCategory>();
                        SyndCategory cat = new SyndCategoryImpl();
                        cat.setName(t.getName());
                        categories.add(cat);
                        for (Node node : t.getNodes()) {
                            // check Static types (Static has value and has leafs)
                            if (!node.getMethod().equals("direct")) {
                                if (!node.getLeafs().isEmpty()) {

                                    for (Leaf leaf : node.getLeafs()) {
                                        String title = node.getName() + ":" + leaf.getName();
                                        String link = "";
                                        if (leaf.getValue().contains(":")) {
                                            link = Settings.getHost() + "WAVe/gene/" + g.getHGNC() + "/" + leaf.getValue();
                                            if (leaf.getValue().contains("hsa:")) {
                                                link = Settings.getHost() + "WAVe/gene/" + g.getHGNC() + "/" + node.getUa() + ":" + leaf.getValue();
                                            }
                                        } else {
                                            link = Settings.getHost() + "WAVe/gene/" + g.getHGNC() + "/" + node.getUa() + ":" + leaf.getValue();
                                        }
                                        String desc = "<a href=\"" + link + "\">" + link + "</a>";

                                        SyndEntry entry;
                                        SyndContent description;

                                        entry = new SyndEntryImpl();
                                        entry.setTitle(title);
                                        entry.setLink(link);
                                        entry.setPublishedDate(new java.util.Date());
                                        description = new SyndContentImpl();
                                        description.setType("text/plain");
                                        description.setValue(desc);
                                        entry.setDescription(description);
                                        entry.setAuthor("WAVe | Web Analysis of the Variome");
                                        entry.setUri("http://bioinformatics.ua.pt/WAVe/variant/" + g.getHGNC() + "/all");
                                        entry.setCategories(categories);
                                        entries.add(entry);
                                    }
                                }
                                // check Dynamic types (method direct or onload)
                            } else {
                                String title = node.getName() + ":" + g.getHGNC();
                                String link = Settings.getHost() + "WAVe/gene/" + g.getHGNC() + "/" + node.getUa() + ":" + g.getHGNC();
                                node.getValue().replace("#replaceme#", g.getHGNC());
                                String desc = "<a href=\"" + link + "\">" + link + "</a>";
                                SyndEntry entry;
                                SyndContent description;

                                entry = new SyndEntryImpl();
                                entry.setTitle(title);
                                entry.setLink(link);
                                entry.setPublishedDate(new java.util.Date());
                                description = new SyndContentImpl();
                                description.setType("text/plain");
                                description.setValue(desc);
                                entry.setDescription(description);
                                entry.setAuthor("WAVe | Web Analysis of the Variome");
                                entry.setUri("http://bioinformatics.ua.pt/WAVe/variant/" + g.getHGNC() + "/all");
                                entry.setCategories(categories);
                                entries.add(entry);
                            }
                        }
                    }
                }
                feed.setEntries(entries);
            } catch (Exception ex) {
                System.out.println("[API][Gene] Unable to generate " + g.getHGNC() + " Atom card\n\t" + ex.toString());
            } finally {
                SyndFeedOutput output = new SyndFeedOutput();

                return output.outputString(feed);
            }
        }
    }

    /**
     * Verifies if a given variant is valid.
     *
     * @param variant The variant to test.
     * @return True if variant is invalid and should not be added to WAVe
     * database, false it is valid.
     */
    public static boolean rejectVariant(String variant) {
        List<String> reject = Arrays.asList("water", "init", "IV", "info", "WT", "UK", "Ex", "vari", "geen", "cyto", "part", "pdf", ".fi", ".de", "net", "com", "org", "kb", "dupli", "dele", "ex", "onic", "var", "gen", "op", "Not", "unk", "dele", "Mut", "c.0", "clear", "edu", "yu", "win", "=", "set", "title", ".nl", "med", "value", "uk", "php", "win", "ih", "id", "@", "cgi", "il", "be", "ucl", "jpg", "htm", "src", "ac", "Mol", "Gen", "png", "gif", "js", "c.c", "J", "Vis", "__", "EVEN", "get");

        if (variant.equals("")) {
            return true;
        }

        if (variant == null) {
            return true;
        }

        for (String test : reject) {
            if (variant.contains(test)) {
                //System.out.println("[Reject] " + variant + " contains " + test);
                return true;
            }
        }

        if (variant.equals("?")) {
            return true;
        }
        return false;
    }

    /**
     * Loads an LSDB link based on the provided ID.
     *
     * @param id the LSDB id
     * @return a String with the LSDB URL.
     */
    static String getLsdb(String id) {
        String url = "";
        try {
            db.connect();
            ResultSet rs = db.getData("SELECT value FROM wave#build#_leaf WHERE id = " + id);
            while (rs.next()) {
                url = rs.getString("value");
            }

        } catch (Exception e) {
            System.out.println("[API] Unable to load LSDB link\n\t" + e.toString());
        } finally {
            db.close();
        }
        return url;
    }

    /**
     * Loads given Gene description from Freebase database.
     *
     * @param gene the Freebase query
     * @return
     * @deprecated
     */
    public static String getFreebase(String gene) {
        /* gene = sanitize(gene);
         String stream = "";
         try {
         URL query = new URL("http://www.freebase.com/api/service/search?query=" + gene.toLowerCase() + "&limit=1");
         BufferedReader queryReader = new BufferedReader(new InputStreamReader(query.openStream()));
         JSONObject queryObj = (JSONObject) JSONValue.parse(queryReader);
         JSONArray queryResults = (JSONArray) queryObj.get("result");
         for (Object queryResult : queryResults) {
         JSONObject r = (JSONObject) queryResult;
         URL geneUrl = new URL("http://api.freebase.com/api/experimental/topic/standard?id=" + r.get("id"));
         BufferedReader geneReader = new BufferedReader(new InputStreamReader(geneUrl.openStream()));
         JSONObject geneObj = (JSONObject) JSONValue.parse(geneReader);
         JSONObject idObj = (JSONObject) geneObj.get(r.get("id"));
         JSONObject resultObj = (JSONObject) idObj.get("result");
         stream = r.get("id") + "#" + (String) resultObj.get("description");
         }
         queryReader.close();
         } catch (Exception ex) {
         Logger.getLogger(API.class.getName()).log(Level.SEVERE, null, ex);
         }*/
        //return stream;
        return "";
    }

    /**
     * Check strings for XSS prevention.
     *
     * @param string
     * @return
     */
    public static String sanitize(String string) {
        return string.replaceAll("(?i)<script.*?>.*?</script.*?>", "") // case 1
                .replaceAll("(?i)<.*?javascript:.*?>.*?</.*?>", "") // case 2
                .replaceAll("(?i)<.*?\\s+on.*?>.*?</.*?>", "");     // case 3
    }

    public static String getLsdbs() {
        return "(1,2,3,4)";
    }
}
