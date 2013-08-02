package pt.ua.bioinformatics.wave.varcrawler;

import au.com.bytecode.opencsv.CSVReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Node;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import pt.ua.bioinformatics.wave.domain.MetaType;
import pt.ua.bioinformatics.wave.domain.Type;
import pt.ua.bioinformatics.wave.services.API;
import pt.ua.bioinformatics.wave.services.DB;
import pt.ua.bioinformatics.wave.services.Settings;

/**
 *
 * @author pedrolopes
 * @version 1.2, 2010-12-17
 */
public class Creator {

    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    DocumentBuilder builder;
    Document doc;
    XPathFactory xp_factory;
    XPath xpath;
    ArrayList<String> metatypes = new ArrayList<String>();

    /**
     * Loads the configuration file.
     * <p><b>Feature:</b><br />
     * Loads the configuration file config/config.xml and instances all the required variables
     * for further defaults initializations.
     * </p>
     *
     * @throws SAXException
     * @throws IOException
     * @throws XPathExpressionException
     */
    void loadConfig() throws SAXException, IOException, XPathExpressionException {
        try {
            factory.setNamespaceAware(true);
            builder = factory.newDocumentBuilder();

            doc = (Document) builder.parse(Settings.getPath() + "setup.xml");

        } catch (ParserConfigurationException ex) {
            System.out.println("[Creator] Unale to load document\n\t" + ex.toString());
            Logger.getLogger(Creator.class.getName()).log(Level.SEVERE, null, ex);

        } finally {
            System.out.println("[Creator] Document loaded");
        }
    }

    /**
     * Loads genes from the Gene List provided in the configuration file.
     * <p><b>Feature:</b><br />
     * Instances a new AllGenes loader and adds data about all known genes to the database.
     * </p>
     *
     * @returns boolean True if inserts all genes successfully, false if fails to insert.
     */
    boolean loadGenes() throws IOException {
        boolean success = false;
        try {
            xp_factory = XPathFactory.newInstance();
            xpath = xp_factory.newXPath();
            XPathExpression expr = xpath.compile("//config[@id='gene']/src");

            Object result = expr.evaluate(doc, XPathConstants.NODESET);
            System.out.println("[Creator] Loading genes");
            NodeList nodes = (NodeList) result;

            for (int i = 0; i < nodes.getLength(); i++) {
                //System.out.println("[Creator] Gene List at " + nodes.item(i).getNodeName() + ":" + nodes.item(i).getTextContent());
                success = addGenes(nodes.item(i).getTextContent());
            }
            System.out.println("[Creator] Genes loaded");
        } catch (XPathExpressionException ex) {
            System.out.println("[Creator] Unable to load genes\n\t" + ex.toString());
            Logger.getLogger(Creator.class.getName()).log(Level.SEVERE, null, ex);
        } finally {

            return success;
        }
    }

