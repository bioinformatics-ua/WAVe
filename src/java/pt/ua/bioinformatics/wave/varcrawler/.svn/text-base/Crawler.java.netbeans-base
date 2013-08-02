package pt.ua.bioinformatics.wave.varcrawler;

import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import pt.ua.bioinformatics.arabella.Spider;
import pt.ua.bioinformatics.wave.domain.Gene;
import pt.ua.bioinformatics.wave.domain.Leaf;
import pt.ua.bioinformatics.wave.domain.Variant;
import pt.ua.bioinformatics.wave.services.API;

/**
 * Class for reading variants using Arabella crawler.
 *
 * @author pedrolopes
 * @version 1.2, 2010-12-17
 */
public class Crawler {

    private Gene gene;
    private Spider spd;
    private ArrayList<String> variants = new ArrayList<String>();
    private Variant variant;
    private String crawlType;

    /**
     *
     * @param gene
     * @param crawlType
     */
    public Crawler(Gene gene, String crawlType) {
        try {
            this.gene = gene;
            this.crawlType = crawlType;
            spd = new Spider("config\\crawler.xml");
        } catch (ParserConfigurationException ex) {
            System.out.println("[Crawler][" + crawlType + "] Unable to parse configuration file\n\t" + ex.toString());
        } catch (SAXException ex) {
            System.out.println("[Crawler][" + crawlType + "] Unable to sax configuration file\n\t" + ex.toString());
        } catch (IOException ex) {
            System.out.println("[Crawler][" + crawlType + "] Unable to read configuration file\n\t" + ex.toString());
        } finally {
            variant = new Variant();
        }
    }

    public String getCrawlType() {
        return crawlType;
    }

    public void setCrawlType(String crawlType) {
        this.crawlType = crawlType;
    }

    public Gene getGene() {
        return gene;
    }

    public void setGene(Gene gene) {
        this.gene = gene;
    }

    public Spider getSpd() {
        return spd;
    }

    public void setSpd(Spider spd) {
        this.spd = spd;
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

    /**
     * Reads variants from the crawling process and adds them to the database.
     * <p><b>Feature:</b><br/>
     * Read the variants and add them to WAVe database to the respective gene.
     * <br/>
     * <b>Operation:</b>
     * <ul>
     *  <li>Configure Gene in Arabella crawler</li>
     *  <li>Launch Arabella</li>
     *  <li>Read variants from Arabella results</li>
     *  <li>Add read variants to database</li>
     * </ul>
     * 
     * @param address 
     * @param source
     * @return Success of the operation (True if variants are read and added, false if fails to read)
     */
    public boolean readVariants(String address, Leaf source) {
        boolean success = false;
        try {

            // compose URL to be crawled
            spd.put("gene", address);

            // launch Arabella
            //System.out.println("[Crawler][" + crawlType + "][" + gene.getHGNC() + "] Launching crawler for " + address);
            spd.go();

            // read variants
            for (String v : spd.get("vars")) {

                if (!API.rejectVariant(v)) {
                    variants.add(v);
                    //   System.out.println("[Crawler][" + crawlType + "][" + gene.getHGNC() + "] Read variant " + v);
                }
            }

            //   System.out.println("[Crawler][" + crawlType + "][" + gene.getHGNC() + "] Starting Variation database insert to " + gene.getHGNC());
            variant.setGene(gene);
            variant.setSource(source);
            variant.addVariantsToDB(variants);
            success = true;
        } catch (Exception e) {
            System.out.println("[Crawler][" + crawlType + "][" + gene.getHGNC() + "] Unable to add variants to WAVe database\n\t" + e.toString());
            success = false;
        } finally {
            // System.out.println("[Crawler][" + crawlType + "][" + gene.getHGNC() + "] Added variants to WAVe database");
            return success;
        }
    }
}
