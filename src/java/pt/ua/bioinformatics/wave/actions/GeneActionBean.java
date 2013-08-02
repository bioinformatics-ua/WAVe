package pt.ua.bioinformatics.wave.actions;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.StreamingResolution;
import net.sourceforge.stripes.action.UrlBinding;
import pt.ua.bioinformatics.wave.domain.Gene;
import pt.ua.bioinformatics.wave.services.GeneList;
import pt.ua.bioinformatics.wave.domain.Type;
import pt.ua.bioinformatics.wave.services.API;

/**
 *
 * @author pedrolopes
 */
@UrlBinding("/gene/{hgnc}/{query}/{$event}")
public class GeneActionBean implements ActionBean {

    private ActionBeanContext context;
    private ArrayList<Gene> genes = new ArrayList<Gene>();
    private Gene gene = null;
    private String hgnc;
    private GeneList genelist = GeneList.INSTANCE;
    private int numberOfGenes = 0;
    private int numberOfResults = 0;
    private ArrayList<Type> datatypes = new ArrayList<Type>();
    private String query;
    private String addressUA = "null";
    private ArrayList<Gene> mesh = new ArrayList<Gene>();
    private boolean single = true;
    private String type = "gene";
    private String id = "";
    private boolean previous = false;

    public boolean isPrevious() {
        return previous;
    }

    public void setPrevious(boolean previous) {
        this.previous = previous;
    }

    public int getNumberOfResults() {
        return numberOfResults;
    }

    public void setNumberOfResults(int numberOfResults) {
        this.numberOfResults = numberOfResults;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isSingle() {
        return single;
    }

    public void setSingle(boolean single) {
        this.single = single;
    }

    public ArrayList<Gene> getMesh() {
        return mesh;
    }

    public void setMesh(ArrayList<Gene> mesh) {
        this.mesh = mesh;
    }

    public String getAddressUA() {
        return addressUA;
    }

    public void setAddressUA(String addressUA) {
        this.addressUA = addressUA;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public int getNumberOfGenes() {
        return numberOfGenes;
    }

    public ArrayList<Type> getDatatypes() {
        return datatypes;
    }

    public void setDatatypes(ArrayList<Type> datatypes) {
        this.datatypes = datatypes;
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

    public Gene getGene() {
        return gene;
    }

    public void setGene(Gene gene) {
        this.gene = gene;
    }

    public String getHgnc() {
        return hgnc;
    }

    public void setHgnc(String hgnc) {
        this.hgnc = hgnc;
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
     *
     * @return
     */
    @DefaultHandler
    public Resolution action() {
        if (!API.isLoaded()) {
            API.load();
        }
        hgnc = API.sanitize(hgnc);
        if (hgnc == null || hgnc.equals("")) {
            return new ForwardResolution("/index.jsp");
        }
        if (hgnc.equals("*")) {
            return browse();
        }
        if (query == null || query.equals("")) {
            return view();
        } else {
            if (query.equals("atom")) {
                return atom();
            }
            if (query.equals("json")) {
                return json();
            }
            if (!query.contains(":")) {
                return view();
            } else {
                return get();
            }
        }
    }

    /**
     * Handles simple gene views.
     *
     * @return
     */
    public Resolution view() {
        hgnc = API.sanitize(hgnc);
        try {
            single = (Boolean) context.getRequest().getSession().getAttribute("single");
            mesh = (ArrayList<Gene>) context.getRequest().getSession().getAttribute("mesh");
            type = (String) context.getRequest().getSession().getAttribute("type");
            id = (String) context.getRequest().getSession().getAttribute("value");
            previous = (Boolean) context.getRequest().getSession().getAttribute("previous");
        } catch (Exception ex) {
        }
        try {
            genes = genelist.searchGene(hgnc, false);
            if (genes.size() == 1) {
                gene = genes.get(0);
                numberOfResults = 1;
                numberOfGenes = 1;
                datatypes = gene.loadInfo();
                if (!mesh.contains(gene)) {
                    mesh.clear();
                    type = "gene";
                    single = true;
                    id = hgnc;
                    mesh.add(gene);
                    previous = false;
                }
                if (!previous) {
                    context.getRequest().getSession().setAttribute("type", type);
                    context.getRequest().getSession().setAttribute("mesh", genes);
                    context.getRequest().getSession().setAttribute("value", id);
                }
                return new ForwardResolution("/view.jsp");
            } else {
                numberOfGenes = genes.size();
                single = false;
                id = hgnc;
                type = "empty";
                mesh = genes;
                if (!previous) {
                    context.getRequest().getSession().setAttribute("type", type);
                    context.getRequest().getSession().setAttribute("mesh", genes);
                    context.getRequest().getSession().setAttribute("single", single);
                    context.getRequest().getSession().setAttribute("value", id);
                }
                numberOfResults = genes.size();
                return new ForwardResolution("/browse.jsp");
            }
        } catch (Exception e) {
            System.out.println("[GeneActionBean][view] Unable to load " + hgnc + " info\n\t" + e.toString() + "\n");
            return browse();
        }
    }

    /**
     * Handles gene searches (query for "*").
     *
     * @return
     */
    public Resolution browse() {
        numberOfResults = 30092;
        numberOfGenes = 30092;
        return new ForwardResolution("/browse.jsp");
    }

    /**
     * Handles call for UniversalAccess API.
     *
     * @return
     */
    public Resolution get() {
        hgnc = API.sanitize(hgnc);
        try {
            gene = genelist.getGene(hgnc.toUpperCase());
            datatypes = gene.loadInfo();
            numberOfGenes = 1;
            context.getRequest().getSession().setAttribute("type", type);
            context.getRequest().getSession().setAttribute("mesh", genes);
            context.getRequest().getSession().setAttribute("single", single);
            context.getRequest().getSession().setAttribute("value", id);
            addressUA = API.getUA(query, gene.getId());
        } catch (Exception e) {
            System.out.println("[GeneActionBean][get] Unable to register API and load " + hgnc + " info\n\t" + e.toString() + "\n");
            Logger.getLogger(API.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            return new ForwardResolution("/view.jsp");
        }
    }

    /**
     * Handles call for Gene Navigation Tree Atom API.
     *
     * @return
     */
    public Resolution atom() {
        boolean exists = false;
        if (!API.isLoaded()) {
            API.load();
        }

        try {
            System.out.println("[WAVe][Gene] loaded gene feed for " + hgnc + " from Redis cache");
            return new StreamingResolution("text/xml", API.getJedis().hget("wave:gene:" + hgnc, "feed"));
        } catch (Exception ex) {
            try {
                gene = genelist.getGene(hgnc.toUpperCase());
                exists = true;
            } catch (Exception e) {
                exists = false;
                System.out.println("[GeneActionBean] Unable to find gene " + hgnc + "\n\t" + e.toString());
            }

            if (exists) {
                return new StreamingResolution("text/xml", API.getGeneTree(gene, "atom_1.0"));
            }
            throw new UnsupportedOperationException();
        }
    }

    /**
     * Handles call for Gene Navigation Tree JSON API.
     *
     * @return
     */
    public Resolution json() {
        boolean exists = false;
        try {
            gene = genelist.getGene(hgnc.toUpperCase());
            exists = true;
        } catch (Exception e) {
            exists = false;
            System.out.println("[GeneActionBean] Unable to find gene " + hgnc + "\n\t" + e.toString());
        }

        if (exists) {
            return new StreamingResolution("text/javascript", API.getGeneTree(gene, "json"));
        }
        throw new UnsupportedOperationException();
    }
}
