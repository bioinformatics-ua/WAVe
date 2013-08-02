package pt.ua.bioinformatics.wave.actions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.StreamingResolution;
import net.sourceforge.stripes.action.UrlBinding;
import pt.ua.bioinformatics.wave.domain.Gene;
import pt.ua.bioinformatics.wave.domain.Item;
import pt.ua.bioinformatics.wave.domain.Type;
import pt.ua.bioinformatics.wave.services.GeneList;
import pt.ua.bioinformatics.wave.services.API;
import pt.ua.bioinformatics.wave.services.GeneCompare;
import pt.ua.bioinformatics.wave.services.Index;

/**
 *
 * @author pedrolopes
 */
@UrlBinding("/pharma/{id}/{$event}")
public class PharmaActionBean implements ActionBean {

    private String query = "";
    private ActionBeanContext context;
    private ArrayList<Gene> genes = new ArrayList<Gene>();
    private ArrayList<Item> items;
    private Gene gene = null;
    private String id;
    private GeneList genelist = GeneList.INSTANCE;
    private int numberOfResults = 0;
    private int numberOfGenes = 0;
    private String hgnc = "";
    private ArrayList<Type> datatypes = new ArrayList<Type>();
    private boolean single = false;
    private String addressUA = "null";
    private String type = "pharma";
    private boolean previous = true;

    public boolean isPrevious() {
        return previous;
    }

    public void setPrevious(boolean previous) {
        this.previous = previous;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAddressUA() {
        return addressUA;
    }

    public void setAddressUA(String addressUA) {
        this.addressUA = addressUA;
    }

    public boolean isSingle() {
        return single;
    }

    public void setSingle(boolean single) {
        this.single = single;
    }

    public ArrayList<Type> getDatatypes() {
        return datatypes;
    }

    public void setDatatypes(ArrayList<Type> datatypes) {
        this.datatypes = datatypes;
    }

    public int getNumberOfGenes() {
        return numberOfGenes;
    }

    public void setNumberOfGenes(int numberOfGenes) {
        this.numberOfGenes = numberOfGenes;
    }

    public String getHgnc() {
        return hgnc;
    }

    public void setHgnc(String hgnc) {
        this.hgnc = hgnc;
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public void setItems(ArrayList<Item> items) {
        this.items = items;
    }

    public int getNumberOfResults() {
        return numberOfResults;
    }

    public void setNumberOfResults(int numberOfGenes) {
        this.numberOfResults = numberOfGenes;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public GeneList getGenelist() {
        return genelist;
    }

    public void setGenelist(GeneList genelist) {
        this.genelist = genelist;
    }

    public Gene getGene() {
        return gene;
    }

    public void setGene(Gene gene) {
        this.gene = gene;
    }

    public String getId() {
        return id;
    }

    public void setId(String hgnc) {
        this.id = hgnc;
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
     * Main resolution handler.
     * @return
     */
    @DefaultHandler
    public Resolution action() {
        if (!API.isLoaded()) {
            API.load();
        }
        id = API.sanitize(id);
        query = id;
        hgnc = id;
        if (id == null || id.equals("")) {
            return new ForwardResolution("/index.jsp");
        }
        if (id.equals("*")) {
            return browse();
        } else {
            return get();
        }
    }

    /**
     * Handles gene searches (query for "*").
     * @return
     */
    public Resolution browse() {
        numberOfResults = 30092;
        numberOfGenes = 30092;
        return new ForwardResolution("/browse.jsp");
    }

    /**
     * Handles searches
     * @return
     */
    public Resolution get() {
        id = API.sanitize(id);
        try {
            query = id;
            hgnc = id;
            items = Index.get(id);
            if (items.size() == 1) {
                genes.add(items.get(0).getG());
                gene = items.get(0).getG();
                numberOfResults = 1;
                hgnc = gene.getHGNC();
                numberOfGenes = 1;
                single = true;
                context.getRequest().getSession().setAttribute("type", type);
                context.getRequest().getSession().setAttribute("mesh", genes);
                context.getRequest().getSession().setAttribute("single", single);
                context.getRequest().getSession().setAttribute("value", id);
                context.getRequest().getSession().setAttribute("previous", previous);
            } else {
                for (Item i : items) {
                    if (!genes.contains(i.getG())) {
                        genes.add(i.getG());
                    }
                }
                Collections.sort(genes, new GeneCompare());
                context.getRequest().getSession().setAttribute("type", type);
                context.getRequest().getSession().setAttribute("mesh", genes);
                context.getRequest().getSession().setAttribute("single", single);
                context.getRequest().getSession().setAttribute("value", id);
                context.getRequest().getSession().setAttribute("previous", previous);
                numberOfResults = items.size();
            }
        } catch (Exception e) {
            System.out.println("[GeneActionBean][get] Unable to register API and load " + id + " info\n\t" + e.toString() + "\n");
            Logger.getLogger(PharmaActionBean.class.getName()).log(Level.SEVERE, null, e);
        }
        if (single) {
            return new RedirectResolution("/gene/" + hgnc);
        } else {
            return new ForwardResolution("/search.jsp");
        }
    }

    /**
     * Handles call for Gene Navigation Tree Atom API.
     * @return
     */
    public Resolution atom() {
        try {
            items = Index.get(id);
            for (Item i : items) {
                if (!genes.contains(i.getG())) {
                    genes.add(i.getG());
                }
            }
            Collections.sort(genes, new GeneCompare());
        } catch (Exception e) {
            System.out.println("[GeneActionBean] Unable to find gene " + id + "\n\t" + e.toString());
        }
        return new StreamingResolution("text/xml", API.getGenes(genes, "atom_1.0"));
    }

    /**
     * Handles call for Gene Navigation Tree JSON API.
     * @return
     */
    public Resolution json() {
        try {
            items = Index.get(id);
            for (Item i : items) {
                if (!genes.contains(i.getG())) {
                    genes.add(i.getG());
                }
            }
            Collections.sort(genes, new GeneCompare());
        } catch (Exception e) {
            System.out.println("[GeneActionBean] Unable to find gene " + id + "\n\t" + e.toString());
        }
        return new StreamingResolution("application/json", API.getGenes(genes, "json"));
    }
}
