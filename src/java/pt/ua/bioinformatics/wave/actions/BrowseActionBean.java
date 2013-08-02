package pt.ua.bioinformatics.wave.actions;

import java.util.ArrayList;
import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.StreamingResolution;
import net.sourceforge.stripes.action.UrlBinding;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import pt.ua.bioinformatics.wave.domain.Gene;
import pt.ua.bioinformatics.wave.services.API;
import pt.ua.bioinformatics.wave.services.GeneList;
import pt.ua.bioinformatics.wave.services.Settings;

/**
 *
 * @author pedrolopes
 */
@UrlBinding("/browse/{query}/{$event}")
public class BrowseActionBean implements ActionBean {

    private ActionBeanContext context;
    private ArrayList<Gene> genes = new ArrayList<Gene>();
    private GeneList genelist = GeneList.INSTANCE;
    private int numberOfGenes = 0;
    private String query = "";

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public void setNumberOfGenes(int numberOfGenes) {
        this.numberOfGenes = numberOfGenes;
    }

    public GeneList getGenelist() {
        return genelist;
    }

    public void setGenelist(GeneList genelist) {
        this.genelist = genelist;
    }

    public ArrayList<Gene> getGenes() {
        return genes;
    }

    public void setGenes(ArrayList<Gene> genes) {
        this.genes = genes;
    }

    public void setContext(ActionBeanContext context) {
        this.context = context;
    }

    public ActionBeanContext getContext() {
        return context;
    }

    /**
     * Main resolution handler, returns genes in JSON format for DataTables
     * plugin.
     *
     * @return
     */
    @DefaultHandler
    public Resolution table() {
        if (!Settings.isLoaded()) {
            Settings.load();
        }
        if (query.equals("*")) {
            numberOfGenes = 30092;
            return new ForwardResolution("/resources/javascript/browsegenes.js");
        } else {
            JSONObject stream = new JSONObject();
            JSONArray geneList = new JSONArray();
            genes = genelist.searchGene(query, false);
            numberOfGenes = genes.size();
            for (Gene g : genes) {
                JSONArray info = new JSONArray();
                info.add("<a href=\"" + Settings.getHost() + "WAVe/gene/" + g.getHGNC() + "\" title=\"" + g.getHGNC() + " | WAVe\" class=\"genelink\">" + g.getId() + "</a>");
                info.add("<a href=\"" + Settings.getHost() + "WAVe/gene/" + g.getHGNC() + "\" title=\"" + g.getHGNC() + " | WAVe\" class=\"genelink\">" + g.getHGNC() + "</a>");
                info.add(g.getNumberOfLsdbs());
                info.add(g.getNumberOfVariants());
                geneList.add(info);
            }
            stream.put("aaData", geneList);
            return new StreamingResolution("text/javascript", stream.toString());
        }
    }

    /**
     * Returns enabled genes in JSON format for DataTables plugin.
     *
     * @return
     */
    public Resolution enabled() {
        if (!Settings.isLoaded()) {
            Settings.load();
        }
        JSONObject stream = new JSONObject();
        JSONArray geneList = new JSONArray();
        if (query.equals("*")) {
            genes = genelist.getEnabled();
            for (Gene g : genes) {
                JSONArray info = new JSONArray();
                info.add("<a href=\"" + Settings.getHost() + "WAVe/gene/" + g.getHGNC() + "\" title=\"" + g.getHGNC() + " | WAVe\" class=\"genelink\">" + g.getId() + "</a>");
                info.add("<a href=\"" + Settings.getHost() + "WAVe/gene/" + g.getHGNC() + "\" title=\"" + g.getHGNC() + " | WAVe\" class=\"genelink\">" + g.getHGNC() + "</a>");
                info.add(g.getNumberOfLsdbs());
                info.add(g.getNumberOfVariants());
                geneList.add(info);
            }
            stream.put("aaData", geneList);
        } else {
            genes = genelist.searchGene(query, true);
            numberOfGenes = genes.size();
            for (Gene g : genes) {
                JSONArray info = new JSONArray();
                info.add("<a href=\"" + Settings.getHost() + "WAVe/gene/" + g.getHGNC() + "\" title=\"" + g.getHGNC() + " | WAVe\" class=\"genelink\">" + g.getId() + "</a>");
                info.add("<a href=\"" + Settings.getHost() + "WAVe/gene/" + g.getHGNC() + "\" title=\"" + g.getHGNC() + " | WAVe\" class=\"genelink\">" + g.getHGNC() + "</a>");
                info.add(g.getNumberOfLsdbs());
                info.add(g.getNumberOfVariants());
                geneList.add(info);
            }
            stream.put("aaData", geneList);
        }
        return new StreamingResolution("text/javascript", stream.toString());
    }

    /**
     * Returns all genes in JSON format for DataTables plugin.
     *
     * @return
     */
    public Resolution all() {
        if (!Settings.isLoaded()) {
            Settings.load();
        }
        JSONObject stream = new JSONObject();
        JSONArray geneList = new JSONArray();
        genes = genelist.searchGene(query, false);
        numberOfGenes = genes.size();
        for (Gene g : genes) {
            JSONArray info = new JSONArray();
            info.add(g.getId());
            info.add("<a href=\"" + Settings.getHost() + "WAVe/gene/" + g.getHGNC() + "\" class=\"genelink\">" + g.getHGNC() + "</a>");
            info.add(g.getNumberOfLsdbs());
            info.add(g.getNumberOfVariants());
            geneList.add(info);
        }
        stream.put("aaData", geneList);
        return new StreamingResolution("text/javascript", stream.toString());
    }

    /**
     * Handles call for Browsing Atom API.
     *
     * @return
     */
    public Resolution atom() {
        if (query.equals("*")) {
            return new StreamingResolution("text/xml", API.getGenes(genelist.getAll(), "atom_1.0"));
        }
        return new StreamingResolution("text/xml", API.getGenes(genelist.searchGene(query, false), "atom_1.0"));
    }

    /**
     * Handles call for Browsing JSON API.
     *
     * @return
     */
    public Resolution json() {
        if (query.equals("*")) {
            return new StreamingResolution("application/json", API.getGenes(genelist.getAll(), "json"));
        }
        return new StreamingResolution("application/json", API.getGenes(genelist.searchGene(query, false), "json"));
    }
}
