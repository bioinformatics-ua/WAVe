package pt.ua.bioinformatics.wave.services;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import pt.ua.bioinformatics.wave.domain.Gene;

/**
 *
 * @author pedrolopes
 * @version 1.2, 2010-12-17
 */
public class GeneList {

    public final static GeneList INSTANCE = new GeneList();
    private HashMap<String, Gene> genelist = new HashMap<String, Gene>();

    /**
     * Instances GeneList by loading gene information to in-memory HashMap.
     */
    private GeneList() {
        if (!Settings.isLoaded()) {
            Settings.load();
        }

        if (genelist.isEmpty()) {
            DB db = API.getDb();
            Gene gene = null;

            try {
                db.connect();
                ResultSet rs;

                rs = db.getData("SELECT G.id, G.name, G.locus, G.hgnc, G.enabled, GL.lsdb, GL.variant "
                        + "FROM wave#build#_gene AS G "
                        + "INNER JOIN wave#build#_genelist AS GL ON GL.id = G.id "
                        + "ORDER BY G.hgnc ASC");

                while (rs.next()) {
                    gene = new Gene(rs.getInt("id"), rs.getString("hgnc"), rs.getBoolean("enabled"));
                    gene.setName(rs.getString("name"));
                    gene.setLocus(rs.getString("locus"));
                    gene.setNumberOfLsdbs(rs.getInt("lsdb"));
                    gene.setNumberOfVariants(rs.getInt("variant"));

                    genelist.put(rs.getString("hgnc"), gene);
                }

                System.out.println("\n\t||||| WAVe Loaded");
            } catch (Exception e) {
                System.out.println("[GeneList] Unable to load gene list\n\t" + e.toString());
            } finally {
                db.close();
            }
        }
    }

    /**
     * Exports the WAVe enabled gene list loaded in the Singleton.
     * @return a HashMap containing the gene list.
     */
    public HashMap<String, Gene> getGeneList() {
        return genelist;
    }

    public void setGeneList(HashMap<String, Gene> genes) {
        this.genelist = genes;
    }

    public Gene getGene(String hgnc) {
        return genelist.get(hgnc);
    }

    /**
     * Searches for a gene in the singleton loaded gene list.
     * @param search the gene search query
     * @param checkEnabled returns all genes if false or only enabled genes if true
     * @return ArrayList containing all the genes matching
     */
    public ArrayList<Gene> searchGene(String search, boolean checkEnabled) {
        search = search.toUpperCase();
        ArrayList<Gene> genes = new ArrayList<Gene>();
        Gene gene = new Gene();
        if (search.contains("*")) {
            genes = new ArrayList(genelist.values());
            //Collections.sort(genes, new GeneCompare());
        } else {
            try {
                gene = genelist.get(search);
                if (gene.getId() != 0) {
                    if (checkEnabled) {
                        if (gene.isEnabled()) {
                            genes.add(gene);
                        }
                    } else {
                        genes.add(gene);
                    }
                }
            } catch (Exception ex) {
                for (String s : genelist.keySet()) {
                    if (s.startsWith(search)) {
                        gene = genelist.get(s);
                        if (checkEnabled) {
                            if (gene.isEnabled()) {
                                genes.add(gene);
                            }
                        } else {
                            genes.add(gene);
                        }
                    }
                }
            }
        }
        Collections.sort(genes, new GeneCompare());
        return genes;
    }

    /**
     * Exports all genes loaded in the singleton list.
     * @return ArrayList containing all genes.
     */
    public ArrayList<Gene> getAll() {
        ArrayList<Gene> genes = (ArrayList<Gene>) genelist.values();
        //Collections.sort(genes, new GeneCompare());
        return (ArrayList<Gene>) genes;
    }

    /**
     * Exports all enabled genes loaded in the singleton list
     * @return
     */
    public ArrayList<Gene> getEnabled() {
        ArrayList<Gene> genes = new ArrayList<Gene>();
        for (Gene g : genelist.values()) {
            if (g.isEnabled()) {
                genes.add(g);
            }
        }
        Collections.sort(genes, new GeneCompare());
        return genes;
    }
}
