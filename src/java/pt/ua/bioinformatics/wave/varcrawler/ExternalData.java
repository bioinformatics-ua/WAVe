package pt.ua.bioinformatics.wave.varcrawler;

import au.com.bytecode.opencsv.CSVReader;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import pt.ua.bioinformatics.wave.services.DB;
import pt.ua.bioinformatics.wave.domain.Gene;
import pt.ua.bioinformatics.wave.domain.Node;
import pt.ua.bioinformatics.wave.domain.Type;
import pt.ua.bioinformatics.wave.services.API;

/**
 *
 * @author pedrolopes
 * @version 1.2, 2010-12-17
 */
public class ExternalData {

    private ArrayList<Gene> geneList = new ArrayList<Gene>();
    private ArrayList<NodeList> nodeList = new ArrayList<NodeList>();
    private HashMap<String, Node> nodes = new HashMap<String, Node>();
    private DB sqlWAVe = API.getDb();
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    DocumentBuilder builder;
    Document doc;
    XPathFactory xp_factory;
    XPath xpath;
    Type type = API.getTypes().get("geneleaf");

    public ExternalData() {
    }

    /**
     * Updates current enabled genes list.
     * 
     * <p><b>Feature:</b><br />
     * Loads enabled genes from WAVe database and adds them to the internal gene list in memory.
     * </p>
     * @return Success of the operation (true if enabled gene list is loaded, false if fails to load gene list)
     */
    boolean updateWAVeInfo() {
        boolean success = false;

        Type t = API.getTypes().get("genesequence");

        try {
            System.out.println("[ExternalData] Loading gene list");
            sqlWAVe.connect();

            ResultSet rs = sqlWAVe.getData("SELECT DISTINCT * FROM wave#build#_gene");
            while (rs.next()) {
                geneList.add(new Gene(rs.getInt("id"), rs.getString("hgnc"), rs.getBoolean("enabled")));
            }
            rs.close();

            rs = sqlWAVe.getData("SELECT DISTINCT * FROM wave#build#_node WHERE reftypeid != " + t.getId());
            while (rs.next()) {
                nodes.put(rs.getString("shortname"), new Node(rs.getInt("id"), rs.getString("name"), rs.getString("description"), rs.getString("value"), rs.getString("shortname"), new Node(rs.getInt("parent"))));
            }

            success = true;
        } catch (Exception e) {
            System.out.println("[ExternalData] Unable to load gene list\n\t" + e.toString());
            success = false;
        } finally {
            System.out.println("[ExternalData] Gene list loaded");
            sqlWAVe.close();
            return success;
        }

    }

    /**
     * Updates existing nodes list.
     *
     * <p><b>Feature:</b><br />
     * Loads node configuration from WAVe database and adds them to the internal node list in memory.
     * </p>
     * @return Success of the operation (true if node list is loaded, false if fails to load node list)
     */
    boolean updateXmlNodeList() {
        boolean success = false;
        try {
            factory.setNamespaceAware(true);
            builder = factory.newDocumentBuilder();
            doc = (Document) builder.parse("config//setup.xml");
            xp_factory = XPathFactory.newInstance();
            xpath = xp_factory.newXPath();
            XPathExpression expr_config = xpath.compile("//config[@id='node']/values/value");
            Object result = expr_config.evaluate(doc, XPathConstants.NODESET);

            NodeList nodelist = (NodeList) result;
            for (int i = 0; i < nodelist.getLength(); i++) {
                nodeList.add(nodelist.item(i).getChildNodes());
            }
            success = true;
        } catch (Exception e) {
            System.out.println("[ExternalData] Unable to load node list\n\t" + e.toString());
            success = false;
        } finally {
            System.out.println("[ExternalData] Node list loaded");
            sqlWAVe.close();
        }
        return success;
    }

