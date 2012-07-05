package pt.ua.bioinformatics.wave.varcrawler;

import au.com.bytecode.opencsv.CSVReader;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import pt.ua.bioinformatics.wave.services.DB;
import pt.ua.bioinformatics.wave.domain.Gene;
import pt.ua.bioinformatics.wave.domain.Leaf;
import pt.ua.bioinformatics.wave.domain.Variant;
import pt.ua.bioinformatics.wave.services.API;

/**
 *
 * @author pedrolopes
 * @version 1.2, 2010-12-17
 */
public class LOVDFeedReader {

    private DB db = API.getDb();
    private ArrayList<String> variants = new ArrayList<String>();
    private Variant variant = new Variant();
    private Gene gene;
    private URL url;
    private String feedType;
    private CSVReader csvReader;

    // constructors
    public LOVDFeedReader() {
    }

    /**
     * Creates a new Feed from the provided URL to memory.
     * <p><b>Features:</b><br />
     * Executes the required operations to instance a new Feed from the provided URL.
     * </p>
     *
     * @param gene The Gene to which the feed will provide variants
     * @param feedUrl The Feed's URL
     * @param feedType The type of the feed (LOVD, UMD)
     */
    public LOVDFeedReader(Gene gene, String feedUrl, String feedType) {
        try {
            this.gene = gene;
            this.url = new URL(feedUrl);
            this.feedType = feedType;
        } catch (Exception ex) {
            System.out.println("[FeedReader][" + feedType + "] Unable to read Feed at " + feedUrl + "\n\t" + ex.toString());
            //Logger.getLogger(LOVDFeedReader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // getters and setters
    public CSVReader getCsvReader() {
        return csvReader;
    }

    public void setCsvReader(CSVReader csvReader) {
        this.csvReader = csvReader;
    }

    public DB getDb() {
        return db;
    }

    public void setDb(DB db) {
        this.db = db;
    }

    public String getFeedType() {
        return feedType;
    }

    public void setFeedType(String feedType) {
        this.feedType = feedType;
    }

    public Gene getGene() {
        return gene;
    }

    public void setGene(Gene gene) {
        this.gene = gene;
    }

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    public Variant getVariant() {
        return variant;
    }

    public void setVariant(Variant variant) {
        this.variant = variant;
    }

    public ArrayList<String> getVariants() {
        return variants;
    }

    public void setVariants(ArrayList<String> variants) {
        this.variants = variants;
    }

    // operations
    /**
     * Reads variant data from a LOVD feed and adds them to database.
     * <p><b>Feature:</b><br />
     * Read available variants from instanced feed and add them to the database, associated with a Gene and related Leafs.
     * <br /><br />
     * <b>Operation:</b>
     * <ul>
     *  <li>Validate feed gene</li>
     *  <li>Test feed type and proceed accordingly</li>
     *  <li>LOVD
     *      <ul>
     *          <li>Reads variants for the gene</li>
     *          <li>Adds gene variants to the gene in WAVe database</li>
     *      </ul>
     *  </li>
     * </ul>
     * </p>
     *
     * @param source 
     * @return Success of the operation (True if variants are read and added to WAVe database, false if fails).
     * @throws SQLException 
     */
    public boolean readVariants(Leaf source) throws SQLException {
        String[] tmp;
        String v;
        boolean success = false;
        System.out.println("[FeedReader][" + feedType + "][" + gene.getHGNC() + "] Launching feed reader for " + url.toString());

        if (gene.getId() == 0) {
            return false;
        }

        if (feedType.equalsIgnoreCase("LOVD")) {
            try {
                Document lsdb = parse(url);
                List entries = lsdb.selectNodes("//*[local-name()='entry']");
                Iterator entrez = entries.iterator();
                while (entrez.hasNext()) {
                    Element entry = (Element) entrez.next();
                    // get variant title
                    Node n = entry.selectSingleNode("*[local-name()='title']");
                    String v_title = getVariantTitle(n);

                    // get variant inner content
                    Node c = entry.selectSingleNode("*[local-name()='content']");
                    int v_times = getVariantTimes(c);
                    String v_refseq = getVariantRefSeq(c);

                  // add the variants
                    for (int i = 0; i < v_times; i++) {
                        if (!v_refseq.equals("")) {
                            variants.add(v_title);
                        } else {
                            variants.add(v_refseq + ":" + v_title);
                        }
                    }

                }
                variant.setGene(gene);
                variant.setSource(source);
                variant.addVariantsToDB(variants);
                success = true;
            } catch (Exception e) {
                System.out.println("[FeedReader][" + feedType + "] Unable to add variants to " + gene.getHGNC() + "\n\t" + e.toString());
                success = false;
                Logger.getLogger(LOVDFeedReader.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        return success;
    }

    public String getVariantTitle(Node n) {
        String v_title = "";
        String[] tmpTitle = n.getStringValue().split(":");
        v_title = tmpTitle[1];
        if (!v_title.startsWith("c.")) {
            try {
                v_title = tmpTitle[2];
            } catch (Exception ex) {
                // Logger.getLogger(LOVDFeedReader.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (!v_title.startsWith("c.")) {
            try {
                v_title = tmpTitle[0];
            } catch (Exception ex) {
                //Logger.getLogger(LOVDFeedReader.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return v_title;
    }

    public int getVariantTimes(Node c) {
        int v_times = 1;
        String[] fullContent = c.getStringValue().split("\n");

        try {
            String[] fullTimes = fullContent[7].split(":");
            v_times = Integer.parseInt(fullTimes[1]);
        } catch (Exception ex) {
            //Logger.getLogger(LOVDFeedReader.class.getName()).log(Level.SEVERE, null, ex);
        }
        return v_times;
    }

    public String getVariantRefSeq(Node c) {
        String v_refseq = "";
        String[] fullContent = c.getStringValue().split("\n");
        try {

            String[] fullRefseq = fullContent[3].split(":");
            v_refseq = fullRefseq[1];
        } catch (Exception ex) {
            //   Logger.getLogger(LOVDFeedReader.class.getName()).log(Level.SEVERE, null, ex);
        }
        return v_refseq;
    }

    public Document parse(URL url) throws DocumentException {
        SAXReader reader = new SAXReader();
        Document document = reader.read(url);
        return document;
    }
}