    /**
     * Loads default meta types to WAVe DB.
     * <p><b>Feature:</b><br />
     * Reads the configuration files and searches for default meta type information,
     * which is then added to WAVe DB.<br />Data is obtained through XPath and XML reading.
     * <br /><b>Operation</b>
     * <ul>
     *  <li>Load metatype data configuration</li>
     *  <li>Read value information for each meta type (name + description)</li>
     *  <li>Instance each data type and add it to the database</li>
     * </ul>
     * </p>
     * 
     * @return Success of the operation (true if loads meta types, false if fails to insert.
     */
    boolean loadMetaTypes() {
        try {
            xp_factory = XPathFactory.newInstance();
            xpath = xp_factory.newXPath();

            XPathExpression expr_values = xpath.compile("//config[@id='metatype']/values/value");
            Object values = expr_values.evaluate(doc, XPathConstants.NODESET);

            NodeList valueList = (NodeList) values;
            for (int i = 0; i < valueList.getLength(); i++) {
                Node value = valueList.item(i);

                MetaType mt = new MetaType(value.getChildNodes().item(1).getTextContent(), value.getChildNodes().item(3).getTextContent(), value.getChildNodes().item(5).getTextContent());
                //System.out.println(mt.getName() + ":" + mt.getDescription());
                mt.addToDB();
                metatypes.add(mt.getShortname());
            }

        } catch (Exception ex) {
            System.out.println("[Creator][MetaType] Unable to add meta types to DB\n\t" + ex.toString());
            Logger.getLogger(Creator.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } finally {
            System.out.println("[Creator][MetaType] Added all meta types to DB");
            return true;
        }
    }

    /**
     * Loads default types to database according to their concept.
     * <p><b>Feature:</b><br />
     * Reads the concept information from the configuration file and insert each data type to WAVe db.<br/>
     * <b>Operation:</b>
     * <ul>
     *  <li>Read general concept configuration</li>
     *  <li>Instance meta type (from the read values) to which the concept belongs</li>
     *  <li>Read values for type</li>
     *  <li>Instance each type and add it to WAVe DB</li>
     * </ul>
     * </p>
     *
     * @param concept The concept we are adding to the system
     * @return Success of the operation (True if default types loaded successfully, false if fails to load default types)
     */
    boolean loadDefaultTypes() {
        boolean success = false;

        for (String concept : metatypes) {
            try {
                xp_factory = XPathFactory.newInstance();
                xpath = xp_factory.newXPath();
                XPathExpression expr_config = xpath.compile("//config[@id='" + concept + "']");
                Object result = expr_config.evaluate(doc, XPathConstants.NODESET);

                NodeList configs = (NodeList) result;
                System.out.println("[Creator][Type][" + concept + "] Adding types to DB...");

                Node config = configs.item(0);

                // String datatype = config.getChildNodes().item(1).getTextContent();
                //String type = config.getChildNodes().item(3).getTextContent();

                System.out.println(concept);

                MetaType mt = new MetaType(concept);

                XPathExpression expr_values = xpath.compile("//config[@id='" + concept + "']/values/value");
                Object values = expr_values.evaluate(doc, XPathConstants.NODESET);

                NodeList valueList = (NodeList) values;
                for (int i = 0; i < valueList.getLength(); i++) {
                    Node value = valueList.item(i);

                    Type t = new Type(value.getChildNodes().item(1).getTextContent(), value.getChildNodes().item(3).getTextContent(), value.getChildNodes().item(5).getTextContent(), mt.getId());
                    // System.out.println(t.getName() + ":" + t.getDescription() + ":" + t.getMetaType());
                    t.addToDB();
                }
                success = true;
            } catch (Exception ex) {
                System.out.println("[Creator][Type][" + concept + "] Unable to add datatypes to WAVe database\n\t" + ex.toString());
                Logger.getLogger(Creator.class.getName()).log(Level.SEVERE, null, ex);
                success = false;
            } finally {
                System.out.println("[Creator][Type][" + concept + "] Added all datatypes to WAVe database");
                // return true;
            }
        }
        return success;
    }

    /**
     * Loads default nodes for the WAVe tree.
     * <p><b>Feature:</b><br />
     * Reads the tree organization (nodes, subnodes, leafs) from the configuration file and stores the tree in WAVe database.<br/>
     * <b>Operation:</b>
     * <ul>
     *  <li>Read tree configuration</li>
     *  <li>Instance nodes</li>
     *  <li>Read node values</li>
     *  <li>Add each node to DB</li>
     * </ul>
     * </p>
     * @return Success status (true if nodes are added to WAVe db, false if fails to add nodes)
     */
    boolean loadNodes() {
        try {
            xp_factory = XPathFactory.newInstance();
            xpath = xp_factory.newXPath();
            XPathExpression expr_config = xpath.compile("//config[@id='node']/values/value");
            Object result = expr_config.evaluate(doc, XPathConstants.NODESET);

            NodeList nodes = (NodeList) result;


            for (int i = 0; i < nodes.getLength(); i++) {
                pt.ua.bioinformatics.wave.domain.Node nodeParent = new pt.ua.bioinformatics.wave.domain.Node();

                String name = nodes.item(i).getChildNodes().item(1).getTextContent();
                String description = nodes.item(i).getChildNodes().item(3).getTextContent();
                String shortname = nodes.item(i).getChildNodes().item(5).getTextContent();
                String value = nodes.item(i).getChildNodes().item(9).getTextContent();
                String parent = nodes.item(i).getChildNodes().item(11).getTextContent();
                String t = nodes.item(i).getChildNodes().item(13).getTextContent();
                String ua = nodes.item(i).getChildNodes().item(15).getTextContent();
                NodeList source = nodes.item(i).getChildNodes().item(7).getChildNodes();
                String method = source.item(1).getTextContent();

                Type type = new Type(t);
                if (!parent.equals("")) {
                    nodeParent = new pt.ua.bioinformatics.wave.domain.Node(parent);
                }
                pt.ua.bioinformatics.wave.domain.Node n = new pt.ua.bioinformatics.wave.domain.Node(name, description, shortname, method, value, nodeParent, type, ua);

                //System.out.println(name + ":" + description + ":" + shortname + ":" + value + ":" + method + ":" + t);
                if (n.addToDB()) {
                    // System.out.println(n.getName() + "\n\t" + n.getDescription() + "\n\t" + n.getValue() + "\n\t" + n.getParent().getName() + "\n\t" + n.getiStatic() + "\n\t" + n.getType().getName());
                }
            }

        } catch (Exception e) {
            System.out.println("[Creator][Nodes] Unable to load nodes\n\t" + e.toString());
            Logger.getLogger(Creator.class.getName()).log(Level.SEVERE, null, e);
            return false;
        } finally {
            System.out.println("[Creator][Node] Added all nodes to WAVe database");
            return true;
        }
    }

    /**
     * Adds genes to WAVe database.
     * <p><b>Feature:</b><br/>
     * Loads gene data from CSV list and adds genes to WAVe database.
     * </p>
     * @return
     * @throws IOException
     */
    public boolean addGenes(String geneList) throws IOException {
        String query;
        String query_genelist;
        String[] nextLine;
        String hgnc;
        String name;
        String locus;
        String id;
        DB db = API.getDb();
        CSVReader reader;

        try {
            db.connect();
            reader = new CSVReader(new FileReader(Settings.getPath() + geneList), '\t');
        } catch (Exception e) {
            db.close();
            System.out.println("[AllGenes][AddToDB] Unable to add genes to WAVe\n\t" + e.toString());
            Logger.getLogger(Creator.class.getName()).log(Level.SEVERE, null, e);
            return false;
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
                query = "INSERT INTO wave#build#_gene (id, hgnc, name, locus, enabled, timestamp) VALUES(" + id + ", '" + hgnc + "','" + name.replace("'", " ").replace("-", " ").replace(",", " ") + "', '" + locus + "',0, NOW())";
                query_genelist = "INSERT INTO wave#build#_genelist (id, symbol, name) VALUES(" + id + ",'" + hgnc + "','" + name.replace("'", " ") + "')";

                db.connect();
                db.insert(hgnc, query);
                db.insert(hgnc, query_genelist);
                db.close();
            } catch (Exception e) {
                System.out.println("[AllGenes][AddToDB] Unable to add " + hgnc + " to WAVe\n\t" + e.toString());
                Logger.getLogger(Creator.class.getName()).log(Level.SEVERE, null, e);
                return false;
            } finally {
                // show in console
                //System.out.println("[AllGenes][AddToDB] Gene " + gene + " added to database");
            }
        }
        db.close();
        System.out.println("[AllGenes][AddToDB] All genes added to WAVe\n");
        return true;
    }

    /**
     * Executes required operation to load default configuration data in WAVe database.
     * <p><b>Feature:</b><br />
     * Process the configuration reading all the information required to startup the database
     * and initiate the main system configurations.
     * </p>
     * @return
     */
    public boolean create() {
        boolean success = false;

        try {

            // load configuration file and definitions
            loadConfig();

            // load known gene list from file
            loadGenes();

            // load WAVe metatypes
            loadMetaTypes();

            // load default Data Types
            loadDefaultTypes();

            // load default WAVe tree configuration
            loadNodes();

            success = true;
        } catch (Exception e) {
            System.out.println("[Creator] Unable to load configuration\n\t" + e.toString());
            Logger.getLogger(Creator.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            System.out.println("[Creator] WAVe created");
            return success;
        }
    }
}