    /**
     * Reads loaded gene list for processing.
     * <p><b>Feature:</b><br />
     * Reads loaded gene list (from memory) and processes it. Loads for each Gene in the list,
     * information regarding each Node in the node list, from ExternalData database.
     * </p>
     *
     * @return Success of the operation (true if all info is read to all gene, false if fails to load information)
     */
    public boolean loadExternalData() {
        boolean success = false;

        try {
            //readLRG();
            System.out.println("[ExternalData] Loaded LRGs, Processing gene list");
            if (updateWAVeInfo() && updateXmlNodeList()) {
                for (Gene gene : geneList) {
                    System.out.println("[ExternalData] Processing gene " + gene.getHGNC());
                    for (NodeList node : nodeList) {
                        String name = node.item(1).getTextContent();
                        String description = node.item(3).getTextContent();
                        String shortname = node.item(5).getTextContent();
                        String value = node.item(9).getTextContent();
                        String parent = node.item(11).getTextContent();
                        Type theType = new Type(node.item(13).getTextContent());
                        NodeList source = node.item(7).getChildNodes();

                        //    System.out.println("[ExternalData] Processing node " + name);
                        if (processNode(source, gene, nodes.get(shortname))) {
                            success = true;
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("[ExternalData] \n\t" + e.toString());
            Logger.getLogger(API.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            System.out.println("[ExternalData] Gene information added from GeNS");
            return success;
        }
    }

    boolean processNode(NodeList source, Gene gene, Node node) {
        boolean success = false;
        String method = source.item(1).getTextContent();

        if (method.equals("direct")) {
            success = true;
        } else if (method.equals("onload")) {
            success = true;
        } else if (method.equals("cache")) {
            String typeName = source.item(3).getTextContent();

            if (typeName.equals("csv")) {
                String connection = source.item(5).getTextContent().replace("#replaceme#", gene.getHGNC());
                if (!connection.contains("lrg")) {
                    String query = source.item(7).getTextContent();
                    String result = source.item(9).getTextContent();
                    if (readCSV(gene, connection, query, result, node)) {
                        success = true;
                    }
                }
            } else if (typeName.equals("xml")) {
                String connection = source.item(5).getTextContent();
                String query = source.item(7).getTextContent();
                if (readXML(gene, connection, query, node)) {
                    success = true;
                }
            } else if (typeName.equals("feed")) {
                String connection = source.item(5).getTextContent();
                if (readFeed(gene, connection, node)) {
                    success = true;
                }
            } else if (typeName.equals("sql")) {
                String connection = source.item(5).getTextContent();
                String query = source.item(7).getTextContent().replace("#replaceme#", gene.getHGNC());
                String result = source.item(9).getTextContent();

                if (readDB(gene, connection, query, result, node)) {
                    success = true;
                }
            }
        }
        return success;
    }

    /**
     * Reads data from external database (GeNS) for a specific gene and for a specific data type.
     *
     * @param gene the Gene to where the loaded data will be added
     * @param connection the external database connection string
     * @param query the external database data SELECT query
     * @param result the name of the field being loaded from the SELECT results
     * @param n the node to which the loaded data relates to
     * @return
     */
    boolean readDB(Gene gene, String connection, String query, String result, Node n) {
        boolean success = false;

        DB db = new DB("External database", connection);
        DB wave = API.getDb();

        try {
            db.connectX();
            ResultSet rs = db.getData(query);

            wave.connect();
            while (rs.next()) {
                //System.out.println("inserting " + rs.getString(result));
                wave.insert("Info for " + n.getShortname() + " to " + gene.getHGNC(), "INSERT INTO wave#build#_leaf (name, value, timestamp, refnodeid) VALUES('" + rs.getString(result) + "', '" + rs.getString(result) + "', NOW(), " + n.getId() + ");");
                wave.insert("Association for " + n.getShortname() + " to " + gene.getHGNC(), "INSERT INTO wave#build#_association (a,b,reftypeid) VALUES(" + gene.getId() + ",  (SELECT @@identity), " + type.getId() + ");");

            }
            wave.close();
        } catch (Exception e) {
            System.out.println("[ExternalData][DB] unable to add leafs from GeNS to gene " + gene.getHGNC() + "\n\t" + e.toString());
            Logger.getLogger(API.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            db.close();
            return success;
        }
    }

    /**
     * Reads data from external XML file. Not implemented.
     * 
     * @param gene the Gene to where the loaded data will be added
     * @param connection the external XML file address connection string
     * @param query the external database data XPath query
     * @param n the node to which the loaded data relates to
     * @return
     */
    boolean readXML(Gene gene, String connection, String query, Node n) {
        boolean success = true;

        // to implement on request

        return success;
    }

    /**
     * Reads data from external CSV file, for a specific gene and for a specific data type.
     * 
     * @param gene the Gene to where the loaded data will be added
     * @param connection the external CSV file
     * @param query the external CSV file field separator
     * @param result the number of the field being loaded from the CSV file
     * @param n the node to which the loaded data relates to
     * @return
     */
    boolean readCSV(Gene gene, String connection, String query, String result, Node n) {
        boolean success = false;
        CSVReader reader;
        DB wave = API.getDb();
        try {
            reader = new CSVReader(new BufferedReader(new InputStreamReader(new URL(connection).openStream())), query.charAt(0), '\'', 1);
            String[] nextLine;
            wave.connect();
            while ((nextLine = reader.readNext()) != null) {
                String info = nextLine[Integer.parseInt(result)];
                wave.insert("Info for " + n.getShortname() + " to " + gene.getHGNC(), "INSERT INTO wave#build#_leaf (name, value, timestamp, refnodeid) VALUES('" + info + "', '" + info + "', NOW(), " + n.getId() + ");");
                wave.insert("Association for " + n.getShortname() + " to " + gene.getHGNC(), "INSERT INTO wave#build#_association (a,b,reftypeid) VALUES(" + gene.getId() + ",  (SELECT @@identity), " + type.getId() + ");");
            }
            wave.close();
            success = true;
        } catch (Exception e) {
            System.out.println("[ExternalData][CSV] unable to load data from CSV to gene " + gene.getHGNC() + "\n\t" + e.toString());
            Logger.getLogger(LOVDFeedReader.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            return success;
        }
    }

    /**
     * Reads data from an Atom feed, for a given gene, for a specific data type.
     *
     * @param gene the Gene to where the loaded data will be added
     * @param connection the external CSV file
     * @param n the node to which the loaded data relates to
     * @return
     */
    public boolean readFeed(Gene gene, String connection, Node n) {
        boolean success = false;
        DB wave = API.getDb();

        try {
            if (connection.contains("hgvbaseg2p")) {
                connection = connection.replace("#replaceme#", gene.getHGNC());

                URL feedUrl = new URL(connection);
                SyndFeedInput input = new SyndFeedInput();
                SyndFeed feed = input.build(new XmlReader(feedUrl));
                List<SyndEntry> entries = feed.getEntries();

                wave.connect();
                for (SyndEntry e : entries) {
                    String[] link = e.getLink().split("/");
                    String info = link[link.length - 1];

                    wave.insert("Info for " + n.getShortname() + " to " + gene.getHGNC(), "INSERT INTO wave#build#_leaf (name, value, timestamp, refnodeid) VALUES('" + info + "', '" + info + "', NOW(), " + n.getId() + ");");
                    wave.insert("Association for " + n.getShortname() + " to " + gene.getHGNC(), "INSERT INTO wave#build#_association (a,b,reftypeid) VALUES(" + gene.getId() + ",  (SELECT @@identity), " + type.getId() + ");");
                }
                wave.close();
            }
            success = true;
        } catch (Exception e) {
            System.out.println("[ExternalData][Feed] unable to load data from Feed to gene " + gene.getHGNC() + "\n\t" + e.toString());
            Logger.getLogger(API.class.getName()).log(Level.SEVERE, null, e);
        }
        return success;
    }

    /**
     * Loads LRG (Locus Reference Genomic) reference sequence information to the database.
     * 
     * @return success of the operation
     */
    boolean readLRG() {
        boolean success = false;
        // API api = new API();
        DB wave = new DB(); // API.getDb();
        CSVReader reader;
        API.load();
        Type t = API.getTypes().get("genesequence");
        Node n = API.getNodes().get("lrg");

        try {
            reader = new CSVReader(new FileReader("config\\LRGList.csv"), '\t', '\'', 1);
            String[] nextLine;
            wave.connect();
            while ((nextLine = reader.readNext()) != null) {
                String id = nextLine[0];
                String hgnc = nextLine[1];
                String path = nextLine[8];
                String info = nextLine[2];
                Gene gene = new Gene(hgnc);

                System.out.println(gene.getId() + "\n\t" + id + "-" + path + "-" + info);

                wave.insert("LRG for " + gene.getHGNC(), "INSERT INTO wave#build#_leaf (name, value, info, timestamp, refnodeid) VALUES('" + id + "', '" + path + "', '" + info + "' ,NOW(), " + n.getId() + ");");
                wave.insert("Association for " + n.getShortname() + " to " + gene.getHGNC(), "INSERT INTO wave#build#_association (a,b,reftypeid) VALUES(" + gene.getId() + ",  (SELECT @@identity), " + t.getId() + ");");
            }
            wave.close();

            success = true;
        } catch (Exception e) {
            System.out.println("[ExternalData][LRG] unable to load info\n\t" + e.toString());
            Logger.getLogger(API.class.getName()).log(Level.SEVERE, null, e);
        }
        return success;
    }
}
